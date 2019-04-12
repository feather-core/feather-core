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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.feathercore.protocol.handler.PacketHandler;
import org.feathercore.protocol.netty.channel.ChannelInitializer;
import org.feathercore.protocol.netty.util.NettyUtil;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@Log4j2
@RequiredArgsConstructor
public abstract class NettyServer {

    private final String host;
    private final int port;

    private Channel channel;

    public ChannelFuture start() {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(NettyUtil.getBossLoopGroup(), NettyUtil.getWorkerLoopGroup())
                .channel(NettyUtil.getServerChannel())
                //TODO
                .childHandler(new ChannelInitializer(this::generateInitialHandler, log));
        return bootstrap.bind(this.host, this.port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                this.channel = future.channel();
                log.info("Server has been started on " + this.host + ":" + this.port);
            } else {
                log.warn("Server could not bind to " + this.host + ":" + this.port, future.cause());
            }
        });
    }

    protected abstract PacketHandler generateInitialHandler();

}
