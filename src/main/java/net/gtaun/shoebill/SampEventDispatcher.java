/**
 * Copyright (C) 2011-2014 MK124
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

package net.gtaun.shoebill;

import net.gtaun.shoebill.amx.AmxCallEvent;
import net.gtaun.shoebill.amx.AmxHook;
import net.gtaun.shoebill.constant.*;
import net.gtaun.shoebill.data.*;
import net.gtaun.shoebill.event.actor.ActorStreamInEvent;
import net.gtaun.shoebill.event.actor.ActorStreamOutEvent;
import net.gtaun.shoebill.event.checkpoint.CheckpointEnterEvent;
import net.gtaun.shoebill.event.checkpoint.CheckpointLeaveEvent;
import net.gtaun.shoebill.event.checkpoint.RaceCheckpointEnterEvent;
import net.gtaun.shoebill.event.checkpoint.RaceCheckpointLeaveEvent;
import net.gtaun.shoebill.event.dialog.DialogCloseEvent.DialogCloseType;
import net.gtaun.shoebill.event.menu.MenuExitedEvent;
import net.gtaun.shoebill.event.menu.MenuSelectedEvent;
import net.gtaun.shoebill.event.object.ObjectMovedEvent;
import net.gtaun.shoebill.event.object.PlayerObjectMovedEvent;
import net.gtaun.shoebill.event.player.*;
import net.gtaun.shoebill.event.rcon.RconCommandEvent;
import net.gtaun.shoebill.event.rcon.RconLoginEvent;
import net.gtaun.shoebill.event.server.GameModeExitEvent;
import net.gtaun.shoebill.event.server.GameModeInitEvent;
import net.gtaun.shoebill.event.server.IncomingConnectionEvent;
import net.gtaun.shoebill.event.vehicle.*;
import net.gtaun.shoebill.object.*;
import net.gtaun.shoebill.object.PlayerAttach.PlayerAttachSlot;
import net.gtaun.shoebill.object.impl.*;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.util.event.EventManagerRoot;

import java.util.List;
import java.util.Set;

/**
 * @author MK124 & 123marvin123
 */
public class SampEventDispatcher implements SampCallbackHandler {
    private static SampEventDispatcher instance;
    private SampObjectManagerImpl sampObjectStore;
    private EventManagerRoot rootEventManager;
    private boolean active = true;
    private long lastProcessTimeMillis;

    public SampEventDispatcher(SampObjectManagerImpl store, EventManagerRoot manager) {
        sampObjectStore = store;
        rootEventManager = manager;
        instance = this;
        lastProcessTimeMillis = System.currentTimeMillis();
    }

    public static SampEventDispatcher getInstance() {
        return instance;
    }

