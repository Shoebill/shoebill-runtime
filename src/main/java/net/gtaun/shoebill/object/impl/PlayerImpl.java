/**
 * Copyright (C) 2011-2012 MK124
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
package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampObjectPoolImpl;
import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.ShoebillLowLevel;
import net.gtaun.shoebill.constant.DialogStyle;
import net.gtaun.shoebill.constant.FightStyle;
import net.gtaun.shoebill.constant.MapIconStyle;
import net.gtaun.shoebill.constant.PlayerState;
import net.gtaun.shoebill.constant.RaceCheckpointType;
import net.gtaun.shoebill.constant.RecordType;
import net.gtaun.shoebill.constant.ShopName;
import net.gtaun.shoebill.constant.SpecialAction;
import net.gtaun.shoebill.constant.SpectateMode;
import net.gtaun.shoebill.constant.WeaponState;
import net.gtaun.shoebill.constant.WeaponType;
import net.gtaun.shoebill.data.Area;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.LocationAngle;
import net.gtaun.shoebill.data.LocationRadius;
import net.gtaun.shoebill.data.Point3D;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.data.Time;
import net.gtaun.shoebill.data.Velocity;
import net.gtaun.shoebill.data.WeaponData;
import net.gtaun.shoebill.events.dialog.DialogCancelEvent;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.exception.IllegalLengthException;
import net.gtaun.shoebill.object.Checkpoint;
import net.gtaun.shoebill.object.Dialog;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerAttach;
import net.gtaun.shoebill.object.PlayerWeaponSkill;
import net.gtaun.shoebill.object.RaceCheckpoint;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.proxy.ProxyManager;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124, JoJLlmAn
 */
public class PlayerImpl implements Player
{
	private ProxyManager proxyManager;
	
	public static void enableStuntBonusForAll( boolean enabled )
	{
		for( Player player : ShoebillImpl.getInstance().getSampObjectPool().getPlayers() )
		{
			player.enableStuntBonus( enabled );
		}
	}

	public static void sendMessageToAll( Color color, String message )
	{
		for( Player player : ShoebillImpl.getInstance().getSampObjectPool().getPlayers() )
		{
			player.sendMessage( color, message );
		}
	}
	
	public static void sendMessageToAll( Color color, String format, Object... args )
	{
		for( Player player : ShoebillImpl.getInstance().getSampObjectPool().getPlayers() )
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
	
	
	private int id = INVALID_ID;
	
	private final PlayerKeyStateImpl keyState;
	private final PlayerAttach playerAttach;
	
	private boolean isControllable = true;
	private boolean isStuntBonusEnabled = false;
	private boolean isSpectating = false;
	private boolean isRecording = false;
	
	private Player spectatingPlayer;
	private Vehicle spectatingVehicle;

	private int updateFrameCount = -1;
	private int weatherId;

	private LocationAngle location = new LocationAngle();
	private Area worldBound = new Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f);
	private Velocity velocity = new Velocity();
	private PlayerWeaponSkill skill;
	
	private Checkpoint checkpoint;
	private RaceCheckpoint raceCheckpoint;
	private Dialog dialog;
	
	
	public PlayerImpl( int id )
	{
		this.id = id;
		
		playerAttach = new PlayerAttachImpl(this);
		keyState = new PlayerKeyStateImpl(this);
		skill = new PlayerWeaponSkillImpl(this);
		
		SampNativeFunction.getPlayerPos(id, location);
		SampNativeFunction.getPlayerFacingAngle(id);
		
		location.setInteriorId( SampNativeFunction.getPlayerInterior(id) );
		location.setWorldId( SampNativeFunction.getPlayerVirtualWorld(id) );
		
		SampNativeFunction.getPlayerVelocity(id, velocity);
		SampNativeFunction.getPlayerKeys(id, keyState);

		SampObjectPoolImpl pool = (SampObjectPoolImpl) ShoebillImpl.getInstance().getSampObjectPool();
		if( pool.getPlayer(id) != null ) throw new UnsupportedOperationException();
	}
	
