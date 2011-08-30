#include <a_samp>

forward MKAC_SetPlayerPos( playerid, Float:x, Float:y, Float:z );
forward MKAC_FixVehicleHealth( vehicleid, Float:health );


new PlayerSpeedTick[MAX_PLAYERS];
new Float:PlayerPos[MAX_PLAYERS][3];
new Text:PlayerSpeedText[MAX_PLAYERS];

new PlayerAirbrkCount[MAX_PLAYERS];
new Float:PlayerAirbrkRate[MAX_PLAYERS];

new Float:PosSpeed[MAX_PLAYERS];
new Float:VelSpeed[MAX_PLAYERS];

new PlayerSpeedSkipTick[MAX_PLAYERS];

new PlayerLastMoney[MAX_PLAYERS];
new PlayerVehicleHealthChangeTick[MAX_PLAYERS];
new Float:PlayerVehicleHealth[MAX_PLAYERS];


enum StateType
{
	ST_SPAWNED,
	ST_ISDRIVER,
	ST_ISPASSENGER,
}


//new PlayerState[MAX_PLAYERS][StateType];
new PlayerWeapon[MAX_PLAYERS][13][2];



public MKAC_SetPlayerPos( playerid, Float:x, Float:y, Float:z )
{
    PlayerSpeedSkipTick[playerid] = GetTickCount()+1000;
	PlayerPos[playerid][0] = x;
	PlayerPos[playerid][1] = y;
	PlayerPos[playerid][2] = z;
	return SetPlayerPos( playerid, x, y, z );
}

