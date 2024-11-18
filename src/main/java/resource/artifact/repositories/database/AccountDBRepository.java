package resource.artifact.repositories.database;

import resource.artifact.domains.Account;
import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.Friendship;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class AccountDBRepository extends AbstractDataBaseRepository<Long, Account> {
    private final Repository<Long, User> userRepository;

    public AccountDBRepository(Validator<Account> validator, DataBaseConnectInfo connectInfo, String tableName, Repository<Long, User> userRepository) {
        super(validator, connectInfo, tableName);
        this.userRepository = userRepository;
        ReadDataBaseEntries();
    }

    @Override
    public Account createEntity(ResultSet resultSet) {
        try {
            return new Account(resultSet.getString("username"), resultSet.getString("password"),
                    userRepository.findOne(resultSet.getLong("id_user")).orElse(new User()));
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String addFormat(Account entity) {
        return String.format("Insert into %s (username,password,id_user) Values ('%s','%s',%d)",
                getTableName(), entity.getUsername(), entity.getPassword(),entity.getId());
    }

    @Override
    public String delFormat(Account entity) {
        return String.format("Delete From %s Where id_user = %d", getTableName(),entity.getId());
    }

    @Override
    public String updateFormat(Account entity) {
        return String.format("Update %s Set username='%s', password='%s' Where id_user = %d", getTableName(),
                entity.getUsername(),entity.getUsername(),entity.getId());
    }

    @Override
    public Optional<Account> save(Account entity){
        findAll().forEach(acc ->{
            if(Objects.equals(acc.getUsername(), entity.getUsername()))
                throw new ValidationException("Username already taken!");
        });

        return super.save(entity);
    }

}
