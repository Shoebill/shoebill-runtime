package net.gtaun.shoebill.amx;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.event.amx.AmxLoadEvent;
import net.gtaun.shoebill.event.amx.AmxUnloadEvent;
import net.gtaun.util.event.EventManager;

public class AmxInstanceManagerImpl implements AmxInstanceManager
{
	private final Set<AmxInstance> instances;
	private final EventManager eventManager;
	
	
	public AmxInstanceManagerImpl(EventManager eventManager, int[] existedHandles)
	{
		this.eventManager = eventManager;
		instances = new HashSet<>();
		for (int handle : existedHandles)
		{
			AmxInstance instance = new AmxInstanceImpl(handle);
			instances.add(instance);
		}
	}
	
	public void onAmxLoad(int handle)
	{
		AmxInstance instance = new AmxInstanceImpl(handle);
		instances.add(instance);
		
		AmxLoadEvent event = new AmxLoadEvent(instance);
		eventManager.dispatchEvent(event, this);
	}

	public void onAmxUnload(int handle)
	{
		AmxInstance instance = new AmxInstanceImpl(handle);
		instances.remove(instance);
		
		AmxUnloadEvent event = new AmxUnloadEvent(instance);
		eventManager.dispatchEvent(event, this);
	}
	
	@Override
	public Set<AmxInstance> getAmxInstances()
	{
		return Collections.unmodifiableSet(instances);
	}
}
