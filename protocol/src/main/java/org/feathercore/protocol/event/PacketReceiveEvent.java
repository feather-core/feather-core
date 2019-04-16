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

package org.feathercore.protocol.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.feathercore.eventbus.CancellableEvent;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;

/**
 * Created by k.shandurenko on 16/04/2019
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PacketReceiveEvent extends CancellableEvent {

    private final Connection connection;
    private final Packet packet;

}
