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
import net.gtaun.shoebill.amx.AmxInstance;
import net.gtaun.shoebill.amx.AmxInstanceManager;
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
import net.gtaun.util.event.EventHandler;
import net.gtaun.util.event.EventManagerRoot;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author MK124 & 123marvin123
 */
public class SampEventDispatcher implements SampCallbackHandler {
    private static SampEventDispatcher instance;
    private SampObjectManagerImpl sampObjectStore;
    private EventManagerRoot rootEventManager;
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
            Collection<TimerImpl> timerCollection = sampObjectStore.getTimerImpls();
            if (timerCollection.size() > 0) {
                long nowTick = System.currentTimeMillis();
                int interval = (int) (nowTick - lastProcessTimeMillis);
                lastProcessTimeMillis = nowTick;

                for (TimerImpl timer : timerCollection) {
                    timer.tick(interval);
                }
            } else lastProcessTimeMillis = System.currentTimeMillis();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onGameModeExit() {
        GameModeExitEvent exitEvent = new GameModeExitEvent();
        rootEventManager.dispatchEvent(exitEvent);
        return true;
    }

    @Override
    public boolean onGameModeInit() {
        GameModeInitEvent initEvent = new GameModeInitEvent();
        rootEventManager.dispatchEvent(initEvent);
        return true;
    }

    @Override
    public void onAmxLoad(int handle) {

    }

    @Override
    public void onAmxUnload(int handle) {

    }

