package net.gtaun.shoebill.amx;

import net.gtaun.shoebill.event.amx.AmxLoadEvent;
import net.gtaun.shoebill.event.amx.AmxUnloadEvent;
import net.gtaun.util.event.EventManager;

import java.util.*;
import java.util.function.Consumer;

public class AmxInstanceManagerImpl implements AmxInstanceManager {
    private final Set<AmxInstance> instances;
    private final EventManager eventManager;
    private final Map<AmxInstance, Set<Consumer<AmxCallEvent>>> amxHooks;

    public AmxInstanceManagerImpl(EventManager eventManager, int[] existedHandles) {
        this.eventManager = eventManager;
        instances = new HashSet<>();
        amxHooks = new HashMap<>();
        for (int handle : existedHandles) {
            AmxInstance instance = new AmxInstanceImpl(handle);
            instances.add(instance);
        }
    }

    public void onAmxLoad(int handle) {
        AmxInstance instance = new AmxInstanceImpl(handle);
        instances.add(instance);
        amxHooks.put(instance, new HashSet<>());
        AmxLoadEvent event = new AmxLoadEvent(instance);
        eventManager.dispatchEvent(event, this);
    }

    public void onAmxUnload(int handle) {
        AmxInstance instance = new AmxInstanceImpl(handle);
        instances.remove(instance);
        amxHooks.remove(instance);
        AmxUnloadEvent event = new AmxUnloadEvent(instance);
        eventManager.dispatchEvent(event, this);
    }

    @Override
    public Set<AmxInstance> getAmxInstances() {
        return Collections.unmodifiableSet(instances);
    }

    @Override
    public Set<Consumer<AmxCallEvent>> getAmxHooks(AmxInstance instance) {
        if (amxHooks.containsKey(instance))
            return Collections.unmodifiableSet(amxHooks.get(instance));
        return null;
    }

    @Override
    public void hook(String functionName, Consumer<AmxCallEvent> consumer, boolean isCallback, Class<?>... classes) {
        /*for (AmxInstance instance : instances) {
            Set<Consumer<AmxCallEvent>> hooks = amxHooks.get(instance);
            hooks.add(consumer);
        }
        SampNativeFunction.registerHookArguments(functionName, isCallback, classes);*/
        //Not yet implemented
    }
}
