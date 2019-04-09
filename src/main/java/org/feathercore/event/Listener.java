package org.feathercore.event;

/**
 * Class, which implements IListener and also auto-registers itself in EventManager.
 *
 * Created by k.shandurenko on 09/04/2019
 */
public class Listener implements IListener {

    public Listener() {
        EventManager.register(this);
    }

}