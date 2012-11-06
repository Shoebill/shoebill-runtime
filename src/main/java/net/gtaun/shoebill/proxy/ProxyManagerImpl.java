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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtaun.shoebill.object.Proxyable;
import net.gtaun.shoebill.proxy.MethodInterceptor.Helper;
import net.gtaun.shoebill.proxy.MethodInterceptor.Interceptor;
import net.gtaun.shoebill.proxy.MethodInterceptor.Priority;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * 
 * @author MK124
 */
public class ProxyManagerImpl implements ProxyManager
{
	private static final Callback PROXYABLE_METHOD_INTERCEPTOR = new net.sf.cglib.proxy.MethodInterceptor()
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
	
	public class ManageHelper
	{
		public void cancel(MethodInterceptor methodInterceptor)
		{
			removeMethodInterceptor(methodInterceptor);
		}
	}
	
	private static final Comparator<MethodInterceptor> METHOD_INTERCEOTOR_COMPARTOR = new Comparator<MethodInterceptor>()
	{
		@Override
		public int compare(MethodInterceptor o1, MethodInterceptor o2)
		{
			return o2.getPriority()-o1.getPriority();
		}
	};
	
	
	private final net.sf.cglib.proxy.MethodInterceptor interceptor;
	private final Map<Method, Collection<MethodInterceptor>> methodMapInterceptors;
	private final ManageHelper manageHelper;
	
	
	public ProxyManagerImpl()
	{
		interceptor = new net.sf.cglib.proxy.MethodInterceptor()
		{
			@Override
			public Object intercept(Object obj, final Method method, Object[] args, final MethodProxy proxy) throws Throwable
			{
				Collection<MethodInterceptor> interceptors = methodMapInterceptors.get(method);
				if(interceptors == null)
				{
					return proxy.invoke(obj, args);
				}
				
				final Queue<MethodInterceptor> interceptorQueue = new PriorityQueue<>(interceptors.size(), METHOD_INTERCEOTOR_COMPARTOR);
				interceptorQueue.addAll(interceptors);
				
				final Helper helper = new Helper()
				{
					@Override
					public Object invokeOriginal(Object obj, Object[] args) throws Throwable
					{
						return proxy.invoke(obj, args);
					}
					
					@Override
					public Object invokeLower(Object obj, Object[] args) throws Throwable
					{
						if(interceptorQueue.isEmpty()) return invokeOriginal(obj, args);

						MethodInterceptor methodInterceptor = interceptorQueue.poll();
						Interceptor interceptor = methodInterceptor.getInterceptor();
						
						return interceptor.intercept(this, method, obj, args);
					}
				};
				
				return helper.invokeLower(obj, args);
			}
		};
		
		methodMapInterceptors = new HashMap<>();
		manageHelper = new ManageHelper();
	}
	
	public void addMethodInterceptor(MethodInterceptor methodInterceptor)
	{
		final Method method = methodInterceptor.getMethod();
		Collection<MethodInterceptor> methodInterceptors = methodMapInterceptors.get(method);
		if(methodInterceptors == null)
		{
			methodInterceptors = new ConcurrentLinkedQueue<>();
			methodMapInterceptors.put(method, methodInterceptors);
		}
		
		methodInterceptors.add(methodInterceptor);
	}
	
	public void removeMethodInterceptor(MethodInterceptor methodInterceptor)
	{
		final Method method = methodInterceptor.getMethod();
		Collection<MethodInterceptor> methodInterceptors = methodMapInterceptors.get(method);
		
		methodInterceptors.remove(methodInterceptor);
		if(methodInterceptors.isEmpty())
		{
			methodMapInterceptors.remove(method);
		}
	}

	@Override
	public MethodInterceptor createMethodInterceptor(Method method, Interceptor interceptor, Priority priority)
	{
		return createMethodInterceptor(method, interceptor, priority.getValue());
	}

	@Override
	public MethodInterceptor createMethodInterceptor(Method method, Interceptor interceptor, short priority)
	{
		MethodInterceptor methodInterceptor = new MethodInterceptorImpl(manageHelper, method, interceptor, priority);
		addMethodInterceptor(methodInterceptor);
		return methodInterceptor;
	}
}
