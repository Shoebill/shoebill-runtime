#include <a_samp>

#define _DEBUG 0

forward MKAC_SetPlayerPos( playerid, Float:x, Float:y, Float:z );
forward MKAC_FixPlayerPos( playerid, Float:x, Float:y, Float:z );
forward MKAC_FixVehicleHealth( vehicleid, Float:health );


new PlayerSpeedTick[MAX_PLAYERS];
new Float:PlayerPos[MAX_PLAYERS][3];

new PlayerAirbrkCount[MAX_PLAYERS];
new Float:PlayerAirbrkRate[MAX_PLAYERS];

new Float:PosSpeed[MAX_PLAYERS];
new Float:VelSpeed[MAX_PLAYERS];

new PlayerSpeedSkipTick[MAX_PLAYERS];

new PlayerLastMoney[MAX_PLAYERS];
new PlayerVehicleHealthChangeUpdate[MAX_PLAYERS];
new Float:PlayerVehicleHealth[MAX_PLAYERS];


enum StateType
{
	ST_SPAWNED,
	ST_ISDRIVER,
	ST_ISPASSENGER,
}



public MKAC_SetPlayerPos( playerid, Float:x, Float:y, Float:z )
{
    PlayerSpeedSkipTick[playerid] = GetTickCount()+1000;
	PlayerPos[playerid][0] = x;
	PlayerPos[playerid][1] = y;
	PlayerPos[playerid][2] = z;
	return SetPlayerPos( playerid, x, y, z );
}
public MKAC_FixPlayerPos( playerid, Float:x, Float:y, Float:z )
{
    PlayerSpeedSkipTick[playerid] = GetTickCount()+1000;
	PlayerPos[playerid][0] = x;
	PlayerPos[playerid][1] = y;
	PlayerPos[playerid][2] = z;
}
public MKAC_FixVehicleHealth( vehicleid, Float:health )
{
	for( new i=0; i<MAX_PLAYERS; i++ )
	{
		if( GetPlayerVehicleID(i) != vehicleid ) continue;
		PlayerVehicleHealthChangeUpdate[i] = 10;
		PlayerVehicleHealth[i] = health;
	}
}


forward UpdateSpeed();
public UpdateSpeed()
{
	new nowTick = GetTickCount();

	for( new i=0; i<MAX_PLAYERS; i++ )
	{
	    if( IsPlayerConnected(i) == 0 ) continue;

		new Float:fixedvs = VelSpeed[i] <= 0.2 ? 0.2 : VelSpeed[i];
		new Float:rate = PosSpeed[i] / fixedvs;

        PlayerAirbrkRate[i] = PlayerAirbrkRate[i] + rate;

        new playerState = GetPlayerState(i);
        //new playerVehicle = GetPlayerVehicleID(i);
		if( (nowTick > PlayerSpeedSkipTick[i]) &&
			((playerState == PLAYER_STATE_DRIVER && PosSpeed[i]>0.5 && rate>=5.0) ||
			(playerState == PLAYER_STATE_ONFOOT && GetPlayerSurfingVehicleID(i) == INVALID_VEHICLE_ID && VelSpeed[i]>0.2 && rate>=5.0))
			)
		{
		    PlayerAirbrkCount[i]++;
		    
			if(
			 //(PlayerAirbrkCount[i] >= 2 && PlayerAirbrkRate[i] >= 15.0) ||
			 (PlayerAirbrkCount[i] >= 3 && PlayerAirbrkRate[i] >= 15.0)
			  )
			{
			    #if _DEBUG
				    new str[64];
	           		format( str, sizeof(str), "AntiCheat: AirBrk ( %.1f, %.1f, %.2fx, acc%.2fx )", PosSpeed[i], VelSpeed[i], rate, PlayerAirbrkRate[i] );
					SendClientMessage( i, 0xff000000, str );
				#endif
				
				CallRemoteFunction( "MKAC_OnPlayerCheat", "iii", i, 0, 0 );
			}
			else
			{
			    #if _DEBUG
				    new str[64];
					format( str, sizeof(str), "AntiCheat: Pre-AirBrk ( %.1f, %.1f, %.2fx )", PosSpeed[i], VelSpeed[i], rate );
					SendClientMessage( i, 0xffff0000, str );
				#endif
			}
		}
		else
		{
			PlayerAirbrkCount[i] = 0;
			PlayerAirbrkRate[i] = 0;
		}

		PosSpeed[i] = 0;
		VelSpeed[i] = 0;
	}
}

