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

package org.feathercore.protocol.netty.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.netty.HandlerBoss;
import org.jetbrains.annotations.NotNull;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class NettyAttributes {

    public static final AttributeKey<Connection> CONNECTION_ATTRIBUTE_KEY = AttributeKey.newInstance("CAK");
    public static final AttributeKey<HandlerBoss> HANDLER_BOSS_ATTRIBUTE_KEY = AttributeKey.newInstance("HBAK");

    public static <T> void setAttribute(@NotNull ChannelHandlerContext ctx, @NotNull AttributeKey<T> key,
                                        @NotNull T value) {
        ctx.channel().attr(key).set(value);
    }

    public static <T> T getAttribute(@NotNull ChannelHandlerContext ctx, @NotNull AttributeKey<T> key) {
        return ctx.channel().hasAttr(key) ? ctx.channel().attr(key).get() : null;
    }

}