    @Override
    public boolean onPlayerConnect(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerConnectEvent event = new PlayerConnectEvent(player);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerDisconnect(int playerId, int reason) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            if (player == null) return false;
            DialogId dialog = player.getDialog();
            if (dialog != null) {
                DialogEventUtils.dispatchCloseEvent(rootEventManager, dialog, player, DialogCloseType.CANCEL);
            }

            //Destroy all PlayerTextdraws related to the player when disconnecting
            PlayerTextdraw.get(player).forEach(Destroyable::destroy);

            PlayerDisconnectEvent event = new PlayerDisconnectEvent(player, reason);
            rootEventManager.dispatchEvent(event, player);

            sampObjectStore.setPlayer(playerId, null);
            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public boolean onPlayerSpawn(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerSpawnEvent event = new PlayerSpawnEvent(player);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerDeath(int playerId, int killerId, int reason) {
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

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehicleSpawn(int vehicleId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleSpawnEvent event = new VehicleSpawnEvent(vehicle);
            rootEventManager.dispatchEvent(event, vehicle);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehicleDeath(int vehicleId, int killerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player killer = sampObjectStore.getPlayer(killerId);

            VehicleDeathEvent event = new VehicleDeathEvent(vehicle, killer);
            rootEventManager.dispatchEvent(event, vehicle, killer);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
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
    public boolean onPlayerRequestClass(int playerId, int classId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerRequestClassEvent event = new PlayerRequestClassEvent(player, classId);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean onPlayerEnterVehicle(int playerId, int vehicleId, int isPassenger) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player, isPassenger != 0);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerExitVehicle(int playerId, int vehicleId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleExitEvent event = new VehicleExitEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerStateChange(int playerId, int state, int oldState) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState);
            rootEventManager.dispatchEvent(event, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerEnterCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Checkpoint checkpoint = player.getCheckpoint();

            if (checkpoint == null) return false;
            checkpoint.onEnter(player);

            CheckpointEnterEvent event = new CheckpointEnterEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerLeaveCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Checkpoint checkpoint = player.getCheckpoint();

            if (checkpoint == null) return false;
            checkpoint.onLeave(player);

            CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerEnterRaceCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            RaceCheckpoint checkpoint = player.getRaceCheckpoint();

            if (checkpoint == null) return false;
            checkpoint.onEnter(player);

            RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerLeaveRaceCheckpoint(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            RaceCheckpoint checkpoint = player.getRaceCheckpoint();

            if (checkpoint == null) return false;
            checkpoint.onLeave(player);

            RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
            rootEventManager.dispatchEvent(event, checkpoint, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
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
    public boolean onPlayerRequestSpawn(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent(player);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onObjectMoved(int objectId) {
        try {
            SampObject object = sampObjectStore.getObject(objectId);

            ObjectMovedEvent event = new ObjectMovedEvent(object);
            rootEventManager.dispatchEvent(event, object);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerObjectMoved(int playerId, int objectId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            PlayerObject object = sampObjectStore.getPlayerObject(player, objectId);

            PlayerObjectMovedEvent event = new PlayerObjectMovedEvent(object);
            rootEventManager.dispatchEvent(event, object, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerPickUpPickup(int playerId, int pickupId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Pickup pickup = sampObjectStore.getPickup(pickupId);
            if (pickup == null) { // pickup with CreatePickup not found
                List<PickupImpl> staticPickups = sampObjectStore.getStaticPickups();
                if (staticPickups.size() - 1 >= pickupId) {
                    pickup = sampObjectStore.getStaticPickups().get(pickupId); //AddStaticPickup won't return a id. This is the only way to get it
                    if (pickup == null) // pickup with AddStaticPickup not found
                        return false;
                } else return false;
            }

            PlayerPickupEvent event = new PlayerPickupEvent(player, pickup);

            if (pickup instanceof PickupImpl) {
                PickupImpl pickupImpl = (PickupImpl) pickup;
                EventHandler<PlayerPickupEvent> handler = pickupImpl.getPickupHandler();
                if (handler != null) handler.handleEvent(event);
            }
            rootEventManager.dispatchEvent(event, pickup, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehicleMod(int playerId, int vehicleId, int componentId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleModEvent event = new VehicleModEvent(vehicle, componentId);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onEnterExitModShop(int playerId, int enterexit, int interiorId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerEnterExitModShopEvent event = new PlayerEnterExitModShopEvent(player, enterexit, interiorId);
            rootEventManager.dispatchEvent(event, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehiclePaintjob(int playerId, int vehicleId, int paintjobId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehiclePaintjobEvent event = new VehiclePaintjobEvent(vehicle, paintjobId);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehicleRespray(int playerId, int vehicleId, int color1, int color2) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);

            VehicleResprayEvent event = new VehicleResprayEvent(vehicle, color1, color2);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehicleDamageStatusUpdate(int vehicleId, int playerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(playerId);

            VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onUnoccupiedVehicleUpdate(int vehicleId, int playerId, int passengerSeat, float newX, float newY, float newZ, float vel_x, float vel_y, float vel_z) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(playerId);
            Location location = vehicle.getLocation();

            location.set(newX, newY, newZ);
            UnoccupiedVehicleUpdateEvent event = new UnoccupiedVehicleUpdateEvent(vehicle, player, location, new Vector3D(vel_x, vel_y, vel_z));
            rootEventManager.dispatchEvent(event, vehicle, player);
            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerSelectedMenuRow(int playerId, int row) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Menu menu = player.getCurrentMenu();

            MenuSelectedEvent event = new MenuSelectedEvent(menu, player, row);
            rootEventManager.dispatchEvent(event, menu, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerExitedMenu(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Menu menu = player.getCurrentMenu();

            MenuExitedEvent event = new MenuExitedEvent(menu, player);
            rootEventManager.dispatchEvent(event, menu, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean onPlayerInteriorChange(int playerId, int interiorId, int oldInteriorId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerInteriorChangeEvent event = new PlayerInteriorChangeEvent(player, oldInteriorId);
            rootEventManager.dispatchEvent(event, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerKeyStateChange(int playerId, int keys, int oldKeys) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerKeyStateChangeEvent event = new PlayerKeyStateChangeEvent(player, new PlayerKeyStateImpl(player, oldKeys));
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
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
    public boolean onPlayerUpdate(int playerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerUpdateEvent event = new PlayerUpdateEvent(player);
            rootEventManager.dispatchEvent(event, player);

            boolean ret = event.getResponse() > 0;
            if (!ret) return false;

            Vehicle vehicle = player.getVehicle();
            if (player.getState() == PlayerState.DRIVER && vehicle != null) {
                VehicleUpdateEvent vehicleUpdateEvent = new VehicleUpdateEvent(vehicle, player, player.getVehicleSeat());
                rootEventManager.dispatchEvent(vehicleUpdateEvent, vehicle, player);
            }

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerStreamIn(int playerId, int forPlayerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player forPlayer = sampObjectStore.getPlayer(forPlayerId);

            PlayerStreamInEvent event = new PlayerStreamInEvent(player, forPlayer);
            rootEventManager.dispatchEvent(event, player, forPlayer);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean onPlayerStreamOut(int playerId, int forPlayerId) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player forPlayer = sampObjectStore.getPlayer(forPlayerId);

            PlayerStreamOutEvent event = new PlayerStreamOutEvent(player, forPlayer);
            rootEventManager.dispatchEvent(event, player, forPlayer);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehicleStreamIn(int vehicleId, int forPlayerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(forPlayerId);

            VehicleStreamInEvent event = new VehicleStreamInEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onVehicleStreamOut(int vehicleId, int forPlayerId) {
        try {
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleId);
            Player player = sampObjectStore.getPlayer(forPlayerId);

            VehicleStreamOutEvent event = new VehicleStreamOutEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
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
    public boolean onPlayerTakeDamage(int playerId, int issuerId, float amount, int weaponId, int bodypart) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player issuer = sampObjectStore.getPlayer(issuerId);

            PlayerTakeDamageEvent event = new PlayerTakeDamageEvent(player, issuer, amount, weaponId, bodypart);
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player victim = sampObjectStore.getPlayer(damagedId);

            PlayerGiveDamageEvent event = new PlayerGiveDamageEvent(player, victim, amount, weaponId, bodypart);
            rootEventManager.dispatchEvent(event, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerClickMap(int playerId, float x, float y, float z) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);

            PlayerClickMapEvent event = new PlayerClickMapEvent(player, x, y, z);
            rootEventManager.dispatchEvent(event, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerClickTextDraw(int playerid, int clickedid) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            Textdraw textdraw = sampObjectStore.getTextdraw(clickedid);

            PlayerClickTextDrawEvent event = new PlayerClickTextDrawEvent(player, textdraw);
            rootEventManager.dispatchEvent(event, player, textdraw);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerClickPlayerTextDraw(int playerid, int playertextid) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            PlayerTextdraw textdraw = sampObjectStore.getPlayerTextdraw(player, playertextid);

            PlayerClickPlayerTextDrawEvent event = new PlayerClickPlayerTextDrawEvent(player, textdraw);
            rootEventManager.dispatchEvent(event, player, textdraw);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerClickPlayer(int playerId, int clickedPlayerId, int source) {
        try {
            Player player = sampObjectStore.getPlayer(playerId);
            Player clickedPlayer = sampObjectStore.getPlayer(clickedPlayerId);

            PlayerClickPlayerEvent event = new PlayerClickPlayerEvent(player, clickedPlayer, source);
            rootEventManager.dispatchEvent(event, player, clickedPlayer);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
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
    public boolean onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            PlayerAttachSlot slot = player.getAttach().getSlotByBone(PlayerAttachBone.get(boneid));

            PlayerEditAttachedObjectEvent event = new PlayerEditAttachedObjectEvent(player, slot, response == 1,
                    new Vector3D(fOffsetX, fOffsetY, fOffsetZ),
                    new Vector3D(fRotX, fRotY, fRotZ),
                    new Vector3D(fScaleX, fScaleY, fScaleZ));
            rootEventManager.dispatchEvent(event, player);

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ) {
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

            return false;
        } catch (Throwable e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public boolean onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);

            PlayerWeaponShotEvent event = new PlayerWeaponShotEvent(player, weaponid, hittype, hitid, new Vector3D(fX, fY, fZ));
            rootEventManager.dispatchEvent(event, player);

            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
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
    public boolean onTrailerUpdate(int playerid, int vehicleid) {
        try {
            Player player = sampObjectStore.getPlayer(playerid);
            Vehicle vehicle = sampObjectStore.getVehicle(vehicleid);
            TrailerUpdateEvent event = new TrailerUpdateEvent(vehicle, player);
            rootEventManager.dispatchEvent(event, vehicle, player);
            return event.getResponse() > 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
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
    public boolean onActorStreamIn(int actor, int playerid) {
        try {
            Actor actorObject = Actor.get(actor);
            Player player = Player.get(playerid);
            ActorStreamInEvent event = new ActorStreamInEvent(actorObject, player);
            rootEventManager.dispatchEvent(event, actorObject, player);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean onActorStreamOut(int actor, int playerid) {
        try {
            Actor actorObject = Actor.get(actor);
            Player player = Player.get(playerid);
            ActorStreamOutEvent event = new ActorStreamOutEvent(actorObject, player);
            rootEventManager.dispatchEvent(event, actorObject, player);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
    public int onRegisteredFunctionCall(int amx, String name, Object[] parameters) {
        try {
            int returnValue = 0;
            for(AmxInstance instance : AmxInstanceManager.get().getAmxInstances()) {
                if(instance.getHandle() != amx) continue;
                if(!instance.hasRegisteredFunction(name)) continue;
                returnValue = instance.callRegisteredFunction(name, parameters);
            }
            return returnValue;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }
}
