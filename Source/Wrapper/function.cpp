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

#include "amx.h"
#include "a_samp.h"


//----------------------------------------------------------
// a_samp.inc

// Util

int SendClientMessage( int playerid, int color, const char* message )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SendClientMessage");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, color, amx_NewString(pAMX, message),
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[3] );
	return ret;
}

int SendClientMessageToAll( int color, const char* message )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SendClientMessageToAll");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		color, amx_NewString(pAMX, message)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	return ret;
}

int SendPlayerMessageToPlayer( int playerid, int senderid, const char* message )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SendPlayerMessageToPlayer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, senderid, amx_NewString(pAMX, message)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[3] );
	return ret;
}

int SendPlayerMessageToAll( int senderid, const char* message )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SendPlayerMessageToAll");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		senderid, amx_NewString(pAMX, message)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	return ret;
}

int SendDeathMessage( int killer, int victim, int weapon )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SendDeathMessage");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		killer, victim, weapon
	};

	return func(pAMX, args);
}

int GameTextForAll( const char* string, int time, int style )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GameTextForAll");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, string), time, style
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[1] );
	return ret;
}

int GameTextForPlayer( int playerid, const char* string, int time, int style )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GameTextForPlayer");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_NewString(pAMX, string), time, style
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	return ret;
}

int SetTimer( int timerIndex, int interval, int repeating )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetTimer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		timerIndex, interval, repeating
	};

	return func(pAMX, args);
}

int KillTimer( int timerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "KillTimer");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		timerid
	};

	return func(pAMX, args);
}

int GetMaxPlayers()
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetMaxPlayers");

	cell args[1] =
	{
		sizeof(args)- sizeof(cell),
	};

	return func(pAMX, args);
}


// Game

int SetGameModeText( const char* text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetGameModeText");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, text)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[1] );
	return ret;
}

int SetTeamCount( int count )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetTeamCount");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		count
	};

	return func(pAMX, args);
}

int AddPlayerClass( int modelid, float x, float y, float z, float z_angle, int weapon1id, int weapon1ammo, int weapon2id, int weapon2ammo, int weapon3id, int weapon3ammo )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AddPlayerClass");

	cell args[12] =
	{
		sizeof(args)- sizeof(cell),
		modelid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(z_angle), weapon1id, weapon1ammo, weapon2id, weapon2ammo, weapon3id, weapon3ammo
	};

	return func(pAMX, args);
}

int AddPlayerClassEx( int teamid, int modelid, float x, float y, float z, float z_angle, int weapon1id, int weapon1ammo, int weapon2id, int weapon2ammo, int weapon3id, int weapon3ammo )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AddPlayerClassEx");

	cell args[13] =
	{
		sizeof(args)- sizeof(cell),
		teamid, modelid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(z_angle), weapon1id, weapon1ammo, weapon2id, weapon2ammo, weapon3id, weapon3ammo
	};

	return func(pAMX, args);
}

int AddStaticVehicle( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2 )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AddStaticVehicle");

	cell args[8] =
	{
		sizeof(args)- sizeof(cell),
		modelid, amx_ftoc(spawn_x), amx_ftoc(spawn_y), amx_ftoc(spawn_z), amx_ftoc(z_angle), color1, color2
	};

	return func(pAMX, args);
}

int AddStaticVehicleEx( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2, int respawn_delay )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AddStaticVehicleEx");

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		modelid, amx_ftoc(spawn_x), amx_ftoc(spawn_y), amx_ftoc(spawn_z), amx_ftoc(z_angle), color1, color2, respawn_delay
	};

	return func(pAMX, args);
}

int AddStaticPickup( int model, int type, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AddStaticPickup");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		model, type, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int CreatePickup( int model, int type, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "CreatePickup");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		model, type, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int DestroyPickup( int pickup )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DestroyPickup");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		pickup
	};

	return func(pAMX, args);
}

int ShowNameTags( int show )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ShowNameTags");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		show
	};

	return func(pAMX, args);
}

int ShowPlayerMarkers( int show )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ShowPlayerMarkers");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		show
	};

	return func(pAMX, args);
}

int GameModeExit()
{
	static amx_func_t func = amx_FindFunction(pAMX, "GameModeExit");

	cell args[1] =
	{
		sizeof(args)- sizeof(cell),
	};

	return func(pAMX, args);
}

int SetWorldTime( int hour )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetWorldTime");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		hour
	};

	return func(pAMX, args);
}

int GetWeaponName( int weaponid, char* weapon, int len )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetWeaponName");
	cell *str_phys;

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		weaponid, amx_Allot(pAMX, len, &str_phys), len
	};

	int ret = func(pAMX, args);

	amx_GetString( weapon, str_phys, 0, len );
	amx_Release( pAMX, args[2] );
	return ret;
}

int EnableTirePopping( int enable )
{
	static amx_func_t func = amx_FindFunction(pAMX, "EnableTirePopping");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		enable
	};

	return func(pAMX, args);
}

int AllowInteriorWeapons( int allow )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AllowInteriorWeapons");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		allow
	};

	return func(pAMX, args);
}

int SetWeather( int weatherid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetWeather");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		weatherid
	};

	return func(pAMX, args);
}

int SetGravity( float gravity )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetGravity");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_ftoc(gravity)
	};

	return func(pAMX, args);
}

int AllowAdminTeleport( int allow )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AllowAdminTeleport");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		allow
	};

	return func(pAMX, args);
}

int SetDeathDropAmount( int amount )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetDeathDropAmount");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amount
	};

	return func(pAMX, args);
}