public MKAC_FixVehicleHealth( vehicleid, Float:health )
{
	for( new i=0; i<MAX_PLAYERS; i++ )
	{
		if( GetPlayerVehicleID(i) != vehicleid ) continue;
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
	    if( IsPlayerAdmin(i) == 0 ) continue;

		new Float:fixedvs = VelSpeed[i] <= 0.2 ? 0.2 : VelSpeed[i];
		new Float:rate = PosSpeed[i] / fixedvs;

		new str[64];
		format( str, sizeof(str), "[INC] %.1f %.1f M/S / %.2fx", PosSpeed[i], VelSpeed[i], rate );
		TextDrawSetString( PlayerSpeedText[i], str );

        PlayerAirbrkRate[i] = PlayerAirbrkRate[i] + rate;
        new playerState = GetPlayerState(i);
		if( (nowTick > PlayerSpeedSkipTick[i]) &&
			((playerState == PLAYER_STATE_DRIVER && PosSpeed[i]>0.5 && rate>=5.0) ||
			(playerState == PLAYER_STATE_ONFOOT && GetPlayerSurfingVehicleID(i) == INVALID_VEHICLE_ID && VelSpeed[i]>0.2 && rate>=5.0))
			)
		{
		    PlayerAirbrkCount[i]++;

			if(
			 (PlayerAirbrkCount[i] >= 2 && PlayerAirbrkRate[i] >= 15.0) ||
			 (PlayerAirbrkCount[i] >= 3 && PlayerAirbrkRate[i] >= 5.0)
			  )
			{
           		format( str, sizeof(str), "AntiCheat: AirBrk ( %.1f, %.1f, %.2fx, acc%.2fx )", PosSpeed[i], VelSpeed[i], rate, PlayerAirbrkRate[i] );
				SendClientMessage( i, 0xff000000, str );
			}
			else
			{
				format( str, sizeof(str), "AntiCheat: Pre-AirBrk ( %.1f, %.1f, %.2fx )", PosSpeed[i], VelSpeed[i], rate );
				SendClientMessage( i, 0xffff0000, str );
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

forward UpdatePlayerSpeed(i);
public UpdatePlayerSpeed(i)
{
	new nowTick = GetTickCount();

		new Float:fixedvs = VelSpeed[i] <= 0.1 ? 0.1 : VelSpeed[i];
		new Float:rate = PosSpeed[i] / fixedvs;

		new str[64];
		format( str, sizeof(str), "[INC] %.1f %.1f M/S / %.2fx", PosSpeed[i], VelSpeed[i], rate );
		TextDrawSetString( PlayerSpeedText[i], str );

        PlayerAirbrkRate[i] = PlayerAirbrkRate[i] + rate;
        new playerState = GetPlayerState(i);
		if( (nowTick > PlayerSpeedSkipTick[i]) &&
			((playerState == PLAYER_STATE_DRIVER && PosSpeed[i]>0.1 && rate>=5.0) ||
			(playerState == PLAYER_STATE_ONFOOT && GetPlayerSurfingVehicleID(i) == INVALID_VEHICLE_ID && VelSpeed[i]>0.1 && rate>=5.0))
			)
		{
		    PlayerAirbrkCount[i]++;

			if(
			 (PlayerAirbrkCount[i] >= 4 && PlayerAirbrkRate[i] >= 20.0)
			  )
			{
           		format( str, sizeof(str), "AntiCheat: AirBrk ( %.1f, %.1f, %.2fx, acc%.2fx )", PosSpeed[i], VelSpeed[i], rate, PlayerAirbrkRate[i] );
				SendClientMessage( i, 0xff000000, str );
			}
			else
			{
				format( str, sizeof(str), "AntiCheat: Pre-AirBrk ( %.1f, %.1f, %.2fx )", PosSpeed[i], VelSpeed[i], rate );
				SendClientMessage( i, 0xffff0000, str );
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

stock strrmchr( str[], chr )
{
	for( new i=0, cur=-1, mbcs=0; cur==-1 || str[cur]!=0; mbcs = !mbcs && str[i] > 0x7F, str[++cur] = str[i++] )
		if( str[i] == chr && !mbcs ) while( str[i] == ' ' ) i++;
}

public OnFilterScriptInit()
{
	new str[63] = "  B la nk Scr ipt中文 测   试    ";
	print(str);
	strrmchr(str,' ');
	print(str);

	for( new i=0; i<MAX_PLAYERS; i++ ) if( IsPlayerConnected(i) ) OnPlayerConnect(i);
	for( new i=0; i<MAX_PLAYERS; i++ )
	{
		PlayerVehicleHealth[i] = 1000.0;
		PlayerVehicleHealthChangeTick[i] = 0;
   		PlayerSpeedSkipTick[i] = GetTickCount()+1000;
	}

	//SetTimer( "UpdateSpeed", 150, true );
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
	PlayerSpeedText[playerid] = TextDrawCreate( 540.0, 360.0, " " );
	
	TextDrawTextSize( PlayerSpeedText[playerid], 4, 8 );
	TextDrawAlignment( PlayerSpeedText[playerid], 3 );
	TextDrawShowForPlayer( playerid, PlayerSpeedText[playerid] );
	return 1;
}

public OnPlayerDisconnect(playerid, reason)
{
	TextDrawDestroy( PlayerSpeedText[playerid] );
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
	return 1;
}

public OnPlayerExitVehicle(playerid, vehicleid)
{
	PlayerVehicleHealthChangeTick[playerid] = 0;
	return 1;
}

public OnPlayerStateChange(playerid, newstate, oldstate)
{
	if( newstate == PLAYER_STATE_DRIVER )
	{
    	PlayerSpeedSkipTick[playerid] = GetTickCount()+500;
    
		PlayerVehicleHealth[playerid] = 1000.0;
		PlayerVehicleHealthChangeTick[playerid] = 0;
	}
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
   	PlayerSpeedSkipTick[playerid] = GetTickCount()+500;
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
		PlayerVehicleHealthChangeTick[playerid] = 0;
		PlayerVehicleHealth[playerid] = 1000.0;
	}

    PlayerLastMoney[playerid] = money;


// Vehicle Health Cheat
	if( nowstate == PLAYER_STATE_DRIVER )
	{
   		if( PlayerVehicleHealthChangeTick[playerid] != 0 &&
		    nowTick > PlayerVehicleHealthChangeTick[playerid]+1000 )
		{
		    new message[64];
		    format( message, sizeof(message), "车辆HP被异常更改。" );
			SendClientMessage( playerid, 0xFF3333FF, message );

			SetVehicleHealth( vehicleid, PlayerVehicleHealth[playerid] );
			PlayerVehicleHealthChangeTick[playerid] = 0;
		}
	    else if( PlayerVehicleHealthChangeTick[playerid] == 0 )
	    {
		    new Float:vhp;
			GetVehicleHealth( vehicleid, vhp );
			if( vhp > PlayerVehicleHealth[playerid] )
				PlayerVehicleHealthChangeTick[playerid] = GetTickCount();
			else
				PlayerVehicleHealth[playerid] = vhp;
		}
	}

// Weapon / Ammo
	for( new i=0; i<13; i++ )
	{
	    new weapon, ammo;
		GetPlayerWeaponData( playerid, i, weapon, ammo );
		
		if( weapon!=0 && (weapon != PlayerWeapon[playerid][i][0] || ammo > PlayerWeapon[playerid][i][1]) )
		{
			SendClientMessage( playerid, 0xFF3333FF, "新增武器/弹药。" );
		}
		
		PlayerWeapon[playerid][i][0] = weapon;
		PlayerWeapon[playerid][i][1] = ammo;
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
	
	UpdatePlayerSpeed( playerid );
	return 1;
}

public OnPlayerStreamIn(playerid, forplayerid)
{
	return 1;
}

public OnPlayerStreamOut(playerid, forplayerid)
{
	return 1;
}

public OnVehicleStreamIn(vehicleid, forplayerid)
{
	return 1;
}

public OnVehicleStreamOut(vehicleid, forplayerid)
{
	return 1;
}

public OnDialogResponse(playerid, dialogid, response, listitem, inputtext[])
{
	return 0;
}

public OnPlayerClickPlayer(playerid, clickedplayerid, source)
{
	return 1;
}

