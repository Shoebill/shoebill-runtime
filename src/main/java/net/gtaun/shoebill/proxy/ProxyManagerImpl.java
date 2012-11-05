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

import java.lang.reflect.Method;

import net.gtaun.shoebill.object.Proxyable;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * 
 * @author MK124
 */
public class ProxyManagerImpl implements ProxyManager
{
	private static final Callback PROXYABLE_METHOD_INTERCEPTOR = new MethodInterceptor()
	{
		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
		{
			Proxyable proxyable = (Proxyable)obj;
			ProxyManagerImpl manager = (ProxyManagerImpl) proxyable.getProxyManager();
			return manager.interceptor.intercept(obj, method, args, proxy);
		}
	};
	
	public static Enhancer createProxyableFactory(Class<? extends Proxyable> clz)
	{
		Enhancer factory = new Enhancer();
		factory.setSuperclass(clz);
		factory.setCallback(PROXYABLE_METHOD_INTERCEPTOR);
		return factory;
	}
	
	
	private MethodInterceptor interceptor;
	
	
	public ProxyManagerImpl()
	{
		
	}
}
