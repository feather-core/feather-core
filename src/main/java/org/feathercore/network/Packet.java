package org.feathercore.network;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public abstract class Packet implements Cloneable {

    public abstract int getId();

    public abstract void write(Buffer buffer);

    public abstract void read(Buffer buffer);

    @Override
    public Packet clone() {
        try {
            return (Packet) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}