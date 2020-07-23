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

package org.feathercore.protocol.annotation;

import java.lang.annotation.*;

/**
 * Annotation used for creation of {@link org.feathercore.protocol.packet.PacketType}
 * from the class annotated with it.
 *
 * @apiNote This is an alternative to {@link PacketFactory} - they cannot be used at the same time.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketId {
    /**
     * ID of a packet which this class represents.
     *
     * @return ID of the packet which this class represents
     */
    int value();
}
