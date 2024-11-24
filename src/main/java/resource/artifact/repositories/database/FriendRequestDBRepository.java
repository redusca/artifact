package resource.artifact.repositories.database;

import resource.artifact.domains.*;
import resource.artifact.domains.validators.Validator;
import resource.artifact.utils.DateTimeFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FriendRequestDBRepository extends AbstractDataBaseRepository<Tuple<Long,Long>, FriendRequest> {

    public FriendRequestDBRepository(Validator<FriendRequest> validator,
                                     DataBaseConnectInfo connectInfo, String tableName) {
        super(validator, connectInfo, tableName);
        ReadDataBaseEntries();
    }

    @Override
    public FriendRequest createEntity(ResultSet resultSet) {
        try {
            LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"), DateTimeFormat.DATE_TIME_FORMATTER);
            return new FriendRequest(resultSet.getLong("id_sender"), resultSet.getLong("id_receiver"),date,resultSet.getBoolean("status"));
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String addFormat(FriendRequest entity) {
        return String.format("Insert Into %s (id_sender,id_receiver,date,status) Values (%d,%d,'%s',%b)",
                getTableName(),entity.getSender(),entity.getReceiver(),entity.getFDate(),entity.getStatus());
    }

    @Override
    public String delFormat(FriendRequest entity) {
        return String.format("Delete From %s Where id_sender = %d and id_receiver = %d",
                getTableName(),entity.getSender(),entity.getReceiver());
    }

    @Override
    public String updateFormat(FriendRequest entity) {
        return String.format("Update %s Set status = %b Where id_sender = %d and id_receiver = %d",
                getTableName(),entity.getStatus(),entity.getSender(),entity.getReceiver());
    }

}
