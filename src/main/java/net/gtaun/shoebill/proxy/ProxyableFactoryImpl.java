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

package net.gtaun.shoebill.proxy;

import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.proxy.Enhancer;

/**
 * 
 * 
 * @author MK124
 */
public class ProxyableFactoryImpl<T extends Proxyable> implements ProxyableFactory<T>
{
	static
	{
		ProxyableFactory.Impl.implClass = ProxyableFactoryImpl.class;
	}
	
	
	private final Class<T> clazz;
	private final Enhancer enhancer;
	private GlobalProxyManager globalProxyManager;
	

	public ProxyableFactoryImpl(Class<T> clz)
	{
		this(clz, null);
	}
	
	public ProxyableFactoryImpl(Class<T> clz, GlobalProxyManager globalProxyManager)
	{
		this.clazz = clz;
		enhancer = new Enhancer();
		enhancer.setSuperclass(clz);
		this.globalProxyManager = globalProxyManager;
	}
	
	@Override
	public T create() throws Throwable
	{
		final ProxyManagerImpl proxyManager = new ProxyManagerImpl(globalProxyManager);
		synchronized (enhancer)
		{
			try
			{
				enhancer.setCallback(proxyManager.getCallback());
				return clazz.cast(enhancer.create());	
			}
			catch (CodeGenerationException e)
			{
				throw e.getCause();
			}
		}
	}

	@Override
	public T create(Class<?>[] paramTypes, Object... params) throws Throwable
	{
		final ProxyManagerImpl proxyManager = new ProxyManagerImpl(globalProxyManager);
		synchronized (enhancer)
		{
			try
			{
				enhancer.setCallback(proxyManager.getCallback());
				return clazz.cast(enhancer.create(paramTypes, params));
			}
			catch (CodeGenerationException e)
			{
				throw e.getCause();
			}
		}
	}
}
