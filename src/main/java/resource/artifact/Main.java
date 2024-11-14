package resource.artifact;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.User;
import resource.artifact.domains.validator.UserValidator;
import resource.artifact.repositories.database.AbstractDataBaseRepository;
import resource.artifact.repositories.database.UserDBRepository;

public class Main {
    public static void main(String[] args) {
       UserDBRepository dbRepository = new UserDBRepository(
             new UserValidator(),
               new DataBaseConnectInfo("postgres","1231","lab6tema"),
               "utilizatori"
       );

       dbRepository.findAll().forEach(System.out::println);

       User user = new User("andrei","mihai");
       user.setId(19L);
       dbRepository.update(user);

       dbRepository.findAll().forEach(System.out::println);
        //MainApplication.main(args);
    }
}
