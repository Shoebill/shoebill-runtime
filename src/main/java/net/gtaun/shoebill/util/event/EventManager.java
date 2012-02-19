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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtaun.shoebill.util.event.event.EventListenerAddedEvent;
import net.gtaun.shoebill.util.event.event.EventListenerRemovedEvent;

/**
 * @author MK124
 *
 */

public class EventManager implements IEventManager
{
	private Map<Class<? extends Event>, Map<Object, Queue<EventListenerEntry>>> listenerEntryContainersMap;
	
	
	public EventManager()
	{
		listenerEntryContainersMap = new HashMap<Class<? extends Event>, Map<Object, Queue<EventListenerEntry>>>();
	}
	

	@Override
	public EventListenerEntry addListener( Class<? extends Event> type, IEventListener listener, EventListenerPriority priority )
	{
		return addListener( type, Object.class, listener, priority.getValue() );
	}
	
	@Override
	public EventListenerEntry addListener( Class<? extends Event> type, IEventListener listener, short priority )
	{
		return addListener( type, Object.class, listener, priority );
	}
	
	@Override
	public EventListenerEntry addListener( Class<? extends Event> type, Class<?> relatedClass, IEventListener listener, EventListenerPriority priority )
	{
		return addListener( type, (Object)relatedClass, listener, priority.getValue() );
	}

	@Override
	public EventListenerEntry addListener( Class<? extends Event> type, Class<?> relatedClass, IEventListener listener, short customPriority )
	{
		return addListener( type, (Object)relatedClass, listener, customPriority );
	}

	@Override
	public EventListenerEntry addListener( Class<? extends Event> type, Object relatedObject, IEventListener listener, EventListenerPriority priority )
	{
		return addListener( type, relatedObject, listener, priority.getValue() );
	}

	@Override
	public EventListenerEntry addListener( Class<? extends Event> type, Object relatedObject, IEventListener listener, short customPriority )
	{
		EventListenerEntry entry = new EventListenerEntry( type, relatedObject, listener, customPriority );
		return addListener(entry);
	}
	
	@Override
	public EventListenerEntry addListener( EventListenerEntry entry )
	{
		Class<? extends Event> type = entry.getType();
		Object relatedObject = entry.getRelatedObject();
		IEventListener listener = entry.getListener();
		
		Map<Object, Queue<EventListenerEntry>> objectListenerEntries = listenerEntryContainersMap.get(type);
		if( objectListenerEntries == null )
		{
			objectListenerEntries = new HashMap<Object, Queue<EventListenerEntry>>();
			listenerEntryContainersMap.put( type, objectListenerEntries );
		}
		
		Queue<EventListenerEntry> entries = objectListenerEntries.get(relatedObject);
		if( entries == null )
		{
			entries = new ConcurrentLinkedQueue<EventListenerEntry>();
			objectListenerEntries.put( relatedObject, entries );
		}
		
		for( EventListenerEntry e : entries )
		{
			if( e.getListener() != listener ) continue;
			removeListener( type, relatedObject, listener );
		}
		
		entries.add( entry );
		
		EventListenerAddedEvent event = new EventListenerAddedEvent(entry);
		dispatchEvent( event, this );
		
		return entry;
	}


	@Override
	public void removeListener( Class<? extends Event> type, IEventListener listener )
	{
		removeListener( type, Object.class, listener );
	}
	
	@Override
	public void removeListener( Class<? extends Event> type, Class<?> clz, IEventListener listener )
	{
		removeListener( type, (Object)clz, listener );
	}
	
	@Override
	public void removeListener( Class<? extends Event> type, Object relatedObject, IEventListener listener )
	{
		Map<Object, Queue<EventListenerEntry>> objectListenerEntries = listenerEntryContainersMap.get(type);
		if( objectListenerEntries == null ) return;
		
		Queue<EventListenerEntry> entries = objectListenerEntries.get(relatedObject);
		if( entries == null ) return;
		
		for( EventListenerEntry entry : entries )
		{
			if( entry.getListener() != listener ) continue;
			entries.remove( entry );
			
			EventListenerRemovedEvent event = new EventListenerRemovedEvent(entry);
			dispatchEvent( event, this );
		}
		
		if( entries.size() == 0 ) objectListenerEntries.remove( relatedObject );
		if( objectListenerEntries.size() == 0 ) listenerEntryContainersMap.remove( type );
	}
	
