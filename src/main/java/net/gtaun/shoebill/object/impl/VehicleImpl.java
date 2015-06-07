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
import net.gtaun.shoebill.constant.PlayerState;
import net.gtaun.shoebill.constant.VehicleModel;
import net.gtaun.shoebill.data.*;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.event.player.PlayerStateChangeEvent;
import net.gtaun.shoebill.event.vehicle.VehicleCreateEvent;
import net.gtaun.shoebill.event.vehicle.VehicleSpawnEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.*;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124, JoJLlmAn & 123marvin123
 */
public class VehicleImpl implements Vehicle {
    private final SampObjectStoreImpl store;

    private boolean isStatic = false;

    private int id = INVALID_ID;
    private int modelId;
    private int interiorId;
    private int color1, color2;
    private int respawnDelay;
    private boolean hasSiren;

    private VehicleParamImpl param;
    private VehicleComponentImpl component;
    private VehicleDamageImpl damage;

    private EventManagerNode eventManagerNode;

    public VehicleImpl(EventManager eventManager, SampObjectStoreImpl store, int modelid, float x, float y, float z, float angle, int worldid, int interiorid, int color1, int color2, int respawnDelay) {
        this(eventManager, store, modelid, new AngledLocation(x, y, z, interiorid, worldid, angle), color1, color2, respawnDelay);
    }

    public VehicleImpl(EventManager eventManager, SampObjectStoreImpl store, int modelid, AngledLocation loc, int color1, int color2, int respawnDelay) {
        this(eventManager, store, modelid, loc, color1, color2, respawnDelay, 0, true, -1);
    }

    public VehicleImpl(EventManager eventManager, SampObjectStoreImpl store, int modelId, AngledLocation loc, int color1, int color2, int respawnDelay, int addsiren, boolean doInit, int id) {
        this(eventManager, store, modelId, loc, color1, color2, respawnDelay, doInit, id, addsiren > 0);
    }

    public VehicleImpl(EventManager eventManager, SampObjectStoreImpl store, int modelId, AngledLocation loc, int color1, int color2, int respawnDelay, boolean doInit, int id, boolean addsiren) throws CreationFailedException {
        this.store = store;
        eventManagerNode = eventManager.createChildNode();
        switch (modelId) {
            case 537:
            case 538:
            case 569:
            case 570:
            case 590:
                if (doInit || id < 0)
                    SampEventDispatcher.getInstance().executeWithoutEvent(() -> this.id = SampNativeFunction.addStaticVehicleEx(modelId, loc.x, loc.y, loc.z, loc.angle, color1, color2, respawnDelay, addsiren));
                else this.id = id;
                isStatic = true;
                break;

            default:
                if (doInit || id < 0)
                    SampEventDispatcher.getInstance().executeWithoutEvent(() -> this.id = SampNativeFunction.createVehicle(modelId, loc.x, loc.y, loc.z, loc.angle, color1, color2, respawnDelay, addsiren));
                else this.id = id;
        }
        if (this.id == INVALID_ID) throw new CreationFailedException();
        store.setVehicle(this.id, this);
        initialize(modelId, interiorId, loc.getWorldId(), color1, color2, respawnDelay, addsiren);
    }

    public VehicleImpl(EventManager eventManager, SampObjectStoreImpl store, int modelId, float x, float y, float z, float angle, int interiorid, int worldid,
                       int color1, int color2, int respawnDelay, int addsiren, boolean doInit, int id) throws CreationFailedException {
        this(eventManager, store, modelId, new AngledLocation(x, y, z, interiorid, worldid, angle), color1, color2, respawnDelay, addsiren, doInit, id);
    }

    private void initialize(int modelId, int interiorId, int worldId, int color1, int color2, int respawnDelay, boolean addsiren) throws CreationFailedException {
        this.modelId = modelId;
        this.interiorId = interiorId;
        this.color1 = color1;
        this.color2 = color2;
        this.hasSiren = addsiren;
        this.respawnDelay = respawnDelay;
        SampNativeFunction.linkVehicleToInterior(id, interiorId);
        SampNativeFunction.setVehicleVirtualWorld(id, worldId);

        param = new VehicleParamImpl(this);
        component = new VehicleComponentImpl(this);
        damage = new VehicleDamageImpl(this);

        VehicleCreateEvent createEvent = new VehicleCreateEvent(this);
        eventManagerNode.dispatchEvent(createEvent, this);

        VehicleSpawnEvent event = new VehicleSpawnEvent(this);
        eventManagerNode.dispatchEvent(event, this);
    }

