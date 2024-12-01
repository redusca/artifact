package resource.artifact.domains.validators;

import resource.artifact.domains.FriendRequest;
import resource.artifact.domains.User;
import resource.artifact.repositories.Repository;

public class FriendRequestValidator implements Validator<FriendRequest> {

    private final Repository<Long, User> repository;

    public FriendRequestValidator(Repository<Long, User> repository) {
        this.repository = repository;
    }
    @Override
    public void validate(FriendRequest Entity) throws ValidationException {
        String errorString = "ValidationExceptions:\n";
        int nr = errorString.length();

        if(Entity.getSender() == null)
            errorString += "    Sender Id is required \n";
        if(Entity.getReceiver() == null)
            errorString += "    Receiver Id is required \n";

        if(Entity.getSender() <= 0)
            errorString += "    Sender Id must be greater than 0 \n";
        if(Entity.getReceiver() <= 0)
            errorString += "    Receiver Id must be greater than 0 \n";

        if(repository.findOne(Entity.getReceiver()).isEmpty())
            errorString += "    Receiver  not found \n";
        if(repository.findOne(Entity.getSender()).isEmpty())
            errorString += "    Sender  not found \n";

        if(nr != errorString.length())
            throw new ValidationException(errorString);
    }
}
