package resource.artifact;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.User;
import resource.artifact.domains.validator.FriendshipValidator;
import resource.artifact.domains.validator.UserValidator;
import resource.artifact.repositories.database.FriendshipDBRepository;
import resource.artifact.repositories.database.UserDBRepository;

public class Main {
    public static void main(String[] args) {
        DataBaseConnectInfo infoConnect =new DataBaseConnectInfo("postgres","1231","lab6tema");

       UserDBRepository userDBRepository = new UserDBRepository(
             new UserValidator(),infoConnect,
               "users"
       );
        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(
          userDBRepository, new FriendshipValidator(userDBRepository),
          infoConnect , "friendships"
        );

        userDBRepository.findAll().forEach(System.out::println);
        friendshipDBRepository.findAll().forEach(System.out::println);

        userDBRepository.save(new User("marius","esca"));

        userDBRepository.findAll().forEach(user -> System.out.println(user.getId()));
        //MainApplication.main(args);
    }
}
