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

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

/**
 * Created by k.shandurenko on 12/04/2019
 */
class CipherTranslator {

    private final Cipher cipher;
    private byte[] temporaryArray = new byte[0];
    private byte[] temporaryArray2 = new byte[0];

    CipherTranslator(Cipher cipher) {
        this.cipher = cipher;
    }

    private byte[] readViaTemporary(ByteBuf buffer) {
        int size = buffer.readableBytes();

        if (this.temporaryArray.length < size) {
            this.temporaryArray = new byte[size];
        }

        buffer.readBytes(this.temporaryArray, 0, size);
        return this.temporaryArray;
    }

    ByteBuf decipher(ChannelHandlerContext ctx, ByteBuf buffer) throws ShortBufferException {
        int size = buffer.readableBytes();
        byte[] data = this.readViaTemporary(buffer);
        ByteBuf bytebuf = ctx.alloc().heapBuffer(this.cipher.getOutputSize(size));
        bytebuf.writerIndex(this.cipher.update(data, 0, size, bytebuf.array(), bytebuf.arrayOffset()));
        return bytebuf;
    }

    void cipher(ByteBuf in, ByteBuf out) throws ShortBufferException {
        int size = in.readableBytes();
        byte[] data = this.readViaTemporary(in);
        int outputSize = this.cipher.getOutputSize(size);

        if (this.temporaryArray2.length < outputSize) {
            this.temporaryArray2 = new byte[outputSize];
        }

        out.writeBytes(this.temporaryArray2, 0, this.cipher.update(data, 0, size, this.temporaryArray2));
    }

}
