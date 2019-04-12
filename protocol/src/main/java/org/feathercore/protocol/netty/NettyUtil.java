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

import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.experimental.UtilityClass;
import org.feathercore.shared.util.NamedThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@UtilityClass
public class NettyUtil {
    private static boolean bossIsWorker = true;
    private static int bossThreads = 2;
    private static int workerThreads = 0;

    private static EventLoopGroup bossLoopGroup = null;
    private static EventLoopGroup workerLoopGroup = null;

    public static void setup(boolean bossIsWorker, int bossThreads, int workerThreads) {
        NettyUtil.bossIsWorker = bossIsWorker;
        NettyUtil.bossThreads = bossThreads;
        NettyUtil.workerThreads = workerThreads;
    }

    public static boolean isUsed() {
        return bossLoopGroup != null;
    }

    public synchronized static void shutdownLoopGroups() {
        if (bossLoopGroup != null) {
            bossLoopGroup.shutdownGracefully();
            if (bossIsWorker) {
                workerLoopGroup = null;
            }
            bossLoopGroup = null;
        }
        if (workerLoopGroup != null) {
            workerLoopGroup.shutdownGracefully();
            workerLoopGroup = null;
        }
    }

    public synchronized static EventLoopGroup getBossLoopGroup() {
        if (bossLoopGroup == null) {
            ThreadFactory tf;
            if (bossIsWorker) {
                tf = new NamedThreadFactory("Netty IO Thread #", false);
            } else {
                tf = new NamedThreadFactory("Netty Boss Thread #", false);
            }
            bossLoopGroup = newEventLoopGroup(bossThreads, tf);
        }
        return bossLoopGroup;
    }

    public synchronized static EventLoopGroup getWorkerLoopGroup() {
        if (workerLoopGroup == null) {
            if (bossIsWorker) {
                workerLoopGroup = getBossLoopGroup();
            } else {
                ThreadFactory tf = new NamedThreadFactory("Netty Worker Thread #", false);
                workerLoopGroup = newEventLoopGroup(workerThreads, tf);
            }
        }
        return workerLoopGroup;
    }

    public static Class<? extends SocketChannel> getChannel() {
        return Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    public static Class<? extends ServerChannel> getServerChannel() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    private static EventLoopGroup newEventLoopGroup(int threads, ThreadFactory tf) {
        return Epoll.isAvailable() ? new EpollEventLoopGroup(threads, tf) : new NioEventLoopGroup(threads, tf);
    }
}