package resource.artifact.domains;

import resource.artifact.utils.DateTimeFormat;

import java.time.LocalDateTime;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Class Friendship will always have the fields first and last sorted ascended by value
 */
public class Friendship extends Entity<Tuple<Long,Long>> {

    private final LocalDateTime fDate;


    public Friendship(Long elem1, Long elem2) {
        setId(new Tuple<>(min(elem1, elem2),max(elem1, elem2)));

        this.fDate = LocalDateTime.now();
    }
    public Friendship(Long elem1, Long elem2,LocalDateTime date) {
        setId(new Tuple<>(min(elem1, elem2),max(elem1, elem2)));

        this.fDate = date;
    }

    public String getFDate() {
        return fDate.format(DateTimeFormat.DATE_TIME_FORMATTER) ;
    }

    public Long getFirst(){
        return getId().first();
    }

    public Long getLast(){
        return getId().last();
    }

    @Override
    public String toString() {
        return getFirst().toString() + " is friend with " + getLast().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return getFirst().equals(that.getFirst()) && getLast().equals(that.getLast());
    }

}