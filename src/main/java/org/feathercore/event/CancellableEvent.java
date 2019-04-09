package org.feathercore.event;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public abstract class CancellableEvent extends Event {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean value) {
        this.cancelled = value;
    }

}