int CreateExplosion( float x, float y, float z, int type, float Radius )
{
	static amx_func_t func = amx_FindFunction(pAMX, "CreateExplosion");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), type, amx_ftoc(Radius)
	};

	return func(pAMX, args);
}

int EnableZoneNames( int enable )
{
	static amx_func_t func = amx_FindFunction(pAMX, "EnableZoneNames");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		enable
	};

	return func(pAMX, args);
}

int UsePlayerPedAnims()
{
	static amx_func_t func = amx_FindFunction(pAMX, "UsePlayerPedAnims");

	cell args[1] =
	{
		sizeof(args)- sizeof(cell),
	};

	return func(pAMX, args);
}

int DisableInteriorEnterExits()
{
	static amx_func_t func = amx_FindFunction(pAMX, "DisableInteriorEnterExits");

	cell args[1] =
	{
		sizeof(args)- sizeof(cell),
	};

	return func(pAMX, args);
}

int SetNameTagDrawDistance( float distance )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetNameTagDrawDistance");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_ftoc(distance)
	};

	return func(pAMX, args);
}

int DisableNameTagLOS()
{
	static amx_func_t func = amx_FindFunction(pAMX, "DisableNameTagLOS");

	cell args[1] =
	{
		sizeof(args)- sizeof(cell),
	};

	return func(pAMX, args);
}

int LimitGlobalChatRadius( float chat_radius )
{
	static amx_func_t func = amx_FindFunction(pAMX, "LimitGlobalChatRadius");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_ftoc(chat_radius)
	};

	return func(pAMX, args);
}

int LimitPlayerMarkerRadius( float marker_radius )
{
	static amx_func_t func = amx_FindFunction(pAMX, "LimitPlayerMarkerRadius");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_ftoc(marker_radius)
	};

	return func(pAMX, args);
}


// Npc

int ConnectNPC( const char* name, const char* script )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ConnectNPC");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, name), amx_NewString(pAMX, script),
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	amx_Release( pAMX, args[1] );
	return ret;
}

int IsPlayerNPC( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerNPC");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}


// Admin

int IsPlayerAdmin( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerAdmin");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int Kick( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "Kick");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int Ban( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "Ban");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int BanEx( int playerid, const char* reason )
{
	static amx_func_t func = amx_FindFunction(pAMX, "BanEx");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_NewString(pAMX, reason)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	return ret;
}


int SendRconCommand( const char* cmd )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SendRconCommand");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, cmd)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[1] );
	return ret;
}

int GetServerVarAsString( const char* varname, char* buffer, int len )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetServerVarAsInt");
	cell *str_phys;

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, varname), amx_Allot(pAMX, len, &str_phys), len
	};

	int ret = func(pAMX, args);

	amx_GetString( buffer, str_phys, 0, len );
	amx_Release( pAMX, args[2] );
	amx_Release( pAMX, args[1] );
	return ret;
}

int GetServerVarAsInt( const char* varname )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetServerVarAsInt");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, varname)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[1] );
	return ret;
}

int GetServerVarAsBool( const char* varname )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetServerVarAsBool");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, varname)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[1] );
	return ret;
}


// Menu

int CreateMenu( const char* title, int columns, float x, float y, float col1width, float col2width )
{
	static amx_func_t func = amx_FindFunction(pAMX, "CreateMenu");

	cell args[7] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, title), columns, amx_ftoc(x), amx_ftoc(y), amx_ftoc(col1width), amx_ftoc(col2width)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[1] );
	return ret;
}

int DestroyMenu( int menuid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DestroyMenu");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		menuid
	};

	return func(pAMX, args);
}

int AddMenuItem( int menuid, int column, const char* menutext )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AddMenuItem");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		menuid, column, amx_NewString(pAMX, menutext)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[3] );
	return ret;
}

int SetMenuColumnHeader( int menuid, int column, const char* columnheader )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetMenuColumnHeader");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		menuid, column, amx_NewString(pAMX, columnheader)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[3] );
	return ret;
}

int ShowMenuForPlayer( int menuid, int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ShowMenuForPlayer");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		menuid, playerid
	};

	return func(pAMX, args);
}

int HideMenuForPlayer( int menuid, int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "HideMenuForPlayer");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		menuid, playerid
	};

	return func(pAMX, args);
}

int IsValidMenu( int menuid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsValidMenu");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		menuid
	};

	return func(pAMX, args);
}

int DisableMenu( int menuid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DisableMenu");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		menuid
	};

	return func(pAMX, args);
}

int DisableMenuRow( int menuid, int row )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DisableMenuRow");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		menuid, row
	};

	return func(pAMX, args);
}

int GetPlayerMenu( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerMenu");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}


// Text Draw

int TextDrawCreate( float x, float y, const char* text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawCreate");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		amx_ftoc(x), amx_ftoc(y), amx_NewString(pAMX, text)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[3] );
	return ret;
}

int TextDrawDestroy( int text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawDestroy");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		text
	};

	return func(pAMX, args);
}

int TextDrawLetterSize( int text, float x, float y )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawLetterSize");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		text, amx_ftoc(x), amx_ftoc(y)
	};

	return func(pAMX, args);
}

int TextDrawTextSize( int text, float x, float y )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawTextSize");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		text, amx_ftoc(x), amx_ftoc(y)
	};

	return func(pAMX, args);
}

int TextDrawAlignment( int text, int alignment )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawAlignment");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, alignment
	};

	return func(pAMX, args);
}

int TextDrawColor( int text, int color )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawColor");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, color
	};

	return func(pAMX, args);
}

int TextDrawUseBox( int text, int use )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawUseBox");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, use
	};

	return func(pAMX, args);
}

int TextDrawBoxColor( int text, int color )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawBoxColor");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, color
	};

	return func(pAMX, args);
}

