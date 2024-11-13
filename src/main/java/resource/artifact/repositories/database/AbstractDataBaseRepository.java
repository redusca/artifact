package resource.artifact.repositories.database;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.Entity;
import resource.artifact.domains.Validator.Validator;
import resource.artifact.repositories.inMemory.InMemoryRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AbstractDataBaseRepository<Id,E extends Entity<Id>> extends InMemoryRepository<Id,E> {
    //connection parameters
    //only one connection per project, all objects will use this static value
    protected static Connection dataBaseConnection;
    protected String tableName; //operation will be made on this table
    int nr =2;

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
     *
     */
    private void ReadDataBaseEntries(){
        Statement statement;
        ResultSet resultSet=null;
        tableName = "default";
    }

    public AbstractDataBaseRepository(Validator<E> validator, DataBaseConnectInfo connectInfo) {
        super(validator);
        connect(connectInfo);
        ReadDataBaseEntries();
    }

}
