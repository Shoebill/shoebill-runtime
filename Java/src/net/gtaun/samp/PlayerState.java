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

/**
 * @author MK124
 *
 */

public class PlayerState
{
	public static final int STATE_NONE =							0;
	public static final int STATE_ONFOOT =							1;
	public static final int STATE_DRIVER =							2;
	public static final int STATE_PASSENGER =						3;
	public static final int STATE_EXIT_VEHICLE =					4;
	public static final int STATE_ENTER_VEHICLE_DRIVER =			5;
	public static final int STATE_ENTER_VEHICLE_PASSENGER =			6;
	public static final int STATE_WASTED =							7;
	public static final int STATE_SPAWNED =							8;
	public static final int STATE_SPECTATING =						9;
	
	
	int state, vehicleSeat;
	boolean classSelected, dead, isPassenger;
	
	
	PlayerState()
	{
		
	}
}
