/**
 * Copyright (C) 2011-2014 MK124
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

import java.util.List;
import java.util.Objects;

import net.gtaun.shoebill.CharsetUtils;
import net.gtaun.shoebill.DialogEventUtils;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStore;
import net.gtaun.shoebill.constant.CameraCutStyle;
import net.gtaun.shoebill.constant.DialogStyle;
import net.gtaun.shoebill.constant.FightStyle;
import net.gtaun.shoebill.constant.PlayerState;
import net.gtaun.shoebill.constant.RaceCheckpointType;
import net.gtaun.shoebill.constant.RecordType;
import net.gtaun.shoebill.constant.ShopName;
import net.gtaun.shoebill.constant.SpecialAction;
import net.gtaun.shoebill.constant.SpectateMode;
import net.gtaun.shoebill.constant.WeaponModel;
import net.gtaun.shoebill.constant.WeaponState;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Area;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Radius;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.data.Time;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.data.Velocity;
import net.gtaun.shoebill.data.WeaponData;
import net.gtaun.shoebill.event.dialog.DialogCloseEvent.DialogCloseType;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.exception.IllegalLengthException;
import net.gtaun.shoebill.object.Checkpoint;
import net.gtaun.shoebill.object.DialogId;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerAttach;
import net.gtaun.shoebill.object.PlayerMapIcon;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.PlayerWeaponSkill;
import net.gtaun.shoebill.object.RaceCheckpoint;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 *
 * @author MK124, JoJLlmAn
 */
public class PlayerImpl implements Player
{
	private final SampObjectStore store;

	private int id = INVALID_ID;

	private final PlayerKeyStateImpl keyState;
	private final PlayerAttach playerAttach;
	private final PlayerMapIcon mapIcon;

	private boolean isControllable = true;
	private boolean isStuntBonusEnabled = false;
	private boolean isSpectating = false;
	private boolean isRecording = false;

	private Player spectatingPlayer;
	private Vehicle spectatingVehicle;

	private int updateCount = -1;
	private int weatherId;