int TextDrawSetShadow( int text, int size )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawSetShadow");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, size
	};

	return func(pAMX, args);
}

int TextDrawSetOutline( int text, int size )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawSetOutline");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, size
	};

	return func(pAMX, args);
}

int TextDrawBackgroundColor( int text, int color )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawBackgroundColor");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, color
	};

	return func(pAMX, args);
}

int TextDrawFont( int text, int font )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawFont");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, font
	};

	return func(pAMX, args);
}

int TextDrawSetProportional( int text, int set )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawSetProportional");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, set
	};

	return func(pAMX, args);
}

int TextDrawShowForPlayer( int playerid, int text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawShowForPlayer");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, text
	};

	return func(pAMX, args);
}

int TextDrawHideForPlayer( int playerid, int text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawHideForPlayer");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, text
	};

	return func(pAMX, args);
}

int TextDrawShowForAll( int text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawShowForAll");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		text
	};

	return func(pAMX, args);
}

int TextDrawHideForAll( int text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawHideForAll");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		text
	};

	return func(pAMX, args);
}

int TextDrawSetString( int text, const char* string )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TextDrawSetString");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		text, amx_NewString(pAMX, string)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	return ret;
}


// Gang Zones

int GangZoneCreate( float minx, float miny, float maxx, float maxy )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneCreate");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		amx_ftoc(minx), amx_ftoc(miny), amx_ftoc(maxx), amx_ftoc(maxy)
	};

	return func(pAMX, args);
}

int GangZoneDestroy( int zone )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneDestroy");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		zone
	};

	return func(pAMX, args);
}

int GangZoneShowForPlayer( int playerid, int zone, int color )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneShowForPlayer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, zone, color
	};

	return func(pAMX, args);
}

int GangZoneShowForAll( int zone, int color )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneShowForAll");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		zone, color
	};

	return func(pAMX, args);
}

int GangZoneHideForPlayer( int playerid, int zone )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneHideForPlayer");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, zone
	};

	return func(pAMX, args);
}

int GangZoneHideForAll( int zone )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneHideForAll");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		zone
	};

	return func(pAMX, args);
}

int GangZoneFlashForPlayer( int playerid, int zone, int flashcolor )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneFlashForPlayer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, zone, flashcolor
	};

	return func(pAMX, args);
}

int GangZoneFlashForAll( int zone, int flashcolor )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneFlashForAll");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		zone, flashcolor
	};

	return func(pAMX, args);
}

int GangZoneStopFlashForPlayer( int playerid, int zone )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneStopFlashForPlayer");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, zone
	};

	return func(pAMX, args);
}

int GangZoneStopFlashForAll( int zone )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GangZoneStopFlashForAll");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		zone
	};

	return func(pAMX, args);
}


// Global 3D Text Labels

int Create3DTextLabel( const char* text, int color, float x, float y, float z, float DrawDistance, int virtualworld, int testLOS )
{
	static amx_func_t func = amx_FindFunction(pAMX, "Create3DTextLabel");

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		amx_NewString(pAMX, text), color, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(DrawDistance), virtualworld, testLOS
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[1] );
	return ret;
}

int Delete3DTextLabel( int id )
{
	static amx_func_t func = amx_FindFunction(pAMX, "Delete3DTextLabel");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		id
	};

	return func(pAMX, args);
}


int Attach3DTextLabelToPlayer( int id, int playerid, float OffsetX, float OffsetY, float OffsetZ )
{
	static amx_func_t func = amx_FindFunction(pAMX, "Attach3DTextLabelToPlayer");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		id, playerid, amx_ftoc(OffsetX), amx_ftoc(OffsetY), amx_ftoc(OffsetZ)
	};

	return func(pAMX, args);
}

int Attach3DTextLabelToVehicle( int id, int vehicleid, float OffsetX, float OffsetY, float OffsetZ )
{
	static amx_func_t func = amx_FindFunction(pAMX, "Attach3DTextLabelToVehicle");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		id, vehicleid, amx_ftoc(OffsetX), amx_ftoc(OffsetY), amx_ftoc(OffsetZ)
	};

	return func(pAMX, args);
}

int Update3DTextLabelText( int id, int color, const char* text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "Update3DTextLabelText");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		id, color, amx_NewString(pAMX, text)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[3] );
	return ret;
}


// Per-player 3D Text Labels

int CreatePlayer3DTextLabel( int playerid, const char* text, int color, float x, float y, float z, float DrawDistance, int attachedplayer, int attachedvehicle, int testLOS )
{
	static amx_func_t func = amx_FindFunction(pAMX, "CreatePlayer3DTextLabel");

	cell args[11] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_NewString(pAMX, text), color, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(DrawDistance), attachedplayer, attachedvehicle, testLOS
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	return ret;
}

int DeletePlayer3DTextLabel( int playerid, int id )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DeletePlayer3DTextLabel");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, id
	};

	return func(pAMX, args);
}

int UpdatePlayer3DTextLabelText( int playerid, int id, int color, const char* text )
{
	static amx_func_t func = amx_FindFunction(pAMX, "UpdatePlayer3DTextLabelText");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, id, color, amx_NewString(pAMX, text)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[4] );
	return ret;
}


// Player GUI Dialog

int ShowPlayerDialog( int playerid, int dialogid, int style, const char* caption, const char* info, const char* button1, const char* button2 )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ShowPlayerDialog");

	cell args[8] =
	{
		sizeof(args)- sizeof(cell),
		playerid, dialogid, style, amx_NewString(pAMX, caption), amx_NewString(pAMX, info), amx_NewString(pAMX, button1), amx_NewString(pAMX, button2)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[7] );
	amx_Release( pAMX, args[6] );
	amx_Release( pAMX, args[5] );
	amx_Release( pAMX, args[4] );
	return ret;
}


