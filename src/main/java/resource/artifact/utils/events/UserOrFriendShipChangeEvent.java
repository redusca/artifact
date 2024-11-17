package resource.artifact.utils.events;

import resource.artifact.domains.User;

public class UserOrFriendShipChangeEvent implements Event{
    private final ChangeEvent event;
    private User oldUser;
    private final User user;

    public UserOrFriendShipChangeEvent(ChangeEvent type, User user){
        this.event = type;
        this.user = user;
    }

    public UserOrFriendShipChangeEvent(ChangeEvent type, User user, User oldUser){
        this.event = type;
        this.user = user;
        this.oldUser=oldUser;
    }

    public ChangeEvent getEvent(){return event;}

    public User getData(){return user;}

    public User getOldData(){return oldUser;}
}
