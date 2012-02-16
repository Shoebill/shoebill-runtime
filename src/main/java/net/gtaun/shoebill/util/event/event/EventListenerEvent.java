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

package net.gtaun.shoebill.util.event.event;

import net.gtaun.shoebill.util.event.Event;
import net.gtaun.shoebill.util.event.EventListenerEntry;
import net.gtaun.shoebill.util.event.IEventListener;

/**
 * @author MK124
 *
 */

public class EventListenerEvent extends Event
{
	private EventListenerEntry entry;
	

	public EventListenerEntry getEntry()			{ return entry; }
	
	
	public EventListenerEvent( EventListenerEntry entry )
	{
		this.entry = entry;
	}
	

	public Class<? extends Event> getType()
	{
		return entry.getType();
	}
	
	public Object getRelatedObject()
	{
		return entry.getRelatedObject();
	}
	
	public Class<?> getRelatedClass()
	{
		return entry.getRelatedClass();
	}
	
	public IEventListener getListener()
	{
		return entry.getListener();
	}
	
	public short getPriority()
	{
		return entry.getPriority();
	}
}
