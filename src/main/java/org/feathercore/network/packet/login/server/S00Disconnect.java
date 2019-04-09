package org.feathercore.network.packet.login.server;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;
import org.feathercore.util.chat.component.ChatComponent;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class S00Disconnect extends Packet {

    private ChatComponent reason;

    public S00Disconnect(ChatComponent reason) {
        this.reason = reason;
    }

    public S00Disconnect() {
    }

    public ChatComponent getReason() {
        return this.reason;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeChatComponent(this.reason);
    }

    @Override
    public void read(Buffer buffer) {
        this.reason = buffer.readChatComponent();
    }

}
