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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.ShoebillLowLevel;
import net.gtaun.shoebill.event.timer.TimerTickEvent;
import net.gtaun.shoebill.object.Timer;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public abstract class TimerImpl implements Timer
{
	private int interval, count;
	
	private boolean running;
	private int counting, realInterval;
	
	
	public TimerImpl(int interval, int count)
	{
		initialize(interval, count);
	}
	
	private void initialize(int interval, int count)
	{
		this.interval = interval;
		this.count = count;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public int getInterval()
	{
		return interval;
	}
	
	@Override
	public void setInterval(int interval)
	{
		this.interval = interval;
	}
	
	@Override
	public int getCount()
	{
		return count;
	}
	
	@Override
	public void setCount(int count)
	{
		this.count = count;
	}
	
	@Override
	public boolean isRunning()
	{
		return running;
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
	public void tick(int realint)
	{
		if (running == false) return;
		
		realInterval += realint;
		if (realInterval < interval) return;
		
		if (count > 0) counting--;
		TimerTickEvent event = new TimerTickEvent(this, realInterval);
		ShoebillLowLevel shoebillLowLevel = ShoebillImpl.getInstance();
		shoebillLowLevel.getEventManager().dispatchEvent(event, this);
		
		realInterval = 0;
		if (count > 0 && counting == 0) stop();
	}
}
