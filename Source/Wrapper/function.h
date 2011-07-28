/*  SA-MP Functions
 *
 *  (c) Copyright 2005-2010, SA-MP Team
 *
 */


typedef void (*logprintf_t)(const char* format, ...);
extern logprintf_t logprintf;


//----------------------------------------------------------
// a_samp.inc

// Util
int SendClientMessage(int playerid, int color, const char* message);
int SendClientMessageToAll(int color, const char* message);
int SendPlayerMessageToPlayer(int playerid, int senderid, const char* message);
int SendPlayerMessageToAll(int senderid, const char* message);
int SendDeathMessage(int killer, int killee, int weapon);
int GameTextForAll(const char* string, int time, int style);
int GameTextForPlayer(int playerid, const char* string, int time, int style);
int SetTimer(int timerIndex, int interval, int repeating);
int KillTimer(int timerid);
int GetMaxPlayers();
int LimitGlobalChatRadius(float chat_radius);

// Game
int SetGameModeText(const char* string);
int SetTeamCount(int count);
int AddPlayerClass(int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo);
int AddPlayerClassEx(int teamid, int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo);
int AddStaticVehicle(int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2);
int AddStaticVehicleEx(int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2, int respawn_delay);
int AddStaticPickup(int model, int type, float X, float Y, float Z, int virtualworld);
int CreatePickup(int model, int type, float X, float Y, float Z, int virtualworld);
int DestroyPickup(int pickup);
int ShowNameTags(int show);
int ShowPlayerMarkers(int mode);
int GameModeExit();
int SetWorldTime(int hour);
int GetWeaponName(int weaponid, char* weapon, int len);
int EnableTirePopping(int enable);
int AllowInteriorWeapons(int allow);
int SetWeather(int weatherid);
int SetGravity(float gravity);
int AllowAdminTeleport(int allow);
int SetDeathDropAmount(int amount);
int CreateExplosion(float X, float Y, float Z, int type, float Radius);
int EnableZoneNames(int enable);
int UsePlayerPedAnims();
int DisableInteriorEnterExits();
int SetNameTagDrawDistance(float distance);
int DisableNameTagLOS();
int LimitGlobalChatRadius( float chat_radius );
int LimitPlayerMarkerRadius( float marker_radius );

// Npc
int ConnectNPC(const char* name, const char* script);
int IsPlayerNPC(int playerid);

// Admin
int IsPlayerAdmin(int playerid);
int Kick(int playerid);
int Ban(int playerid);
int BanEx(int playerid, const char* reason);
int SendRconCommand(const char* cmd);
int GetServerVarAsString(const char* varname, char* buffer, int len);
int GetServerVarAsInt(const char* varname);
int GetServerVarAsBool(const char* varname);
int GetPlayerNetworkStats(int playerid, char* retstr, int retstr_size);
int GetNetworkStats(char* retstr, int retstr_size);

// Menu
int CreateMenu(const char* title, int columns, float x, float y, float col1width, float col2width = 0.0);
int DestroyMenu(int menuid);
int AddMenuItem(int menuid, int column, const char* menutext);
int SetMenuColumnHeader(int menuid, int column, const char* columnheader);
int ShowMenuForPlayer(int menuid, int playerid);
int HideMenuForPlayer(int menuid, int playerid);
int IsValidMenu(int menuid);
int DisableMenu(int menuid);
int DisableMenuRow(int menuid, int row);
int GetPlayerMenu(int playerid);

// Text Draw
int TextDrawCreate(float x, float y, const char* text);
int TextDrawDestroy(int text);
int TextDrawLetterSize(int text, float x, float y);
int TextDrawTextSize(int text, float x, float y);
int TextDrawAlignment(int text, int alignment);
int TextDrawColor(int text, int color);
int TextDrawUseBox(int text, int use);
int TextDrawBoxColor(int text, int color);
int TextDrawSetShadow(int text, int size);
int TextDrawSetOutline(int text, int size);
int TextDrawBackgroundColor(int text, int color);
int TextDrawFont(int text, int font);
int TextDrawSetProportional(int text, int set);
int TextDrawShowForPlayer(int playerid, int text);
int TextDrawHideForPlayer(int playerid, int text);
int TextDrawShowForAll(int text);
int TextDrawHideForAll(int text);
int TextDrawSetString(int text, const char* string);

