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

package org.feathercore.shared.util;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InvokeUtilTest {

    @Test
    void testToStaticRunnable() {
        assertDoesNotThrow(
                () -> InvokeUtil.toStaticRunnable(TestClass.class.getDeclaredMethod("staticVoidMethod")).run()
        );
        assertDoesNotThrow(
                () -> InvokeUtil.toStaticRunnable(TestClass.class.getDeclaredMethod("staticStringMethod")).run()
        );
    }

    @Test
    void testToBoundRunnable() {
        assertDoesNotThrow(() -> InvokeUtil.toBoundRunnable(
                TestClass.class.getDeclaredMethod("nonStaticStringMethod"), new TestClass()
                ).run()
        );
        assertDoesNotThrow(() -> InvokeUtil.toBoundRunnable(
                TestClass.class.getDeclaredMethod("nonStaticVoidMethod"), new TestClass()
                ).run()
        );
    }

    @Test
    @SneakyThrows(NoSuchMethodException.class)
    void testToStaticSupplier() {
        assertThat(
                InvokeUtil.toStaticSupplier(TestClass.class.getDeclaredMethod("staticStringMethod")).get(),
                equalTo("hi")
        );
    }

    @Test
    @SneakyThrows(NoSuchMethodException.class)
    void testToBoundSupplier() {
        assertThat(
                InvokeUtil.toBoundSupplier(
                        TestClass.class.getDeclaredMethod("nonStaticStringMethod"), new TestClass()
                ).get(),
                equalTo("bro")
        );
    }

    @Test
    @SneakyThrows(NoSuchMethodException.class)
    void testToSupplier() {
        assertThat(
                new TestClass(),
                equalTo(InvokeUtil.toSupplier(TestClass.class.getConstructor()).get())
        );
    }

    @EqualsAndHashCode
    public static class TestClass {

        public static void staticVoidMethod() {}

        public static String staticStringMethod() {
            return "hi";
        }

        public void nonStaticVoidMethod() {}

        public String nonStaticStringMethod() {
            return "bro";
        }
    }
}