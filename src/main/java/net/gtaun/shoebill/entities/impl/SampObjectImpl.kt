/**
 * Copyright (C) 2011-2014 MK124
 * Copyright (C) 2011 JoJLlmAn

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
import net.gtaun.shoebill.entities.SampObject
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.EventManagerNode

/**
 * @author MK124
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class SampObjectImpl @Throws(CreationFailedException::class)
constructor(eventManager: EventManager, store: SampObjectStoreImpl, override val modelId: Int,
                          loc: Location, rot: Vector3D, drawDistance: Float) : SampObject() {

    override var id = SampNativeFunction.createObject(modelId, loc.x, loc.y, loc.z, rot.x, rot.y, rot.z, drawDistance)
        private set

    override var location: Location = Location(loc)
        get() {
            if (attachedPlayer != null) field.set(attachedPlayer!!.location)
            else if (attachedVehicle != null) field.set(attachedVehicle!!.location)
            else SampNativeFunction.getObjectPos(id, field)

            return field.clone()
        }
        set(value) {
            field.set(value)
            speed = 0.0f
            SampNativeFunction.setObjectPos(id, value.x, value.y, value.z)
        }

    override var speed = 0.0f
        get() {
            if (isDestroyed) return 0.0f

            if (attachedPlayer != null && attachedPlayer!!.isOnline) return attachedPlayer!!.velocity.speed3d
            if (attachedObject != null && !attachedObject!!.isDestroyed) return attachedObject!!.speed
            if (attachedVehicle != null && !attachedVehicle!!.isDestroyed)
                return attachedVehicle!!.velocity.speed3d

            return field
        }

    override val drawDistance = drawDistance

    private var attachedOffset: Vector3D? = null

    private var attachedRotate: Vector3D? = null

    override var attachedPlayer: Player? = null
        private set
    override var attachedObject: SampObject? = null
        private set
    override var attachedVehicle: Vehicle? = null
        private set

    private val eventManagerNode: EventManagerNode = eventManager.createChildNode()

    init {
        if (id == SampObject.INVALID_ID) throw CreationFailedException()
        store.setObject(id, this)
    }

    fun onObjectMoved() {
        speed = 0.0f
        SampNativeFunction.getObjectPos(id, location)
    }

    override fun destroy() {
        if (isDestroyed) return

        SampNativeFunction.destroyObject(id)

        val destroyEvent = DestroyEvent(this)
        eventManagerNode.dispatchEvent(destroyEvent, this)

        eventManagerNode.destroy()
        id = SampObject.INVALID_ID
    }


    override val isDestroyed: Boolean
        get() = id == SampObject.INVALID_ID

    override var rotation: Vector3D
        get() {
            val rotate = Vector3D()
            SampNativeFunction.getObjectRot(id, rotate)

            return rotate
        }
        set(rot) {
            SampNativeFunction.setObjectRot(id, rot.x, rot.y, rot.z)
        }

    override val isMoving: Boolean
        get() = !isDestroyed && SampNativeFunction.isObjectMoving(id)

    override fun move(x: Float, y: Float, z: Float, speed: Float): Int {
        if (attachedPlayer == null && attachedVehicle == null) this.speed = speed
        return SampNativeFunction.moveObject(id, x, y, z, speed, -1000.0f, -1000.0f, -1000.0f)
    }

    override fun move(x: Float, y: Float, z: Float, speed: Float, rx: Float, ry: Float, rz: Float): Int {
        if (attachedPlayer == null && attachedVehicle == null) this.speed = speed
        return SampNativeFunction.moveObject(id, x, y, z, speed, rx, ry, rz)
    }

    override fun move(pos: Vector3D, speed: Float): Int = move(pos.x, pos.y, pos.z, speed)

    override fun move(pos: Vector3D, speed: Float, rot: Vector3D): Int =
            move(pos.x, pos.y, pos.z, speed, rot.x, rot.y, rot.z)

    override fun stop() {
        speed = 0.0f
        SampNativeFunction.stopObject(id)
    }

    override fun attach(player: Player, x: Float, y: Float, z: Float, rx: Float, ry: Float, rz: Float) {
        if (!player.isOnline) return

        SampNativeFunction.attachObjectToPlayer(id, player.id, x, y, z, rx, ry, rz)

        speed = 0.0f
        attachedOffset = Vector3D(x, y, z)
        attachedRotate = Vector3D(rx, ry, rz)

        attachedPlayer = player
        attachedObject = null
        attachedVehicle = null
    }

    override fun attach(player: Player, offset: Vector3D, rot: Vector3D) {
        attach(player, offset.x, offset.y, offset.z, rot.x, rot.y, rot.z)
    }

    override fun attachCamera(player: Player) = SampNativeFunction.attachCameraToObject(id, player.id)

    override fun attach(`object`: SampObject, x: Float, y: Float, z: Float, rx: Float, ry: Float, rz: Float, isSyncRotation: Boolean) {
        if (`object`.isDestroyed) return

        if (`object` is PlayerObjectImpl) throw UnsupportedOperationException()
        SampNativeFunction.attachObjectToObject(id, `object`.id, x, y, z, rx, ry, rz, if (isSyncRotation) 1 else 0)

        speed = 0.0f
        attachedOffset = Vector3D(x, y, z)
        attachedRotate = Vector3D(rx, ry, rz)

        attachedPlayer = null
        attachedObject = `object`
        attachedVehicle = null
    }

    override fun attach(`object`: SampObject, offset: Vector3D, rot: Vector3D, isSyncRotation: Boolean) =
            attach(`object`, offset.x, offset.y, offset.z, rot.x, rot.y, rot.z, isSyncRotation)

    override fun attach(vehicle: Vehicle, x: Float, y: Float, z: Float, rx: Float, ry: Float, rz: Float) {
        if (vehicle.isDestroyed) return

        SampNativeFunction.attachObjectToVehicle(id, vehicle.id, x, y, z, rx, ry, rz)

        speed = 0.0f
        attachedOffset = Vector3D(x, y, z)
        attachedRotate = Vector3D(rx, ry, rz)

        attachedPlayer = null
        attachedObject = null
        attachedVehicle = vehicle
    }

    override fun attach(vehicle: Vehicle, offset: Vector3D, rot: Vector3D) =
            attach(vehicle, offset.x, offset.y, offset.z, rot.x, rot.y, rot.z)


    override fun setMaterial(materialIndex: Int, modelId: Int, txdName: String, textureName: String, materialColor: Color) {
        SampNativeFunction.setObjectMaterial(id, materialIndex, modelId, txdName, textureName, materialColor.rgbaValue)
    }

    override fun setMaterial(materialIndex: Int, modelId: Int, txdName: String, textureName: String) =
            setMaterial(materialIndex, modelId, txdName, textureName, Color.TRANSPARENT)

    override fun setMaterialText(text: String, materialIndex: Int, materialSize: ObjectMaterialSize, fontFace: String,
                                 fontSize: Int, isBold: Boolean, fontColor: Color, backColor: Color,
                                 textAlignment: ObjectMaterialTextAlign) =
            SampNativeFunction.setObjectMaterialText(id, text, materialIndex, materialSize.value, fontFace, fontSize,
                    if (isBold) 1 else 0, fontColor.rgbaValue, backColor.rgbaValue, textAlignment.value)

    override fun setMaterialText(text: String) =
            setMaterialText(text, 0, ObjectMaterialSize.SIZE_256x128, "Arial", 24, true, Color.WHITE, Color.TRANSPARENT,
                    ObjectMaterialTextAlign.LEFT)

    override fun setNoCameraCol() {
        SampNativeFunction.setObjectNoCameraCol(id)
    }
}
