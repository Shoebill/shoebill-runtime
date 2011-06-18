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

package net.gtaun.samp;

import java.util.Vector;

import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.PointRange;

/**
 * @author MK124
 *
 */

public class LabelBase
{
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances( GameModeBase.instance.labelPool, cls );
	}
	
	
	public int id;
	public String text;
	public int color;
	public PointRange point;
	public boolean testLOS;
	

	public LabelBase( int color, Point point, float drawDistance, boolean testLOS )
	{
		this.text = "";
		this.color = color;
		this.point = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		
		init();
	}
	
	public LabelBase( String text, int color, Point point, float drawDistance, boolean testLOS )
	{
		this.text = text;
		this.color = color;
		this.point = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		
		init();
	}
	
	public LabelBase( String text, int color, PointRange point, boolean testLOS )
	{
		this.text = text;
		this.color = color;
		this.point = point.clone();
		this.testLOS = testLOS;
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.create3DTextLabel( text, color,
				point.x, point.y, point.z, point.distance, point.world, testLOS );
	}
	
	public void destroy()
	{
		NativeFunction.delete3DTextLabel( id );
		GameModeBase.instance.labelPool[ id ] = null;
	}
}