	private Area worldBound = new Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f);
	private Velocity velocity = new Velocity();
	private PlayerWeaponSkill skill;

	private Checkpoint checkpoint;
	private RaceCheckpoint raceCheckpoint;
	private DialogId dialog;

	private EventManagerNode eventManagerNode;


	public PlayerImpl(EventManager eventManager, SampObjectStore store, int id)
	{
		this.store = store;
		this.id = id;

		playerAttach = new PlayerAttachImpl(this);
		keyState = new PlayerKeyStateImpl(this);
		skill = new PlayerWeaponSkillImpl(this);
		mapIcon = new PlayerMapIconImpl(this);

		setColor(Color.WHITE);

		SampNativeFunction.getPlayerVelocity(id, velocity);
		SampNativeFunction.getPlayerKeys(id, keyState);

		eventManagerNode = eventManager.createChildNode();
	}

	public void onPlayerUpdate()
	{
		SampNativeFunction.getPlayerVelocity(PlayerImpl.this.id, velocity);

		updateCount++;
		if (updateCount < 0) updateCount = 0;
	}

	public void onPlayerDisconnect()
	{
		id = INVALID_ID;
	}

	public void onDialogResponse()
	{
		dialog = null;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", id).append("name", getName()).append("state", getState()).toString();
	}

	@Override
	public boolean isOnline()
	{
		return id != INVALID_ID;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public PlayerAttach getAttach()
	{
		return playerAttach;
	}

	@Override
	public PlayerKeyStateImpl getKeyState()
	{
		keyState.update();
		return keyState;
	}

	@Override
	public int getUpdateCount()
	{
		return updateCount;
	}

	@Override
	public int getWeather()
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
	public float getAngle()
	{
		if (isOnline() == false) return 0.0f;

		return SampNativeFunction.getPlayerFacingAngle(id);
	}

	@Override
	public AngledLocation getLocation()
	{
		AngledLocation location = new AngledLocation();
		SampNativeFunction.getPlayerPos(id, location);
		location.setAngle(SampNativeFunction.getPlayerFacingAngle(id));
		location.setInteriorId(SampNativeFunction.getPlayerInterior(id));
		location.setWorldId(SampNativeFunction.getPlayerVirtualWorld(id));
		return location;
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
	public WeaponData getWeaponData(int slot)
	{
		WeaponData data = new WeaponData();
		SampNativeFunction.getPlayerWeaponData(id, slot, data);
		return data;
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
	public DialogId getDialog()
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
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerPing(id);
	}

	@Override
	public int getTeam()
	{
		if (isOnline() == false) return NO_TEAM;

		return SampNativeFunction.getPlayerTeam(id);
	}

	@Override
	public int getSkin()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerSkin(id);
	}

	@Override
	public int getWantedLevel()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerWantedLevel(id);
	}

	@Override
	public int getCodepage()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerCodepage(id);
	}

	@Override
	public String getIp()
	{
		if (isOnline() == false) return "0.0.0.0";

		return SampNativeFunction.getPlayerIp(id);
	}

	@Override
	public String getName()
	{
		if (isOnline() == false) return "Unknown";

		return SampNativeFunction.getPlayerName(id);
	}

	@Override
	public Color getColor()
	{
		if (isOnline() == false) return null;

		return new Color(SampNativeFunction.getPlayerColor(id));
	}

	@Override
	public float getHealth()
	{
		if (isOnline() == false) return 0.0f;

		return SampNativeFunction.getPlayerHealth(id);
	}

	@Override
	public float getArmour()
	{
		if (isOnline() == false) return 0.0f;

		return SampNativeFunction.getPlayerArmour(id);
	}

	@Override
	public WeaponModel getArmedWeapon()
	{
		if (isOnline() == false) return WeaponModel.NONE;

		return WeaponModel.get(SampNativeFunction.getPlayerWeapon(id));
	}

	@Override
	public void setArmedWeapon(WeaponModel model)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerArmedWeapon(id, model.getId());
	}

	@Override
	public int getArmedWeaponAmmo()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerAmmo(id);
	}

	@Override
	public int getMoney()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerMoney(id);
	}

	@Override
	public int getScore()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerScore(id);
	}

	@Override
	public int getCameraMode()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerCameraMode(id);
	}

	@Override
	public float getCameraAspectRatio()
	{
		if (isOnline() == false) return 0.0f;

		return SampNativeFunction.getPlayerCameraAspectRatio(id);
	}

	@Override
	public float getCameraZoom()
	{
		if (isOnline() == false) return 0.0f;

		return SampNativeFunction.getPlayerCameraZoom(id);
	}

	@Override
	public FightStyle getFightStyle()
	{
		if (isOnline() == false) return FightStyle.NORMAL;

		return FightStyle.get(SampNativeFunction.getPlayerFightingStyle(id));
	}

	@Override
	public Vehicle getVehicle()
	{
		if (isOnline() == false) return null;

		int vehicleId = SampNativeFunction.getPlayerVehicleID(id);
		return store.getVehicle(vehicleId);
	}

	@Override
	public int getVehicleSeat()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerVehicleSeat(id);
	}

	@Override
	public SpecialAction getSpecialAction()
	{
		if (isOnline() == false) return SpecialAction.NONE;

		return SpecialAction.get(SampNativeFunction.getPlayerSpecialAction(id));
	}

	@Override
	public PlayerState getState()
	{
		if (isOnline() == false) return PlayerState.NONE;

		return PlayerState.values()[SampNativeFunction.getPlayerState(id)];
	}

	@Override
	public void setCodepage(int codepage)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerCodepage(id, codepage);
	}

	@Override
	public void setName(String name) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException
	{
		if (isOnline() == false) return;

		if (name == null) throw new IllegalArgumentException();
		if (name.length() < 3 || name.length() > 20) throw new IllegalLengthException();

		int ret = SampNativeFunction.setPlayerName(id, name);
		if (ret == 0) throw new AlreadyExistException();
		if (ret == -1) throw new IllegalArgumentException();
	}

	@Override
	public void setSpawnInfo(float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, WeaponModel weapon1, int ammo1, WeaponModel weapon2, int ammo2, WeaponModel weapon3, int ammo3)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setSpawnInfo(id, team, skin, x, y, z, angle, weapon1.getId(), ammo1, weapon2.getId(), ammo2, weapon3.getId(), ammo3);
	}

	@Override
	public void setSpawnInfo(Vector3D pos, int interiorId, int worldId, float angle, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3)
	{
		setSpawnInfo(pos.getX(), pos.getY(), pos.getZ(), interiorId, worldId, angle, skin, team, weapon1.getModel(), weapon1.getAmmo(), weapon2.getModel(), weapon2.getAmmo(), weapon3.getModel(), weapon3.getAmmo());
	}

	@Override
	public void setSpawnInfo(Location loc, float angle, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3)
	{
		setSpawnInfo(loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId(), loc.getWorldId(), angle, skin, team, weapon1.getModel(), weapon1.getAmmo(), weapon2.getModel(), weapon2.getAmmo(), weapon3.getModel(), weapon3.getAmmo());
	}

	@Override
	public void setSpawnInfo(AngledLocation loc, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3)
	{
		setSpawnInfo(loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId(), loc.getWorldId(), loc.getAngle(), skin, team, weapon1.getModel(), weapon1.getAmmo(), weapon2.getModel(), weapon2.getAmmo(), weapon3.getModel(), weapon3.getAmmo());
	}

	@Override
	public void setSpawnInfo(SpawnInfo info)
	{
		if (isOnline() == false) return;

		WeaponData weapon1 = info.getWeapon1();
		WeaponData weapon2 = info.getWeapon2();
		WeaponData weapon3 = info.getWeapon3();

		setSpawnInfo(info.getLocation(), info.getSkinId(), info.getTeamId(), weapon1, weapon2, weapon3);
	}

	@Override
	public void setColor(Color color)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerColor(id, color.getValue());
	}

	@Override
	public void setHealth(float health)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerHealth(id, health);
	}

	@Override
	public void setArmour(float armour)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerArmour(id, armour);
	}

	@Override
	public void setWeaponAmmo(WeaponModel weapon, int ammo)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerAmmo(id, weapon.getId(), ammo);
	}

	@Override
	public void setMoney(int money)
	{
		if (isOnline() == false) return;

		SampNativeFunction.resetPlayerMoney(id);
		if (money != 0) SampNativeFunction.givePlayerMoney(id, money);
	}

	@Override
	public void giveMoney(int money)
	{
		if (isOnline() == false) return;
		SampNativeFunction.givePlayerMoney(id, money);
	}

	@Override
	public void setScore(int score)
	{
		if (isOnline() == false) return;
		SampNativeFunction.setPlayerScore(id, score);
	}

	@Override
	public void setWeather(int weather)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerWeather(id, weather);
		this.weatherId = weather;
	}

	@Override
	public void setFightStyle(FightStyle style)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerFightingStyle(id, style.getValue());
	}

	@Override
	public void setVehicle(Vehicle vehicle, int seat)
	{
		if (isOnline() == false) return;
		if (vehicle != null && vehicle.isDestroyed()) return;

		vehicle.putPlayer(this, seat);
	}

	@Override
	public void setVehicle(Vehicle vehicle)
	{
		setVehicle(vehicle, 0);
	}

	@Override
	public void setLocation(float x, float y, float z)
	{
		if (isOnline() == false) return;

		if (isInAnyVehicle() && getVehicleSeat() == 0)
		{
			Vehicle vehicle = getVehicle();
			vehicle.setLocation(x, y, z);
		}
		else
		{
			SampNativeFunction.setPlayerPos(id, x, y, z);
		}
	}

	@Override
	public void setLocation(Vector3D pos)
	{
		SampNativeFunction.setPlayerPos(id, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void setLocation(Location loc)
	{
		if (isOnline() == false) return;

		if (isInAnyVehicle() && getVehicleSeat() == 0)
		{
			Vehicle vehicle = getVehicle();
			vehicle.setLocation(loc);
		}
		else
		{
			SampNativeFunction.setPlayerPos(id, loc.getX(), loc.getY(), loc.getZ());
			SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
			SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
		}
	}

	@Override
	public void setLocation(AngledLocation loc)
	{
		if (isOnline() == false) return;

		if (isInAnyVehicle() && getVehicleSeat() == 0)
		{
			Vehicle vehicle = getVehicle();
			vehicle.setLocation(loc);
		}
		else
		{
			SampNativeFunction.setPlayerPos(id, loc.getX(), loc.getY(), loc.getZ());
			SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
			SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
			SampNativeFunction.setPlayerFacingAngle(id, loc.getAngle());
		}
	}

	@Override
	public void setLocationFindZ(float x, float y, float z)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerPosFindZ(id, x, y, z);
	}

	@Override
	public void setLocationFindZ(Vector3D pos)
	{
		SampNativeFunction.setPlayerPosFindZ(id, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void setLocationFindZ(Location loc)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerPosFindZ(id, loc.getX(), loc.getY(), loc.getZ());

		SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
		SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
	}

	@Override
	public void setLocationFindZ(AngledLocation loc)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerPosFindZ(id, loc.getX(), loc.getY(), loc.getZ());
		SampNativeFunction.setPlayerFacingAngle(id, loc.getAngle());

		SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
		SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
	}

	@Override
	public void setAngle(float angle)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerFacingAngle(id, angle);
	}

	@Override
	public void setInterior(int interiorId)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerInterior(id, interiorId);
	}

	@Override
	public void setWorld(int worldId)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerVirtualWorld(id, worldId);
	}

	@Override
	public void setWorldBound(Area bound)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerWorldBounds(id, bound.getMaxX(), bound.getMinX(), bound.getMaxY(), bound.getMinY());
		worldBound.set(bound);
	}

	@Override
	public void setVelocity(Vector3D vel)
	{
		if (isOnline() == false) return;

		velocity.set(vel);
		SampNativeFunction.setPlayerVelocity(id, vel.getX(), vel.getY(), vel.getZ());
	}

	private void sendMessageInternal(Color color, String message)
	{
		List<String> msgs = CharsetUtils.splitStringByCharsetLength(message, 144, CharsetUtils.getCharsetByCodepage(getCodepage()), "  ");
		for (String s : msgs) SampNativeFunction.sendClientMessage(id, color.getValue(), s);
	}

	@Override
	public void sendMessage(Color color, String message)
	{
		if (isOnline() == false) return;
		if (message == null) throw new NullPointerException();

		sendMessageInternal(color, message);
	}

	@Override
	public void sendMessage(Color color, String format, Object... args)
	{
		if (isOnline() == false) return;

		String message = String.format(format, args);
		sendMessageInternal(color, message);
	}

	@Override
	public void sendChat(Player player, String message)
	{
		if (isOnline() == false) return;

		if (message == null) throw new NullPointerException();
		SampNativeFunction.sendPlayerMessageToPlayer(player.getId(), id, message);
	}

	@Override
	public void sendChatToAll(String message)
	{
		if (isOnline() == false) return;

		if (message == null) throw new NullPointerException();
		for (Player player : store.getPlayers())
		{
			sendChat(player, message);
		}
	}

	@Override
	public void sendDeathMessage(Player killer, Player victim, WeaponModel reason)
	{
		if (isOnline() == false) return;

		SampNativeFunction.sendDeathMessageToPlayer(getId(), killer != null ? killer.getId() : PlayerImpl.INVALID_ID, victim != null ? victim.getId() : PlayerImpl.INVALID_ID, reason.getId());
	}

	@Override
	public void sendGameText(int time, int style, String text)
	{
		if (isOnline() == false) return;

		if (text == null) throw new NullPointerException();
		SampNativeFunction.gameTextForPlayer(id, text, time, style);
	}

	@Override
	public void sendGameText(int time, int style, String format, Object... args)
	{
		if (isOnline() == false) return;

		String text = String.format(format, args);
		SampNativeFunction.gameTextForPlayer(id, text, time, style);
	}

	@Override
	public void spawn()
	{
		if (isOnline() == false) return;

		SampNativeFunction.spawnPlayer(id);
	}

	@Override
	public void setDrunkLevel(int level)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerDrunkLevel(id, level);
	}

	@Override
	public int getDrunkLevel()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerDrunkLevel(id);
	}

	@Override
	public void applyAnimation(String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync)
	{
		if (isOnline() == false) return;

		if (animlib == null || animname == null) throw new NullPointerException();
		SampNativeFunction.applyAnimation(id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync);
	}

	@Override
	public void clearAnimations(int forcesync)
	{
		if (isOnline() == false) return;

		SampNativeFunction.clearAnimations(id, forcesync);
	}

	@Override
	public int getAnimationIndex()
	{
		if (isOnline() == false) return 0;

		return SampNativeFunction.getPlayerAnimationIndex(id);
	}

	@Override
	public void playSound(int sound, float x, float y, float z)
	{
		if (isOnline() == false) return;

		SampNativeFunction.playerPlaySound(id, sound, x, y, z);
	}

	@Override
	public void playSound(int sound, Vector3D loc)
	{
		if (isOnline() == false) return;

		SampNativeFunction.playerPlaySound(id, sound, loc.getX(), loc.getY(), loc.getZ());
	}

	@Override
	public void playSound(int sound)
	{
		if (isOnline() == false) return;

		SampNativeFunction.playerPlaySound(id, sound, 0.0f, 0.0f, 0.0f);
	}

	@Override
	public void markerForPlayer(Player player, Color color)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerMarkerForPlayer(id, player.getId(), color.getValue());
	}

	@Override
	public void showNameTagForPlayer(Player player, boolean show)
	{
		if (isOnline() == false) return;

		SampNativeFunction.showPlayerNameTagForPlayer(id, player.getId(), show);
	}

	@Override
	public void kick()
	{
		if (isOnline() == false) return;

		SampNativeFunction.kick(id);
	}

	@Override
	public void ban()
	{
		if (isOnline() == false) return;

		SampNativeFunction.ban(id);
	}

	@Override
	public void ban(String reason)
	{
		if (isOnline() == false) return;

		if (reason == null) throw new NullPointerException();
		SampNativeFunction.banEx(id, reason);
	}

	@Override
	public Menu getCurrentMenu()
	{
		if (isOnline() == false) return null;

		return store.getMenu(SampNativeFunction.getPlayerMenu(id));
	}

	@Override
	public void setCameraPosition(float x, float y, float z)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerCameraPos(id, x, y, z);
	}

	@Override
	public void setCameraPosition(Vector3D pos)
	{
		if (isOnline() == false) return;

		if (pos == null) throw new NullPointerException();
		SampNativeFunction.setPlayerCameraPos(id, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void setCameraLookAt(float x, float y, float z, CameraCutStyle cut)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerCameraLookAt(id, x, y, z, cut.getValue());
	}

	@Override
	public void setCameraLookAt(Vector3D lookAt, CameraCutStyle cut)
	{
		setCameraLookAt(lookAt.getX(), lookAt.getY(), lookAt.getZ(), cut);
	}

	@Override
	public void setCameraLookAt(float x, float y, float z)
	{
		setCameraLookAt(x, y, z, CameraCutStyle.CUT);
	}

	@Override
	public void setCameraLookAt(Vector3D lookAt)
	{
		setCameraLookAt(lookAt.getX(), lookAt.getY(), lookAt.getZ(), CameraCutStyle.CUT);
	}

	@Override
	public void setCameraBehind()
	{
		if (isOnline() == false) return;

		SampNativeFunction.setCameraBehindPlayer(id);
	}

	@Override
	public Vector3D getCameraPosition()
	{
		if (isOnline() == false) return null;

		Vector3D pos = new Vector3D();
		SampNativeFunction.getPlayerCameraPos(id, pos);
		return pos;
	}

	@Override
	public Vector3D getCameraFrontVector()
	{
		if (isOnline() == false) return null;

		Vector3D lookAt = new Vector3D();
		SampNativeFunction.getPlayerCameraFrontVector(id, lookAt);
		return lookAt;
	}

	@Override
	public boolean isInAnyVehicle()
	{
		if (isOnline() == false) return false;

		return SampNativeFunction.isPlayerInAnyVehicle(id);
	}

	@Override
	public boolean isInVehicle(Vehicle vehicle)
	{
		if (isOnline() == false) return false;
		if (vehicle.isDestroyed()) return false;

		return SampNativeFunction.isPlayerInVehicle(id, vehicle.getId());
	}

	@Override
	public boolean isAdmin()
	{
		if (isOnline() == false) return false;

		return SampNativeFunction.isPlayerAdmin(id);
	}

	@Override
	public boolean isStreamedIn(Player forPlayer)
	{
		if (isOnline() == false) return false;
		if (forPlayer.isOnline() == false) return false;

		return SampNativeFunction.isPlayerStreamedIn(id, forPlayer.getId());
	}

	@Override
	public boolean isNpc()
	{
		if (isOnline() == false) return false;

		return SampNativeFunction.isPlayerNPC(id);
	}

	@Override
	public void setCheckpoint(Checkpoint checkpoint)
	{
		if (isOnline() == false) return;

		if (checkpoint == null)
		{
			disableCheckpoint();
			return;
		}

		Vector3D loc = checkpoint.getLocation();
		SampNativeFunction.setPlayerCheckpoint(id, loc.getX(), loc.getY(), loc.getZ(), checkpoint.getSize());
		this.checkpoint = checkpoint;
	}

	@Override
	public void disableCheckpoint()
	{
		if (isOnline() == false) return;

		SampNativeFunction.disablePlayerCheckpoint(id);
		checkpoint = null;
	}

	@Override
	public void setRaceCheckpoint(RaceCheckpoint checkpoint)
	{
		if (isOnline() == false) return;

		if (checkpoint == null)
		{
			disableRaceCheckpoint();
			return;
		}

		RaceCheckpoint next = checkpoint.getNext();
		Vector3D loc = checkpoint.getLocation();

		if (checkpoint.getNext() != null)
		{
			Vector3D nextLoc = next.getLocation();
			SampNativeFunction.setPlayerRaceCheckpoint(id, checkpoint.getType().getValue(), loc.getX(), loc.getY(), loc.getZ(), nextLoc.getX(), nextLoc.getY(), nextLoc.getZ(), checkpoint.getSize());
		}
		else
		{
			RaceCheckpointType type = checkpoint.getType();

			if (type == RaceCheckpointType.NORMAL) type = RaceCheckpointType.NORMAL_FINISH;
			else if (type == RaceCheckpointType.AIR) type = RaceCheckpointType.AIR_FINISH;

			SampNativeFunction.setPlayerRaceCheckpoint(id, type.getValue(), loc.getX(), loc.getY(), loc.getZ(), loc.getX(), loc.getY(), loc.getZ(), checkpoint.getSize());
		}

		raceCheckpoint = checkpoint;
	}

	@Override
	public void disableRaceCheckpoint()
	{
		if (isOnline() == false) return;

		SampNativeFunction.disablePlayerRaceCheckpoint(id);
		raceCheckpoint = null;
	}

	@Override
	public WeaponState getWeaponState()
	{
		if (isOnline() == false) return WeaponState.UNKNOWN;

		return WeaponState.get(SampNativeFunction.getPlayerWeaponState(id));
	}

	@Override
	public void setTeam(int team)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerTeam(id, team);
	}

	@Override
	public void setSkin(int skin)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerSkin(id, skin);
	}

	@Override
	public void giveWeapon(WeaponModel type, int ammo)
	{
		if (isOnline() == false) return;

		SampNativeFunction.givePlayerWeapon(id, type.getId(), ammo);
	}

	@Override
	public void giveWeapon(WeaponData data)
	{
		giveWeapon(data.getModel(), data.getAmmo());
	}

	@Override
	public void resetWeapons()
	{
		if (isOnline() == false) return;

		SampNativeFunction.resetPlayerWeapons(id);
	}

	@Override
	public void setTime(int hour, int minute)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerTime(id, hour, minute);
	}

	@Override
	public void setTime(Time time)
	{
		setTime(time.getHour(), time.getMinute());
	}

	@Override
	public Time getTime()
	{
		if (isOnline() == false) return null;

		Time time = new Time();
		SampNativeFunction.getPlayerTime(id, time);
		return time;
	}

	@Override
	public void toggleClock(boolean toggle)
	{
		if (isOnline() == false) return;

		SampNativeFunction.togglePlayerClock(id, toggle);
	}

	@Override
	public void forceClassSelection()
	{
		if (isOnline() == false) return;

		SampNativeFunction.forceClassSelection(id);
	}

	@Override
	public void setWantedLevel(int level)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerWantedLevel(id, level);
	}

	@Override
	public void playCrimeReport(int suspectId, int crimeId)
	{
		if (isOnline() == false) return;

		SampNativeFunction.playCrimeReportForPlayer(id, suspectId, crimeId);
	}

	@Override
	public void setShopName(ShopName shop)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerShopName(id, shop.getValue());
	}

	@Override
	public Vehicle getSurfingVehicle()
	{
		if (isOnline() == false) return null;

		int vehicleId = SampNativeFunction.getPlayerSurfingVehicleID(id);
		return store.getVehicle(vehicleId);
	}

	@Override
	public void removeFromVehicle()
	{
		if (isOnline() == false) return;

		SampNativeFunction.removePlayerFromVehicle(id);
	}

	@Override
	public void toggleControllable(boolean toggle)
	{
		if (isOnline() == false) return;

		SampNativeFunction.togglePlayerControllable(id, toggle);
		isControllable = toggle;
	}

	@Override
	public void setSpecialAction(SpecialAction action)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerSpecialAction(id, action.getValue());
	}

	@Override
	public PlayerMapIcon getMapIcon()
	{
		return mapIcon;
	}

	@Override
	public void enableStuntBonus(boolean enable)
	{
		if (isOnline() == false) return;

		SampNativeFunction.enableStuntBonusForPlayer(id, enable ? 1 : 0);
		isStuntBonusEnabled = enable;
	}

	@Override
	public void toggleSpectating(boolean toggle)
	{
		if (isOnline() == false) return;

		SampNativeFunction.togglePlayerSpectating(id, toggle);
		isSpectating = toggle;

		if (toggle) return;

		spectatingPlayer = null;
		spectatingVehicle = null;
	}

	@Override
	public void spectate(Player player, SpectateMode mode)
	{
		if (isOnline() == false) return;

		if (!isSpectating) return;

		SampNativeFunction.playerSpectatePlayer(id, player.getId(), mode.getValue());
		spectatingPlayer = player;
		spectatingVehicle = null;
	}

	@Override
	public void spectate(Vehicle vehicle, SpectateMode mode)
	{
		if (isOnline() == false) return;

		if (!isSpectating) return;

		SampNativeFunction.playerSpectateVehicle(id, vehicle.getId(), mode.getValue());
		spectatingPlayer = null;
		spectatingVehicle = vehicle;
	}

	@Override
	public void startRecord(RecordType type, String recordName)
	{
		if (isOnline() == false) return;

		SampNativeFunction.startRecordingPlayerData(id, type.getValue(), recordName);
		isRecording = true;
	}

	@Override
	public void stopRecord()
	{
		if (isOnline() == false) return;

		SampNativeFunction.stopRecordingPlayerData(id);
		isRecording = false;
	}

	@Override
	public SampObject getSurfingObject()
	{
		if (isOnline() == false) return null;

		int objectid = SampNativeFunction.getPlayerSurfingObjectID(id);
		if (objectid == SampObject.INVALID_ID) return null;

		return store.getObject(objectid);
	}

	@Override
	public String getNetworkStats()
	{
		if (isOnline() == false) return "Disconnected.";

		return SampNativeFunction.getPlayerNetworkStats(id);
	}

	@Override
	public Player getAimedTarget()
	{
		if (isOnline() == false) return null;

		return store.getPlayer(SampNativeFunction.getPlayerTargetPlayer(id));
	}

	@Override
	public void playAudioStream(String url)
	{
		if (isOnline() == false) return;

		SampNativeFunction.playAudioStreamForPlayer(id, url, 0.0f, 0.0f, 0.0f, 0.0f, 0);
	}

	@Override
	public void playAudioStream(String url, float x, float y, float z, float distance)
	{
		if (isOnline() == false) return;

		SampNativeFunction.playAudioStreamForPlayer(id, url, x, y, z, distance, 1);
	}

	@Override
	public void playAudioStream(String url, Vector3D pos, float distance)
	{
		playAudioStream(url, pos.getX(), pos.getY(), pos.getZ(), distance);
	}

	@Override
	public void playAudioStream(String url, Radius loc)
	{
		playAudioStream(url, loc.getX(), loc.getY(), loc.getZ(), loc.getRadius());
	}

	@Override
	public void stopAudioStream()
	{
		if (isOnline() == false) return;

		SampNativeFunction.stopAudioStreamForPlayer(id);
	}

	@Override
	public void removeBuilding(int modelId, float x, float y, float z, float radius)
	{
		if (isOnline() == false) return;

		SampNativeFunction.removeBuildingForPlayer(id, modelId, x, y, z, radius);
	}

	@Override
	public Vector3D getLastShotOrigin()
	{
		if (isOnline() == false) return new Vector3D();

		Vector3D origin = new Vector3D();
		Vector3D hitpos = new Vector3D();

		SampNativeFunction.getPlayerLastShotVectors(id, origin, hitpos);
		return origin;
	}

	@Override
	public Vector3D getLastShotHitPosition()
	{
		if (isOnline() == false) return new Vector3D();

		Vector3D origin = new Vector3D();
		Vector3D hitpos = new Vector3D();

		SampNativeFunction.getPlayerLastShotVectors(id, origin, hitpos);
		return hitpos;
	}

	@Override
	public void removeBuilding(int modelId, Vector3D pos, float radius)
	{
		removeBuilding(modelId, pos.getX(), pos.getY(), pos.getZ(), radius);
	}

	@Override
	public void removeBuilding(int modelId, Radius loc)
	{
		removeBuilding(modelId, loc.getX(), loc.getY(), loc.getZ(), loc.getRadius());
	}

	@Override
	public void showDialog(DialogId dialog, DialogStyle style, String caption, String text, String button1, String button2)
	{
		if (isOnline() == false) return;

		Objects.requireNonNull(dialog);
		Objects.requireNonNull(style);
		Objects.requireNonNull(caption);
		Objects.requireNonNull(text);
		Objects.requireNonNull(button1);
		Objects.requireNonNull(button2);

		if (this.dialog != null)
		{
			DialogEventUtils.dispatchCloseEvent(eventManagerNode, this.dialog, this, DialogCloseType.OVERRIDE);
		}

		SampNativeFunction.showPlayerDialog(id, dialog.getId(), style.getValue(), caption, text, button1, button2);
		this.dialog = dialog;

		DialogEventUtils.dispatchShowEvent(eventManagerNode, dialog, this);
	}

	@Override
	public void cancelDialog()
	{
		if (isOnline() == false) return;

		if (dialog == null) return;
		SampNativeFunction.showPlayerDialog(id, DialogIdImpl.INVALID_ID, 0, " ", " ", " ", " ");

		DialogId canceledDialog = dialog;
		dialog = null;

		DialogEventUtils.dispatchCloseEvent(eventManagerNode, canceledDialog, this, DialogCloseType.CANCEL);
	}

	@Override
	public boolean editObject(SampObject object)
	{
		if (isOnline() == false) return false;
		if (object.isDestroyed()) return false;

		return SampNativeFunction.editObject(id, object.getId());
	}

	@Override
	public boolean editPlayerObject(PlayerObject object)
	{
		if (isOnline() == false) return false;
		if (object.isDestroyed()) return false;

		return SampNativeFunction.editPlayerObject(id, object.getId());
	}

	@Override
	public void selectObject()
	{
		if (isOnline() == false) return;

		SampNativeFunction.selectObject(id);
	}

	@Override
	public void cancelEdit()
	{
		if (isOnline() == false) return;

		SampNativeFunction.cancelEdit(id);
	}

	@Override
	public void attachCameraTo(SampObject object)
	{
		if (isOnline() == false) return;
		if (object.isDestroyed()) return;

		SampNativeFunction.attachCameraToObject(id, object.getId());
	}

	@Override
	public void attachCameraTo(PlayerObject object)
	{
		if (isOnline() == false) return;
		if (object.isDestroyed()) return;
		if (object.getPlayer() != this) return;

		SampNativeFunction.attachCameraToPlayerObject(id, object.getId());
	}

	@Override
	public void interpolateCameraPosition(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, int time, CameraCutStyle cut)
	{
		if (isOnline() == false) return;

		SampNativeFunction.interpolateCameraPos(id, fromX, fromY, fromZ, toX, toY, toZ, time, cut.getValue());
	}

	@Override
	public void interpolateCameraPosition(Vector3D from, Vector3D to, int time, CameraCutStyle cut)
	{
		interpolateCameraPosition(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), time, cut);
	}

	@Override
	public void interpolateCameraLookAt(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, int time, CameraCutStyle cut)
	{
		if (isOnline() == false) return;

		SampNativeFunction.interpolateCameraLookAt(id, fromX, fromY, fromZ, toX, toY, toZ, time, cut.getValue());
	}

	@Override
	public void interpolateCameraLookAt(Vector3D from, Vector3D to, int time, CameraCutStyle cut)
	{
		interpolateCameraLookAt(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), time, cut);
	}

	@Override
	public void selectTextDraw(Color hoverColor)
	{
		if (isOnline() == false) return;

		SampNativeFunction.selectTextDraw(id, hoverColor.getValue());
	}

	@Override
	public void cancelSelectTextDraw()
	{
		if (isOnline() == false) return;

		SampNativeFunction.cancelSelectTextDraw(id);
	}

	@Override
	public void createExplosion(float x, float y, float z, int type, float radius)
	{
		if (isOnline() == false) return;

		SampNativeFunction.createExplosionForPlayer(id, x, y, z, type, radius);
	}

	@Override
	public String getVersion()
	{
		if (isOnline() == false) return "Offline";

		return SampNativeFunction.getPlayerVersion(id);
	}

	@Override
	public int getConnectedTime()
	{
		return SampNativeFunction.netStats_GetConnectedTime(id);
	}

	@Override
	public int getMessagesReceived()
	{
		return SampNativeFunction.netStats_MessagesReceived(id);
	}

	@Override
	public int getBytesReceived()
	{
		return SampNativeFunction.netStats_BytesReceived(id);
	}

	@Override
	public int getMessagesSent()
	{
		return SampNativeFunction.netStats_MessagesSent(id);
	}

	@Override
	public int getBytesSent()
	{
		return SampNativeFunction.netStats_BytesSent(id);
	}

	@Override
	public int getMessagesRecvPerSecond()
	{
		return SampNativeFunction.netStats_MessagesRecvPerSecond(id);
	}

	@Override
	public float getPacketLossPercent()
	{
		return SampNativeFunction.netStats_PacketLossPercent(id);
	}

	@Override
	public int getConnectionStatus()
	{
		return SampNativeFunction.netStats_ConnectionStatus(id);
	}

	@Override
	public String getIpPort()
	{
		return SampNativeFunction.netStats_GetIpPort(id);
	}

	@Override
	public void setChatBubble(String text, Color color, float drawDistance, int expireTime)
	{
		if (isOnline() == false) return;

		SampNativeFunction.setPlayerChatBubble(id, text, color.getValue(), drawDistance, expireTime);
	}
}
