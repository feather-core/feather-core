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

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class ServerPingAdapter extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object bufferObj) {
        ByteBuf buffer = (ByteBuf) bufferObj;
        buffer.markReaderIndex();
        boolean flag = true;

        try {
            if (buffer.readUnsignedByte() == 254) {
                int i = buffer.readableBytes();

                switch (i) {
                    case 0:
                        String result = getServerMOTD() + "\u00a7" + getCurrentPlayerCount() + "\u00a7" + getMaxPlayers();
                        this.writeAndFlush(ctx, this.getStringBuffer(result));
                        break;

                    case 1:
                        if (buffer.readUnsignedByte() != 1) {
                            return;
                        }

                        result = "\u00a71\u0000127\u0000" + getMinecraftVersion() + "\u0000" + getServerMOTD() + "\u0000" + getCurrentPlayerCount() + "\u0000" + getMaxPlayers();
                        this.writeAndFlush(ctx, this.getStringBuffer(result));
                        break;

                    default:
                        boolean flag2 = buffer.readUnsignedByte() == 1;
                        flag2 = flag2 & buffer.readUnsignedByte() == 250;
                        flag2 = flag2 & "MC|PingHost".equals(new String(buffer.readBytes(buffer.readShort() * 2).array(), Charsets.UTF_16BE));
                        int size = buffer.readUnsignedShort();
                        flag2 = flag2 & buffer.readUnsignedByte() >= 73;
                        flag2 = flag2 & 3 + buffer.readBytes(buffer.readShort() * 2).array().length + 4 == size;
                        flag2 = flag2 & buffer.readInt() <= 65535;
                        flag2 = flag2 & buffer.readableBytes() == 0;

                        if (!flag2) {
                            return;
                        }

                        result = "\u00a71\u0000127\u0000" + getMinecraftVersion() + "\u0000" + getServerMOTD() + "\u0000" + getCurrentPlayerCount() + "\u0000" + getMaxPlayers();
                        ByteBuf out = this.getStringBuffer(result);

                        try {
                            this.writeAndFlush(ctx, out);
                        } finally {
                            out.release();
                        }
                }

                buffer.release();
                flag = false;
            }
        } catch (RuntimeException ignored) {
        } finally {
            if (flag) {
                buffer.resetReaderIndex();
                ctx.channel().pipeline().remove("legacy_query");
                ctx.fireChannelRead(bufferObj);
            }
        }
    }

    //TODO
    private String getServerMOTD() {
        return "";
    }

    //TODO
    private int getCurrentPlayerCount() {
        return 0;
    }

    //TODO
    private int getMaxPlayers() {
        return 0;
    }

    //TODO
    private String getMinecraftVersion() {
        return "";
    }

    private void writeAndFlush(ChannelHandlerContext ctx, ByteBuf data) {
        ctx.pipeline().firstContext().writeAndFlush(data).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf getStringBuffer(String string) {
        ByteBuf bytebuf = Unpooled.buffer();
        bytebuf.writeByte(255);
        char[] chars = string.toCharArray();
        bytebuf.writeShort(chars.length);

        for (char c : chars) {
            bytebuf.writeChar(c);
        }

        return bytebuf;
    }

}
