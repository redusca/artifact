package resource.artifact.repositories.database;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.Entity;
import resource.artifact.domains.enums.RowCase;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.inMemory.InMemoryRepository;

import java.sql.*;
import java.util.Optional;

public abstract class AbstractDataBaseRepository<Id,E extends Entity<Id>> extends InMemoryRepository<Id,E> {
    //connection parameters
    //only one connection per project, all objects will use this static value
    protected static Connection dataBaseConnection;
    private final String tableName; //operation will be made on this table
    /**
     * Function with the role to make on single connection in the project threw out all instances of this class
     * @param connectInfo :- struct containing 3 String for connecting to the database
     */
    private void connect(DataBaseConnectInfo connectInfo) {
        try{
            if(dataBaseConnection == null || dataBaseConnection.isClosed()){
                Class.forName("org.postgresql.Driver");
                dataBaseConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
                        connectInfo.getDataBaseName(), connectInfo.getHost(),connectInfo.getPassword());
                if(dataBaseConnection != null){
                    System.out.println("Connection established!");
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     *Function converts all entries from the database into the MAP of the repository
     */
    private void ReadDataBaseEntries() {
        Statement statement;
        ResultSet resultSet;
        try {
            String query = String.format("SELECT * FROM " + tableName);
            statement = dataBaseConnection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                E entity = createEntity(resultSet);
                entities.put(entity.getId(), entity);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Function that Execute a line of SQL based on Row add,remove or update
     * @param entity that represents the future add/del/updated in database
     * @param rowCase an {@code rowCase}
     *                - ADD -> save ,
     *                - DEL -> remove,
     *                - UPDATE -> update.
     */
    private void RowStatement(E entity, RowCase rowCase) {
        Statement statement;
        try{
            String query = queryGenerator(entity,rowCase);
            statement = dataBaseConnection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * generate sql commands based on {@code rowCase}
     * @param entity that represents the future add/del/updated in database
     * @param rowCase an {@code rowCase}
     *                   - ADD -> save ,
     *                   - DEL -> remove,
     *                   - UPDATE -> update.
     * @return String of ideal statement
     */
    private String queryGenerator(E entity, RowCase rowCase) {
        switch (rowCase) {
            case ADD -> {
                return addFormat(entity);
            }
            case DELETE -> {
                return delFormat(entity);
            }
            case UPDATE -> {
                return updateFormat(entity);
            }
            default -> throw new RuntimeException("Invalid row case");
        }
    }

    /**
     * Take the result from the select statement and create an entity
     * @param resultSet - one of the rows in result
     * @return The entity
     */
    public abstract E createEntity(ResultSet resultSet);

    /**
     * Transform the Entity into a string format for insertion in database table
     * @param entity the entity we want to transform
     * @return string for a statement
     */
    public abstract String addFormat(E entity);

    public abstract String delFormat(E entity);

    public abstract String updateFormat(E entity);

    public AbstractDataBaseRepository(Validator<E> validator, DataBaseConnectInfo connectInfo, String tableName) {
        super(validator);
        connect(connectInfo);
        this.tableName = tableName;
        ReadDataBaseEntries();
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public Optional<E> save(E entity) {

        Optional<E> returnObj = super.save(entity);
        if(returnObj.isEmpty())
            RowStatement(entity,RowCase.ADD);

        return returnObj;
    }

    @Override
    public Optional<E> delete(Id id) {
        Optional<E> returnObj = super.delete(id);
        returnObj.ifPresent(value -> RowStatement(value, RowCase.DELETE));
        return returnObj;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> oldValue = findOne(entity.getId());
        Optional<E> returnObj = super.update(entity);
        if(returnObj.isPresent() && oldValue.isPresent() && returnObj.get() == oldValue.get())
            RowStatement(entity,RowCase.UPDATE);
        return returnObj;
    }
}
