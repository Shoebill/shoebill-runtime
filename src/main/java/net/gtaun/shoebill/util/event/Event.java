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

package net.gtaun.shoebill.util.event;

/**
 * @author MK124
 *
 */

public abstract class Event
{
	protected int result;
	
	boolean interruptable;
	boolean interrupted;
	
	boolean cancelable;
	boolean canceled;
	
	
	public Event()
	{
		this.interruptable = true;
		this.cancelable = false;
	}
	
	public Event( boolean interruptable, boolean cancelable )
	{
		this.cancelable = cancelable;
	}

	public int getResult()
	{
		return result;
	}
	public void setResult( int result )
	{
		this.result = result;
	}
	
	public boolean isInterruptable()
	{
		return interruptable;
	}
	public boolean interrupt()
	{
		interrupted = interruptable;
		return interrupted;
	}
	
	public boolean isCancelable()
	{
		return cancelable;
	}
	public boolean cancel()
	{
		canceled = cancelable;
		return canceled;
	}
	
	protected void onCancel()
	{
		
	}
}