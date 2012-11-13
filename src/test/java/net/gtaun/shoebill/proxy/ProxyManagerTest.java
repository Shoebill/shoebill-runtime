package net.gtaun.shoebill.proxy;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import net.gtaun.shoebill.object.Proxyable;
import net.gtaun.shoebill.proxy.MethodInterceptor.Helper;
import net.gtaun.shoebill.proxy.MethodInterceptor.Interceptor;
import net.gtaun.shoebill.proxy.MethodInterceptor.InterceptorPriority;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProxyManagerTest
{
	static interface A extends Proxyable
	{
		int method(int unknown);
	}
	
	static abstract class B implements A
	{
		private int counter = 0;
		
		@Override
		public int method(int unknown)
		{
			return ++counter;
		}
	}

	
	public ProxyManagerTest()
	{
		
	}
	
	@Before
	public void setUp() throws Exception
	{
		
	}

	@After
	public void tearDown() throws Exception
	{
		
	}
	
	@Test
	public void testSingleMethodInterceptor() throws NoSuchMethodException, SecurityException
	{
		final TapableCounter counter = new TapableCounter();
		
		A a = ProxyManagerImpl.createProxyableFactory(B.class).create();
		
		Method method = A.class.getMethod("method", int.class);
		MethodInterceptor interceptor = a.getProxyManager().createMethodInterceptor(method, 
			new Interceptor()
			{
				@Override
				public Object intercept(Helper helper, Method method, Object obj, Object[] args) throws Throwable
				{
					counter.tap();
					return helper.invokeLower(obj, args);
				}
			}, InterceptorPriority.NORMAL);
		
		int ret = a.method(0);

		assertEquals(1, ret);
		assertEquals(1, counter.getTaps());
		
		interceptor.cancel();
		
		assertEquals(1, ret);
		assertEquals(1, counter.getTaps());
	}
}
