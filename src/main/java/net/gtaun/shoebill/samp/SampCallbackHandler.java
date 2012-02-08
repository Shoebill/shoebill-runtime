package net.gtaun.shoebill.samp;

public abstract class SampCallbackHandler implements ISampCallbackHandler
{
	protected SampCallbackHandler()
	{
		
	}
	
	
	public int onGameModeInit()
	{
		return 1;
	}

	public int onGameModeExit()
	{
		return 1;
	}

	public int onPlayerConnect( int playerid )
	{
		return 1;
	}

	public int onPlayerDisconnect( int playerid, int reason )
	{
		return 1;
	}

	public int onPlayerSpawn( int playerid )
	{
		return 1;
	}

	public int onPlayerDeath( int playerid, int killerid, int reason )
	{
		return 1;
	}

	public int onVehicleSpawn( int vehicleid )
	{
		return 1;
	}

	public int onVehicleDeath( int vehicleid, int killerid )
	{
		return 1;
	}

	public int onPlayerText( int playerid, String text )
	{
		return 1;
	}

	public int onPlayerCommandText( int playerid, String cmdtext )
	{
		return 0;
	}

	public int onPlayerRequestClass( int playerid, int classid )
	{
		return 1;
	}

	public int onPlayerEnterVehicle( int playerid, int vehicleid, int ispassenger )
	{
		return 1;
	}

	public int onPlayerExitVehicle( int playerid, int vehicleid )
	{
		return 1;
	}

	public int onPlayerStateChange( int playerid, int newstate, int oldstate )
	{
		return 1;
	}

	public int onPlayerEnterCheckpoint( int playerid )
	{
		return 1;
	}

	public int onPlayerLeaveCheckpoint( int playerid )
	{
		return 1;
	}

	public int onPlayerEnterRaceCheckpoint( int playerid )
	{
		return 1;
	}

	public int onPlayerLeaveRaceCheckpoint( int playerid )
	{
		return 1;
	}

	public int onRconCommand( String cmd )
	{
		return 0;
	}

	public int onPlayerRequestSpawn( int playerid )
	{
		return 1;
	}

	public int onObjectMoved( int objectid )
	{
		return 1;
	}

	public int onPlayerObjectMoved( int playerid, int objectid )
	{
		return 1;
	}
	
	public int onPlayerPickUpPickup( int playerid, int pickupid )
	{
		return 1;
	}

	public int onVehicleMod( int playerid, int vehicleid, int componentid )
	{
		return 1;
	}

	public int onEnterExitModShop( int playerid, int enterexit, int interiorid )
	{
		return 1;
	}

	public int onVehiclePaintjob( int playerid, int vehicleid, int paintjobid )
	{
		return 1;
	}

	public int onVehicleRespray( int playerid, int vehicleid, int color1, int color2 )
	{
		return 1;
	}

	public int onVehicleDamageStatusUpdate( int vehicleid, int playerid )
	{
		return 1;
	}

	public int onUnoccupiedVehicleUpdate( int vehicleid, int playerid, int passenger_seat )
	{
		return 1;
	}

	public int onPlayerSelectedMenuRow( int playerid, int row )
	{
		return 1;
	}

	public int onPlayerExitedMenu( int playerid )
	{
		return 1;
	}

	public int onPlayerInteriorChange( int playerid, int newinteriorid, int oldinteriorid )
	{
		return 1;
	}

	public int onPlayerKeyStateChange( int playerid, int newkeys, int oldkeys )
	{
		return 1;
	}

	public int onRconLoginAttempt( String ip, String password, int success )
	{
		return 1;
	}

	public int onPlayerUpdate( int playerid )
	{
		return 1;
	}

	public int onPlayerStreamIn( int playerid, int forplayerid )
	{
		return 1;
	}

	public int onPlayerStreamOut( int playerid, int forplayerid )
	{
		return 1;
	}

	public int onVehicleStreamIn( int vehicleid, int forplayerid )
	{
		return 1;
	}

	public int onVehicleStreamOut( int vehicleid, int forplayerid )
	{
		return 1;
	}

	public int onDialogResponse( int playerid, int dialogid, int response, int listitem, String inputtext )
	{
		return 0;
	}

	public int onPlayerTakeDamage( int playerId, int issuerId, float amount, int weaponId )
	{
		return 1;
	}

	public int onPlayerGiveDamage( int playerId, int damagedId, float amount, int weaponId )
	{
		return 1;
	}

	public int onPlayerClickMap( int playerId, float x, float y, float z )
	{
		return 1;
	}
	
	public int onPlayerClickPlayer( int playerid, int clickedplayerid, int source )
	{
		return 1;
	}

	public void onProcessTick()
	{
		
	}
}
