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
import java.util.Map;

/**
 * 
 * 
 * @author MK124
 */
public class ServiceManagerImpl implements ServiceManager
{
	private Map<Class<? extends Service>, Service> services;
	
	
	public ServiceManagerImpl()
	{
		services = new HashMap<>();
	}

	@Override
	public <T extends Service> void registerService(Class<T> type, T service)
	{
		services.put(type, service);
	}

	@Override
	public void unregisterService(Class<? extends Service> type)
	{
		services.remove(type);
	}
	
	@Override
	public <T extends Service> T getService(Class<T> type)
	{
		return type.cast(services.get(type));
	}
	
	@Override
	public Collection<Service> getServices()
	{
		return services.values();
	}
	
	@Override
	public Collection<Class<? extends Service>> getServiceTypes()
	{
		return services.keySet();
	}
}