    @Override
    public void onProcessTick() {
        try {
            long nowTick = System.currentTimeMillis();
            int interval = (int) (nowTick - lastProcessTimeMillis);
            lastProcessTimeMillis = nowTick;

            for (TimerImpl timer : sampObjectStore.getTimerImpls()) {
                timer.tick(interval);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onGameModeExit() {
        GameModeExitEvent exitEvent = new GameModeExitEvent();
        rootEventManager.dispatchEvent(exitEvent);
        return 1;
    }

    @Override
    public int onGameModeInit() {
        GameModeInitEvent initEvent = new GameModeInitEvent();
        rootEventManager.dispatchEvent(initEvent);
        return 1;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void onAmxLoad(int handle) {

    }

    @Override
    public void onAmxUnload(int handle) {

    }

    @Override
    public int onPlayerConnect(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerConnectEvent event = new PlayerConnectEvent(player);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerDisconnect(int playerId, int reason) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            if (player == null) return 0;
            DialogId dialog = player.getDialog();
            if (dialog != null) {
                DialogEventUtils.dispatchCloseEvent(rootEventManager, dialog, player, DialogCloseType.CANCEL);
            }

            PlayerDisconnectEvent event = new PlayerDisconnectEvent(player, reason);
            rootEventManager.dispatchEvent(event, player);

            sampObjectStore.setPlayer(playerId, null);
            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerSpawn(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerSpawnEvent event = new PlayerSpawnEvent(player);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerDeath(int playerId, int killerId, int reason) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player killer = null;

            if (killerId != Player.INVALID_ID) {
                killer = sampObjectStore.getPlayer(killerId);
                PlayerKillEvent event = new PlayerKillEvent(killer, player, reason);
                rootEventManager.dispatchEvent(event, killer);
            }

            PlayerDeathEvent event = new PlayerDeathEvent(player, killer, WeaponModel.get(reason));
            rootEventManager.dispatchEvent(event, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehicleSpawn(int vehicleId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleSpawnEvent event = new VehicleSpawnEvent(vehicle);
            rootEventManager.dispatchEvent(event, vehicle);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehicleDeath(int vehicleId, int killerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player killer = sampObjectStore.getPlayer(killerId);

            VehicleDeathEvent event = new VehicleDeathEvent(vehicle, killer);
            rootEventManager.dispatchEvent(event, vehicle, killer);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerText(int playerId, String text) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            PlayerTextEvent event = new PlayerTextEvent(player, text);
            rootEventManager.dispatchEvent(event, player);
            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerCommandText(int playerId, String cmdtext) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            PlayerCommandEvent event = new PlayerCommandEvent(player, cmdtext);
            rootEventManager.dispatchEvent(event, player);
            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerRequestClass(int playerId, int classId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerRequestClassEvent event = new PlayerRequestClassEvent(player, classId);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public int onPlayerEnterVehicle(int playerId, int vehicleId, int isPassenger) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player, isPassenger != 0);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerExitVehicle(int playerId, int vehicleId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleExitEvent event = new VehicleExitEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerStateChange(int playerId, int state, int oldState) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState);
            rootEventManager.dispatchEvent(event, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerEnterCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Checkpoint checkpoint = player.getCheckpoint();

            if (checkpoint == null) return 0;
            checkpoint.onEnter(player);

            CheckpointEnterEvent event = new CheckpointEnterEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerLeaveCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Checkpoint checkpoint = player.getCheckpoint();

            if (checkpoint == null) return 0;
            checkpoint.onLeave(player);

            CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerEnterRaceCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            RaceCheckpoint checkpoint = player.getRaceCheckpoint();

            if (checkpoint == null) return 0;
            checkpoint.onEnter(player);

            RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerLeaveRaceCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            RaceCheckpoint checkpoint = player.getRaceCheckpoint();

            if (checkpoint == null) return 0;
            checkpoint.onLeave(player);

            RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onRconCommand(String cmd) {
        try {
            RconCommandEvent event = new RconCommandEvent(cmd);
            rootEventManager.dispatchEvent(event, sampObjectStore.getServer());

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerRequestSpawn(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent(player);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onObjectMoved(int objectId) {
        try {
            SampObject object = sampObjectStore.getObject(objectId);

            ObjectMovedEvent event = new ObjectMovedEvent(object);
            rootEventManager.dispatchEvent(event, object);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerObjectMoved(int playerId, int objectId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            PlayerObject object = sampObjectStore.getPlayerObject(player, objectId);

            PlayerObjectMovedEvent event = new PlayerObjectMovedEvent(object);
            rootEventManager.dispatchEvent(event, object, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerPickUpPickup(int playerId, int pickupId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Pickup pickup = sampObjectStore.getPickup(pickupId);
            if (pickup == null) { // pickup with CreatePickup not found
                List<PickupImpl> staticPickups = sampObjectStore.getStaticPickups();
                if (staticPickups.size() - 1 >= pickupId) {
                    pickup = sampObjectStore.getStaticPickups().get(pickupId); //AddStaticPickup won't return a id. This is the only way to get it
                    if (pickup == null) // pickup with AddStaticPickup not found
                        return 0;
                } else return 0;
            }
            PlayerPickupEvent event = new PlayerPickupEvent(player, pickup);
            rootEventManager.dispatchEvent(event, pickup, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehicleMod(int playerId, int vehicleId, int componentId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleModEvent event = new VehicleModEvent(vehicle, componentId);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onEnterExitModShop(int playerId, int enterexit, int interiorId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerEnterExitModShopEvent event = new PlayerEnterExitModShopEvent(player, enterexit, interiorId);
            rootEventManager.dispatchEvent(event, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehiclePaintjob(int playerId, int vehicleId, int paintjobId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehiclePaintjobEvent event = new VehiclePaintjobEvent(vehicle, paintjobId);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehicleRespray(int playerId, int vehicleId, int color1, int color2) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleResprayEvent event = new VehicleResprayEvent(vehicle, color1, color2);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehicleDamageStatusUpdate(int vehicleId, int playerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(playerId);

            VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onUnoccupiedVehicleUpdate(int vehicleId, int playerId, int passengerSeat, float newX, float newY, float newZ, float vel_x, float vel_y, float vel_z) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(playerId);
            Location location = vehicle.getLocation();

            location.set(newX, newY, newZ);
            UnoccupiedVehicleUpdateEvent event = new UnoccupiedVehicleUpdateEvent(vehicle, player, location, new Vector3D(vel_x, vel_y, vel_z));
            rootEventManager.dispatchEvent(event, vehicle, player);
            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerSelectedMenuRow(int playerId, int row) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Menu menu = player.getCurrentMenu();

            MenuSelectedEvent event = new MenuSelectedEvent(menu, player, row);
            rootEventManager.dispatchEvent(event, menu, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerExitedMenu(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Menu menu = player.getCurrentMenu();

            MenuExitedEvent event = new MenuExitedEvent(menu, player);
            rootEventManager.dispatchEvent(event, menu, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public int onPlayerInteriorChange(int playerId, int interiorId, int oldInteriorId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerInteriorChangeEvent event = new PlayerInteriorChangeEvent(player, oldInteriorId);
            rootEventManager.dispatchEvent(event, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerKeyStateChange(int playerId, int keys, int oldKeys) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerKeyStateChangeEvent event = new PlayerKeyStateChangeEvent(player, new PlayerKeyStateImpl(player, oldKeys));
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onRconLoginAttempt(String ip, String password, int isSuccess) {
        try {
            RconLoginEvent event = new RconLoginEvent(ip, password, isSuccess != 0);
            rootEventManager.dispatchEvent(event);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerUpdate(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerUpdateEvent event = new PlayerUpdateEvent(player);
            rootEventManager.dispatchEvent(event, player);

            int ret = event.getResponse();
            if (ret == 0) return ret;

            Vehicle vehicle = player.getVehicle();
            if (player.getState() == PlayerState.DRIVER && vehicle != null) {
                VehicleUpdateEvent vehicleUpdateEvent = new VehicleUpdateEvent(vehicle, player, player.getVehicleSeat());
                rootEventManager.dispatchEvent(vehicleUpdateEvent, vehicle, player);
            }

            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerStreamIn(int playerId, int forPlayerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player forPlayer = sampObjectStore.getPlayer(forPlayerId);

            PlayerStreamInEvent event = new PlayerStreamInEvent(player, forPlayer);
            rootEventManager.dispatchEvent(event, player, forPlayer);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public int onPlayerStreamOut(int playerId, int forPlayerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player forPlayer = sampObjectStore.getPlayer(forPlayerId);

            PlayerStreamOutEvent event = new PlayerStreamOutEvent(player, forPlayer);
            rootEventManager.dispatchEvent(event, player, forPlayer);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehicleStreamIn(int vehicleId, int forPlayerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(forPlayerId);

            VehicleStreamInEvent event = new VehicleStreamInEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onVehicleStreamOut(int vehicleId, int forPlayerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(forPlayerId);

            VehicleStreamOutEvent event = new VehicleStreamOutEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onDialogResponse(int playerId, int dialogId, int response, int listitem, String inputtext) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            DialogId dialog = sampObjectStore.getDialog(dialogId);

            if (dialog == null) return 0;
            if (player != null && player instanceof PlayerImpl)
                ((PlayerImpl) player).setDialog(null);

            int ret = DialogEventUtils.dispatchResponseEvent(rootEventManager, dialog, player, response, listitem, inputtext);
            DialogEventUtils.dispatchCloseEvent(rootEventManager, dialog, player, DialogCloseType.RESPOND);
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerTakeDamage(int playerId, int issuerId, float amount, int weaponId, int bodypart) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player issuer = sampObjectStore.getPlayer(issuerId);

            PlayerTakeDamageEvent event = new PlayerTakeDamageEvent(player, issuer, amount, weaponId, bodypart);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player victim = sampObjectStore.getPlayer(damagedId);

            PlayerGiveDamageEvent event = new PlayerGiveDamageEvent(player, victim, amount, weaponId, bodypart);
            rootEventManager.dispatchEvent(event, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerClickMap(int playerId, float x, float y, float z) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerClickMapEvent event = new PlayerClickMapEvent(player, x, y, z);
            rootEventManager.dispatchEvent(event, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerClickTextDraw(int playerid, int clickedid) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            Textdraw textdraw = sampObjectStore.getTextdraw(clickedid);

            PlayerClickTextDrawEvent event = new PlayerClickTextDrawEvent(player, textdraw);
            rootEventManager.dispatchEvent(event, player, textdraw);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerClickPlayerTextDraw(int playerid, int playertextid) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            PlayerTextdraw textdraw = sampObjectStore.getPlayerTextdraw(player, playertextid);

            PlayerClickPlayerTextDrawEvent event = new PlayerClickPlayerTextDrawEvent(player, textdraw);
            rootEventManager.dispatchEvent(event, player, textdraw);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerClickPlayer(int playerId, int clickedPlayerId, int source) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player clickedPlayer = sampObjectStore.getPlayer(clickedPlayerId);

            PlayerClickPlayerEvent event = new PlayerClickPlayerEvent(player, clickedPlayer, source);
            rootEventManager.dispatchEvent(event, player, clickedPlayer);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            ObjectEditResponse editResponse = ObjectEditResponse.get(response);

            if (playerobject == 0) {
                SampObject object = sampObjectStore.getObject(objectid);

                Location newLocation = object.getLocation().clone();
                newLocation.x = fX;
                newLocation.y = fY;
                newLocation.z = fZ;

                Vector3D newRotation = new Vector3D(fRotX, fRotY, fRotZ);

                PlayerEditObjectEvent event = new PlayerEditObjectEvent(player, object, editResponse, newLocation, newRotation);
                rootEventManager.dispatchEvent(event, player, object);
            } else {
                PlayerObject object = sampObjectStore.getPlayerObject(player, objectid);

                Location newLocation = object.getLocation().clone();
                newLocation.x = fX;
                newLocation.y = fY;
                newLocation.z = fZ;

                Vector3D newRotation = new Vector3D(fRotX, fRotY, fRotZ);

                PlayerEditPlayerObjectEvent event = new PlayerEditPlayerObjectEvent(player, object, editResponse, newLocation, newRotation);
                rootEventManager.dispatchEvent(event, player, object);
            }

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            PlayerAttachSlot slot = player.getAttach().getSlotByBone(PlayerAttachBone.get(boneid));

            PlayerEditAttachedObjectEvent event = new PlayerEditAttachedObjectEvent(player, slot);
            rootEventManager.dispatchEvent(event, player);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);

            if (type == 0) {
                SampObject object = sampObjectStore.getObject(objectid);

                PlayerSelectObjectEvent event = new PlayerSelectObjectEvent(player, object);
                rootEventManager.dispatchEvent(event, player, object);
            } else {
                PlayerObject object = sampObjectStore.getPlayerObject(player, objectid);

                PlayerSelectPlayerObjectEvent event = new PlayerSelectPlayerObjectEvent(player, object);
                rootEventManager.dispatchEvent(event, player, object);
            }

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);

            PlayerWeaponShotEvent event = new PlayerWeaponShotEvent(player, weaponid, hittype, hitid, new Vector3D(fX, fY, fZ));
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onIncomingConnection(int playerid, String ipAddress, int port) {
        try {
            IncomingConnectionEvent event = new IncomingConnectionEvent(playerid, ipAddress, port);
            rootEventManager.dispatchEvent(event);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onTrailerUpdate(int playerid, int vehicleid) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleid);
            TrailerUpdateEvent event = new TrailerUpdateEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);
            return event.getResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxVehicleCreated(int vehicleid, int modelid, float x, float y, float z, float angle, int interiorid, int worldid, int color1, int color2, int respawn_delay) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleid);
            if (vehicle == null) {
                new VehicleImpl(rootEventManager, sampObjectStore, modelid, x, y, z, angle, interiorid, worldid, color1, color2, respawn_delay, false, vehicleid);
            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDestroyVehicle(int vehicleid) {
        try {
            Vehicle vehicle = Vehicle.get(vehicleid);
            if (vehicle instanceof VehicleImpl) {
                ((VehicleImpl) vehicle).destroyWithoutExec();

                return 1;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int onAmxSampObjectCreated(int object_id, int modelid, float x, float y, float z, float rX, float rY, float rZ, int world, int interior, float render_distance) {
        try {
            SampObject object = new SampObjectImpl(rootEventManager, sampObjectStore, modelid, new Location(x, y, z, interior, world), new Vector3D(rX, rY, rZ), render_distance, false, object_id);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSampObjectDestroyed(int object_id) {
        try {
            SampObject object = sampObjectStore.getObject(object_id);
            if (object != null && object instanceof SampObjectImpl) {
                ((SampObjectImpl) object).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxAttachObjectToVehicle(int object_id, int vehicleid, float x, float y, float z, float rX, float rY, float rZ) {
        try {
            SampObject objectImpl = SampObject.get(object_id);
            Vehicle vehicle = Vehicle.get(vehicleid);
            if (objectImpl != null && objectImpl instanceof SampObjectImpl && vehicle != null) {
                ((SampObjectImpl) objectImpl).attachWithoutExecution(vehicle, new Vector3D(x, y, z), new Vector3D(rX, rY, rZ));

                return 1;
            } else return 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxAttachObjectToPlayer(int object_id, int playerid, float x, float y, float z, float rX, float rY, float rZ) {
        try {
            SampObject objectImpl = SampObject.get(object_id);
            Player player = Player.get(playerid);
            if (objectImpl != null && objectImpl instanceof SampObjectImpl && player != null) {
                ((SampObjectImpl) objectImpl).attachWithoutExecution(player, new Vector3D(x, y, z), new Vector3D(rX, rY, rZ));

                return 1;
            } else return 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxAttachObjectToObject(int object_id, int other_object_id, float x, float y, float z, float rX, float rY, float rZ) {
        try {
            SampObject objectImpl = SampObject.get(object_id);
            SampObject otherObject = SampObject.get(other_object_id);
            if (objectImpl != null && objectImpl instanceof SampObjectImpl && otherObject != null) {
                ((SampObjectImpl) objectImpl).attachWithoutExecution(otherObject, new Vector3D(x, y, z), new Vector3D(rX, rY, rZ));

                return 1;
            } else return 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxCreatePlayerObject(int playerid, int modelid, float x, float y, float z, float rX, float rY, float rZ, int worldid, int interiorid, float drawdistance, int objectid) {
        try {
            Player player = Player.get(playerid);
            if (player == null) {
                return 0;
            } else {
                PlayerObjectImpl playerObject = new PlayerObjectImpl(rootEventManager, sampObjectStore, player, modelid, new Location(x, y, z, interiorid, worldid), new Vector3D(rX, rY, rZ), drawdistance, false, objectid);

                return 1;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDestroyPlayerObject(int playerid, int objectid) {
        try {
            Player player = Player.get(playerid);
            if (player == null) {
                return 0;
            } else {
                PlayerObject impl = PlayerObject.get(player, objectid);
                if (impl instanceof PlayerObjectImpl) {
                    ((PlayerObjectImpl) impl).destroyWithoutExec();

                }
                return 1;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetPlayerAttachedObject(int playerid, int index, int modelid, int bone, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, int materialcolor1, int materialcolor2) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            ((PlayerAttachImpl.PlayerAttachSlotImpl) player.getAttach().getSlot(index)).setWithoutExec(PlayerAttachBone.get(bone), modelid, new Vector3D(offsetX, offsetY, offsetZ), new Vector3D(rotX, rotY, rotZ), new Vector3D(scaleX, scaleY, scaleZ), materialcolor1, materialcolor2);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxRemovePlayerAttachedObject(int playerid, int index) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            ((PlayerAttachImpl.PlayerAttachSlotImpl) player.getAttach().getSlot(index)).removeWithoutExec();

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDestroyPickup(int pickupid) {
        try {
            Pickup pickup = Pickup.get(pickupid);
            if (pickup != null && pickup instanceof PickupImpl) {
                ((PickupImpl) pickup).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxCreatePickup(int model, int type, float posX, float posY, float posZ, int virtualworld, int id) {
        try {
            PickupImpl pickup = new PickupImpl(rootEventManager, sampObjectStore, model, type, new Location(posX, posY, posZ, virtualworld), false, id, false);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxAddStaticPickup(int model, int type, float posX, float posY, float posZ, int virtualworld) {
        try {
            PickupImpl pickup = new PickupImpl(rootEventManager, sampObjectStore, model, type, new Location(posX, posY, posZ, virtualworld), false, -1, true);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxCreateLabel(String text, int color, float posX, float posY, float posZ, float drawDistance, int virtualWorld, int testlos, int id) {
        try {
            boolean testLOS = (testlos == 1);
            LabelImpl label = new LabelImpl(rootEventManager, sampObjectStore, text, new Color(color), new Location(posX, posY, posZ, virtualWorld), drawDistance, testLOS, false, id);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDeleteLabel(int labelid) {
        try {
            Label label = Label.get(labelid);
            if (label != null && label instanceof LabelImpl) {
                ((LabelImpl) label).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxUpdateLabel(int labelid, int color, String text) {
        try {
            Label label = Label.get(labelid);
            if (label != null && label instanceof LabelImpl) {
                ((LabelImpl) label).updateWithoutExec(new Color(color), text);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxCreatePlayerLabel(int playerid, String text, int color, float posX, float posY, float posZ, float drawDistance, int attachedPlayer, int attachedVehicle, int testlos, int id) {
        try {
            PlayerLabelImpl playerLabel;
            Player player = Player.get(playerid);
            boolean testLOS = (testlos == 1);
            if (player == null) return 0;
            if (attachedPlayer == Player.INVALID_ID && attachedVehicle == Vehicle.INVALID_ID) {
                playerLabel = new PlayerLabelImpl(rootEventManager, sampObjectStore, player, text, new Color(color), new Location(posX, posY, posZ), drawDistance, testLOS, false, id);
            } else if (attachedPlayer != Player.INVALID_ID && attachedVehicle == Vehicle.INVALID_ID) {
                Player attachedPlayerInstance = Player.get(attachedPlayer);
                if (attachedPlayerInstance == null) return 0;
                playerLabel = new PlayerLabelImpl(rootEventManager, sampObjectStore, player, text, new Color(color), new Location(posX, posY, posZ), drawDistance, testLOS, attachedPlayerInstance, false, id);
            } else if (attachedPlayer == Player.INVALID_ID) {
                Vehicle vehicle = Vehicle.get(attachedVehicle);
                if (vehicle == null) return 0;
                playerLabel = new PlayerLabelImpl(rootEventManager, sampObjectStore, player, text, new Color(color), new Location(posX, posY, posZ), drawDistance, testLOS, vehicle, false, id);
            } else return 0;

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDeletePlayerLabel(int playerid, int labelid) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            PlayerLabel playerLabel = PlayerLabel.get(player, labelid);
            if (playerLabel != null && playerLabel instanceof PlayerLabelImpl) {
                ((PlayerLabelImpl) playerLabel).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxUpdatePlayerLabel(int playerid, int labelid, int color, String text) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            PlayerLabel playerLabel = PlayerLabel.get(player, labelid);
            if (playerLabel != null && playerLabel instanceof PlayerLabelImpl) {
                ((PlayerLabelImpl) playerLabel).updateWithoutExec(new Color(color), text);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxAttachLabelToPlayer(int labelid, int playerid, float offsetX, float offsetY, float offsetZ) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            Label label = Label.get(labelid);
            if (label != null && label instanceof LabelImpl) {
                ((LabelImpl) label).attachWithoutExec(player, offsetX, offsetY, offsetZ);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxAttachLabelToVehicle(int labelid, int vehicleid, float offsetX, float offsetY, float offsetZ) {
        try {
            Vehicle vehicle = Vehicle.get(vehicleid);
            if (vehicle == null) return 0;
            Label label = Label.get(labelid);
            if (label != null && label instanceof LabelImpl) {
                ((LabelImpl) label).attachWithoutExec(vehicle, offsetX, offsetY, offsetZ);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxCreateMenu(String title, int columns, float x, float y, float col1Width, float col2Width, int id) {
        try {
            MenuImpl menu = new MenuImpl(rootEventManager, sampObjectStore, title, columns, x, y, col1Width, col2Width, false, id);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetMenuColumnHeader(int id, int column, String text) {
        try {
            Menu menu = Menu.get(id);
            if (menu != null && menu instanceof MenuImpl) {
                ((MenuImpl) menu).setColumnHeaderWithoutExec(column, text);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDestroyMenu(int id) {
        try {
            Menu menu = Menu.get(id);
            if (menu != null && menu instanceof MenuImpl) {
                ((MenuImpl) menu).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneCreate(float minX, float minY, float maxX, float maxY, int id) {
        try {
            ZoneImpl zone = new ZoneImpl(sampObjectStore, rootEventManager, minX, minY, maxX, maxY, false, id);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneDestroy(int id) {
        try {
            Zone zone = Zone.get(id);
            if (zone != null && zone instanceof ZoneImpl) {
                ((ZoneImpl) zone).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneShowForPlayer(int playerid, int zone, int color) {
        try {
            Zone zoneInstance = Zone.get(zone);
            Player player = Player.get(playerid);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl && player != null) {
                ((ZoneImpl) zoneInstance).showWithoutExec(player);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneShowForAll(int zone, int color) {
        try {
            Zone zoneInstance = Zone.get(zone);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl) {
                ((ZoneImpl) zoneInstance).showForAllWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneHideForPlayer(int playerid, int zone) {
        try {
            Player player = Player.get(playerid);
            Zone zoneInstance = Zone.get(zone);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl && player != null) {
                ((ZoneImpl) zoneInstance).hideWithoutExec(player);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneHideForAll(int zone) {
        try {
            Zone zoneInstance = Zone.get(zone);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl) {
                ((ZoneImpl) zoneInstance).hideForAllWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneFlashForPlayer(int playerid, int zone, int flashColor) {
        try {
            Player player = Player.get(playerid);
            Zone zoneInstance = Zone.get(zone);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl && player != null) {
                ((ZoneImpl) zoneInstance).flashWithoutExec(player);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneFlashForAll(int zone, int flashColor) {
        try {
            Zone zoneInstance = Zone.get(zone);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl) {
                ((ZoneImpl) zoneInstance).flashForAllWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneStopFlashForPlayer(int playerid, int zone) {
        try {
            Zone zoneInstance = Zone.get(zone);
            Player player = Player.get(playerid);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl && player != null) {
                ((ZoneImpl) zoneInstance).stopFlashWithoutExec(player);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxGangZoneStopFlashForAll(int zone) {
        try {
            Zone zoneInstance = Zone.get(zone);
            if (zoneInstance != null && zoneInstance instanceof ZoneImpl) {
                ((ZoneImpl) zoneInstance).stopFlashForAllWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetSkillLevel(int playerid, int skill, int level) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player.getWeaponSkill() instanceof PlayerWeaponSkillImpl) {
                ((PlayerWeaponSkillImpl) player.getWeaponSkill()).setLevelWithoutExec(WeaponSkill.get(skill), level);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetPlayerMapIcon(int playerid, int iconid, float x, float y, float z, int markertype, int color, int style) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player.getMapIcon() instanceof PlayerMapIconImpl) {
                PlayerMapIcon.MapIcon icon = player.getMapIcon().createIcon(iconid);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxRemovePlayerMapIcon(int playerid, int iconid) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player.getMapIcon() instanceof PlayerMapIconImpl) {
                PlayerMapIcon.MapIcon mapIcon = player.getMapIcon().getIcon(iconid);
                if (mapIcon != null && mapIcon instanceof PlayerMapIconImpl.MapIconImpl) {
                    ((PlayerMapIconImpl.MapIconImpl) mapIcon).destroyWithoutExec();

                }
            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxShowPlayerDialog(int playerid, int dialogid, int style, String caption, String info, String button1, String button2) {
        try {
            if (sampObjectStore.getDialog(dialogid) != null) return 1;
            DialogIdImpl dialogId = new DialogIdImpl(null, dialogid, null, null, null);
            sampObjectStore.putDialog(dialogid, dialogId);
            sampObjectStore.addOccupiedDialogId(dialogid);
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).setDialog(dialogId);
            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetPlayerWorldBounds(int playerid, float minX, float minY, float maxX, float maxY) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).setWorldBoundWithoutExec(new Area(minX, minY, maxX, maxY));

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetPlayerWeather(int playerid, int weatherid) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).setWeatherWithoutExec(weatherid);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetPlayerCheckpoint(int playerid, float x, float y, float z, float size) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).setCheckpointWithoutExec(() -> new Radius(x, y, z, size));

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDisablePlayerCheckpoint(int playerid) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).disableCheckpointWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxSetPlayerRaceCheckpoint(int playerid, int type, float x, float y, float z, float nextX, float nextY, float nextZ, float size) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).setRaceCheckpointWithoutExec(new RaceCheckpoint() {
                    @Override
                    public RaceCheckpointType getType() {
                        return RaceCheckpointType.get(type);
                    }

                    @Override
                    public RaceCheckpoint getNext() {
                        if (x == 0 && y == 0 && z == 0)
                            return null;
                        else
                            return new RaceCheckpoint() {
                                @Override
                                public RaceCheckpointType getType() {
                                    return null;
                                }

                                @Override
                                public RaceCheckpoint getNext() {
                                    return null;
                                }

                                @Override
                                public Radius getLocation() {
                                    return new Radius(nextX, nextY, nextZ, size);
                                }
                            };
                    }

                    @Override
                    public Radius getLocation() {
                        return new Radius(x, y, z, size);
                    }
                });

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxDisablePlayerRaceCheckpoint(int playerid) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).disableRaceCheckpointWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTogglePlayerSpectating(int playerid, int toggle) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).toggleSpectatingWithoutExec((toggle == 1));

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxPlayerSpectatePlayer(int playerid, int target, int mode) {
        try {
            Player player = Player.get(playerid);
            Player targetPlayer = Player.get(target);
            if (player != null && player instanceof PlayerImpl && targetPlayer != null) {
                ((PlayerImpl) player).spectateWithoutExec(targetPlayer);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxPlayerSpectateVehicle(int playerid, int target, int mode) {
        try {
            Player player = Player.get(playerid);
            Vehicle targetVehicle = Vehicle.get(target);
            if (player != null && player instanceof PlayerImpl && targetVehicle != null) {
                ((PlayerImpl) player).spectateWithoutExec(targetVehicle);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxEnableStuntBonusForPlayer(int playerid, int toggle) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).enableStuntBonusWithoutExec((toggle == 1));

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxStartRecording(int playerid, int type, String recordName) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).startRecordingWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxStopRecording(int playerid) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).stopRecordingWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxToggleControllable(int playerid, int toggle) {
        try {
            Player player = Player.get(playerid);
            if (player != null && player instanceof PlayerImpl) {
                ((PlayerImpl) player).toggleControllableWithoutExec((toggle == 1));

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTextDrawCreate(float x, float y, String text, int id) {
        try {
            TextdrawImpl textdraw = new TextdrawImpl(rootEventManager, sampObjectStore, x, y, text, false, id);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTextDrawDestroy(int id) {
        try {
            Textdraw textdraw = Textdraw.get(id);
            if (textdraw != null && textdraw instanceof TextdrawImpl) {
                ((TextdrawImpl) textdraw).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTextDrawSetString(int id, String text) {
        try {
            Textdraw textdraw = Textdraw.get(id);
            if (textdraw != null && textdraw instanceof TextdrawImpl) {
                ((TextdrawImpl) textdraw).setTextWithoutExec(text);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTextDrawShowForPlayer(int playerid, int id) {
        try {
            Textdraw textdraw = Textdraw.get(id);
            Player player = Player.get(playerid);
            if (textdraw != null && textdraw instanceof TextdrawImpl && player != null) {
                ((TextdrawImpl) textdraw).showWithoutExec(player);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTextDrawHideForPlayer(int playerid, int id) {
        try {
            Textdraw textdraw = Textdraw.get(id);
            Player player = Player.get(playerid);
            if (textdraw != null && textdraw instanceof TextdrawImpl && player != null) {
                ((TextdrawImpl) textdraw).hideWithoutExec(player);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTextDrawShowForAll(int id) {
        try {
            Textdraw textdraw = Textdraw.get(id);
            if (textdraw != null && textdraw instanceof TextdrawImpl) {
                ((TextdrawImpl) textdraw).showForAllWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxTextDrawHideForAll(int id) {
        try {
            Textdraw textdraw = Textdraw.get(id);
            if (textdraw != null && textdraw instanceof TextdrawImpl) {
                ((TextdrawImpl) textdraw).hideForAllWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxCreatePlayerTextDraw(int playerid, float x, float y, String text, int id) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            PlayerTextdrawImpl playerTextdraw = new PlayerTextdrawImpl(rootEventManager, sampObjectStore, player, x, y, text, false, id);

            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxPlayerTextDrawDestroy(int playerid, int id) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            PlayerTextdraw playerTextdraw = PlayerTextdraw.get(player, id);
            if (playerTextdraw != null && playerTextdraw instanceof PlayerTextdrawImpl) {
                ((PlayerTextdrawImpl) playerTextdraw).destroyWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxPlayerTextDrawSetString(int playerid, int id, String text) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            PlayerTextdraw playerTextdraw = PlayerTextdraw.get(player, id);
            if (playerTextdraw != null && playerTextdraw instanceof PlayerTextdrawImpl) {
                ((PlayerTextdrawImpl) playerTextdraw).setTextWithoutExec(text);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxPlayerTextDrawShow(int playerid, int id) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            PlayerTextdraw playerTextdraw = PlayerTextdraw.get(player, id);
            if (playerTextdraw != null && playerTextdraw instanceof PlayerTextdrawImpl) {
                ((PlayerTextdrawImpl) playerTextdraw).showWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxPlayerTextDrawHide(int playerid, int id) {
        try {
            Player player = Player.get(playerid);
            if (player == null) return 0;
            PlayerTextdraw playerTextdraw = PlayerTextdraw.get(player, id);
            if (playerTextdraw != null && playerTextdraw instanceof PlayerTextdrawImpl) {
                ((PlayerTextdrawImpl) playerTextdraw).hideWithoutExec();

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxAddVehicleComponent(int vehicleid, int componentid) {
        try {
            Vehicle vehicle = Vehicle.get(vehicleid);
            if (vehicle != null && vehicle.getComponent() instanceof VehicleComponentImpl) {
                ((VehicleComponentImpl) vehicle.getComponent()).addWithoutExec(componentid);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxLinkVehicleToInterior(int vehicleid, int interiorid) {
        try {
            Vehicle vehicle = Vehicle.get(vehicleid);
            if (vehicle != null && vehicle instanceof VehicleImpl) {
                ((VehicleImpl) vehicle).setInteriorIdWithoutExec(interiorid);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int onAmxChangeVehicleColor(int vehicleid, int color1, int color2) {
        try {
            Vehicle vehicle = Vehicle.get(vehicleid);
            if (vehicle != null && vehicle instanceof VehicleImpl) {
                ((VehicleImpl) vehicle).setColorWithoutExec(color1, color2);

            }
            return 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int[] onHookCall(String name, Object... objects) {
        AmxCallEvent event = new AmxCallEvent(name, objects);
        try {
            Set<AmxHook> hooks = Shoebill.get().getAmxInstanceManager().getAmxHooks();
            hooks.stream().filter(hook -> hook.getName().equals(name)).forEach(hook -> hook.getOnCall().accept(event));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new int[]{event.getReturnValue(), event.isDisallow()};
    }

    @Override
    public int onActorStreamIn(int actor, int playerid) {
        try {
            Actor actorObject = Actor.get(actor);
            Player player = Player.get(playerid);
            ActorStreamInEvent event = new ActorStreamInEvent(actorObject, player);
            rootEventManager.dispatchEvent(event, actorObject, player);
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public int onActorStreamOut(int actor, int playerid) {
        try {
            Actor actorObject = Actor.get(actor);
            Player player = Player.get(playerid);
            ActorStreamOutEvent event = new ActorStreamOutEvent(actorObject, player);
            rootEventManager.dispatchEvent(event, actorObject, player);
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public int onPlayerGiveDamageActor(int playerid, int actor, int amount, int weapon, int bodypart) {
        try {
            Player player = Player.get(playerid);
            Actor actorObject = Actor.get(actor);
            WeaponModel model = WeaponModel.get(weapon);
            PlayerDamageActorEvent event = new PlayerDamageActorEvent(player, actorObject, amount, model, bodypart);
            rootEventManager.dispatchEvent(event, player, actorObject, model);
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public int onVehicleSirenStateChange(int playerid, int vehicleid, int newstate) {
        try {
            Player player = Player.get(playerid);
            Vehicle vehicle = Vehicle.get(vehicleid);
            VehicleSirenStateChangeEvent event = new VehicleSirenStateChangeEvent(vehicle, player, newstate > 0);
            rootEventManager.dispatchEvent(event, vehicle, player, newstate > 0);
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public int onAmxCreateActor(int id, int modelid, float x, float y, float z, float rotation) {
        try {
            ActorImpl actor = new ActorImpl(modelid, new Vector3D(x, y, z), rotation, false, id);
            sampObjectStore.setActor(id, actor);
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public int onAmxDestroyActor(int id) {
        try {
            Actor actor = sampObjectStore.getActor(id);
            if (actor != null && actor instanceof ActorImpl) {
                ((ActorImpl) actor).destroyWithoutExec();
                sampObjectStore.setActor(id, null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public void executeWithoutEvent(Runnable func) {
        this.active = false;
        func.run();
        this.active = true;
    }
}
