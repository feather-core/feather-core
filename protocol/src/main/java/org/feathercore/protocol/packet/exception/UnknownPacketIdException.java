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

package org.feathercore.protocol.packet.exception;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
public class UnknownPacketIdException extends PacketException {

    public UnknownPacketIdException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public UnknownPacketIdException(@Nullable final String message) {
        super(message);
    }

    public UnknownPacketIdException(@Nullable final Throwable cause) {
        super(cause);
    }
}
