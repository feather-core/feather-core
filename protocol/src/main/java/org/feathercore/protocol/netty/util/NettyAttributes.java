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
import org.feathercore.protocol.handler.PacketRegistry;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class NettyAttributes {

    public final static AttributeKey<PacketRegistry> PACKET_REGISTRY_ATTRIBUTE_KEY = AttributeKey.newInstance("PRAK");

    public static <T> void setAttribute(ChannelHandlerContext ctx, AttributeKey<T> key, T value) {
        ctx.channel().attr(key).set(value);
    }

    public static <T> T getAttribute(ChannelHandlerContext ctx, AttributeKey<T> key) {
        return ctx.channel().hasAttr(key) ? ctx.channel().attr(key).get() : null;
    }

}
