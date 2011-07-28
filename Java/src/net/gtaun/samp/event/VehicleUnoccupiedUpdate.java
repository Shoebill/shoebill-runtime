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

package net.gtaun.samp.event;

import net.gtaun.event.Event;
import net.gtaun.samp.PlayerBase;
import net.gtaun.samp.VehicleBase;

/**
 * @author JoJLlmAn
 *
 */

public class VehicleUnoccupiedUpdate extends Event
{
	VehicleBase vehicle;
	PlayerBase player;
	int passenger_seat;
	
	public VehicleBase vehicle()	{ return vehicle; }
	public PlayerBase player()	{ return player; }
	public int passenger_seat()	{ return passenger_seat; }
	
	public VehicleUnoccupiedUpdate( VehicleBase vehicle, PlayerBase player, int passenger_seat )
	{
		this.vehicle = vehicle;
		this.player = player;
		this.passenger_seat = passenger_seat;
	}
}