// Gang Zones
int GangZoneCreate(float minx, float miny, float maxx, float maxy);
int GangZoneDestroy(int zone);
int GangZoneShowForPlayer(int playerid, int zone, int color);
int GangZoneShowForAll(int zone, int color);
int GangZoneHideForPlayer(int playerid, int zone);
int GangZoneHideForAll(int zone);
int GangZoneFlashForPlayer(int playerid, int zone, int flashcolor);
int GangZoneFlashForAll(int zone, int flashcolor);
int GangZoneStopFlashForPlayer(int playerid, int zone);
int GangZoneStopFlashForAll(int zone);

// Global 3D Text Labels
int Create3DTextLabel(const char* text, int color, float X, float Y, float Z, float DrawDistance, int virtualworld, int testLOS=0);
int Delete3DTextLabel(int id);
int Attach3DTextLabelToPlayer(int id, int playerid, float OffsetX, float OffsetY, float OffsetZ);
int Attach3DTextLabelToVehicle(int id, int vehicleid, float OffsetX, float OffsetY, float OffsetZ);
int Update3DTextLabelText(int id, int color, const char* text);

// Per-player 3D Text Labels
int CreatePlayer3DTextLabel(int playerid, const char* text, int color, float X, float Y, float Z, float DrawDistance, int attachedplayer=INVALID_PLAYER_ID, int attachedvehicle=INVALID_VEHICLE_ID, int testLOS=0);
int DeletePlayer3DTextLabel(int playerid, int id);
int UpdatePlayer3DTextLabelText(int playerid, int id, int color, const char* text);

// Player GUI Dialog
int ShowPlayerDialog( int playerid, int dialogid, int style, const char* caption, const char* info, const char* button1, const char* button2 );


//----------------------------------------------------------
// a_players.inc
int SetSpawnInfo(int playerid, int team, int skin, float x, float y, float z, float rotation, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo);
int SpawnPlayer(int playerid);

// Player info
int SetPlayerPos(int playerid, float x, float y, float z);
int SetPlayerPosFindZ(int playerid, float x, float y, float z);
int GetPlayerPos(int playerid, float& x, float& y, float& z);
int SetPlayerFacingAngle(int playerid, float ang);
int GetPlayerFacingAngle(int playerid, float& ang);
int IsPlayerInRangeOfPoint(int playerid, float range, float x, float y, float z);
int GetPlayerDistanceFromPoint(int playerid, float x, float y, float z); //0.3c r3
int IsPlayerStreamedIn(int playerid, int forplayerid);
int SetPlayerInterior(int playerid, int interiorid);
int GetPlayerInterior(int playerid);
int SetPlayerHealth(int playerid, float health);
int GetPlayerHealth(int playerid, float &health);
int SetPlayerArmour(int playerid, float armour);
int GetPlayerArmour(int playerid, float &armour);
int SetPlayerAmmo(int playerid, int weaponslot, int ammo);
int GetPlayerAmmo(int playerid);
int GetPlayerWeaponState(int playerid);
int SetPlayerTeam(int playerid, int teamid);
int GetPlayerTeam(int playerid);
int SetPlayerScore(int playerid, int score);
int GetPlayerScore(int playerid);
int GetPlayerDrunkLevel(int playerid);
int SetPlayerDrunkLevel(int playerid, int level);
int SetPlayerColor(int playerid, int color);
int GetPlayerColor(int playerid);
int SetPlayerSkin(int playerid, int skinid);
int GetPlayerSkin(int playerid);
int GivePlayerWeapon(int playerid, int weaponid, int ammo);
int ResetPlayerWeapons(int playerid);
int SetPlayerArmedWeapon(int playerid, int weaponid);
int GetPlayerWeaponData(int playerid, int slot, int &weapons, int &ammo);
int GivePlayerMoney(int playerid, int money);
int ResetPlayerMoney(int playerid);
int SetPlayerName(int playerid, const char* name);
int GetPlayerMoney(int playerid);
int GetPlayerState(int playerid);
int GetPlayerIp(int playerid, char* ip, int len);
int GetPlayerPing(int playerid);
int GetPlayerWeapon(int playerid);
int GetPlayerKeys(int playerid, int &keys, int &updown, int &leftright);
int GetPlayerName(int playerid, char* name, int len);
int SetPlayerTime(int playerid, int hour, int minute);
int GetPlayerTime(int playerid, int &hour, int &minute);
int TogglePlayerClock(int playerid, int toggle);
int SetPlayerWeather(int playerid, int weather);
int ForceClassSelection(int playerid);
int SetPlayerWantedLevel(int playerid, int level);
int GetPlayerWantedLevel(int playerid);
int SetPlayerFightingStyle(int playerid, int style);
int GetPlayerFightingStyle(int playerid);
int SetPlayerVelocity(int playerid, float X, float Y, float Z);
int GetPlayerVelocity(int playerid, float &X, float &Y, float &Z );
int PlayCrimeReportForPlayer(int playerid, int suspectid, int crime);
int SetPlayerShopName(int playerid, const char* shopname);
int SetPlayerSkillLevel(int playerid, int skill, int level);
int GetPlayerSurfingVehicleID(int playerid);
int GetPlayerSurfingObjectID(int playerid); //0.3c r3

