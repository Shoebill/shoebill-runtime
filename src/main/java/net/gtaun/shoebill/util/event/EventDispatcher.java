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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * @author MK124
 *
 */

public class EventDispatcher implements IEventDispatcher
{
	public HashMap<Class<?>, List<WeakReference<IEventListener>>> listenerContainers;
	
	
	public EventDispatcher()
	{
		listenerContainers = new HashMap<Class<?>, List<WeakReference<IEventListener>>>();
	}
	

	@Override
	public void addListener( Class<?> type, IEventListener listener )
	{
		addListener( type, listener, -1 );
	}

	@Override
	public void addListener( Class<?> type, IEventListener listener, int priority )
	{
		List<WeakReference<IEventListener>> listeners = listenerContainers.get(type);
		
		if( listeners == null )
		{
			listeners = new Vector<WeakReference<IEventListener>>();
			listenerContainers.put( type, listeners );
		}
		
		for( int i=0; i<listeners.size(); i++ )
			if( listeners.get(i).get() == listener ) return;
		
		WeakReference<IEventListener> reference = new WeakReference<IEventListener>( listener );
		
		if(priority==-1)	listeners.add( reference );
		else				listeners.add( priority, reference );
	}

	@Override
	public void removeListener( Class<?> type, IEventListener listener )
	{
		List<WeakReference<IEventListener>> listeners = listenerContainers.get(type);		
		if( listeners == null ) return;
		
		for( int i=0; i<listeners.size(); i++ )
			if( listeners.get(i).get() == listener ) listeners.remove( i );
	}

	@Override
	public boolean hasListener( Class<?> type )
	{
		List<WeakReference<IEventListener>> listeners = listenerContainers.get(type);		
		if( listeners == null ) return false;
		
		return true;
	}

	@Override
	public boolean hasListener( Class<?> type, IEventListener listener )
	{
		List<WeakReference<IEventListener>> listeners = listenerContainers.get(type);		
		if( listeners == null ) return false;
		
		for( int i=0; i<listeners.size(); i++ )
		{
			WeakReference<IEventListener> reference = listeners.get(i);
			if( reference.get() == listener ) return true;
		}
		
		return false;
	}
	
	@Override
	public void dispatchEvent( Event event )
	{
		List<WeakReference<IEventListener>> listeners = listenerContainers.get(event.getClass());		
		if( listeners == null ) return;
		
		for( int i=0; i<listeners.size(); i++ )
		{
			WeakReference<IEventListener> reference = listeners.get(i);
			IEventListener listener = reference.get();
			
			if( listener == null ) listeners.remove( reference );
			else
			{
				try
				{
					listener.handleEvent( event );
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				if( event.canceled ) return;
			}
		}
	}
}
