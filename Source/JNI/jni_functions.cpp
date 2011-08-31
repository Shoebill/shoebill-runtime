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

#include <string.h>

#include "jni_functions.h"
#include "samp_core.h"
#include "encoding.h"
#include "../Wrapper/a_samp.h"


/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setServerCodepage
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setServerCodepage
  (JNIEnv *env, jclass jcls, jint codepage)
{
	server_codepage = codepage;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getServerCodepage
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getServerCodepage
  (JNIEnv *env, jclass jcls)
{
	return server_codepage;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerCodepage
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerCodepage
  (JNIEnv *env, jclass jcls, jint playerid, jint codepage)
{
	player_codepage[playerid] = codepage;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerCodepage
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerCodepage
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return player_codepage[playerid];
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    sendClientMessage
 * Signature: (IILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_sendClientMessage
  (JNIEnv *env, jclass jcls, jint playerid, jint color, jstring message)
{
	const jchar* wmsg = env->GetStringChars(message, NULL);
	int len = env->GetStringLength(message);
	
	char msg[1024];
	wcs2mbs( player_codepage[playerid], wmsg, len, msg, sizeof(msg) );
	env->ReleaseStringChars(message, wmsg);

	SendClientMessage( playerid, color, msg );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    sendClientMessageToAll
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_sendClientMessageToAll
  (JNIEnv *env, jclass jcls, jint color, jstring message)
{
	const jchar* wmsg = env->GetStringChars(message, NULL);
	int len = env->GetStringLength(message);

	char msg[1024];
	wcs2mbs( server_codepage, wmsg, len, msg, sizeof(msg) );
	env->ReleaseStringChars(message, wmsg);

	SendClientMessageToAll( color, msg );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    sendPlayerMessageToPlayer
 * Signature: (IILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_sendPlayerMessageToPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint senderid, jstring message)
{
	const jchar* wmsg = env->GetStringChars(message, NULL);
	int len = env->GetStringLength(message);

	char msg[1024];
	wcs2mbs( player_codepage[playerid], wmsg, len, msg, sizeof(msg) );
	env->ReleaseStringChars(message, wmsg);

	SendPlayerMessageToPlayer( playerid, senderid, msg );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    sendPlayerMessageToAll
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_sendPlayerMessageToAll
  (JNIEnv *env, jclass jcls, jint senderid, jstring message)
{
	const jchar* wmsg = env->GetStringChars(message, NULL);
	int len = env->GetStringLength(message);

	char msg[1024];
	wcs2mbs( server_codepage, wmsg, len, msg, sizeof(msg) );
	env->ReleaseStringChars(message, wmsg);
	
	SendPlayerMessageToAll( senderid, msg );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    sendDeathMessage
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_sendDeathMessage
  (JNIEnv *env, jclass jcls, jint killerid, jint victimid, jint reason)
{
	SendDeathMessage( killerid, victimid, reason );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gameTextForAll
 * Signature: (Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gameTextForAll
  (JNIEnv *env, jclass jcls, jstring string, jint time, jint style)
{
	const jchar* wmsg = env->GetStringChars(string, NULL);
	int len = env->GetStringLength(string);

	char str[1024];
	wcs2mbs( server_codepage, wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(string, wmsg);

	GameTextForAll( str, time, style );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gameTextForPlayer
 * Signature: (ILjava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gameTextForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jstring string, jint time, jint style)
{
	const jchar* wmsg = env->GetStringChars(string, NULL);
	int len = env->GetStringLength(string);

	char str[1024];
	wcs2mbs( player_codepage[playerid], wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(string, wmsg);

	GameTextForPlayer( playerid, str, time, style );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setTimer
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_setTimer
  (JNIEnv *env, jclass jcls, jint index, jint interval, jint repeating)
{
	return SetTimer( index, interval, repeating );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    killTimer
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_killTimer
  (JNIEnv *env, jclass jcls, jint timerid)
{
	KillTimer( timerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getMaxPlayers
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getMaxPlayers
  (JNIEnv *env, jclass jcls)
{
	return GetMaxPlayers();
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setGameModeText
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setGameModeText
  (JNIEnv *env, jclass jcls, jstring string)
{
	const jchar* wmsg = env->GetStringChars(string, NULL);
	int len = env->GetStringLength(string);

	char str[1024];
	wcs2mbs( server_codepage, wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(string, wmsg);

	SetGameModeText( str );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setTeamCount
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setTeamCount
  (JNIEnv *env, jclass jcls, jint count)
{
	SetTeamCount( count );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    addPlayerClass
 * Signature: (IFFFFIIIIII)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_addPlayerClass
  (JNIEnv *env, jclass jcls, jint modelid, jfloat spawn_x, jfloat spawn_y, jfloat spawn_z,
  jfloat z_angle, jint weapon1, jint weapon1_ammo, jint weapon2, jint weapon2_ammo, jint weapon3, jint weapon3_ammo)
{
	return AddPlayerClass( modelid, spawn_x, spawn_y, spawn_z, z_angle, 
		weapon1, weapon1_ammo, weapon2, weapon2_ammo, weapon3, weapon3_ammo );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    addPlayerClassEx
 * Signature: (IIFFFFIIIIII)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_addPlayerClassEx
  (JNIEnv *env, jclass jcls, int teamid, jint modelid, jfloat spawn_x, jfloat spawn_y, jfloat spawn_z,
  jfloat z_angle, jint weapon1, jint weapon1_ammo, jint weapon2, jint weapon2_ammo, jint weapon3, jint weapon3_ammo)
{
	return AddPlayerClassEx( teamid, modelid, spawn_x, spawn_y, spawn_z, z_angle, 
		weapon1, weapon1_ammo, weapon2, weapon2_ammo, weapon3, weapon3_ammo );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    addStaticVehicle
 * Signature: (IFFFFII)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_addStaticVehicle
  (JNIEnv *env, jclass jcls, jint modelid, jfloat spawn_x, jfloat spawn_y, jfloat spawn_z, 
  jfloat z_angle, jint color1, jint color2)
{
	return AddStaticVehicle( modelid, spawn_x, spawn_x, spawn_z, z_angle, color1, color2 );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    addStaticVehicleEx
 * Signature: (IFFFFIII)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_addStaticVehicleEx
  (JNIEnv *env, jclass jcls,  jint modelid, jfloat spawn_x, jfloat spawn_y, jfloat spawn_z, 
  jfloat z_angle, jint color1, jint color2, jint respawn_delay)
{
	return AddStaticVehicleEx( modelid, spawn_x, spawn_y, spawn_z, z_angle, color1, color2, respawn_delay );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    addStaticPickup
 * Signature: (IIFFFI)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_addStaticPickup
  (JNIEnv *env, jclass jcls, jint model, jint type, jfloat x, jfloat y, jfloat z, jint virtualworld)
{
	return AddStaticPickup( model, type, x, y, z, virtualworld );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    createPickup
 * Signature: (IIFFFI)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_createPickup
  (JNIEnv *env, jclass jcls, jint model, jint type, jfloat x, jfloat y, jfloat z, jint virtualworld)
{
	return CreatePickup( model, type, x, y, z, virtualworld );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    destroyPickup
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_destroyPickup
  (JNIEnv *env, jclass jcls, jint pickup)
{
	DestroyPickup( pickup );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    showNameTags
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_showNameTags
  (JNIEnv *env, jclass jcls, jboolean enabled)
{
	ShowNameTags( enabled );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    showPlayerMarkers
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_showPlayerMarkers
  (JNIEnv *env, jclass jcls, jint mode)
{
	ShowPlayerMarkers( mode );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gameModeExit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gameModeExit
  (JNIEnv *env, jclass jcls)
{
	GameModeExit();
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setWorldTime
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setWorldTime
  (JNIEnv *env, jclass jcls, jint hour)
{
	SetWorldTime( hour );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getWeaponName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_gtaun_samp_NativeFunction_getWeaponName
  (JNIEnv *env, jclass jcls, jint weaponid)
{
	char name[32];
	GetWeaponName( weaponid, name, sizeof(name) );

	return env->NewStringUTF(name);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    enableTirePopping
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_enableTirePopping
  (JNIEnv *env, jclass jcls, jboolean enabled)
{
	EnableTirePopping( enabled );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    allowInteriorWeapons
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_allowInteriorWeapons
  (JNIEnv *env, jclass jcls, jboolean allow)
{
	AllowInteriorWeapons( allow );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setWeather
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setWeather
  (JNIEnv *env, jclass jcls, jint weatherid)
{
	SetWeather( weatherid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setGravity
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setGravity
  (JNIEnv *env, jclass jcls, jfloat gravity)
{
	SetGravity( gravity );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    allowAdminTeleport
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_allowAdminTeleport
  (JNIEnv *env, jclass jcls, jboolean allow)
{
	AllowAdminTeleport( allow );	
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setDeathDropAmount
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setDeathDropAmount
  (JNIEnv *env, jclass jcls, jint amount)
{
	SetDeathDropAmount( amount );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    createExplosion
 * Signature: (FFFIF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_createExplosion
  (JNIEnv *env, jclass jcls, jfloat x, jfloat y, jfloat z, jint type, jfloat radius)
{
	CreateExplosion( x, y, z, type, radius );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    enableZoneNames
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_enableZoneNames
  (JNIEnv *env, jclass jcls, jboolean enabled)
{
	EnableZoneNames( enabled );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    usePlayerPedAnims
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_usePlayerPedAnims
  (JNIEnv *env, jclass jcls)
{
	UsePlayerPedAnims();
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    disableInteriorEnterExits
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_disableInteriorEnterExits
  (JNIEnv *env, jclass jcls)
{
	DisableInteriorEnterExits();
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setNameTagDrawDistance
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setNameTagDrawDistance
  (JNIEnv *env, jclass jcls, jfloat distance)
{
	SetNameTagDrawDistance( distance );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    disableNameTagLOS
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_disableNameTagLOS
  (JNIEnv *env, jclass jcls)
{
	DisableNameTagLOS();
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    limitGlobalChatRadius
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_limitGlobalChatRadius
  (JNIEnv *env, jclass jcls, jfloat chat_radius)
{
	LimitGlobalChatRadius( chat_radius );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    limitPlayerMarkerRadius
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_limitPlayerMarkerRadius
  (JNIEnv *env, jclass jcls, jfloat chat_radius)
{
	LimitPlayerMarkerRadius( chat_radius );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    connectNPC
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_connectNPC
  (JNIEnv *env, jclass jcls, jstring name, jstring script)
{
	const char* str_name = env->GetStringUTFChars(name, NULL);
	const char* str_script = env->GetStringUTFChars(script, NULL);

	ConnectNPC( str_name, str_script );

	env->ReleaseStringUTFChars(name, str_name);
	env->ReleaseStringUTFChars(name, str_script);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerNPC
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerNPC
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return IsPlayerNPC(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerAdmin
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerAdmin
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return IsPlayerAdmin(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    kick
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_kick
  (JNIEnv *env, jclass jcls, jint playerid)
{
	Kick( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    ban
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_ban
  (JNIEnv *env, jclass jcls, jint playerid)
{
	Ban( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    banEx
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_banEx
  (JNIEnv *env, jclass jcls, jint playerid, jstring reason)
{
	const jchar* wmsg = env->GetStringChars(reason, NULL);
	int len = env->GetStringLength(reason);

	char str[1024];
	wcs2mbs( server_codepage, wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(reason, wmsg);

	BanEx( playerid, str );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    sendRconCommand
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_sendRconCommand
  (JNIEnv *env, jclass jcls, jstring cmd)
{
	const jchar* wmsg = env->GetStringChars(cmd, NULL);
	int len = env->GetStringLength(cmd);

	char str[1024];
	wcs2mbs( server_codepage, wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(cmd, wmsg);

	SendRconCommand( str );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getServerVarAsString
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_gtaun_samp_NativeFunction_getServerVarAsString
  (JNIEnv *env, jclass jcls, jstring varname)
{
	const char* str_varname = env->GetStringUTFChars(varname, NULL);

	char var[256];
	GetServerVarAsString( str_varname, var, sizeof(var) );
	env->ReleaseStringUTFChars(varname, str_varname);

	jchar wstr[256];
	int len = mbs2wcs( server_codepage, var, -1, wstr, sizeof(wstr)/sizeof(wstr[0]) );
	return env->NewString(wstr, len-1);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getServerVarAsInt
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getServerVarAsInt
  (JNIEnv *env, jclass jcls, jstring varname)
{
	const char* str_varname = env->GetStringUTFChars(varname, NULL);

	int ret = GetServerVarAsInt(str_varname);

	env->ReleaseStringUTFChars(varname, str_varname);
	return ret;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getServerVarAsBool
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_getServerVarAsBool
  (JNIEnv *env, jclass jcls, jstring varname)
{
	const char* str_varname = env->GetStringUTFChars(varname, NULL);

	int ret = GetServerVarAsBool(str_varname);

	env->ReleaseStringUTFChars(varname, str_varname);
	return ret;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerNetworkStats
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerNetworkStats
  (JNIEnv *env, jclass jcls, jint playerid)
{
	char retstr[2048];
	GetPlayerNetworkStats( playerid, retstr, sizeof(retstr) );

	jchar wstr[2048];
	int len = mbs2wcs( server_codepage, retstr, -1, wstr, sizeof(wstr)/sizeof(wstr[0]) );

	return env->NewString(wstr, len-1);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getNetworkStats
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_gtaun_samp_NativeFunction_getNetworkStats
  (JNIEnv *env, jclass jcls)
{
	char retstr[2048];
	GetNetworkStats( retstr, sizeof(retstr) );

	jchar wstr[2048];
	int len = mbs2wcs( server_codepage, retstr, -1, wstr, sizeof(wstr)/sizeof(wstr[0]) );

	return env->NewString(wstr, len-1);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    createMenu
 * Signature: (Ljava/lang/String;IFFFF)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_createMenu
  (JNIEnv *env, jclass jcls, jstring title, jint columns, jfloat x, jfloat y, jfloat col1width, jfloat col2width)
{
	const char* str_title = env->GetStringUTFChars(title, NULL);

	int ret = CreateMenu(str_title, columns, x, y, col1width, col2width);

	env->ReleaseStringUTFChars(title, str_title);
	return ret;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    destroyMenu
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_destroyMenu
  (JNIEnv *env, jclass jcls, jint menuid)
{
	DestroyMenu( menuid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    addMenuItem
 * Signature: (IILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_addMenuItem
  (JNIEnv *env, jclass jcls, jint menuid, jint column, jstring menutext)
{
	const char* str_menutext = env->GetStringUTFChars(menutext, NULL);

	AddMenuItem( menuid, column, str_menutext );

	env->ReleaseStringUTFChars(menutext, str_menutext);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setMenuColumnHeader
 * Signature: (IILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setMenuColumnHeader
  (JNIEnv *env, jclass jcls, jint menuid, jint column, jstring columnheader)
{
	const char* str_columnheader = env->GetStringUTFChars(columnheader, NULL);

	AddMenuItem( menuid, column, str_columnheader );

	env->ReleaseStringUTFChars(columnheader, str_columnheader);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    showMenuForPlayer
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_showMenuForPlayer
  (JNIEnv *env, jclass jcls, jint menuid, jint playerid)
{
	ShowMenuForPlayer( menuid, playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    hideMenuForPlayer
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_hideMenuForPlayer
  (JNIEnv *env, jclass jcls, jint menuid, jint playerid)
{
	HideMenuForPlayer( menuid, playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isValidMenu
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isValidMenu
  (JNIEnv *env, jclass jcls, jint menuid)
{
	return IsValidMenu( menuid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    disableMenu
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_disableMenu
  (JNIEnv *env, jclass jcls, jint menuid)
{
	DisableMenu( menuid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    disableMenuRow
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_disableMenuRow
  (JNIEnv *env, jclass jcls, jint menuid, jint row)
{
	DisableMenuRow( menuid, row );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerMenu
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerMenu
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerMenu(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawCreate
 * Signature: (FFLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_textDrawCreate
  (JNIEnv *env, jclass jcls, jfloat x, jfloat y, jstring text)
{
	const char* str = env->GetStringUTFChars(text, NULL);

	int ret = TextDrawCreate( x, y, str );

	env->ReleaseStringUTFChars(text, str);
	return ret;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawDestroy
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawDestroy
  (JNIEnv *env, jclass jcls, jint textid)
{
	TextDrawDestroy( textid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawLetterSize
 * Signature: (IFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawLetterSize
  (JNIEnv *env, jclass jcls, jint textid, jfloat x, jfloat y)
{
	TextDrawLetterSize( textid, x, y );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawTextSize
 * Signature: (IFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawTextSize
  (JNIEnv *env, jclass jcls, jint textid, jfloat x, jfloat y)
{
	TextDrawTextSize( textid, x, y );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawAlignment
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawAlignment
  (JNIEnv *env, jclass jcls, jint textid, jint alignment)
{
	TextDrawAlignment( textid, alignment );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawColor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawColor
  (JNIEnv *env, jclass jcls, jint textid, jint color)
{
	TextDrawColor( textid, color );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawUseBox
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawUseBox
  (JNIEnv *env, jclass jcls, jint textid, jboolean use)
{
	TextDrawUseBox( textid, use );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawBoxColor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawBoxColor
  (JNIEnv *env, jclass jcls, jint textid, jint color)
{
	TextDrawBoxColor( textid, color );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawSetShadow
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawSetShadow
  (JNIEnv *env, jclass jcls, jint textid, jint size)
{
	TextDrawSetShadow( textid, size );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawSetOutline
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawSetOutline
  (JNIEnv *env, jclass jcls, jint textid, jint size)
{
	TextDrawSetOutline( textid, size );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawBackgroundColor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawBackgroundColor
  (JNIEnv *env, jclass jcls, jint textid, jint color)
{
	TextDrawBackgroundColor( textid, color );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawFont
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawFont
  (JNIEnv *env, jclass jcls, jint textid, jint font)
{
	TextDrawFont( textid, font );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawSetProportional
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawSetProportional
  (JNIEnv *env, jclass jcls, jint textid, jint set)
{
	TextDrawSetProportional( textid, set );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawShowForPlayer
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawShowForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint textid)
{
	TextDrawShowForPlayer( playerid, textid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawHideForPlayer
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawHideForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint textid)
{
	TextDrawHideForPlayer( playerid, textid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawShowForAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawShowForAll
  (JNIEnv *env, jclass jcls, jint textid)
{
	TextDrawShowForAll( textid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawHideForAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawHideForAll
  (JNIEnv *env, jclass jcls, jint textid)
{
	TextDrawHideForAll( textid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    textDrawSetString
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_textDrawSetString
  (JNIEnv *env, jclass jcls, jint textid, jstring string)
{
	const char* str = env->GetStringUTFChars(string, NULL);

	TextDrawSetString( textid, str );

	env->ReleaseStringUTFChars(string, str);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneCreate
 * Signature: (FFFF)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneCreate
  (JNIEnv *env, jclass jcls, jfloat minx, jfloat miny, jfloat maxx, jfloat maxy)
{
	return GangZoneCreate(minx, miny, maxx, maxy);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneDestroy
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneDestroy
  (JNIEnv *env, jclass jcls, jint zoneid)
{
	GangZoneDestroy( zoneid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneShowForPlayer
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneShowForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint zoneid, jint color)
{
	GangZoneShowForPlayer( playerid, zoneid, color );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneShowForAll
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneShowForAll
  (JNIEnv *env, jclass jcls, jint zoneid, jint color)
{
	GangZoneShowForAll( zoneid, color );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneHideForPlayer
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneHideForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint zoneid)
{
	GangZoneHideForPlayer( playerid, zoneid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneHideForAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneHideForAll
  (JNIEnv *env, jclass jcls, jint zoneid)
{
	GangZoneHideForAll( zoneid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneFlashForPlayer
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneFlashForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint zoneid, jint flashcolor)
{
	GangZoneFlashForPlayer( playerid, zoneid, flashcolor );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneFlashForAll
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneFlashForAll
  (JNIEnv *env, jclass jcls, jint zoneid, jint flashcolor)
{
	GangZoneFlashForAll( zoneid, flashcolor );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneStopFlashForPlayer
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneStopFlashForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint zoneid)
{
	GangZoneStopFlashForPlayer( playerid, zoneid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    gangZoneStopFlashForAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_gangZoneStopFlashForAll
  (JNIEnv *env, jclass jcls, jint zoneid)
{
	GangZoneStopFlashForAll( zoneid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    create3DTextLabel
 * Signature: (Ljava/lang/String;IFFFFIZ)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_create3DTextLabel
  (JNIEnv *env, jclass jcls, jstring text, jint color, jfloat x, jfloat y , jfloat z,
  jfloat drawDistance, jint worldid, jboolean testLOS)
{
	const jchar* wmsg = env->GetStringChars(text, NULL);
	int len = env->GetStringLength(text);

	char str[1024];
	wcs2mbs( server_codepage, wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(text, wmsg);

	return Create3DTextLabel(str, color, x, y, z, drawDistance, worldid, testLOS);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    delete3DTextLabel
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_delete3DTextLabel
  (JNIEnv *env, jclass jcls, jint id)
{
	Delete3DTextLabel( id );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    attach3DTextLabelToPlayer
 * Signature: (IIFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_attach3DTextLabelToPlayer
  (JNIEnv *env, jclass jcls, jint id, jint playerid, jfloat offsetX, jfloat offsetY, jfloat offsetZ)
{
	Attach3DTextLabelToPlayer( id, playerid, offsetX, offsetY, offsetZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    attach3DTextLabelToVehicle
 * Signature: (IIFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_attach3DTextLabelToVehicle
  (JNIEnv *env, jclass jcls, jint id, jint vehicleid, jfloat offsetX, jfloat offsetY, jfloat offsetZ)
{
	Attach3DTextLabelToVehicle( id, vehicleid, offsetX, offsetY, offsetZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    update3DTextLabelText
 * Signature: (IILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_update3DTextLabelText
  (JNIEnv *env, jclass jcls, jint id, jint color, jstring text)
{
	const jchar* wmsg = env->GetStringChars(text, NULL);
	int len = env->GetStringLength(text);

	char str[1024];
	wcs2mbs( server_codepage, wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(text, wmsg);

	Update3DTextLabelText( id, color, str );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    createPlayer3DTextLabel
 * Signature: (ILjava/lang/String;IFFFFIIZ)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_createPlayer3DTextLabel
  (JNIEnv *env, jclass jcls, jint playerid, jstring text, jint color, jfloat x, jfloat y, jfloat z,
  jfloat drawDistance, jint attachedplayerid, jint attachedvehicleid, jboolean testLOS)
{
	const jchar* wmsg = env->GetStringChars(text, NULL);
	int len = env->GetStringLength(text);

	char str[1024];
	wcs2mbs( player_codepage[playerid], wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(text, wmsg);

	return CreatePlayer3DTextLabel( playerid, str, color, x, y, z, drawDistance, attachedplayerid, attachedvehicleid, testLOS );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    deletePlayer3DTextLabel
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_deletePlayer3DTextLabel
  (JNIEnv *env, jclass jcls, jint playerid, jint id)
{
	DeletePlayer3DTextLabel( playerid, id );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    updatePlayer3DTextLabelText
 * Signature: (IIILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_updatePlayer3DTextLabelText
  (JNIEnv *env, jclass jcls, jint playerid, jint id, jint color, jstring text)
{
	const jchar* wmsg = env->GetStringChars(text, NULL);
	int len = env->GetStringLength(text);

	char str[1024];
	wcs2mbs( player_codepage[playerid], wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(text, wmsg);

	UpdatePlayer3DTextLabelText( playerid, id, color, str );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    showPlayerDialog
 * Signature: (IIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_showPlayerDialog
  (JNIEnv *env, jclass jcls, jint playerid, jint dialogid, jint style,
  jstring caption, jstring info, jstring button1, jstring button2)
{
	char str_caption[128], str_info[1024], str_button1[32], str_button2[32];
	int len;
	
	const jchar* wmsg1 = env->GetStringChars(caption, NULL);
	len = env->GetStringLength(caption);
	wcs2mbs( player_codepage[playerid], wmsg1, len, str_caption, sizeof(str_caption) );
	env->ReleaseStringChars(caption, wmsg1);

	const jchar* wmsg2 = env->GetStringChars(info, NULL);
	len = env->GetStringLength(info);
	wcs2mbs( player_codepage[playerid], wmsg2, len, str_info, sizeof(str_info) );
	env->ReleaseStringChars(info, wmsg2);

	const jchar* wmsg3 = env->GetStringChars(button1, NULL);
	len = env->GetStringLength(button1);
	wcs2mbs( player_codepage[playerid], wmsg3, len, str_button1, sizeof(str_button1) );
	env->ReleaseStringChars(button1, wmsg3);

	const jchar* wmsg4 = env->GetStringChars(button2, NULL);
	len = env->GetStringLength(button2);
	wcs2mbs( player_codepage[playerid], wmsg4, len, str_button2, sizeof(str_button2) );
	env->ReleaseStringChars(button2, wmsg4);

	return ShowPlayerDialog(playerid, dialogid, style, str_caption, str_info, str_button1, str_button2);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setSpawnInfo
 * Signature: (IIIFFFFIIIIII)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setSpawnInfo
  (JNIEnv *env, jclass jcls, jint playerid, jint teamid, jint skinid, jfloat x, jfloat y, jfloat z,
  jfloat rotation, jint weapon1, jint ammo1, jint weapon2, jint ammo2, jint weapon3, jint ammo3)
{
	SetSpawnInfo( playerid, teamid, skinid, x, y, z, rotation, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3 );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    spawnPlayer
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_spawnPlayer
  (JNIEnv *env, jclass jcls, jint playerid)
{
	SpawnPlayer( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerPos
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerPos
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x, jfloat y, jfloat z)
{
	SetPlayerPos( playerid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerPosFindZ
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerPosFindZ
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x, jfloat y, jfloat z)
{
	SetPlayerPosFindZ( playerid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerPos
 * Signature: (ILnet/gtaun/samp/data/Position;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerPos
  (JNIEnv *env, jclass jcls, jint playerid, jobject point)
{
	static jclass cls = env->GetObjectClass(point);
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float x, y, z;
	GetPlayerPos( playerid, x, y, z );

	env->SetFloatField( point, fidX, x );
	env->SetFloatField( point, fidY, y );
	env->SetFloatField( point, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerFacingAngle
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerFacingAngle
  (JNIEnv *env, jclass jcls, jint playerid, jfloat angle)
{
	SetPlayerFacingAngle( playerid, angle );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerFacingAngle
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerFacingAngle
  (JNIEnv *env, jclass jcls, jint playerid)
{
	float angle;
	GetPlayerFacingAngle( playerid, angle );

	return angle;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerInRangeOfPoint
 * Signature: (IFFFF)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerInRangeOfPoint
  (JNIEnv *env, jclass jcls, jint playerid, jfloat range, jfloat x, jfloat y, jfloat z)
{
	return IsPlayerInRangeOfPoint(playerid, range, x, y, z);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerDistanceFromPoint
 * Signature: (IFFF)F
 */
JNIEXPORT jfloat JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerDistanceFromPoint
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x, jfloat y, jfloat z)
{
	return GetPlayerDistanceFromPoint(playerid, x, y, z);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerStreamedIn
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerStreamedIn
  (JNIEnv *env, jclass jcls, jint playerid, jint forplayerid)
{
	return IsPlayerStreamedIn(playerid, forplayerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerInterior
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerInterior
  (JNIEnv *env, jclass jcls, jint playerid, jint interiorid)
{
	SetPlayerInterior( playerid, interiorid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerInterior
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerInterior
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerInterior(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerHealth
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerHealth
  (JNIEnv *env, jclass jcls, jint playerid, jfloat health)
{
	SetPlayerHealth( playerid, health );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerHealth
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerHealth
  (JNIEnv *env, jclass jcls, jint playerid)
{
	float health;
	GetPlayerHealth( playerid, health );

	return health;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerArmour
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerArmour
  (JNIEnv *env, jclass jcls, jint playerid, jfloat armour)
{
	SetPlayerArmour( playerid, armour );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerArmour
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerArmour
  (JNIEnv *env, jclass jcls, jint playerid)
{
	float armour;
	GetPlayerArmour( playerid, armour );

	return armour;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerAmmo
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerAmmo
  (JNIEnv *env, jclass jcls, jint playerid, jint weaponslot, jint ammo)
{
	SetPlayerAmmo( playerid, weaponslot, ammo );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerAmmo
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerAmmo
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerAmmo(playerid);;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerWeaponState
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerWeaponState
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerWeaponState(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerTeam
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerTeam
  (JNIEnv *env, jclass jcls, jint playerid, jint teamid)
{
	SetPlayerTeam( playerid, teamid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerTeam
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerTeam
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerTeam(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerScore
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerScore
  (JNIEnv *env, jclass jcls, jint playerid, jint score)
{
	SetPlayerScore( playerid, score );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerScore
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerScore
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerScore(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerDrunkLevel
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerDrunkLevel
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerDrunkLevel(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerDrunkLevel
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerDrunkLevel
  (JNIEnv *env, jclass jcls, jint playerid, jint level)
{
	SetPlayerDrunkLevel( playerid, level );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerColor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerColor
  (JNIEnv *env, jclass jcls, jint playerid, jint color)
{
	SetPlayerColor( playerid, color );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerColor
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerColor
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerColor(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerSkin
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerSkin
  (JNIEnv *env, jclass jcls, jint playerid, jint skinid)
{
	SetPlayerSkin( playerid, skinid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerSkin
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerSkin
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerSkin(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    givePlayerWeapon
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_givePlayerWeapon
  (JNIEnv *env, jclass jcls, jint playerid, jint weaponid, jint ammo)
{
	GivePlayerWeapon( playerid, weaponid, ammo );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    resetPlayerWeapons
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_resetPlayerWeapons
  (JNIEnv *env, jclass jcls, jint playerid)
{
	ResetPlayerWeapons( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerArmedWeapon
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerArmedWeapon
  (JNIEnv *env, jclass jcls, jint playerid, jint weaponid)
{
	SetPlayerArmedWeapon( playerid, weaponid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerWeaponData
 * Signature: (IILnet/gtaun/samp/data/WeaponData;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerWeaponData
  (JNIEnv *env, jclass jcls, jint playerid, jint slot, jobject weapondata)
{
	static jclass cls = env->GetObjectClass(weapondata);
	static jfieldID fidId = env->GetFieldID(cls, "id", "I");
	static jfieldID fidAmmo = env->GetFieldID(cls, "ammo", "I");

	int weaponid, ammo;
	GetPlayerWeaponData( playerid, slot, weaponid, ammo );

	env->SetIntField( weapondata, fidId, weaponid );
	env->SetIntField( weapondata, fidAmmo, ammo );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    givePlayerMoney
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_givePlayerMoney
  (JNIEnv *env, jclass jcls, jint playerid, jint money)
{
	GivePlayerMoney( playerid, money );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    resetPlayerMoney
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_resetPlayerMoney
  (JNIEnv *env, jclass jcls, jint playerid)
{
	ResetPlayerMoney( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerName
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerName
  (JNIEnv *env, jclass jcls, jint playerid, jstring name)
{
	const jchar* wmsg = env->GetStringChars(name, NULL);
	int len = env->GetStringLength(name);

	char str[1024];
	wcs2mbs( server_codepage, wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(name, wmsg);

	return SetPlayerName(playerid, str);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerMoney
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerMoney
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerMoney(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerState
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerState
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerState(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerIp
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerIp
  (JNIEnv *env, jclass jcls, jint playerid)
{
	char ip[16];
	GetPlayerIp( playerid, ip, sizeof(ip) );

	return env->NewStringUTF(ip);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerPing
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerPing
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerPing(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerWeapon
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerWeapon
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerWeapon(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerKeys
 * Signature: (ILnet/gtaun/samp/data/KeyState;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerKeys
  (JNIEnv *env, jclass jcls, jint playerid, jobject keystate)
{
	static jclass cls = env->GetObjectClass(keystate);
	static jfieldID fidKeys = env->GetFieldID(cls, "keys", "I");
	static jfieldID fidUpdown = env->GetFieldID(cls, "updown", "I");
	static jfieldID fidLeftright = env->GetFieldID(cls, "leftright", "I");

	int keys, updown, leftright;
	GetPlayerKeys( playerid, keys, updown, leftright );

	env->SetIntField( keystate, fidKeys, keys );
	env->SetIntField( keystate, fidUpdown, updown );
	env->SetIntField( keystate, fidLeftright, leftright );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerName
  (JNIEnv *env, jclass jcls, jint playerid)
{
	char name[MAX_PLAYER_NAME];
	GetPlayerName( playerid, name, sizeof(name) );

	jchar wstr[MAX_PLAYER_NAME];
	int len = mbs2wcs( server_codepage, name, -1, wstr, sizeof(wstr)/sizeof(wstr[0]) );

	return env->NewString(wstr, len-1);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerTime
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerTime
  (JNIEnv *env, jclass jcls, jint playerid, jint hour, jint minute)
{
	SetPlayerTime( playerid, hour, minute );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerTime
 * Signature: (ILjava/sql/Time;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerTime
  (JNIEnv *env, jclass jcls, jint playerid, jobject time)
{
	static jclass cls = env->GetObjectClass(time);
	static jfieldID fidHour = env->GetFieldID(cls, "hour", "I");
	static jfieldID fidMinute = env->GetFieldID(cls, "minute", "I");

	int hour, minute;
	GetPlayerTime( playerid, hour, minute );

	env->SetIntField( time, fidHour, hour );
	env->SetIntField( time, fidMinute, minute );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    togglePlayerClock
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_togglePlayerClock
  (JNIEnv *env, jclass jcls, jint playerid, jboolean toggle)
{
	TogglePlayerClock( playerid, toggle );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerWeather
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerWeather
  (JNIEnv *env, jclass jcls, jint playerid, jint weather)
{
	SetPlayerWeather( playerid, weather );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    forceClassSelection
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_forceClassSelection
  (JNIEnv *env, jclass jcls, jint playerid)
{
	ForceClassSelection( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerWantedLevel
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerWantedLevel
  (JNIEnv *env, jclass jcls, jint playerid, jint level)
{
	SetPlayerWantedLevel( playerid, level );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerWantedLevel
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerWantedLevel
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerWantedLevel(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerFightingStyle
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerFightingStyle
  (JNIEnv *env, jclass jcls, jint playerid, jint style)
{
	SetPlayerFightingStyle( playerid, style );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerFightingStyle
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerFightingStyle
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerFightingStyle(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerVelocity
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerVelocity
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x, jfloat y, jfloat z)
{
	SetPlayerVelocity( playerid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerVelocity
 * Signature: (ILnet/gtaun/samp/data/Velocity;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerVelocity
  (JNIEnv *env, jclass jcls, jint playerid, jobject velocity)
{
	static jclass cls = env->GetObjectClass(velocity);
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float x, y, z;
	GetPlayerVelocity( playerid, x, y, z );

	env->SetFloatField( velocity, fidX, x );
	env->SetFloatField( velocity, fidY, y );
	env->SetFloatField( velocity, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    playCrimeReportForPlayer
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_playCrimeReportForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint suspectid, jint crime)
{
	PlayCrimeReportForPlayer( playerid, suspectid, crime );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerShopName
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerShopName
  (JNIEnv *env, jclass jcls, jint playerid, jstring name)
{
	const jchar* wmsg = env->GetStringChars(name, NULL);
	int len = env->GetStringLength(name);

	char str[1024];
	wcs2mbs( player_codepage[playerid], wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(name, wmsg);

	SetPlayerShopName( playerid, str );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerSkillLevel
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerSkillLevel
  (JNIEnv *env, jclass jcls, jint playerid, jint skill, jint level)
{
	SetPlayerSkillLevel( playerid, skill, level );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerSurfingVehicleID
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerSurfingVehicleID
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerSurfingVehicleID(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerSurfingObjectID
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerSurfingObjectID
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerSurfingObjectID(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerAttachedObject
 * Signature: (IIIIFFFFFFFFF)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerAttachedObject
  (JNIEnv *env, jclass jcls, jint playerid, jint index, jint modelid, jint bone, jfloat offsetX, jfloat offsetY,
  jfloat offsetZ, jfloat rotX, jfloat rotY, jfloat rotZ, jfloat scaleX, jfloat scaleY, jfloat scaleZ)
{
	return SetPlayerAttachedObject( playerid, index, modelid, bone,
		offsetX, offsetY, offsetZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    removePlayerAttachedObject
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_removePlayerAttachedObject
  (JNIEnv *env, jclass jcls, jint playerid, jint index)
{
	return RemovePlayerAttachedObject(playerid, index);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerAttachedObjectSlotUsed
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerAttachedObjectSlotUsed
  (JNIEnv *env, jclass jcls, jint playerid, jint index)
{
	return IsPlayerAttachedObjectSlotUsed(playerid, index);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerChatBubble
 * Signature: (ILjava/lang/String;IFI)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerChatBubble
  (JNIEnv *env, jclass jcls, jint playerid, jstring text, jint color, jfloat drawdistance, jint expiretime)
{
	const jchar* wmsg = env->GetStringChars(text, NULL);
	int len = env->GetStringLength(text);

	char str[1024];
	wcs2mbs( player_codepage[playerid], wmsg, len, str, sizeof(str) );
	env->ReleaseStringChars(text, wmsg);

	SetPlayerChatBubble( playerid, str, color, drawdistance, expiretime );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    putPlayerInVehicle
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_putPlayerInVehicle
  (JNIEnv *env, jclass jcls, jint playerid, jint vehicleid, jint seatid)
{
	PutPlayerInVehicle( playerid, vehicleid, seatid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerVehicleID
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerVehicleID
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerVehicleID(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerVehicleSeat
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerVehicleSeat
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerVehicleSeat(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    removePlayerFromVehicle
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_removePlayerFromVehicle
  (JNIEnv *env, jclass jcls, jint playerid)
{
	RemovePlayerFromVehicle(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    togglePlayerControllable
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_togglePlayerControllable
  (JNIEnv *env, jclass jcls, jint playerid, jboolean toggle)
{
	TogglePlayerControllable( playerid, toggle );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    playerPlaySound
 * Signature: (IIFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_playerPlaySound
  (JNIEnv *env, jclass jcls, jint playerid, jint soundid, jfloat x, jfloat y, jfloat z)
{
	PlayerPlaySound( playerid, soundid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    applyAnimation
 * Signature: (ILjava/lang/String;Ljava/lang/String;FIIIIII)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_applyAnimation
  (JNIEnv *env, jclass jcls, jint playerid, jstring animlib, jstring animname,
  jfloat delta, jint loop, jint lockX, jint lockY, jint freeze, jint time, jint forcesync)
{
	const char* str_animlib = env->GetStringUTFChars(animlib, NULL);
	const char* str_animname = env->GetStringUTFChars(animname, NULL);

	ApplyAnimation( playerid, str_animlib, str_animname, delta, loop, lockX, lockY, freeze, time, forcesync );

	env->ReleaseStringUTFChars(animlib, str_animlib);
	env->ReleaseStringUTFChars(animname, str_animname);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    clearAnimations
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_clearAnimations
  (JNIEnv *env, jclass jcls, jint playerid, jint forcesync)
{
	ClearAnimations( playerid, forcesync );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerAnimationIndex
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerAnimationIndex
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerAnimationIndex(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerSpecialAction
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerSpecialAction
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerSpecialAction(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerSpecialAction
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerSpecialAction
  (JNIEnv *env, jclass jcls, jint playerid, jint actionid)
{
	SetPlayerSpecialAction( playerid, actionid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerCheckpoint
 * Signature: (IFFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerCheckpoint
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x, jfloat y, jfloat z, jfloat size)
{
	SetPlayerCheckpoint( playerid, x, y, z, size );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    disablePlayerCheckpoint
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_disablePlayerCheckpoint
  (JNIEnv *env, jclass jcls, jint playerid)
{
	DisablePlayerCheckpoint( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerRaceCheckpoint
 * Signature: (IIFFFFFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerRaceCheckpoint
  (JNIEnv *env, jclass jcls, jint playerid, jint type, jfloat x, jfloat y, jfloat z,
  jfloat nextX, jfloat nextY, jfloat nextZ, jfloat size)
{
	SetPlayerRaceCheckpoint( playerid, type, x,  y,  z, nextX, nextY, nextZ, size );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    disablePlayerRaceCheckpoint
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_disablePlayerRaceCheckpoint
  (JNIEnv *env, jclass jcls, jint playerid)
{
	DisablePlayerRaceCheckpoint( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerWorldBounds
 * Signature: (IFFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerWorldBounds
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x_max, jfloat x_min, jfloat y_max, jfloat y_min)
{
	SetPlayerWorldBounds( playerid, x_max, x_min, y_max, y_min );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerMarkerForPlayer
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerMarkerForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint showplayerid, jint color)
{
	SetPlayerMarkerForPlayer( playerid, showplayerid, color );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    showPlayerNameTagForPlayer
 * Signature: (IIZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_showPlayerNameTagForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint showplayerid, jboolean show)
{
	ShowPlayerNameTagForPlayer( playerid, showplayerid, show );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerMapIcon
 * Signature: (IIFFFIII)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerMapIcon
  (JNIEnv *env, jclass jcls, jint playerid, jint iconid, jfloat x, jfloat y, jfloat z, jint markertype, jint color, jint style)
{
	SetPlayerMapIcon( playerid, iconid, x, y, z, markertype, color, style );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    removePlayerMapIcon
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_removePlayerMapIcon
  (JNIEnv *env, jclass jcls, jint playerid, jint iconid)
{
	RemovePlayerMapIcon( playerid, iconid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    allowPlayerTeleport
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_allowPlayerTeleport
  (JNIEnv *env, jclass jcls, jint playerid, jboolean allow)
{
	AllowPlayerTeleport( playerid, allow );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerCameraPos
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerCameraPos
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x, jfloat y, jfloat z)
{
	SetPlayerCameraPos( playerid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerCameraLookAt
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerCameraLookAt
  (JNIEnv *env, jclass jcls, jint playerid, jfloat x, jfloat y, jfloat z)
{
	SetPlayerCameraLookAt( playerid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setCameraBehindPlayer
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setCameraBehindPlayer
  (JNIEnv *env, jclass jcls, jint playerid)
{
	SetCameraBehindPlayer( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerCameraPos
 * Signature: (ILnet/gtaun/samp/data/Point;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerCameraPos
  (JNIEnv *env, jclass jcls, jint playerid, jobject point)
{
	static jclass cls = env->GetObjectClass(point);
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float x, y, z;
	GetPlayerCameraPos( playerid, x, y, z );

	env->SetFloatField( point, fidX, x );
	env->SetFloatField( point, fidY, y );
	env->SetFloatField( point, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerCameraMode
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerCameraMode
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerCameraMode(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerConnected
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerConnected
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return IsPlayerConnected(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerInVehicle
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerInVehicle
  (JNIEnv *env, jclass jcls, jint playerid, jint vehicleid)
{
	return IsPlayerInVehicle(playerid, vehicleid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerInAnyVehicle
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerInAnyVehicle
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return IsPlayerInAnyVehicle(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerInCheckpoint
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerInCheckpoint
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return IsPlayerInCheckpoint(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isPlayerInRaceCheckpoint
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isPlayerInRaceCheckpoint
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return IsPlayerInRaceCheckpoint(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerVirtualWorld
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerVirtualWorld
  (JNIEnv *env, jclass jcls, jint playerid, jint worldid)
{
	SetPlayerVirtualWorld( playerid, worldid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerVirtualWorld
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerVirtualWorld
  (JNIEnv *env, jclass jcls, jint playerid)
{
	return GetPlayerVirtualWorld(playerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    enableStuntBonusForPlayer
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_enableStuntBonusForPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint enabled)
{
	EnableStuntBonusForPlayer( playerid, enabled );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    enableStuntBonusForAll
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_enableStuntBonusForAll
  (JNIEnv *env, jclass jcls, jboolean enabled)
{
	EnableStuntBonusForAll( enabled );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    togglePlayerSpectating
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_togglePlayerSpectating
  (JNIEnv *env, jclass jcls, jint playerid, jboolean toggle)
{
	TogglePlayerSpectating( playerid, toggle );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    playerSpectatePlayer
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_playerSpectatePlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint targetplayerid, jint mode)
{
	PlayerSpectatePlayer( playerid, targetplayerid, mode );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    playerSpectateVehicle
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_playerSpectateVehicle
  (JNIEnv *env, jclass jcls, jint playerid, jint targetvehicleid, jint mode)
{
	PlayerSpectateVehicle( playerid, targetvehicleid, mode );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    startRecordingPlayerData
 * Signature: (IILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_startRecordingPlayerData
  (JNIEnv *env, jclass jcls, jint playerid, jint recordtype, jstring recordname)
{
	const char* str_recordname = env->GetStringUTFChars(recordname, NULL);

	StartRecordingPlayerData( playerid, recordtype, str_recordname );

	env->ReleaseStringUTFChars(recordname, str_recordname);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    stopRecordingPlayerData
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_stopRecordingPlayerData
  (JNIEnv *env, jclass jcls, jint playerid)
{
	StopRecordingPlayerData( playerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    createVehicle
 * Signature: (IFFFFIII)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_createVehicle
  (JNIEnv *env, jclass jcls, jint model, jfloat x, jfloat y, jfloat z, jfloat rotation, jint color1, jint color2, jint respawnDelay)
{
	return CreateVehicle(model, x, y, z, rotation, color1, color2, respawnDelay);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    destroyVehicle
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_destroyVehicle
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	DestroyVehicle( vehicleid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isVehicleStreamedIn
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isVehicleStreamedIn
  (JNIEnv *env, jclass jcls, jint vehicleid, jint forplayerid)
{
	return IsVehicleStreamedIn(vehicleid, forplayerid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehiclePos
 * Signature: (ILnet/gtaun/samp/data/Position;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getVehiclePos
  (JNIEnv *env, jclass jcls, jint vehicleid, jobject point)
{
	static jclass cls = env->GetObjectClass(point);
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float x, y, z;
	GetVehiclePos( vehicleid, x, y, z );

	env->SetFloatField( point, fidX, x );
	env->SetFloatField( point, fidY, y );
	env->SetFloatField( point, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehiclePos
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehiclePos
  (JNIEnv *env, jclass jcls, jint vehicleid, jfloat x, jfloat y, jfloat z)
{
	SetVehiclePos( vehicleid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleZAngle
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleZAngle
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	float angle;
	GetVehicleZAngle( vehicleid, angle );

	return angle;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleRotationQuat
 * Signature: (ILnet/gtaun/samp/data/Quaternions;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleRotationQuat
  (JNIEnv *env, jclass jcls, jint vehicleid, jobject quat)
{
	static jclass cls = env->GetObjectClass(quat);
	static jfieldID fidW = env->GetFieldID(cls, "w", "F");
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float w, x, y, z;
	GetVehicleRotationQuat( vehicleid, w, x, y, z );

	env->SetFloatField( quat, fidW, w );
	env->SetFloatField( quat, fidX, x );
	env->SetFloatField( quat, fidY, y );
	env->SetFloatField( quat, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleDistanceFromPoint
 * Signature: (IFFF)F
 */
JNIEXPORT jfloat JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleDistanceFromPoint
  (JNIEnv *env, jclass jcls, jint vehicleid, jfloat x, jfloat y, jfloat z)
{
	return GetVehicleDistanceFromPoint(vehicleid, x, y, z);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleZAngle
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleZAngle
  (JNIEnv *env, jclass jcls, jint vehicleid, jfloat angle)
{
	SetVehicleZAngle( vehicleid, angle );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleParamsForPlayer
 * Signature: (IIZZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleParamsForPlayer
  (JNIEnv *env, jclass jcls, jint vehicleid, jint playerid, jboolean objective, jboolean doorslocked)
{
	SetVehicleParamsForPlayer( vehicleid, playerid, objective, doorslocked );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    manualVehicleEngineAndLights
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_manualVehicleEngineAndLights
  (JNIEnv *env, jclass jcls)
{
	ManualVehicleEngineAndLights();
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleParamsEx
 * Signature: (IZZZZZZZ)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleParamsEx
  (JNIEnv *env, jclass jcls, jint vehicleid, jint engine, jint lights,
  jint alarm, jint doors, jint bonnet, jint boot, jint objective)
{
	SetVehicleParamsEx( vehicleid, engine, lights, alarm, doors, bonnet, boot, objective );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleParamsEx
 * Signature: (ILnet/gtaun/samp/data/VehicleState;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleParamsEx
  (JNIEnv *env, jclass jcls, jint vehicleid, jobject state)
{
	static jclass cls = env->GetObjectClass(state);
	static jfieldID fidEngine = env->GetFieldID(cls, "engine", "I");
	static jfieldID fidLights = env->GetFieldID(cls, "lights", "I");
	static jfieldID fidAlarm = env->GetFieldID(cls, "alarm", "I");
	static jfieldID fidDoors = env->GetFieldID(cls, "doors", "I");
	static jfieldID fidBonnet = env->GetFieldID(cls, "bonnet", "I");
	static jfieldID fidBoot = env->GetFieldID(cls, "boot", "I");
	static jfieldID fidObjective = env->GetFieldID(cls, "objective", "I");

	int engine, lights, alarm, doors, bonnet, boot, objective;
	GetVehicleParamsEx( vehicleid, engine, lights, alarm, doors, bonnet, boot, objective );

	env->SetIntField( state, fidEngine, engine );
	env->SetIntField( state, fidLights, lights );
	env->SetIntField( state, fidAlarm, alarm );
	env->SetIntField( state, fidDoors, doors );
	env->SetIntField( state, fidBonnet, bonnet );
	env->SetIntField( state, fidBoot, boot );
	env->SetIntField( state, fidObjective, objective );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleToRespawn
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleToRespawn
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	SetVehicleToRespawn( vehicleid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    linkVehicleToInterior
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_linkVehicleToInterior
  (JNIEnv *env, jclass jcls, jint vehicleid, jint interiorid)
{
	LinkVehicleToInterior( vehicleid, interiorid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    addVehicleComponent
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_addVehicleComponent
  (JNIEnv *env, jclass jcls, jint vehicleid, jint componentid)
{
	AddVehicleComponent( vehicleid, componentid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    removeVehicleComponent
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_removeVehicleComponent
  (JNIEnv *env, jclass jcls, jint vehicleid, jint componentid)
{
	RemoveVehicleComponent( vehicleid, componentid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    changeVehicleColor
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_changeVehicleColor
  (JNIEnv *env, jclass jcls, jint vehicleid, jint color1, jint color2)
{
	ChangeVehicleColor( vehicleid, color1, color2 );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    changeVehiclePaintjob
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_changeVehiclePaintjob
  (JNIEnv *env, jclass jcls, jint vehicleid, jint paintjobid)
{
	ChangeVehiclePaintjob( vehicleid, paintjobid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleHealth
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleHealth
  (JNIEnv *env, jclass jcls, jint vehicleid, jfloat health)
{
	SetVehicleHealth( vehicleid, health );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleHealth
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleHealth
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	float health;
	GetVehicleHealth( vehicleid, health );

	return health;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    attachTrailerToVehicle
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_attachTrailerToVehicle
  (JNIEnv *env, jclass jcls, jint trailerid, jint vehicleid)
{
	AttachTrailerToVehicle( trailerid, vehicleid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    detachTrailerFromVehicle
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_detachTrailerFromVehicle
  (JNIEnv *env, jclass jcls, jint trailerid)
{
	DetachTrailerFromVehicle( trailerid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isTrailerAttachedToVehicle
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isTrailerAttachedToVehicle
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	return IsTrailerAttachedToVehicle(vehicleid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleTrailer
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleTrailer
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	return GetVehicleTrailer(vehicleid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleNumberPlate
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleNumberPlate
  (JNIEnv *env, jclass jcls, jint vehicleid, jstring numberplate)
{
	const char* str_numberplate = env->GetStringUTFChars(numberplate, NULL);

	SetVehicleNumberPlate( vehicleid, str_numberplate );

	env->ReleaseStringUTFChars(numberplate, str_numberplate);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleModel
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleModel
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	return 0;
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleComponentInSlot
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleComponentInSlot
  (JNIEnv *env, jclass jcls, jint vehicleid, jint slot)
{
	return GetVehicleComponentInSlot(vehicleid, slot);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleComponentType
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleComponentType
  (JNIEnv *env, jclass jcls, jint component)
{
	return GetVehicleComponentType(component);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    repairVehicle
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_repairVehicle
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	RepairVehicle( vehicleid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleVelocity
 * Signature: (ILnet/gtaun/samp/data/Velocity;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleVelocity
  (JNIEnv *env, jclass jcls, jint vehicleid, jobject velocity)
{
	static jclass cls = env->GetObjectClass(velocity);
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float x, y, z;
	GetVehicleVelocity( vehicleid, x, y, z );

	env->SetFloatField( velocity, fidX, x );
	env->SetFloatField( velocity, fidY, y );
	env->SetFloatField( velocity, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleVelocity
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleVelocity
  (JNIEnv *env, jclass jcls, jint vehicleid, jfloat x, jfloat y, jfloat z)
{
	SetVehicleVelocity( vehicleid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleAngularVelocity
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleAngularVelocity
  (JNIEnv *env, jclass jcls, jint vehicleid, jfloat x, jfloat y, jfloat z)
{
	SetVehicleAngularVelocity( vehicleid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleDamageStatus
 * Signature: (ILnet/gtaun/samp/data/VehicleDamage;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleDamageStatus
  (JNIEnv *env, jclass jcls, jint vehicleid, jobject damage )
{
	static jclass cls = env->GetObjectClass(damage);
	static jfieldID fidPanels = env->GetFieldID(cls, "panels", "I");
	static jfieldID fidDoors = env->GetFieldID(cls, "doors", "I");
	static jfieldID fidLights = env->GetFieldID(cls, "lights", "I");
	static jfieldID fidTires = env->GetFieldID(cls, "lights", "I");

	int panels, doors, lights, tires;
	GetVehicleDamageStatus( vehicleid, panels, doors, lights, tires );

	env->SetIntField( damage, fidPanels, panels );
	env->SetIntField( damage, fidDoors, doors );
	env->SetIntField( damage, fidLights, lights );
	env->SetIntField( damage, fidTires, tires );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    updateVehicleDamageStatus
 * Signature: (IIIII)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_updateVehicleDamageStatus
  (JNIEnv *env, jclass jcls, jint vehicleid, jint panels, jint doors, jint lights, jint tires)
{
	UpdateVehicleDamageStatus( vehicleid, panels, doors, lights, tires );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setVehicleVirtualWorld
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setVehicleVirtualWorld
  (JNIEnv *env, jclass jcls, jint vehicleid, jint worldid)
{
	SetVehicleVirtualWorld( vehicleid, worldid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getVehicleVirtualWorld
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_getVehicleVirtualWorld
  (JNIEnv *env, jclass jcls, jint vehicleid)
{
	return GetVehicleVirtualWorld(vehicleid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    createObject
 * Signature: (IFFFFFFF)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_createObject
  (JNIEnv *env, jclass jcls, jint modelid, jfloat x, jfloat y, jfloat z, jfloat rX, jfloat rY, jfloat rZ, jfloat drawDistance)
{
	return CreateObject( modelid, x, y, z, rX, rY, rZ, drawDistance );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    attachObjectToVehicle
 * Signature: (IIFFFFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_attachObjectToVehicle
  (JNIEnv *env, jclass jcls, jint objectid, jint vehicleid, jfloat x, jfloat y, jfloat z, jfloat rX, jfloat rY, jfloat rZ)
{
	AttachObjectToVehicle( objectid, vehicleid, x, y, z, rX, rY, rZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setObjectPos
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setObjectPos
  (JNIEnv *env, jclass jcls, jint objectid, jfloat x, jfloat y, jfloat z)
{
	SetObjectPos( objectid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getObjectPos
 * Signature: (ILnet/gtaun/samp/data/Point;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getObjectPos
  (JNIEnv *env, jclass jcls, jint objectid, jobject point)
{
	static jclass cls = env->GetObjectClass(point);
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float x, y, z;
	GetObjectPos( objectid, x, y, z );

	env->SetFloatField( point, fidX, x );
	env->SetFloatField( point, fidY, y );
	env->SetFloatField( point, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setObjectRot
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setObjectRot
  (JNIEnv *env, jclass jcls, jint objectid, jfloat rotX, jfloat rotY, jfloat rotZ)
{
	SetObjectRot( objectid, rotX, rotY, rotZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getObjectRot
 * Signature: (ILnet/gtaun/samp/data/PointRot;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getObjectRot
  (JNIEnv *env, jclass jcls, jint objectid, jobject pointrot)
{
	static jclass cls = env->GetObjectClass(pointrot);
	static jfieldID fidRotX = env->GetFieldID(cls, "rotX", "F");
	static jfieldID fidRotY = env->GetFieldID(cls, "rotY", "F");
	static jfieldID fidRotZ = env->GetFieldID(cls, "rotZ", "F");

	float rotX, rotY, rotZ;
	GetObjectPos( objectid, rotX, rotY, rotZ );

	env->SetFloatField( pointrot, fidRotX, rotX );
	env->SetFloatField( pointrot, fidRotY, rotY );
	env->SetFloatField( pointrot, fidRotZ, rotZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isValidObject
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isValidObject
  (JNIEnv *env, jclass jcls, jint objectid)
{
	return IsValidObject(objectid);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    destroyObject
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_destroyObject
  (JNIEnv *env, jclass jcls, jint objectid)
{
	DestroyObject( objectid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    moveObject
 * Signature: (IFFFF)V
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_moveObject
  (JNIEnv *env, jclass jcls, jint objectid, jfloat x, jfloat y, jfloat z, jfloat speed)
{
	return MoveObject( objectid, x, y, z, speed );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    stopObject
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_stopObject
  (JNIEnv *env, jclass jcls, jint objectid)
{
	StopObject( objectid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    createPlayerObject
 * Signature: (IIFFFFFFF)I
 */
JNIEXPORT jint JNICALL Java_net_gtaun_samp_NativeFunction_createPlayerObject
  (JNIEnv *env, jclass jcls, jint playerid, jint modelid, jfloat x, jfloat y, jfloat z, jfloat rX, jfloat rY, jfloat rZ, jfloat drawDistance)
{
	return CreatePlayerObject(playerid, modelid, x, y, z, rX, rY, rZ, drawDistance);
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerObjectPos
 * Signature: (IIFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerObjectPos
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid, jfloat x, jfloat y, jfloat z)
{
	SetPlayerObjectPos( playerid, objectid, x, y, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerObjectPos
 * Signature: (IILnet/gtaun/samp/data/Point;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerObjectPos
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid, jobject point)
{
	static jclass cls = env->GetObjectClass(point);
	static jfieldID fidX = env->GetFieldID(cls, "x", "F");
	static jfieldID fidY = env->GetFieldID(cls, "y", "F");
	static jfieldID fidZ = env->GetFieldID(cls, "z", "F");

	float x, y, z;
	GetPlayerObjectPos( playerid, objectid, x, y, z );

	env->SetFloatField( point, fidX, x );
	env->SetFloatField( point, fidY, y );
	env->SetFloatField( point, fidZ, z );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    setPlayerObjectRot
 * Signature: (IIFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_setPlayerObjectRot
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid, jfloat rotX, jfloat rotY, jfloat rotZ)
{
	SetPlayerObjectRot( playerid, objectid, rotX, rotY, rotZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    getPlayerObjectRot
 * Signature: (IILnet/gtaun/samp/data/PointRot;)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_getPlayerObjectRot
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid, jobject pointrot)
{
	static jclass cls = env->GetObjectClass(pointrot);
	static jfieldID fidRotX = env->GetFieldID(cls, "rotX", "F");
	static jfieldID fidRotY = env->GetFieldID(cls, "rotY", "F");
	static jfieldID fidRotZ = env->GetFieldID(cls, "rotZ", "F");

	float rotX, rotY, rotZ;
	GetPlayerObjectRot( playerid, objectid, rotX, rotY, rotZ );

	env->SetFloatField( pointrot, fidRotX, rotX );
	env->SetFloatField( pointrot, fidRotY, rotY );
	env->SetFloatField( pointrot, fidRotZ, rotZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    isValidPlayerObject
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_net_gtaun_samp_NativeFunction_isValidPlayerObject
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid)
{
	return IsValidPlayerObject( playerid, objectid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    destroyPlayerObject
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_destroyPlayerObject
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid)
{
	DestroyPlayerObject( playerid, objectid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    movePlayerObject
 * Signature: (IIFFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_movePlayerObject
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid, jfloat x, jfloat y, jfloat z, jfloat speed)
{
	MovePlayerObject( playerid, objectid, x, y, z, speed );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    stopPlayerObject
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_stopPlayerObject
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid)
{
	StopPlayerObject( playerid, objectid );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    attachObjectToPlayer
 * Signature: (IIFFFFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_attachObjectToPlayer
  (JNIEnv *env, jclass jcls, jint objectid, jint playerid,
  jfloat offsetX, jfloat offsetY, jfloat offsetZ, jfloat rX, jfloat rY, jfloat rZ)
{
	AttachObjectToPlayer( objectid, playerid, offsetX, offsetY, offsetZ, rX, rY, rZ );
}

/*
 * Class:     net_gtaun_samp_NativeFunction
 * Method:    attachPlayerObjectToPlayer
 * Signature: (IIIFFFFFF)V
 */
JNIEXPORT void JNICALL Java_net_gtaun_samp_NativeFunction_attachPlayerObjectToPlayer
  (JNIEnv *env, jclass jcls, jint playerid, jint objectid, jint attachplayerid,
  jfloat offsetX, jfloat offsetY, jfloat offsetZ, jfloat rX, jfloat rY, jfloat rZ)
{
	AttachPlayerObjectToPlayer( playerid, objectid, attachplayerid, offsetX, offsetY, offsetZ, rX, rY, rZ );
}
