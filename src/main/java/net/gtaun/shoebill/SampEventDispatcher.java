/**
 * Copyright (C) 2011-2012 MK124
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

package net.gtaun.shoebill;

import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.events.checkpoint.CheckpointEnterEvent;
import net.gtaun.shoebill.events.checkpoint.CheckpointLeaveEvent;
import net.gtaun.shoebill.events.checkpoint.RaceCheckpointEnterEvent;
import net.gtaun.shoebill.events.checkpoint.RaceCheckpointLeaveEvent;
import net.gtaun.shoebill.events.dialog.DialogResponseEvent;
import net.gtaun.shoebill.events.menu.MenuExitedEvent;
import net.gtaun.shoebill.events.menu.MenuSelectedEvent;
import net.gtaun.shoebill.events.object.ObjectMovedEvent;
import net.gtaun.shoebill.events.object.PlayerObjectMovedEvent;
import net.gtaun.shoebill.events.player.PlayerClickMapEvent;
import net.gtaun.shoebill.events.player.PlayerClickPlayerEvent;
import net.gtaun.shoebill.events.player.PlayerCommandEvent;
import net.gtaun.shoebill.events.player.PlayerConnectEvent;
import net.gtaun.shoebill.events.player.PlayerDeathEvent;
import net.gtaun.shoebill.events.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.events.player.PlayerEnterExitModShopEvent;
import net.gtaun.shoebill.events.player.PlayerGiveDamageEvent;
import net.gtaun.shoebill.events.player.PlayerInteriorChangeEvent;
import net.gtaun.shoebill.events.player.PlayerKeyStateChangeEvent;
import net.gtaun.shoebill.events.player.PlayerKillEvent;
import net.gtaun.shoebill.events.player.PlayerPickupEvent;
import net.gtaun.shoebill.events.player.PlayerRequestClassEvent;
import net.gtaun.shoebill.events.player.PlayerRequestSpawnEvent;
import net.gtaun.shoebill.events.player.PlayerSpawnEvent;
import net.gtaun.shoebill.events.player.PlayerStateChangeEvent;
import net.gtaun.shoebill.events.player.PlayerStreamInEvent;
import net.gtaun.shoebill.events.player.PlayerStreamOutEvent;
import net.gtaun.shoebill.events.player.PlayerTakeDamageEvent;
import net.gtaun.shoebill.events.player.PlayerTextEvent;
import net.gtaun.shoebill.events.player.PlayerUpdateEvent;
import net.gtaun.shoebill.events.rcon.RconCommandEvent;
import net.gtaun.shoebill.events.rcon.RconLoginEvent;
import net.gtaun.shoebill.events.vehicle.VehicleDeathEvent;
import net.gtaun.shoebill.events.vehicle.VehicleEnterEvent;
import net.gtaun.shoebill.events.vehicle.VehicleExitEvent;
import net.gtaun.shoebill.events.vehicle.VehicleModEvent;
import net.gtaun.shoebill.events.vehicle.VehiclePaintjobEvent;
import net.gtaun.shoebill.events.vehicle.VehicleResprayEvent;
import net.gtaun.shoebill.events.vehicle.VehicleSpawnEvent;
import net.gtaun.shoebill.events.vehicle.VehicleStreamInEvent;
import net.gtaun.shoebill.events.vehicle.VehicleStreamOutEvent;
import net.gtaun.shoebill.events.vehicle.VehicleUnoccupiedUpdateEvent;
import net.gtaun.shoebill.events.vehicle.VehicleUpdateDamageEvent;
import net.gtaun.shoebill.object.Dialog;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.Pickup;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Timer;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.object.impl.PlayerImpl;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.util.event.EventManagerImpl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public class SampEventDispatcher implements SampCallbackHandler
{
	private SampObjectPoolImpl sampObjectPool;
	private EventManagerImpl eventManager;
	
	private long lastProcessTimeMillis;
	
	
	public SampEventDispatcher(SampObjectPoolImpl pool, EventManagerImpl manager)
	{
		sampObjectPool = pool;
		eventManager = manager;
		
		lastProcessTimeMillis = System.currentTimeMillis();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public int onGameModeInit()
	{
		return 1;
	}
	
	@Override
	public int onGameModeExit()
	{
		return 1;
	}
	
	@Override
	public int onPlayerConnect(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerConnectEvent event = new PlayerConnectEvent(player);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerDisconnect(int playerId, int reason)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerDisconnectEvent event = new PlayerDisconnectEvent(player, reason);
			eventManager.dispatchEvent(event, player);
			
			sampObjectPool.removePlayer(player);
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerSpawn(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerSpawnEvent event = new PlayerSpawnEvent(player);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerDeath(int playerId, int killerId, int reason)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Player killer = null;
			
			if (killerId != Player.INVALID_ID)
			{
				killer = sampObjectPool.getPlayer(killerId);
				PlayerKillEvent event = new PlayerKillEvent(killer, player, reason);
				eventManager.dispatchEvent(event, killer);
			}
			
			PlayerDeathEvent event = new PlayerDeathEvent(player, killer, reason);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleSpawn(int vehicleId)
	{
		try
		{
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			
			VehicleSpawnEvent event = new VehicleSpawnEvent(vehicle);
			eventManager.dispatchEvent(event, vehicle);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleDeath(int vehicleId, int killerId)
	{
		try
		{
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			Player killer = sampObjectPool.getPlayer(killerId);
			
			VehicleDeathEvent event = new VehicleDeathEvent(vehicle, killer);
			eventManager.dispatchEvent(event, vehicle, killer);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerText(int playerId, String text)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerTextEvent event = new PlayerTextEvent(player, text);
			eventManager.dispatchEvent(event, player);
			
			if (event.getResponse() != 0)
			{
				PlayerImpl.sendMessageToAll(Color.WHITE, "{FE8B13}" + player.getName() + ": {FFFFFF}" + text);
			}
			
			return 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerCommandText(int playerId, String cmdtext)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerCommandEvent event = new PlayerCommandEvent(player, cmdtext);
			eventManager.dispatchEvent(event, player);
			
			return event.getResponse();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerRequestClass(int playerId, int classId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerRequestClassEvent event = new PlayerRequestClassEvent(player, classId);
			eventManager.dispatchEvent(event, player);
			
			return event.getResponse();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
	}
	
	@Override
	public int onPlayerEnterVehicle(int playerId, int vehicleId, int isPassenger)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			
			VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player, isPassenger != 0);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerExitVehicle(int playerId, int vehicleId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			
			VehicleExitEvent event = new VehicleExitEvent(vehicle, player);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerStateChange(int playerId, int state, int oldState)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerEnterCheckpoint(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			CheckpointEnterEvent event = new CheckpointEnterEvent(player);
			eventManager.dispatchEvent(event, player.getCheckpoint(), player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerLeaveCheckpoint(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);
			eventManager.dispatchEvent(event, player.getCheckpoint(), player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerEnterRaceCheckpoint(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
			eventManager.dispatchEvent(event, player.getRaceCheckpoint(), player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerLeaveRaceCheckpoint(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
			eventManager.dispatchEvent(event, player.getRaceCheckpoint(), player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onRconCommand(String cmd)
	{
		try
		{
			RconCommandEvent event = new RconCommandEvent(cmd);
			eventManager.dispatchEvent(event, ShoebillImpl.getInstance().getSampObjectPool().getServer());
			
			return event.getResponse();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerRequestSpawn(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent(player);
			eventManager.dispatchEvent(event, player);
			
			return event.getResponse();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onObjectMoved(int objectId)
	{
		try
		{
			SampObject object = sampObjectPool.getObject(objectId);
			
			ObjectMovedEvent event = new ObjectMovedEvent(object);
			eventManager.dispatchEvent(event, object);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerObjectMoved(int playerId, int objectId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			PlayerObject object = sampObjectPool.getPlayerObject(player, objectId);
			
			PlayerObjectMovedEvent event = new PlayerObjectMovedEvent(player, object);
			eventManager.dispatchEvent(event, object, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerPickUpPickup(int playerId, int pickupId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Pickup pickup = sampObjectPool.getPickup(pickupId);
			
			PlayerPickupEvent event = new PlayerPickupEvent(player, pickup);
			eventManager.dispatchEvent(event, pickup, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleMod(int playerId, int vehicleId, int componentId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			
			VehicleModEvent event = new VehicleModEvent(vehicle, componentId);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onEnterExitModShop(int playerId, int enterexit, int interiorId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerEnterExitModShopEvent event = new PlayerEnterExitModShopEvent(player, enterexit, interiorId);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehiclePaintjob(int playerId, int vehicleId, int paintjobId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			
			VehiclePaintjobEvent event = new VehiclePaintjobEvent(vehicle, paintjobId);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleRespray(int playerId, int vehicleId, int color1, int color2)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			
			VehicleResprayEvent event = new VehicleResprayEvent(vehicle, color1, color2);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleDamageStatusUpdate(int vehicleId, int playerId)
	{
		try
		{
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			Player player = sampObjectPool.getPlayer(playerId);
			
			VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onUnoccupiedVehicleUpdate(int vehicleId, int playerId, int passengerSeat)
	{
		try
		{
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			Player player = sampObjectPool.getPlayer(playerId);
			
			VehicleUnoccupiedUpdateEvent event = new VehicleUnoccupiedUpdateEvent(vehicle, player, passengerSeat);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerSelectedMenuRow(int playerId, int row)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Menu menu = player.getMenu();
			
			MenuSelectedEvent event = new MenuSelectedEvent(menu, player, row);
			eventManager.dispatchEvent(event, menu, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerExitedMenu(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Menu menu = player.getMenu();
			
			MenuExitedEvent event = new MenuExitedEvent(menu, player);
			eventManager.dispatchEvent(event, menu, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
	}
	
	@Override
	public int onPlayerInteriorChange(int playerId, int interiorId, int oldInteriorId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerInteriorChangeEvent event = new PlayerInteriorChangeEvent(player, oldInteriorId);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerKeyStateChange(int playerId, int keys, int oldKeys)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerKeyStateChangeEvent event = new PlayerKeyStateChangeEvent(player, oldKeys);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onRconLoginAttempt(String ip, String password, int isSuccess)
	{
		try
		{
			RconLoginEvent event = new RconLoginEvent(ip, password, isSuccess != 0);
			eventManager.dispatchEvent(event);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerUpdate(int playerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerUpdateEvent event = new PlayerUpdateEvent(player);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerStreamIn(int playerId, int forPlayerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Player forPlayer = sampObjectPool.getPlayer(forPlayerId);
			
			PlayerStreamInEvent event = new PlayerStreamInEvent(player, forPlayer);
			eventManager.dispatchEvent(event, player, forPlayer);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
	}
	
	@Override
	public int onPlayerStreamOut(int playerId, int forPlayerId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Player forPlayer = sampObjectPool.getPlayer(forPlayerId);
			
			PlayerStreamOutEvent event = new PlayerStreamOutEvent(player, forPlayer);
			eventManager.dispatchEvent(event, player, forPlayer);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleStreamIn(int vehicleId, int forPlayerId)
	{
		try
		{
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			Player player = sampObjectPool.getPlayer(forPlayerId);
			
			VehicleStreamInEvent event = new VehicleStreamInEvent(vehicle, player);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleStreamOut(int vehicleId, int forPlayerId)
	{
		try
		{
			Vehicle vehicle = sampObjectPool.getVehicle(vehicleId);
			Player player = sampObjectPool.getPlayer(forPlayerId);
			
			VehicleStreamOutEvent event = new VehicleStreamOutEvent(vehicle, player);
			eventManager.dispatchEvent(event, vehicle, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onDialogResponse(int playerId, int dialogId, int response, int listitem, String inputtext)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Dialog dialog = sampObjectPool.getDialog(dialogId);
			
			if (dialog == null) return 0;
			
			DialogResponseEvent event = new DialogResponseEvent(dialog, player, response, listitem, inputtext);
			eventManager.dispatchEvent(event, dialog, player);
			
			return event.getResponse();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerTakeDamage(int playerId, int issuerId, float amount, int weaponId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Player issuer = sampObjectPool.getPlayer(issuerId);
			
			PlayerTakeDamageEvent event = new PlayerTakeDamageEvent(player, issuer, amount, weaponId);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Player victim = sampObjectPool.getPlayer(damagedId);
			
			PlayerGiveDamageEvent event = new PlayerGiveDamageEvent(player, victim, amount, weaponId);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerClickMap(int playerId, float x, float y, float z)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			
			PlayerClickMapEvent event = new PlayerClickMapEvent(player, x, y, z);
			eventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerClickPlayer(int playerId, int clickedPlayerId, int source)
	{
		try
		{
			Player player = sampObjectPool.getPlayer(playerId);
			Player clickedPlayer = sampObjectPool.getPlayer(clickedPlayerId);
			
			PlayerClickPlayerEvent event = new PlayerClickPlayerEvent(player, clickedPlayer, source);
			eventManager.dispatchEvent(event, player, clickedPlayer);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public void onProcessTick()
	{
		try
		{
			long nowTick = System.currentTimeMillis();
			int interval = (int) (nowTick - lastProcessTimeMillis);
			lastProcessTimeMillis = nowTick;
			
			for (Timer timer : sampObjectPool.getTimers())
			{
				timer.tick(interval);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
