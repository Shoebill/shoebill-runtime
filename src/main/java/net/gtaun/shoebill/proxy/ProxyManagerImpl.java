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
import java.util.Arrays;
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
	private static final String METHOD_NAME_GET_PROXY_MANAGER = "getProxyManager";
	
	public static <T extends Proxyable> ProxyableFactory<T> createProxyableFactory(final Class<T> clz)
	{
		return new ProxyableFactory<T>()
		{
			private final Enhancer enhancer;
			
			{
				enhancer = new Enhancer();
				enhancer.setSuperclass(clz);
			}
			
			@Override
			public T create()
			{
				final ProxyManagerImpl proxyManager = new ProxyManagerImpl();
				synchronized (enhancer)
				{
					enhancer.setCallback(proxyManager.callback);
					return clz.cast(enhancer.create());
				}
			}

			@Override
			public T create(Class<?>[] paramTypes, Object... params)
			{
				final ProxyManagerImpl proxyManager = new ProxyManagerImpl();
				synchronized (enhancer)
				{
					enhancer.setCallback(proxyManager.callback);
					return clz.cast(enhancer.create(paramTypes, params));
				}
			}
		};
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
			return o2.getPriority() - o1.getPriority();
		}
	};
	
	
	private final Callback callback;
	private final Map<String, Collection<MethodInterceptor>> methodMapInterceptors;
	private final ManageHelper manageHelper;
	
	
	private ProxyManagerImpl()
	{
		callback = new net.sf.cglib.proxy.MethodInterceptor()
		{
			@Override
			public Object intercept(Object obj, final Method method, Object[] args, final MethodProxy proxy) throws Throwable
			{
				final String methodName = method.getName();
				if(methodName.equals(METHOD_NAME_GET_PROXY_MANAGER)) return ProxyManagerImpl.this;
				
				Collection<MethodInterceptor> interceptors = methodMapInterceptors.get(methodName);
				if (interceptors == null)
				{
					return proxy.invokeSuper(obj, args);
				}
				
				final Queue<MethodInterceptor> interceptorQueue = new PriorityQueue<>(interceptors.size(), METHOD_INTERCEOTOR_COMPARTOR);
				interceptorQueue.addAll(interceptors);
				
				final Helper helper = new Helper()
				{
					@Override
					public Object invokeOriginal(Object obj, Object[] args) throws Throwable
					{
						return proxy.invokeSuper(obj, args);
					}
					
					@Override
					public Object invokeLower(Object obj, Object[] args) throws Throwable
					{
						Interceptor interceptor = null;
						while(interceptorQueue.isEmpty() == false && interceptor == null)
						{
							MethodInterceptor methodInterceptor = interceptorQueue.poll();
							Method m = methodInterceptor.getMethod();
							
							if(method.getReturnType() != m.getReturnType()) continue;
							if(Arrays.equals(method.getParameterTypes(), m.getParameterTypes()) == false) continue;
							
							interceptor = methodInterceptor.getInterceptor();
						}
						
						if(interceptor == null) return invokeOriginal(obj, args);
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
		final String methodName = method.getName();
		
		Collection<MethodInterceptor> methodInterceptors = methodMapInterceptors.get(methodName);
		if (methodInterceptors == null)
		{
			methodInterceptors = new ConcurrentLinkedQueue<>();
			methodMapInterceptors.put(methodName, methodInterceptors);
		}
		
		methodInterceptors.add(methodInterceptor);
	}
	
	public void removeMethodInterceptor(MethodInterceptor methodInterceptor)
	{
		final Method method = methodInterceptor.getMethod();
		final String methodName = method.getName();
		
		Collection<MethodInterceptor> methodInterceptors = methodMapInterceptors.get(methodName);
		
		methodInterceptors.remove(methodInterceptor);
		if (methodInterceptors.isEmpty())
		{
			methodMapInterceptors.remove(methodName);
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
