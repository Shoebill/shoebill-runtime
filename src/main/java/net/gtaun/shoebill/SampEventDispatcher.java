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

package net.gtaun.shoebill;

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
import net.gtaun.shoebill.event.player.PlayerCommandEvent;
import net.gtaun.shoebill.event.player.PlayerConnectEvent;
import net.gtaun.shoebill.event.player.PlayerDeathEvent;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.event.player.PlayerEnterExitModShopEvent;
import net.gtaun.shoebill.event.player.PlayerGiveDamageEvent;
import net.gtaun.shoebill.event.player.PlayerInteriorChangeEvent;
import net.gtaun.shoebill.event.player.PlayerKeyStateChangeEvent;
import net.gtaun.shoebill.event.player.PlayerKillEvent;
import net.gtaun.shoebill.event.player.PlayerPickupEvent;
import net.gtaun.shoebill.event.player.PlayerRequestClassEvent;
import net.gtaun.shoebill.event.player.PlayerRequestSpawnEvent;
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
import net.gtaun.shoebill.event.vehicle.VehicleUnoccupiedUpdateEvent;
import net.gtaun.shoebill.event.vehicle.VehicleUpdateDamageEvent;
import net.gtaun.shoebill.object.IDialog;
import net.gtaun.shoebill.object.IMenu;
import net.gtaun.shoebill.object.IObject;
import net.gtaun.shoebill.object.IPickup;
import net.gtaun.shoebill.object.IPlayer;
import net.gtaun.shoebill.object.IPlayerObject;
import net.gtaun.shoebill.object.ITimer;
import net.gtaun.shoebill.object.IVehicle;
import net.gtaun.shoebill.object.impl.DialogImpl;
import net.gtaun.shoebill.object.impl.MenuImpl;
import net.gtaun.shoebill.object.impl.ObjectImpl;
import net.gtaun.shoebill.object.impl.PickupImpl;
import net.gtaun.shoebill.object.impl.PlayerImpl;
import net.gtaun.shoebill.object.impl.PlayerObjectImpl;
import net.gtaun.shoebill.object.impl.VehicleImpl;
import net.gtaun.shoebill.samp.ISampCallbackHandler;
import net.gtaun.shoebill.util.event.EventManager;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 *
 */

public class SampEventDispatcher implements ISampCallbackHandler
{
	private SampObjectPool sampObjectPool;
	private EventManager eventManager;

	private long lastProcessTimeMillis;
	
	
	public SampEventDispatcher( SampObjectPool pool, EventManager manager )
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
	public int onPlayerConnect( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerConnectEvent event = new PlayerConnectEvent( player );
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerDisconnect( int playerId, int reason )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerDisconnectEvent event = new PlayerDisconnectEvent( player, reason );
				eventManager.dispatchEvent( event, player );
			}
			
			sampObjectPool.removePlayer( player );
			
