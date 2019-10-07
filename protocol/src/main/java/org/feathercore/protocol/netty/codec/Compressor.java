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
import lombok.Setter;
import org.feathercore.protocol.netty.NettyBuffer;

import java.util.zip.Deflater;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class Compressor extends MessageToByteEncoder<ByteBuf> {

    private final byte[] buffer = new byte[8192];
    private final Deflater deflater;
    @Setter private int threshold;

    public Compressor(int threshold) {
        this.threshold = threshold;
        this.deflater = new Deflater();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        int size = in.readableBytes();
        NettyBuffer buffer = NettyBuffer.newInstance(out);

        if (size < this.threshold) {
            buffer.writeVarInt(0);
            buffer.writeBytes(in);
        } else {
            byte[] temporary = new byte[size];
            in.readBytes(temporary);
            buffer.writeVarInt(temporary.length);
            this.deflater.setInput(temporary, 0, size);
            this.deflater.finish();

            while (!this.deflater.finished()) {
                int deflatedSize = this.deflater.deflate(this.buffer);
                buffer.writeBytes(this.buffer, 0, deflatedSize);
            }

            this.deflater.reset();
        }
    }

}
