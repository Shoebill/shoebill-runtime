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

import net.gtaun.shoebill.constant.ObjectEditResponse;
import net.gtaun.shoebill.constant.PlayerAttachBone;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.event.checkpoint.CheckpointEnterEvent;
import net.gtaun.shoebill.event.checkpoint.CheckpointLeaveEvent;
import net.gtaun.shoebill.event.checkpoint.RaceCheckpointEnterEvent;
import net.gtaun.shoebill.event.checkpoint.RaceCheckpointLeaveEvent;
import net.gtaun.shoebill.event.dialog.DialogResponseEvent;
import net.gtaun.shoebill.event.menu.MenuExitedEvent;
import net.gtaun.shoebill.event.menu.MenuSelectedEvent;
import net.gtaun.shoebill.event.object.ObjectMovedEvent;
import net.gtaun.shoebill.event.object.PlayerObjectMovedEvent;
import net.gtaun.shoebill.event.player.PlayerClickMapEvent;
import net.gtaun.shoebill.event.player.PlayerClickPlayerEvent;
import net.gtaun.shoebill.event.player.PlayerClickPlayerTextDrawEvent;
import net.gtaun.shoebill.event.player.PlayerClickTextDrawEvent;
import net.gtaun.shoebill.event.player.PlayerCommandEvent;
import net.gtaun.shoebill.event.player.PlayerConnectEvent;
import net.gtaun.shoebill.event.player.PlayerDeathEvent;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.event.player.PlayerEditAttachedObjectEvent;
import net.gtaun.shoebill.event.player.PlayerEditObjectEvent;
import net.gtaun.shoebill.event.player.PlayerEditPlayerObjectEvent;
import net.gtaun.shoebill.event.player.PlayerEnterExitModShopEvent;
import net.gtaun.shoebill.event.player.PlayerGiveDamageEvent;
import net.gtaun.shoebill.event.player.PlayerInteriorChangeEvent;
import net.gtaun.shoebill.event.player.PlayerKeyStateChangeEvent;
import net.gtaun.shoebill.event.player.PlayerKillEvent;
import net.gtaun.shoebill.event.player.PlayerPickupEvent;
import net.gtaun.shoebill.event.player.PlayerRequestClassEvent;
import net.gtaun.shoebill.event.player.PlayerRequestSpawnEvent;
import net.gtaun.shoebill.event.player.PlayerSelectObjectEvent;
import net.gtaun.shoebill.event.player.PlayerSelectPlayerObjectEvent;
import net.gtaun.shoebill.event.player.PlayerSpawnEvent;
import net.gtaun.shoebill.event.player.PlayerStateChangeEvent;
import net.gtaun.shoebill.event.player.PlayerStreamInEvent;
import net.gtaun.shoebill.event.player.PlayerStreamOutEvent;
import net.gtaun.shoebill.event.player.PlayerTakeDamageEvent;
import net.gtaun.shoebill.event.player.PlayerTextEvent;
import net.gtaun.shoebill.event.player.PlayerUpdateEvent;
import net.gtaun.shoebill.event.rcon.RconCommandEvent;
import net.gtaun.shoebill.event.rcon.RconLoginEvent;
import net.gtaun.shoebill.event.vehicle.VehicleDeathEvent;
import net.gtaun.shoebill.event.vehicle.VehicleEnterEvent;
import net.gtaun.shoebill.event.vehicle.VehicleExitEvent;
import net.gtaun.shoebill.event.vehicle.VehicleModEvent;
import net.gtaun.shoebill.event.vehicle.VehiclePaintjobEvent;
import net.gtaun.shoebill.event.vehicle.VehicleResprayEvent;
import net.gtaun.shoebill.event.vehicle.VehicleSpawnEvent;
import net.gtaun.shoebill.event.vehicle.VehicleStreamInEvent;
import net.gtaun.shoebill.event.vehicle.VehicleStreamOutEvent;
import net.gtaun.shoebill.event.vehicle.VehicleUpdateDamageEvent;
import net.gtaun.shoebill.event.vehicle.VehicleUpdateEvent;
import net.gtaun.shoebill.object.Dialog;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.Pickup;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerAttach.PlayerAttachSlot;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.PlayerTextdraw;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Textdraw;
import net.gtaun.shoebill.object.Timer;
import net.gtaun.shoebill.object.Vehicle;
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
	private SampObjectStoreImpl sampObjectStore;
	private EventManagerImpl rootEventManager;
	
	private long lastProcessTimeMillis;
	
	
	public SampEventDispatcher(SampObjectStoreImpl store, EventManagerImpl manager)
	{
		sampObjectStore = store;
		rootEventManager = manager;
		
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerConnectEvent event = new PlayerConnectEvent(player);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerDisconnectEvent event = new PlayerDisconnectEvent(player, reason);
			rootEventManager.dispatchEvent(event, player);
			
			sampObjectStore.setPlayer(playerId, null);
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerSpawnEvent event = new PlayerSpawnEvent(player);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Player killer = null;
			
			if (killerId != Player.INVALID_ID)
			{
				killer = sampObjectStore.getPlayer(killerId);
				PlayerKillEvent event = new PlayerKillEvent(killer, player, reason);
				rootEventManager.dispatchEvent(event, killer);
			}
			
			PlayerDeathEvent event = new PlayerDeathEvent(player, killer, reason);
			rootEventManager.dispatchEvent(event, player);
			
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
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			
			VehicleSpawnEvent event = new VehicleSpawnEvent(vehicle);
			rootEventManager.dispatchEvent(event, vehicle);
			
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
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			Player killer = sampObjectStore.getPlayer(killerId);
			
			VehicleDeathEvent event = new VehicleDeathEvent(vehicle, killer);
			rootEventManager.dispatchEvent(event, vehicle, killer);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerTextEvent event = new PlayerTextEvent(player, text);
			rootEventManager.dispatchEvent(event, player);
			
			if (event.getResponse() != 0)
			{
				String hexColor = player.getColor().toEmbeddingString();
				sampObjectStore.getServer().sendMessageToAll(Color.WHITE, hexColor + player.getName() + ": {FFFFFF}" + text);
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerCommandEvent event = new PlayerCommandEvent(player, cmdtext);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerRequestClassEvent event = new PlayerRequestClassEvent(player, classId);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			
			VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player, isPassenger != 0);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			
			VehicleExitEvent event = new VehicleExitEvent(vehicle, player);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			CheckpointEnterEvent event = new CheckpointEnterEvent(player);
			rootEventManager.dispatchEvent(event, player.getCheckpoint(), player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);
			rootEventManager.dispatchEvent(event, player.getCheckpoint(), player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
			rootEventManager.dispatchEvent(event, player.getRaceCheckpoint(), player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
			rootEventManager.dispatchEvent(event, player.getRaceCheckpoint(), player);
			
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
			rootEventManager.dispatchEvent(event, sampObjectStore.getServer());
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent(player);
			rootEventManager.dispatchEvent(event, player);
			
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
			SampObject object = sampObjectStore.getObject(objectId);
			
			ObjectMovedEvent event = new ObjectMovedEvent(object);
			rootEventManager.dispatchEvent(event, object);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			PlayerObject object = sampObjectStore.getPlayerObject(player, objectId);
			
			PlayerObjectMovedEvent event = new PlayerObjectMovedEvent(object);
			rootEventManager.dispatchEvent(event, object, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Pickup pickup = sampObjectStore.getPickup(pickupId);
			
			PlayerPickupEvent event = new PlayerPickupEvent(player, pickup);
			rootEventManager.dispatchEvent(event, pickup, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			
			VehicleModEvent event = new VehicleModEvent(vehicle, componentId);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerEnterExitModShopEvent event = new PlayerEnterExitModShopEvent(player, enterexit, interiorId);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			
			VehiclePaintjobEvent event = new VehiclePaintjobEvent(vehicle, paintjobId);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			
			VehicleResprayEvent event = new VehicleResprayEvent(vehicle, color1, color2);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			Player player = sampObjectStore.getPlayer(playerId);
			
			VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			Player player = sampObjectStore.getPlayer(playerId);
			
			VehicleUpdateEvent event = new VehicleUpdateEvent(vehicle, player, passengerSeat);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Menu menu = player.getCurrentMenu();
			
			MenuSelectedEvent event = new MenuSelectedEvent(menu, player, row);
			rootEventManager.dispatchEvent(event, menu, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Menu menu = player.getCurrentMenu();
			
			MenuExitedEvent event = new MenuExitedEvent(menu, player);
			rootEventManager.dispatchEvent(event, menu, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerInteriorChangeEvent event = new PlayerInteriorChangeEvent(player, oldInteriorId);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerKeyStateChangeEvent event = new PlayerKeyStateChangeEvent(player, oldKeys);
			rootEventManager.dispatchEvent(event, player);
			
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
			rootEventManager.dispatchEvent(event);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerUpdateEvent event = new PlayerUpdateEvent(player);
			rootEventManager.dispatchEvent(event, player);
			
			int ret = event.getResponse();
			if (ret == 0) return ret;
			
			int seat = player.getVehicleSeat();
			if (seat == 0)
			{
				Vehicle vehicle = player.getVehicle();
				VehicleUpdateEvent vehicleUpdateEvent = new VehicleUpdateEvent(vehicle, player, seat);
				rootEventManager.dispatchEvent(vehicleUpdateEvent, vehicle, player);
			}
			
			return ret;
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
			Player player = sampObjectStore.getPlayer(playerId);
			Player forPlayer = sampObjectStore.getPlayer(forPlayerId);
			
			PlayerStreamInEvent event = new PlayerStreamInEvent(player, forPlayer);
			rootEventManager.dispatchEvent(event, player, forPlayer);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Player forPlayer = sampObjectStore.getPlayer(forPlayerId);
			
			PlayerStreamOutEvent event = new PlayerStreamOutEvent(player, forPlayer);
			rootEventManager.dispatchEvent(event, player, forPlayer);
			
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
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			Player player = sampObjectStore.getPlayer(forPlayerId);
			
			VehicleStreamInEvent event = new VehicleStreamInEvent(vehicle, player);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
			Player player = sampObjectStore.getPlayer(forPlayerId);
			
			VehicleStreamOutEvent event = new VehicleStreamOutEvent(vehicle, player);
			rootEventManager.dispatchEvent(event, vehicle, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Dialog dialog = sampObjectStore.getDialog(dialogId);
			
			if (dialog == null) return 0;
			
			DialogResponseEvent event = new DialogResponseEvent(dialog, player, response, listitem, inputtext);
			rootEventManager.dispatchEvent(event, dialog, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Player issuer = sampObjectStore.getPlayer(issuerId);
			
			PlayerTakeDamageEvent event = new PlayerTakeDamageEvent(player, issuer, amount, weaponId);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			Player victim = sampObjectStore.getPlayer(damagedId);
			
			PlayerGiveDamageEvent event = new PlayerGiveDamageEvent(player, victim, amount, weaponId);
			rootEventManager.dispatchEvent(event, player);
			
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
			Player player = sampObjectStore.getPlayer(playerId);
			
			PlayerClickMapEvent event = new PlayerClickMapEvent(player, x, y, z);
			rootEventManager.dispatchEvent(event, player);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerClickTextDraw(int playerid, int clickedid)
	{
		try
		{
			Player player = sampObjectStore.getPlayer(playerid);
			Textdraw textdraw = sampObjectStore.getTextdraw(clickedid);
			
			PlayerClickTextDrawEvent event = new PlayerClickTextDrawEvent(player, textdraw);
			rootEventManager.dispatchEvent(event, player, textdraw);
			
			return event.getResponse();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerClickPlayerTextDraw(int playerid, int playertextid)
	{
		try
		{
			Player player = sampObjectStore.getPlayer(playerid);
			PlayerTextdraw textdraw = sampObjectStore.getPlayerTextdraw(player, playertextid);
			
			PlayerClickPlayerTextDrawEvent event = new PlayerClickPlayerTextDrawEvent(player, textdraw);
			rootEventManager.dispatchEvent(event, player, textdraw);
			
			return event.getResponse();
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
			Player player = sampObjectStore.getPlayer(playerId);
			Player clickedPlayer = sampObjectStore.getPlayer(clickedPlayerId);
			
			PlayerClickPlayerEvent event = new PlayerClickPlayerEvent(player, clickedPlayer, source);
			rootEventManager.dispatchEvent(event, player, clickedPlayer);
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ)
	{
		try
		{
			Player player = sampObjectStore.getPlayer(playerid);
			ObjectEditResponse editResponse = ObjectEditResponse.get(response);
			
			if (playerobject == 0)
			{
				SampObject object = sampObjectStore.getObject(objectid);
				
				PlayerEditObjectEvent event = new PlayerEditObjectEvent(player, object, editResponse);
				rootEventManager.dispatchEvent(event, player, object);
			}
			else
			{
				PlayerObject object = sampObjectStore.getPlayerObject(player, objectid);
				
				PlayerEditPlayerObjectEvent event = new PlayerEditPlayerObjectEvent(player, object, editResponse);
				rootEventManager.dispatchEvent(event, player, object);
			}
			
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ)
	{
		try
		{
			Player player = sampObjectStore.getPlayer(playerid);
			PlayerAttachSlot slot = player.getAttach().getSlotByBone(PlayerAttachBone.get(boneid));
			
			PlayerEditAttachedObjectEvent event = new PlayerEditAttachedObjectEvent(player, slot);
			rootEventManager.dispatchEvent(event, player);

			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ)
	{
		try
		{
			Player player = sampObjectStore.getPlayer(playerid);
			
			if (type == 0)
			{
				SampObject object = sampObjectStore.getObject(objectid);
				
				PlayerSelectObjectEvent event = new PlayerSelectObjectEvent(player, object);
				rootEventManager.dispatchEvent(event, player, object);
			}
			else
			{
				PlayerObject object = sampObjectStore.getPlayerObject(player, objectid);
				
				PlayerSelectPlayerObjectEvent event = new PlayerSelectPlayerObjectEvent(player, object);
				rootEventManager.dispatchEvent(event, player, object);
			}
			
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
			
			for (Timer timer : sampObjectStore.getTimers())
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
