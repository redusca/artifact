package resource.artifact.domains;

import java.util.Objects;

public class Tuple<E1,E2> {
    private final E1 firstElem;
    private final E2 lastElem;

    public Tuple(E1 firstElem, E2 lastElem) {
        this.firstElem = firstElem;
        this.lastElem = lastElem;
    }

    public E1 first(){
        return firstElem;
    }
    public E2 last(){
        return lastElem;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj == null || obj.getClass()!= this.getClass())
            return false;
        @SuppressWarnings("unchecked") Tuple<E1,E2> e = (Tuple<E1, E2>) obj;

        return firstElem.equals(e.first()) && lastElem.equals(e.last()) || firstElem.equals(e.last()) && lastElem.equals(e.first());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first(), last());
    }
}
