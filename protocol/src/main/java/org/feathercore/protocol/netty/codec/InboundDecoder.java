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
import io.netty.handler.codec.CorruptedFrameException;
import org.feathercore.protocol.netty.NettyBuffer;

import java.util.List;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class InboundDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        byte[] readableSize = new byte[3];
        for (int i = 0; i < readableSize.length; ++i) {
            if (!in.isReadable()) {
                in.resetReaderIndex();
                return;
            }
            readableSize[i] = in.readByte();
            if (readableSize[i] >= 0) {
                NettyBuffer buffer = NettyBuffer.newInstance(Unpooled.wrappedBuffer(readableSize));
                try {
                    int size = buffer.readVarInt();
                    if (in.readableBytes() >= size) {
                        out.add(in.readBytes(size));
                        return;
                    }
                    in.resetReaderIndex();
                } finally {
                    buffer.release();
                }
            }
        }
        throw new CorruptedFrameException("Packet size doesn't fit varint: it exceeds it's maximal size");
    }

}
