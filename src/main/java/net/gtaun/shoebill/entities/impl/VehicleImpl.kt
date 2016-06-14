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
import net.gtaun.shoebill.constant.VehicleModel
import net.gtaun.shoebill.data.*
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.entities.VehicleParam
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.event.player.PlayerStateChangeEvent
import net.gtaun.shoebill.event.vehicle.VehicleCreateEvent
import net.gtaun.shoebill.event.vehicle.VehicleSpawnEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.EventManagerNode
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author MK124
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class VehicleImpl @Throws(CreationFailedException::class)
constructor(eventManager: EventManager, private val store: SampObjectStoreImpl, modelId: Int, loc: AngledLocation,
            color1: Int, color2: Int, respawnDelay: Int, addSiren: Boolean) : Vehicle() {

    override var isStatic = false
        private set

    override var id = Vehicle.INVALID_ID
        private set

    override var modelId: Int = modelId
        private set

    override var interior: Int = loc.interiorId
        set(interiorId) {
            SampNativeFunction.linkVehicleToInterior(id, interiorId)
            field = interiorId
        }

    override var color1: Int = color1
        private set

    override var color2: Int = color2
        private set

    override var respawnDelay: Int = respawnDelay
        private set

    private var hasSiren: Boolean = addSiren

    override var world: Int
        get() {
            if (isDestroyed) return 0
            return SampNativeFunction.getVehicleVirtualWorld(id)
        }
        set(worldId) {
            if (isDestroyed) return
            SampNativeFunction.setVehicleVirtualWorld(id, worldId)
        }

    private var param: VehicleParamImpl
    lateinit override var component: VehicleComponentImpl
    lateinit override var damage: VehicleDamageImpl

    private val eventManagerNode: EventManagerNode = eventManager.createChildNode()

    init {
        when (modelId) {
            537, 538, 569, 570, 590 -> {
                this.id = SampNativeFunction.addStaticVehicleEx(modelId, loc.x, loc.y, loc.z, loc.angle, color1,
                        color2, respawnDelay, addSiren)
                isStatic = true
            }
            else ->
                this.id = SampNativeFunction.createVehicle(modelId, loc.x, loc.y, loc.z, loc.angle, color1, color2,
                        respawnDelay, addSiren)
        }

        if (id == Vehicle.INVALID_ID) throw CreationFailedException()
        store.setVehicle(id, this)

        world = loc.worldId

        SampNativeFunction.linkVehicleToInterior(id, interior)
        SampNativeFunction.setVehicleVirtualWorld(id, world)

        param = VehicleParamImpl(this)
        component = VehicleComponentImpl(this)
        damage = VehicleDamageImpl(this)

        val createEvent = VehicleCreateEvent(this)
        eventManagerNode.dispatchEvent(createEvent, this)

        val event = VehicleSpawnEvent(this)
        eventManagerNode.dispatchEvent(event, this)
    }

    fun onVehicleMod() {
        component.update()
    }

    fun onVehicleUpdateDamage() {
        SampNativeFunction.getVehicleDamageStatus(id, damage)
    }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("id", id)
            .append("isStatic", isStatic)
            .append("modelId", modelId)
            .toString()

    override fun destroy() {
        if (isDestroyed) return
        if (isStatic) return

        SampNativeFunction.destroyVehicle(id)
        val destroyEvent = DestroyEvent(this)
        eventManagerNode.dispatchEvent(destroyEvent, this)

        eventManagerNode.destroy()
        id = Vehicle.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() = id == Vehicle.INVALID_ID

    override val modelName: String
        get() = VehicleModel.get(modelId)!!.modelName

    override val state: VehicleParam
        get() = param

    override var location: AngledLocation
        get() {
            if (isDestroyed) return AngledLocation()

            val location = AngledLocation()

            SampNativeFunction.getVehiclePos(id, location)
            location.angle = SampNativeFunction.getVehicleZAngle(id)
            location.interiorId = interior
            location.worldId = SampNativeFunction.getVehicleVirtualWorld(id)

            return location
        }
        set(loc) {
            if (isDestroyed) return

            SampNativeFunction.setVehiclePos(id, loc.x, loc.y, loc.z)
            SampNativeFunction.setVehicleZAngle(id, loc.angle)
            SampNativeFunction.linkVehicleToInterior(id, loc.interiorId)
            SampNativeFunction.setVehicleVirtualWorld(id, loc.worldId)
        }

    override fun setLocation(x: Float, y: Float, z: Float) {
        if (isDestroyed) return

        val loc = location.clone()
        loc.set(x, y, z)
        location = loc
    }

    override fun setLocation(pos: Vector3D) {
        setLocation(pos.x, pos.y, pos.z)
    }

    override fun setLocation(loc: Location) {
        if (isDestroyed) return

        val newLoc = location.clone()
        newLoc.set(loc)
        location = newLoc
    }

    override var angle: Float
        get() = SampNativeFunction.getVehicleZAngle(id)
        set(angle) = SampNativeFunction.setVehicleZAngle(id, angle)

    override val rotationQuat: Quaternion
        get() {
            val quaternions = Quaternion()
            SampNativeFunction.getVehicleRotationQuat(id, quaternions)
            return quaternions
        }

    override var health: Float
        get() = SampNativeFunction.getVehicleHealth(id)
        set(health) = SampNativeFunction.setVehicleHealth(id, health)

    override var velocity: Velocity
        get() {
            val velocity = Velocity()
            SampNativeFunction.getVehicleVelocity(id, velocity)
            return velocity
        }
        set(velocity) = SampNativeFunction.setVehicleVelocity(id, velocity.x, velocity.y, velocity.z)

    override fun putPlayer(player: Player, seat: Int) {
        if (!player.isOnline) return

        val state = player.state
        val isInAnyVehicle = player.isInAnyVehicle

        if (isInAnyVehicle) player.removeFromVehicle()
        SampNativeFunction.putPlayerInVehicle(player.id, id, seat)

        if (isInAnyVehicle && state == player.state) {
            val event = PlayerStateChangeEvent(player, state)
            eventManagerNode.dispatchEvent(event, player)
        }
    }

    override fun isPlayerIn(player: Player): Boolean = !isDestroyed && player.isOnline &&
            SampNativeFunction.isPlayerInVehicle(player.id, id)

    override fun isStreamedIn(forPlayer: Player): Boolean = !isDestroyed && forPlayer.isOnline &&
            SampNativeFunction.isVehicleStreamedIn(id, forPlayer.id)

    override fun setParamsForPlayer(player: Player, objective: Boolean, doorsLocked: Boolean) {
        if (!player.isOnline) return

        SampNativeFunction.setVehicleParamsForPlayer(id, player.id, objective, doorsLocked)
    }

    override fun respawn() = SampNativeFunction.setVehicleToRespawn(id)

    override fun setColor(color1: Int, color2: Int) {
        SampNativeFunction.changeVehicleColor(id, color1, color2)
        this.color1 = color1
        this.color2 = color2
    }

    override fun setPaintjob(paintjobId: Int) = SampNativeFunction.changeVehiclePaintjob(id, paintjobId)

    override val trailer: Vehicle?
        get() {
            val trailerId = SampNativeFunction.getVehicleTrailer(id)
            return store.getVehicle(trailerId)
        }

    override fun attachTrailer(trailer: Vehicle) {
        if (trailer.isDestroyed) return

        SampNativeFunction.attachTrailerToVehicle(trailer.id, id)
    }

    override fun detachTrailer() = SampNativeFunction.detachTrailerFromVehicle(id)

    override val isTrailerAttached: Boolean
        get() = !isDestroyed && SampNativeFunction.isTrailerAttachedToVehicle(id)

    override fun setNumberPlate(number: String) = SampNativeFunction.setVehicleNumberPlate(id, number)

    override fun repair() = SampNativeFunction.repairVehicle(id)

    override var doors: VehicleState
        get() {
            val params = VehicleState()
            SampNativeFunction.getVehicleParamsCarDoors(id, params)
            return params
        }
        set(vehicleState) = SampNativeFunction.setVehicleParamsCarDoors(id, vehicleState.driver,
                vehicleState.passenger, vehicleState.backLeft, vehicleState.backRight)

    override var windows: VehicleState
        get() {
            val params = VehicleState()
            SampNativeFunction.getVehicleParamsCarWindows(id, params)
            return params
        }
        set(vehicleState) = SampNativeFunction.setVehicleParamsCarWindows(id, vehicleState.driver,
                vehicleState.passenger, vehicleState.backLeft, vehicleState.backRight)

    override val sirenState: Int
        get() = SampNativeFunction.getVehicleParamsSirenState(id)

    override fun setAngularVelocity(velocity: Velocity) = SampNativeFunction.setVehicleAngularVelocity(id,
            velocity.x, velocity.y, velocity.z)

    override fun hasSiren(): Boolean = hasSiren
}