	public void processPlayerUpdate()
	{
		SampNativeFunction.getPlayerPos( id, location );
		location.setAngle( SampNativeFunction.getPlayerFacingAngle(id) );
		SampNativeFunction.getPlayerVelocity( id, velocity );
		keyState.update();

		updateFrameCount++;
		if( updateFrameCount<0 ) updateFrameCount = 0;
	}
	
	public void processPlayerInteriorChange()
	{
		location.setInteriorId( SampNativeFunction.getPlayerInterior(id) );
	}
	
	public void processPlayerDisconnect()
	{
		id = INVALID_ID;
	}
	
	public void processDialogResponse()
	{
		dialog = null;
	}

	@Override
	public ProxyManager getProxyManager()
	{
		return proxyManager;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public boolean isOnline()
	{
		return id != INVALID_ID;
	}

	@Override public int getId()
	{
		return id;
	}
	
	@Override
	public PlayerAttach getPlayerAttach()
	{
		return playerAttach;
	}

	@Override
	public PlayerKeyStateImpl getKeyState()
	{
		return keyState;
	}
	
	@Override
	public int getUpdateFrameCount()
	{
		return updateFrameCount;
	}
	
	@Override
	public int getWeatherId()
	{
		return weatherId;
	}
	
	@Override
	public Player getSpectatingPlayer()
	{
		return spectatingPlayer;
	}
	
	@Override
	public Vehicle getSpectatingVehicle()
	{
		return spectatingVehicle;
	}
	
	@Override
	public LocationAngle getLocation()
	{
		return location.clone();
	}
	
	@Override
	public Area getWorldBound()
	{
		return worldBound.clone();
	}
	
	@Override
	public Velocity getVelocity()
	{
		return velocity.clone();
	}
	
	@Override
	public PlayerWeaponSkill getWeaponSkill()
	{
		return skill;
	}
	
	@Override
	public Checkpoint getCheckpoint()
	{
		return checkpoint;
	}
	
	@Override
	public RaceCheckpoint getRaceCheckpoint()
	{
		return raceCheckpoint;
	}
	
	@Override
	public Dialog getDialog()
	{
		return dialog;
	}

	@Override
	public boolean isStuntBonusEnabled()
	{
		return isStuntBonusEnabled;
	}
	
	@Override
	public boolean isSpectating()
	{
		return isSpectating;
	}
	
	@Override
	public boolean isRecording()
	{
		return isRecording;
	}
	
	@Override
	public boolean isControllable()
	{
		return isControllable;
	}
	
	@Override
	public int getPing()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerPing(id);
	}
	
	@Override
	public int getTeamId()
	{
		if( isOnline() == false ) return NO_TEAM;
		
		return SampNativeFunction.getPlayerTeam(id);
	}
	
