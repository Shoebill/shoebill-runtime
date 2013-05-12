package net.gtaun.shoebill.amx;

public class AmxInstanceImpl implements AmxInstance
{
	private final int handle;
	
	
	public AmxInstanceImpl(int handle)
	{
		this.handle = handle;
	}
	
	public int getHandle()
	{
		return handle;
	}
	
	@Override
	public AmxCallable getPublic(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public AmxCallable getNative(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void registerPublic(String name, AmxCallable callable)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void registerNative(String name, AmxCallable callable)
	{
		// TODO Auto-generated method stub
		
	}
}
