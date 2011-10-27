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

package net.gtaun.shoebill.object;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;

import net.gtaun.lungfish.event.timer.TimerTickEvent;
import net.gtaun.lungfish.object.ITimer;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;

/**
 * @author MK124
 *
 */

public class Timer implements ITimer
{
	public static final int COUNT_INFINITE = 0;
	

	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int interval, count;
	
	boolean running;
	int counting, realInterval;

	
	public IEventDispatcher getEventDispatcher()	{ return eventDispatcher; }
	
	public int getInterval()						{ return interval; }
	public int getCount()							{ return count; }

	public boolean isRunning()						{ return running; }
	

	public Timer( int interval )
	{
		this.interval = interval;
		this.count = COUNT_INFINITE;
		
		init();
	}
	
	public Timer( int interval, int count )
	{
		this.interval = interval;
		this.count = count;
		
		init();
	}
	
	private void init()
	{
		Gamemode.instance.timerPool.add( new WeakReference<Timer>(this) );
	}
	
	protected void finalize()
	{
		destroy();
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		Iterator<Reference<Timer>> iterator = Gamemode.instance.timerPool.iterator();
		while( iterator.hasNext() )
		{
			Reference<Timer> reference = iterator.next();
			if( reference.get() != this ) continue;
			
			Gamemode.instance.timerPool.remove( reference );
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
		eventDispatcher.dispatchEvent( new TimerTickEvent(this, realInterval) );
		
		realInterval = 0;
		if( count > 0 && counting == 0 ) stop();
	}
}
