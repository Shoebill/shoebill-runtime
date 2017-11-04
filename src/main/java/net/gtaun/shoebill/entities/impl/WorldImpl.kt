/**
 * Copyright (C) 2011-2014 MK124

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.entities.impl

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.SampObjectStoreImpl
import net.gtaun.shoebill.constant.PlayerMarkerMode
import net.gtaun.shoebill.constant.WeaponModel
import net.gtaun.shoebill.constant.Weather
import net.gtaun.shoebill.data.Location
import net.gtaun.shoebill.data.SpawnInfo
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.data.WeaponData
import net.gtaun.shoebill.entities.World

/**
 * @author MK124
 * @author Marvin Haschker
 */
class WorldImpl(private val store: SampObjectStoreImpl) : World() {

    override var nameTagDrawDistance = 70f
        set(distance) {
            SampNativeFunction.setNameTagDrawDistance(distance)
            field = distance
        }

    override var chatRadius = -1f
        set(radius) {
            SampNativeFunction.limitGlobalChatRadius(radius)
            field = radius
        }

    override var playerMarkerRadius = -1f
        set(radius) {
            SampNativeFunction.limitPlayerMarkerRadius(radius)
            field = radius
        }

    override var weather = Weather.SUNNY_VEGAS
        set(value) {
            SampNativeFunction.setWeather(value.id)
            field = value
        }

    override var teamCount: Int = 1
        set(value) {
            SampNativeFunction.setTeamCount(value)
            field = value
        }

    override fun addPlayerClass(modelId: Int, x: Float, y: Float, z: Float, angle: Float, weapon1: Int, ammo1: Int,
                                weapon2: Int, ammo2: Int, weapon3: Int, ammo3: Int): Int {
        val id = SampNativeFunction.addPlayerClass(modelId, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3)
        store.setPlayerClass(id, SpawnInfo(x, y, z, 0, 0, angle, modelId, 0, WeaponData(WeaponModel.get(weapon1)!!, ammo1),
                WeaponData(WeaponModel.get(weapon2)!!, ammo2), WeaponData(WeaponModel.get(weapon3)!!, ammo3)))
        return id
    }

    override fun addPlayerClass(teamId: Int, modelId: Int, x: Float, y: Float, z: Float, angle: Float, weapon1: Int,
                                ammo1: Int, weapon2: Int, ammo2: Int, weapon3: Int, ammo3: Int): Int {
        val id = SampNativeFunction.addPlayerClassEx(teamId, modelId, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3)
        store.setPlayerClass(id, SpawnInfo(x, y, z, 0, 0, angle, modelId, teamId, WeaponData(WeaponModel.get(weapon1)!!, ammo1),
                WeaponData(WeaponModel.get(weapon2)!!, ammo2), WeaponData(WeaponModel.get(weapon3)!!, ammo3)))
        return id
    }

    override fun addPlayerClass(modelId: Int, position: Vector3D, angle: Float, weapon1: WeaponModel, ammo1: Int,
                                weapon2: WeaponModel, ammo2: Int, weapon3: WeaponModel, ammo3: Int): Int {
        val id = SampNativeFunction.addPlayerClass(modelId, position.x, position.y, position.z, angle, weapon1.id,
                ammo1, weapon2.id, ammo2, weapon3.id, ammo3)
        store.setPlayerClass(id, SpawnInfo(position, 0, 0, angle, modelId, 0, WeaponData(weapon1, ammo1),
                WeaponData(weapon2, ammo2), WeaponData(weapon3, ammo3)))
        return id
    }

    override fun addPlayerClass(teamId: Int, modelId: Int, position: Vector3D, angle: Float, weapon1: WeaponModel,
                                ammo1: Int,
                                weapon2: WeaponModel, ammo2: Int, weapon3: WeaponModel, ammo3: Int): Int {
        val id = SampNativeFunction.addPlayerClassEx(teamId, modelId, position.x, position.y, position.z, angle,
                weapon1.id, ammo1, weapon2.id, ammo2, weapon3.id, ammo3)
        store.setPlayerClass(id, SpawnInfo(position, 0, 0, angle, modelId, teamId, WeaponData(weapon1, ammo1),
                WeaponData(weapon2, ammo2), WeaponData(weapon3, ammo3)))
        return id
    }

