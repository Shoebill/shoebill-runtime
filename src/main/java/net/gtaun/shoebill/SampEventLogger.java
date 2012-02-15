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

import net.gtaun.shoebill.object.IPickup;
import net.gtaun.shoebill.object.IPlayer;
import net.gtaun.shoebill.object.IVehicle;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.samp.ISampCallbackHandler;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124
 *
 */

public class SampEventLogger implements ISampCallbackHandler
{
	private ISampObjectPool sampObjectPool;
	
	
	public SampEventLogger( ISampObjectPool pool )
	{
		sampObjectPool = pool;
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
		Shoebill.LOGGER.info( "[join] " + SampNativeFunction.getPlayerName(playerId) + " has joined the server (" + playerId + ":" + SampNativeFunction.getPlayerIp(playerId) + ")" );
		return 1;
	}
	
	@Override
	public int onPlayerDisconnect( int playerId, int reason )
	{
		Shoebill.LOGGER.info( "[part] " + SampNativeFunction.getPlayerName(playerId) + " has left the server (" + playerId + ":" + reason + ")" );
		return 1;
	}
	
	@Override
	public int onPlayerSpawn( int playerId )
	{
		IPlayer player = sampObjectPool.getPlayer(playerId);
		Shoebill.LOGGER.info( "[spawn] " + player.getName() + " has spawned (" + playerId + ")" );
	
		return 1;
	}
	
	@Override
	public int onPlayerDeath( int playerId, int killerId, int reason )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		
		if( killerId == Player.INVALID_ID )
		{
			Shoebill.LOGGER.info( "[death] " + player.getName() + " died (" + playerId + ":" + reason + ")" );
			return 1;
		}
		
		IPlayer killer = sampObjectPool.getPlayer( killerId );
		Shoebill.LOGGER.info( "[kill] " + killer.getName() + " killed " + player.getName() + " (" + SampNativeFunction.getWeaponName(reason) + ")" );
		return 1;
	}
	
	@Override
	public int onVehicleSpawn( int vehicleId )
	{
		return 1;
	}
	
	@Override
	public int onVehicleDeath( int vehicleId, int killerId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerText( int playerId, String text )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		Shoebill.LOGGER.info( "[chat] [" + player.getName() + "]: " + text );
		
		return 1;
	}
	
	@Override
	public int onPlayerCommandText( int playerId, String cmdtext )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		Shoebill.LOGGER.info( "[cmd] [" + player.getName() + "] " + cmdtext );
		
		return 1;
	}
	
	@Override
	public int onPlayerRequestClass( int playerId, int classId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerEnterVehicle( int playerId, int vehicleId, int isPassenger )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
		
		Shoebill.LOGGER.info( "[vehicle] " + player.getName() + " enter a vehicle (" + vehicle.getModelId() + ")" );
		return 1;
	}
	
	@Override
	public int onPlayerExitVehicle( int playerId, int vehicleId )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		IVehicle vehicle = sampObjectPool.getVehicle( vehicleId );
		
		Shoebill.LOGGER.info( "[vehicle] " + player.getName() + " leave a vehicle (" + vehicle.getModelId() + ")" );
		return 1;
	}
	
	@Override
	public int onPlayerStateChange( int playerId, int state, int oldState )
	{
		return 1;
	}
	
	@Override
	public int onPlayerEnterCheckpoint( int playerId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerLeaveCheckpoint( int playerId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerEnterRaceCheckpoint( int playerId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerLeaveRaceCheckpoint( int playerId )
	{
		return 1;
	}
	
	@Override
	public int onRconCommand( String cmd )
	{
		Shoebill.LOGGER.info( "[rcon] " + " command: " + cmd );
		return 1;
	}
	
	@Override
	public int onPlayerRequestSpawn( int playerId )
	{
		return 1;
	}
	
	@Override
	public int onObjectMoved( int objectId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerObjectMoved( int playerId, int objectId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerPickUpPickup( int playerId, int pickupId )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		IPickup pickup = sampObjectPool.getPickup( pickupId );
		
		Shoebill.LOGGER.info( "[pickup] " + player.getName() + " pickup " + pickup.getModelId() + " (" + pickup.getType() + ")" );
		return 1;
	}
	
	@Override
	public int onVehicleMod( int playerId, int vehicleId, int componentId )
	{
		return 1;
	}
	
	@Override
	public int onEnterExitModShop( int playerId, int enterexit, int interiorId )
	{
		return 1;
	}
	
	@Override
	public int onVehiclePaintjob( int playerId, int vehicleId, int paintjobId )
	{
		return 1;
	}
	
	@Override
	public int onVehicleRespray( int playerId, int vehicleId, int color1, int color2 )
	{
		return 1;
	}
	
	@Override
	public int onVehicleDamageStatusUpdate( int vehicleId, int playerId )
	{
		return 1;
	}
	
	@Override
	public int onUnoccupiedVehicleUpdate( int vehicleId, int playerId, int passengerSeat )
	{
		return 1;
	}
	
	@Override
	public int onPlayerSelectedMenuRow( int playerId, int row )
	{
		return 1;
	}
	
	@Override
	public int onPlayerExitedMenu( int playerId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerInteriorChange( int playerId, int interiorId, int oldInteriorId )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		Shoebill.LOGGER.info( "[interior] " + player.getName() + " interior has changed to " + interiorId );
	
		return 1;
	}
	
	@Override
	public int onPlayerKeyStateChange( int playerId, int keys, int oldKeys )
	{
		return 1;
	}
	
	@Override
	public int onRconLoginAttempt( String ip, String password, int isSuccess )
	{
		if( isSuccess == 0 )
			Shoebill.LOGGER.info( "[rcon] " + " bad rcon attempy by: " + ip + " (" + password + ")" );
		else
			Shoebill.LOGGER.info( "[rcon] " + ip + " has logged." );
		
		return 1;
	}
	
	@Override
	public int onPlayerUpdate( int playerId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerStreamIn( int playerId, int forPlayerId )
	{
		return 1;
	}
	
	@Override
	public int onPlayerStreamOut( int playerId, int forPlayerId )
	{
		return 1;
	}
	
	@Override
	public int onVehicleStreamIn( int vehicleId, int forPlayerId )
	{
		return 1;
	}
	
	@Override
	public int onVehicleStreamOut( int vehicleId, int forPlayerId )
	{
		return 1;
	}
	
	@Override
	public int onDialogResponse( int playerId, int dialogId, int response, int listitem, String inputtext )
	{
		return 1;
	}
	
	@Override
	public int onPlayerTakeDamage( int playerid, int issuerId, float amount, int weaponid )
	{
		return 1;
	}


	@Override
	public int onPlayerGiveDamage( int playerId, int damagedId, float amount, int weaponId )
	{
		return 1;
	}


	@Override
	public int onPlayerClickMap( int playerId, float x, float y, float z )
	{
		return 1;
	}
	
	@Override
	public int onPlayerClickPlayer( int playerId, int clickedPlayerId, int source )
	{
		IPlayer player = sampObjectPool.getPlayer( playerId );
		IPlayer clickedPlayer = sampObjectPool.getPlayer( clickedPlayerId );
		
		Shoebill.LOGGER.info( "[click] " + player.getName() + " has clicked " + clickedPlayer.getName() );
		return 1;
	}
	
	@Override
	public void onProcessTick()
	{
		
	}
}
