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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.feathercore.protocol.Connection;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@RequiredArgsConstructor
public class NettyConnection implements Connection {

    @Getter
    private final ChannelHandlerContext context;

    @Override
    public void write(@NotNull byte[] bytes) {
        this.context.writeAndFlush(bytes, context.voidPromise());
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) this.context.channel().remoteAddress();
    }
}