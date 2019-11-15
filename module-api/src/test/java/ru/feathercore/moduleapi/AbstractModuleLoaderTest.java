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

package ru.feathercore.moduleapi;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.progrm_jarvis.javacommons.object.ValueContainer;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractModuleLoaderTest {

    @Mock FooModule fooModule;
    @Spy ModuleInitializer<FooModule, Void> fooModuleInitializer;
    @Mock BarModule barModule;
    @Spy ModuleInitializer<BarModule, Integer> barModuleInitializer;

    private Exception
            nullConfigException = new RuntimeException("null passed as config") {},
            config0Exception = new RuntimeException("0 passed as config") {},
            config1Exception = new RuntimeException("1 passed as config") {};


    interface SomeModule extends Module {}

    // Stubs to have different parent classes for modules
    private static class FooModule implements SomeModule {}

    private static class BarModule implements SomeModule {}

    private static class BazModule implements SomeModule {}

    protected static Stream<Arguments> provideTestedModuleLoader() {
        throw new IllegalStateException("provideTestedModuleLoader() should be overwritten in extending classes");
    }

    @ParameterizedTest
    @MethodSource("provideTestedModuleLoader")
    void testSimpleLoadUnload(final ModuleLoader<SomeModule> moduleLoader) {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());

        // check initial state
        assertThat(moduleLoader.getModules(), empty());

        // load foo ...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertSame(moduleLoader.getModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));

        // unload foo
        assertThat(moduleLoader.unloadModule(FooModule.class), notNullValue());
        // and check if it was unloaded
        assertThat(moduleLoader.getModules(), empty());
    }

    @ParameterizedTest
    @MethodSource("provideTestedModuleLoader")
    void checkContainment(final ModuleLoader<SomeModule> moduleLoader) {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());

        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);

        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertSame(moduleLoader.getModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));

        assertThat(moduleLoader.getModules(), not(contains(barModule)));
        assertSame(moduleLoader.getModule(BarModule.class).isPresent(), false);
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
    }

    @ParameterizedTest
    @MethodSource("provideTestedModuleLoader")
    void testDuplicateLoading(final ModuleLoader<SomeModule> moduleLoader) {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());

        // check initial state
        assertThat(moduleLoader.getModules(), empty());

        // load foo once ...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertSame(moduleLoader.getModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));

        // load foo again ...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        // ... and check if module loader did not load anything new
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertSame(moduleLoader.getModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
    }

    @ParameterizedTest
    @MethodSource("provideTestedModuleLoader")
    void testMultipleLoading(final ModuleLoader<SomeModule> moduleLoader) {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());
        doReturn(barModule).when(barModuleInitializer).loadModule(any());

        // load foo ...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertSame(moduleLoader.getModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));

        // load bar ...
        assertSame(moduleLoader.loadModule(barModuleInitializer, BarModule.class), barModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));
        assertSame(moduleLoader.getModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        assertSame(moduleLoader.getModule(BarModule.class).orElseThrow(AssertionError::new), barModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertTrue(moduleLoader.isModuleLoaded(barModule));
        assertTrue(moduleLoader.isModuleLoaded(BarModule.class));

        // unload each
        assertSame(moduleLoader.unloadModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
    }

    @ParameterizedTest
    @MethodSource("provideTestedModuleLoader")
    void testUnloadAll(final ModuleLoader<SomeModule> moduleLoader) {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());
        doReturn(barModule).when(barModuleInitializer).loadModule(any());

        // load foo ...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        assertSame(moduleLoader.loadModule(barModuleInitializer, BarModule.class), barModule);

        assertThat(moduleLoader.unloadAllModules(), containsInAnyOrder(fooModule, barModule));
        assertThat(moduleLoader.getModules(), empty());
    }

    @ParameterizedTest
    @MethodSource("provideTestedModuleLoader")
    void testIntensiveLoadingWithParameters(final ModuleLoader<SomeModule> moduleLoader) {
        // foo module
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());
        // bar module
        doReturn(ValueContainer.of(0)).when(barModuleInitializer).getDefaultConfiguration();
        doThrow(nullConfigException).when(barModuleInitializer).loadModule(isNull());
        doThrow(config0Exception).when(barModuleInitializer).loadModule(0);
        doThrow(config1Exception).when(barModuleInitializer).loadModule(1);
        doReturn(barModule).when(barModuleInitializer).loadModule(2);

        ///////////////////////////////////////////////////////////////////////////
        // Test module loading
        ///////////////////////////////////////////////////////////////////////////

        // make sure that by default there are no loaded modules
        assertSame(moduleLoader.isModuleLoaded(fooModule), false);
        assertSame(moduleLoader.isModuleLoaded(FooModule.class), false);
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
        assertThat(moduleLoader.getModules(), empty());

        // try to load foo module...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        // ... and check if it is now loaded
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertThat(moduleLoader.getModules(), contains(fooModule));

        // try to load bar module...
        // * With default (0) configuration
        assertThrows(config0Exception.getClass(), () -> moduleLoader.loadModule(barModuleInitializer, BarModule.class));
        verify(barModuleInitializer).getDefaultConfiguration();
        // ~ checking if nothing has broken
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
        // * With null configuration
        assertThrows(nullConfigException.getClass(), () -> moduleLoader.loadModule(barModuleInitializer, null, BarModule.class));
        // ~ checking if nothing has broken
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
        // * With 1 as configuration
        assertThrows(config1Exception.getClass(), () -> moduleLoader.loadModule(barModuleInitializer, 1, BarModule.class));
        // ~ checking if nothing has broken
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
        // * Normally
        assertSame(moduleLoader.loadModule(barModuleInitializer, 2, BarModule.class), barModule);
        // ... and check if it is now loaded
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertTrue(moduleLoader.isModuleLoaded(barModule));
        assertTrue(moduleLoader.isModuleLoaded(BarModule.class));
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));

        ///////////////////////////////////////////////////////////////////////////
        // Now test Module Unloading
        ///////////////////////////////////////////////////////////////////////////

        // make sure that unloading dummy modules does nothing
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertTrue(moduleLoader.isModuleLoaded(barModule));
        assertTrue(moduleLoader.isModuleLoaded(BarModule.class));

        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertTrue(moduleLoader.isModuleLoaded(barModule));
        assertTrue(moduleLoader.isModuleLoaded(BarModule.class));

        // unload foo module ...
        assertSame(moduleLoader.unloadModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        // ... and check loaded modules
        assertSame(moduleLoader.isModuleLoaded(fooModule), false);
        assertSame(moduleLoader.isModuleLoaded(FooModule.class), false);
        assertTrue(moduleLoader.isModuleLoaded(barModule));
        assertTrue(moduleLoader.isModuleLoaded(BarModule.class));
        assertThat(moduleLoader.getModules(), contains(barModule));

        // unload bar module...
        assertSame(moduleLoader.unloadModule(BarModule.class).orElseThrow(AssertionError::new), barModule);
        // ... and check loaded modules
        assertSame(moduleLoader.isModuleLoaded(fooModule), false);
        assertSame(moduleLoader.isModuleLoaded(FooModule.class), false);
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Test loading after unloading
        ///////////////////////////////////////////////////////////////////////////

        // try to load foo module...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        // ... and check if it is now loaded
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertThat(moduleLoader.getModules(), contains(fooModule));

        ///////////////////////////////////////////////////////////////////////////
        // Try unloading by type
        ///////////////////////////////////////////////////////////////////////////

        // unload bar module...
        assertSame(moduleLoader.unloadModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        // ... and check loaded modules
        assertSame(moduleLoader.isModuleLoaded(fooModule), false);
        assertSame(moduleLoader.isModuleLoaded(FooModule.class), false);
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Test loading after unloading
        ///////////////////////////////////////////////////////////////////////////

        // try to load foo module...
        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        // ... and check if it is now loaded
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertThat(moduleLoader.getModules(), contains(fooModule));

        ///////////////////////////////////////////////////////////////////////////
        // Try unloading by type
        ///////////////////////////////////////////////////////////////////////////

        // unload foo module...
        assertSame(moduleLoader.unloadModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        // ... and check loaded modules
        assertSame(moduleLoader.isModuleLoaded(fooModule), false);
        assertSame(moduleLoader.isModuleLoaded(FooModule.class), false);
        assertSame(moduleLoader.isModuleLoaded(barModule), false);
        assertSame(moduleLoader.isModuleLoaded(BarModule.class), false);
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Try to load multiple modules now
        ///////////////////////////////////////////////////////////////////////////

        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        assertSame(moduleLoader.loadModule(barModuleInitializer, 2, BarModule.class), barModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertTrue(moduleLoader.isModuleLoaded(barModule));
        assertTrue(moduleLoader.isModuleLoaded(BarModule.class));
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));

        ///////////////////////////////////////////////////////////////////////////
        // Unload all modules now
        ///////////////////////////////////////////////////////////////////////////

        assertThat(moduleLoader.unloadAllModules(), containsInAnyOrder(fooModule, barModule));
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Try to load multiple modules now again for other unload type
        ///////////////////////////////////////////////////////////////////////////

        assertSame(moduleLoader.loadModule(fooModuleInitializer, FooModule.class), fooModule);
        assertSame(moduleLoader.loadModule(barModuleInitializer, 2, BarModule.class), barModule);
        assertTrue(moduleLoader.isModuleLoaded(fooModule));
        assertTrue(moduleLoader.isModuleLoaded(FooModule.class));
        assertTrue(moduleLoader.isModuleLoaded(barModule));
        assertTrue(moduleLoader.isModuleLoaded(BarModule.class));
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));

        ///////////////////////////////////////////////////////////////////////////
        // Unload by type now
        ///////////////////////////////////////////////////////////////////////////

        assertSame(moduleLoader.unloadModule(FooModule.class).orElseThrow(AssertionError::new), fooModule);
        assertThat(moduleLoader.getModules(), contains(barModule));

        assertFalse(moduleLoader.unloadModule(BazModule.class).isPresent());
        assertThat(moduleLoader.getModules(), contains(barModule));

        assertSame(moduleLoader.unloadModule(BarModule.class).orElseThrow(AssertionError::new), barModule);
        assertThat(moduleLoader.getModules(), empty());
    }
}