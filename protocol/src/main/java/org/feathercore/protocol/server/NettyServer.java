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

package org.feathercore.protocol.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.feathercore.protocol.handler.PacketHandler;
import org.feathercore.protocol.netty.channel.ChannelInitializer;
import org.feathercore.protocol.netty.util.SharedNettyResources;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@Log4j2
@RequiredArgsConstructor
public abstract class NettyServer {

    @NonNull private final String host;
    private final int port;

    @NonNull private final SharedNettyResources sharedNettyResources;

    private Channel channel;

    public NettyServer(@NonNull final String host, final int port) {
        this(host, port, SharedNettyResources.builder().build());
    }

    public ChannelFuture start() {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(sharedNettyResources.getBossLoopGroup(), sharedNettyResources.getWorkerLoopGroup())
                .channel(sharedNettyResources.getServerChannelClass())
                //TODO
                .childHandler(new ChannelInitializer(this::generateInitialHandler, log));
        return bootstrap.bind(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("Server has been started on " + host + ":" + port);
            } else {
                log.warn("Server could not bind to " + host + ":" + port, future.cause());
            }
        });
    }

    protected abstract PacketHandler generateInitialHandler();
}
