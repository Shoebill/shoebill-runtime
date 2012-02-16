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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

import net.gtaun.shoebill.util.event.event.EventListenerAddedEvent;
import net.gtaun.shoebill.util.event.event.EventListenerRemovedEvent;

/**
 * @author MK124
 *
 */

public class EventManager implements IEventManager
{
	class Entry<V>
	{
		short priority;
		Reference<V> reference;
		
		Entry( short priority, V value )
		{
			this.priority = priority;
			this.reference = new WeakReference<V>( value );
		}
		
		short getPriority()
		{
			return priority;
		}
		
		V getValue()
		{
			return reference.get();
		}
	}
	
	
	private Map<Class<? extends Event>, Map<Object, List<Entry<IEventListener>>>> objectListenerContainersMap;
	
	
	public EventManager()
	{
		objectListenerContainersMap = new HashMap<Class<? extends Event>, Map<Object, List<Entry<IEventListener>>>>();
	}
	

	@Override
	public <T extends Event> void addListener( Class<T> type, IEventListener listener, EventListenerPriority priority )
	{
		addListener( type, Object.class, listener, priority.getValue() );
	}
	
	@Override
	public <T extends Event> void addListener( Class<T> type, IEventListener listener, short priority )
	{
		addListener( type, Object.class, listener, priority );
	}
	
	@Override
	public <T extends Event> void addListener( Class<T> type, Class<?> relatedClass, IEventListener listener, EventListenerPriority priority )
	{
		addListener( type, (Object)relatedClass, listener, priority.getValue() );
	}

	@Override
	public <T extends Event> void addListener( Class<T> type, Class<?> relatedClass, IEventListener listener, short customPriority )
	{
		addListener( type, (Object)relatedClass, listener, customPriority );
	}

	@Override
	public <T extends Event> void addListener( Class<T> type, Object relatedObject, IEventListener listener, EventListenerPriority priority )
	{
		addListener( type, relatedObject, listener, priority.getValue() );
	}

	@Override
	public <T extends Event> void addListener( Class<T> type, Object relatedObject, IEventListener listener, short customPriority )
	{
		Map<Object, List<Entry<IEventListener>>> objectListeners = objectListenerContainersMap.get(type);
		if( objectListeners == null )
		{
			objectListeners = new HashMap<Object, List<Entry<IEventListener>>>();
			objectListenerContainersMap.put( type, objectListeners );
		}
		
		List<Entry<IEventListener>> listeners = objectListeners.get(relatedObject);
		if( listeners == null )
		{
			listeners = new Vector<Entry<IEventListener>>();
			objectListeners.put( relatedObject, listeners );
		}
		
		for( int i=0; i<listeners.size(); i++ )
		{
			if( listeners.get(i).getValue() != listener ) continue;
			removeListener( type, relatedObject, listener );
		}
		
		Entry<IEventListener> entry = new Entry<IEventListener>( customPriority, listener );
		listeners.add( entry );
		
		EventListenerAddedEvent event = new EventListenerAddedEvent( type, relatedObject, listener, customPriority );
		dispatchEvent( event, this );
	}


	@Override
	public <T extends Event> void removeListener( Class<T> type, IEventListener listener )
	{
		removeListener( type, Object.class, listener );
	}
	
	@Override
	public <T extends Event> void removeListener( Class<T> type, Class<?> clz, IEventListener listener )
	{
		removeListener( type, (Object)clz, listener );
	}
	
	@Override
	public <T extends Event> void removeListener( Class<T> type, Object object, IEventListener listener )
	{
		Map<Object, List<Entry<IEventListener>>> objectListeners = objectListenerContainersMap.get(type);
		if( objectListeners == null ) return;
		
		List<Entry<IEventListener>> listeners = objectListeners.get(object);
		if( listeners == null ) return;
		
		for( int i=0; i<listeners.size(); i++ )
		{
			Entry<IEventListener> entry = listeners.get(i);
			if( entry.getValue() != listener ) continue;
			
			listeners.remove( i );
			
			EventListenerRemovedEvent event = new EventListenerRemovedEvent( type, object, listener, entry.getPriority() );
			dispatchEvent( event, this );
		}
		
		if( listeners.size() == 0 ) objectListeners.remove( listeners );
		if( objectListeners.size() == 0 ) objectListeners.remove( listener );
	}
	

