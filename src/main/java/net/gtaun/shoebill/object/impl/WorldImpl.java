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

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStore;
import net.gtaun.shoebill.constant.PlayerMarkerMode;
import net.gtaun.shoebill.constant.WeaponModel;
import net.gtaun.shoebill.data.*;
import net.gtaun.shoebill.object.World;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 */
public class WorldImpl implements World {
    private final SampObjectStore store;

    private float nameTagDrawDistance = 70;
    private float chatRadius = -1;
    private float playerMarkerRadius = -1;


    public WorldImpl(SampObjectStore store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).toString();
    }

    @Override
    public void setTeamCount(int count) {
        SampNativeFunction.setTeamCount(count);
    }

    @Override
    public int addPlayerClass(int modelId, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3) {
        return SampNativeFunction.addPlayerClass(modelId, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3);
    }

    @Override
    public int addPlayerClass(int teamId, int modelId, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3) {
        return SampNativeFunction.addPlayerClassEx(teamId, modelId, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3);
    }

    @Override
    public int addPlayerClass(int modelId, Vector3D position, float angle, WeaponModel weapon1, int ammo1, WeaponModel weapon2, int ammo2, WeaponModel weapon3, int ammo3) {
        return SampNativeFunction.addPlayerClass(modelId, position.x, position.y, position.z, angle, weapon1.getId(), ammo1, weapon2.getId(), ammo2, weapon3.getId(), ammo3);
    }

    @Override
    public int addPlayerClass(int teamId, int modelId, Vector3D position, float angle, WeaponModel weapon1, int ammo1, WeaponModel weapon2, int ammo2, WeaponModel weapon3, int ammo3) {
        return SampNativeFunction.addPlayerClassEx(teamId, modelId, position.x, position.y, position.z, angle, weapon1.getId(), ammo1, weapon2.getId(), ammo2, weapon3.getId(), ammo3);
    }

    @Override
    public int addPlayerClass(int modelId, Vector3D position, float angle, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3) {
        return SampNativeFunction.addPlayerClass(modelId, position.x, position.y, position.z, angle, weapon1.getModel().getId(), weapon1.getAmmo(), weapon2.getModel().getId(), weapon2.getAmmo(), weapon3.getModel().getId(), weapon3.getAmmo());
    }

    @Override
    public int addPlayerClass(int teamId, int modelId, Vector3D position, float angle, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3) {
        return SampNativeFunction.addPlayerClassEx(teamId, modelId, position.x, position.y, position.z, angle, weapon1.getModel().getId(), weapon1.getAmmo(), weapon2.getModel().getId(), weapon2.getAmmo(), weapon3.getModel().getId(), weapon3.getAmmo());
    }

    @Override
    public int addPlayerClass(SpawnInfo spawnInfo) {
        AngledLocation loc = spawnInfo.getLocation();
        WeaponData weapon1 = spawnInfo.getWeapon1();
        WeaponData weapon2 = spawnInfo.getWeapon2();
        WeaponData weapon3 = spawnInfo.getWeapon3();

        return SampNativeFunction.addPlayerClassEx(spawnInfo.getTeamId(), spawnInfo.getSkinId(), loc.getX(), loc.getY(), loc.getZ(), loc.getAngle(), weapon1.getModel().getId(), weapon1.getAmmo(), weapon2.getModel().getId(), weapon2.getAmmo(), weapon3.getModel().getId(), weapon3.getAmmo());
    }

    @Override
    public float getChatRadius() {
        return chatRadius;
    }

    @Override
    public void setChatRadius(float radius) {
        SampNativeFunction.limitGlobalChatRadius(radius);
    }

    @Override
    public float getPlayerMarkerRadius() {
        return playerMarkerRadius;
    }

    @Override
    public void setPlayerMarkerRadius(float radius) {
        SampNativeFunction.limitPlayerMarkerRadius(radius);
    }

    @Override
    public int getWeather() {
        return SampNativeFunction.getServerVarAsInt("weather");
    }

    @Override
    public void setWeather(int weatherid) {
        SampNativeFunction.setWeather(weatherid);
    }

    @Override
    public float getGravity() {
        return Float.parseFloat(SampNativeFunction.getServerVarAsString("gravity"));
    }

    @Override
    public void setGravity(float gravity) {
        SampNativeFunction.setGravity(gravity);
    }

    @Override
    public void setWorldTime(int hour) {
        SampNativeFunction.setWorldTime(hour);
    }

    @Override
    public float getNameTagDrawDistance() {
        return nameTagDrawDistance;
    }

    @Override
    public void setNameTagDrawDistance(float distance) {
        nameTagDrawDistance = distance;
        SampNativeFunction.setNameTagDrawDistance(distance);
    }

    @Override
    public void showNameTags(boolean enabled) {
        SampNativeFunction.showNameTags(enabled);
    }

    @Override
    public void showPlayerMarkers(PlayerMarkerMode mode) {
        SampNativeFunction.showPlayerMarkers(mode.getValue());
    }

    @Override
    public void enableTirePopping(boolean enabled) {
        SampNativeFunction.enableTirePopping(enabled);
    }

    @Override
    public void enableVehicleFriendlyFire() {
        SampNativeFunction.enableVehicleFriendlyFire();
    }

    @Override
    public void allowInteriorWeapons(boolean allow) {
        SampNativeFunction.allowInteriorWeapons(allow);
    }

    @Override
    public void createExplosion(Location location, int type, float radius) {
        SampNativeFunction.createExplosion(location.getX(), location.getY(), location.getZ(), type, radius);
    }

    @Override
    public void enableZoneNames(boolean enabled) {
        SampNativeFunction.enableZoneNames(enabled);
    }

    @Override
    public void usePlayerPedAnims() {
        SampNativeFunction.usePlayerPedAnims();
    }

    @Override
    public void disableInteriorEnterExits() {
        SampNativeFunction.disableInteriorEnterExits();
    }

    @Override
    public void disableNameTagLOS() {
        SampNativeFunction.disableNameTagLOS();
    }

    @Override
    public void enableStuntBonusForAll(boolean enabled) {
        SampNativeFunction.enableStuntBonusForAll(enabled);
    }

    @Override
    public void manualEngineAndLights() {
        SampNativeFunction.manualVehicleEngineAndLights();
    }
}
