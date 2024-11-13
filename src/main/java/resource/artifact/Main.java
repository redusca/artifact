package resource.artifact;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.User;
import resource.artifact.domains.Validator.UserValidator;
import resource.artifact.repositories.database.AbstractDataBaseRepository;

public class Main {
    public static void main(String[] args) {
        AbstractDataBaseRepository<Long, User> dbRepository = new AbstractDataBaseRepository<Long, User>(
                new UserValidator(),
                new DataBaseConnectInfo("postgres","1231","lab6tema")
        );
        //MainApplication.main(args);
    }
}
