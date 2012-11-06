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

import net.gtaun.shoebill.proxy.ProxyManagerImpl.ManageHelper;

/**
 * 
 * 
 * @author MK124
 */
public class MethodInterceptorImpl implements MethodInterceptor
{
	private final ManageHelper manageHelper;
	private final Method method;
	private final Interceptor interceptor;
	private final short priority;
	
	
	public MethodInterceptorImpl(ManageHelper manageHelper, Method method, Interceptor interceptor, short priority)
	{
		this.manageHelper = manageHelper;
		this.method = method;
		this.interceptor = interceptor;
		this.priority = priority;
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
		manageHelper.cancel(this);
	}
}
