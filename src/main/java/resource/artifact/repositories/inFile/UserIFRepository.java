package resource.artifact.repositories.inFile;

import resource.artifact.domains.User;
import resource.artifact.domains.validators.IDfromStringValidator;
import resource.artifact.domains.validators.Validator;

import java.util.Optional;

public class UserIFRepository extends AbstractInFileRepository<Long, User>{
    private static Long nextUserId = 1L;

    public UserIFRepository(Validator<User> validator, String filename) {
        super(validator, filename);
        entities.keySet().forEach(key->{
            if(key>=nextUserId)
                nextUserId=key+1;
        });
    }

    @Override
    public User createEntity(String line) {
        String[] userValues = line.split(";");
        if(userValues.length != 5)
            throw new IllegalArgumentException("The format is invalid for User!");
        User inFileUser = new User(userValues[1], userValues[2],userValues[3],userValues[4]);

        IDfromStringValidator IDValidator= new IDfromStringValidator();
        IDValidator.validate(userValues[0]);

        inFileUser.setId(Long.parseLong(userValues[0]));
        return inFileUser;
    }

    @Override
    public String saveEntity(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() +
                ";" + entity.getPassword() + ";" +entity.getUsername();
    }

    @Override
    public Optional<User> save(User entity){
        entity.setId(nextUserId++);
        return super.save(entity);
    }
}
