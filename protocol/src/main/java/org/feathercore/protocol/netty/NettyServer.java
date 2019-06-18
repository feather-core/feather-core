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
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.netty.channel.ChannelInitializer;
import org.feathercore.protocol.netty.util.SharedNettyResources;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.server.AbstractServer;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.net.SocketAddress;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@Log4j2
public abstract class NettyServer<P extends Packet> extends AbstractServer<P> {

    @NonNull private final SharedNettyResources sharedNettyResources;

    protected Channel channel;

    public NettyServer(@NonNull final SocketAddress address) {
        super(address);
        this.sharedNettyResources = SharedNettyResources.builder().build();
    }

    @Override
    public ChannelFuture start() {
        val address = getAddress();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(sharedNettyResources.getBossLoopGroup(), sharedNettyResources.getWorkerLoopGroup())
                .channel(sharedNettyResources.getServerChannelClass())
                .childHandler(new ChannelInitializer(new SoftReference<>(this), log));
        return bootstrap.bind(getAddress()).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("Server has been started on " + address);
            } else {
                log.warn("Server could not bind to " + address, future.cause());
            }
        });
    }

    @Override
    public ChannelFuture stop() {
        return channel.close().syncUninterruptibly();
    }

    @Override
    public void handleConnect(final @NotNull Connection connection) {}

    @Override
    public void handleDisconnect(final @NotNull Connection connection) {}
}
