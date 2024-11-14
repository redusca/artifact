package resource.artifact.domains.validator;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.User;
import resource.artifact.repositories.inMemory.InMemoryRepository;

public class FriendshipValidator implements Validator<Friendship> {

    private final InMemoryRepository<Long, User> repository;

    public FriendshipValidator(InMemoryRepository<Long, User> repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorString = "ValidationExceptions:\n";
        int nr = errorString.length();

        if(entity.first() == null)
            errorString += "First Id is required \n";
        if(entity.last() == null)
            errorString += "Last Id is required \n";

        if(entity.first() <= 0)
            errorString += "First Id must be greater than 0 \n";
        if(entity.last() <= 0)
            errorString += "Last Id must be greater than 0 \n";

        if(repository.findOne(entity.first()).isEmpty())
            errorString += "First Id is not found \n";
        if(repository.findOne(entity.last()).isEmpty())
            errorString += "Last Id is not found \n";

        if(nr != errorString.length())
            throw new ValidationException(errorString);
    }
}
