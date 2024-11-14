package resource.artifact.repositories.database;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.domains.validator.Validator;
import resource.artifact.repositories.inMemory.InMemoryRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class FriendshipDBRepository extends AbstractDataBaseRepository<Tuple<Long,Long>, Friendship>{
    private final InMemoryRepository<Long, User> usersRepo;

    public FriendshipDBRepository(InMemoryRepository<Long, User> usersRepo,Validator<Friendship> validator,
                                  DataBaseConnectInfo connectInfo, String tableName) {
        super(validator, connectInfo, tableName);
        this.usersRepo=usersRepo;
        postLoad();
    }

    private void postLoad() {
        findAll().forEach(friendship -> {
            usersRepo.findOne(friendship.first()).get().addFriend(
                    usersRepo.findOne(friendship.last()).get().getId());
            usersRepo.findOne(friendship.last()).get().addFriend(
                    usersRepo.findOne(friendship.first()).get().getId());
        });
    }

    @Override
    public Friendship createEntity(ResultSet resultSet) {
        try {
            return new Friendship(resultSet.getLong("id_low"), resultSet.getLong("id_high"));
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String addFormat(Friendship entity) {
        return String.format("Insert Into %s (id_low,id_high) Values (%d,%d)",getTableName(),entity.first(),entity.last());
    }

    @Override
    public String delFormat(Friendship entity) {
        return String.format("Delete From %s Where id_low = %d and id_high = %d",getTableName(),entity.first(),entity.last());
    }

    @Override
    public String updateFormat(Friendship entity) {
        return "";
    }

    @Override
    public Optional<Friendship> save(Friendship entity){
        usersRepo.findOne(entity.first()).get().addFriend(
                usersRepo.findOne(entity.last()).get().getId());
        usersRepo.findOne(entity.last()).get().addFriend(
                usersRepo.findOne(entity.first()).get().getId());

        return super.save(entity);
    }
}