	@Override
	public <T extends Event> boolean hasListener( Class<T> type, Class<?> clz )
	{
		return hasListener( type, (Object)clz );
	}
	
	@Override
	public <T extends Event> boolean hasListener( Class<T> type, Class<?> clz, IEventListener listener )
	{
		return hasListener( type, (Object)clz, listener );
	}
	
	@Override
	public <T extends Event> boolean hasListener( Class<T> type, Object object )
	{
		Map<Object, List<Entry<IEventListener>>> objectListeners = objectListenerContainersMap.get(type);
		if( objectListeners == null ) return false;
		
		List<Entry<IEventListener>> listeners = objectListeners.get(object);
		if( listeners == null ) return false;
		
		return true;
	}

	@Override
	public <T extends Event> boolean hasListener( Class<T> type, Object object, IEventListener listener )
	{
		Map<Object, List<Entry<IEventListener>>> objectListeners = objectListenerContainersMap.get(type);
		if( objectListeners == null ) return false;
		
		List<Entry<IEventListener>> listeners = objectListeners.get(object);
		if( listeners == null ) return false;
		
		for( int i=0; i<listeners.size(); i++ )
		{
			Entry<IEventListener> reference = listeners.get(i);
			if( reference.getValue() == listener ) return true;
		}
		
		return false;
	}
	
	
	@Override
	public <T extends Event> void dispatchEvent( T event, Object ...objects )
	{
		Class<? extends Event> type = event.getClass();
		PriorityQueue<Entry<IEventListener>> listenerEntryQueue = new PriorityQueue<Entry<IEventListener>>( 16,
			new Comparator<Entry<IEventListener>>()
			{
				public int compare( Entry<IEventListener> o1, Entry<IEventListener> o2 )
				{
					return o2.priority - o1.priority;
				}
			}
		);
		
		final Object obj = new Object();
		if( objects.length == 0 ) objects = new Object[] { obj };
		for( Object object : objects )
		{
			Class<?> cls = object.getClass();
			
			Map<Object, List<Entry<IEventListener>>> objectListenerContainers = objectListenerContainersMap.get(type);
			if( objectListenerContainers == null ) return;
			
			List<Entry<IEventListener>> listeners		= objectListenerContainers.get( object );
			if( listeners != null ) for( int i=0; i<listeners.size(); i++ )
			{
				Entry<IEventListener> entry = listeners.get(i);
				
				if( entry.getValue() == null )
				{
					listeners.remove( i );
					i--;
				}
				else listenerEntryQueue.add( entry );
			}

			Class<?>[] interfaces = cls.getInterfaces();
			for( Class<?> clz : interfaces )
			{
				List<Entry<IEventListener>> classListeners	= objectListenerContainers.get( clz );
				if( classListeners != null ) for( int i=0; i<classListeners.size(); i++ )
				{
					Entry<IEventListener> entry = classListeners.get(i);
					
					if( entry.getValue() == null )
					{
						listeners.remove( i );
						i--;
					}
					else listenerEntryQueue.add( entry );
				}
			}
			
			Class<?> clz = cls;
			while( clz != null )
			{
				List<Entry<IEventListener>> classListeners	= objectListenerContainers.get( clz );
				if( classListeners != null ) for( int i=0; i<classListeners.size(); i++ )
				{
					Entry<IEventListener> entry = classListeners.get(i);
					
					if( entry.getValue() == null )
					{
						listeners.remove( i );
						i--;
					}
					else listenerEntryQueue.add( entry );
				}
				
				clz = clz.getSuperclass();
			}
		}
		
		while( listenerEntryQueue.isEmpty() == false && event.isInterrupted() == false )
		{
			Entry<IEventListener> entry = listenerEntryQueue.poll();
			IEventListener listener = entry.getValue();
			
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