//----------------------------------------------------------
// a_players.inc

int SetSpawnInfo( int playerid, int team, int skin, float x, float y, float z, float rotation, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetSpawnInfo");

	cell args[14] =
	{
		sizeof(args)- sizeof(cell),
		playerid, team, skin, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(rotation), weapon1, weapon1_ammo, weapon2, weapon2_ammo, weapon3, weapon3_ammo
	};

	return func(pAMX, args);
}

int SpawnPlayer( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SpawnPlayer");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}


// Player info

int SetPlayerPos( int playerid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerPos");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int SetPlayerPosFindZ( int playerid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerPosFindZ");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int GetPlayerPos( int playerid, float& x, float& y, float& z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerPos");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int SetPlayerFacingAngle( int playerid, float ang )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerFacingAngle");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(ang)
	};

	return func(pAMX, args);
}

int GetPlayerFacingAngle( int playerid, float& ang )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerFacingAngle");
	cell *phys;

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &phys)
	};

	int ret = func(pAMX, args);

	ang = *((float*)phys);

	amx_Release( pAMX, args[2] );
	return ret;
}

int IsPlayerInRangeOfPoint( int playerid, float range, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerInRangeOfPoint");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(range), amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int IsPlayerStreamedIn( int playerid, int forplayerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerStreamedIn");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, forplayerid
	};

	return func(pAMX, args);
}

int SetPlayerInterior( int playerid, int interiorid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerInterior");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, interiorid
	};

	return func(pAMX, args);
}

int GetPlayerInterior( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerInterior");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerHealth( int playerid, float health )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerHealth");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(health)
	};

	return func(pAMX, args);
}

int GetPlayerHealth( int playerid, float &health )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerHealth");
	cell *phys;

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &phys)
	};

	int ret = func(pAMX, args);

	health = *((float*)phys);

	amx_Release( pAMX, args[2] );
	return ret;
}

int SetPlayerArmour( int playerid, float armour )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerArmour");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(armour)
	};

	return func(pAMX, args);
}

int GetPlayerArmour( int playerid, float &armour)
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerArmour");
	cell *phys;

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &phys)
	};

	int ret = func(pAMX, args);

	armour = *((float*)phys);

	amx_Release( pAMX, args[2] );
	return ret;
}

int SetPlayerAmmo( int playerid, int weaponslot, int ammo )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerAmmo");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, weaponslot, ammo
	};

	return func(pAMX, args);
}

int GetPlayerAmmo( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerAmmo");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerWeaponState( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerWeaponState");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerTeam( int playerid, int teamid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerTeam");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, teamid
	};

	return func(pAMX, args);
}

int GetPlayerTeam( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerTeam");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerScore( int playerid, int score )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerScore");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, score
	};

	return func(pAMX, args);
}

int GetPlayerScore( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerScore");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerDrunkLevel( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerDrunkLevel");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerDrunkLevel( int playerid, int level )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerDrunkLevel");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, level
	};

	return func(pAMX, args);
}

int SetPlayerColor( int playerid, int color )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerColor");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, color
	};

	return func(pAMX, args);
}

int GetPlayerColor( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerColor");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerSkin( int playerid, int skinid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerSkin");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, skinid
	};

	return func(pAMX, args);
}

int GetPlayerSkin( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerSkin");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GivePlayerWeapon( int playerid, int weaponid, int ammo )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GivePlayerWeapon");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, weaponid, ammo
	};

	return func(pAMX, args);
}

int ResetPlayerWeapons( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ResetPlayerWeapons");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerArmedWeapon( int playerid, int weaponid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerArmedWeapon");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, weaponid
	};

	return func(pAMX, args);
}

int GetPlayerWeaponData( int playerid, int slot, int &weapons, int &ammo )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerWeaponData");
	cell *w_phys, *a_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, slot, amx_Allot(pAMX, 1, &w_phys), amx_Allot(pAMX, 1, &a_phys)
	};

	int ret = func(pAMX, args);

	weapons = *w_phys;
	ammo = *a_phys;

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	return ret;
}

int GivePlayerMoney( int playerid, int money )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GivePlayerMoney");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, money
	};

	return func(pAMX, args);
}

int ResetPlayerMoney( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ResetPlayerMoney");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerName( int playerid, const char* name )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerName");
	
	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_NewString(pAMX, name)
	};
	
	int ret = func(pAMX, args);
	
	amx_Release( pAMX, args[2] );
	return ret;
}

int GetPlayerMoney( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerMoney");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerState( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerState");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerIp( int playerid, char* name, int len )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerIp");
	cell *phys;

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, len, &phys), len
	};

	int ret = func(pAMX, args);

	amx_GetString( name, phys, 0, len );
	amx_Release( pAMX, args[2] );
	return ret;
}

int GetPlayerPing( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerPing");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerWeapon( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerWeapon");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerKeys( int playerid, int &keys, int &updown, int &leftright )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerKeys");
	cell *k_phys, *u_phys, *l_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &k_phys), amx_Allot(pAMX, 1, &u_phys), amx_Allot(pAMX, 1, &l_phys)
	};

	int ret = func(pAMX, args);

	keys = *k_phys;
	updown = *u_phys;
	leftright = *l_phys;

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int GetPlayerName( int playerid, char* name, int len )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerName");
	cell *str_phys;

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, len, &str_phys), len
	};

	int ret = func(pAMX, args);

	amx_GetString( name, str_phys, 0, len );
	amx_Release( pAMX, args[2] );
	return ret;
}

