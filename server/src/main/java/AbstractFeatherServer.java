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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.server.Server;
import ru.feathercore.moduleapi.Module;
import ru.feathercore.moduleapi.ModuleLoader;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.concurrent.ExecutionException;

/**
 * Base for a regular feather-server implementation.
 *
 * @param <P> super-type of packets used by the server's protocol
 * @param <M> super-type of modules which this server is able to load
 */
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractFeatherServer<P extends Packet, M extends Module> implements FeatherServer<P, M> {

    /**
     * IO-server used by this feather-server
     */
    @NonNull @Getter Server<P> ioServer;
    /**
     * Module-loader used by this server
     */
    @NonNull @Getter ModuleLoader<M> moduleLoader;

    @Override
    @OverridingMethodsMustInvokeSuper
    public void start() {
        try {
            ioServer.start().get();
        } catch (final InterruptedException | ExecutionException e) {
            log.error("An exception occurred while starting the IO-server", e);
        }
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void close() {
        try {
            moduleLoader.unloadAllModules();
        } catch (final Throwable e) {
            log.error("An exception occurred while unloading modules", e);
        }
        try {
            ioServer.stop().get();
        } catch (final Throwable e) {
            log.error("An exception occurred while stopping the IO-server", e);
        }
    }
}