    override fun addPlayerClass(modelId: Int, position: Vector3D, angle: Float, weapon1: WeaponData, weapon2: WeaponData,
                                weapon3: WeaponData): Int {
        val id = SampNativeFunction.addPlayerClass(modelId, position.x, position.y, position.z, angle, weapon1.model.id,
                weapon1.ammo, weapon2.model.id, weapon2.ammo, weapon3.model.id, weapon3.ammo)
        store.setPlayerClass(id, SpawnInfo(position, 0, 0, angle, modelId, 0,
                WeaponData(weapon1.model, weapon1.ammo), WeaponData(weapon2.model, weapon2.ammo),
                WeaponData(weapon3.model, weapon3.ammo)))
        return id
    }

    override fun addPlayerClass(teamId: Int, modelId: Int, position: Vector3D, angle: Float, weapon1: WeaponData,
                                weapon2: WeaponData, weapon3: WeaponData): Int {
        val id = SampNativeFunction.addPlayerClassEx(teamId, modelId, position.x, position.y, position.z, angle,
                weapon1.model.id, weapon1.ammo, weapon2.model.id, weapon2.ammo, weapon3.model.id, weapon3.ammo)
        store.setPlayerClass(id, SpawnInfo(position, 0, 0, angle, modelId, teamId,
                WeaponData(weapon1.model, weapon1.ammo), WeaponData(weapon2.model, weapon2.ammo),
                WeaponData(weapon3.model, weapon3.ammo)))
        return id
    }

    override fun addPlayerClass(spawnInfo: SpawnInfo): Int {
        val loc = spawnInfo.location
        val weapon1 = spawnInfo.weapon1
        val weapon2 = spawnInfo.weapon2
        val weapon3 = spawnInfo.weapon3
        val id = SampNativeFunction.addPlayerClassEx(spawnInfo.teamId, spawnInfo.skinId, loc.x, loc.y, loc.z,
                loc.angle, weapon1.model.id, weapon1.ammo, weapon2.model.id, weapon2.ammo, weapon3.model.id, weapon3.ammo)
        store.setPlayerClass(id, spawnInfo)
        return id
    }

    override var gravity: Float
        get() = java.lang.Float.parseFloat(SampNativeFunction.getConsoleVarAsString("gravity"))
        set(gravity) = SampNativeFunction.setGravity(gravity)

    override fun setWorldTime(hour: Int) = SampNativeFunction.setWorldTime(hour)

    override fun showNameTags(enabled: Boolean) = SampNativeFunction.showNameTags(enabled)

    override fun showPlayerMarkers(mode: PlayerMarkerMode) = SampNativeFunction.showPlayerMarkers(mode.value)

    override fun enableTirePopping(enabled: Boolean) = SampNativeFunction.enableTirePopping(enabled)

    override fun enableVehicleFriendlyFire() = SampNativeFunction.enableVehicleFriendlyFire()

    override fun allowInteriorWeapons(allow: Boolean) = SampNativeFunction.allowInteriorWeapons(allow)

    override fun createExplosion(location: Location, type: Int, radius: Float) =
            SampNativeFunction.createExplosion(location.x, location.y, location.z, type, radius)

    override fun enableZoneNames(enabled: Boolean) = SampNativeFunction.enableZoneNames(enabled)

    override fun usePlayerPedAnims() = SampNativeFunction.usePlayerPedAnims()

    override fun disableInteriorEnterExits() = SampNativeFunction.disableInteriorEnterExits()

    override fun disableNameTagLOS() = SampNativeFunction.disableNameTagLOS()

    override fun enableStuntBonusForAll(enabled: Boolean) = SampNativeFunction.enableStuntBonusForAll(enabled)

    override fun manualEngineAndLights() = SampNativeFunction.manualVehicleEngineAndLights()

    override fun setObjectsDefaultCameraCol(disable: Boolean) =
            SampNativeFunction.setObjectsDefaultCameraCol(if (disable) 1 else 0)
}
