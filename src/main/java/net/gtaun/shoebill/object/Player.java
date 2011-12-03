/**
 * Copyright (C) 2011 MK124
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill.object;

import java.util.Collection;

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.data.Area;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.KeyState;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.LocationAngular;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.data.Time;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.data.Velocity;
import net.gtaun.shoebill.data.type.FightStyle;
import net.gtaun.shoebill.data.type.MapIconStyle;
import net.gtaun.shoebill.data.type.PlayerState;
import net.gtaun.shoebill.data.type.RaceCheckpointType;
import net.gtaun.shoebill.data.type.RecordType;
import net.gtaun.shoebill.data.type.ShopName;
import net.gtaun.shoebill.data.type.SpecialAction;
import net.gtaun.shoebill.data.type.SpectateMode;
import net.gtaun.shoebill.data.type.WeaponState;
import net.gtaun.shoebill.data.type.WeaponType;
import net.gtaun.shoebill.event.dialog.DialogCancelEvent;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.exception.IllegalLengthException;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Player implements IPlayer
{
	public static final int INVALID_ID =							0xFFFF;
	public static final int PLAYER_NO_TEAM =						255;
	public static final int MAX_NAME_LENGTH =						24;
	
	public static final int MAX_CHATBUBBLE_LENGTH =					144;
	
	
	public static Collection<IPlayer> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayers();
	}
	
	public static <T extends IPlayer> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayers( cls );
	}
	
	public static <T extends IPlayer> T get( Class<T> cls, int id )
	{
		return cls.cast( Shoebill.getInstance().getManagedObjectPool().getPlayer(id) );
	}
	
	public static int getMaxPlayers()
	{
		return SampNativeFunction.getMaxPlayers();
	}
	
	public static void enableStuntBonusForAll( boolean enabled )
	{
		for( IPlayer player : get() ) player.enableStuntBonus( enabled );
	}

	public static void sendMessageToAll( Color color, String message )
	{
		for( IPlayer player : get() ) player.sendMessage( color, message );
	}
	
	public static void sendMessageToAll( Color color, String format, Object... args )
	{
		for( IPlayer player : get() )
		{
			String message = String.format(format, args);
			player.sendMessage( color, message );
		}
	}

	public static void gameTextToAll( int time, int style, String text )
	{
		SampNativeFunction.gameTextForAll( text, time, style );
	}
	
	public static void gameTextToAll( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		SampNativeFunction.gameTextForAll( text, time, style );
	}
	
	
	private EventDispatcher eventDispatcher = new EventDispatcher();
	
	private int id = -1;
	private String ip;
	private String name;
	private SpawnInfo spawnInfo = new SpawnInfo();
	private Color color;
	
	private boolean controllable = true;
	private boolean isStuntBonusEnabled = false;
	private boolean spectating = false;
	private boolean isRecording = false;
	
	private IPlayer spectatingPlayer;
	private IVehicle spectatingVehicle;

	private int updateTick = -1;
	private float health, armour;
	private int money, score;
	private int weather;

	private LocationAngular position = new LocationAngular();
	private Area worldBound = new Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f);
	private Velocity velocity = new Velocity();
	private KeyState keyState = new KeyState();
	private IPlayerAttach playerAttach;
	private IPlayerWeaponSkill skill;
	private ICheckpoint checkpoint;
	private IRaceCheckpoint raceCheckpoint;
	
	private IDialog dialog;
	

	@Override public IEventDispatcher getEventDispatcher()			{ return eventDispatcher; }
	
	@Override public int getId()									{ return id; }
	@Override public int getPing()									{ return SampNativeFunction.getPlayerPing(id); }
	@Override public int getTeam()									{ return SampNativeFunction.getPlayerTeam(id); }
	@Override public int getSkin()									{ return SampNativeFunction.getPlayerSkin(id); }
	@Override public int getWantedLevel()							{ return SampNativeFunction.getPlayerWantedLevel(id); }
	@Override public int getCodepage()								{ return SampNativeFunction.getPlayerCodepage(id); };
	@Override public String getIp()									{ return ip; }
	@Override public String getName()								{ return name; }
	@Override public SpawnInfo getSpawnInfo()						{ return spawnInfo.clone(); }
	@Override public Color getColor()								{ return color; }

	@Override public int getUpdateTick()							{ return updateTick; }
	@Override public float getHealth()								{ return health; }
	@Override public float getArmour()								{ return armour; }
	@Override public WeaponType getArmedWeapon()					{ return WeaponType.get( SampNativeFunction.getPlayerWeapon(id) ); }
	@Override public int getArmedWeaponAmmo()						{ return SampNativeFunction.getPlayerAmmo(id); }
	@Override public int getMoney()									{ return money; }
	@Override public int getScore()									{ return score; }
	@Override public int getWeather()								{ return weather; }
	@Override public int getCameraMode()							{ return SampNativeFunction.getPlayerCameraMode(id); }
	@Override public FightStyle getFightStyle()						{ return FightStyle.get(SampNativeFunction.getPlayerFightingStyle(id)); }
	@Override public IVehicle getVehicle()							{ return Vehicle.get(SampNativeFunction.getPlayerVehicleID(id)); }
	@Override public int getVehicleSeat()							{ return SampNativeFunction.getPlayerVehicleSeat(id); }
	@Override public SpecialAction getSpecialAction()				{ return SpecialAction.get(SampNativeFunction.getPlayerSpecialAction(id)); }
	@Override public IPlayer getSpectatingPlayer()					{ return spectatingPlayer; }
	@Override public IVehicle getSpectatingVehicle()				{ return spectatingVehicle; }
	
	@Override public LocationAngular getPosition()						{ return position.clone(); }
	@Override public Area getWorldBound()							{ return worldBound.clone(); }
	@Override public Velocity getVelocity()							{ return velocity.clone(); }
	@Override public PlayerState getState()							{ return PlayerState.values()[ SampNativeFunction.getPlayerState(id) ]; }
	@Override public KeyState getKeyState()							{ return keyState.clone(); }
	@Override public IPlayerAttach getPlayerAttach()				{ return playerAttach; }
	@Override public IPlayerWeaponSkill getWeaponSkill()					{ return skill; }
	@Override public ICheckpoint getCheckpoint()					{ return checkpoint; }
	@Override public IRaceCheckpoint getRaceCheckpoint()			{ return raceCheckpoint; }
	
	@Override public IDialog getDialog()							{ return dialog; }

	@Override public boolean isOnline()								{ return id == -1; }
	@Override public boolean isStuntBonusEnabled()					{ return isStuntBonusEnabled; }
	@Override public boolean isSpectating()							{ return spectating; }
	@Override public boolean isRecording()							{ return isRecording; }
	@Override public boolean isControllable()						{ return controllable; }
	
	
	protected Player( int id )
	{	
		this.id = id;
		ip = SampNativeFunction.getPlayerIp(id);
		name = SampNativeFunction.getPlayerName(id);
		color = new Color( SampNativeFunction.getPlayerColor(id) );
		
		health = SampNativeFunction.getPlayerHealth(id);
		armour = SampNativeFunction.getPlayerArmour(id);
		money = SampNativeFunction.getPlayerMoney(id);
		score = SampNativeFunction.getPlayerScore(id);
		
		SampNativeFunction.getPlayerPos(id, position);
		SampNativeFunction.getPlayerFacingAngle(id);
		
		position.interior = SampNativeFunction.getPlayerInterior(id);
		position.world = SampNativeFunction.getPlayerVirtualWorld(id);
		
		SampNativeFunction.getPlayerVelocity(id, velocity);
		
		SampNativeFunction.getPlayerKeys(id, keyState);
		
		playerAttach = new PlayerAttach(id);
		skill = new PlayerWeaponSkill(id);

		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		if( pool.getPlayer(id) != null ) throw new UnsupportedOperationException();
		pool.setPlayer( id, this );
	}

	
	@Override
	public void setCodepage( int codepage )
	{
		SampNativeFunction.setPlayerCodepage( id, codepage );
	}
	
	@Override
	public void setName( String name ) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException
	{
		if( name == null ) throw new IllegalArgumentException();
		if( name.length()<3 || name.length()>20 ) throw new IllegalLengthException();
		
		int ret = SampNativeFunction.setPlayerName(id, name);
		if( ret == 0 )	throw new AlreadyExistException();
		if( ret == -1 )	throw new IllegalArgumentException();
		
		this.name = name;
	}
	
	@Override
	public void setSpawnInfo( float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, WeaponType weapon1, int ammo1, WeaponType weapon2, int ammo2, WeaponType weapon3, int ammo3 )
	{
		SpawnInfo info = new SpawnInfo(x, y, z, interiorId, worldId, angle, skin, team, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3);
		setSpawnInfo( info );
	}
	
	@Override
	public void setSpawnInfo( SpawnInfo info )
	{
		SampNativeFunction.setSpawnInfo( id, info.team, info.skin, info.position.x, info.position.y, info.position.z, info.position.angle, info.weapon1.type.getId(), info.weapon1.ammo, info.weapon2.type.getId(), info.weapon2.ammo, info.weapon3.type.getId(), info.weapon3.ammo );
		spawnInfo = info;
	}
	
	@Override
	public void setColor( Color color )
	{
		this.color = color.clone();
		SampNativeFunction.setPlayerColor( id, color.getValue() );
	}

	@Override
	public void setHealth( float health )
	{
		SampNativeFunction.setPlayerHealth( id, health );
		this.health = health;
	}
	
	@Override
	public void setArmour( float armour)
	{
		SampNativeFunction.setPlayerArmour( id, armour );
		this.armour = armour;
	}
	
	@Override
	public void setWeaponAmmo( int weaponSlot, int ammo )
	{
		SampNativeFunction.setPlayerAmmo( id, weaponSlot, ammo );
	}
	
	@Override
	public void setMoney( int money )
	{
		SampNativeFunction.resetPlayerMoney( id );
		if( money != 0 ) SampNativeFunction.givePlayerMoney( id, money );
		
		this.money = money;
	}
	
	@Override
	public void giveMoney( int money )
	{
		SampNativeFunction.givePlayerMoney( id, money );
		this.money = SampNativeFunction.getPlayerMoney(id);
	}
	
	@Override
	public void setScore( int score )
	{
		SampNativeFunction.setPlayerScore( id, score );
		this.score = score;
	}
	
	@Override
	public void setWeather( int weather )
	{
		SampNativeFunction.setPlayerWeather( id, weather );
		this.weather = weather;
	}
	
	@Override
	public void setFightStyle( FightStyle style )
	{
		SampNativeFunction.setPlayerFightingStyle( id, style.getData() );
	}

	@Override
	public void setVehicle( IVehicle vehicle, int seat )
	{
		if( vehicle == null )
		{
			removeFromVehicle();
			return;
		}
		
		vehicle.putPlayer( this, seat );
	}
	
	@Override
	public void setVehicle( IVehicle vehicle )
	{
		setVehicle( vehicle, 0 );
	}

	@Override
	public void setPosition( float x, float y, float z )
	{
		SampNativeFunction.setPlayerPos( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	@Override
	public void setPosition( Location position )
	{
		SampNativeFunction.setPlayerPos( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}

	@Override
	public void setPosition( LocationAngular position )
	{
		SampNativeFunction.setPlayerPos( id, position.x, position.y, position.z );
		SampNativeFunction.setPlayerFacingAngle( id, position.angle );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	@Override
	public void setPositionFindZ( float x, float y, float z )
	{
		SampNativeFunction.setPlayerPosFindZ( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	@Override
	public void setPositionFindZ( Location position )
	{
		SampNativeFunction.setPlayerPosFindZ( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	@Override
	public void setPositionFindZ( LocationAngular position )
	{
		SampNativeFunction.setPlayerPosFindZ( id, position.x, position.y, position.z );
		SampNativeFunction.setPlayerFacingAngle( id, position.angle );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	@Override
	public void setAngle( float angle )
	{
		SampNativeFunction.setPlayerFacingAngle( id, angle );
		position.angle = angle;
	}
	
	@Override
	public void setInterior( int interiorId )
	{
		SampNativeFunction.setPlayerInterior( id, interiorId );
		position.interior = interiorId;
	}
	
	@Override
	public void setVirtualWorld( int worldId )
	{
		SampNativeFunction.setPlayerVirtualWorld( id, worldId );
		position.world = worldId;
	}
	
	@Override
	public void setWorldBound( Area bound )
	{
		SampNativeFunction.setPlayerWorldBounds( id, bound.maxX, bound.minX, bound.maxY, bound.minY );
		worldBound.set( bound );
	}
	
	@Override
	public void setSpeed( Velocity speed )
	{
		SampNativeFunction.setPlayerVelocity( id, speed.x, speed.y, speed.z );
		velocity.set( speed );
	}

	@Override
	public void sendMessage( Color color, String message )
	{
		if( message == null ) throw new NullPointerException();
		SampNativeFunction.sendClientMessage( id, color.getValue(), message );
	}
	
	@Override
	public void sendMessage( Color color, String format, Object... args )
	{
		String message = String.format(format, args);
		SampNativeFunction.sendClientMessage( id, color.getValue(), message );
	}
	
	@Override
	public void sendChat( IPlayer player, String message )
	{
		if( message == null ) throw new NullPointerException();
		SampNativeFunction.sendPlayerMessageToPlayer( player.getId(), id, message );
	}
	
	@Override
	public void sendChatToAll( String message )
	{
		if( message == null ) throw new NullPointerException();
		for( IPlayer player : get() ) sendChat( player, message );
	}

	@Override
	public void sendDeathMessage( IPlayer killer, int reason )
	{
		if( killer == null )
			SampNativeFunction.sendDeathMessage( INVALID_ID, id, reason );
		else
			SampNativeFunction.sendDeathMessage( killer.getId(), id, reason );
	}

	@Override
	public void sendGameText( int time, int style, String text )
	{
		if( text == null ) throw new NullPointerException();
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}
	
	@Override
	public void sendGameText( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}

	@Override
	public void spawn()
	{
		SampNativeFunction.spawnPlayer( id );
	}
	
	@Override
	public void setDrunkLevel( int level )
	{
		SampNativeFunction.setPlayerDrunkLevel( id, level );
	}
	
	@Override
	public int getDrunkLevel()
	{
		return SampNativeFunction.getPlayerDrunkLevel(id);
	}

	@Override
	public void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync )
	{
		if( animlib == null || animname == null ) throw new NullPointerException();
		SampNativeFunction.applyAnimation( id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync );
	}
	
	@Override
	public void clearAnimations( int forcesync )
	{
		SampNativeFunction.clearAnimations( id, forcesync );
	}
	
	@Override
	public int getAnimationIndex()
	{
		return SampNativeFunction.getPlayerAnimationIndex(id);
	}

	@Override
	public void playSound( int sound, float x, float y, float z )
	{
		SampNativeFunction.playerPlaySound( id, sound, x, y, z );
	}
	
	@Override
	public void playSound( int sound, Location point )
	{
		SampNativeFunction.playerPlaySound( id, sound, point.x, point.y, point.z );
	}
	
	@Override
	public void markerForPlayer( IPlayer player, Color color )
	{
		SampNativeFunction.setPlayerMarkerForPlayer( id, player.getId(), color.getValue() );
	}
	
	@Override
	public void showNameTagForPlayer( IPlayer player, boolean show )
	{
		SampNativeFunction.showPlayerNameTagForPlayer( id, player.getId(), show );
	}

	@Override
	public void kick()
	{
		SampNativeFunction.kick( id );
	}
	
	@Override
	public void ban()
	{
		SampNativeFunction.ban( id );
	}
	
	@Override
	public void ban( String reason )
	{
		if( reason == null ) throw new NullPointerException();
		SampNativeFunction.banEx( id, reason );
	}
	
	@Override
	public IMenu getMenu()
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenu( SampNativeFunction.getPlayerMenu(id) );
	}

	@Override
	public void setCameraPosition( float x, float y, float z )
	{
		SampNativeFunction.setPlayerCameraPos( id, x, y, z );
	}
	
	@Override
	public void setCameraPosition( Location pos )
	{
		if( pos == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerCameraPos( id, pos.x, pos.y, pos.z );
	}

	@Override
	public void setCameraLookAt( float x, float y, float z )
	{
		SampNativeFunction.setPlayerCameraLookAt(id, x, y, z);
	}
	
	@Override
	public void setCameraLookAt( Location lookat )
	{
		if( lookat == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerCameraLookAt(id, lookat.x, lookat.y, lookat.z);
	}
	
	@Override
	public void setCameraBehind()
	{
		SampNativeFunction.setCameraBehindPlayer(id);
	}
	
	@Override
	public Location getCameraPosition()
	{
		Location pos = new Location();
		SampNativeFunction.getPlayerCameraPos( id, pos );
		return pos;
	}
	
	@Override
	public Location getCameraFrontVector()
	{
		Location lookat = new Location();
		SampNativeFunction.getPlayerCameraFrontVector( id, lookat );
		return lookat;
	}
	
	@Override
	public boolean isInAnyVehicle()
	{
		return SampNativeFunction.isPlayerInAnyVehicle( id );
	}
	
	@Override
	public boolean isInVehicle( IVehicle vehicle )
	{
		return SampNativeFunction.isPlayerInVehicle( id, vehicle.getId() );
	}
	
	@Override
	public boolean isAdmin()
	{
		return SampNativeFunction.isPlayerAdmin(id);
	}
	
	@Override
	public boolean isStreamedIn( IPlayer forPlayer )
	{
		return SampNativeFunction.isPlayerStreamedIn(id, forPlayer.getId());
	}
	
	@Override
	public void setCheckpoint( ICheckpoint checkpoint )
	{
		if( checkpoint == null )
		{
			disableCheckpoint();
			return;
		}

		Vector3D position = checkpoint.getPosition();
		SampNativeFunction.setPlayerCheckpoint( id, position.x, position.y, position.z, checkpoint.getSize() );
		this.checkpoint = checkpoint;
	}
	
	@Override
	public void disableCheckpoint()
	{
		SampNativeFunction.disablePlayerCheckpoint( id );
		checkpoint = null;
	}
	
	@Override
	public void setRaceCheckpoint( IRaceCheckpoint checkpoint )
	{
		if( checkpoint == null )
		{
			disableRaceCheckpoint();
			return;
		}
		
		IRaceCheckpoint next = checkpoint.getNext();
		
		Vector3D position = checkpoint.getPosition();
		Vector3D nextPosition = next.getPosition();
		
		if( checkpoint.getNext() != null )
		{
			SampNativeFunction.setPlayerRaceCheckpoint( id, checkpoint.getType().getData(), position.x, position.y, position.z, nextPosition.x, nextPosition.y, nextPosition.z, checkpoint.getSize() );
		}
		else
		{
			RaceCheckpointType type = checkpoint.getType();
			
			if( type == RaceCheckpointType.NORMAL )			type = RaceCheckpointType.NORMAL_FINISH;
			else if( type == RaceCheckpointType.AIR )		type = RaceCheckpointType.AIR_FINISH;
			
			SampNativeFunction.setPlayerRaceCheckpoint( id, type.getData(), position.x, position.y, position.z, position.x, position.y, position.z, checkpoint.getSize() );
		}
		
		raceCheckpoint = checkpoint;
	}
	
	@Override
	public void disableRaceCheckpoint()
	{
		SampNativeFunction.disablePlayerRaceCheckpoint( id );
		raceCheckpoint = null;
	}
	
	@Override
	public WeaponState getWeaponState()
	{
		return WeaponState.get( SampNativeFunction.getPlayerWeaponState(id) );
	}
	
	@Override
	public void setTeam( int team )
	{
		SampNativeFunction.setPlayerTeam( id, team );
	}
	
	@Override
	public void setSkin( int skin )
	{
		SampNativeFunction.setPlayerSkin( id, skin );
	}
	
	@Override
	public void giveWeapon( WeaponType type, int ammo )
	{
		SampNativeFunction.givePlayerWeapon( id, type.getId(), ammo );
	}
	
	@Override
	public void resetWeapons()
	{
		SampNativeFunction.resetPlayerWeapons( id );
	}
	
	@Override
	public void setTime( int hour, int minute )
	{
		SampNativeFunction.setPlayerTime( id, hour, minute );
	}
	
	@Override
	public Time getTime()
	{
		Time time = new Time();
		SampNativeFunction.getPlayerTime( id, time );
		return time;
	}
	
	@Override
	public void toggleClock( boolean toggle )
	{
		SampNativeFunction.togglePlayerClock( id, toggle );
	}
	
	@Override
	public void forceClassSelection()
	{
		SampNativeFunction.forceClassSelection( id );
	}
	
	@Override
	public void setWantedLevel( int level )
	{
		SampNativeFunction.setPlayerWantedLevel( id, level );
	}
	
	@Override
	public void playCrimeReport( int suspectId, int crimeId )
	{
		SampNativeFunction.playCrimeReportForPlayer( id, suspectId, crimeId );
	}
	
	@Override
	public void setShopName( ShopName shop )
	{
		if( name == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerShopName(id, shop.getData());
	}
	
	@Override
	public IVehicle getSurfingVehicle()
	{
		return Vehicle.get(IVehicle.class, SampNativeFunction.getPlayerSurfingVehicleID(id));
	}
	
	@Override
	public void removeFromVehicle()
	{
		SampNativeFunction.removePlayerFromVehicle(id);
	}
	
	@Override
	public void toggleControllable( boolean toggle )
	{
		SampNativeFunction.togglePlayerControllable( id, toggle );
		controllable = toggle;
	}
	
	@Override
	public void setSpecialAction( SpecialAction action )
	{
		SampNativeFunction.setPlayerSpecialAction( id, action.getData() );
	}
	
	@Override
	public void setMapIcon( int iconId, Location point, int markerType, Color color, MapIconStyle style )
	{
		SampNativeFunction.setPlayerMapIcon( id, iconId, point.x, point.y, point.z, markerType, color.getValue(), style.getData() );
	}
	
	@Override
	public void removeMapIcon( int iconid )
	{
		SampNativeFunction.removePlayerMapIcon( id, iconid );
	}
	
	@Override
	public void enableStuntBonus( boolean enable )
	{
		if( enable )	SampNativeFunction.enableStuntBonusForPlayer( id, 1 );
		else			SampNativeFunction.enableStuntBonusForPlayer( id, 0 );
		
		isStuntBonusEnabled = enable;
	}
	
	@Override
	public void toggleSpectating( boolean toggle )
	{
		SampNativeFunction.togglePlayerSpectating( id, toggle );
		spectating = toggle;
		
		if( toggle ) return;
		
		spectatingPlayer = null;
		spectatingVehicle = null;
	}
	
	@Override
	public void spectate( IPlayer player, SpectateMode mode )
	{
		if( !spectating ) return;
		
		SampNativeFunction.playerSpectatePlayer(id, player.getId(), mode.getData());
		spectatingPlayer = player;
		spectatingVehicle = null;
	}
	
	@Override
	public void spectate( IVehicle vehicle, SpectateMode mode )
	{
		if( !spectating ) return;

		SampNativeFunction.playerSpectateVehicle(id, vehicle.getId(), mode.getData());
		spectatingPlayer = null;
		spectatingVehicle = vehicle;
	}
	
	@Override
	public void startRecord( RecordType type, String recordName )
	{
		SampNativeFunction.startRecordingPlayerData( id, type.getData(), recordName );
		isRecording = true;
	}
	
	@Override
	public void stopRecord()
	{
		SampNativeFunction.stopRecordingPlayerData( id );
		isRecording = false;
	}
	
	@Override
	public IObject getSurfingObject()
	{
		int objectid = SampNativeFunction.getPlayerSurfingObjectID(id);
		if( objectid == ObjectBase.INVALID_ID ) return null;
		
		return Shoebill.getInstance().getManagedObjectPool().getObject( objectid );
	}
	
	@Override
	public String getNetworkStats()
	{
		return SampNativeFunction.getPlayerNetworkStats(id);
	}
	
	
	@Override
	public IPlayer getAimedTarget()
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayer( SampNativeFunction.getPlayerTargetPlayer(id) );
	}

	@Override
	public void playAudioStream( String url )
	{
		SampNativeFunction.playAudioStreamForPlayer( id, url, 0.0f, 0.0f, 0.0f, 0.0f, 0 );
	}
	
	@Override
	public void playAudioStream( String url, Vector3D position, float distance )
	{
		SampNativeFunction.playAudioStreamForPlayer( id, url, position.x, position.y, position.z, distance, 1 );
	}
	
	@Override
	public void stopAudioStream()
	{
		SampNativeFunction.stopAudioStreamForPlayer( id );
	}
	
	@Override
	public void removeBuilding( int modelId, float x, float y, float z, float radius )
	{
		SampNativeFunction.removeBuildingForPlayer( id, modelId, x, y, z, radius );
	}
	
	
	@Override
	public void showDialog( IDialog dialog, String caption, String text, String button1, String button2 )
	{
		if( caption == null || text == null || button1 == null || button2 == null ) throw new NullPointerException();
		cancelDialog();
		
		SampNativeFunction.showPlayerDialog( id, dialog.getId(), dialog.getStyle().getData(), caption, text, button1, button2 );
		this.dialog = dialog;
	}

	@Override
	public void cancelDialog()
	{
		if( dialog == null ) return;
		SampNativeFunction.showPlayerDialog( id, -1, 0, "", "", "", "" );
		
		DialogCancelEvent event = new DialogCancelEvent( dialog, this );
		
		dialog.getEventDispatcher().dispatchEvent( event );
		getEventDispatcher().dispatchEvent( event );
		Shoebill.getInstance().getGlobalEventDispatcher().dispatchEvent( event );
	}
}
