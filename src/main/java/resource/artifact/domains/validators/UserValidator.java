package resource.artifact.domains.validators;

import resource.artifact.domains.User;

import java.util.Objects;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errorString = "ValidationExceptions:\n";
        int nr = errorString.length();

        if(entity.getId() == null)
           errorString = errorString + "    Id is required\n";

        if(entity.getUsername() == null || Objects.equals(entity.getUsername(), ""))
            errorString += "   Username cannot be empty!\n";

        if(entity.getPassword() == null || Objects.equals((entity.getUsername()),""))
            errorString += "   Password cannot be empty!\n";

        if(entity.getId() <= 0)
           errorString += "   Id must be greater than 0\n";

        if(entity.getFirstName() == null || entity.getFirstName().isEmpty())
           errorString += "    FirstName is required\n";

        if(entity.getLastName() == null || entity.getLastName().isEmpty())
           errorString += "    LastName is required\n";

        if(nr != errorString.length())
            throw new ValidationException(errorString);
    }
}
