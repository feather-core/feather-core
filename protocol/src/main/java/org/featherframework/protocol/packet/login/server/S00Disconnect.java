/*
 * Copyright 2019 Feather Core
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.feathercore.network.packet.login.server;

import org.featherframework.protocol.Buffer;
import org.featherframework.protocol.Packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class S00Disconnect extends Packet {

    /* TODO
    private ChatComponent reason;

    public S00Disconnect(ChatComponent reason) {
        this.reason = reason;
    }

    public S00Disconnect() {
    }

    public ChatComponent getReason() {
        return this.reason;
    }
    */

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void write(Buffer buffer) {
        if (true) throw new UnsupportedOperationException("Should be recreated using Mojang API");
        // TODO buffer.writeChatComponent(this.reason);
    }

    @Override
    public void read(Buffer buffer) {
        if (true) throw new UnsupportedOperationException("Should be recreated using Mojang API");
        // TODO this.reason = buffer.readChatComponent();
    }

}
