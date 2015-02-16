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

import net.gtaun.shoebill.*;
import net.gtaun.shoebill.constant.*;
import net.gtaun.shoebill.data.*;
import net.gtaun.shoebill.event.dialog.DialogCloseEvent.DialogCloseType;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.exception.IllegalLengthException;
import net.gtaun.shoebill.object.*;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author MK124, JoJLlmAn, 123marvin123
 */
public class PlayerImpl implements Player {
    private final SampObjectStore store;
    private final PlayerKeyStateImpl keyState;
    private final PlayerAttach playerAttach;
    private final PlayerMapIcon mapIcon;
    private int id = INVALID_ID;
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


    public PlayerImpl(EventManager eventManager, SampObjectStore store, int id) {
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

    public void onPlayerUpdate() {
        SampNativeFunction.getPlayerVelocity(PlayerImpl.this.id, velocity);

        updateCount++;
        if (updateCount < 0) updateCount = 0;
    }

    public void onPlayerDisconnect() {
        id = INVALID_ID;
    }

    public void onDialogResponse() {
        dialog = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("id", id).append("name", getName()).append("state", getState()).toString();
    }

    @Override
    public boolean isOnline() {
        return id != INVALID_ID;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public PlayerAttach getAttach() {
        return playerAttach;
    }

    @Override
    public PlayerKeyStateImpl getKeyState() {
        keyState.update();
        return keyState;
    }

    @Override
    public int getUpdateCount() {
        return updateCount;
    }

    @Override
    public int getWeather() {
        return weatherId;
    }

    @Override
    public void setWeather(int weather) {
        if (!isOnline()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.setPlayerWeather(id, weather));
        setWeatherWithoutExec(weather);
    }

    @Override
    public Player getSpectatingPlayer() {
        return spectatingPlayer;
    }

    @Override
    public Vehicle getSpectatingVehicle() {
        return spectatingVehicle;
    }

    @Override
    public float getAngle() {
        if (!isOnline()) return 0.0f;

        return SampNativeFunction.getPlayerFacingAngle(id);
    }

    @Override
    public void setAngle(float angle) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerFacingAngle(id, angle);
    }

    @Override
    public AngledLocation getLocation() {
        AngledLocation location = new AngledLocation();
        SampNativeFunction.getPlayerPos(id, location);
        location.setAngle(SampNativeFunction.getPlayerFacingAngle(id));
        location.setInteriorId(SampNativeFunction.getPlayerInterior(id));
        location.setWorldId(SampNativeFunction.getPlayerVirtualWorld(id));
        return location;
    }

    @Override
    public void setLocation(AngledLocation loc) {
        if (!isOnline()) return;

        Vehicle vehicle = getVehicle();
        if (vehicle != null && getVehicleSeat() == 0) {
            vehicle.setLocation(loc);
        } else {
            SampNativeFunction.setPlayerPos(id, loc.getX(), loc.getY(), loc.getZ());
            SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
            SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
            SampNativeFunction.setPlayerFacingAngle(id, loc.getAngle());
        }
    }

    @Override
    public Area getWorldBound() {
        return worldBound.clone();
    }

    @Override
    public void setWorldBound(Area bound) {
        if (!isOnline()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.setPlayerWorldBounds(id, bound.getMaxX(), bound.getMinX(), bound.getMaxY(), bound.getMinY()));
    }

    @Override
    public Velocity getVelocity() {
        return velocity.clone();
    }

    @Override
    public void setVelocity(Vector3D vel) {
        if (!isOnline()) return;

        velocity.set(vel);
        SampNativeFunction.setPlayerVelocity(id, vel.getX(), vel.getY(), vel.getZ());
    }

    @Override
    public PlayerWeaponSkill getWeaponSkill() {
        return skill;
    }

    @Override
    public WeaponData getWeaponData(int slot) {
        WeaponData data = new WeaponData();
        SampNativeFunction.getPlayerWeaponData(id, slot, data);
        return data;
    }

    @Override
    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    @Override
    public void setCheckpoint(Checkpoint checkpoint) {
        if (!isOnline()) return;

        if (checkpoint == null) {
            disableCheckpoint();
            return;
        }

        Vector3D loc = checkpoint.getLocation();
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.setPlayerCheckpoint(id, loc.getX(), loc.getY(), loc.getZ(), checkpoint.getSize()));
        setCheckpointWithoutExec(checkpoint);
    }

    @Override
    public RaceCheckpoint getRaceCheckpoint() {
        return raceCheckpoint;
    }

