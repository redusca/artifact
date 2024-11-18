package resource.artifact.utils.events;

import resource.artifact.domains.Account;
import resource.artifact.domains.User;

public class AccountEvent implements Event{
    private final ChangeEvent event;

    private Account oldAcc;
    private Account acc;
    private User oldUser;
    private User user;

    public AccountEvent(ChangeEvent event, Account user) {
        this.event = event;
        this.acc = user;
    }

    public AccountEvent(ChangeEvent event, Account user, Account oldUser){
        this.event = event;
        this.acc = user;
        this.oldAcc = oldUser;
    }

    public AccountEvent(ChangeEvent type, User user){
        this.event = type;
        this.user = user;
    }

    public AccountEvent(ChangeEvent type, User user, User oldUser){
        this.event = type;
        this.user = user;
        this.oldUser=oldUser;
    }

    public ChangeEvent getEvent(){return event;}

    public Account getACC(){return acc;}

    public Account getOldACC(){return oldAcc;}

    public User getData(){return user;}

    public User getOldData(){return oldUser;}

}
