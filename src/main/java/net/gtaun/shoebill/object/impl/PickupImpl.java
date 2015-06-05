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
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.event.player.PlayerPickupEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Pickup;
import net.gtaun.util.event.EventHandler;
import net.gtaun.util.event.EventManager;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124, JoJLlmAn & 123marvin123
 */
public class PickupImpl implements Pickup {
    private final EventManager rootEventManager;

    private int id = INVALID_ID;
    private int modelId, type;
    private Location location;
    private boolean isStatic;
    private EventHandler<PlayerPickupEvent> onPickupCallback;

    public PickupImpl(EventManager eventManager, SampObjectStoreImpl store, int modelId, int type, Location loc, EventHandler<PlayerPickupEvent> handler) {
        this(eventManager, store, modelId, type, loc, handler, true, -1, false);
    }

    public PickupImpl(EventManager eventManager, SampObjectStoreImpl store, int modelId, int type, Location loc, EventHandler<PlayerPickupEvent> handler, boolean doInit, int id, boolean isStatic) throws CreationFailedException {
        this.rootEventManager = eventManager;

        this.onPickupCallback = handler;
        this.modelId = modelId;
        this.type = type;
        this.location = new Location(loc);
        this.isStatic = isStatic;

        if (!isStatic) {
            if (doInit || id < 0)
                SampEventDispatcher.getInstance().executeWithoutEvent(() -> this.id = SampNativeFunction.createPickup(modelId, type, loc.getX(), loc.getY(), loc.getZ(), loc.getWorldId()));
            else this.id = id;
            if (this.id == INVALID_ID) throw new CreationFailedException();
            store.setPickup(this.id, this);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("id", id).append("modelId", modelId).append("type", type).toString();
    }

    @Override
    public void destroy() {
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.destroyPickup(id));
        destroyWithoutExec();
    }

    public void destroyWithoutExec() {
        if (isDestroyed()) return;

        DestroyEvent destroyEvent = new DestroyEvent(this);
        rootEventManager.dispatchEvent(destroyEvent, this);

        id = INVALID_ID;
    }

    public EventHandler<PlayerPickupEvent> getPickupCallback()
    {
        return onPickupCallback;
    }

    @Override
    public boolean isDestroyed() {
        return id == INVALID_ID;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getModelId() {
        return modelId;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }
}
