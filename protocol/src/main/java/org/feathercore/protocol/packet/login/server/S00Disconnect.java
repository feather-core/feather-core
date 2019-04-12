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

package org.feathercore.protocol.packet.login.server;

import net.md_5.bungee.api.chat.BaseComponent;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.packet.Packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class S00Disconnect implements Packet {

    public final static int ID = 0x00;

    private BaseComponent reason;

    public S00Disconnect(BaseComponent reason) {
        this.reason = reason;
    }

    public S00Disconnect() {
    }

    public BaseComponent getReason() {
        return this.reason;
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

    @Override
    public int getID() {
        return ID;
    }

}