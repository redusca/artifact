package resource.artifact.domains;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message{
    Message message;

    public ReplyMessage(User from, List<User> to, String message, LocalDateTime date, Message objMessage) {
        super(from, to, message, date);
        this.message = objMessage;
    }

   public String replyMessage(){
        String msg = "Reply to: " + message.getMessage();
        if(msg.length() > 70)
            return msg.substring(0, 70) + "...";
        return msg + "\n" + getMessage();
   }
}
