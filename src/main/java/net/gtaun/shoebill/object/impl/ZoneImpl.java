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

import net.gtaun.shoebill.SampEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStoreImpl;
import net.gtaun.shoebill.data.Area;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.Zone;
import net.gtaun.util.event.EventManager;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124, JoJLlmAn & 123marvin123
 */
public class ZoneImpl implements Zone {
    private final EventManager rootEventManager;

    private int id = INVALID_ID;
    private Area area;

    private boolean[] isPlayerShowed = new boolean[SampObjectStoreImpl.MAX_PLAYERS];
    private boolean[] isPlayerFlashing = new boolean[SampObjectStoreImpl.MAX_PLAYERS];

    public ZoneImpl(SampObjectStoreImpl store, EventManager eventManager, float minX, float minY, float maxX, float maxY, boolean doInit, int id) throws CreationFailedException {
        this.rootEventManager = eventManager;
        initialize(store, minX, minY, maxX, maxY, doInit, id);
    }

    public ZoneImpl(SampObjectStoreImpl store, EventManager eventManager, float minX, float minY, float maxX, float maxY) throws CreationFailedException {
        this.rootEventManager = eventManager;
        initialize(store, minX, minY, maxX, maxY, true, -1);
    }

    private void initialize(SampObjectStoreImpl store, float minX, float minY, float maxX, float maxY, boolean doInit, int id) throws CreationFailedException {
        area = new Area(minX, minY, maxX, maxY);
        for (int i = 0; i < isPlayerShowed.length; i++) {
            isPlayerShowed[i] = false;
            isPlayerFlashing[i] = false;
        }

        if (doInit || id < 0)
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> setup(store, SampNativeFunction.gangZoneCreate(minX, minY, maxX, maxY)));
        else setup(store, id);
    }

    private void setup(SampObjectStoreImpl store, int id) {
        if (id == INVALID_ID) throw new CreationFailedException();

        this.id = id;
        store.setZone(id, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", id).toString();
    }

    @Override
    public void destroy() {
        if (isDestroyed()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneDestroy(id));
        destroyWithoutExec();
    }

    public void destroyWithoutExec() {
        if (isDestroyed()) return;

        DestroyEvent destroyEvent = new DestroyEvent(this);
        rootEventManager.dispatchEvent(destroyEvent, this);

        id = INVALID_ID;
    }

    @Override
    public boolean isDestroyed() {
        return id == INVALID_ID;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Area getArea() {
        return area.clone();
    }

    @Override
    public void show(Player player, Color color) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneShowForPlayer(playerId, id, color.getValue()));
        showWithoutExec(player);

    }

    public void showWithoutExec(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();
        isPlayerShowed[playerId] = true;
        isPlayerFlashing[playerId] = false;
    }

    @Override
    public void hide(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;

        int playerId = player.getId();
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneHideForPlayer(playerId, id));
        hideWithoutExec(player);
    }

    public void hideWithoutExec(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();
        isPlayerShowed[playerId] = false;
        isPlayerFlashing[playerId] = false;
    }

    @Override
    public void flash(Player player, Color color) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;

        int playerId = player.getId();

        if (isPlayerShowed[playerId]) {
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneFlashForPlayer(playerId, id, color.getValue()));
            flashWithoutExec(player);
        }
    }

    public void flashWithoutExec(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();

        if (isPlayerShowed[playerId])
            isPlayerFlashing[playerId] = true;
    }

    @Override
    public void stopFlash(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;

        int playerId = player.getId();

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneStopFlashForPlayer(playerId, id));
        stopFlashWithoutExec(player);
    }

    public void stopFlashWithoutExec(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();
        isPlayerFlashing[playerId] = false;
    }

    @Override
    public void showForAll(Color color) {
        if (isDestroyed()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneShowForAll(id, color.getValue()));
        showForAllWithoutExec();
    }

    public void showForAllWithoutExec() {
        if (isDestroyed()) return;
        for (int i = 0; i < isPlayerShowed.length; i++)
            isPlayerShowed[i] = true;
    }

    @Override
    public void hideForAll() {
        if (isDestroyed()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneHideForAll(id));

        hideForAllWithoutExec();
    }

    public void hideForAllWithoutExec() {
        if (isDestroyed()) return;
        for (int i = 0; i < isPlayerShowed.length; i++) {
            isPlayerShowed[i] = false;
            isPlayerFlashing[i] = false;
        }
    }

    @Override
    public void flashForAll(Color color) {
        if (isDestroyed()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneFlashForAll(id, color.getValue()));
        flashForAllWithoutExec();
    }

    public void flashForAllWithoutExec() {
        if (isDestroyed()) return;
        for (int i = 0; i < isPlayerShowed.length; i++)
            if (isPlayerShowed[i]) isPlayerFlashing[i] = true;
    }

    @Override
    public void stopFlashForAll() {
        if (isDestroyed()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.gangZoneStopFlashForAll(id));
        stopFlashForAllWithoutExec();
    }

    public void stopFlashForAllWithoutExec() {
        if (isDestroyed()) return;
        for (int i = 0; i < isPlayerFlashing.length; i++)
            isPlayerFlashing[i] = false;
    }

    @Override
    public boolean isInRange(Vector3D pos) {
        return area.isInRange(pos);
    }
}