public OnFilterScriptInit()
{
	for( new i=0; i<MAX_PLAYERS; i++ ) if( IsPlayerConnected(i) ) OnPlayerConnect(i);
	for( new i=0; i<MAX_PLAYERS; i++ )
	{
		PlayerVehicleHealth[i] = 1000.0;
		PlayerVehicleHealthChangeUpdate[i] = -1;
   		PlayerSpeedSkipTick[i] = GetTickCount()+1000;
	}

	SetTimer( "UpdateSpeed", 150, true );
	
//	SendClientMessageToAll( 0xFFFFFFFF, "Now Working: MK124's Anti Cheat System. (AB/VHP)" );
	return 1;
}

public OnFilterScriptExit()
{
	for( new i=0; i<MAX_PLAYERS; i++ ) if( IsPlayerConnected(i) ) OnPlayerDisconnect(i, 0);
	return 1;
}

public OnGameModeInit()
{
	return 1;
}

public OnGameModeExit()
{
	return 1;
}

public OnPlayerRequestClass(playerid, classid)
{
	return 1;
}

public OnPlayerConnect(playerid)
{
	PlayerSpeedTick[playerid] = GetTickCount();
	PlayerVehicleHealthChangeUpdate[playerid] = -1;
	return 1;
}

public OnPlayerDisconnect(playerid, reason)
{
	return 1;
}

public OnPlayerSpawn(playerid)
{
	return 1;
}

public OnPlayerDeath(playerid, killerid, reason)
{
	return 1;
}

public OnVehicleSpawn(vehicleid)
{
	return 1;
}

public OnVehicleDeath(vehicleid, killerid)
{
	return 1;
}

public OnPlayerText(playerid, text[])
{
	return 1;
}

public OnPlayerCommandText(playerid, cmdtext[])
{
	return 0;
}

public OnPlayerEnterVehicle(playerid, vehicleid, ispassenger)
{
	PlayerVehicleHealth[playerid] = 1000.0;
	PlayerVehicleHealthChangeUpdate[playerid] = -1;

   	PlayerSpeedSkipTick[playerid] = GetTickCount()+1000;
	return 1;
}

public OnPlayerExitVehicle(playerid, vehicleid)
{
	SetVehicleHealth( vehicleid, PlayerVehicleHealth[playerid] );
	PlayerVehicleHealthChangeUpdate[playerid] = -1;
	return 1;
}

public OnPlayerStateChange(playerid, newstate, oldstate)
{
	//if( newstate == PLAYER_STATE_DRIVER )
	//{
	//
	//}
	return 1;
}

public OnPlayerEnterCheckpoint(playerid)
{
	return 1;
}

public OnPlayerLeaveCheckpoint(playerid)
{
	return 1;
}

public OnPlayerEnterRaceCheckpoint(playerid)
{
	return 1;
}

public OnPlayerLeaveRaceCheckpoint(playerid)
{
	return 1;
}

public OnRconCommand(cmd[])
{
	return 1;
}

public OnPlayerRequestSpawn(playerid)
{
	return 1;
}

public OnObjectMoved(objectid)
{
	return 1;
}

public OnPlayerObjectMoved(playerid, objectid)
{
	return 1;
}

public OnPlayerPickUpPickup(playerid, pickupid)
{
	return 1;
}

