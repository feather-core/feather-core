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

import org.feathercore.shared.object.ValueContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractModuleLoaderTest {

    @Mock FooModule fooModule;
    @Spy ModuleInitializer<FooModule, Void> fooModuleInitializer;
    @Mock BarModule barModule;
    @Spy ModuleInitializer<FooModule, Integer> barModuleInitializer;
    // < exact type is required for use of protected methods >
    @SuppressWarnings("unchecked") private AbstractModuleLoader<Module> moduleLoader = mock(AbstractModuleLoader.class,
            withSettings().defaultAnswer(Answers.CALLS_REAL_METHODS).useConstructor(new HashSet<>())
    );

    private Exception
            nullConfigException = new RuntimeException("null passed as config"){},
            config0Exception = new RuntimeException("0 passed as config"){},
            config1Exception = new RuntimeException("1 passed as config"){};

    // Stubs to have different parent classes for modules
    private class FooModule implements Module {}
    private class BarModule implements Module {}
    private class BazModule implements Module {}

    @Test
    void testSimpleLoadUnload() {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());

        // check initial state
        assertThat(moduleLoader.getModules(), empty());

        // load foo ...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        verify(moduleLoader).onModuleLoad(fooModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertThat(moduleLoader.getModules(fooModule.getClass()), contains(fooModule));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule.getClass()), is(true));

        // unload foo
        assertThat(moduleLoader.unloadModule(fooModule), is(true));
        verify(moduleLoader).onModuleUnload(fooModule);
        // and check if it was unloaded
        assertThat(moduleLoader.getModules(), empty());
    }

    @Test
    void checkContainment() {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());

        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));

        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertThat(moduleLoader.getModules(fooModule.getClass()), contains(fooModule));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule.getClass()), is(true));

        assertThat(moduleLoader.getModules(), not(contains(barModule)));
        assertThat(moduleLoader.getModules(barModule.getClass()), empty());
        assertThat(moduleLoader.getModule(barModule.getClass()).isPresent(), is(false));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(barModule.getClass()), is(false));
    }

    @Test
    void testDuplicateLoading() {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());

        // check initial state
        assertThat(moduleLoader.getModules(), empty());

        // load foo once ...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        verify(moduleLoader).onModuleLoad(fooModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertThat(moduleLoader.getModules(fooModule.getClass()), contains(fooModule));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule.getClass()), is(true));

        // load foo again ...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        verify(moduleLoader).onModuleLoad(fooModule); // 1 time again which means that it was not called again
        // ... and check if module loader did not load anything new
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertThat(moduleLoader.getModules(fooModule.getClass()), contains(fooModule));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule.getClass()), is(true));
    }

    @Test
    void testMultipleLoading() {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());
        doReturn(barModule).when(barModuleInitializer).loadModule(any());

        // load foo ...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        verify(moduleLoader).onModuleLoad(fooModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), contains(fooModule));
        assertThat(moduleLoader.getModules(fooModule.getClass()), contains(fooModule));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule.getClass()), is(true));

        // load bar ...
        assertThat(moduleLoader.loadModule(barModuleInitializer), is(barModule));
        verify(moduleLoader).onModuleLoad(barModule);
        // ... and check if it was loaded normally
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));
        assertThat(moduleLoader.getModules(fooModule.getClass()), contains(fooModule));
        assertThat(moduleLoader.getModules(barModule.getClass()), contains(barModule));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule.getClass()), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule.getClass()), is(true));

        // unload each
        assertThat(moduleLoader.unloadModule(fooModule), is(true));
    }

    @Test
    void testUnloadAll() {
        doReturn(fooModule).when(fooModuleInitializer).loadModule(any());
        doReturn(barModule).when(barModuleInitializer).loadModule(any());

        // load foo ...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        assertThat(moduleLoader.loadModule(barModuleInitializer), is(barModule));

        assertThat(moduleLoader.unloadAllModules(), containsInAnyOrder(fooModule, barModule));
        assertThat(moduleLoader.getModules(), empty());
    }

    @Test
    void testIntensiveLoadingWithParameters() {
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
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(false));
        assertThat(moduleLoader.getModules(), empty());

        // try to load foo module...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        verify(moduleLoader).onModuleLoad(fooModule);
        // ... and check if it is now loaded
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.getModules(), contains(fooModule));

        // try to load bar module...
        // * With default (0) configuration
        assertThrows(config0Exception.getClass(), () -> moduleLoader.loadModule(barModuleInitializer));
        verify(barModuleInitializer).getDefaultConfiguration();
        verify(moduleLoader, never()).onModuleLoad(barModule);
        // ~ checking if nothing has broken
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(false));
        // * With null configuration
        assertThrows(nullConfigException.getClass(), () -> moduleLoader.loadModule(barModuleInitializer, null));
        verify(moduleLoader, never()).onModuleLoad(barModule);
        // ~ checking if nothing has broken
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(false));
        // * With 1 as configuration
        assertThrows(config1Exception.getClass(), () -> moduleLoader.loadModule(barModuleInitializer, 1));
        verify(moduleLoader, never()).onModuleLoad(barModule);
        // ~ checking if nothing has broken
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(false));
        // * Normally
        assertThat(moduleLoader.loadModule(barModuleInitializer, 2), is(barModule));
        verify(moduleLoader).onModuleLoad(barModule);
        // ... and check if it is now loaded
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(true));
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));

        ///////////////////////////////////////////////////////////////////////////
        // Now test Module Unloading
        ///////////////////////////////////////////////////////////////////////////

        // make sure that unloading dummy modules does nothing
        assertThat(moduleLoader.unloadModule(new Module(){}), is(false));
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(true));

        assertThat(moduleLoader.unloadModules(new Module(){}.getClass()), empty());
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(true));

        // unload foo module ...
        assertThat(moduleLoader.unloadModule(fooModule), is(true));
        verify(moduleLoader).onModuleUnload(fooModule);
        // ... and check loaded modules
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(true));
        assertThat(moduleLoader.getModules(), contains(barModule));

        // unload bar module...
        assertThat(moduleLoader.unloadModule(barModule), is(true));
        verify(moduleLoader).onModuleUnload(barModule);
        // ... and check loaded modules
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(false));
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Test loading after unloading
        ///////////////////////////////////////////////////////////////////////////

        // try to load foo module...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        verify(moduleLoader, times(2)).onModuleLoad(fooModule);
        // ... and check if it is now loaded
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.getModules(), contains(fooModule));

        ///////////////////////////////////////////////////////////////////////////
        // Try unloading by type
        ///////////////////////////////////////////////////////////////////////////

        // unload bar module...
        assertThat(moduleLoader.unloadModule(fooModule), is(true));
        verify(moduleLoader, times(2)).onModuleUnload(fooModule);
        // ... and check loaded modules
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(false));
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Test loading after unloading
        ///////////////////////////////////////////////////////////////////////////

        // try to load foo module...
        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        verify(moduleLoader, times(3)).onModuleLoad(fooModule);
        // ... and check if it is now loaded
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.getModules(), contains(fooModule));

        ///////////////////////////////////////////////////////////////////////////
        // Try unloading by type
        ///////////////////////////////////////////////////////////////////////////

        // unload foo module...
        assertThat(moduleLoader.unloadModule(fooModule), is(true));
        // ... and check loaded modules
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(false));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(false));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(false));
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Try to load multiple modules now
        ///////////////////////////////////////////////////////////////////////////

        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        assertThat(moduleLoader.loadModule(barModuleInitializer, 2), is(barModule));
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(true));
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));

        ///////////////////////////////////////////////////////////////////////////
        // Unload all modules now
        ///////////////////////////////////////////////////////////////////////////

        assertThat(moduleLoader.unloadAllModules(), containsInAnyOrder(fooModule, barModule));
        assertThat(moduleLoader.getModules(), empty());

        ///////////////////////////////////////////////////////////////////////////
        // Try to load multiple modules now again for other unload type
        ///////////////////////////////////////////////////////////////////////////

        assertThat(moduleLoader.loadModule(fooModuleInitializer), is(fooModule));
        assertThat(moduleLoader.loadModule(barModuleInitializer, 2), is(barModule));
        assertThat(moduleLoader.isModuleLoaded(Module.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(fooModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(FooModule.class), is(true));
        assertThat(moduleLoader.isModuleLoaded(barModule), is(true));
        assertThat(moduleLoader.isModuleLoaded(BarModule.class), is(true));
        assertThat(moduleLoader.getModules(), containsInAnyOrder(fooModule, barModule));

        ///////////////////////////////////////////////////////////////////////////
        // Unload by type now
        ///////////////////////////////////////////////////////////////////////////

        assertThat(moduleLoader.unloadModules(fooModule.getClass()), contains(fooModule));
        assertThat(moduleLoader.getModules(), contains(barModule));

        assertThat(moduleLoader.unloadModules(BazModule.class), empty());
        assertThat(moduleLoader.getModules(), contains(barModule));

        assertThat(moduleLoader.unloadModules(barModule.getClass()), contains(barModule));
        assertThat(moduleLoader.getModules(), empty());
    }
}