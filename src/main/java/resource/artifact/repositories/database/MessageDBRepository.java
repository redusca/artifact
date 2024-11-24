package resource.artifact.repositories.database;

import resource.artifact.domains.*;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.Repository;
import resource.artifact.utils.DateTimeFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class MessageDBRepository extends AbstractDataBaseRepository<Long, Message>{
    private final Repository<Long, User> userRepo;
    private static Long nextId =1L;
    public MessageDBRepository(Validator<Message> validator, DataBaseConnectInfo connectInfo, String tableName, Repository<Long, User> userRepo) {
        super(validator, connectInfo, tableName);
        this.userRepo = userRepo;

        ReadDataBaseEntries();
        nextId = entities.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
    }

    @Override
    public Message createEntity(ResultSet resultSet) {
        try {
            LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"), DateTimeFormat.DATE_TIME_FORMATTER_SECONDS);

            Message msg =  new Message(userRepo.findOne(resultSet.getLong("id_sender")).orElse(new User()),
                    userRepo.findOne(resultSet.getLong("id_receiver")).orElse(new User()),
                    resultSet.getString("message"),date);
            msg.setId(resultSet.getLong("id"));
            return msg;
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String addFormat(Message entity) {
        final String[] returnString = {String.format("Insert Into %s (id,id_sender,id_receiver,message,date) Values ", getTableName())};
        entity.getTo().forEach(
                user -> returnString[0] += String.format("(%d,%d,%d,'%s','%s'), ",
                        entity.getId(),entity.getFrom().getId(),user.getId(),entity.getMessage(),entity.getDate())
        );

        return returnString[0].substring(0, returnString[0].length()-2);
    }

    @Override
    public String delFormat(Message entity) {
        return String.format("Delete From %s Where id = %d",getTableName(),entity.getId());
    }

    @Override
    public String updateFormat(Message entity) {
        return "";
    }

    @Override
    public Optional<Message> save(Message entity) {

        if(entities.get(entity.getId()) != null) {
            entities.get(entity.getId()).getTo().addAll(entity.getTo());
            return Optional.ofNullable(entities.get(entity.getId()));
        }
        nextId = entities.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        entity.setId(nextId);
        return super.save(entity);
    }
}