	@Override
	public void removeListener( EventListenerEntry entry )
	{
		Class<? extends Event> type = entry.getType();
		Object relatedObject = entry.getRelatedObject();
		
		Map<Object, Queue<EventListenerEntry>> objectListenerEntries = listenerEntryContainersMap.get(type);
		if( objectListenerEntries == null ) return;
		
		Queue<EventListenerEntry> entries = objectListenerEntries.get(relatedObject);
		if( entries == null ) return;
		
		for( EventListenerEntry e : entries )
		{
			if( e != entry ) continue;	
			entries.remove( entry );
			
			EventListenerRemovedEvent event = new EventListenerRemovedEvent(entry);
			dispatchEvent( event, this );
			break;
		}
		
		if( entries.size() == 0 ) objectListenerEntries.remove( relatedObject );
		if( objectListenerEntries.size() == 0 ) listenerEntryContainersMap.remove( type );
	}
	

	@Override
	public boolean hasListener( Class<? extends Event> type, Class<?> clz )
	{
		return hasListener( type, (Object)clz );
	}
	
	@Override
	public boolean hasListener( Class<? extends Event> type, Class<?> clz, IEventListener listener )
	{
		return hasListener( type, (Object)clz, listener );
	}
	
	@Override
	public boolean hasListener( Class<? extends Event> type, Object object )
	{
		Map<Object, Queue<EventListenerEntry>> objectListenerEntries = listenerEntryContainersMap.get(type);
		if( objectListenerEntries == null ) return false;
		
		Queue<EventListenerEntry> entries = objectListenerEntries.get(object);
		if( entries == null ) return false;
		
		return true;
	}

	@Override
	public boolean hasListener( Class<? extends Event> type, Object object, IEventListener listener )
	{
		Map<Object, Queue<EventListenerEntry>> objectListenerEntries = listenerEntryContainersMap.get(type);
		if( objectListenerEntries == null ) return false;
		
		Queue<EventListenerEntry> entries = objectListenerEntries.get(object);
		if( entries == null ) return false;
		
		for( EventListenerEntry entry : entries )
		{
			if( entry.getListener() == listener ) return true;
		}
		
		return false;
	}
	
	@Override
	public boolean hasListener( EventListenerEntry entry )
	{
		Class<? extends Event> type = entry.getType();
		Object relatedObject = entry.getRelatedObject();
		
		Map<Object, Queue<EventListenerEntry>> objectListenerEntries = listenerEntryContainersMap.get(type);
		if( objectListenerEntries == null ) return false;
		
		Queue<EventListenerEntry> entries = objectListenerEntries.get(relatedObject);
		if( entries == null ) return false;
		
		for( EventListenerEntry e : entries )
		{
			if( e == entry ) return true;
		}
		
		return false;
	}
	
	
	@Override
	public <T extends Event> void dispatchEvent( T event, Object ...objects )
	{
		if( objects.length == 1 && objects[0] instanceof Object[] ) objects = (Object[]) objects[0];
		
		Class<? extends Event> type = event.getClass();
		PriorityQueue<EventListenerEntry> listenerEntryQueue = new PriorityQueue<EventListenerEntry>( 16,
			new Comparator<EventListenerEntry>()
			{
				public int compare( EventListenerEntry o1, EventListenerEntry o2 )
				{
					return o2.getPriority() - o1.getPriority();
				}
			}
		);
		
		final Object obj = new Object();
		if( objects.length == 0 ) objects = new Object[] { obj };
		for( Object object : objects )
		{
			Class<?> cls = object.getClass();
			
			Map<Object, Queue<EventListenerEntry>> objectListenerEntries = listenerEntryContainersMap.get(type);
			if( objectListenerEntries == null ) return;
			
			Queue<EventListenerEntry> entries = objectListenerEntries.get( object );
			if( entries != null ) for( EventListenerEntry entry : entries )
			{
				if( entry.getListener() == null ) entries.remove( entry );
				else listenerEntryQueue.add( entry );
			}

			Class<?>[] interfaces = cls.getInterfaces();
			for( Class<?> clz : interfaces )
			{
				Queue<EventListenerEntry> classListenerEntries = objectListenerEntries.get( clz );
				if( classListenerEntries != null ) for( EventListenerEntry entry : classListenerEntries )
				{
					if( entry.getListener() == null ) entries.remove( entry );
					else listenerEntryQueue.add( entry );
				}
			}
			
			Class<?> clz = cls;
			while( clz != null )
			{
				Queue<EventListenerEntry> classListenerEntries = objectListenerEntries.get( clz );
				if( classListenerEntries != null ) for( EventListenerEntry entry : classListenerEntries )
				{
					if( entry.getListener() == null ) entries.remove( entry );
					else listenerEntryQueue.add( entry );
				}
				
				clz = clz.getSuperclass();
			}
		}
		
		while( listenerEntryQueue.isEmpty() == false && event.isInterrupted() == false )
		{
			EventListenerEntry entry = listenerEntryQueue.poll();
			IEventListener listener = entry.getListener();
			
			if( listener == null ) continue;
			
			try
			{
				listener.handleEvent( event );	
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
}
