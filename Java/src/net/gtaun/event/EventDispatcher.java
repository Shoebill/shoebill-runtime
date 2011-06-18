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

package net.gtaun.event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MK124
 *
 */

public class EventDispatcher <T extends Event> implements IEventDispatcher<T>
{
	List<WeakReference<IEventListener<T>>> listeners;
	
	
	public EventDispatcher()
	{
		listeners = new ArrayList<WeakReference<IEventListener<T>>> ();
	}
	
	
	public void addListener( IEventListener<T> listener )
	{
		addListener( listener, -1 );
	}
	
	public void addListener( IEventListener<T> listener, int priority )
	{
		for( int i=0; i<listeners.size(); i++ )
			if( listeners.get(i).get() == listener )
			{
				listeners.remove(i);
				break;
			}
		
		WeakReference<IEventListener<T>> reference = new WeakReference<IEventListener<T>>( listener );
		
		if(priority==-1)	listeners.add( reference );
		else				listeners.add( priority, reference );
	}
	
	public boolean hasListener( IEventListener<T> listener )
	{
		for( int i=0; i<listeners.size(); i++ )
			if( listeners.get(i).get() == listener ) return true;
		
		return false;
	}
	
	public void removeListener( IEventListener<T> listener )
	{
		for( int i=0; i<listeners.size(); i++ )
			if( listeners.get(i).get() == listener ) listeners.remove( i );
	}
	
	public void removeAllListener()
	{
		listeners.clear();
	}
	
	public int dispatchEvent( T event )
	{
		for( int i=0; i<listeners.size(); i++ )
		{
			WeakReference<IEventListener<T>> reference = listeners.get(i);
			IEventListener<T> listener = reference.get();
			
			if( listener == null ) listeners.remove( reference );
			else
			{
				listener.handleEvent( event );
				if( event.canceled ) return event.result;
			}
		}
		
		return event.result;
	}
}
