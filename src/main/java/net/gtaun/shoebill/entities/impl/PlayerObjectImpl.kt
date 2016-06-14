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
import net.gtaun.shoebill.constant.ObjectMaterialSize
import net.gtaun.shoebill.constant.ObjectMaterialTextAlign
import net.gtaun.shoebill.data.Color
import net.gtaun.shoebill.data.Location
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.PlayerObject
import net.gtaun.shoebill.entities.SampObject
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.EventManagerNode
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author MK124
 * @author Marvin Haschker
 */
class PlayerObjectImpl @Throws(CreationFailedException::class)
constructor(eventManager: EventManager, store: SampObjectStoreImpl, override val player: Player,
                          override val modelId: Int, loc: Location, rot: Vector3D, drawDistance: Float) : PlayerObject() {

    override var id = SampNativeFunction.createPlayerObject(player.id, modelId, loc.x, loc.y, loc.z, rot.x,
            rot.y, rot.z, drawDistance)
        private set

    override var location: Location = loc.clone()
        get() {
            SampNativeFunction.getPlayerObjectPos(player.id, id, field)
            return field
        }
        set(value) {
            field.set(value)
            SampNativeFunction.setPlayerObjectPos(player.id, id, value.x, value.y, value.z)
        }

    override var speed: Float = 0.0f
        get() {
            if (attachedPlayer != null && attachedPlayer!!.isOnline) return attachedPlayer!!.velocity.speed3d
            if (attachedVehicle != null && !attachedVehicle!!.isDestroyed)
                return attachedVehicle!!.velocity.speed3d

            return field
        }
        private set

    override val drawDistance = drawDistance

    override var attachedPlayer: Player? = null
        private set
    override var attachedVehicle: Vehicle? = null
        private set

    private val eventManagerNode: EventManagerNode = eventManager.createChildNode()

    init {
        if (this.id == PlayerObject.INVALID_ID) throw CreationFailedException()
        store.setPlayerObject(player, id, this)
    }

    fun onPlayerObjectMoved() {
        speed = 0.0f
    }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("player", player)
            .append("id", id)
            .append("modelId", modelId)
            .toString()

    override fun destroy() {
        if (isDestroyed) return
        if (player.isOnline)
            SampNativeFunction.destroyPlayerObject(player.id, id)

        val destroyEvent = DestroyEvent(this)
        eventManagerNode.dispatchEvent(destroyEvent, this)

        eventManagerNode.destroy()
        id = PlayerObject.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() {
            if (!player.isOnline && id != PlayerObject.INVALID_ID) destroy()
            return id == PlayerObject.INVALID_ID
        }

    override val attachedObject: SampObject?
        get() = null

    override var rotation: Vector3D
        get() {
            if (isDestroyed) return Vector3D()

            val rotate = Vector3D()
            SampNativeFunction.getPlayerObjectRot(player.id, id, rotate)
            return rotate
        }
        set(rot) = setRotation(rot.x, rot.y, rot.z)

    fun setRotation(rx: Float, ry: Float, rz: Float) = SampNativeFunction.setPlayerObjectRot(player.id, id, rx, ry, rz)

    override val isMoving: Boolean
        get() = !isDestroyed && SampNativeFunction.isPlayerObjectMoving(player.id, id)

    override fun move(x: Float, y: Float, z: Float, speed: Float): Int {
        if (isDestroyed) return 0

        if (attachedPlayer == null && attachedVehicle == null) this.speed = speed
        return SampNativeFunction.movePlayerObject(player.id, id, x, y, z, speed, -1000.0f, -1000.0f, -1000.0f)
    }

    override fun move(x: Float, y: Float, z: Float, speed: Float, rx: Float, ry: Float, rz: Float): Int {
        if (isDestroyed) return 0

        if (attachedPlayer == null && attachedVehicle == null) this.speed = speed
        return SampNativeFunction.movePlayerObject(player.id, id, x, y, z, speed, rx, ry, rz)
    }

    override fun move(pos: Vector3D, speed: Float): Int = move(pos.x, pos.y, pos.z, speed)

    override fun move(pos: Vector3D, speed: Float, rot: Vector3D): Int =
            move(pos.x, pos.y, pos.z, speed, rot.x, rot.y, rot.z)

    override fun stop() {
        speed = 0.0f
        SampNativeFunction.stopPlayerObject(player.id, id)
    }

    override fun attach(player: Player, x: Float, y: Float, z: Float, rx: Float, ry: Float, rz: Float) {
        if (isDestroyed) return
        if (!player.isOnline) return

        SampNativeFunction.attachPlayerObjectToPlayer(player.id, id, player.id, x, y, z, rx, ry, rz)

        attachedPlayer = player
        attachedVehicle = null
        speed = 0.0f
    }

    override fun attach(player: Player, offset: Vector3D, rot: Vector3D) =
            attach(player, offset.x, offset.y, offset.z, rot.x, rot.y, rot.z)

    override fun attachCamera(player: Player) = SampNativeFunction.attachCameraToPlayerObject(player.id, id)

    override fun attach(`object`: SampObject, x: Float, y: Float, z: Float, rx: Float, ry: Float, rz: Float,
                        isSyncRotation: Boolean) {
        throw UnsupportedOperationException()
    }

    override fun attach(`object`: SampObject, offset: Vector3D, rot: Vector3D, isSyncRotation: Boolean) {
        throw UnsupportedOperationException()
    }

    override fun attach(vehicle: Vehicle, x: Float, y: Float, z: Float, rx: Float, ry: Float, rz: Float) {
        if (vehicle.isDestroyed) return

        SampNativeFunction.attachPlayerObjectToVehicle(player.id, id, vehicle.id, x, y, z, rx, ry, rz)

        attachedPlayer = null
        attachedVehicle = vehicle
        speed = 0.0f
    }

    override fun attach(vehicle: Vehicle, offset: Vector3D, rot: Vector3D) =
            attach(vehicle, offset.x, offset.y, offset.z, rot.x, rot.y, rot.z)

    override fun setMaterial(materialIndex: Int, modelId: Int, txdName: String, textureName: String,
                             materialColor: Color) =
            SampNativeFunction.setPlayerObjectMaterial(player.id, id, materialIndex, modelId, txdName, textureName,
                    materialColor.value)

    override fun setMaterial(materialIndex: Int, modelId: Int, txdName: String, textureName: String) =
            SampNativeFunction.setPlayerObjectMaterial(player.id, id, materialIndex, modelId, txdName, textureName, 0)

    override fun setMaterialText(text: String, materialIndex: Int, materialSize: ObjectMaterialSize, fontFace: String,
                                 fontSize: Int, isBold: Boolean, fontColor: Color, backColor: Color,
                                 textAlignment: ObjectMaterialTextAlign) =
            SampNativeFunction.setPlayerObjectMaterialText(player.id, id, text, materialIndex, materialSize.value,
                    fontFace, fontSize, if (isBold) 1 else 0, fontColor.value, backColor.value, textAlignment.value)

    override fun setMaterialText(text: String) =
            SampNativeFunction.setPlayerObjectMaterialText(player.id, id, text, 0,
                    ObjectMaterialSize.SIZE_256x128.value, "Arial", 24, 1, 0xFFFFFFFF.toInt(), 0, 0)

    override fun setNoCameraCol() {
        SampNativeFunction.setObjectNoCameraCol(id)
    }

    override fun setPlayerObjectNoCameraCol() {
        SampNativeFunction.setPlayerObjectNoCameraCol(player.id, id)
    }
}
