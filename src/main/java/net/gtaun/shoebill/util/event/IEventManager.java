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

public interface IEventManager
{
	EventListenerEntry addListener( Class<? extends Event> type, IEventListener listener, EventListenerPriority priority );
	EventListenerEntry addListener( Class<? extends Event> type, IEventListener listener, short priority );
	EventListenerEntry addListener( Class<? extends Event> type, Class<?> clz, IEventListener listener, EventListenerPriority priority );
	EventListenerEntry addListener( Class<? extends Event> type, Class<?> clz, IEventListener listener, short priority );
	EventListenerEntry addListener( Class<? extends Event> type, Object object, IEventListener listener, EventListenerPriority priority );
	EventListenerEntry addListener( Class<? extends Event> type, Object object, IEventListener listener, short priority );
	EventListenerEntry addListener( EventListenerEntry entry );

	void removeListener( Class<? extends Event> type, IEventListener listener );
	void removeListener( Class<? extends Event> type, Class<?> clz, IEventListener listener );
	void removeListener( Class<? extends Event> type, Object object, IEventListener listener );
	void removeListener( EventListenerEntry entry );

	boolean hasListener( Class<? extends Event> type, Class<?> clz );
	boolean hasListener( Class<? extends Event> type, Class<?> clz, IEventListener listener );
	boolean hasListener( Class<? extends Event> type, Object object );
	boolean hasListener( Class<? extends Event> type, Object object, IEventListener listener );
	boolean hasListener( EventListenerEntry entry );
	
	<T extends Event> void dispatchEvent( T event, Object ...objects );
}
