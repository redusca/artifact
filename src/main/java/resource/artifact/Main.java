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
        MainApplication.main(args);
    }
}
