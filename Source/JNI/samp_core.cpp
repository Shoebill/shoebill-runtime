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

#include <jni.h>

#include "encoding.h"
#include "jni_core.h"
#include "a_samp.h"


#define classpath "./gamemodes/*.jar"
#define modeclass "net/gtaun/mklms/GameMode"


jclass jmodecls = NULL;
jobject jmodeobj = NULL;

int server_codepage = 0;
int player_codepage[MAX_PLAYERS] = {0};


bool OnLoadPlugin()
{
	logprintf( "  > Shoebill JNI v0.0.0.0 by MK124." );


	if( jni_jvm_create(classpath) < 0 )
	{
		logprintf( "  > Can't create Java VM." );
		return false;
	}

	logprintf( "  > Java VM has been created." );
	return true;
}

void OnUnloadPlugin()
{
	if( jni_jvm_destroy() >= 0 ) logprintf( "Java VM destory." );
}

void OnProcessTick()
{
	if( !jmodeobj ) return;

	jmethodID jmid = env->GetMethodID(jmodecls, "onProcessTick", "()V");
	if( !jmid ) return;

	env->CallVoidMethod(jmodeobj, jmid);
}

int OnGameModeInit()
{
	jmodecls = env->FindClass(modeclass);
	if( !jmodecls )
	{
		logprintf( "Can't find gamemode class." );
		return 0;
	}

	if( jni_jvm_newobject(jmodecls, &jmodeobj) < 0 )
	{
		logprintf( "Can't create gamemode object." );
		return 0;
	}

	logprintf( "Gamemode object has been created." );
	return 1;
}

int OnGameModeExit()
{
	if( jmodeobj != NULL )
	{
		jmethodID jmid = env->GetMethodID(jmodecls, "onGameModeExit", "()I");
		env->DeleteLocalRef(jmodecls);
		if( !jmid )
		{
			logprintf( "Can't find %s::onGameModeExit().", modeclass );
			return 0;
		}

		env->CallIntMethod(jmodeobj, jmid);
		jmodeobj = NULL;
		jmodecls = NULL;

		return 1;
	}

	if(jmodecls) env->DeleteLocalRef(jmodecls);
	return 0;
}

int OnFilterScriptInit()
{
	return 0;
}

int OnFilterScriptExit()
{
	return 0;
}

int OnPlayerConnect( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerConnect", "(I)I");
	if( !jmid ) return 0;
	
	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnPlayerDisconnect( int playerid, int reason )
{
	if( !jmodeobj ) return 0;

	player_codepage[playerid] = 0;
	
	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerDisconnect", "(II)I");
	if( !jmid ) return 0;
	
	return env->CallIntMethod(jmodeobj, jmid, playerid, reason);
}

int OnPlayerSpawn( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerSpawn", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnPlayerDeath( int playerid, int killerid, int reason )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerDeath", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, killerid, reason);
}

int OnVehicleSpawn( int vehicleid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehicleSpawn", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, vehicleid);
}

int OnVehicleDeath( int vehicleid, int killerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehicleDeath", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, vehicleid, killerid);
}

int OnPlayerText( int playerid, char* text )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerText", "(ILjava/lang/String;)I");
	if( !jmid ) return 0;

	jchar wtext[1024];
	int len = mbs2wcs(player_codepage[playerid], text, -1, wtext, sizeof(wtext));

	jstring str = env->NewString(wtext, len-1);
	return env->CallIntMethod(jmodeobj, jmid, playerid, str);
}

int OnPlayerCommandText( int playerid, char* cmdtext )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerCommandText", "(ILjava/lang/String;)I");
	if( !jmid ) return 0;

	jchar wtext[1024];
	int len = mbs2wcs(player_codepage[playerid], cmdtext, -1, wtext, sizeof(wtext));

	jstring str = env->NewString(wtext, len-1);
	return env->CallIntMethod(jmodeobj, jmid, playerid, str);
}

int OnPlayerRequestClass( int playerid, int classid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerRequestClass", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, classid);
}

int OnPlayerEnterVehicle( int playerid, int vehicleid, int ispassenger )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerEnterVehicle", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, vehicleid, ispassenger);
}

int OnPlayerExitVehicle( int playerid, int vehicleid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerExitVehicle", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, vehicleid);
}

int OnPlayerStateChange( int playerid, int newstate, int oldstate )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerStateChange", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, newstate, oldstate);
}

