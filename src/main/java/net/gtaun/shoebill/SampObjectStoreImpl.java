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

import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.event.dialog.DialogResponseEvent;
import net.gtaun.shoebill.event.object.ObjectMovedEvent;
import net.gtaun.shoebill.event.object.PlayerObjectMovedEvent;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.event.player.PlayerUpdateEvent;
import net.gtaun.shoebill.event.vehicle.VehicleModEvent;
import net.gtaun.shoebill.event.vehicle.VehicleUpdateDamageEvent;
import net.gtaun.shoebill.object.*;
import net.gtaun.shoebill.object.Timer;
import net.gtaun.shoebill.object.impl.*;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.HandlerPriority;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author MK124 & 123marvin123
 */
public class SampObjectStoreImpl implements SampObjectStore
{
    public static final int MAX_PLAYERS = 1000;
    public static final int MAX_VEHICLES = 2000;
    public static final int MAX_OBJECTS = 1000;
    public static final int MAX_ZONES = 1024;
    public static final int MAX_TEXT_DRAWS = 2048;
    public static final int MAX_PLAYER_TEXT_DRAWS = 256;
    public static final int MAX_MENUS = 128;
    public static final int MAX_GLOBAL_LABELS = 1024;
    public static final int MAX_PLAYER_LABELS = 1024;
    public static final int MAX_PICKUPS = 4096;
    public static final int MAX_CLASSES = 300;
    public static final int MAX_ACTORS = 1000;

    private static <T> Collection<T> getNotNullInstances(T[] items) {
        Collection<T> list = new ArrayList<>();
        if (items == null) return list;

        for (T item : items) {
            if (item != null) list.add(item);
        }
        return list;
    }

