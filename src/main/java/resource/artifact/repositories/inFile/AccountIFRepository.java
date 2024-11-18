package resource.artifact.repositories.inFile;

import resource.artifact.domains.Account;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.AccountValidator;
import resource.artifact.domains.validators.IDfromStringValidator;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.Repository;

import java.lang.annotation.Native;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Long.parseLong;

public class AccountIFRepository extends AbstractInFileRepository<Long, Account> {
    private final Repository<Long, User> userRepository;

    public AccountIFRepository(Validator<Account> validator, String filename, Repository<Long,User> userRepository) {
        super(validator, filename);
        this.userRepository = userRepository;
    }

    @Override
    public Account createEntity(String line) {
        String[] accValues = line.split(";");
        if(accValues.length != 3)
            throw new IllegalArgumentException("The format is invalid for Account!");

        IDfromStringValidator IDValidator= new IDfromStringValidator();
        IDValidator.validate(accValues[2]);

        return new Account(accValues[0],accValues[1],userRepository.findOne(parseLong(accValues[2])).orElse(new User()));
    }

    @Override
    public String saveEntity(Account entity) {
        return entity.getUsername()+";"+entity.getPassword()+";"+entity.getUser().getId();
    }

    @Override
    public Optional<Account> save(Account entity){
        findAll().forEach(acc ->{
            if(Objects.equals(acc.getUsername(), entity.getUsername()))
                throw new ValidationException("Username already taken!");
        });

        return super.save(entity);
    }

}
