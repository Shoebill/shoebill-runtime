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
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * 
 * @author MK124
 */
public class GlobalProxyManagerImpl extends AbstractProxyManager implements GlobalProxyManager
{
	public GlobalProxyManagerImpl()
	{
		
	}
	
	@Override
	public Collection<MethodInterceptor> getMethodInterceptors(final Method method)
	{
		final Collection<Reference<MethodInterceptor>> methodInterceptors = methodMapInterceptors.get(method.getName());
		if (methodInterceptors == null) return null;
		
		return new AbstractCollection<MethodInterceptor>()
		{
			@Override
			public Iterator<MethodInterceptor> iterator()
			{
				return new Iterator<MethodInterceptor>()
				{
					Iterator<Reference<MethodInterceptor>> iterator = methodInterceptors.iterator();
					MethodInterceptor next = null;
					
					@Override
					public void remove()
					{
						throw new UnsupportedOperationException();
					}
					
					@Override
					public MethodInterceptor next()
					{
						if (next == null && hasNext() == false) throw new NoSuchElementException();
						
						MethodInterceptor ret = next;
						next = null;
						return ret;
					}
					
					@Override
					public boolean hasNext()
					{
						if (next != null) return true;
						
						while (true)
						{
							if (iterator.hasNext() == false) return false;
							next = iterator.next().get();
							
							if (next == null)
							{
								iterator.remove();
								continue;
							}

							return true;
						}
					}
				};
			}

			@Override
			public int size()
			{
				return methodInterceptors.size();
			}
		};
	}
}
