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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.NonNull;
import org.apache.logging.log4j.Logger;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.exception.PacketHandleException;
import org.feathercore.protocol.handler.PacketHandler;
import org.feathercore.protocol.netty.util.NettyAttributes;
import org.feathercore.protocol.packet.Packet;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class HandlerBoss extends ChannelInboundHandlerAdapter {

    private PacketHandler packetHandler;
    private final Logger logger;

    public HandlerBoss(PacketHandler packetHandler, Logger logger) {
        this.packetHandler = packetHandler;
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public PacketHandler getHandler() {
        return this.packetHandler;
    }

    public void setHandler(@NonNull PacketHandler handler) {
        this.packetHandler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NettyConnection connection = new NettyConnection(ctx);
        NettyAttributes.setAttribute(ctx, NettyAttributes.CONNECTION_ATTRIBUTE_KEY, connection);
        NettyAttributes.setAttribute(ctx, NettyAttributes.HANDLER_BOSS_ATTRIBUTE_KEY, this);
        this.packetHandler.onConnected(connection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Connection connection = NettyAttributes.getAttribute(ctx, NettyAttributes.CONNECTION_ATTRIBUTE_KEY);
        if (connection == null) {
            return;
        }
        this.packetHandler.onDisconnected(connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Connection connection = NettyAttributes.getAttribute(ctx, NettyAttributes.CONNECTION_ATTRIBUTE_KEY);
        if (connection == null) {
            return;
        }
        try {
            this.packetHandler.handle(connection, (Packet) msg);
        } catch (Exception ex) {
            throw new PacketHandleException(msg.getClass().getSimpleName(), ex);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (ctx.channel().isActive()) {
            if (cause instanceof TimeoutException) {
                this.logger.info(ctx.channel().remoteAddress() + " - read timed out");
            } else if (cause instanceof IOException) {
                this.logger.info(ctx.channel().remoteAddress() + " - IOException: " + cause.getMessage());
            } else if (cause instanceof DecoderException) {
                this.logger.warn(ctx.channel().remoteAddress() + " - Decoder exception: ", cause);
            } else if (cause instanceof PacketHandleException) {
                this.logger.warn(
                        ctx.channel().remoteAddress() + " - Can not handle packet: " + cause.getMessage(),
                        cause.getCause()
                );
            } else {
                this.logger.error(ctx.channel().remoteAddress() + " - Encountered exception", cause);
            }
            ctx.close();
        }
    }

}