    public void onVehicleMod() {
        component.update();
    }

    public void onVehicleUpdateDamage() {
        SampNativeFunction.getVehicleDamageStatus(id, damage);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("id", id).append("isStatic", isStatic).append("modelId", modelId).toString();
    }

    @Override
    public void destroy() {
        if (isDestroyed()) return;
        if (isStatic) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.destroyVehicle(id));
        destroyWithoutExec();
    }

    public void destroyWithoutExec() {
        if (isDestroyed()) return;
        if (isStatic) return;

        DestroyEvent destroyEvent = new DestroyEvent(this);
        eventManagerNode.dispatchEvent(destroyEvent, this);

        eventManagerNode.destroy();
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
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public int getModelId() {
        return modelId;
    }

    @Override
    public String getModelName() {
        return VehicleModel.getName(modelId);
    }

    @Override
    public int getColor1() {
        return color1;
    }

    @Override
    public int getColor2() {
        return color2;
    }

    @Override
    public int getRespawnDelay() {
        return respawnDelay;
    }

    @Override
    public VehicleParam getState() {
        return param;
    }

    @Override
    public VehicleComponent getComponent() {
        return component;
    }

    @Override
    public VehicleDamage getDamage() {
        return damage;
    }

    @Override
    public AngledLocation getLocation() {
        if (isDestroyed()) return null;

        AngledLocation location = new AngledLocation();

        SampNativeFunction.getVehiclePos(id, location);
        location.setAngle(SampNativeFunction.getVehicleZAngle(id));
        location.setInteriorId(interiorId);
        location.setWorldId(SampNativeFunction.getVehicleVirtualWorld(id));

        return location;
    }

    @Override
    public void setLocation(AngledLocation loc) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehiclePos(id, loc.getX(), loc.getY(), loc.getZ());
        SampNativeFunction.setVehicleZAngle(id, loc.getAngle());
        SampNativeFunction.linkVehicleToInterior(id, loc.getInteriorId());
        SampNativeFunction.setVehicleVirtualWorld(id, loc.getWorldId());
    }

    @Override
    public void setLocation(float x, float y, float z) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehiclePos(id, x, y, z);
    }

    @Override
    public void setLocation(Vector3D pos) {
        setLocation(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setLocation(Location loc) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehiclePos(id, loc.getX(), loc.getY(), loc.getZ());
        SampNativeFunction.linkVehicleToInterior(id, loc.getInteriorId());
        SampNativeFunction.setVehicleVirtualWorld(id, loc.getWorldId());
    }

    @Override
    public float getAngle() {
        if (isDestroyed()) return 0.0F;

        return SampNativeFunction.getVehicleZAngle(id);
    }

    @Override
    public void setAngle(float angle) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehicleZAngle(id, angle);
    }

    @Override
    public Quaternion getRotationQuat() {
        if (isDestroyed()) return null;

        Quaternion quaternions = new Quaternion();
        SampNativeFunction.getVehicleRotationQuat(id, quaternions);

        return quaternions;
    }

    @Override
    public int getInteriorId() {
        return interiorId;
    }

    @Override
    public void setInteriorId(int interiorId) {
        if (isDestroyed()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.linkVehicleToInterior(id, interiorId));
        setInteriorIdWithoutExec(interiorId);
    }

    public void setInteriorIdWithoutExec(int interiorId) {
        this.interiorId = interiorId;
    }

    @Override
    public int getWorldId() {
        if (isDestroyed()) return 0;

        return SampNativeFunction.getVehicleVirtualWorld(id);
    }

    @Override
    public void setWorldId(int worldId) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehicleVirtualWorld(id, worldId);
    }

    @Override
    public float getHealth() {
        if (isDestroyed()) return 0.0F;

        return SampNativeFunction.getVehicleHealth(id);
    }

    @Override
    public void setHealth(float health) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehicleHealth(id, health);
    }

    @Override
    public Velocity getVelocity() {
        if (isDestroyed()) return null;

        Velocity velocity = new Velocity();
        SampNativeFunction.getVehicleVelocity(id, velocity);

        return velocity;
    }

    @Override
    public void setVelocity(Velocity velocity) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehicleVelocity(id, velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public void putPlayer(Player player, int seat) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;

        PlayerState state = player.getState();
        boolean isInAnyVehicle = player.isInAnyVehicle();

        if (isInAnyVehicle) player.removeFromVehicle();
        SampNativeFunction.putPlayerInVehicle(player.getId(), id, seat);

        if (isInAnyVehicle && state == player.getState()) {
            PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, state.getValue());
            eventManagerNode.dispatchEvent(event, player);
        }
    }

    @Override
    public boolean isPlayerIn(Player player) {
        if (isDestroyed()) return false;
        return player.isOnline() && SampNativeFunction.isPlayerInVehicle(player.getId(), id);

    }

    @Override
    public boolean isStreamedIn(Player forPlayer) {
        if (isDestroyed()) return false;
        return forPlayer.isOnline() && SampNativeFunction.isVehicleStreamedIn(id, forPlayer.getId());

    }

    @Override
    public void setParamsForPlayer(Player player, boolean objective, boolean doorslocked) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;

        SampNativeFunction.setVehicleParamsForPlayer(id, player.getId(), objective, doorslocked);
    }

    @Override
    public void respawn() {
        if (isDestroyed()) return;

        SampNativeFunction.setVehicleToRespawn(id);
    }

    @Override
    public void setColor(int color1, int color2) {
        if (isDestroyed()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.changeVehicleColor(id, color1, color2));

        setColorWithoutExec(color1, color2);
    }

    public void setColorWithoutExec(int color1, int color2) {
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public void setPaintjob(int paintjobId) {
        if (isDestroyed()) return;

        SampNativeFunction.changeVehiclePaintjob(id, paintjobId);
    }

    @Override
    public Vehicle getTrailer() {
        if (isDestroyed()) return null;

        int trailerId = SampNativeFunction.getVehicleTrailer(id);
        return store.getVehicle(trailerId);
    }

    @Override
    public void attachTrailer(Vehicle trailer) {
        if (isDestroyed()) return;
        if (trailer.isDestroyed()) return;

        SampNativeFunction.attachTrailerToVehicle(trailer.getId(), id);
    }

    @Override
    public void detachTrailer() {
        if (isDestroyed()) return;

        SampNativeFunction.detachTrailerFromVehicle(id);
    }

    @Override
    public boolean isTrailerAttached() {
        return !isDestroyed() && SampNativeFunction.isTrailerAttachedToVehicle(id);

    }

    @Override
    public void setNumberPlate(String numberplate) {
        if (isDestroyed()) return;

        if (numberplate == null) throw new NullPointerException();
        SampNativeFunction.setVehicleNumberPlate(id, numberplate);
    }

    @Override
    public void repair() {
        if (isDestroyed()) return;

        SampNativeFunction.repairVehicle(id);
    }

    @Override
    public VehicleState getDoors() {
        VehicleState params = new VehicleState();
        SampNativeFunction.getVehicleParamsCarDoors(id, params);
        return params;
    }

    @Override
    public VehicleState getWindows() {
        VehicleState params = new VehicleState();
        SampNativeFunction.getVehicleParamsCarWindows(id, params);
        return params;
    }

    @Override
    public int getSirenState() {
        return SampNativeFunction.getVehicleParamsSirenState(id);
    }

    @Override
    public void setDoors(VehicleState vehicleState) {
        SampNativeFunction.setVehicleParamsCarDoors(id, vehicleState.driver, vehicleState.passenger, vehicleState.backLeft, vehicleState.backRight);
    }

    @Override
    public void setWindows(VehicleState vehicleState) {
        SampNativeFunction.setVehicleParamsCarWindows(id, vehicleState.driver, vehicleState.passenger, vehicleState.backLeft, vehicleState.backRight);
    }

    @Override
    public void setAngularVelocity(Velocity velocity) {
        if (isDestroyed()) return;

        SampNativeFunction.setVehicleAngularVelocity(id, velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public boolean hasSiren() {
        return hasSiren;
    }
}
