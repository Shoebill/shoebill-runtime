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

public class EventListenerEntry
{
	private Class<? extends Event> type;
	private Object relatedObject;
	private IEventListener listener;
	private short priority;

	public Class<? extends Event> getType()			{ return type; }
	public Object getRelatedObject()				{ return relatedObject; }
	public IEventListener getListener()				{ return listener; }
	public short getPriority()						{ return priority; }
	
	
	public EventListenerEntry( Class<? extends Event> type, Object relatedObject, IEventListener listener, short priority )
	{
		this.type = type;
		this.relatedObject = relatedObject;
		this.listener = listener;
		this.priority = priority;
	}
	
	
	public Class<?> getRelatedClass()
	{
		if( relatedObject instanceof Class ) return (Class<?>) relatedObject;
		return null;
	}
}
