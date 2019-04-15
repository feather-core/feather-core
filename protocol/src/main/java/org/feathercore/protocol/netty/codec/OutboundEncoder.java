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
import io.netty.handler.codec.MessageToByteEncoder;
import org.feathercore.protocol.netty.NettyBuffer;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class OutboundEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        int readable = in.readableBytes();
        int readableSize = getVarIntSize(readable);
        if (readableSize > 3) {
            throw new IllegalArgumentException(
                    "Packet size doesn't fit varint: it requires " + readableSize + " bytes");
        }
        NettyBuffer buffer = NettyBuffer.newInstance(out);
        buffer.ensureWritable(readable + readableSize);
        buffer.writeVarInt(readableSize);
        buffer.writeBytes(in, in.readerIndex(), readable);
    }

    private int getVarIntSize(int input) {
        for (int i = 1; i < 5; ++i) {
            if ((input & -1 << i * 7) == 0) {
                return i;
            }
        }
        return 5;
    }

}
