package net.gtaun.shoebill.proxy;

public class TapableCounter
{
	private int taps;
	
	
	public TapableCounter()
	{
		taps = 0;
	}
	
	public int tap()
	{
		taps++;
		return taps;
	}
	
	public int getTaps()
	{
		return taps;
	}
}
