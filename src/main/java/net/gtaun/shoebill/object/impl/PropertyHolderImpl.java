package net.gtaun.shoebill.object.impl;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import net.gtaun.shoebill.object.PropertyHolder;

/**
 * 
 * 
 * @author MK124
 */
public class PropertyHolderImpl implements PropertyHolder
{
	private final Map<Object, Object> properties;
	
	
	public PropertyHolderImpl()
	{
		properties = new HashMap<>();
	}
	
	@Override
	public Object getProperty(Object key)
	{
		Object obj = properties.get(key);
		if (obj instanceof WeakReference == false) return obj;
		
		WeakReference<?> ref = (WeakReference<?>) obj;
		Object o = ref.get();
		if (o == null) properties.remove(key);
		return o;
	}
	
	@Override
	public <T> T getProperty(Class<T> clz)
	{
		return clz.cast(properties.get(clz));
	}
	
	@Override
	public void setProperty(Object key, Object value)
	{
		properties.put(key, value);
	}
	
	@Override
	public <T> void setProperty(Class<T> clz, T value)
	{
		properties.put(clz, value);
	}
	
	@Override
	public void setWeakProperty(Object key, Object value)
	{
		properties.put(key, new WeakReference<Object>(value));
	}
	
	@Override
	public <T> void setWeakProperty(Class<T> clz, T value)
	{
		properties.put(clz, new WeakReference<Object>(value));
	}
	
	@Override
	public boolean isPropertyExisted(Object key)
	{
		Object obj = properties.get(key);
		if (obj == null) return false;
		if (obj instanceof WeakReference == false) return true;
		
		WeakReference<?> ref = (WeakReference<?>) obj;
		if (ref.get() != null) return true;
		
		properties.remove(key);
		return false;
	}
	
	@Override
	public boolean removeProperty(Object key)
	{
		return properties.remove(key) != null;
	}
}
