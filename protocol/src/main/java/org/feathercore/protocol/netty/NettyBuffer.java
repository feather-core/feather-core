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
import lombok.experimental.Delegate;
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
    private boolean releaseNetty = false;

    @Delegate(excludes = __DelegateExclusions.class) private ByteBuf buffer;

    protected NettyBuffer(Recycler.Handle<NettyBuffer> recycler) {
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

    @Override
    public String readString(int maxLength) {
        int length = readVarInt();
        if (length > maxLength) {
            throw new IllegalStateException(
                    "Trying to read string with length " + length + " when the max is " + maxLength);
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

    @Override
    public boolean release() {
        setHandle(null);
        recycler.recycle(this);

        return true;
    }

    @Override
    public int hashCode() {
        return buffer == null ? 0 : buffer.hashCode();
    }

    // Object's methods overridden due to ByteBuf's contracts

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(final Object obj) {
        if (buffer == null) return obj == null;
        return buffer.equals(obj);
    }

    @Override
    public String toString() {
        return getClass().getName() + (buffer == null ?  "{Uninitialized}" : "{" + buffer.toString() + "}");
    }

    // exclusions for Lombok's @Delegate
    private interface __DelegateExclusions {
        void release();
    }
}
