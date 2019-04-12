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

package org.feathercore.protocol.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.Recycler;
import org.feathercore.protocol.Buffer;

import java.nio.charset.StandardCharsets;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class NettyBuffer extends Buffer {

    private static Recycler<NettyBuffer> RECYCLER = new Recycler<NettyBuffer>() {
        @Override
        protected NettyBuffer newObject(Handle<NettyBuffer> handle) {
            return new NettyBuffer(handle);
        }
    };

    private final Recycler.Handle<NettyBuffer> recycler;
    private ByteBuf buffer;
    private boolean releaseNetty = false;

    private NettyBuffer(Recycler.Handle<NettyBuffer> recycler) {
        this.recycler = recycler;
    }

    public void setHandle(ByteBuf buffer) {
        if (this.buffer != null && releaseNetty) {
            releaseNetty = false;
            this.buffer.release();
        }
        if (buffer == null) {
            releaseNetty = false;
        }
        this.buffer = buffer;
    }

    public ByteBuf getHandle() {
        return this.buffer;
    }

    @Override
    public void release() {
        setHandle(null);
        recycler.recycle(this);
    }

    @Override
    public int readableBytes() {
        return this.buffer.readableBytes();
    }

    @Override
    public void ensureWritable(int size) {
        this.buffer.ensureWritable(size);
    }

    @Override
    public byte readByte() {
        return this.buffer.readByte();
    }

    @Override
    public void writeByte(byte val) {
        this.buffer.writeByte(val);
    }

    @Override
    public short readShort() {
        return this.buffer.readShort();
    }

    @Override
    public void writeShort(short val) {
        this.buffer.writeShort(val);
    }

    @Override
    public int readInt() {
        return this.buffer.readInt();
    }

    @Override
    public void writeInt(int val) {
        this.buffer.writeInt(val);
    }

    @Override
    public long readLong() {
        return this.buffer.readLong();
    }

    @Override
    public void writeLong(long val) {
        this.buffer.writeLong(val);
    }

    @Override
    public float readFloat() {
        return this.buffer.readFloat();
    }

    @Override
    public void writeFloat(float val) {
        this.buffer.writeFloat(val);
    }

    @Override
    public double readDouble() {
        return this.buffer.readDouble();
    }

    @Override
    public void writeDouble(double val) {
        this.buffer.writeDouble(val);
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        this.buffer.readBytes(bytes);
        return bytes;
    }

    @Override
    public void writeBytes(byte[] bytes) {
        this.buffer.writeBytes(bytes);
    }

    @Override
    public void writeBytes(byte[] bytes, int index, int size) {
        this.buffer.writeBytes(bytes, index, size);
    }

    public void writeBytes(ByteBuf buffer) {
        this.buffer.writeBytes(buffer);
    }

    public void writeBytes(ByteBuf buffer, int index, int size) {
        this.buffer.writeBytes(buffer, index, size);
    }

    @Override
    public String readString(int maxLength) {
        int length = readVarInt();
        if (length > maxLength) {
            throw new IllegalStateException("Trying to read string with length " + length + " when the max is " + maxLength);
        }
        String read = this.buffer.toString(this.buffer.readerIndex(), length, StandardCharsets.UTF_8);
        this.buffer.skipBytes(length);
        return read;
    }

    @Override
    public void writeString(String s) {
        ByteBuf encoded = buffer.alloc().buffer(ByteBufUtil.utf8MaxBytes(s));
        try {
            ByteBufUtil.writeUtf8(encoded, s);
            writeVarInt(encoded.readableBytes());
            this.buffer.writeBytes(encoded);
        } finally {
            encoded.release();
        }
    }

    @Override
    public NettyBuffer newBuffer(int size) {
        NettyBuffer wrapped = newInstance(buffer.alloc().buffer(size));
        wrapped.releaseNetty = true;
        return wrapped;
    }

    public static NettyBuffer newInstance() {
        return RECYCLER.get();
    }

    public static NettyBuffer newInstance(ByteBuf buffer) {
        NettyBuffer wrapper = RECYCLER.get();
        wrapper.setHandle(buffer);
        return wrapper;
    }

}
