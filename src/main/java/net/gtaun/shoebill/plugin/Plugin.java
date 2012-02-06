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


/**
 * @author MK124
 *
 */

public abstract class Plugin
{
	private boolean isEnabled;
	
	private PluginDescription description;
	private IShoebill shoebill;
	private File dataFolder;
	

	public boolean isEnabled()						{ return isEnabled; }

	public PluginDescription getDescription()		{ return description; }
	public IShoebill getShoebill()					{ return shoebill; }
	public File getDataFolder()						{ return dataFolder; }
	
	
	protected Plugin()
	{
		
	}
		
	void setContext( PluginDescription description, IShoebill shoebill, File dataFolder )
	{
		this.description = description;
		this.shoebill = shoebill;
		this.dataFolder = dataFolder;
	}

	protected abstract void onEnable();
	protected abstract void onDisable();
}
