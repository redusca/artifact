package resource.artifact.domains;

import resource.artifact.utils.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FriendRequest extends Entity<Tuple<Long,Long>> {
    private Boolean status;
    private final LocalDateTime fDate;

    public FriendRequest(Long elem1, Long elem2,LocalDateTime fDate) {
        setId(new Tuple<>(elem1, elem2));
        this.fDate = fDate;
        this.status = false;
    }

    public Long getSender(){
        return getId().first();
    }

    public Long getReceiver(){
        return getId().last();
    }

    public String getFDate() {
        return fDate.format(DateTimeFormat.DATE_TIME_FORMATTER);
    }

    public Boolean getStatus() {
        return status;
    }

    public void reverseStatus() {
        status = !status;
    }

    @Override
    public String toString() {
        return getSender().toString() + " wants to be friend with " + getReceiver().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return getSender().equals(that.getSender()) && getReceiver().equals(that.getReceiver());
    }
}
