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

package org.feathercore.protocol.packet.status.client;

import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C00ServerQuery implements Packet {

    public final static int ID = 0x00;

    @Override
    public void write(@NotNull Buffer buffer) {

    }

    @Override
    public void read(@NotNull Buffer buffer) {

    }

    @Override
    public int getId() {
        return ID;
    }
}
