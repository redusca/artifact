package resource.artifact.repositories.database;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.Repository;
import resource.artifact.utils.DateTimeFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class FriendshipDBRepository extends AbstractDataBaseRepository<Tuple<Long,Long>, Friendship>{
    private final Repository<Long, User> usersRepo;

    public FriendshipDBRepository(Repository<Long, User> usersRepo,Validator<Friendship> validator,
                                  DataBaseConnectInfo connectInfo, String tableName) {
        super(validator, connectInfo, tableName);
        this.usersRepo=usersRepo;
        ReadDataBaseEntries();
        postLoad();
    }

    private void postLoad() {
        findAll().forEach(this::addBothFriends);
    }

    private void addBothFriends(Friendship entity) {
        usersRepo.findOne(entity.getFirst()).orElse(new User()).addFriend(
                usersRepo.findOne(entity.getLast()).orElse(new User()).getId());
        usersRepo.findOne(entity.getLast()).orElse(new User()).addFriend(
                usersRepo.findOne(entity.getFirst()).orElse(new User()).getId());
    }

    @Override
    public Friendship createEntity(ResultSet resultSet) {
        try {
            LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"), DateTimeFormat.DATE_TIME_FORMATTER);
            return new Friendship(resultSet.getLong("id_low"), resultSet.getLong("id_high"),date);
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String addFormat(Friendship entity) {
        return String.format("Insert Into %s (id_low,id_high,date) Values (%d,%d,'%s')",getTableName(),entity.getFirst(),entity.getLast(),entity.getFDate());
    }

    @Override
    public String delFormat(Friendship entity) {
        return String.format("Delete From %s Where id_low = %d and id_high = %d",getTableName(),entity.getFirst(),entity.getLast());
    }

    //never called
    @Override
    public String updateFormat(Friendship entity) {
        return "";
    }

    @Override
    public Optional<Friendship> save(Friendship entity){
        addBothFriends(entity);
        return super.save(entity);
    }
}
