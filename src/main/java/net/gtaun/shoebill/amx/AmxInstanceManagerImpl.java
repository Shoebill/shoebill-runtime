package net.gtaun.shoebill.amx;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.event.amx.AmxLoadEvent;
import net.gtaun.shoebill.event.amx.AmxUnloadEvent;
import net.gtaun.util.event.EventManager;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;

public class AmxInstanceManagerImpl implements AmxInstanceManager {

    private final Set<AmxInstance> instances;
    private final EventManager eventManager;
    private final List<AmxHook> hooks;

    public AmxInstanceManagerImpl(EventManager eventManager, int[] existedHandles) {
        Instance.reference = new WeakReference<AmxInstanceManager>(this);
        this.eventManager = eventManager;
        instances = new HashSet<>();
        hooks = new ArrayList<>();
        for (int handle : existedHandles) {
            AmxInstance instance = new AmxInstanceImpl(handle);
            instances.add(instance);
        }
    }

    public void onAmxLoad(int handle) {
        AmxInstance instance = new AmxInstanceImpl(handle);
        instances.add(instance);
        AmxLoadEvent event = new AmxLoadEvent(instance);
        eventManager.dispatchEvent(event, this);
    }

    public void onAmxUnload(int handle) {
        AmxInstance instance = new AmxInstanceImpl(handle);
        instances.remove(instance);
        AmxUnloadEvent event = new AmxUnloadEvent(instance);
        eventManager.dispatchEvent(event, this);
    }

    @Override
    public Set<AmxInstance> getAmxInstances() {
        return Collections.unmodifiableSet(instances);
    }

    @Override
    public Set<AmxHook> getAmxHooks() {
        return Collections.unmodifiableSet(new HashSet<>(hooks));
    }

    @Override
    public boolean hookCallback(String name, Consumer<AmxCallEvent> onCall, String parameters) {
        if (SampNativeFunction.hookCallback(name, parameters)) {
            hooks.add(new AmxHook(name, onCall, parameters));
            return true;
        }
        return false;
    }

    @Override
    public boolean unhookCallback(String callbackName) {
        AmxHook hook = hooks.stream().filter(hk -> hk.getName().equals(callbackName)).findAny().orElse(null);
        if (SampNativeFunction.unhookCallback(callbackName) && hook != null) {
            hooks.remove(hook);
            return true;
        }
        return false;
    }
}
