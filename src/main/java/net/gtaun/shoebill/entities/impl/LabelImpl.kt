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
import net.gtaun.shoebill.data.Color
import net.gtaun.shoebill.data.Location
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Label
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import org.apache.commons.lang3.StringUtils

/**
 * @author MK124
 * @author Marvin Haschker
 */
class LabelImpl @Throws(CreationFailedException::class)
constructor(private val rootEventManager: EventManager, store: SampObjectStoreImpl, text: String, color: Color,
            loc: Location, override val drawDistance: Float, testLOS: Boolean) : Label() {

    override var color: Color = color
        private set

    override val location: Location = loc
        get() {
            if (isDestroyed) return Location()

            var loc: Location? = null

            if (attachedPlayer != null) loc = attachedPlayer!!.location
            if (attachedVehicle != null) loc = attachedVehicle!!.location

            if (loc != null) {
                field.set(loc.x + offset!!.x, loc.y + offset!!.y, loc.z + offset!!.z, loc.interiorId, loc.worldId)
            }

            return field.clone()
        }

    override var id = SampNativeFunction.create3DTextLabel(text, color.value, location.x, location.y,
            location.z, drawDistance, location.worldId, testLOS)
        private set

    override var text: String = text
        private set(value) {
            if(StringUtils.isEmpty(value)) field = " "
            else field = value
        }

    private var offset: Vector3D? = null

    override var attachedPlayer: Player? = null
        private set

    override var attachedVehicle: Vehicle? = null
        private set

    init {
        if (id == Label.INVALID_ID) throw CreationFailedException()
        store.setLabel(id, this)
    }

    override fun destroy() {
        if (isDestroyed) return

        SampNativeFunction.delete3DTextLabel(id)
        val destroyEvent = DestroyEvent(this)
        rootEventManager.dispatchEvent(destroyEvent, this)

        id = Label.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() = id == Label.INVALID_ID

    override fun attach(player: Player, offset: Vector3D) {
        if (!player.isOnline) return

        SampNativeFunction.attach3DTextLabelToPlayer(id, player.id, offset.x, offset.y, offset.z)
        this.offset = offset
        attachedPlayer = player
        attachedVehicle = null
    }

    override fun attach(vehicle: Vehicle, offset: Vector3D) {
        if (vehicle.isDestroyed) return

        SampNativeFunction.attach3DTextLabelToVehicle(id, vehicle.id, offset.x, offset.y, offset.z)
        this.offset = offset
        attachedPlayer = null
        attachedVehicle = vehicle
    }

    override fun update(color: Color, text: String) {
        this.color.set(color)
        this.text = text
        SampNativeFunction.update3DTextLabelText(id, color.value, text)
    }
}
