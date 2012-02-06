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

import java.util.HashMap;
import java.util.List;

/**
 * @author MK124
 *
 */

public class Configuration
{
	protected HashMap<String, Object> root;
	
	
	public Configuration()
	{
		this( new HashMap<String, Object>() );
	}
	
	public Configuration( HashMap<String, Object> root )
	{
		this.root = root;
	}
	
	public boolean contains( String path )
	{
		return get(path) != null;
	}
	
	@SuppressWarnings("unchecked")
	public Object get( String path )
	{
		String[] childs = path.split( "." );
		HashMap<String, Object> node = root;
		
		for( int i=0; i<childs.length-1; i++ )
		{
			Object obj = node.get(childs[i]);
			if( obj instanceof HashMap<?, ?> == false ) return null;
			node = (HashMap<String, Object>) obj;
		}
		
		return node.get( childs[ childs.length-1 ] );
	}
	
	public String getString( String path )
	{
		return (String) get(path);
	}
	
	public boolean isString( String path )
	{
		return get(path) instanceof String;
	}
	
	public int getInt( String path )
	{
		return (Integer) get(path);
	}
	
	public boolean isInt( String path )
	{
		return get(path) instanceof Integer;
	}
	
	public int getLong( String path )
	{
		return (Integer) get(path);
	}
	
	public boolean isLong( String path )
	{
		return get(path) instanceof Long;
	}
	
	public double getDouble( String path )
	{
		return (Double) get(path);
	}
	
	public boolean isDouble( String path )
	{
		return get(path) instanceof Double;
	}
	
	public boolean getBoolean( String path )
	{
		return (Boolean) get(path);
	}
	
	public boolean isBoolean( String path )
	{
		return get(path) instanceof Boolean;
	}

	public List<?> getList( String path )
	{
		return (List<?>) get(path);
	}
	
	public boolean isList( String path )
	{
		return get(path) instanceof List<?>;
	}
}