int SetPlayerAttachedObject(int playerid, int index, int modelid, int bone, float fOffsetX = 0.0, float fOffsetY = 0.0, float fOffsetZ = 0.0, float fRotX = 0.0, float fRotY = 0.0, float fRotZ = 0.0, float fScaleX = 1.0, float fScaleY = 1.0, float fScaleZ = 1.0);
int RemovePlayerAttachedObject(int playerid, int index);
int IsPlayerAttachedObjectSlotUsed(int playerid, int index);

int SetPlayerChatBubble(int playerid, const char* text, int color, float drawdistance, int expiretime);

// Player controls
int PutPlayerInVehicle(int playerid, int vehicleid, int seatid);
int GetPlayerVehicleID(int playerid);
int GetPlayerVehicleSeat(int playerid);
int RemovePlayerFromVehicle(int playerid);
int TogglePlayerControllable(int playerid, int toggle);
int PlayerPlaySound(int playerid, int soundid, float x, float y, float z);
int ApplyAnimation(int playerid, const char* animlib, const char* animname, float fDelta, int loop, int lockx, int locky, int freeze, int time, int forcesync);
int ClearAnimations(int playerid, int forcesync = 0);
int GetPlayerAnimationIndex(int playerid);
int GetAnimationName(int index, char* animlib, int len1, char* animname, int len2);
int GetPlayerSpecialAction(int playerid);
int SetPlayerSpecialAction(int playerid, int actionid);

// Player world/map related
int SetPlayerCheckpoint(int playerid, float x, float y, float z, float size);
int DisablePlayerCheckpoint(int playerid);
int SetPlayerRaceCheckpoint(int playerid, int type, float x, float y, float z, float nextx, float nexty, float nextz, float size);
int DisablePlayerRaceCheckpoint(int playerid);
int SetPlayerWorldBounds(int playerid, float x_max, float x_min, float y_max, float y_min);
int SetPlayerMarkerForPlayer(int playerid, int showplayerid, int color);
int ShowPlayerNameTagForPlayer(int playerid, int showplayerid, int show);

int SetPlayerMapIcon(int playerid, int iconid, float x, float y, float z, int markertype, int color, int style);
int RemovePlayerMapIcon(int playerid, int iconid);
int AllowPlayerTeleport(int playerid, int allow);

// Player camera
int SetPlayerCameraPos(int playerid,float x, float y, float z);
int SetPlayerCameraLookAt(int playerid, float x, float y, float z);
int SetCameraBehindPlayer(int playerid);
int GetPlayerCameraPos(int playerid, float &x, float &y, float &z);
int GetPlayerCameraFrontVector(int playerid, float &x, float &y, float &z);
int GetPlayerCameraMode(int playerid); //0.3c r3

// Player conditionals
int IsPlayerConnected(int playerid);
int IsPlayerInVehicle(int playerid, int vehicleid);
int IsPlayerInAnyVehicle(int playerid);
int IsPlayerInCheckpoint(int playerid);
int IsPlayerInRaceCheckpoint(int playerid);

// Virtual Worlds
int SetPlayerVirtualWorld(int playerid, int worldid);
int GetPlayerVirtualWorld(int playerid);

// Insane Stunts
int EnableStuntBonusForPlayer(int playerid, int enable);
int EnableStuntBonusForAll(int enable);

// Spectating
int TogglePlayerSpectating(int playerid, int toggle);
int PlayerSpectatePlayer(int playerid, int targetplayerid, int mode = SPECTATE_MODE_NORMAL);
int PlayerSpectateVehicle(int playerid, int targetvehicleid, int mode = SPECTATE_MODE_NORMAL);

// Npc Record
int StartRecordingPlayerData(int playerid, int recordtype, const char* recordname);
int StopRecordingPlayerData(int playerid);


