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
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtaun.shoebill.proxy.MethodInterceptor.Interceptor;
import net.gtaun.shoebill.proxy.MethodInterceptor.InterceptorPriority;

/**
 * 
 * 
 * @author MK124
 */
public abstract class AbstractProxyManager implements ProxyManager
{
	public class MethodInterceptorImpl implements MethodInterceptor
	{
		private final Method method;
		private final Interceptor interceptor;
		private final short priority;
		
		public MethodInterceptorImpl(Method method, Interceptor interceptor, short priority)
		{
			this.method = method;
			this.interceptor = interceptor;
			this.priority = priority;
		}
		
		@Override
		protected void finalize() throws Throwable
		{
			super.finalize();
			removeMethodInterceptor(this);
		}
		
		@Override
		public Method getMethod()
		{
			return method;
		}
		
		@Override
		public Interceptor getInterceptor()
		{
			return interceptor;
		}
		
		@Override
		public short getPriority()
		{
			return priority;
		}
		
		@Override
		public void cancel()
		{
			removeMethodInterceptor(this);
		}
	}

	protected static final Comparator<MethodInterceptor> METHOD_INTERCEOTOR_COMPARTOR = new Comparator<MethodInterceptor>()
	{
		@Override
		public int compare(MethodInterceptor o1, MethodInterceptor o2)
		{
			return o2.getPriority() - o1.getPriority();
		}
	};
	
	
	protected final Map<String, Collection<Reference<MethodInterceptor>>> methodMapInterceptors;
	
	
	protected AbstractProxyManager()
	{
		methodMapInterceptors = new ConcurrentHashMap<>(256, 0.5f);
	}
	
	private void addMethodInterceptor(MethodInterceptor methodInterceptor)
	{
		final Method method = methodInterceptor.getMethod();
		final String methodName = method.getName();
		
		Collection<Reference<MethodInterceptor>> methodInterceptors = methodMapInterceptors.get(methodName);
		if (methodInterceptors == null)
		{
			methodInterceptors = new ConcurrentLinkedQueue<>();
			methodMapInterceptors.put(methodName, methodInterceptors);
		}
		
		methodInterceptors.add(new WeakReference<MethodInterceptor>(methodInterceptor));
	}
	
	private void removeMethodInterceptor(MethodInterceptor methodInterceptor)
	{
		final Method method = methodInterceptor.getMethod();
		final String methodName = method.getName();
		
		Collection<Reference<MethodInterceptor>> methodInterceptors = methodMapInterceptors.get(methodName);
		Iterator<Reference<MethodInterceptor>> iterator = methodInterceptors.iterator();
		
		while(iterator.hasNext())
		{
			MethodInterceptor value = iterator.next().get();
			if(value == methodInterceptor || value == null)
			{
				iterator.remove();
			}
		}
		
		if (methodInterceptors.isEmpty())
		{
			methodMapInterceptors.remove(methodName);
		}
	}
	
	@Override
	public MethodInterceptor createMethodInterceptor(Method method, Interceptor interceptor, InterceptorPriority priority)
	{
		return createMethodInterceptor(method, interceptor, priority.getValue());
	}
	
	@Override
	public MethodInterceptor createMethodInterceptor(Method method, Interceptor interceptor, short priority)
	{
		MethodInterceptor methodInterceptor = new MethodInterceptorImpl(method, interceptor, priority);
		addMethodInterceptor(methodInterceptor);
		return methodInterceptor;
	}
}