public OnVehicleMod(playerid, vehicleid, componentid)
{
	return 1;
}

public OnVehiclePaintjob(playerid, vehicleid, paintjobid)
{
	return 1;
}

public OnVehicleRespray(playerid, vehicleid, color1, color2)
{
	return 1;
}

public OnPlayerSelectedMenuRow(playerid, row)
{
	return 1;
}

public OnPlayerExitedMenu(playerid)
{
	return 1;
}

public OnPlayerInteriorChange(playerid, newinteriorid, oldinteriorid)
{
	return 1;
}

public OnPlayerKeyStateChange(playerid, newkeys, oldkeys)
{
	return 1;
}

public OnRconLoginAttempt(ip[], password[], success)
{
	return 1;
}

public OnPlayerUpdate(playerid)
{
	new nowstate = GetPlayerState(playerid);
	new vehicleid = GetPlayerVehicleID(playerid);
	new money = GetPlayerMoney(playerid);
	new nowTick = GetTickCount(); //-GetPlayerPing(playerid)/2;

// Money
	if( money == PlayerLastMoney[playerid]-100 ||
		money == PlayerLastMoney[playerid]-150 ||
		money == PlayerLastMoney[playerid]-500  )
	{
		PlayerVehicleHealthChangeUpdate[playerid] = -1;
		PlayerVehicleHealth[playerid] = 1000.0;
	}

    PlayerLastMoney[playerid] = money;


// Vehicle Health Cheat
	if( IsPlayerAdmin(playerid) && nowstate == PLAYER_STATE_DRIVER )
	{
		new Float:vhp;
		GetVehicleHealth( vehicleid, vhp );
		
		if( PlayerVehicleHealthChangeUpdate[playerid] >= 0 )
			PlayerVehicleHealthChangeUpdate[playerid]--;
	
   		if( PlayerVehicleHealthChangeUpdate[playerid] == 0 &&
			vhp > PlayerVehicleHealth[playerid] )
		{
			//#if _DEBUG
				new str[64];
	           	format( str, sizeof(str), "AntiCheat: Vehicle Health Cheat (INC %f).", vhp - PlayerVehicleHealth[playerid] );
				SendClientMessage( playerid, 0xff000000, str );
			//#endif
		
			//CallRemoteFunction( "MKAC_OnPlayerCheat", "iii", playerid, 2, 0 );

			SetVehicleHealth( vehicleid, PlayerVehicleHealth[playerid] );
			PlayerVehicleHealthChangeUpdate[playerid] = -1;
		}
	    else if( PlayerVehicleHealthChangeUpdate[playerid] < 0 )
	    {
			if( vhp > PlayerVehicleHealth[playerid] )
				PlayerVehicleHealthChangeUpdate[playerid] = 20;
			else
				PlayerVehicleHealth[playerid] = vhp;
		}
	}


// Speed / AirBrk
	new Float:x, Float:y, Float:z;
	if( IsPlayerInAnyVehicle(playerid) )	GetVehicleVelocity( vehicleid, x, y, z );
	else									GetPlayerVelocity( playerid, x, y, z );

	new Float:spd = floatsqroot( x*x + y*y + z*z ) * (nowTick-PlayerSpeedTick[playerid]) / 20.0;

	GetPlayerPos( playerid, x, y, z );
	new Float:vx = x-PlayerPos[playerid][0];
	new Float:vy = y-PlayerPos[playerid][1];
	new Float:vz = z-PlayerPos[playerid][2];
	new Float:spdPos = floatsqroot( vx*vx + vy*vy + vz*vz );

	PlayerPos[playerid][0] = x;
	PlayerPos[playerid][1] = y;
	PlayerPos[playerid][2] = z;
	PlayerSpeedTick[playerid] = nowTick;

	PosSpeed[playerid] += spdPos;
	VelSpeed[playerid] += spd;

	return 1;
}

