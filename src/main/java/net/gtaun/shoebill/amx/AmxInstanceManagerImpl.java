package net.gtaun.shoebill.amx;

import net.gtaun.shoebill.event.amx.AmxLoadEvent;
import net.gtaun.shoebill.event.amx.AmxUnloadEvent;
import net.gtaun.util.event.EventManager;

import java.util.*;

public class AmxInstanceManagerImpl implements AmxInstanceManager {
    private final Set<AmxInstance> instances;
    private final EventManager eventManager;
    private final Map<AmxInstance, Set<AmxHook>> amxHooks;

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
    public Set<AmxHook> getAmxHooks(AmxInstance instance) {
        if (amxHooks.containsKey(instance))
            return Collections.unmodifiableSet(amxHooks.get(instance));
        return null;
    }
}
