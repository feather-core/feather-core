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

package org.feathercore.protocol.packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public enum ConnectionState {
    HANDSHAKING(-1),
    PLAY(0),
    STATUS(1),
    LOGIN(2);

    private final static ConnectionState[] VALUES = values();

    public static ConnectionState getByID(int id) {
        for (ConnectionState state : VALUES) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    private final int id;

    ConnectionState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
