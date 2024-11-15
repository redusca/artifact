package resource.artifact.utils;

import resource.artifact.domains.ConexComponents;
import resource.artifact.repositories.database.FriendshipDBRepository;
import resource.artifact.repositories.database.UserDBRepository;

import java.awt.*;

public class ComunityStructureUtils {

    public static Integer NumberOfCommunities(UserDBRepository userDBRepository){
        ConexComponents<Long> CC = new ConexComponents<Long>();

        userDBRepository.findAll().forEach(user -> {
            if(!user.getFriends().isEmpty())
                user.getFriends().forEach(friendId->CC.addToConexCompThatHave(friendId,user.getId()));
            else
                CC.createConex(user.getId());
        });

        CC.printComp();
        return CC.size();
    }

    public static String LongestChainOfFriendships(UserDBRepository userDBRepository){
       return null;
    }
}
