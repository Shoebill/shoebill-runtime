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
import net.gtaun.shoebill.event.dialog.DialogCloseEvent;
import net.gtaun.shoebill.event.dialog.DialogResponseEvent;
import net.gtaun.shoebill.event.dialog.DialogShowEvent;
import net.gtaun.shoebill.event.player.PlayerPickupEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.*;
import net.gtaun.shoebill.object.Timer.TimerCallback;
import net.gtaun.shoebill.object.impl.*;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.util.event.Attentions;
import net.gtaun.util.event.EventHandler;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.HandlerPriority;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author MK124 & 123marvin123
 */
public class SampObjectManagerImpl extends SampObjectStoreImpl implements SampObjectManager
{
    private static final int MAX_DIALOG_ID = 32767;

	private int allocatedDialogId = MAX_DIALOG_ID / 2;
	private Queue<Integer> recycledDialogIds = new LinkedList<>();
	private Set<Integer> occupiedDialogIds = new TreeSet<>();
    private SampCallbackHandler callbackHandler;


    public SampObjectManagerImpl(EventManager eventManager)
	{
        super(eventManager);
        init();

        callbackHandler = new SampCallbackHandler() {
            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public int onPlayerConnect(int playerid) {
                try {
                    PlayerImpl player = createPlayer(playerid);
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

    private void init() {
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

    private WorldImpl createWorld() {
        WorldImpl world = new WorldImpl(this);
        super.setWorld(world);
        return world;
    }

    private ServerImpl createServer() {
        ServerImpl server = new ServerImpl(this);
        super.setServer(server);
        return server;
    }

    public PlayerImpl createPlayer(int playerId) throws UnsupportedOperationException {
        PlayerImpl player = new PlayerImpl(eventManagerNode, this, playerId);
        super.setPlayer(playerId, player);
        return player;
    }

    @Override
    public VehicleImpl createVehicle(int modelId, AngledLocation loc, int color1, int color2, int respawnDelay, boolean addsiren) throws CreationFailedException {
        return new VehicleImpl(eventManagerNode, this, modelId, loc, color1, color2, respawnDelay, true, -1, addsiren);
    }

    @Override
    public SampObjectImpl createObject(int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException {
        return new SampObjectImpl(eventManagerNode, this, modelId, loc, rot, drawDistance);
    }

    @Override
    public PlayerObjectImpl createPlayerObject(Player player, int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException {
        if (!player.isOnline()) throw new CreationFailedException();
        return new PlayerObjectImpl(eventManagerNode, this, player, modelId, loc, rot, drawDistance);
    }

    @Override
    public PickupImpl createPickup(int modelId, int type, Location loc) throws CreationFailedException {
        return new PickupImpl(eventManagerNode, this, modelId, type, loc, null);
    }

    @Override
    public PickupImpl createPickup(int modelId, int type, Location loc, EventHandler<PlayerPickupEvent> handler) throws CreationFailedException {
		return new PickupImpl(eventManagerNode, this, modelId, type, loc, handler);
    }

    @Override
    public LabelImpl createLabel(String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException {
        return new LabelImpl(eventManagerNode, this, text, color, loc, drawDistance, testLOS);
    }

    @Override
    public PlayerLabelImpl createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException {
        if (!player.isOnline()) return null;
        return new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS);
    }

    @Override
    public PlayerLabelImpl createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Player attachedPlayer) throws CreationFailedException {
        if (!player.isOnline()) throw new CreationFailedException();
        return new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS, attachedPlayer);
    }

    @Override
    public PlayerLabelImpl createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Vehicle attachedVehicle) throws CreationFailedException {
        if (!player.isOnline()) throw new CreationFailedException();
        return new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS, attachedVehicle);
    }

    @Override
    public TextdrawImpl createTextdraw(float x, float y, String text) throws CreationFailedException {
        return new TextdrawImpl(eventManagerNode, this, x, y, text);
    }

    @Override
    public PlayerTextdrawImpl createPlayerTextdraw(Player player, float x, float y, String text) throws CreationFailedException {
        if (!player.isOnline()) throw new CreationFailedException();
        return new PlayerTextdrawImpl(eventManagerNode, this, player, x, y, text);
    }

    @Override
    public ZoneImpl createZone(float minX, float minY, float maxX, float maxY) throws CreationFailedException {
        return new ZoneImpl(this, eventManagerNode, minX, minY, maxX, maxY);
    }

    @Override
    public MenuImpl createMenu(String title, int columns, float x, float y, float col1Width, float col2Width) throws CreationFailedException {
        return new MenuImpl(eventManagerNode, this, title, columns, x, y, col1Width, col2Width);
    }

    private int allocateDialogId() {
		Integer dialogId = recycledDialogIds.poll();
		if (dialogId == null || occupiedDialogIds.contains(dialogId))
		{
			while (true)
			{
				if (allocatedDialogId > MAX_DIALOG_ID) throw new CreationFailedException();
				if (!occupiedDialogIds.contains(allocatedDialogId)) break;

				allocatedDialogId++;
			}

			dialogId = allocatedDialogId;
		}

        return dialogId;
    }

	public void addOccupiedDialogId(int dialogId) {
		if(occupiedDialogIds.contains(dialogId)) return;
		occupiedDialogIds.add(dialogId);
	}

    private void recycleDialogId(int dialogId) {
		recycledDialogIds.offer(dialogId);
    }

    @Override
    public DialogIdImpl createDialogId(EventHandler<DialogResponseEvent> onResponse, EventHandler<DialogShowEvent> onShow, EventHandler<DialogCloseEvent> onClose) throws CreationFailedException {
        try {
            Integer dialogId = allocateDialogId();
            DialogIdImpl dialog = new DialogIdImpl(eventManagerNode, dialogId, onResponse, onShow, onClose);
            super.putDialog(dialog.getId(), dialog);
            return dialog;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public TimerImpl createTimer(int interval, int count, TimerCallback callback) {
        try {
            TimerImpl timer = new TimerImpl(eventManagerNode, interval, count, callback);
            super.putTimer(timer);
            return timer;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }

    @Override
    public ActorImpl createActor(int modelid, Vector3D position, float angle) throws CreationFailedException {
        try {
            ActorImpl actor = new ActorImpl(modelid, position, angle);
            super.setActor(actor.getId(), actor);
            return actor;
        } catch (Throwable e) {
            throw new CreationFailedException(e);
        }
    }
}
