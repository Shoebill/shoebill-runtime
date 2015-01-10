package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.constant.MapIconStyle;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerMapIcon;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerMapIconImpl implements PlayerMapIcon {
    private Player player;
    private Queue<Integer> emptyIds;
    private MapIcon[] mapIcons;

    public PlayerMapIconImpl(Player player) {
        this.player = player;
        this.emptyIds = new ConcurrentLinkedQueue<>();
        this.mapIcons = new MapIconImpl[101];
        for (int i = 1; i <= 100; i++)
            emptyIds.add(i);
    }

    private int pullRandomId() {
        int id = emptyIds.stream().findFirst().orElseThrow(() -> new CreationFailedException("There are no empty Ids left."));
        emptyIds.remove(id);
        return id;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public MapIcon getIcon(int iconid) {
        if (mapIcons[iconid] != null)
            return mapIcons[iconid];
        else
            return null;
    }

    @Override
    public void setIcon(int iconid, MapIcon icon) {
        mapIcons[iconid] = icon;
    }

    @Override
    public MapIcon createIcon(float x, float y, float z, int markerType, Color color, MapIconStyle style) {
        return createIcon(x, y, z, markerType, color, style, pullRandomId());
    }

    @Override
    public MapIcon createIcon(Vector3D pos, int markerType, Color color, MapIconStyle style) {
        return createIcon(pos, markerType, color, style, pullRandomId());
    }

    @Override
    public MapIcon createIcon(float x, float y, float z, int markerType, Color color, MapIconStyle style, int iconId) {
        MapIcon icon = createIcon(iconId);
        icon.update(x, y, z, markerType, color, style);
        setIcon(iconId, icon);
        return icon;
    }

    @Override
    public MapIcon createIcon(Vector3D pos, int markerType, Color color, MapIconStyle style, int iconId) {
        MapIcon icon = createIcon(iconId);
        icon.update(pos, markerType, color, style);
        setIcon(iconId, icon);
        return icon;
    }

    @Override
    public MapIcon createIcon() {
        int id = pullRandomId();
        MapIconImpl mapIcon = new MapIconImpl(id, player);
        setIcon(id, mapIcon);
        return mapIcon;
    }

    @Override
    public MapIcon createIcon(int iconId) {
        if (emptyIds.contains(iconId))
            emptyIds.remove(iconId);
        MapIconImpl mapIcon = new MapIconImpl(iconId, player);
        setIcon(iconId, mapIcon);
        return mapIcon;
    }

    public class MapIconImpl implements MapIcon {
        private int id = -1;
        private Player player;

        public MapIconImpl(int id, Player player) {
            this.id = id;
            this.player = player;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public boolean isDestroyed() {
            return id == -1;
        }

        @Override
        public void destroy() {
            if (isDestroyed()) return;
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.removePlayerMapIcon(player.getId(), id));
            destroyWithoutExec();
        }

        public void destroyWithoutExec() {
            if (isDestroyed()) return;
            emptyIds.add(id);
            id = -1;
        }

        @Override
        public void update(Vector3D pos, int markerType, Color color, MapIconStyle style) {
            update(pos.getX(), pos.getY(), pos.getZ(), markerType, color, style);
        }

        @Override
        public void update(float x, float y, float z, int markerType, Color color, MapIconStyle style) {
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.setPlayerMapIcon(player.getId(), id, x, y, z, markerType, color.getValue(), style.getValue()));
        }
    }
}
