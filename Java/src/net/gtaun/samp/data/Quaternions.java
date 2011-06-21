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

package net.gtaun.samp.data;

/**
 * @author JoJLlmAn
 *
 */

public class Quaternions extends Vector3D
{
	//public float w,x,y,z;
	
	public float w;
	
	public Quaternions()
	{
		
	}
	
	public Quaternions( float w, float x, float y, float z )
	{
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
//---------------------------------------------------------
	
	public Quaternions getConjugate()
	{
		return new Quaternions(w,-x,-y,-z);
	}
	
	public float[][] transformMatrix()
	{
		float[][] matrix = new float[4][4];
		
		matrix[0][0] = 1 - 2 * (y*y + z*z);
		matrix[0][1] = 	   2 * (x*y - w*z);
		matrix[0][2] = 	   2 * (w*y + x*z);
		matrix[0][3] = 0;
		
		matrix[1][0] =     2 * (x*y + w*z);
		matrix[1][1] = 1 - 2 * (x*x + z*z);
		matrix[1][2] =     2 * (y*z - w*x);
		matrix[1][3] = 0;
		
		matrix[2][0] =     2 * (x*z - w*y);
		matrix[2][1] =     2 * (y*z + w*x);
		matrix[2][2] = 1 - 2 * (x*x + y*y);
		matrix[2][3] = 0;
		
		matrix[3][0] = 0;
		matrix[3][1] = 0;
		matrix[3][2] = 0;
		matrix[3][3] = 1;
		
		return matrix;
	}
	
	public float[] rotatedMatrix( float dx, float dy, float dz, double angle )
	{
		float w = (float) Math.cos(angle / 2);
		
		float[][] matrix = new float[3][3];
		
		matrix[0][0] = 1 - 2 * (y*y + z*z);
		matrix[0][1] = 	   2 * (x*y - w*z);
		matrix[0][2] = 	   2 * (w*y + x*z);
		
		matrix[1][0] =     2 * (x*y + w*z);
		matrix[1][1] = 1 - 2 * (x*x + z*z);
		matrix[1][2] =     2 * (y*z - w*x);
		
		matrix[2][0] =     2 * (x*z - w*y);
		matrix[2][1] =     2 * (y*z + w*x);
		matrix[2][2] = 1 - 2 * (x*x + y*y);
		
		float[] rotated = new float[3];
		
		rotated[0] = dx*matrix[0][0] + dy*matrix[0][1] + dz*matrix[0][2];
		rotated[1] = dx*matrix[1][0] + dy*matrix[1][1] + dz*matrix[1][2];
		rotated[2] = dx*matrix[2][0] + dy*matrix[2][1] + dz*matrix[2][2];
		
		return rotated;
	}
	
	public Quaternions clone()
	{
		return new Quaternions(w,x,y,z);
	}
}
