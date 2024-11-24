package resource.artifact.utils.events;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.Message;
import resource.artifact.domains.User;

public class AccountEvent implements Event{
    private final ChangeEvent event;
    private Message msg;
    private User oldUser;
    private User user;
    private Friendship friendship;

    public AccountEvent(ChangeEvent type, User user){
        this.event = type;
        this.user = user;
    }

    public AccountEvent(ChangeEvent type, User user, User oldUser){
        this.event = type;
        this.user = user;
        this.oldUser=oldUser;
    }

    public AccountEvent(ChangeEvent type,Friendship friendship){
        this.event = type;
        this.friendship = friendship;
    }

    public AccountEvent(ChangeEvent changeEvent, Message msg) {
        this.event = changeEvent;
        this.msg = msg;
    }

    public Message getMessage() {return msg;}

    public ChangeEvent getEvent(){return event;}

    public User getData(){return user;}

    public User getOldData(){return oldUser;}

    public Friendship getFriendship() { return friendship;}
}
