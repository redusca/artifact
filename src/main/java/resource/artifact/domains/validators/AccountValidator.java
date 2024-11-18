package resource.artifact.domains.validators;

import resource.artifact.domains.Account;
import resource.artifact.domains.User;
import resource.artifact.repositories.Repository;
import resource.artifact.repositories.inMemory.InMemoryRepository;

import java.util.Objects;

public class AccountValidator implements Validator<Account> {
    private final Repository<Long,User> repository;
    public AccountValidator(InMemoryRepository<Long, User> repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Account Entity) throws ValidationException {
        String errorString = "ValidationExceptions:\n";
        int nr = errorString.length();

        if( Entity.getUsername() == null || Objects.equals(Entity.getUsername(), ""))
            errorString += "   Username cannot be empty!\n";

        if( Entity.getPassword() == null || Objects.equals((Entity.getUsername()),""))
            errorString += "   Password cannot be empty!\n";

        if( Entity.getUser() == null || repository.findOne(Entity.getUser().getId()).isEmpty())
            errorString += "   The account doens't have linked a user!\n";

        if(nr != errorString.length())
            throw new ValidationException(errorString);
    }
}
