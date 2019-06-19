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

import lombok.experimental.Delegate;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.server.Server;
import ru.feathercore.moduleapi.Module;
import ru.feathercore.moduleapi.ModuleLoader;

/**
 * <b>feather-core</b> server, core component of the whole project.
 *
 * @param <P> super-type of packets used by the server's protocol
 * @param <M> super-type of modules which this server is able to load
 */
public interface FeatherServer<P extends Packet, M extends Module> extends AutoCloseable {

    /**
     * Gets the IO-server used by this feather-server.
     *
     * @return IO-server used by this feather-server
     */
    Server<P> getIoServer();

    /**
     * Gets the module-loader of this feather-server.
     *
     * @return module-loader of this feather-server
     */
    @Delegate ModuleLoader<M> getModuleLoader();

    /**
     * Starts this feather-server.
     */
    void start();

    /**
     * Shuts down this feather-server.
     */
    @Override
    void close();
}
