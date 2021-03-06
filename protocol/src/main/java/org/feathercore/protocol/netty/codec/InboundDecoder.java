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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.feathercore.protocol.netty.NettyBuffer;
import org.feathercore.protocol.packet.exception.WrongPacketSizeException;

import java.util.List;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class InboundDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!in.isReadable()) {
            return;
        }

        int origReaderIndex = in.readerIndex();
        for (int i = 0; i < 3; i++) {
            if (!in.isReadable()) {
                in.readerIndex(origReaderIndex);
                return;
            }

            byte read = in.readByte();
            if (read >= 0) {
                in.readerIndex(origReaderIndex);
                // TODO: Don't create NettyBuffer just for reading varint?
                int packetLength = NettyBuffer.newInstance(in).readVarInt();
                if (packetLength == 0) {
                    return;
                }

                if (in.readableBytes() < packetLength) {
                    in.readerIndex(origReaderIndex);
                    return;
                }

                out.add(in.readRetainedSlice(packetLength));
                return;
            }
        }
        throw new WrongPacketSizeException("Packet size doesn't fit varint: it exceeds it's maximal size");
    }
}
