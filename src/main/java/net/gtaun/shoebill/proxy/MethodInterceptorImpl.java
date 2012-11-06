package net.gtaun.shoebill.proxy;

import java.lang.reflect.Method;

import net.gtaun.shoebill.proxy.ProxyManagerImpl.ManageHelper;

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
