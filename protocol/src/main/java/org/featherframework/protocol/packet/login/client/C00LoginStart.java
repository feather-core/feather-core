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

package org.featherframework.protocol.packet.login.client;

import com.mojang.authlib.GameProfile;
import org.featherframework.protocol.Buffer;
import org.featherframework.protocol.Packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C00LoginStart extends Packet {

    private GameProfile profile;

    public C00LoginStart(GameProfile profile) {
        this.profile = profile;
    }

    public C00LoginStart() {
    }

    public GameProfile getProfile() {
        return profile;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.profile.getName());
    }

    @Override
    public void read(Buffer buffer) {
        this.profile = new GameProfile(null, buffer.readString(16));
    }

}