    @Override
    public void setRaceCheckpoint(RaceCheckpoint checkpoint) {
        if (!isOnline()) return;

        if (checkpoint == null) {
            disableRaceCheckpoint();
            return;
        }

        RaceCheckpoint next = checkpoint.getNext();
        Vector3D loc = checkpoint.getLocation();

        if (next != null) {
            Vector3D nextLoc = next.getLocation();
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.setPlayerRaceCheckpoint(id, checkpoint.getType().getValue(), loc.getX(), loc.getY(), loc.getZ(), nextLoc.getX(), nextLoc.getY(), nextLoc.getZ(), checkpoint.getSize()));
        } else {
            RaceCheckpointType type = checkpoint.getType();

            if (type == RaceCheckpointType.NORMAL) type = RaceCheckpointType.NORMAL_FINISH;
            else if (type == RaceCheckpointType.AIR) type = RaceCheckpointType.AIR_FINISH;

            final RaceCheckpointType finalType = type;
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.setPlayerRaceCheckpoint(id, finalType.getValue(), loc.getX(), loc.getY(), loc.getZ(), loc.getX(), loc.getY(), loc.getZ(), checkpoint.getSize()));
        }

        setRaceCheckpointWithoutExec(checkpoint);
    }

    @Override
    public DialogId getDialog() {
        return dialog;
    }

    public void setDialog(DialogId dialog) {
        this.dialog = dialog;
    }

    @Override
    public boolean isStuntBonusEnabled() {
        return isStuntBonusEnabled;
    }

    @Override
    public boolean isSpectating() {
        return isSpectating;
    }

    @Override
    public boolean isRecording() {
        return isRecording;
    }

    @Override
    public boolean isControllable() {
        return isControllable;
    }

    @Override
    public int getPing() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerPing(id);
    }

    @Override
    public int getTeam() {
        if (!isOnline()) return NO_TEAM;

        return SampNativeFunction.getPlayerTeam(id);
    }

    @Override
    public void setTeam(int team) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerTeam(id, team);
    }

    @Override
    public int getSkin() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerSkin(id);
    }

    @Override
    public void setSkin(int skin) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerSkin(id, skin);
    }

    @Override
    public int getWantedLevel() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerWantedLevel(id);
    }

    @Override
    public void setWantedLevel(int level) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerWantedLevel(id, level);
    }

    @Override
    public int getCodepage() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerCodepage(id);
    }

    @Override
    public void setCodepage(int codepage) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerCodepage(id, codepage);
    }

    @Override
    public String getIp() {
        if (!isOnline()) return "0.0.0.0";

        return SampNativeFunction.getPlayerIp(id);
    }

    @Override
    public String getName() {
        if (!isOnline()) return "Unknown";

        return SampNativeFunction.getPlayerName(id);
    }

    @Override
    public void setName(String name) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException {
        if (!isOnline()) return;

        if (name == null) throw new IllegalArgumentException();
        if (name.length() < 3 || name.length() > 20) throw new IllegalLengthException();

        int ret = SampNativeFunction.setPlayerName(id, name);
        if (ret == 0) throw new AlreadyExistException();
        if (ret == -1) throw new IllegalArgumentException();
    }

    @Override
    public Color getColor() {
        if (!isOnline()) return null;

        return new Color(SampNativeFunction.getPlayerColor(id));
    }

    @Override
    public void setColor(Color color) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerColor(id, color.getValue());
    }

    @Override
    public float getHealth() {
        if (!isOnline()) return 0.0f;

        return SampNativeFunction.getPlayerHealth(id);
    }

    @Override
    public void setHealth(float health) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerHealth(id, health);
    }

    @Override
    public float getArmour() {
        if (!isOnline()) return 0.0f;

        return SampNativeFunction.getPlayerArmour(id);
    }

    @Override
    public void setArmour(float armour) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerArmour(id, armour);
    }

    @Override
    public WeaponModel getArmedWeapon() {
        if (!isOnline()) return WeaponModel.NONE;

        return WeaponModel.get(SampNativeFunction.getPlayerWeapon(id));
    }

    @Override
    public void setArmedWeapon(WeaponModel model) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerArmedWeapon(id, model.getId());
    }

    @Override
    public int getArmedWeaponAmmo() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerAmmo(id);
    }

    @Override
    public int getMoney() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerMoney(id);
    }

    @Override
    public void setMoney(int money) {
        if (!isOnline()) return;

        SampNativeFunction.resetPlayerMoney(id);
        if (money != 0) SampNativeFunction.givePlayerMoney(id, money);
    }

    @Override
    public int getScore() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerScore(id);
    }

    @Override
    public void setScore(int score) {
        if (!isOnline()) return;
        SampNativeFunction.setPlayerScore(id, score);
    }

    @Override
    public int getCameraMode() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerCameraMode(id);
    }

    @Override
    public float getCameraAspectRatio() {
        if (!isOnline()) return 0.0f;

        return SampNativeFunction.getPlayerCameraAspectRatio(id);
    }

    @Override
    public float getCameraZoom() {
        if (!isOnline()) return 0.0f;

        return SampNativeFunction.getPlayerCameraZoom(id);
    }

    @Override
    public FightStyle getFightStyle() {
        if (!isOnline()) return FightStyle.NORMAL;

        return FightStyle.get(SampNativeFunction.getPlayerFightingStyle(id));
    }

    @Override
    public void setFightStyle(FightStyle style) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerFightingStyle(id, style.getValue());
    }

    @Override
    public Vehicle getVehicle() {
        if (!isOnline()) return null;

        int vehicleId = SampNativeFunction.getPlayerVehicleID(id);
        return store.getVehicle(vehicleId);
    }

    @Override
    public void setVehicle(Vehicle vehicle) {
        setVehicle(vehicle, 0);
    }

    @Override
    public int getVehicleSeat() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerVehicleSeat(id);
    }

    @Override
    public SpecialAction getSpecialAction() {
        if (!isOnline()) return SpecialAction.NONE;

        return SpecialAction.get(SampNativeFunction.getPlayerSpecialAction(id));
    }

    @Override
    public void setSpecialAction(SpecialAction action) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerSpecialAction(id, action.getValue());
    }

    @Override
    public PlayerState getState() {
        if (!isOnline()) return PlayerState.NONE;

        return PlayerState.values()[SampNativeFunction.getPlayerState(id)];
    }

    @Override
    public void setSpawnInfo(float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, WeaponModel weapon1, int ammo1, WeaponModel weapon2, int ammo2, WeaponModel weapon3, int ammo3) {
        if (!isOnline()) return;

        SampNativeFunction.setSpawnInfo(id, team, skin, x, y, z, angle, weapon1.getId(), ammo1, weapon2.getId(), ammo2, weapon3.getId(), ammo3);
    }

    @Override
    public void setSpawnInfo(Vector3D pos, int interiorId, int worldId, float angle, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3) {
        setSpawnInfo(pos.getX(), pos.getY(), pos.getZ(), interiorId, worldId, angle, skin, team, weapon1.getModel(), weapon1.getAmmo(), weapon2.getModel(), weapon2.getAmmo(), weapon3.getModel(), weapon3.getAmmo());
    }

    @Override
    public void setSpawnInfo(Location loc, float angle, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3) {
        setSpawnInfo(loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId(), loc.getWorldId(), angle, skin, team, weapon1.getModel(), weapon1.getAmmo(), weapon2.getModel(), weapon2.getAmmo(), weapon3.getModel(), weapon3.getAmmo());
    }

    @Override
    public void setSpawnInfo(AngledLocation loc, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3) {
        setSpawnInfo(loc.getX(), loc.getY(), loc.getZ(), loc.getInteriorId(), loc.getWorldId(), loc.getAngle(), skin, team, weapon1.getModel(), weapon1.getAmmo(), weapon2.getModel(), weapon2.getAmmo(), weapon3.getModel(), weapon3.getAmmo());
    }

    @Override
    public void setSpawnInfo(SpawnInfo info) {
        if (!isOnline()) return;

        WeaponData weapon1 = info.getWeapon1();
        WeaponData weapon2 = info.getWeapon2();
        WeaponData weapon3 = info.getWeapon3();

        setSpawnInfo(info.getLocation(), info.getSkinId(), info.getTeamId(), weapon1, weapon2, weapon3);
    }

    @Override
    public void setWeaponAmmo(WeaponModel weapon, int ammo) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerAmmo(id, weapon.getId(), ammo);
    }

    @Override
    public void giveMoney(int money) {
        if (!isOnline()) return;
        SampNativeFunction.givePlayerMoney(id, money);
    }

    public void setWeatherWithoutExec(int weather) {
        if (!isOnline()) return;
        this.weatherId = weather;
    }

    @Override
    public void setVehicle(Vehicle vehicle, int seat) {
        if (!isOnline()) return;
        if (vehicle == null || vehicle.isDestroyed()) return;

        vehicle.putPlayer(this, seat);
    }

    @Override
    public void setLocation(float x, float y, float z) {
        if (!isOnline()) return;

        Vehicle vehicle = getVehicle();
        if (vehicle != null && getVehicleSeat() == 0) {
            vehicle.setLocation(x, y, z);
        } else {
            SampNativeFunction.setPlayerPos(id, x, y, z);
        }
    }

    @Override
    public void setLocation(Vector3D pos) {
        SampNativeFunction.setPlayerPos(id, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setLocation(Location loc) {
        if (!isOnline()) return;

        Vehicle vehicle = getVehicle();
        if (vehicle != null && getVehicleSeat() == 0) {
            vehicle.setLocation(loc);
        } else {
            SampNativeFunction.setPlayerPos(id, loc.getX(), loc.getY(), loc.getZ());
            SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
            SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
        }
    }

    @Override
    public void setLocationFindZ(float x, float y, float z) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerPosFindZ(id, x, y, z);
    }

    @Override
    public void setLocationFindZ(Vector3D pos) {
        SampNativeFunction.setPlayerPosFindZ(id, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setLocationFindZ(Location loc) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerPosFindZ(id, loc.getX(), loc.getY(), loc.getZ());

        SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
        SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
    }

    @Override
    public void setLocationFindZ(AngledLocation loc) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerPosFindZ(id, loc.getX(), loc.getY(), loc.getZ());
        SampNativeFunction.setPlayerFacingAngle(id, loc.getAngle());

        SampNativeFunction.setPlayerInterior(id, loc.getInteriorId());
        SampNativeFunction.setPlayerVirtualWorld(id, loc.getWorldId());
    }

    @Override
    public void setInterior(int interiorId) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerInterior(id, interiorId);
    }

    @Override
    public void setWorld(int worldId) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerVirtualWorld(id, worldId);
    }

    public void setWorldBoundWithoutExec(Area bound) {
        if (!isOnline()) return;
        worldBound.set(bound);
    }

    @Override
    public void sendMessage(String s) {
        sendMessage(Color.WHITE, s);
    }

    private void sendMessageInternal(Color color, String message) {
        List<String> msgs = CharsetUtils.splitStringByCharsetLength(message, 144, CharsetUtils.getCharsetByCodepage(getCodepage()), "  ");
        for (String s : msgs) SampNativeFunction.sendClientMessage(id, color.getValue(), s);
    }

    @Override
    public void sendMessage(Color color, String message) {
        if (!isOnline()) return;
        if (message == null) throw new NullPointerException();

        sendMessageInternal(color, message);
    }

    @Override
    public void sendMessage(Color color, String format, Object... args) {
        if (!isOnline()) return;

        String message = String.format(format, args);
        sendMessageInternal(color, message);
    }

    @Override
    public void sendChat(Player player, String message) {
        if (!isOnline()) return;

        if (message == null) throw new NullPointerException();
        SampNativeFunction.sendPlayerMessageToPlayer(player.getId(), id, message);
    }

    @Override
    public void sendChatToAll(String message) {
        if (!isOnline()) return;

        if (message == null) throw new NullPointerException();
        for (Player player : store.getPlayers()) {
            sendChat(player, message);
        }
    }

    @Override
    public void sendDeathMessage(Player killer, Player victim, WeaponModel reason) {
        if (!isOnline()) return;

        SampNativeFunction.sendDeathMessageToPlayer(getId(), killer != null ? killer.getId() : PlayerImpl.INVALID_ID, victim != null ? victim.getId() : PlayerImpl.INVALID_ID, reason.getId());
    }

    @Override
    public void sendGameText(int time, int style, String text) {
        if (!isOnline()) return;

        if (text == null) throw new NullPointerException();
        SampNativeFunction.gameTextForPlayer(id, text, time, style);
    }

    @Override
    public void sendGameText(int time, int style, String format, Object... args) {
        if (!isOnline()) return;

        String text = String.format(format, args);
        SampNativeFunction.gameTextForPlayer(id, text, time, style);
    }

    @Override
    public void spawn() {
        if (!isOnline()) return;
        SampNativeFunction.spawnPlayer(id);
    }

    @Override
    public int getDrunkLevel() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerDrunkLevel(id);
    }

    @Override
    public void setDrunkLevel(int level) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerDrunkLevel(id, level);
    }

    @Override
    public void applyAnimation(String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync) {
        if (!isOnline()) return;

        if (animlib == null || animname == null) throw new NullPointerException();
        SampNativeFunction.applyAnimation(id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync);
    }

    @Override
    public void clearAnimations(int forcesync) {
        if (!isOnline()) return;

        SampNativeFunction.clearAnimations(id, forcesync);
    }

    @Override
    public int getAnimationIndex() {
        if (!isOnline()) return 0;

        return SampNativeFunction.getPlayerAnimationIndex(id);
    }

    @Override
    public void playSound(int sound, float x, float y, float z) {
        if (!isOnline()) return;

        SampNativeFunction.playerPlaySound(id, sound, x, y, z);
    }

    @Override
    public void playSound(int sound, Vector3D loc) {
        if (!isOnline()) return;

        SampNativeFunction.playerPlaySound(id, sound, loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void playSound(int sound) {
        if (!isOnline()) return;

        SampNativeFunction.playerPlaySound(id, sound, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void markerForPlayer(Player player, Color color) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerMarkerForPlayer(id, player.getId(), color.getValue());
    }

    @Override
    public void showNameTagForPlayer(Player player, boolean show) {
        if (!isOnline()) return;

        SampNativeFunction.showPlayerNameTagForPlayer(id, player.getId(), show);
    }

    @Override
    public void kick() {
        if (!isOnline()) return;

        SampNativeFunction.kick(id);
    }

    @Override
    public void ban() {
        if (!isOnline()) return;

        SampNativeFunction.ban(id);
    }

    @Override
    public void ban(String reason) {
        if (!isOnline()) return;

        if (reason == null) throw new NullPointerException();
        SampNativeFunction.banEx(id, reason);
    }

    @Override
    public Menu getCurrentMenu() {
        if (!isOnline()) return null;

        return store.getMenu(SampNativeFunction.getPlayerMenu(id));
    }

    @Override
    public void setCameraPosition(float x, float y, float z) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerCameraPos(id, x, y, z);
    }

    @Override
    public void setCameraLookAt(float x, float y, float z, CameraCutStyle cut) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerCameraLookAt(id, x, y, z, cut.getValue());
    }

    @Override
    public void setCameraLookAt(Vector3D lookAt, CameraCutStyle cut) {
        setCameraLookAt(lookAt.getX(), lookAt.getY(), lookAt.getZ(), cut);
    }

    @Override
    public void setCameraLookAt(float x, float y, float z) {
        setCameraLookAt(x, y, z, CameraCutStyle.CUT);
    }

    @Override
    public void setCameraLookAt(Vector3D lookAt) {
        setCameraLookAt(lookAt.getX(), lookAt.getY(), lookAt.getZ(), CameraCutStyle.CUT);
    }

    @Override
    public void setCameraBehind() {
        if (!isOnline()) return;

        SampNativeFunction.setCameraBehindPlayer(id);
    }

    @Override
    public Vector3D getCameraPosition() {
        if (!isOnline()) return null;

        Vector3D pos = new Vector3D();
        SampNativeFunction.getPlayerCameraPos(id, pos);
        return pos;
    }

    @Override
    public void setCameraPosition(Vector3D pos) {
        if (!isOnline()) return;

        if (pos == null) throw new NullPointerException();
        SampNativeFunction.setPlayerCameraPos(id, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public Vector3D getCameraFrontVector() {
        if (!isOnline()) return null;

        Vector3D lookAt = new Vector3D();
        SampNativeFunction.getPlayerCameraFrontVector(id, lookAt);
        return lookAt;
    }

    @Override
    public boolean isInAnyVehicle() {
        return isOnline() && SampNativeFunction.isPlayerInAnyVehicle(id);

    }

    @Override
    public boolean isInVehicle(Vehicle vehicle) {
        if (!isOnline()) return false;
        return !vehicle.isDestroyed() && SampNativeFunction.isPlayerInVehicle(id, vehicle.getId());

    }

    @Override
    public boolean isAdmin() {
        return isOnline() && SampNativeFunction.isPlayerAdmin(id);

    }

    @Override
    public boolean isStreamedIn(Player forPlayer) {
        if (!isOnline()) return false;
        return forPlayer.isOnline() && SampNativeFunction.isPlayerStreamedIn(id, forPlayer.getId());

    }

    @Override
    public boolean isNpc() {
        return isOnline() && SampNativeFunction.isPlayerNPC(id);

    }

    public void setCheckpointWithoutExec(Checkpoint checkpoint) {
        if (!isOnline()) return;
        if (checkpoint == null) {
            disableCheckpoint();
            return;
        }
        this.checkpoint = checkpoint;
    }

    @Override
    public void disableCheckpoint() {
        if (!isOnline()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.disablePlayerCheckpoint(id));
        disableCheckpointWithoutExec();
    }

    public void disableCheckpointWithoutExec() {
        if (!isOnline()) return;
        checkpoint = null;
    }

    public void setRaceCheckpointWithoutExec(RaceCheckpoint checkpoint) {
        if (!isOnline()) return;
        if (checkpoint == null) {
            disableRaceCheckpoint();
            return;
        }
        raceCheckpoint = checkpoint;
    }

    @Override
    public void disableRaceCheckpoint() {
        if (!isOnline()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.disablePlayerRaceCheckpoint(id));
        disableRaceCheckpointWithoutExec();
    }

    public void disableRaceCheckpointWithoutExec() {
        if (!isOnline()) return;
        raceCheckpoint = null;
    }

    @Override
    public WeaponState getWeaponState() {
        if (!isOnline()) return WeaponState.UNKNOWN;

        return WeaponState.get(SampNativeFunction.getPlayerWeaponState(id));
    }

    @Override
    public void giveWeapon(WeaponModel type, int ammo) {
        if (!isOnline()) return;

        SampNativeFunction.givePlayerWeapon(id, type.getId(), ammo);
    }

    @Override
    public void giveWeapon(WeaponData data) {
        giveWeapon(data.getModel(), data.getAmmo());
    }

    @Override
    public void resetWeapons() {
        if (!isOnline()) return;

        SampNativeFunction.resetPlayerWeapons(id);
    }

    @Override
    public void setTime(int hour, int minute) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerTime(id, hour, minute);
    }

    @Override
    public Time getTime() {
        if (!isOnline()) return null;

        Time time = new Time();
        SampNativeFunction.getPlayerTime(id, time);
        return time;
    }

    @Override
    public void setTime(Time time) {
        setTime(time.getHour(), time.getMinute());
    }

    @Override
    public void toggleClock(boolean toggle) {
        if (!isOnline()) return;

        SampNativeFunction.togglePlayerClock(id, toggle);
    }

    @Override
    public void forceClassSelection() {
        if (!isOnline()) return;

        SampNativeFunction.forceClassSelection(id);
    }

    @Override
    public void playCrimeReport(int suspectId, int crimeId) {
        if (!isOnline()) return;

        SampNativeFunction.playCrimeReportForPlayer(id, suspectId, crimeId);
    }

    @Override
    public void setShopName(ShopName shop) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerShopName(id, shop.getValue());
    }

    @Override
    public Vehicle getSurfingVehicle() {
        if (!isOnline()) return null;

        int vehicleId = SampNativeFunction.getPlayerSurfingVehicleID(id);
        return store.getVehicle(vehicleId);
    }

    @Override
    public void removeFromVehicle() {
        if (!isOnline()) return;

        SampNativeFunction.removePlayerFromVehicle(id);
    }

    @Override
    public void toggleControllable(boolean toggle) {
        if (!isOnline()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.togglePlayerControllable(id, toggle));
        toggleControllableWithoutExec(toggle);
    }

    public void toggleControllableWithoutExec(boolean toggle) {
        if (!isOnline()) return;
        isControllable = toggle;
    }

    @Override
    public PlayerMapIcon getMapIcon() {
        return mapIcon;
    }

    @Override
    public void enableStuntBonus(boolean enable) {
        if (!isOnline()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.enableStuntBonusForPlayer(id, enable ? 1 : 0));
        enableStuntBonusWithoutExec(enable);
    }

    public void enableStuntBonusWithoutExec(boolean enable) {
        if (!isOnline()) return;
        isStuntBonusEnabled = enable;
    }

    @Override
    public void toggleSpectating(boolean toggle) {
        if (!isOnline()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.togglePlayerSpectating(id, toggle));
        toggleSpectatingWithoutExec(toggle);
    }

    public void toggleSpectatingWithoutExec(boolean toggle) {
        if (!isOnline()) return;
        isSpectating = toggle;
        if (toggle) return;
        spectatingPlayer = null;
        spectatingVehicle = null;
    }

    @Override
    public void spectate(Player player, SpectateMode mode) {
        if (!isOnline()) return;

        if (!isSpectating) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.playerSpectatePlayer(id, player.getId(), mode.getValue()));
        spectateWithoutExec(player);
    }

    public void spectateWithoutExec(Player player) {
        if (!isOnline()) return;
        if (!isSpectating) return;
        spectatingPlayer = player;
        spectatingVehicle = null;
    }

    @Override
    public void spectate(Vehicle vehicle, SpectateMode mode) {
        if (!isOnline()) return;

        if (!isSpectating) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.playerSpectateVehicle(id, vehicle.getId(), mode.getValue()));
        spectateWithoutExec(vehicle);
    }

    public void spectateWithoutExec(Vehicle vehicle) {
        if (!isOnline()) return;
        if (!isSpectating) return;
        spectatingPlayer = null;
        spectatingVehicle = vehicle;
    }

    @Override
    public void startRecord(RecordType type, String recordName) {
        if (!isOnline()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.startRecordingPlayerData(id, type.getValue(), recordName));
        startRecordingWithoutExec();
    }

    public void startRecordingWithoutExec() {
        if (!isOnline()) return;
        isRecording = true;
    }

    @Override
    public void stopRecord() {
        if (!isOnline()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.stopRecordingPlayerData(id));
        stopRecordingWithoutExec();
    }

    public void stopRecordingWithoutExec() {
        if (!isOnline()) return;
        isRecording = false;
    }

    @Override
    public SampObject getSurfingObject() {
        if (!isOnline()) return null;

        int objectid = SampNativeFunction.getPlayerSurfingObjectID(id);
        if (objectid == SampObject.INVALID_ID) return null;

        return store.getObject(objectid);
    }

    @Override
    public String getNetworkStats() {
        if (!isOnline()) return "Disconnected.";

        return SampNativeFunction.getPlayerNetworkStats(id);
    }

    @Override
    public Player getAimedTarget() {
        if (!isOnline()) return null;

        return store.getPlayer(SampNativeFunction.getPlayerTargetPlayer(id));
    }

    @Override
    public void playAudioStream(String url) {
        if (!isOnline()) return;

        SampNativeFunction.playAudioStreamForPlayer(id, url, 0.0f, 0.0f, 0.0f, 0.0f, 0);
    }

    @Override
    public void playAudioStream(String url, float x, float y, float z, float distance) {
        if (!isOnline()) return;

        SampNativeFunction.playAudioStreamForPlayer(id, url, x, y, z, distance, 1);
    }

    @Override
    public void playAudioStream(String url, Vector3D pos, float distance) {
        playAudioStream(url, pos.getX(), pos.getY(), pos.getZ(), distance);
    }

    @Override
    public void playAudioStream(String url, Radius loc) {
        playAudioStream(url, loc.getX(), loc.getY(), loc.getZ(), loc.getRadius());
    }

    @Override
    public void stopAudioStream() {
        if (!isOnline()) return;

        SampNativeFunction.stopAudioStreamForPlayer(id);
    }

    @Override
    public void removeBuilding(int modelId, float x, float y, float z, float radius) {
        if (!isOnline()) return;

        SampNativeFunction.removeBuildingForPlayer(id, modelId, x, y, z, radius);
    }

    @Override
    public Vector3D getLastShotOrigin() {
        if (!isOnline()) return new Vector3D();

        Vector3D origin = new Vector3D();
        Vector3D hitpos = new Vector3D();

        SampNativeFunction.getPlayerLastShotVectors(id, origin, hitpos);
        return origin;
    }

    @Override
    public Vector3D getLastShotHitPosition() {
        if (!isOnline()) return new Vector3D();

        Vector3D origin = new Vector3D();
        Vector3D hitpos = new Vector3D();

        SampNativeFunction.getPlayerLastShotVectors(id, origin, hitpos);
        return hitpos;
    }

    @Override
    public void removeBuilding(int modelId, Vector3D pos, float radius) {
        removeBuilding(modelId, pos.getX(), pos.getY(), pos.getZ(), radius);
    }

    @Override
    public void removeBuilding(int modelId, Radius loc) {
        removeBuilding(modelId, loc.getX(), loc.getY(), loc.getZ(), loc.getRadius());
    }

    @Override
    public void showDialog(DialogId dialog, DialogStyle style, String caption, String text, String button1, String button2) {
        if (!isOnline()) return;

        Objects.requireNonNull(dialog);
        Objects.requireNonNull(style);
        Objects.requireNonNull(caption);
        Objects.requireNonNull(text);
        Objects.requireNonNull(button1);
        Objects.requireNonNull(button2);

        if (this.dialog != null) {
            DialogEventUtils.dispatchCloseEvent(eventManagerNode, this.dialog, this, DialogCloseType.OVERRIDE);
        }

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.showPlayerDialog(id, dialog.getId(), style.getValue(), caption, text, button1, button2));
        this.dialog = dialog;

        DialogEventUtils.dispatchShowEvent(eventManagerNode, dialog, this);
    }

    @Override
    public void cancelDialog() {
        if (!isOnline()) return;

        if (dialog == null) return;
        SampNativeFunction.showPlayerDialog(id, DialogIdImpl.INVALID_ID, 0, " ", " ", " ", " ");

        DialogId canceledDialog = dialog;
        dialog = null;

        DialogEventUtils.dispatchCloseEvent(eventManagerNode, canceledDialog, this, DialogCloseType.CANCEL);
    }

    @Override
    public boolean editObject(SampObject object) {
        if (!isOnline()) return false;
        return !object.isDestroyed() && SampNativeFunction.editObject(id, object.getId());

    }

    @Override
    public boolean editPlayerObject(PlayerObject object) {
        if (!isOnline()) return false;
        return !object.isDestroyed() && SampNativeFunction.editPlayerObject(id, object.getId());

    }

    @Override
    public void selectObject() {
        if (!isOnline()) return;

        SampNativeFunction.selectObject(id);
    }

    @Override
    public void cancelEdit() {
        if (!isOnline()) return;

        SampNativeFunction.cancelEdit(id);
    }

    @Override
    public void attachCameraTo(SampObject object) {
        if (!isOnline()) return;
        if (object.isDestroyed()) return;

        SampNativeFunction.attachCameraToObject(id, object.getId());
    }

    @Override
    public void attachCameraTo(PlayerObject object) {
        if (!isOnline()) return;
        if (object.isDestroyed()) return;
        if (object.getPlayer() != this) return;

        SampNativeFunction.attachCameraToPlayerObject(id, object.getId());
    }

    @Override
    public void interpolateCameraPosition(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, int time, CameraCutStyle cut) {
        if (!isOnline()) return;

        SampNativeFunction.interpolateCameraPos(id, fromX, fromY, fromZ, toX, toY, toZ, time, cut.getValue());
    }

    @Override
    public void interpolateCameraPosition(Vector3D from, Vector3D to, int time, CameraCutStyle cut) {
        interpolateCameraPosition(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), time, cut);
    }

    @Override
    public void interpolateCameraLookAt(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, int time, CameraCutStyle cut) {
        if (!isOnline()) return;

        SampNativeFunction.interpolateCameraLookAt(id, fromX, fromY, fromZ, toX, toY, toZ, time, cut.getValue());
    }

    @Override
    public void interpolateCameraLookAt(Vector3D from, Vector3D to, int time, CameraCutStyle cut) {
        interpolateCameraLookAt(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), time, cut);
    }

    @Override
    public void selectTextDraw(Color hoverColor) {
        if (!isOnline()) return;

        SampNativeFunction.selectTextDraw(id, hoverColor.getValue());
    }

    @Override
    public void cancelSelectTextDraw() {
        if (!isOnline()) return;

        SampNativeFunction.cancelSelectTextDraw(id);
    }

    @Override
    public void createExplosion(float x, float y, float z, int type, float radius) {
        if (!isOnline()) return;

        SampNativeFunction.createExplosionForPlayer(id, x, y, z, type, radius);
    }

    @Override
    public String getVersion() {
        if (!isOnline()) return "Offline";

        return SampNativeFunction.getPlayerVersion(id);
    }

    @Override
    public LocationZone getMainZoneName() {
        return LocationZone.getPlayerMainZone(this);
    }

    @Override
    public LocationZone getZoneName() {
        return LocationZone.getPlayerZone(this);
    }

    @Override
    public int getConnectedTime() {
        return SampNativeFunction.netStats_GetConnectedTime(id);
    }

    @Override
    public int getMessagesReceived() {
        return SampNativeFunction.netStats_MessagesReceived(id);
    }

    @Override
    public int getBytesReceived() {
        return SampNativeFunction.netStats_BytesReceived(id);
    }

    @Override
    public int getMessagesSent() {
        return SampNativeFunction.netStats_MessagesSent(id);
    }

    @Override
    public int getBytesSent() {
        return SampNativeFunction.netStats_BytesSent(id);
    }

    @Override
    public int getMessagesRecvPerSecond() {
        return SampNativeFunction.netStats_MessagesRecvPerSecond(id);
    }

    @Override
    public float getPacketLossPercent() {
        return SampNativeFunction.netStats_PacketLossPercent(id);
    }

    @Override
    public int getConnectionStatus() {
        return SampNativeFunction.netStats_ConnectionStatus(id);
    }

    @Override
    public String getIpPort() {
        return SampNativeFunction.netStats_GetIpPort(id);
    }

    @Override
    public void setChatBubble(String text, Color color, float drawDistance, int expireTime) {
        if (!isOnline()) return;

        SampNativeFunction.setPlayerChatBubble(id, text, color.getValue(), drawDistance, expireTime);
    }

    @Override
    public void setVarInt(String name, int value) {
        SampNativeFunction.setPVarInt(id, name, value);
    }

    @Override
    public int getVarInt(String name) {
        return SampNativeFunction.getPVarInt(id, name);
    }

    @Override
    public void setVarString(String name, String value) {
        SampNativeFunction.setPVarString(id, name, value);
    }

    @Override
    public String getVarString(String name) {
        return SampNativeFunction.getPVarString(id, name);
    }

    @Override
    public void setVarFloat(String name, float value) {
        SampNativeFunction.setPVarFloat(id, name, value);
    }

    @Override
    public float getVarFloat(String name) {
        return SampNativeFunction.getPVarFloat(id, name);
    }

    @Override
    public boolean deleteVar(String name) {
        return SampNativeFunction.deletePVar(id, name) != 0;
    }

    @Override
    public List<String> getVarNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < SampNativeFunction.getPVarsUpperIndex(id) + 1; i++) {
            names.add(SampNativeFunction.getPVarNameAtIndex(id, i));
        }
        return names;
    }

    @Override
    public PlayerVarType getVarType(String name) {
        return PlayerVarType.get(SampNativeFunction.getPVarType(id, name));
    }
}