			if( player instanceof PlayerImpl )
			{
				PlayerImpl pl = (PlayerImpl) player;
				pl.processPlayerDisconnect();
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerSpawn( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerSpawnEvent event = new PlayerSpawnEvent( player );
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerDeath( int playerId, int killerId, int reason )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPlayer killer = null;
			
			if( killerId != IPlayer.INVALID_ID )
			{
				killer = sampObjectPool.getPlayer(killerId);
				
				if( killer instanceof PlayerImpl )
				{
					PlayerKillEvent event = new PlayerKillEvent( killer, player, reason );
					eventManager.dispatchEvent( event, killer );
				}
			}
			
			if( player instanceof PlayerImpl )
			{
				PlayerDeathEvent event = new PlayerDeathEvent( player, killer, reason );
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleSpawn( int vehicleId )
	{
		try
		{
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			
			if( vehicle instanceof VehicleImpl )
			{
				VehicleSpawnEvent event = new VehicleSpawnEvent(vehicle);
				eventManager.dispatchEvent( event, vehicle );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleDeath( int vehicleId, int killerId )
	{
		try
		{
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			IPlayer killer = sampObjectPool.getPlayer( killerId );

			VehicleDeathEvent event = new VehicleDeathEvent(vehicle, killer);
			
			if( vehicle instanceof VehicleImpl && killer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, vehicle, killer );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
			else if( killer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, killer );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerText( int playerId, String text )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerTextEvent event = new PlayerTextEvent( player, text );
				eventManager.dispatchEvent( event, player );
				
				if( event.getResult() != 0 )
				{
					PlayerImpl.sendMessageToAll( Color.WHITE, "{FE8B13}" + player.getName() + ": {FFFFFF}" + text );
					return 1;
				}
			}
			
			return 0;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerCommandText( int playerId, String cmdtext )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerCommandEvent event = new PlayerCommandEvent( player, cmdtext );
				eventManager.dispatchEvent( event, player );
				
				return event.getResult();
			}
			
			return 0;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerRequestClass( int playerId, int classId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerRequestClassEvent event = new PlayerRequestClassEvent( player, classId );
				eventManager.dispatchEvent( event, player );
				
				return event.getResult();
			}
			
			return 0;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	
	}
	
	@Override
	public int onPlayerEnterVehicle( int playerId, int vehicleId, int isPassenger )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );

			VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player, isPassenger != 0);
			
			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerExitVehicle( int playerId, int vehicleId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			
			VehicleExitEvent event = new VehicleExitEvent(vehicle, player);
			
			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerStateChange( int playerId, int state, int oldState )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerStateChangeEvent event = new PlayerStateChangeEvent( player, oldState );
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerEnterCheckpoint( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				CheckpointEnterEvent event = new CheckpointEnterEvent( player );
				eventManager.dispatchEvent( event, player.getCheckpoint(), player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerLeaveCheckpoint( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);
				eventManager.dispatchEvent( event, player.getCheckpoint(), player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerEnterRaceCheckpoint( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
				eventManager.dispatchEvent( event, player.getRaceCheckpoint(), player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerLeaveRaceCheckpoint( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
				eventManager.dispatchEvent( event, player.getRaceCheckpoint(), player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onRconCommand( String cmd )
	{
		try
		{
			RconCommandEvent event = new RconCommandEvent(cmd);
			eventManager.dispatchEvent( event, Shoebill.getInstance().getManagedObjectPool().getServer() );
			
			return event.getResult();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerRequestSpawn( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent( player );
				eventManager.dispatchEvent( event, player );
				
				return event.getResult();
			}
			
			return 0;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onObjectMoved( int objectId )
	{
		try
		{
			IObject object = sampObjectPool.getObject( objectId );
			
			if( object instanceof ObjectImpl )
			{
				ObjectImpl obj = (ObjectImpl) object;
				obj.processObjectMoved();
				
				ObjectMovedEvent event = new ObjectMovedEvent(object);
				eventManager.dispatchEvent( event, object );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerObjectMoved( int playerId, int objectId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPlayerObject object = sampObjectPool.getPlayerObject( player, objectId );
			
			if( object instanceof PlayerObjectImpl )
			{
				PlayerObjectImpl playerObject = (PlayerObjectImpl) object;
				playerObject.processPlayerObjectMoved();
			}

			PlayerObjectMovedEvent event = new PlayerObjectMovedEvent(player, object);

			if( player instanceof PlayerImpl && object instanceof ObjectImpl )
			{
				eventManager.dispatchEvent( event, object, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( object instanceof ObjectImpl )
			{
				eventManager.dispatchEvent( event, object );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerPickUpPickup( int playerId, int pickupId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPickup pickup = sampObjectPool.getPickup( pickupId );

			PlayerPickupEvent event = new PlayerPickupEvent(player, pickup);
			
			if( pickup instanceof PickupImpl && player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, pickup, player );
			}
			else if( pickup instanceof PickupImpl )
			{
				eventManager.dispatchEvent( event, pickup );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleMod( int playerId, int vehicleId, int componentId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			
			VehicleModEvent event = new VehicleModEvent(vehicle, componentId);
			
			if( vehicle instanceof VehicleImpl )
			{
				VehicleImpl veh = (VehicleImpl) vehicle;
				veh.processVehicleMod();
			}
			
			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onEnterExitModShop( int playerId, int enterexit, int interiorId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerEnterExitModShopEvent event = new PlayerEnterExitModShopEvent(player, enterexit, interiorId);
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehiclePaintjob( int playerId, int vehicleId, int paintjobId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			
			VehiclePaintjobEvent event = new VehiclePaintjobEvent(vehicle, paintjobId);

			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleRespray( int playerId, int vehicleId, int color1, int color2 )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			
			VehicleResprayEvent event = new VehicleResprayEvent( vehicle, color1, color2 );

			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleDamageStatusUpdate( int vehicleId, int playerId )
	{
		try
		{
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( vehicle instanceof VehicleImpl )
			{
				VehicleImpl veh = (VehicleImpl) vehicle;
				veh.processVehicleDamageStatusUpdate();
			}
			
			VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
			
			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onUnoccupiedVehicleUpdate( int vehicleId, int playerId, int passengerSeat )
	{
		
		try
		{
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			VehicleUnoccupiedUpdateEvent event = new VehicleUnoccupiedUpdateEvent( vehicle, player, passengerSeat );
			
			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
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
	public int onPlayerSelectedMenuRow( int playerId, int row )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IMenu menu = player.getMenu();
			
			MenuSelectedEvent event = new MenuSelectedEvent( menu, player, row );

			if( player instanceof PlayerImpl && menu instanceof MenuImpl )
			{
				eventManager.dispatchEvent( event, menu, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( menu instanceof MenuImpl )
			{
				eventManager.dispatchEvent( event, menu );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerExitedMenu( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IMenu menu = player.getMenu();
			
			MenuExitedEvent event = new MenuExitedEvent( menu, player );
			
			if( player instanceof PlayerImpl && menu instanceof MenuImpl )
			{
				eventManager.dispatchEvent( event, menu, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( menu instanceof MenuImpl )
			{
				eventManager.dispatchEvent( event, menu );
			}
				
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	
	}
	
	@Override
	public int onPlayerInteriorChange( int playerId, int interiorId, int oldInteriorId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerImpl pl = (PlayerImpl) player;
				pl.processPlayerInteriorChange();
				
				PlayerInteriorChangeEvent event = new PlayerInteriorChangeEvent(player, oldInteriorId);
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerKeyStateChange( int playerId, int keys, int oldKeys )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerKeyStateChangeEvent event = new PlayerKeyStateChangeEvent(player, oldKeys);
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onRconLoginAttempt( String ip, String password, int isSuccess )
	{
		try
		{
			RconLoginEvent event = new RconLoginEvent( ip, password, isSuccess!=0 );
			eventManager.dispatchEvent( event );
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerUpdate( int playerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );

			if( player instanceof PlayerImpl )
			{
				PlayerImpl pl = (PlayerImpl) player;
				pl.processPlayerUpdate();
				
				PlayerUpdateEvent event = new PlayerUpdateEvent(player);
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerStreamIn( int playerId, int forPlayerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPlayer forPlayer = sampObjectPool.getPlayer( forPlayerId );

			PlayerStreamInEvent event = new PlayerStreamInEvent(player, forPlayer);
			
			if( player instanceof PlayerImpl && forPlayer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player, forPlayer );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( forPlayer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, forPlayer );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
		
	}
	
	@Override
	public int onPlayerStreamOut( int playerId, int forPlayerId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPlayer forPlayer = sampObjectPool.getPlayer( forPlayerId );
			
			PlayerStreamOutEvent event = new PlayerStreamOutEvent(player, forPlayer);

			if( player instanceof PlayerImpl && forPlayer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player, forPlayer );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( forPlayer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, forPlayer );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleStreamIn( int vehicleId, int forPlayerId )
	{
		try
		{
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			IPlayer player = sampObjectPool.getPlayer( forPlayerId );
			
			VehicleStreamInEvent event = new VehicleStreamInEvent(vehicle, player);
			
			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
	
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onVehicleStreamOut( int vehicleId, int forPlayerId )
	{
		try
		{
			IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
			IPlayer player = sampObjectPool.getPlayer( forPlayerId );
			
			VehicleStreamOutEvent event = new VehicleStreamOutEvent(vehicle, player);

			if( player instanceof PlayerImpl && vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( vehicle instanceof VehicleImpl )
			{
				eventManager.dispatchEvent( event, vehicle );
			}
	
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onDialogResponse( int playerId, int dialogId, int response, int listitem, String inputtext )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IDialog dialog = sampObjectPool.getDialog( dialogId );
			
			if( dialog == null ) return 0;
			
			DialogResponseEvent event = new DialogResponseEvent(dialog, player, response, listitem, inputtext);

			if( player instanceof PlayerImpl && dialog instanceof DialogImpl )
			{
				eventManager.dispatchEvent( event, dialog, player );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( dialog instanceof DialogImpl )
			{
				eventManager.dispatchEvent( event, dialog );
			}

			if( player instanceof PlayerImpl )
			{
				PlayerImpl pl = (PlayerImpl) player;
				pl.processDialogResponse();
			}

			return event.getResult();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerTakeDamage( int playerId, int issuerId, float amount, int weaponId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPlayer issuer = sampObjectPool.getPlayer( issuerId );

			if( player instanceof PlayerImpl )
			{
				PlayerTakeDamageEvent event = new PlayerTakeDamageEvent( player, issuer, amount, weaponId );
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}


	@Override
	public int onPlayerGiveDamage( int playerId, int damagedId, float amount, int weaponId )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPlayer victim = sampObjectPool.getPlayer( damagedId );

			if( player instanceof PlayerImpl )
			{
				PlayerGiveDamageEvent event = new PlayerGiveDamageEvent( player, victim, amount, weaponId );
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int onPlayerClickMap( int playerId, float x, float y, float z )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			
			if( player instanceof PlayerImpl )
			{
				PlayerClickMapEvent event = new PlayerClickMapEvent( player, x, y, z );
				eventManager.dispatchEvent( event, player );
			}
			
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerClickPlayer( int playerId, int clickedPlayerId, int source )
	{
		try
		{
			IPlayer player = sampObjectPool.getPlayer( playerId );
			IPlayer clickedPlayer = sampObjectPool.getPlayer( clickedPlayerId );

			PlayerClickPlayerEvent event = new PlayerClickPlayerEvent( player, clickedPlayer, source );

			if( player instanceof PlayerImpl && clickedPlayer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player, clickedPlayer );
			}
			else if( player instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, player );
			}
			else if( clickedPlayer instanceof PlayerImpl )
			{
				eventManager.dispatchEvent( event, clickedPlayer );
			}
			
			return 1;
		}
		catch( Exception e )
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
			
			for( ITimer timer : sampObjectPool.getTimers() ) timer.tick( interval );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
