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

import lombok.NonNull;
import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.feathercore.protocol.netty.util.SimpleSharedNettyResources;
import org.feathercore.protocol.server.SimpleMinecraftServer;
import ru.feathercore.moduleapi.Module;
import ru.feathercore.moduleapi.SimpleModuleLoader;

import java.net.SocketAddress;

/**
 * Simple feather-server for standard minecraft protocol.
 *
 * @param <M> super-type of modules which this server is able to load
 */
public class SimpleMinecraftFeatherServer<M extends Module> extends AbstractFeatherServer<MinecraftPacket, M> {

    /**
     * Creates new feather-server for standard minecraft protocol
     *
     * @param address address of this server used by IO-server
     */
    public SimpleMinecraftFeatherServer(@NonNull final SocketAddress address) {
        super(new SimpleMinecraftServer(address, SimpleSharedNettyResources.createDefault()), new SimpleModuleLoader<>());
    }
}
