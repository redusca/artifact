package resource.artifact;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.validators.FriendshipValidator;
import resource.artifact.domains.validators.UserValidator;
import resource.artifact.repositories.database.FriendshipDBRepository;
import resource.artifact.repositories.database.UserDBRepository;
import resource.artifact.repositories.inFile.FriendshipIFRepository;
import resource.artifact.repositories.inFile.UserIFRepository;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.CommunityStructureUtils;

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
        //Service
        SocialNetworking SN = new SocialNetworking(userDBRepository,friendshipDBRepository);

        //System.out.println(CommunityStructureUtils.NumberOfCommunities(userDBRepository));

        UserIFRepository userIFRepository = new UserIFRepository(
                new UserValidator(),"C:\\Users\\redis\\Desktop\\Faculta\\sem3\\MAP\\TemaLab\\data\\user.txt");
        FriendshipIFRepository friendshipIFRepository = new FriendshipIFRepository(
                new FriendshipValidator(userIFRepository),userIFRepository,"C:\\Users\\redis\\Desktop\\Faculta\\sem3\\MAP\\TemaLab\\data\\friendships.txt");

        SocialNetworking SN_file = new SocialNetworking(userIFRepository,friendshipIFRepository);
        SN_file.get_all_users().forEach(System.out::println);
        SN_file.get_all_friendships().forEach(System.out::println);

        //MainApplication.main(args);
    }
}
