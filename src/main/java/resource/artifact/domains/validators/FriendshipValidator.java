package resource.artifact.domains.validators;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.User;
import resource.artifact.repositories.Repository;
import resource.artifact.repositories.inMemory.InMemoryRepository;

public class FriendshipValidator implements Validator<Friendship> {

    private final Repository<Long, User> repository;

    public FriendshipValidator(Repository<Long, User> repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorString = "ValidationExceptions:\n";
        int nr = errorString.length();

        if(entity.getFirst() == null)
            errorString += "    First Id is required \n";
        if(entity.getLast() == null)
            errorString += "    Last Id is required \n";

        if(entity.getFirst() <= 0)
            errorString += "    First Id must be greater than 0 \n";
        if(entity.getLast() <= 0)
            errorString += "    Last Id must be greater than 0 \n";

        if(repository.findOne(entity.getFirst()).isEmpty())
            errorString += "    First Id is not found \n";
        if(repository.findOne(entity.getLast()).isEmpty())
            errorString += "    Last Id is not found \n";

        if(nr != errorString.length())
            throw new ValidationException(errorString);
    }
}
