package resource.artifact.domains;

import resource.artifact.utils.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long>{
    private User from;
    private List<User> to = new ArrayList<>();
    private String message;
    private LocalDateTime date;

    public Message(User from, User to, String message, LocalDateTime date) {
        this.from = from;
        this.to.add(to);
        this.message = message;
        this.date = date;
    }

    public Message(User from, List<User> to, String message, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public void AddTo(User to) {
        this.to.add(to);
    }

    public String getMessage() {
        return message;
    }

    public String getDate(){
        return date.format(DateTimeFormat.DATE_TIME_FORMATTER_SECONDS) ;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", to=" +
                to.toString() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(from,message);
    }

    public void setFrom(User user) {
        this.from = user;
    }


    public void setMessage(String msg) {
        this.message = msg;
    }

    public Message getThis() {
        return this;
    }
}
