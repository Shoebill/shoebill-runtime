/**
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill.data;

import java.io.Serializable;

/**
 * @author JoJLlmAn
 *
 */

public class Vector3D extends Vector2D implements Cloneable, Serializable
{
	private static final long serialVersionUID = 8493095902831171278L;
	
	
	public float z;
	
	
	public Vector3D()
	{

	}
	
	public Vector3D( float x, float y, float z ) {
		super(x, y);
		this.z = z;
	}
	
	@Override
	public Vector3D clone()
	{
		return new Vector3D(x, y, z);
	}

}