int SetPlayerTime( int playerid, int hour, int minute )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerTime");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, hour, minute
	};

	return func(pAMX, args);
}

int GetPlayerTime( int playerid, int &hour, int &minute )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerTime");
	cell *h_phys, *m_phys;

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &h_phys), amx_Allot(pAMX, 1, &m_phys)
	};

	int ret = func(pAMX, args);

	hour = *h_phys;
	minute = *m_phys;

	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int TogglePlayerClock( int playerid, int toggle )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TogglePlayerClock");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, toggle
	};

	return func(pAMX, args);
}

int SetPlayerWeather( int playerid, int weather )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerWeather");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, weather
	};

	return func(pAMX, args);
}

int ForceClassSelection( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ForceClassSelection");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerWantedLevel( int playerid, int level )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerWantedLevel");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, level
	};

	return func(pAMX, args);
}

int GetPlayerWantedLevel( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerWantedLevel");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerFightingStyle( int playerid, int style )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerFightingStyle");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, style
	};

	return func(pAMX, args);
}

int GetPlayerFightingStyle( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerFightingStyle");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerVelocity( int playerid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerVelocity");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int GetPlayerVelocity( int playerid, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerVelocity");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int PlayCrimeReportForPlayer( int playerid, int suspectid, int crime )
{
	static amx_func_t func = amx_FindFunction(pAMX, "PlayCrimeReportForPlayer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, suspectid, crime
	};

	return func(pAMX, args);
}

int SetPlayerShopName( int playerid, const char* shopname )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerShopName");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_NewString(pAMX, shopname)
	};

	return func(pAMX, args);
}

int SetPlayerSkillLevel( int playerid, int skill, int level )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerSkillLevel");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, skill, level
	};

	return func(pAMX, args);
}

int GetPlayerSurfingVehicleID( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerSurfingVehicleID");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}


int SetPlayerAttachedObject( int playerid, int index, int modelid, int bone, float fOffsetX, float fOffsetY, float fOffsetZ, float frotX, float frotY, float frotZ, float fScaleX, float fScaleY, float fScaleZ )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerAttachedObject");

	cell args[14] =
	{
		sizeof(args)- sizeof(cell),
		playerid, index, modelid, bone, amx_ftoc(fOffsetX), amx_ftoc(fOffsetY), amx_ftoc(fOffsetZ), amx_ftoc(frotX), amx_ftoc(frotY), amx_ftoc(frotZ), amx_ftoc(fScaleX), amx_ftoc(fScaleY), amx_ftoc(fScaleZ)
	};

	return func(pAMX, args);
}

int RemovePlayerAttachedObject( int playerid, int index )
{
	static amx_func_t func = amx_FindFunction(pAMX, "RemovePlayerAttachedObject");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, index
	};

	return func(pAMX, args);
}

int IsPlayerAttachedObjectSlotUsed( int playerid, int index )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerAttachedObjectSlotUsed");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, index
	};

	return func(pAMX, args);
}


int SetPlayerChatBubble( int playerid, const char* text, int color, float drawdistance, int expiretime )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerChatBubble");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_NewString(pAMX, text), color, amx_ftoc(drawdistance), expiretime
	};

	return func(pAMX, args);
}

// Player controls

int PutPlayerInVehicle( int playerid, int vehicleid, int seatid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "PutPlayerInVehicle");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, vehicleid, seatid
	};

	return func(pAMX, args);
}

int GetPlayerVehicleID( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerVehicleID");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerVehicleSeat( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerVehicleSeat");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int RemovePlayerFromVehicle( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "RemovePlayerFromVehicle");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int TogglePlayerControllable( int playerid, int toggle )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TogglePlayerControllable");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, toggle
	};

	return func(pAMX, args);
}

int PlayerPlaySound( int playerid, int soundid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "PlayerPlaySound");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, soundid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int ApplyAnimation( int playerid, const char* animlib, const char* animname, float fDelta, int loop, int lockx, int locky, int freeze, int time, int forcesync )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ApplyAnimation");

	cell args[11] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_NewString(pAMX, animlib), amx_NewString(pAMX, animname), amx_ftoc(fDelta), loop, lockx, locky, freeze, time, forcesync
	};

	amx_Release( pAMX, args[2] );
	amx_Release( pAMX, args[1] );
	return func(pAMX, args);
}

int ClearAnimations( int playerid, int forcesync )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ClearAnimations");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, forcesync
	};

	return func(pAMX, args);
}

int GetPlayerAnimationIndex( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerAnimationIndex");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetAnimationName( int index, char* animlib, int len1, char* animname, int len2 )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetAnimationName");
	cell *phys1, *phys2;

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		index, amx_Allot(pAMX, len1, &phys1), len1, amx_Allot(pAMX, len2, &phys2), len2
	};

	int ret = func(pAMX, args);

	amx_GetString( animlib, phys1, 0, len1 );
	amx_GetString( animname, phys2, 0, len2 );
	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int GetPlayerSpecialAction( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerSpecialAction");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerSpecialAction( int playerid, int actionid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerSpecialAction");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, actionid
	};

	return func(pAMX, args);
}


// Player world/map related

int SetPlayerCheckpoint( int playerid, float x, float y, float z, float size )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerCheckpoint");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(size)
	};

	return func(pAMX, args);
}

int DisablePlayerCheckpoint( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DisablePlayerCheckpoint");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerRaceCheckpoint( int playerid, int type, float x, float y, float z, float nextx, float nexty, float nextz, float size )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerRaceCheckpoint");

	cell args[10] =
	{
		sizeof(args)- sizeof(cell),
		playerid, type, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(nextx), amx_ftoc(nexty), amx_ftoc(nextz),  amx_ftoc(size)
	};

	return func(pAMX, args);
}