//----------------------------------------------------------
// a_vehicles.inc
int CreateVehicle(int vehicletype, float x, float y, float z, float rotation, int color1, int color2, int respawn_delay);
int DestroyVehicle(int vehicleid);
int IsVehicleStreamedIn(int vehicleid, int forplayerid);
int GetVehiclePos(int vehicleid, float &x, float &y, float &z);
int SetVehiclePos(int vehicleid, float x, float y, float z);
int GetVehicleZAngle(int vehicleid, float &z_angle);
int GetVehicleRotationQuat(int vehicleid, float &w, float &x, float &y, float &z);
int GetVehicleDistanceFromPoint(int vehicleid, float x, float y, float z); //0.3c r3
int SetVehicleZAngle(int vehicleid, float z_angle);
int SetVehicleParamsForPlayer(int vehicleid, int playerid, int objective, int doorslocked);
int ManualVehicleEngineAndLights();
int SetVehicleParamsEx(int vehicleid, int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective);
int GetVehicleParamsEx(int vehicleid, int &engine, int &lights, int &alarm, int &doors, int &bonnet, int &boot, int &objective);
int SetVehicleToRespawn(int vehicleid);
int LinkVehicleToInterior(int vehicleid, int interiorid);
int AddVehicleComponent(int vehicleid, int componentid);
int RemoveVehicleComponent(int vehicleid, int componentid);
int ChangeVehicleColor(int vehicleid, int color1, int color2);
int ChangeVehiclePaintjob(int vehicleid, int paintjobid);
int SetVehicleHealth(int vehicleid, float health);
int GetVehicleHealth(int vehicleid, float &health);
int AttachTrailerToVehicle(int trailerid, int vehicleid);
int DetachTrailerFromVehicle(int vehicleid);
int IsTrailerAttachedToVehicle(int vehicleid);
int GetVehicleTrailer(int vehicleid);
int SetVehicleNumberPlate(int vehicleid, const char* numberplate);
int GetVehicleModel(int vehicleid);
int GetVehicleComponentInSlot(int vehicleid, int slot);
int GetVehicleComponentType(int component);
int RepairVehicle(int vehicleid);
int GetVehicleVelocity(int vehicleid, float& X, float& Y, float& Z);
int SetVehicleVelocity(int vehicleid, float X, float Y, float Z);
int SetVehicleAngularVelocity(int vehicleid, float X, float Y, float Z);
int GetVehicleDamageStatus(int vehicleid, int &panels, int &doors, int &lights, int &tires);
int UpdateVehicleDamageStatus(int vehicleid, int panels, int doors, int lights,int tires);

// Virtual Worlds
int SetVehicleVirtualWorld(int vehicleid, int worldid);
int GetVehicleVirtualWorld(int vehicleid);


//----------------------------------------------------------
// a_objects.inc
int CreateObject(int modelid, float X, float Y, float Z, float rX, float rY, float rZ, float drawDistance);
int AttachObjectToVehicle(int objectid, int vehicleid, float OffsetX, float OffsetY, float OffsetZ, float RotX, float RotY, float RotZ);
int SetObjectPos(int objectid, float X, float Y, float Z);
int GetObjectPos(int objectid, float &X, float &Y, float &Z);
int SetObjectRot(int objectid, float RotX, float RotY, float RotZ);
int GetObjectRot(int objectid, float &RotX, float &RotY, float &RotZ);
int IsValidObject(int objectid);
int DestroyObject(int objectid);
int MoveObject(int objectid, float X, float Y, float Z, float Speed);
int StopObject(int objectid);
int CreatePlayerObject(int playerid, int modelid, float X, float Y, float Z, float rX, float rY, float rZ, float drawDistance);
int SetPlayerObjectPos(int playerid, int objectid, float X, float Y, float Z);
int GetPlayerObjectPos(int playerid, int objectid, float &X, float &Y, float &Z);
int SetPlayerObjectRot(int playerid, int objectid, float RotX, float RotY, float RotZ);
int GetPlayerObjectRot(int playerid, int objectid, float &RotX, float &RotY, float &RotZ);
int IsValidPlayerObject(int playerid, int objectid);
int DestroyPlayerObject(int playerid, int objectid);
int MovePlayerObject(int playerid, int objectid, float X, float Y, float Z, float Speed);
int StopPlayerObject(int playerid, int objectid);
int AttachObjectToPlayer(int objectid, int playerid, float OffsetX, float OffsetY, float OffsetZ, float rX, float rY, float rZ);
int AttachPlayerObjectToPlayer(int objectplayer, int objectid, int attachplayer, float OffsetX, float OffsetY, float OffsetZ, float rX, float rY, float rZ);