    private static <T> void clearUnusedReferences(Collection<Reference<T>> collection) {
        Iterator<Reference<T>> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().get() == null) iterator.remove();
        }
    }


    protected final EventManager eventManagerNode;

    private ServerImpl server;
    private WorldImpl world;
    private PlayerImpl[] players = new PlayerImpl[MAX_PLAYERS];
    private VehicleImpl[] vehicles = new VehicleImpl[MAX_VEHICLES];
    private SampObjectImpl[] objects = new SampObjectImpl[MAX_OBJECTS];
    private PlayerObjectImpl[][] playerObjectsArray = new PlayerObjectImpl[MAX_PLAYERS][];
    private PickupImpl[] pickups = new PickupImpl[MAX_PICKUPS];
    private LabelImpl[] labels = new LabelImpl[MAX_GLOBAL_LABELS];
    private PlayerLabelImpl[][] playerLabelsArray = new PlayerLabelImpl[MAX_PLAYERS][];
    private TextdrawImpl[] textdraws = new TextdrawImpl[MAX_TEXT_DRAWS];
    private PlayerTextdrawImpl[][] playerTextdrawsArray = new PlayerTextdrawImpl[MAX_PLAYERS][];
    private ZoneImpl[] zones = new ZoneImpl[MAX_ZONES];
    private MenuImpl[] menus = new MenuImpl[MAX_MENUS];
    private ActorImpl[] actors = new ActorImpl[MAX_ACTORS];
    private SpawnInfo[] playerClasses = new SpawnInfo[MAX_CLASSES];
    private Collection<Reference<TimerImpl>> timers = new ConcurrentLinkedQueue<>();
    private Map<Integer, Reference<DialogIdImpl>> dialogs = new ConcurrentHashMap<>();
    private List<Reference<PickupImpl>> staticPickups = new ArrayList<>();


    SampObjectStoreImpl(EventManager rootEventManager)
    {
        eventManagerNode = rootEventManager.createChildNode();
        setupObjectEventHandler();
    }

    private void setupObjectEventHandler() {
        eventManagerNode.registerHandler(PlayerUpdateEvent.class, HandlerPriority.MONITOR, (e) ->
        {
            Player primitive = e.getPlayer().getPrimitive();
            if (!(primitive instanceof PlayerImpl)) return;

            PlayerImpl player = (PlayerImpl) primitive;
            player.onPlayerUpdate();
        });

        eventManagerNode.registerHandler(PlayerDisconnectEvent.class, HandlerPriority.BOTTOM, (e) ->
        {
            Player primitive = e.getPlayer().getPrimitive();
            if (!(primitive instanceof PlayerImpl)) return;

            PlayerImpl player = (PlayerImpl) primitive;
            player.onPlayerDisconnect();
        });

        eventManagerNode.registerHandler(DialogResponseEvent.class, HandlerPriority.MONITOR, (e) ->
        {
            Player primitive = e.getPlayer().getPrimitive();
            if (!(primitive instanceof PlayerImpl)) return;

            PlayerImpl player = (PlayerImpl) primitive;
            player.onDialogResponse();
        });

        eventManagerNode.registerHandler(PlayerObjectMovedEvent.class, HandlerPriority.MONITOR, (e) ->
        {
            PlayerObject primitive = (PlayerObject) e.getObject().getPrimitive();
            if (!(primitive instanceof PlayerObjectImpl)) return;

            PlayerObjectImpl object = (PlayerObjectImpl) primitive;
            object.onPlayerObjectMoved();
        });

        eventManagerNode.registerHandler(ObjectMovedEvent.class, HandlerPriority.MONITOR, (e) ->
        {
            SampObject primitive = e.getObject().getPrimitive();
            if (!(primitive instanceof SampObjectImpl)) return;

            SampObjectImpl object = (SampObjectImpl) primitive;
            object.onObjectMoved();
        });

        eventManagerNode.registerHandler(VehicleModEvent.class, HandlerPriority.MONITOR, (e) ->
        {
            Vehicle primitive = e.getVehicle().getPrimitive();
            if (!(primitive instanceof VehicleImpl)) return;

            VehicleImpl vehicle = (VehicleImpl) primitive;
            vehicle.onVehicleMod();
        });

        eventManagerNode.registerHandler(VehicleUpdateDamageEvent.class, HandlerPriority.MONITOR, (e) ->
        {
            Vehicle primitive = e.getVehicle().getPrimitive();
            if (!(primitive instanceof VehicleImpl)) return;

            VehicleImpl vehicle = (VehicleImpl) primitive;
            vehicle.onVehicleUpdateDamage();
        });
    }

    @Override
    public Server getServer() {
        return server;
    }

    public void setServer(ServerImpl server) {
        this.server = server;
    }

    @Override
    public World getWorld() {
        return world;
    }

    public void setWorld(WorldImpl world) {
        this.world = world;
    }

    @Override
    public Player getPlayer(int id) {
        if (id < 0 || id >= MAX_PLAYERS) return null;
        return players[id];
    }

    @Override
    public Player getPlayer(String name) {
        if (name == null) return null;
        name = name.trim();

        for (Player player : players) {
            if (player == null) continue;
            if (player.getName().equalsIgnoreCase(name)) return player;
        }

        return null;
    }

    @Override
    public Vehicle getVehicle(int id) {
        if (id < 0 || id >= MAX_VEHICLES) return null;
        return vehicles[id];
    }

    @Override
    public SampObject getObject(int id) {
        if (id < 0 || id >= MAX_OBJECTS) return null;
        return objects[id];
    }

    @Override
    public PlayerObject getPlayerObject(Player player, int id) {
        if (player == null) return null;

        int playerId = player.getId();
        if (playerId < 0 || playerId >= MAX_PLAYERS) return null;

        if (id < 0 || id >= MAX_OBJECTS) return null;

        PlayerObject[] playerObjects = playerObjectsArray[playerId];
        if (playerObjects == null) return null;

        return playerObjects[id];
    }

    @Override
    public Pickup getPickup(int id) {
        if (id < 0 || id >= MAX_PICKUPS) return null;
        return pickups[id];
    }

    @Override
    public Label getLabel(int id) {
        if (id < 0 || id >= MAX_GLOBAL_LABELS) return null;
        return labels[id];
    }

    @Override
    public PlayerLabel getPlayerLabel(Player player, int id) {
        if (player == null) return null;

        int playerId = player.getId();
        if (playerId < 0 || playerId >= MAX_PLAYERS) return null;

        if (id < 0 || id >= MAX_PLAYER_LABELS) return null;

        PlayerLabel[] playerLabels = playerLabelsArray[playerId];
        if (playerLabels == null) return null;

        return playerLabels[id];
    }

    @Override
    public Textdraw getTextdraw(int id) {
        if (id < 0 || id >= MAX_TEXT_DRAWS) return null;
        return textdraws[id];
    }

    @Override
    public PlayerTextdraw getPlayerTextdraw(Player player, int id) {
        if (player == null) return null;

        int playerId = player.getId();
        if (playerId < 0 || playerId >= MAX_PLAYERS) return null;

        if (id < 0 || id >= MAX_PLAYER_TEXT_DRAWS) return null;

        PlayerTextdraw[] textdraws = playerTextdrawsArray[playerId];
        if (textdraws == null) return null;

        return textdraws[id];
    }

    @Override
    public Zone getZone(int id) {
        if (id < 0 || id >= MAX_ZONES) return null;
        return zones[id];
    }

    @Override
    public Menu getMenu(int id) {
        if (id < 0 || id >= MAX_MENUS) return null;
        return menus[id];
    }

    @Override
    public DialogId getDialog(int id) {
        Reference<DialogIdImpl> ref = dialogs.get(id);
        return ref != null ? ref.get() : null;
    }

    @Override
    public Collection<Player> getPlayers() {
        return getNotNullInstances(players);
    }

    @Override
    public Collection<Player> getHumanPlayers() {
        Collection<Player> list = new ArrayList<>();
        if (players == null) return list;

        for (Player item : players) {
            if (item != null && !item.isNpc()) list.add(item);
        }
        return list;
    }

    @Override
    public Collection<Player> getNpcPlayers() {
        Collection<Player> list = new ArrayList<>();
        if (players == null) return list;

        for (Player item : players) {
            if (item != null && item.isNpc()) list.add(item);
        }
        return list;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return getNotNullInstances(vehicles);
    }

    @Override
    public Collection<SampObject> getObjects() {
        return getNotNullInstances(objects);
    }

    @Override
    public Collection<PlayerObject> getPlayerObjects(Player player) {
        if (player == null) return null;

        int playerId = player.getId();
        if (playerId < 0 || playerId >= MAX_PLAYERS) return new ArrayList<>(0);

        PlayerObject[] playerObjects = playerObjectsArray[playerId];
        return getNotNullInstances(playerObjects);
    }

    @Override
    public Collection<Pickup> getPickups() {
        return getNotNullInstances(pickups);
    }

    @Override
    public Collection<Label> getLabels() {
        return getNotNullInstances(labels);
    }

    @Override
    public Collection<PlayerLabel> getPlayerLabels(Player player) {
        if (player == null) return null;

        int playerId = player.getId();
        if (playerId < 0 || playerId >= MAX_PLAYERS) return new ArrayList<>(0);

        PlayerLabel[] playerLabels = playerLabelsArray[playerId];
        return getNotNullInstances(playerLabels);
    }

    @Override
    public Collection<Textdraw> getTextdraws() {
        return getNotNullInstances(textdraws);
    }

    @Override
    public Collection<PlayerTextdraw> getPlayerTextdraws(Player player) {
        if (player == null) return null;

        int playerId = player.getId();
        if (playerId < 0 || playerId >= MAX_PLAYERS) return new ArrayList<>(0);

        PlayerTextdraw[] textdraws = playerTextdrawsArray[playerId];
        return getNotNullInstances(textdraws);
    }

    @Override
    public Collection<Zone> getZones() {
        return getNotNullInstances(zones);
    }

    @Override
    public Collection<Menu> getMenus() {
        return getNotNullInstances(menus);
    }

    @Override
    public Collection<DialogId> getDialogIds() {
        Collection<DialogId> items = new ArrayList<>();
        for (Reference<DialogIdImpl> reference : dialogs.values()) {
            DialogId dialog = reference.get();
            if (dialog != null) items.add(dialog);
        }

        return items;
    }

    @Override
    public Collection<SpawnInfo> getPlayerClasses() {
        Collection<SpawnInfo> items = new ArrayList<>();
        Collections.addAll(items, playerClasses);
        return items;
    }

    @Override
    public Collection<Actor> getActors() {
        return new ArrayList<>(Arrays.asList(actors));
    }

    @Override
    public Actor getActor(int id) {
        if (id < 0 || id > MAX_ACTORS) return null;
        return actors[id];
    }

    @Override
    public int getVehiclePoolSize() {
        return SampNativeFunction.getVehiclePoolSize();
    }

    @Override
    public int getPlayerPoolSize() {
        return SampNativeFunction.getPlayerPoolSize();
    }

    @Override
    public int getActorPoolSize() {
        return SampNativeFunction.getActorPoolSize();
    }

    public Collection<Timer> getTimers() {
        Collection<Timer> items = new ArrayList<>();
        Collection<Reference<TimerImpl>> unusedItems = new ArrayList<>();
        for (Reference<TimerImpl> reference : timers) {
            Timer timer = reference.get();
            if (timer != null) items.add(timer);
            else unusedItems.add(reference);
        }

        timers.removeAll(unusedItems);
        return items;
    }

    public void setPlayer(int id, PlayerImpl player) {
        if (player == null && players[id] != null) {
            removePlayer(id);
            if (player == null) return;
        }

        players[id] = player;

        playerObjectsArray[id] = new PlayerObjectImpl[MAX_OBJECTS];
        playerLabelsArray[id] = new PlayerLabelImpl[MAX_PLAYER_LABELS];
        playerTextdrawsArray[id] = new PlayerTextdrawImpl[MAX_PLAYER_TEXT_DRAWS];
    }

    private void removePlayer(int id) {
        for (PlayerLabel playerLabel : playerLabelsArray[id]) {
            if (playerLabel != null) playerLabel.destroy();
        }

        for (PlayerObject playerObject : playerObjectsArray[id]) {
            if (playerObject != null) playerObject.destroy();
        }

        playerLabelsArray[id] = null;
        playerObjectsArray[id] = null;
        playerTextdrawsArray[id] = null;
        players[id] = null;
    }

    public void setVehicle(int id, VehicleImpl vehicle) {
        vehicles[id] = vehicle;
    }

    public void setObject(int id, SampObjectImpl object) {
        objects[id] = object;
    }

    public void setPlayerObject(Player player, int id, PlayerObject object) {
        if (!player.isOnline()) return;
        PlayerObject[] playerObjects = playerObjectsArray[player.getId()];
        playerObjects[id] = object;
    }

    public void setPickup(int id, PickupImpl pickup) {
        pickups[id] = pickup;
    }

    public void setLabel(int id, LabelImpl label) {
        labels[id] = label;
    }

    public void setPlayerLabel(Player player, int id, PlayerLabelImpl label) {
        if (!player.isOnline() || label == null) return;
        PlayerLabel[] playerLabels = playerLabelsArray[player.getId()];
        playerLabels[id] = label;
    }

    public void setTextdraw(int id, TextdrawImpl textdraw) {
        textdraws[id] = textdraw;
    }

    public void setPlayerTextdraw(Player player, int id, PlayerTextdraw textdraw) {
        if (!player.isOnline()) return;
        PlayerTextdraw[] textdraws = playerTextdrawsArray[player.getId()];
        textdraws[id] = textdraw;
    }

    public void setZone(int id, ZoneImpl zone) {
        zones[id] = zone;
    }

    public void setMenu(int id, MenuImpl menu) {
        menus[id] = menu;
    }

    public void setPlayerClass(int id, SpawnInfo playerClass) { playerClasses[id] = playerClass; }

    public void setActor(int id, ActorImpl actor) {
        actors[id] = actor;
    }

    public void putTimer(TimerImpl timer) {
        clearUnusedReferences(timers);
        timers.add(new WeakReference<>(timer));
    }

    public void removeTimer(Timer timer) {
        for (Iterator<Reference<TimerImpl>> iterator = timers.iterator(); iterator.hasNext(); ) {
            Reference<TimerImpl> ref = iterator.next();
            Timer t = ref.get();
            if (t == null || t == timer) iterator.remove();
        }
    }

    public void putDialog(int id, DialogIdImpl dialog) {
        clearUnusedReferences(dialogs.values());
        dialogs.put(id, new WeakReference<>(dialog));
    }

    public void removeDialog(DialogId dialog) {
        dialogs.remove(dialog.getId());
    }

    public void addStaticPickup(PickupImpl pickup) {
        if (pickup != null && pickup.isStatic()) {
            clearUnusedReferences(staticPickups);
            staticPickups.add(new WeakReference<>(pickup));
        }
    }

    public void removeStaticPickup(Pickup pickup) {
        for (Iterator<Reference<PickupImpl>> iterator = staticPickups.iterator(); iterator.hasNext(); ) {
            Reference<PickupImpl> ref = iterator.next();
            Pickup p = ref.get();
            if (p == null || p == pickup) iterator.remove();
        }
    }

    public List<PickupImpl> getStaticPickups() {
        List<PickupImpl> items = new ArrayList<>();
        Collection<Reference<PickupImpl>> unusedItems = new ArrayList<>();
        for (Reference<PickupImpl> reference : staticPickups) {
            PickupImpl pickup = reference.get();
            if (pickup != null) items.add(pickup);
            else unusedItems.add(reference);
        }

        staticPickups.removeAll(unusedItems);
        return items;
    }

    public Collection<TimerImpl> getTimerImpls() {
        Collection<TimerImpl> items = new ArrayList<>();
        Collection<Reference<TimerImpl>> unusedItems = new ArrayList<>();
        for (Reference<TimerImpl> reference : timers) {
            TimerImpl timer = reference.get();
            if (timer != null) items.add(timer);
            else unusedItems.add(reference);
        }

        timers.removeAll(unusedItems);
        return items;
    }
}
