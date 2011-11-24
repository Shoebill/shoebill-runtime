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
import net.gtaun.shoebill.event.player.PlayerClickPlayerEvent;
import net.gtaun.shoebill.event.player.PlayerCommandEvent;
import net.gtaun.shoebill.event.player.PlayerConnectEvent;
import net.gtaun.shoebill.event.player.PlayerDeathEvent;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.event.player.PlayerEnterExitModShopEvent;
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
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.samp.ISampCallbackHandler;
import net.gtaun.shoebill.util.event.EventDispatcher;

/**
 * @author MK124
 *
 */

public class SampEventDispatcher implements ISampCallbackHandler
{
	SampObjectPool sampObjectPool;
	EventDispatcher globalEventDispatcher;

	long lastProcessTickTimeMillis;
	
	
	public SampEventDispatcher( SampObjectPool pool, EventDispatcher dispatcher )
	{
		sampObjectPool = pool;
		globalEventDispatcher = dispatcher;
		
		lastProcessTickTimeMillis = System.currentTimeMillis();
	}
	
	
	@Override
	public int onGameModeInit()
	{
		return 1;
	}
	
	@Override
	public int onGameModeExit()
	{
		try
		{
			//eventDispatcher.dispatchEvent( new GamemodeExitEvent(this) );
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int onPlayerConnect( int playerId )
	{
		try
		{
			IPlayer player;
			
			try
			{
				player = Player.class.getConstructor( Integer.class ).newInstance( playerId );
			}
			catch( Exception e )
			{
				e.printStackTrace();
				return 0;
			}

			sampObjectPool.setPlayer( playerId, player );
			
			PlayerConnectEvent event = new PlayerConnectEvent( player );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			PlayerDisconnectEvent event = new PlayerDisconnectEvent( player, reason );
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
			sampObjectPool.setPlayer( playerId, null );
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
			PlayerSpawnEvent event = new PlayerSpawnEvent( player );
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			if( killerId != Player.INVALID_ID )
			{
				killer = sampObjectPool.getPlayer(killerId);
				PlayerKillEvent event = new PlayerKillEvent( killer, player, reason );
				
				killer.getEventDispatcher().dispatchEvent( event );
				globalEventDispatcher.dispatchEvent( event );
			}
			
			PlayerDeathEvent event = new PlayerDeathEvent( player, killer, reason );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			VehicleSpawnEvent event = new VehicleSpawnEvent(vehicle);
			
			vehicle.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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

			vehicle.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			PlayerTextEvent event = new PlayerTextEvent( player, text );
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
			if( event.getResult() != 0 )
			{
				Player.sendMessageToAll( Color.WHITE, "{FE8B13}" + player.getName() + ": {FFFFFF}" + text );
				return 1;
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
			PlayerCommandEvent event = new PlayerCommandEvent( player, cmdtext );
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
			return event.getResult();
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
			PlayerRequestClassEvent event = new PlayerRequestClassEvent( player, classId );
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
			return event.getResult();
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
			
			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			PlayerStateChangeEvent event = new PlayerStateChangeEvent( player, oldState );
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			CheckpointEnterEvent event = new CheckpointEnterEvent( player );
			
			player.getCheckpoint().getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);

			player.getCheckpoint().getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
			
			player.getRaceCheckpoint().getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
			
			player.getRaceCheckpoint().getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			globalEventDispatcher.dispatchEvent( event );
			
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
			PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent( player );
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
			return event.getResult();
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
			ObjectMovedEvent event = new ObjectMovedEvent(object);
			
			object.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			PlayerObjectMovedEvent event = new PlayerObjectMovedEvent(player, object);
			ObjectMovedEvent objectMovedEvent = new ObjectMovedEvent( object );
			
			object.getEventDispatcher().dispatchEvent( event );
			object.getEventDispatcher().dispatchEvent( objectMovedEvent );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			pickup.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			PlayerEnterExitModShopEvent event = new PlayerEnterExitModShopEvent(player, enterexit, interiorId);
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
	
			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
	
			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			//SampNativeFunction.getVehicleDamageStatus(vehicleid, vehicle.damage);
			
			VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
			
			vehicle.getEventDispatcher().dispatchEvent( event );
			if( player != null ) player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
			//player.on();
			//player.event.dispatchEvent( new Player(player) );
			
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

			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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

			menu.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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

			menu.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
				
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
			PlayerInteriorChangeEvent event = new PlayerInteriorChangeEvent(player, oldInteriorId);
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			PlayerKeyStateChangeEvent event = new PlayerKeyStateChangeEvent(player, oldKeys);
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			globalEventDispatcher.dispatchEvent( event );
			
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
			PlayerUpdateEvent event = new PlayerUpdateEvent(player);
			
			//player.update();
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			
			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
	
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
			
			vehicle.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
	
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

			dialog.getEventDispatcher().dispatchEvent( event );
			player.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
			//player.dialog = null;
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
			
			player.getEventDispatcher().dispatchEvent( event );
			clickedPlayer.getEventDispatcher().dispatchEvent( event );
			globalEventDispatcher.dispatchEvent( event );
			
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
			int interval = (int) (nowTick - lastProcessTickTimeMillis);
			lastProcessTickTimeMillis = nowTick;
			
			for( ITimer timer : sampObjectPool.getTimers() ) timer.tick( interval );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