int DisablePlayerRaceCheckpoint( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DisablePlayerRaceCheckpoint");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int SetPlayerWorldBounds( int playerid, float x_max, float x_min, float y_max, float y_min )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerWorldBounds");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(x_max), amx_ftoc(x_min), amx_ftoc(y_max), amx_ftoc(y_min)
	};

	return func(pAMX, args);
}

int SetPlayerMarkerForPlayer( int playerid, int showplayerid, int color )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerMarkerForPlayer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, showplayerid, color
	};

	return func(pAMX, args);
}

int ShowPlayerNameTagForPlayer( int playerid, int showplayerid, int show )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ShowPlayerNameTagForPlayer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, showplayerid, show
	};

	return func(pAMX, args);
}

int SetPlayerMapIcon( int playerid, int iconid, float x, float y, float z, int markertype, int color, int style )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerMapIcon");

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		playerid, iconid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), markertype, color, style
	};

	return func(pAMX, args);
}

int RemovePlayerMapIcon( int playerid, int iconid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "RemovePlayerMapIcon");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, iconid
	};

	return func(pAMX, args);
}

int SetPlayerCameraPos ( int playerid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerCameraPos");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int SetPlayerCameraLookAt ( int playerid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerCameraLookAt");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int SetCameraBehindPlayer( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetCameraBehindPlayer");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int GetPlayerCameraPos( int playerid, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerCameraPos");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int GetPlayerCameraFrontVector( int playerid, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerCameraFrontVector");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		playerid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int AllowPlayerTeleport( int playerid, int allow )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AllowPlayerTeleport");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, allow
	};

	return func(pAMX, args);
}


// Player conditionals

int IsPlayerConnected( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerConnected");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int IsPlayerInVehicle( int playerid, int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerInVehicle");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, vehicleid
	};

	return func(pAMX, args);
}

int IsPlayerInAnyVehicle( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerInAnyVehicle");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int IsPlayerInCheckpoint( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerInCheckpoint");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}

int IsPlayerInRaceCheckpoint( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsPlayerInRaceCheckpoint");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}


// Virtual Worlds

int SetPlayerVirtualWorld( int playerid, int worldid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerVirtualWorld");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, worldid
	};

	return func(pAMX, args);
}

int GetPlayerVirtualWorld( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerVirtualWorld");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}


// Insane Stunts

int EnableStuntBonusForPlayer( int playerid, int enable )
{
	static amx_func_t func = amx_FindFunction(pAMX, "EnableStuntBonusForPlayer");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, enable
	};

	return func(pAMX, args);
}

int EnableStuntBonusForAll( int enable )
{
	static amx_func_t func = amx_FindFunction(pAMX, "EnableStuntBonusForAll");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		enable
	};

	return func(pAMX, args);
}


// Spectating

int TogglePlayerSpectating( int playerid, int toggle )
{
	static amx_func_t func = amx_FindFunction(pAMX, "TogglePlayerSpectating");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, toggle
	};

	return func(pAMX, args);
}

int PlayerSpectatePlayer( int playerid, int targetplayerid, int mode )
{
	static amx_func_t func = amx_FindFunction(pAMX, "PlayerSpectatePlayer");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, targetplayerid, mode
	};

	return func(pAMX, args);
}

int PlayerSpectateVehicle( int playerid, int targetvehicleid, int mode )
{
	static amx_func_t func = amx_FindFunction(pAMX, "PlayerSpectateVehicle");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, targetvehicleid, mode
	};

	return func(pAMX, args);
}


// Npc Record

int StartRecordingPlayerData( int playerid, int recordtype, const char* recordname )
{
	static amx_func_t func = amx_FindFunction(pAMX, "StartRecordingPlayerData");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		playerid, recordtype, amx_NewString(pAMX, recordname)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[3] );
	return ret;
}

int StopRecordingPlayerData( int playerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "StopRecordingPlayerData");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		playerid
	};

	return func(pAMX, args);
}


//----------------------------------------------------------
// a_vehicles.inc

int CreateVehicle( int vehicletype, float x, float y, float z, float rotation, int color1, int color2, int respawn_delay )
{
	static amx_func_t func = amx_FindFunction(pAMX, "CreateVehicle");

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		vehicletype, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(rotation), color1, color2, respawn_delay
	};

	return func(pAMX, args);
}

int DestroyVehicle( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DestroyVehicle");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}

int IsVehicleStreamedIn( int vehicleid, int forplayerid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsVehicleStreamedIn");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, forplayerid
	};

	return func(pAMX, args);
}

int GetVehiclePos( int vehicleid, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehiclePos");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int SetVehiclePos( int vehicleid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehiclePos");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int GetVehicleZAngle( int vehicleid, float &z_angle )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleZAngle");
	cell *phys;

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_Allot(pAMX, 1, &phys)
	};

	int ret = func(pAMX, args);

	z_angle = *((float*)phys);

	amx_Release( pAMX, args[2] );
	return ret;
}

int GetVehicleRotationQuat( int vehicleid, float &w, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleRotationQuat");
	cell *w_phys, *x_phys, *y_phys, *z_phys;

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_Allot(pAMX, 1, &w_phys), amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	w = *((float*)w_phys);
	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[5] );
	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;

}

int SetVehicleZAngle( int vehicleid, float z_angle )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleZAngle");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_ftoc(z_angle)
	};

	return func(pAMX, args);
}