int OnPlayerEnterCheckpoint( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerEnterCheckpoint", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnPlayerLeaveCheckpoint( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerLeaveCheckpoint", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnPlayerEnterRaceCheckpoint( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerEnterRaceCheckpoint", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnPlayerLeaveRaceCheckpoint( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerLeaveRaceCheckpoint", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnRconCommand( char* cmd )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onServerRconCommand", "(Ljava/lang/String;)I");
	if( !jmid ) return 0;

	jchar wtext[1024];
	int len = mbs2wcs(server_codepage, cmd, -1, wtext, sizeof(wtext));

	jstring str = env->NewString(wtext, len-1);
	return env->CallIntMethod(jmodeobj, jmid, str);
}

int OnPlayerRequestSpawn( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerRequestSpawn", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnObjectMoved( int objectid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onObjectMoved", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, objectid);
}

int OnPlayerObjectMoved( int playerid, int objectid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerObjectMoved", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, objectid);
}

int OnPlayerPickUpPickup( int playerid, int pickupid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerPickUpPickup", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, pickupid);
}

int OnVehicleMod( int playerid, int vehicleid, int componentid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehicleMod", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, vehicleid, componentid);
}

int OnEnterExitModShop( int playerid, int enterexit, int interiorid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onEnterExitModShop", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, enterexit, interiorid);
}

int OnVehiclePaintjob( int playerid, int vehicleid, int paintjobid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehiclePaintjob", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, vehicleid, paintjobid);
}

int OnVehicleRespray( int playerid, int vehicleid, int color1, int color2 )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehicleRespray", "(IIII)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, vehicleid, color1, color2);
}

int OnVehicleDamageStatusUpdate( int vehicleid, int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehicleDamageStatusUpdate", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, vehicleid, playerid);
}

int OnPlayerSelectedMenuRow( int playerid, int row )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerSelectedMenuRow", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, row);
}

int OnPlayerExitedMenu( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerExitedMenu", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnPlayerInteriorChange( int playerid, int newinteriorid, int oldinteriorid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerInteriorChange", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, newinteriorid, oldinteriorid);
}

int OnPlayerKeyStateChange( int playerid, int newkeys, int oldkeys )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerKeyStateChange", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, newkeys, oldkeys);
}

int OnRconLoginAttempt( char* ip, char* password, int success )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onServerRconLoginAttempt", "(Ljava/lang/String;Ljava/lang/String;I)I");
	if( !jmid ) return 0;

	jstring iptext = env->NewStringUTF(ip);

	jchar wtext[1024];
	int len = mbs2wcs(server_codepage, password, -1, wtext, sizeof(wtext));
	jstring str = env->NewString(wtext, len-1);

	return env->CallIntMethod(jmodeobj, jmid, iptext, str, success);
}

int OnPlayerUpdate( int playerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerUpdate", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid);
}

int OnPlayerStreamIn( int playerid, int forplayerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerStreamIn", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, forplayerid);
}

int OnPlayerStreamOut( int playerid, int forplayerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerStreamOut", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, forplayerid);
}

int OnVehicleStreamIn( int vehicleid, int forplayerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehicleStreamIn", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, vehicleid, forplayerid);
}

int OnVehicleStreamOut( int vehicleid, int forplayerid )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onVehicleStreamOut", "(II)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, vehicleid, forplayerid);
}

int OnDialogResponse( int playerid, int dialogid, int response, int listitem, char *inputtext )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onDialogResponse", "(IIIILjava/lang/String;)I");
	if( !jmid ) return 0;

	jchar wtext[1024];
	int len = mbs2wcs(player_codepage[playerid], inputtext, -1, wtext, sizeof(wtext));

	jstring str = env->NewString(wtext, len-1);
	return env->CallIntMethod(jmodeobj, jmid, playerid, dialogid, response, listitem, str);
}

int OnPlayerClickPlayer( int playerid, int clickedplayerid, int source )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onPlayerClickPlayer", "(III)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, playerid, clickedplayerid, source);
}

int OnTimer( int TimerIndex )
{
	if( !jmodeobj ) return 0;

	jmethodID jmid = env->GetMethodID(jmodecls, "onTimer", "(I)I");
	if( !jmid ) return 0;

	return env->CallIntMethod(jmodeobj, jmid, TimerIndex);
}
