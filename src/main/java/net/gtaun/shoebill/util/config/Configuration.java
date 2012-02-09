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
		String[] childs;
		
		if( path.contains(".") )	childs = path.split(".");
		else						childs = new String[] { new String(path) };
		
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
		return getString(path, "");
	}
	
	public String getString( String path, String def )
	{
		Object obj = get(path);
		return obj == null ? def : obj.toString();
	}
	
	public boolean isString( String path )
	{
		return get(path) instanceof String;
	}
	
	public int getInt( String path )
	{
		return getInt(path, 0);
	}
	
	public int getInt( String path, int def )
	{
		Object obj = get(path);
		return obj == null ? def : (Integer) obj;
	}
	
	public boolean isInt( String path )
	{
		return get(path) instanceof Integer;
	}

	public long getLong( String path )
	{
		return getLong(path, 0L);
	}
	
	public long getLong( String path, long def )
	{
		Object obj = get(path);
		return obj == null ? def : (Long) obj;
	}
	
	public boolean isLong( String path )
	{
		return get(path) instanceof Long;
	}
	
	public double getDouble( String path )
	{
		return getDouble(path, Double.NaN);
	}
	
	public double getDouble( String path, double def )
	{
		Object obj = get(path);
		return obj == null ? def : (Double) obj;
	}
	
	public boolean isDouble( String path )
	{
		return get(path) instanceof Double;
	}

	public boolean getBoolean( String path )
	{
		return getBoolean(path, false);
	}
	
	public boolean getBoolean( String path, boolean def )
	{
		Object obj = get(path);
		return obj == null ? def : (Boolean) obj;
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
