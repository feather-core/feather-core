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

package org.feathercore.protocol.packet;

import io.netty.buffer.Unpooled;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.netty.NettyBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * Base for all packets used in standard minecraft protocol.
 */
public interface Packet {

    default void read(@NotNull final Buffer buffer) {
        throw new UnsupportedOperationException("Packet " + getClass().getName() + " is not an incoming packet");
    }

    default void write(@NotNull final Buffer buffer) {
        throw new UnsupportedOperationException("Packet " + getClass().getName() + " is not an outcoming packet");
    }

    int getId();

    default byte[] serialize() {
        NettyBuffer buffer = NettyBuffer.newInstance(Unpooled.buffer(128)); //TODO capacity
        buffer.writeVarInt(getId());
        write(buffer);
        try {
            buffer.readerIndex(0);
            return buffer.readByteArray(buffer.readableBytes());
        } finally {
            buffer.release();
        }
    }

}
