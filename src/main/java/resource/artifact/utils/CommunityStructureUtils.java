package resource.artifact.utils;

import resource.artifact.domains.ConexComponents;
import resource.artifact.domains.User;
import resource.artifact.repositories.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityStructureUtils {

    public static Integer NumberOfCommunities(Repository<Long,User> userRepository){
        ConexComponents<Long> CC = new ConexComponents<>();

        userRepository.findAll().forEach(user -> {
            if(!user.getFriends().isEmpty())
                user.getFriends().forEach(friendId->CC.addToConexCompThatHave(friendId,user.getId()));
            else
                CC.createConex(user.getId());
        });

        CC.printComp();
        return CC.size();
    }

    public static List<Long> mostSocialCommunity( Repository<Long,User> userRepository) {
        return null;
    }
}
