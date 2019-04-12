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

package org.feathercore.protocol.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import org.feathercore.protocol.registry.PacketRegistry;
import org.feathercore.protocol.netty.NettyBuffer;
import org.feathercore.protocol.netty.util.NettyAttributes;
import org.feathercore.protocol.packet.Packet;

import java.util.List;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class InboundPacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        NettyBuffer buffer = NettyBuffer.newInstance(in);
        int packetID = buffer.readVarInt();
        PacketRegistry<?> registry = NettyAttributes.getAttribute(ctx, NettyAttributes.PACKET_REGISTRY_ATTRIBUTE_KEY);
        if (registry == null) {
            throw new IllegalStateException("Could not retrieve context packet registry");
        }
        Packet packet = registry.createEmptyPacketByID(packetID);
        if (packet == null) {
            throw new CorruptedFrameException("Packet with unknown id: " + packetID);
        }
        packet.read(buffer);
        out.add(packet);
    }

}
