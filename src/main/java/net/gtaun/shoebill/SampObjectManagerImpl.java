/**
 * Copyright (C) 2012-2014 MK124
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

import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.event.player.PlayerPickupEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.*;
import net.gtaun.shoebill.object.DialogId.OnCloseHandler;
import net.gtaun.shoebill.object.DialogId.OnResponseHandler;
import net.gtaun.shoebill.object.DialogId.OnShowHandler;
import net.gtaun.shoebill.object.Timer.TimerCallback;
import net.gtaun.shoebill.object.impl.*;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.util.event.Attentions;
import net.gtaun.util.event.EventHandler;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.HandlerPriority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author MK124 & 123marvin123
 */
public class SampObjectManagerImpl extends SampObjectStoreImpl implements SampObjectManager {
    private static final int MAX_DIALOG_ID = 32767;

    private List<Integer> occupiedDialogIds;
    private SampCallbackHandler callbackHandler;
    private Random random;

    public SampObjectManagerImpl(EventManager eventManager) {
        super(eventManager);
        initialize();
        random = new Random();
        callbackHandler = new SampCallbackHandler() {
            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public int onPlayerConnect(int playerid) {
                try {
                    Player player = createPlayer(playerid);
                    setPlayer(playerid, player);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                return 1;
            }

            @Override
            public int onPlayerDisconnect(int playerid, int reason) {
                return 1;
            }
        };
    }

    private void initialize() {
        occupiedDialogIds = new ArrayList<>();

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(Vehicle.class), (e) ->
        {
            Vehicle vehicle = (Vehicle) e.getDestroyable();
            super.setVehicle(vehicle.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(PlayerObject.class), (e) ->
        {
            PlayerObject object = (PlayerObject) e.getDestroyable();
            super.setPlayerObject(object.getPlayer(), object.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(SampObject.class), (e) ->
        {
            SampObject object = (SampObject) e.getDestroyable();
            if (object instanceof PlayerObject) return;
            super.setObject(object.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(Pickup.class), (e) ->
        {
            Pickup pickup = (Pickup) e.getDestroyable();
            super.setPickup(pickup.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(Label.class), (e) ->
        {
            Label label = (Label) e.getDestroyable();
            if (label instanceof PlayerLabel) return;
            super.setLabel(label.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(PlayerLabel.class), (e) ->
        {
            PlayerLabel label = (PlayerLabel) e.getDestroyable();
            super.setPlayerLabel(label.getPlayer(), label.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(Textdraw.class), (e) ->
        {
            Textdraw textdraw = (Textdraw) e.getDestroyable();
            if (textdraw instanceof PlayerTextdraw) return;
            super.setTextdraw(textdraw.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(PlayerTextdraw.class), (e) ->
        {
            PlayerTextdraw textdraw = (PlayerTextdraw) e.getDestroyable();
            super.setPlayerTextdraw(textdraw.getPlayer(), textdraw.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(Zone.class), (e) ->
        {
            Zone zone = (Zone) e.getDestroyable();
            super.setZone(zone.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(Menu.class), (e) ->
        {
            Menu menu = (Menu) e.getDestroyable();
            super.setMenu(menu.getId(), null);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(DialogId.class), (e) ->
        {
            DialogId dialog = (DialogId) e.getDestroyable();
            super.removeDialog(dialog);
            int dialogId = dialog.getId();
            recycleDialogId(dialogId);
        });

        eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, Attentions.create().clazz(Timer.class), (e) ->
        {
            Timer timer = (Timer) e.getDestroyable();
            super.removeTimer(timer);
        });

        createServer();
        createWorld();
    }

    public SampCallbackHandler getCallbackHandler() {
        return callbackHandler;
    }

    private World createWorld() {
        try {
            World world = new WorldImpl(this);
            super.setWorld(world);
            return world;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    private Server createServer() {
        try {
            Server server = new ServerImpl(this);
            super.setServer(server);
            return server;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    public Player createPlayer(int playerId) throws UnsupportedOperationException {
        try {
            Player player = new PlayerImpl(eventManagerNode, this, playerId);
            super.setPlayer(playerId, player);
            return player;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Vehicle createVehicle(int modelId, AngledLocation loc, int color1, int color2, int respawnDelay, boolean addsiren) throws CreationFailedException {
        try {
            return new VehicleImpl(eventManagerNode, this, modelId, loc, color1, color2, respawnDelay, true, -1, addsiren);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public SampObject createObject(int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException {
        try {
            return new SampObjectImpl(eventManagerNode, this, modelId, loc, rot, drawDistance);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public PlayerObject createPlayerObject(Player player, int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException {
        try {
            if (!player.isOnline()) throw new CreationFailedException();

            return new PlayerObjectImpl(eventManagerNode, this, player, modelId, loc, rot, drawDistance);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Pickup createPickup(int modelId, int type, Location loc) throws CreationFailedException {
        try {
            return new PickupImpl(eventManagerNode, this, modelId, type, loc);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Pickup createPickup(int modelId, int type, Location loc, EventHandler<PlayerPickupEvent> event) throws CreationFailedException {
        try {
            Pickup pickup = createPickup(modelId, type, loc);
            eventManagerNode.registerHandler(PlayerPickupEvent.class, HandlerPriority.NORMAL, Attentions.create().object(pickup), event);
            return pickup;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Label createLabel(String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException {
        try {
            return new LabelImpl(eventManagerNode, this, text, color, loc, drawDistance, testLOS);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException {
        try {
            if (!player.isOnline()) return null;
            return new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Player attachedPlayer) throws CreationFailedException {
        try {
            if (!player.isOnline()) throw new CreationFailedException();

            return new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS, attachedPlayer);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Vehicle attachedVehicle) throws CreationFailedException {
        try {
            if (!player.isOnline()) throw new CreationFailedException();

            return new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS, attachedVehicle);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Textdraw createTextdraw(float x, float y, String text) throws CreationFailedException {
        try {
            return new TextdrawImpl(eventManagerNode, this, x, y, text);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public PlayerTextdraw createPlayerTextdraw(Player player, float x, float y, String text) throws CreationFailedException {
        try {
            if (!player.isOnline()) throw new CreationFailedException();

            return new PlayerTextdrawImpl(eventManagerNode, this, player, x, y, text);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Zone createZone(float minX, float minY, float maxX, float maxY) throws CreationFailedException {
        try {
            return new ZoneImpl(this, eventManagerNode, minX, minY, maxX, maxY);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Menu createMenu(String title, int columns, float x, float y, float col1Width, float col2Width) throws CreationFailedException {
        try {
            return new MenuImpl(eventManagerNode, this, title, columns, x, y, col1Width, col2Width);
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    private int allocateDialogId() {
        Integer dialogId = random.nextInt(MAX_DIALOG_ID);
        int tries = 0;
        while (occupiedDialogIds.contains(dialogId) && tries < 20) {
            dialogId = random.nextInt(MAX_DIALOG_ID);
            tries++;
        }
        addOccupiedDialogId(dialogId);
        return dialogId;
    }

    public void addOccupiedDialogId(int dialogId) {
        if (occupiedDialogIds.contains(dialogId)) return;
        occupiedDialogIds.add(dialogId);
    }

    private void recycleDialogId(int dialogId) {
        if (occupiedDialogIds.contains(dialogId)) {
            Iterator<Integer> iterator = occupiedDialogIds.iterator();
            while (iterator.hasNext()) {
                int id = iterator.next();
                if (id == dialogId) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public DialogId createDialogId(OnResponseHandler onResponse, OnShowHandler onShow, OnCloseHandler onClose) throws CreationFailedException {
        try {
            Integer dialogId = allocateDialogId();
            DialogId dialog = new DialogIdImpl(eventManagerNode, dialogId, onResponse, onShow, onClose);
            super.putDialog(dialog.getId(), dialog);
            return dialog;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Timer createTimer(int interval, int count, TimerCallback callback) {
        try {
            TimerImpl timer = new TimerImpl(eventManagerNode, interval, count, callback);
            super.putTimer(timer);
            return timer;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public Actor createActor(int modelid, Vector3D position, float angle) throws CreationFailedException {
        try {
            ActorImpl actor = new ActorImpl(modelid, position, angle);
            super.setActor(actor.getId(), actor);
            return actor;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }
}
