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

package net.gtaun.shoebill.plugin;

import java.io.File;

import net.gtaun.shoebill.IShoebill;
import net.gtaun.shoebill.util.event.Event;
import net.gtaun.shoebill.util.event.EventListenerPriority;
import net.gtaun.shoebill.util.event.IEventListener;
import net.gtaun.shoebill.util.event.IEventManager;


/**
 * @author MK124
 *
 */

public abstract class Plugin
{
	private boolean isEnabled;
	
	private PluginDescription description;
	private IShoebill shoebill;
	private IEventManager eventManager;
	private File dataFolder;
	

	public boolean isEnabled()						{ return isEnabled; }

	public PluginDescription getDescription()		{ return description; }
	public IShoebill getShoebill()					{ return shoebill; }
	public IEventManager getEventManager()			{ return eventManager; }
	public File getDataFolder()						{ return dataFolder; }
	
	
	protected Plugin()
	{
		eventManager = new IEventManager()
		{
			
			@Override
			public <T extends Event> void removeListener( Class<T> type, Object object, IEventListener listener )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void removeListener( Class<T> type, Class<?> clz, IEventListener listener )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void removeListener( Class<T> type, IEventListener listener )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> boolean hasListener( Class<T> type, Object object, IEventListener listener )
			{
				// XXX Auto-generated method stub
				return false;
			}
			
			@Override
			public <T extends Event> boolean hasListener( Class<T> type, Object object )
			{
				// XXX Auto-generated method stub
				return false;
			}
			
			@Override
			public <T extends Event> boolean hasListener( Class<T> type, Class<?> clz, IEventListener listener )
			{
				// XXX Auto-generated method stub
				return false;
			}
			
			@Override
			public <T extends Event> boolean hasListener( Class<T> type, Class<?> clz )
			{
				// XXX Auto-generated method stub
				return false;
			}
			
			@Override
			public <T extends Event> void dispatchEvent( T event, Object... objects )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void addListener( Class<T> type, Object object, IEventListener listener, short priority )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void addListener( Class<T> type, Object object, IEventListener listener, EventListenerPriority priority )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void addListener( Class<T> type, Class<?> clz, IEventListener listener, short priority )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void addListener( Class<T> type, Class<?> clz, IEventListener listener, EventListenerPriority priority )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void addListener( Class<T> type, IEventListener listener, short priority )
			{
				// XXX Auto-generated method stub
				
			}
			
			@Override
			public <T extends Event> void addListener( Class<T> type, IEventListener listener, EventListenerPriority priority )
			{
				// XXX Auto-generated method stub
				
			}
		};
	}
		
	void setContext( PluginDescription description, IShoebill shoebill, File dataFolder )
	{
		this.description = description;
		this.shoebill = shoebill;
		this.dataFolder = dataFolder;
	}

	protected abstract void onEnable() throws Exception;
	protected abstract void onDisable() throws Exception;
	
	void enable() throws Exception
	{
		onEnable();
		isEnabled = true;
	}
	
	void disable() throws Exception
	{
		onDisable();
		isEnabled = false;
	}
}
