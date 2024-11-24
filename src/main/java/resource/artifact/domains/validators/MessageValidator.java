package resource.artifact.domains.validators;

import resource.artifact.domains.Message;
import resource.artifact.domains.User;
import resource.artifact.repositories.Repository;

public class MessageValidator implements Validator<Message> {

    private final Repository<Long, User> repo;

    public MessageValidator(Repository<Long, User> repository) {
        this.repo = repository;
    }
    @Override
    public void validate(Message entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null");
        if(entity.getFrom() == null)
            throw new IllegalArgumentException("from is null");
        if(entity.getTo() == null)
            throw new IllegalArgumentException("to is null");
        if(entity.getMessage() == null)
            throw new IllegalArgumentException("message is null");
        if(entity.getDate() == null)
            throw new IllegalArgumentException("date is null");

        if(repo.findOne(entity.getId()).isEmpty())
            throw new IllegalArgumentException("entity already exists");
        entity.getTo().forEach(user -> {
            if(user == null)
                throw new IllegalArgumentException("user is null");
            if(repo.findOne(user.getId()).isEmpty())
                throw new IllegalArgumentException("user does not exist");
        });
    }
}
