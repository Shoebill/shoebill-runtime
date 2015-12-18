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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStoreImpl;
import net.gtaun.shoebill.constant.ObjectMaterialSize;
import net.gtaun.shoebill.constant.ObjectMaterialTextAlign;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124 & 123marvin123
 */
public class PlayerObjectImpl implements PlayerObject {
    private int id = INVALID_ID;
    private Player player;

    private int modelId;
    private Location location;
    private float speed = 0.0F;
    private float drawDistance = 0;


    private Player attachedPlayer;
    private Vehicle attachedVehicle;

    private EventManagerNode eventManagerNode;


    public PlayerObjectImpl(EventManager eventManager, SampObjectStoreImpl store, Player player, int modelId, Location loc, Vector3D rot, float drawDistance) {
        this(eventManager, store, player, modelId, loc, rot, drawDistance, true, -1);
    }

    public PlayerObjectImpl(EventManager eventManager, SampObjectStoreImpl store, Player player, int modelId, Location loc, Vector3D rot, float drawDistance, boolean doInit, int id) throws CreationFailedException {
        if (!player.isOnline()) throw new CreationFailedException();

        eventManagerNode = eventManager.createChildNode();
        this.player = player;
        this.modelId = modelId;
        this.location = new Location(loc);
        this.drawDistance = drawDistance;

        if (doInit || id < 0)
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> setup(store, SampNativeFunction.createPlayerObject(player.getId(), modelId, loc.getX(), loc.getY(), loc.getZ(), rot.getX(), rot.getY(), rot.getZ(), drawDistance)));
        else
            setup(store, id);
    }

    private void setup(SampObjectStoreImpl store, int id) {
        this.id = id;
        if (this.id == INVALID_ID) throw new CreationFailedException();
        store.setPlayerObject(player, id, this);
    }

    public void onPlayerObjectMoved() {
        speed = 0.0F;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("player", player).append("id", id).append("modelId", modelId).toString();
    }

    @Override
    public void destroy() {
        if (id == INVALID_ID) return;
        if (player.isOnline())
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.destroyPlayerObject(player.getId(), id));
        destroyWithoutExec();
    }

    public void destroyWithoutExec() {
        if (id == INVALID_ID) return;

        DestroyEvent destroyEvent = new DestroyEvent(this);
        eventManagerNode.dispatchEvent(destroyEvent, this);

        eventManagerNode.destroy();
        id = INVALID_ID;
    }

    @Override
    public boolean isDestroyed() {
        if (!player.isOnline() && id != INVALID_ID) destroy();
        return id == INVALID_ID;
    }

    @Override
    public Player getPlayer() {
        return player;
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
    public float getSpeed() {
        if (isDestroyed()) return 0.0F;

        if (attachedPlayer != null && attachedPlayer.isOnline()) return attachedPlayer.getVelocity().speed3d();
        if (attachedVehicle != null && !attachedVehicle.isDestroyed())
            return attachedVehicle.getVelocity().speed3d();

        return speed;
    }

    @Override
    public float getDrawDistance() {
        return drawDistance;
    }

    @Override
    public Player getAttachedPlayer() {
        return attachedPlayer;
    }

    @Override
    public SampObject getAttachedObject() {
        return null;
    }

    @Override
    public Vehicle getAttachedVehicle() {
        return attachedVehicle;
    }

    @Override
    public Location getLocation() {
        if (isDestroyed()) return null;

        SampNativeFunction.getPlayerObjectPos(player.getId(), id, location);
        return location.clone();
    }

    @Override
    public void setLocation(Location loc) {
        if (isDestroyed()) return;

        location.set(loc);
        SampNativeFunction.setPlayerObjectPos(player.getId(), id, loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void setLocation(Vector3D pos) {
        if (isDestroyed()) return;

        location.set(pos);
        SampNativeFunction.setPlayerObjectPos(player.getId(), id, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public Vector3D getRotation() {
        if (isDestroyed()) return null;

        Vector3D rotate = new Vector3D();
        SampNativeFunction.getPlayerObjectRot(player.getId(), id, rotate);
        return rotate;
    }

    @Override
    public void setRotation(Vector3D rot) {
        setRotation(rot.getX(), rot.getY(), rot.getZ());
    }

    @Override
    public void setRotation(float rx, float ry, float rz) {
        if (isDestroyed()) return;

        SampNativeFunction.setPlayerObjectRot(player.getId(), id, rx, ry, rz);
    }

    @Override
    public boolean isMoving() {
        return !isDestroyed() && SampNativeFunction.isPlayerObjectMoving(player.getId(), id);

    }

    @Override
    public int move(float x, float y, float z, float speed) {
        if (isDestroyed()) return 0;

        if (attachedPlayer == null && attachedVehicle == null) this.speed = speed;
        return SampNativeFunction.movePlayerObject(player.getId(), id, x, y, z, speed, -1000.0f, -1000.0f, -1000.0f);
    }

    @Override
    public int move(float x, float y, float z, float speed, float rotX, float rotY, float rotZ) {
        if (isDestroyed()) return 0;

        if (attachedPlayer == null && attachedVehicle == null) this.speed = speed;
        return SampNativeFunction.movePlayerObject(player.getId(), id, x, y, z, speed, rotX, rotY, rotZ);
    }

    @Override
    public int move(Vector3D pos, float speed) {
        return move(pos.getX(), pos.getY(), pos.getZ(), speed);
    }

    @Override
    public int move(Vector3D pos, float speed, Vector3D rot) {
        return move(pos.getX(), pos.getY(), pos.getZ(), speed, rot.getX(), rot.getY(), rot.getZ());
    }

    @Override
    public void stop() {
        if (isDestroyed()) return;

        speed = 0.0F;
        SampNativeFunction.stopPlayerObject(player.getId(), id);
    }

    @Override
    public void attach(Player target, float x, float y, float z, float rx, float ry, float rz) {
        if (isDestroyed()) return;
        if (!target.isOnline()) return;

        SampNativeFunction.attachPlayerObjectToPlayer(player.getId(), id, target.getId(), x, y, z, rx, ry, rz);

        attachedPlayer = player;
        attachedVehicle = null;
        speed = 0.0F;
    }

    @Override
    public void attach(Player target, Vector3D pos, Vector3D rot) {
        attach(target, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ());
    }

    @Override
    public void attachCamera(Player player) {
        SampNativeFunction.attachCameraToPlayerObject(player.getId(), id);
    }

    @Override
    public void attach(SampObject object, float x, float y, float z, float rx, float ry, float rz, boolean syncRotation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void attach(SampObject object, Vector3D pos, Vector3D rot, boolean syncRotation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void attach(Vehicle vehicle, float x, float y, float z, float rx, float ry, float rz) {
        if (isDestroyed()) return;
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.attachPlayerObjectToVehicle(player.getId(), id, vehicle.getId(), x, y, z, rx, ry, rz);

        attachedPlayer = null;
        attachedVehicle = vehicle;
        speed = 0.0F;
    }

    @Override
    public void attach(Vehicle vehicle, Vector3D pos, Vector3D rot) {
        attach(vehicle, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ());
    }

    @Override
    public void setMaterial(int materialIndex, int modelId, String txdName, String textureName, Color materialColor) {
        if (isDestroyed()) return;

        SampNativeFunction.setPlayerObjectMaterial(player.getId(), id, materialIndex, modelId, txdName, textureName, materialColor.getValue());
    }

    @Override
    public void setMaterial(int materialIndex, int modelId, String txdName, String textureName) {
        if (isDestroyed()) return;

        SampNativeFunction.setPlayerObjectMaterial(player.getId(), id, materialIndex, modelId, txdName, textureName, 0);
    }

    @Override
    public void setMaterialText(String text, int materialIndex, ObjectMaterialSize materialSize, String fontFace, int fontSize, boolean isBold, Color fontColor, Color backColor, ObjectMaterialTextAlign textAlignment) {
        if (isDestroyed()) return;

        SampNativeFunction.setPlayerObjectMaterialText(player.getId(), id, text, materialIndex, materialSize.getValue(), fontFace, fontSize, isBold ? 1 : 0, fontColor.getValue(), backColor.getValue(), textAlignment.getValue());
    }

    @Override
    public void setMaterialText(String text) {
        if (isDestroyed()) return;

        SampNativeFunction.setPlayerObjectMaterialText(player.getId(), id, text, 0, ObjectMaterialSize.SIZE_256x128.getValue(), "Arial", 24, 1, 0xFFFFFFFF, 0, 0);
    }

    @Override
    public void setNoCameraCol() {
        SampNativeFunction.setObjectNoCameraCol(id);
    }

    @Override
    public void setPlayerObjectNoCameraCol() {
        SampNativeFunction.setPlayerObjectNoCameraCol(player.getId(), id);
    }
}