int SetVehicleParamsForPlayer( int vehicleid, int playerid, int objective, int doorslocked )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleParamsForPlayer");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, playerid, objective, doorslocked
	};

	return func(pAMX, args);
}

int ManualVehicleEngineAndLights()
{
	static amx_func_t func = amx_FindFunction(pAMX, "ManualVehicleEngineAndLights");

	cell args[1] =
	{
		sizeof(args)- sizeof(cell),
	};

	return func(pAMX, args);
}

int SetVehicleParamsEx( int vehicleid, int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleParamsEx");

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, engine, lights, alarm, doors, bonnet, boot, objective
	};

	return func(pAMX, args);
}

int GetVehicleParamsEx( int vehicleid, int &engine, int &lights, int &alarm, int &doors, int &bonnet, int &boot, int &objective )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleParamsEx");
	cell *e_phys, *l_phys, *a_phys, *d_phys, *b_phys, *o_phys, *j_phys;

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_Allot(pAMX, 1, &e_phys), amx_Allot(pAMX, 1, &l_phys), amx_Allot(pAMX, 1, &a_phys),
		amx_Allot(pAMX, 1, &d_phys), amx_Allot(pAMX, 1, &b_phys), amx_Allot(pAMX, 1, &o_phys), amx_Allot(pAMX, 1, &j_phys)
	};

	int ret = func(pAMX, args);

	engine = *e_phys;
	lights = *l_phys;
	alarm = *a_phys;
	doors = *d_phys;
	bonnet = *b_phys;
	boot = *o_phys;
	objective = *j_phys;

	amx_Release( pAMX, args[8] );
	amx_Release( pAMX, args[7] );
	amx_Release( pAMX, args[6] );
	amx_Release( pAMX, args[5] );
	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int SetVehicleToRespawn( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleToRespawn");
	
	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}

int LinkVehicleToInterior( int vehicleid, int interiorid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "LinkVehicleToInterior");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, interiorid
	};

	return func(pAMX, args);
}

int AddVehicleComponent( int vehicleid, int componentid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AddVehicleComponent");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, componentid
	};

	return func(pAMX, args);
}

int RemoveVehicleComponent( int vehicleid, int componentid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "RemoveVehicleComponent");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, componentid
	};

	return func(pAMX, args);
}

int ChangeVehicleColor( int vehicleid, int color1, int color2 )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ChangeVehicleColor");

	cell args[4] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, color1, color2
	};

	return func(pAMX, args);
}

int ChangeVehiclePaintjob( int vehicleid, int paintjobid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "ChangeVehiclePaintjob");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, paintjobid
	};

	return func(pAMX, args);
}

int SetVehicleHealth( int vehicleid, float health )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleHealth");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_ftoc(health)
	};

	return func(pAMX, args);
}

int GetVehicleHealth( int vehicleid, float &health )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleHealth");
	cell *phys;

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_Allot(pAMX, 1, &phys)
	};

	int ret = func(pAMX, args);

	health = *((float*)phys);

	amx_Release( pAMX, args[2] );
	return ret;
}

int AttachTrailerToVehicle( int trailerid, int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AttachTrailerToVehicle");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		trailerid, vehicleid
	};

	return func(pAMX, args);
}

int DetachTrailerFromVehicle( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DetachTrailerFromVehicle");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}

int IsTrailerAttachedToVehicle( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsTrailerAttachedToVehicle");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}

int GetVehicleTrailer( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleTrailer");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}

int SetVehicleNumberPlate( int vehicleid, const char* numberplate )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleNumberPlate");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_NewString(pAMX, numberplate)
	};

	int ret = func(pAMX, args);

	amx_Release( pAMX, args[2] );
	return ret;
}

int GetVehicleModel( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleModel");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}

int GetVehicleComponentInSlot( int vehicleid, int slot )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleComponentInSlot");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, slot
	};

	return func(pAMX, args);
}

int GetVehicleComponentType( int component )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleComponentType");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		component
	};

	return func(pAMX, args);
}

int RepairVehicle( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "RepairVehicle");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}

int GetVehicleVelocity( int vehicleid, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleVelocity");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int SetVehicleVelocity( int vehicleid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleVelocity");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int SetVehicleAngularVelocity( int vehicleid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleAngularVelocity");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int GetVehicleDamageStatus( int vehicleid, int &panels, int &doors, int &lights, int &tires)
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleDamageStatus");
	cell *p_phys, *d_phys, *l_phys, *t_phys;

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, amx_Allot(pAMX, 1, &p_phys), amx_Allot(pAMX, 1, &d_phys), amx_Allot(pAMX, 1, &l_phys), amx_Allot(pAMX, 1, &t_phys)
	};

	int ret = func(pAMX, args);

	panels = *p_phys;
	doors = *d_phys;
	lights = *l_phys;
	tires = *t_phys;

	amx_Release( pAMX, args[5] );
	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int UpdateVehicleDamageStatus( int vehicleid, int panels, int doors, int lights, int tires )
{
	static amx_func_t func = amx_FindFunction(pAMX, "UpdateVehicleDamageStatus");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, panels, doors, lights, tires
	};

	return func(pAMX, args);
}


// Virtual Worlds

int SetVehicleVirtualWorld( int vehicleid, int worldid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetVehicleVirtualWorld");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid, worldid
	};

	return func(pAMX, args);
}

int GetVehicleVirtualWorld( int vehicleid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetVehicleVirtualWorld");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		vehicleid
	};

	return func(pAMX, args);
}



//----------------------------------------------------------
// a_object.inc

int CreateObject( int modelid, float x, float y, float z, float rX, float rY, float rZ)
{
	static amx_func_t func = amx_FindFunction(pAMX, "CreateObject");

	cell args[8] =
	{
		sizeof(args)- sizeof(cell),
		modelid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(rX), amx_ftoc(rY), amx_ftoc(rZ)
	};

	return func(pAMX, args);
}

