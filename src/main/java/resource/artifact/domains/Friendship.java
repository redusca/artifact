package resource.artifact.domains;

import java.time.LocalDateTime;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Class Friendship will always have the fields first and last sorted ascended by value
 */
public class Friendship extends Entity<Tuple<Long,Long>> {
    private final LocalDateTime fDate;

    public Friendship(Long elem1, Long elem2) {

        setId(new Tuple<>(min(elem1, elem2), max(elem1, elem2)));
        this.fDate = LocalDateTime.now();
    }

    public LocalDateTime getFDate() {
        return fDate;
    }

    public Long first(){
        return getId().first();
    }

    public Long last(){
        return getId().last();
    }

    @Override
    public String toString() {
        return first().toString() + " is friend with " + last().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return first().equals(that.first()) && last().equals(that.last());
    }

}