	@Override
	public int getSkinId()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerSkin(id);
	}
	
	@Override
	public int getWantedLevel()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerWantedLevel(id);
	}
	
	@Override
	public int getCodepage()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerCodepage(id);
	}
	
	@Override
	public String getIp()
	{
		if( isOnline() == false ) return "0.0.0.0";
		
		return SampNativeFunction.getPlayerIp(id);
	}
	
	@Override
	public String getName()
	{
		if( isOnline() == false ) return "Unknown";
		
		return SampNativeFunction.getPlayerName(id);
	}
	
	@Override
	public Color getColor()
	{
		if( isOnline() == false ) return null;
		
		return new Color(SampNativeFunction.getPlayerColor(id));
	}
	
	@Override
	public float getHealth()
	{
		if( isOnline() == false ) return 0.0f;
		
		return SampNativeFunction.getPlayerHealth(id);
	}
	
	@Override
	public float getArmour()
	{
		if( isOnline() == false ) return 0.0f;
		
		return SampNativeFunction.getPlayerArmour(id);
	}
	
	@Override
	public WeaponType getArmedWeapon()
	{
		if( isOnline() == false ) return WeaponType.NONE;
		
		return WeaponType.get( SampNativeFunction.getPlayerWeapon(id) );
	}
	
	@Override
	public int getArmedWeaponAmmo()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerAmmo(id);
	}
	
	@Override
	public int getMoney()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerMoney(id);
	}
	
	@Override
	public int getScore()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerScore(id);
	}
	
	@Override
	public int getCameraMode()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerCameraMode(id);
	}
	
	@Override
	public FightStyle getFightStyle()
	{
		if( isOnline() == false ) return FightStyle.NORMAL;
		
		return FightStyle.get(SampNativeFunction.getPlayerFightingStyle(id));
	}
	
	@Override
	public Vehicle getVehicle()
	{
		if( isOnline() == false ) return null;
		
		int vehicleId = SampNativeFunction.getPlayerVehicleID(id);
		return ShoebillImpl.getInstance().getSampObjectPool().getVehicle(vehicleId);
	}
	
	@Override
	public int getVehicleSeat()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerVehicleSeat(id);
	}
	
	@Override
	public SpecialAction getSpecialAction()
	{
		if( isOnline() == false ) return SpecialAction.NONE;
		
		return SpecialAction.get(SampNativeFunction.getPlayerSpecialAction(id));
	}
	
	@Override
	public PlayerState getState()
	{
		if( isOnline() == false ) return PlayerState.NONE;
		
		return PlayerState.values()[ SampNativeFunction.getPlayerState(id) ];
	}
	
	@Override
	public void setCodepage( int codepage )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerCodepage( id, codepage );
	}
	
	@Override
	public void setName( String name ) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException
	{
		if( isOnline() == false ) return;
		
		if( name == null ) throw new IllegalArgumentException();
		if( name.length()<3 || name.length()>20 ) throw new IllegalLengthException();
		
		int ret = SampNativeFunction.setPlayerName(id, name);
		if( ret == 0 )	throw new AlreadyExistException();
		if( ret == -1 )	throw new IllegalArgumentException();
	}

	@Override
	public void setSpawnInfo( float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, WeaponType weapon1, int ammo1, WeaponType weapon2, int ammo2, WeaponType weapon3, int ammo3 )
	{
		if( isOnline() == false ) return;

		SampNativeFunction.setSpawnInfo( id, team, skin, x, y, z, angle, weapon1.getId(), ammo1, weapon2.getId(), ammo2, weapon3.getId(), ammo3 );
	}

	@Override
	public void setSpawnInfo( Point3D pos, int interiorId, int worldId, float angle, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3 )
	{
		setSpawnInfo( pos.getX(), pos.getY(), pos.getZ(), interiorId, worldId, angle, skin, team, weapon1.getType(), weapon1.getAmmo(), weapon2.getType(), weapon2.getAmmo(), weapon3.getType(), weapon3.getAmmo() );
	}

	@Override
	public void setSpawnInfo( Location loc, float angle, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3 )
	{
		setSpawnInfo( loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId(), loc.getWorldId(), angle, skin, team, weapon1.getType(), weapon1.getAmmo(), weapon2.getType(), weapon2.getAmmo(), weapon3.getType(), weapon3.getAmmo() );
	}
	
	@Override
	public void setSpawnInfo( LocationAngle loc, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3 )
	{
		setSpawnInfo( loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId(), loc.getWorldId(), loc.getAngle(), skin, team, weapon1.getType(), weapon1.getAmmo(), weapon2.getType(), weapon2.getAmmo(), weapon3.getType(), weapon3.getAmmo() );
	}
	
	@Override
	public void setSpawnInfo( SpawnInfo info )
	{
		if( isOnline() == false ) return;

		WeaponData weapon1 = info.getWeapon1();
		WeaponData weapon2 = info.getWeapon2();
		WeaponData weapon3 = info.getWeapon3();
		
		setSpawnInfo( info.getLocation(), info.getSkinId(), info.getTeamId(), weapon1, weapon2, weapon3 );
	}
	
	@Override
	public void setColor( Color color )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerColor( id, color.getValue() );
	}

	@Override
	public void setHealth( float health )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerHealth( id, health );
	}
	
	@Override
	public void setArmour( float armour)
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerArmour( id, armour );
	}
	
	@Override
	public void setWeaponAmmo( int weaponSlot, int ammo )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerAmmo( id, weaponSlot, ammo );
	}
	
	@Override
	public void setMoney( int money )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.resetPlayerMoney( id );
		if( money != 0 ) SampNativeFunction.givePlayerMoney( id, money );
	}
	
	@Override
	public void giveMoney( int money )
	{
		if( isOnline() == false ) return;
		SampNativeFunction.givePlayerMoney( id, money );
	}
	
	@Override
	public void setScore( int score )
	{
		if( isOnline() == false ) return;
		SampNativeFunction.setPlayerScore( id, score );
	}
	
	@Override
	public void setWeatherId( int weather )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerWeather( id, weather );
		this.weatherId = weather;
	}
	
	@Override
	public void setFightStyle( FightStyle style )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerFightingStyle( id, style.getData() );
	}

	@Override
	public void setVehicle( Vehicle vehicle, int seat )
	{
		if( isOnline() == false ) return;
		if( vehicle != null && vehicle.isDestroyed() ) return;
		
		if( vehicle == null )
		{
			removeFromVehicle();
			return;
		}
		
		vehicle.putPlayer( this, seat );
	}
	
	@Override
	public void setVehicle( Vehicle vehicle )
	{
		setVehicle( vehicle, 0 );
	}

	@Override
	public void setLocation( float x, float y, float z )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPos( id, x, y, z );

		location.set( x, y, z );
	}

	@Override
	public void setLocation( Point3D pos )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPos( id, pos.getX(), pos.getY(), pos.getZ() );

		location.set( pos );
	}

	@Override
	public void setLocation( Location loc )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPos( id, loc.getX(), loc.getY(), loc.getZ() );

		if( loc.getInteriorId() != location.getInteriorId() ) SampNativeFunction.setPlayerInterior( id, loc.getInteriorId() );
		if( loc.getWorldId() != location.getWorldId() ) SampNativeFunction.setPlayerVirtualWorld( id, loc.getWorldId() );
		
		location.set( loc );
	}

	@Override
	public void setLocation( LocationAngle loc )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPos( id, loc.getX(), loc.getY(), loc.getZ() );
		SampNativeFunction.setPlayerFacingAngle( id, loc.getAngle() );

		if( loc.getInteriorId() != location.getInteriorId() ) SampNativeFunction.setPlayerInterior( id, loc.getInteriorId() );
		if( loc.getWorldId() != location.getWorldId() ) SampNativeFunction.setPlayerVirtualWorld( id, loc.getWorldId() );
		
		location.set( loc );
	}
	
	@Override
	public void setLocationFindZ( float x, float y, float z )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPosFindZ( id, x, y, z );

		location.set( x, y, z );
	}

	@Override
	public void setLocationFindZ( Point3D pos )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPosFindZ( id, pos.getX(), pos.getY(), pos.getZ() );

		location.set( pos );
	}
	
	@Override
	public void setLocationFindZ( Location loc )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPosFindZ( id, loc.getX(), loc.getY(), loc.getZ() );

		if( loc.getInteriorId() != location.getInteriorId() ) SampNativeFunction.setPlayerInterior( id, loc.getInteriorId() );
		if( loc.getWorldId() != location.getWorldId() ) SampNativeFunction.setPlayerVirtualWorld( id, loc.getWorldId() );
		
		location.set( loc );
	}
	
	@Override
	public void setLocationFindZ( LocationAngle loc )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerPosFindZ( id, loc.getX(), loc.getY(), loc.getZ() );
		SampNativeFunction.setPlayerFacingAngle( id, loc.getAngle() );

		if( loc.getInteriorId() != location.getInteriorId() ) SampNativeFunction.setPlayerInterior( id, loc.getInteriorId() );
		if( loc.getWorldId() != location.getWorldId() ) SampNativeFunction.setPlayerVirtualWorld( id, loc.getWorldId() );
		
		location.set( loc );
	}
	
	@Override
	public void setAngle( float angle )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerFacingAngle( id, angle );
		location.setAngle( angle );
	}
	
	@Override
	public void setInteriorId( int interiorId )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerInterior( id, interiorId );
		location.setInteriorId( interiorId );
	}
	
	@Override
	public void setWorldId( int worldId )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerVirtualWorld( id, worldId );
		location.setWorldId( worldId );
	}
	
	@Override
	public void setWorldBound( Area bound )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerWorldBounds( id, bound.getMaxX(), bound.getMinX(), bound.getMaxY(), bound.getMinY() );
		worldBound.set( bound );
	}
	
	@Override
	public void setVelocity( Point3D vel )
	{
		if( isOnline() == false ) return;

		velocity.set( vel );
		SampNativeFunction.setPlayerVelocity( id, vel.getX(), vel.getY(), vel.getZ() );
	}

	@Override
	public void sendMessage( Color color, String message )
	{
		if( isOnline() == false ) return;
		
		if( message == null ) throw new NullPointerException();
		SampNativeFunction.sendClientMessage( id, color.getValue(), message );
	}
	
	@Override
	public void sendMessage( Color color, String format, Object... args )
	{
		if( isOnline() == false ) return;
		
		String message = String.format(format, args);
		SampNativeFunction.sendClientMessage( id, color.getValue(), message );
	}
	
	@Override
	public void sendChat( Player player, String message )
	{
		if( isOnline() == false ) return;
		
		if( message == null ) throw new NullPointerException();
		SampNativeFunction.sendPlayerMessageToPlayer( player.getId(), id, message );
	}
	
	@Override
	public void sendChatToAll( String message )
	{
		if( isOnline() == false ) return;
		
		if( message == null ) throw new NullPointerException();
		for( Player player : ShoebillImpl.getInstance().getSampObjectPool().getPlayers() )
		{
			sendChat( player, message );
		}
	}

	@Override
	public void sendDeathMessage( Player killer, int reason )
	{
		if( isOnline() == false ) return;
		
		if( killer == null )
		{
			SampNativeFunction.sendDeathMessage( INVALID_ID, id, reason );
		}
		else
		{
			SampNativeFunction.sendDeathMessage( killer.getId(), id, reason );
		}
	}

	@Override
	public void sendGameText( int time, int style, String text )
	{
		if( isOnline() == false ) return;
		
		if( text == null ) throw new NullPointerException();
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}
	
	@Override
	public void sendGameText( int time, int style, String format, Object... args )
	{
		if( isOnline() == false ) return;
		
		String text = String.format(format, args);
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}

	@Override
	public void spawn()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.spawnPlayer( id );
	}
	
	@Override
	public void setDrunkLevel( int level )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerDrunkLevel( id, level );
	}
	
	@Override
	public int getDrunkLevel()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerDrunkLevel(id);
	}

	@Override
	public void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync )
	{
		if( isOnline() == false ) return;
		
		if( animlib == null || animname == null ) throw new NullPointerException();
		SampNativeFunction.applyAnimation( id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync );
	}
	
	@Override
	public void clearAnimations( int forcesync )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.clearAnimations( id, forcesync );
	}
	
	@Override
	public int getAnimationIndex()
	{
		if( isOnline() == false ) return 0;
		
		return SampNativeFunction.getPlayerAnimationIndex(id);
	}

	@Override
	public void playSound( int sound, float x, float y, float z )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.playerPlaySound( id, sound, x, y, z );
	}
	
	@Override
	public void playSound( int sound, Point3D loc )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.playerPlaySound( id, sound, loc.getX(), loc.getY(), loc.getZ() );
	}
	
	@Override
	public void markerForPlayer( Player player, Color color )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerMarkerForPlayer( id, player.getId(), color.getValue() );
	}
	
	@Override
	public void showNameTagForPlayer( Player player, boolean show )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.showPlayerNameTagForPlayer( id, player.getId(), show );
	}

	@Override
	public void kick()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.kick( id );
	}
	
	@Override
	public void ban()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.ban( id );
	}
	
	@Override
	public void ban( String reason )
	{
		if( isOnline() == false ) return;
		
		if( reason == null ) throw new NullPointerException();
		SampNativeFunction.banEx( id, reason );
	}
	
	@Override
	public Menu getMenu()
	{
		if( isOnline() == false ) return null;
		
		return ShoebillImpl.getInstance().getSampObjectPool().getMenu( SampNativeFunction.getPlayerMenu(id) );
	}

	@Override
	public void setCameraPosition( float x, float y, float z )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerCameraPos( id, x, y, z );
	}
	
	@Override
	public void setCameraPosition( Point3D pos )
	{
		if( isOnline() == false ) return;
		
		if( pos == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerCameraPos( id, pos.getX(), pos.getY(), pos.getZ() );
	}

	@Override
	public void setCameraLookAt( float x, float y, float z )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerCameraLookAt(id, x, y, z);
	}
	
	@Override
	public void setCameraLookAt( Point3D lookAt )
	{
		if( isOnline() == false ) return;
		
		if( lookAt == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerCameraLookAt(id, lookAt.getX(), lookAt.getY(), lookAt.getZ());
	}
	
	@Override
	public void setCameraBehind()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setCameraBehindPlayer(id);
	}
	
	@Override
	public Point3D getCameraPosition()
	{
		if( isOnline() == false ) return null;
		
		Point3D pos = new Point3D();
		SampNativeFunction.getPlayerCameraPos( id, pos );
		return pos;
	}
	
	@Override
	public Point3D getCameraFrontVector()
	{
		if( isOnline() == false ) return null;
		
		Point3D lookAt = new Point3D();
		SampNativeFunction.getPlayerCameraFrontVector( id, lookAt );
		return lookAt;
	}
	
	@Override
	public boolean isInAnyVehicle()
	{
		if( isOnline() == false ) return false;
		
		return SampNativeFunction.isPlayerInAnyVehicle( id );
	}
	
	@Override
	public boolean isInVehicle( Vehicle vehicle )
	{
		if( isOnline() == false ) return false;
		if( vehicle.isDestroyed() ) return false;
		
		return SampNativeFunction.isPlayerInVehicle( id, vehicle.getId() );
	}
	
	@Override
	public boolean isAdmin()
	{
		if( isOnline() == false ) return false;
		
		return SampNativeFunction.isPlayerAdmin(id);
	}
	
	@Override
	public boolean isStreamedIn( Player forPlayer )
	{
		if( isOnline() == false ) return false;
		if( forPlayer.isOnline() == false ) return false;
		
		return SampNativeFunction.isPlayerStreamedIn(id, forPlayer.getId());
	}
	
	@Override
	public void setCheckpoint( Checkpoint checkpoint )
	{
		if( isOnline() == false ) return;
		
		if( checkpoint == null )
		{
			disableCheckpoint();
			return;
		}

		Point3D loc = checkpoint.getLocation();
		SampNativeFunction.setPlayerCheckpoint( id, loc.getX(), loc.getY(), loc.getZ(), checkpoint.getSize() );
		this.checkpoint = checkpoint;
	}
	
	@Override
	public void disableCheckpoint()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.disablePlayerCheckpoint( id );
		checkpoint = null;
	}
	
	@Override
	public void setRaceCheckpoint( RaceCheckpoint checkpoint )
	{
		if( isOnline() == false ) return;
		
		if( checkpoint == null )
		{
			disableRaceCheckpoint();
			return;
		}
		
		RaceCheckpoint next = checkpoint.getNext();
		
		Point3D loc = checkpoint.getLocation();
		Point3D nextLoc = next.getLocation();
		
		if( checkpoint.getNext() != null )
		{
			SampNativeFunction.setPlayerRaceCheckpoint( id, checkpoint.getType().getData(), loc.getX(), loc.getY(), loc.getZ(), nextLoc.getX(), nextLoc.getY(), nextLoc.getZ(), checkpoint.getSize() );
		}
		else
		{
			RaceCheckpointType type = checkpoint.getType();
			
			if( type == RaceCheckpointType.NORMAL )			type = RaceCheckpointType.NORMAL_FINISH;
			else if( type == RaceCheckpointType.AIR )		type = RaceCheckpointType.AIR_FINISH;
			
			SampNativeFunction.setPlayerRaceCheckpoint( id, type.getData(), loc.getX(), loc.getY(), loc.getZ(), loc.getX(), loc.getY(), loc.getZ(), checkpoint.getSize() );
		}
		
		raceCheckpoint = checkpoint;
	}
	
	@Override
	public void disableRaceCheckpoint()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.disablePlayerRaceCheckpoint( id );
		raceCheckpoint = null;
	}
	
	@Override
	public WeaponState getWeaponState()
	{
		if( isOnline() == false ) return WeaponState.UNKNOWN;
		
		return WeaponState.get( SampNativeFunction.getPlayerWeaponState(id) );
	}
	
	@Override
	public void setTeam( int team )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerTeam( id, team );
	}
	
	@Override
	public void setSkin( int skin )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerSkin( id, skin );
	}

	@Override
	public void giveWeapon( WeaponType type, int ammo )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.givePlayerWeapon( id, type.getId(), ammo );
	}

	@Override
	public void giveWeapon( WeaponData data )
	{
		giveWeapon( data.getType(), data.getAmmo() );
	}
	
	@Override
	public void resetWeapons()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.resetPlayerWeapons( id );
	}

	@Override
	public void setTime( int hour, int minute )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerTime( id, hour, minute );
	}
	
	@Override
	public void setTime( Time time )
	{
		setTime( time.getHour(), time.getMinute() );
	}
	
	@Override
	public Time getTime()
	{
		if( isOnline() == false ) return null;
		
		Time time = new Time();
		SampNativeFunction.getPlayerTime( id, time );
		return time;
	}
	
	@Override
	public void toggleClock( boolean toggle )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.togglePlayerClock( id, toggle );
	}
	
	@Override
	public void forceClassSelection()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.forceClassSelection( id );
	}
	
	@Override
	public void setWantedLevel( int level )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerWantedLevel( id, level );
	}
	
	@Override
	public void playCrimeReport( int suspectId, int crimeId )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.playCrimeReportForPlayer( id, suspectId, crimeId );
	}
	
	@Override
	public void setShopName( ShopName shop )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerShopName(id, shop.getData());
	}
	
	@Override
	public Vehicle getSurfingVehicle()
	{
		if( isOnline() == false ) return null;
		
		int vehicleId = SampNativeFunction.getPlayerSurfingVehicleID(id);
		return ShoebillImpl.getInstance().getSampObjectPool().getVehicle(vehicleId);
	}
	
	@Override
	public void removeFromVehicle()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.removePlayerFromVehicle(id);
	}
	
	@Override
	public void toggleControllable( boolean toggle )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.togglePlayerControllable( id, toggle );
		isControllable = toggle;
	}
	
	@Override
	public void setSpecialAction( SpecialAction action )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerSpecialAction( id, action.getData() );
	}

	@Override
	public void setMapIcon( int iconId, float x, float y, float z, int markerType, Color color, MapIconStyle style )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.setPlayerMapIcon( id, iconId, x, y, z, markerType, color.getValue(), style.getData() );
	}
	
	@Override
	public void setMapIcon( int iconId, Point3D pos, int markerType, Color color, MapIconStyle style )
	{
		setMapIcon( iconId, pos.getX(), pos.getY(), pos.getZ(), markerType, color, style );
	}
	
	@Override
	public void removeMapIcon( int iconId )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.removePlayerMapIcon( id, iconId );
	}
	
	@Override
	public void enableStuntBonus( boolean enable )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.enableStuntBonusForPlayer( id, enable ? 1 : 0 );
		isStuntBonusEnabled = enable;
	}
	
	@Override
	public void toggleSpectating( boolean toggle )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.togglePlayerSpectating( id, toggle );
		isSpectating = toggle;
		
		if( toggle ) return;
		
		spectatingPlayer = null;
		spectatingVehicle = null;
	}
	
	@Override
	public void spectate( Player player, SpectateMode mode )
	{
		if( isOnline() == false ) return;
		
		if( !isSpectating ) return;
		
		SampNativeFunction.playerSpectatePlayer(id, player.getId(), mode.getData());
		spectatingPlayer = player;
		spectatingVehicle = null;
	}
	
	@Override
	public void spectate( Vehicle vehicle, SpectateMode mode )
	{
		if( isOnline() == false ) return;
		
		if( !isSpectating ) return;

		SampNativeFunction.playerSpectateVehicle(id, vehicle.getId(), mode.getData());
		spectatingPlayer = null;
		spectatingVehicle = vehicle;
	}
	
	@Override
	public void startRecord( RecordType type, String recordName )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.startRecordingPlayerData( id, type.getData(), recordName );
		isRecording = true;
	}
	
	@Override
	public void stopRecord()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.stopRecordingPlayerData( id );
		isRecording = false;
	}
	
	@Override
	public SampObject getSurfingObject()
	{
		if( isOnline() == false ) return null;
		
		int objectid = SampNativeFunction.getPlayerSurfingObjectID(id);
		if( objectid == SampObject.INVALID_ID ) return null;
		
		return ShoebillImpl.getInstance().getSampObjectPool().getObject( objectid );
	}
	
	@Override
	public String getNetworkStats()
	{
		if( isOnline() == false ) return "Disconnected.";
		
		return SampNativeFunction.getPlayerNetworkStats(id);
	}
	
	
	@Override
	public Player getAimedTarget()
	{
		if( isOnline() == false ) return null;
		
		return ShoebillImpl.getInstance().getSampObjectPool().getPlayer( SampNativeFunction.getPlayerTargetPlayer(id) );
	}

	@Override
	public void playAudioStream( String url )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.playAudioStreamForPlayer( id, url, 0.0f, 0.0f, 0.0f, 0.0f, 0 );
	}

	@Override
	public void playAudioStream( String url, float x, float y, float z, float distance )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.playAudioStreamForPlayer( id, url, x, y, z, distance, 1 );
	}

	@Override
	public void playAudioStream( String url, Point3D pos, float distance )
	{
		playAudioStream( url, pos.getX(), pos.getY(), pos.getZ(), distance );
	}
	
	@Override
	public void playAudioStream( String url, LocationRadius loc )
	{
		playAudioStream( url, loc.getX(), loc.getY(), loc.getZ(), loc.getRadius() );
	}
	
	@Override
	public void stopAudioStream()
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.stopAudioStreamForPlayer( id );
	}

	@Override
	public void removeBuilding( int modelId, float x, float y, float z, float radius )
	{
		if( isOnline() == false ) return;
		
		SampNativeFunction.removeBuildingForPlayer( id, modelId, x, y, z, radius );
	}
	
	@Override
	public void removeBuilding( int modelId, Point3D pos, float radius )
	{
		removeBuilding( modelId, pos.getX(), pos.getY(), pos.getZ(), radius );
	}
	
	@Override
	public void removeBuilding( int modelId, LocationRadius loc )
	{
		removeBuilding( modelId, loc.getX(), loc.getY(), loc.getZ(), loc.getRadius() );
	}
	
	@Override
	public void showDialog( Dialog dialog, DialogStyle style, String caption, String text, String button1, String button2 )
	{
		if( isOnline() == false ) return;
		
		if( caption == null || text == null || button1 == null || button2 == null ) throw new NullPointerException();
		cancelDialog();
		
		SampNativeFunction.showPlayerDialog( id, dialog.getId(), style.getData(), caption, text, button1, button2 );
		this.dialog = dialog;
	}
	

	@Override
	public void cancelDialog()
	{
		if( isOnline() == false ) return;
		
		if( dialog == null ) return;
		SampNativeFunction.showPlayerDialog( id, -1, 0, "", "", "", "" );
		
		DialogCancelEvent event = new DialogCancelEvent( dialog, this );
		ShoebillLowLevel shoebillLowLevel = (ShoebillLowLevel) ShoebillImpl.getInstance();
		shoebillLowLevel.getEventManager().dispatchEvent( event, dialog, this );
	}
}
