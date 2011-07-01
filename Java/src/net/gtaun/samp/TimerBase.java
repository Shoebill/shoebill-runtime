/**
 * Copyright (C) 2011 MK124
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

package net.gtaun.samp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;

import net.gtaun.event.EventDispatcher;
import net.gtaun.event.IEventDispatcher;
import net.gtaun.samp.event.TimerTickEvent;

/**
 * @author MK124
 *
 */

public class TimerBase
{
	public static final int COUNT_INFINITE = 0;
	
	
	int interval, count;
	
	boolean running;
	int counting, realInterval;

	public int interval()		{ return interval; }
	public int count()			{ return count; }

	public boolean running()	{ return running; }
	
	
	EventDispatcher<TimerTickEvent>		eventTick = new EventDispatcher<TimerTickEvent>();
	
	public IEventDispatcher<TimerTickEvent>		eventTick()		{ return eventTick; }
	

	public TimerBase( int interval )
	{
		this.interval = interval;
		this.count = COUNT_INFINITE;
		
		init();
	}
	
	public TimerBase( int interval, int count )
	{
		this.interval = interval;
		this.count = count;
		
		init();
	}
	
	private void init()
	{
		GameModeBase.instance.timerPool.add( new WeakReference<TimerBase>(this) );
	}
	
	public void finalize()
	{
		destroy();
	}

//---------------------------------------------------------
	
	protected int onTick( int counting, int realtime )
	{
		return 1;
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		Iterator<Reference<TimerBase>> iterator = GameModeBase.instance.timerPool.iterator();
		while( iterator.hasNext() )
		{
			Reference<TimerBase> reference = iterator.next();
			if( reference.get() != this ) continue;
			
			GameModeBase.instance.timerPool.remove( reference );
			return;
		}
	}
	
	public void setInterval( int interval )
	{
		this.interval = interval;
	}
	
	public void setCount( int count )
	{
		this.count = count;
	}
	
	public void start()
	{
		counting = count;
		realInterval = 0;
		running = true;
	}
	
	public void stop()
	{
		running = false;
	}

	
//---------------------------------------------------------
	
	void tick( int realint )
	{
		if( running == false ) return;
		
		realInterval += realint;
		if( realInterval < interval ) return;
		
		if( count > 0 ) counting--;
		onTick( counting, realInterval );
		eventTick.dispatchEvent( new TimerTickEvent(this, realInterval) );
		
		realInterval = 0;
		if( count > 0 && counting == 0 ) stop();
	}
}
