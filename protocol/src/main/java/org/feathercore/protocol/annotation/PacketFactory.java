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
 * from the class containing the annotation.
 * <ul>
 *     <li>When present on the class, it should normally be of type {@link org.feathercore.protocol.packet.Packet}</li>
 *     <li>When present on the method or constructor, it should be the default factory for the packet
 *     and its containing class should normally be of type {@link org.feathercore.protocol.packet.Packet}</li>
 * </ul>.
 */
@Documented
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketFactory {

    /**
     * Retrieves the ID which should be used for creation of {@link org.feathercore.protocol.packet.PacketType}.
     *
     * @return ID which should be used for creation of {@link org.feathercore.protocol.packet.PacketType}
    int value();
     */
}
