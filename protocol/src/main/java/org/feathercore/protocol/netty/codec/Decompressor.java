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
import io.netty.handler.codec.DecoderException;
import lombok.Setter;
import org.feathercore.protocol.netty.NettyBuffer;

import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class Decompressor extends ByteToMessageDecoder {

    private static final int PROTOCOL_MAXIMUM = 2097152;

    private final Inflater inflater;
    @Setter private int threshold;

    public Decompressor(int threshold) {
        this.threshold = threshold;
        this.inflater = new Inflater();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws DecoderException, DataFormatException {
        if (in.readableBytes() != 0) {
            NettyBuffer buffer = NettyBuffer.newInstance(in);
            int size = buffer.readVarInt();

            if (size == 0) {
                out.add(buffer.readBytes(buffer.readableBytes()));
                return;
            }

            if (size < this.threshold) {
                throw new DecoderException("Badly compressed packet: size of " + size + " is below server threshold of " + this.threshold);
            }
            if (size > PROTOCOL_MAXIMUM) {
                throw new DecoderException("Badly compressed packet: size of " + size + " is larger than protocol maximum of " + PROTOCOL_MAXIMUM);
            }

            byte[] temporary = buffer.readBytes(buffer.readableBytes());
            this.inflater.setInput(temporary);
            byte[] compressed = new byte[size];
            this.inflater.inflate(compressed);
            out.add(Unpooled.wrappedBuffer(compressed));
            this.inflater.reset();
        }
    }

}
