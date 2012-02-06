/**
 * Copyright (C) 2012 MK124
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

package net.gtaun.shoebill.util.config;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author MK124
 *
 */

public abstract class FileConfiguration extends Configuration
{
	protected File file;
	

	public FileConfiguration()
	{
		root = new HashMap<String, Object>();
	}
	
	public FileConfiguration( Configuration config )
	{
		super( config.root );
	}
	
	public FileConfiguration( File file )
	{
		setFile( file );
	}
	
	
	public void setFile( File file )
	{
		this.file = file;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public abstract void read( InputStream stream );
	public abstract void write( OutputStream stream );
	
	public abstract void save();
	public abstract void load();
}
