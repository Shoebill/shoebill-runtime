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

import java.lang.ref.Reference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import net.gtaun.shoebill.proxy.MethodInterceptor.Helper;
import net.gtaun.shoebill.proxy.MethodInterceptor.Interceptor;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * 
 * @author MK124
 */
public class ProxyManagerImpl extends AbstractProxyManager implements ProxyManager
{
	private static final String METHOD_NAME_GET_PROXY_MANAGER = "getProxyManager";
	
	
	private final Callback callback;

	
	ProxyManagerImpl(final GlobalProxyManager globalProxyManager)
	{
		callback = new net.sf.cglib.proxy.MethodInterceptor()
		{
			@Override
			public Object intercept(Object obj, final Method method, Object[] args, final MethodProxy proxy) throws Throwable
			{
				final String methodName = method.getName();
				if(methodName.equals(METHOD_NAME_GET_PROXY_MANAGER)) return ProxyManagerImpl.this;
				
				Collection<Reference<MethodInterceptor>> interceptors = methodMapInterceptors.get(methodName);
				
				Collection<MethodInterceptor> globalInterceptors = null;
				if (globalProxyManager != null) globalInterceptors = globalProxyManager.getMethodInterceptors(method);
				
				if (interceptors == null && globalInterceptors == null)
				{
					return proxy.invokeSuper(obj, args);
				}
				
				int capacity = 0;
				if (interceptors != null) capacity += interceptors.size();
				if (globalInterceptors != null) capacity += globalInterceptors.size();
				
				final Queue<MethodInterceptor> interceptorQueue = new PriorityQueue<>(capacity, METHOD_INTERCEOTOR_COMPARTOR);
				
				if (interceptors != null)
				{
					Iterator<Reference<MethodInterceptor>> iterator = interceptors.iterator();
					while(iterator.hasNext())
					{
						MethodInterceptor interceptor = iterator.next().get();
						if(interceptor == null)
						{
							iterator.remove();
							continue;
						}

						Method m = interceptor.getMethod();
						if(method.getReturnType() != m.getReturnType()) continue;
						if(Arrays.equals(method.getParameterTypes(), m.getParameterTypes()) == false) continue;
						interceptorQueue.add(interceptor);
					}
				}
				
				if (globalInterceptors != null)
				{
					Iterator<MethodInterceptor> iterator = globalInterceptors.iterator();
					while(iterator.hasNext())
					{
						MethodInterceptor interceptor = iterator.next();
						if (interceptor.getMethod().getDeclaringClass().isInstance(obj)) interceptorQueue.add(interceptor);
					}
				}
				
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
							interceptor = methodInterceptor.getInterceptor();
						}
						
						if(interceptor == null) return invokeOriginal(obj, args);
						return interceptor.intercept(this, method, obj, args);
					}
				};
				
				return helper.invokeLower(obj, args);
			}
		};
	}
	
	Callback getCallback()
	{
		return callback;
	}
}
