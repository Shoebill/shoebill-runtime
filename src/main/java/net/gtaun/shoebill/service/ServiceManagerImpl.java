/**
 * Copyright (C) 2012 MK124
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

package net.gtaun.shoebill.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.gtaun.shoebill.event.service.ServiceRegisterEvent;
import net.gtaun.shoebill.event.service.ServiceUnregisterEvent;
import net.gtaun.shoebill.resource.Resource;
import net.gtaun.util.event.EventManager;

/**
 * 
 * 
 * @author MK124
 */
public class ServiceManagerImpl implements ServiceManager
{
	class ServiceEntryImpl implements ServiceEntry
	{
		private Resource resource;
		private Class<? extends Service> type;
		private Service service;
		
		<T extends Service> ServiceEntryImpl(Resource resource, Class<T> type, T service)
		{
			this.resource = resource;
			this.type = type;
			this.service = service;
		}
		
		@Override
		public Resource getProvider()
		{
			return resource;
		}
		
		@Override
		public Class<? extends Service> getType()
		{
			return type;
		}
		
		@Override
		public Service getService()
		{
			return service;
		}
	}
	
	
	private EventManager eventManager;
	private Map<Class<? extends Service>, ServiceEntry> services;
	
	
	public ServiceManagerImpl(EventManager eventManager)
	{
		this.eventManager = eventManager;
		services = new HashMap<>();
	}
	
	private void registerServiceEntry(ServiceEntry entry)
	{
		Class<? extends Service> type = entry.getType();
		
		ServiceEntry prevEntry = services.get(type);
		services.put(type, entry);
		
		if(prevEntry != null)
		{
			ServiceUnregisterEvent unregisterEvent = new ServiceUnregisterEvent(prevEntry);
			eventManager.dispatchEvent(unregisterEvent, this);
		}
		
		ServiceRegisterEvent event = new ServiceRegisterEvent(entry, prevEntry);
		eventManager.dispatchEvent(event, this);	
	}
	
	private void unregisterServiceEntry(ServiceEntry entry)
	{
		Class<? extends Service> type = entry.getType();
		services.remove(type);
		
		ServiceUnregisterEvent event = new ServiceUnregisterEvent(entry);
		eventManager.dispatchEvent(event, this);
	}

	@Override
	public <T extends Service> void registerService(Resource resource, Class<T> type, T service)
	{
		ServiceEntry entry = new ServiceEntryImpl(resource, type, service);
		registerServiceEntry(entry);
	}

	@Override
	public <T extends Service> void unregisterService(Resource resource, Class<T> type)
	{
		ServiceEntry entry = services.get(type);
		
		if(entry == null) return;
		if(entry.getProvider() != resource ) return;
		
		unregisterServiceEntry(entry);
	}
	
	@Override
	public <T extends Service> void unregisterServices(Resource resource)
	{
		Iterator<ServiceEntry> iterator = services.values().iterator();
		while(iterator.hasNext())
		{
			ServiceEntry entry = iterator.next();
			if(entry.getProvider() == resource) iterator.remove(); 
		}
	}
	
	@Override
	public <T extends Service> T getService(Class<T> type)
	{
		return type.cast(services.get(type).getService());
	}
	
	@Override
	public <T extends Service> ServiceEntry getServiceEnrty(Class<T> type)
	{
		return services.get(type);
	}
	
	@Override
	public Collection<ServiceEntry> getServiceEntries()
	{
		return services.values();
	}
}
