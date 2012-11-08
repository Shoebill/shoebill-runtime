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
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public class MethodSignature
{
	public static class MethodSignatureCache
	{
		private Map<Method, MethodSignature> cache;
		
		public MethodSignatureCache()
		{
			cache = new WeakHashMap<>();
		}
		
		public MethodSignature get(Method method)
		{
			MethodSignature signature;
			signature = cache.get(method);
			if(signature == null) signature = new MethodSignature(method);
			return signature;
		}
	}
	
	
	public final String name;
	public final Class<?> returnType;
	public final Class<?>[] parameterTypes;
	
	private transient final int hashCode;
	
	
	public MethodSignature(Method method)
	{
		name = method.getName();
		returnType = method.getReturnType();
		parameterTypes = method.getParameterTypes();
		
		hashCode = HashCodeBuilder.reflectionHashCode(this, false);
	}
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof MethodSignature == false) return false;
		MethodSignature sign = (MethodSignature) obj;
		
		if(sign.hashCode != hashCode) return false;
		if(sign.name.equals(name)) return false;
		if(sign.returnType != returnType) return false;
		if(Arrays.equals(sign.parameterTypes, parameterTypes) == false) return false;
		
		return true;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE, false);
	}
}
