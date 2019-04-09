package org.feathercore.event;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class Event {

    /**
     * Calls this event.
     * @see EventManager#call(Event) for more info.
     */
    public final Event call() {
        EventManager.call(this);
        return this;
    }

}
