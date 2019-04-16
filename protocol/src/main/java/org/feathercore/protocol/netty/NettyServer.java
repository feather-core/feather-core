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
import org.feathercore.protocol.server.BaseServer;
import org.feathercore.protocol.netty.channel.ChannelInitializer;
import org.feathercore.protocol.netty.util.SharedNettyResources;

import java.lang.ref.SoftReference;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@Log4j2
public abstract class NettyServer extends BaseServer {

    @NonNull private final SharedNettyResources sharedNettyResources;

    protected Channel channel;

    public NettyServer(@NonNull final String host, final int port) {
        super(host, port);
        this.sharedNettyResources = SharedNettyResources.builder().build();
    }

    @Override
    public ChannelFuture start() {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(sharedNettyResources.getBossLoopGroup(), sharedNettyResources.getWorkerLoopGroup())
                .channel(sharedNettyResources.getServerChannelClass())
                .childHandler(new ChannelInitializer(new SoftReference<>(this), log));
        return bootstrap.bind(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("BaseServer has been started on " + host + ":" + port);
            } else {
                log.warn("BaseServer could not bind to " + host + ":" + port, future.cause());
            }
        });
    }

    @Override
    public ChannelFuture stop() {
        return channel.close().syncUninterruptibly();
    }

}
