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

package org.feathercore.protocol.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.feathercore.protocol.netty.HandlerBoss;
import org.feathercore.protocol.netty.codec.InboundDecoder;
import org.feathercore.protocol.netty.codec.InboundPacketDecoder;
import org.feathercore.protocol.netty.codec.OutboundEncoder;
import org.feathercore.protocol.netty.codec.ServerPingAdapter;
import org.feathercore.protocol.server.Server;

import java.lang.ref.SoftReference;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@RequiredArgsConstructor
public class ChannelInitializer extends io.netty.channel.ChannelInitializer<Channel> {

    private final SoftReference<Server> serverSoftReference;
    private final Logger logger;

    @Override
    protected void initChannel(Channel ch) {
        ch.config().setOption(ChannelOption.TCP_NODELAY, true);
        ch.pipeline()
                .addLast("timeout", new ReadTimeoutHandler(30))
                .addLast("legacy_query", new ServerPingAdapter())
                .addLast("splitter", new InboundDecoder())
                .addLast("decoder", new InboundPacketDecoder())
                .addLast("prepender", new OutboundEncoder())
                .addLast("handler_boss", new HandlerBoss(serverSoftReference, logger));
    }
}
