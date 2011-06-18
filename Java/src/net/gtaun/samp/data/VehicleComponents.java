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

package net.gtaun.samp.data;

/**
 * @author MK124
 *
 */

public class VehicleComponents
{
	public int spoiler, hood, roof, sideskirt, lamps, nitro, exhaust, wheels, stereo,
		hydraulics, frontBumper, rearBumper, ventRight, ventLeft;
	
	
	public VehicleComponents()
	{
		
	}
	
	public VehicleComponents( int[] data )
	{
		read( data );
	}
	
	
	public void read( int[] data )
	{
		spoiler = data[0];
		hood = data[1];
		roof = data[2];
		sideskirt = data[3];
		lamps = data[4];
		nitro = data[5];
		exhaust = data[6];
		wheels = data[7];
		stereo = data[8];
		hydraulics = data[9];
		frontBumper = data[10];
		rearBumper = data[11];
		ventLeft = data[12];
		ventRight = data[13];
	}
	
	public int[] toArray()
	{
		int[] data = {
				spoiler, hood, roof, sideskirt, lamps, nitro, exhaust, wheels, stereo,
				hydraulics, frontBumper, rearBumper, ventRight, ventLeft};
		
		return data;
	}
	
	
	public boolean equals( Object obj )
	{
		if( obj == this )							return true;
		if( !(obj instanceof VehicleComponents) )	return false;
		
		VehicleComponents component = (VehicleComponents) obj;
		if( component.spoiler != spoiler )			return false;
		if( component.hood != hood )				return false;
		if( component.roof != roof )				return false;
		if( component.sideskirt != sideskirt )		return false;
		if( component.lamps != lamps )				return false;
		if( component.nitro != nitro )				return false;
		if( component.exhaust != exhaust )			return false;
		if( component.wheels != wheels )			return false;
		if( component.stereo != stereo )			return false;
		if( component.hydraulics != hydraulics )	return false;
		if( component.frontBumper != frontBumper )	return false;
		if( component.rearBumper != rearBumper )	return false;
		if( component.ventRight != ventRight )		return false;
		if( component.ventLeft != ventLeft )		return false;
		
		return true;
	}
	
	public VehicleComponents clone()
	{
		VehicleComponents component = new VehicleComponents();
		
		component.spoiler = spoiler;
		component.hood = hood;
		component.roof = roof;
		component.sideskirt = sideskirt;
		component.lamps = lamps;
		component.nitro = nitro;
		component.exhaust = exhaust;
		component.wheels = wheels;
		component.stereo = stereo;
		component.hydraulics = hydraulics;
		component.frontBumper = frontBumper;
		component.rearBumper = rearBumper;
		component.ventLeft = ventLeft;
		component.ventRight = ventRight;
		
		return new VehicleComponents();
	}
}