int SetObjectPos( int objectid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetObjectPos");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		objectid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int GetObjectPos( int objectid, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetObjectPos");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		objectid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int SetObjectRot( int objectid, float rotX, float rotY, float rotZ )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetObjectRot");

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		objectid, amx_ftoc(rotX), amx_ftoc(rotY), amx_ftoc(rotZ)
	};

	return func(pAMX, args);
}

int GetObjectRot( int objectid, float &rotX, float &rotY, float &rotZ)
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetObjectRot");
	cell *x_phys, *y_phys, *z_phys;

	cell args[5] =
	{
		sizeof(args)- sizeof(cell),
		objectid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	rotX = *((float*)x_phys);
	rotY = *((float*)y_phys);
	rotZ = *((float*)z_phys);

	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	amx_Release( pAMX, args[2] );
	return ret;
}

int IsValidObject( int objectid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsValidObject");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		objectid
	};

	return func(pAMX, args);
}

int DestroyObject( int objectid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DestroyObject");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		objectid
	};

	return func(pAMX, args);
}

int MoveObject( int objectid, float x, float y, float z, float Speed )
{
	static amx_func_t func = amx_FindFunction(pAMX, "MoveObject");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		objectid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(Speed)
	};

	return func(pAMX, args);
}

int StopObject( int objectid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "StopObject");

	cell args[2] =
	{
		sizeof(args)- sizeof(cell),
		objectid
	};

	return func(pAMX, args);
}


int CreatePlayerObject( int playerid, int modelid, float x, float y, float z, float rx, float ry, float rz )
{
	static amx_func_t func = amx_FindFunction(pAMX, "CreatePlayerObject");

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		playerid, modelid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(rx), amx_ftoc(ry), amx_ftoc(rz)
	};

	return func(pAMX, args);
}

int SetPlayerObjectPos( int playerid, int objectid, float x, float y, float z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerObjectPos");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z)
	};

	return func(pAMX, args);
}

int GetPlayerObjectPos( int playerid, int objectid, float &x, float &y, float &z )
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerObjectPos");
	cell *x_phys, *y_phys, *z_phys;

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	x = *((float*)x_phys);
	y = *((float*)y_phys);
	z = *((float*)z_phys);

	amx_Release( pAMX, args[5] );
	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	return ret;
}

int SetPlayerObjectRot( int playerid, int objectid, float rotX, float rotY, float rotZ )
{
	static amx_func_t func = amx_FindFunction(pAMX, "SetPlayerObjectRot");

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid, amx_ftoc(rotX), amx_ftoc(rotY), amx_ftoc(rotZ)
	};

	return func(pAMX, args);
}

int GetPlayerObjectRot( int playerid, int objectid, float &rotX, float &rotY, float &rotZ)
{
	static amx_func_t func = amx_FindFunction(pAMX, "GetPlayerObjectRot");
	cell *x_phys, *y_phys, *z_phys;

	cell args[6] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid, amx_Allot(pAMX, 1, &x_phys), amx_Allot(pAMX, 1, &y_phys), amx_Allot(pAMX, 1, &z_phys)
	};

	int ret = func(pAMX, args);

	rotX = *((float*)x_phys);
	rotY = *((float*)y_phys);
	rotZ = *((float*)z_phys);

	amx_Release( pAMX, args[5] );
	amx_Release( pAMX, args[4] );
	amx_Release( pAMX, args[3] );
	return ret;
}

int IsValidPlayerObject( int playerid, int objectid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "IsValidPlayerObject");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid
	};

	return func(pAMX, args);
}

int DestroyPlayerObject( int playerid, int objectid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "DestroyPlayerObject");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid
	};

	return func(pAMX, args);
}

int MovePlayerObject( int playerid, int objectid, float x, float y, float z, float Speed )
{
	static amx_func_t func = amx_FindFunction(pAMX, "MovePlayerObject");

	cell args[7] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid, amx_ftoc(x), amx_ftoc(y), amx_ftoc(z), amx_ftoc(Speed)
	};

	return func(pAMX, args);
}

int StopPlayerObject( int playerid, int objectid )
{
	static amx_func_t func = amx_FindFunction(pAMX, "StopPlayerObject");

	cell args[3] =
	{
		sizeof(args)- sizeof(cell),
		playerid, objectid
	};

	return func(pAMX, args);
}

int AttachObjectToPlayer( int objectid, int playerid, float OffsetX, float OffsetY, float OffsetZ, float rX, float rY, float rZ )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AttachObjectToPlayer");

	cell args[9] =
	{
		sizeof(args)- sizeof(cell),
		objectid, playerid, amx_ftoc(OffsetX), amx_ftoc(OffsetY), amx_ftoc(OffsetZ), amx_ftoc(rX), amx_ftoc(rY), amx_ftoc(rZ)
	};

	return func(pAMX, args);
}

int AttachPlayerObjectToPlayer( int objectplayer, int objectid, int attachplayer, float OffsetX, float OffsetY, float OffsetZ, float rX, float rY, float rZ )
{
	static amx_func_t func = amx_FindFunction(pAMX, "AttachPlayerObjectToPlayer");

	cell args[10] =
	{
		sizeof(args)- sizeof(cell),
		objectplayer, objectid, attachplayer, amx_ftoc(OffsetX), amx_ftoc(OffsetY), amx_ftoc(OffsetZ), amx_ftoc(rX), amx_ftoc(rY), amx_ftoc(rZ)
	};

	return func(pAMX, args);
}
