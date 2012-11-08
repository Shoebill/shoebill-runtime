/**
 * Copyright (C) 2011-2012 MK124
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

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import net.gtaun.shoebill.proxy.MethodSignature.MethodSignatureCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author MK124
 */
public class MethodSignatureTest
{
	interface I
	{
		void method(int unknown);
	}
	
	class C implements I
	{
		@Override
		public void method(int unknown)
		{
			
		}
	}
	

	private Method m1, m2;
	
	
	public MethodSignatureTest()
	{
		
	}
	
	@Before
	public void setUp() throws Exception
	{
		m1 = I.class.getMethod("method", int.class);
		m2 = C.class.getMethod("method", int.class);
	}

	@After
	public void tearDown() throws Exception
	{
		
	}
	
	@Test
	public void testHashCodeAndEquals() throws NoSuchMethodException, SecurityException
	{
		MethodSignature ms1 = new MethodSignature(m1);
		MethodSignature ms2 = new MethodSignature(m2);
		
		assertEquals(ms1.hashCode(), ms2.hashCode());
		assertTrue(ms1.equals(ms2));
	}
	
	@Test
	public void testMethodSignatureCacheTest() throws NoSuchMethodException, SecurityException
	{
		MethodSignatureCache cache = new MethodSignatureCache();
		
		MethodSignature ms1 = cache.get(m1);
		MethodSignature ms2 = cache.get(m2);
		
		assertFalse(ms1 == ms2);
		assertTrue(ms1 == cache.get(m1));
		assertTrue(ms2 == cache.get(m2));
	}
}
