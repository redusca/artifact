package resource.artifact.utils.observers;

import resource.artifact.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}