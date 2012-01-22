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

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.event.timer.TimerTickEvent;

/**
 * @author MK124
 *
 */

public class Timer implements ITimer
{
	private static final int COUNT_INFINITE = 0;
	

	private int interval, count;
	
	private boolean running;
	private int counting, realInterval;

	
	@Override public int getInterval()								{ return interval; }
	@Override public int getCount()									{ return count; }

	@Override public boolean isRunning()							{ return running; }
	

	public Timer( int interval )
	{
		this.interval = interval;
		this.count = COUNT_INFINITE;
		
		initialize();
	}
	
	public Timer( int interval, int count )
	{
		this.interval = interval;
		this.count = count;
		
		initialize();
	}
	
	private void initialize()
	{
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.putTimer( this );
	}
	
	
	@Override
	public void setInterval( int interval )
	{
		this.interval = interval;
	}
	
	@Override
	public void setCount( int count )
	{
		this.count = count;
	}
	
	@Override
	public void start()
	{
		counting = count;
		realInterval = 0;
		running = true;
	}
	
	@Override
	public void stop()
	{
		running = false;
	}

	@Override
	public void tick( int realint )
	{
		if( running == false ) return;
		
		realInterval += realint;
		if( realInterval < interval ) return;
		
		if( count > 0 ) counting--;
		TimerTickEvent event = new TimerTickEvent(this, realInterval);
		Shoebill.getInstance().getEventManager().dispatchEvent( event, this );
		
		realInterval = 0;
		if( count > 0 && counting == 0 ) stop();
	}
}
