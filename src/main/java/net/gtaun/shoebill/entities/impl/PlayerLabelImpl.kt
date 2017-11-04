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
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.PlayerLabel
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import org.apache.commons.lang3.StringUtils

/**
 * @author MK124
 * @author Marvin Haschker
 */
class PlayerLabelImpl @Throws(CreationFailedException::class)
constructor(val eventManager: EventManager, val store: SampObjectStoreImpl, override val player: Player, text: String,
            override val color: Color, loc: Location, override val drawDistance: Float, testLOS: Boolean) :
        PlayerLabel() {

    override var text: String = text
        private set(value) {
            field = if(StringUtils.isEmpty(value)) " "
                    else value
        }

    override var location: Location = loc
        get() {
            var pos = Location()

            if (attachedPlayer != null) pos = attachedPlayer!!.location
            if (attachedVehicle != null) pos = attachedVehicle!!.location

            field.set(pos.x + offset!!.x, pos.y, pos.z + offset!!.z, pos.interiorId, pos.worldId)

            return field.clone()
        }

    override var id = SampNativeFunction.createPlayer3DTextLabel(player.id, text, color.value, location.x, location.y,
            location.z, drawDistance, Player.INVALID_ID, Vehicle.INVALID_ID, testLOS)
        private set

    private var offset: Vector3D? = null

    override var attachedPlayer: Player? = null
        private set

    override var attachedVehicle: Vehicle? = null
        private set

    init {
        if (id == PlayerLabel.INVALID_ID) throw CreationFailedException()
        store.setPlayerLabel(player, id, this)
    }

    override fun destroy() {
        if (isDestroyed) return

        if (player.isOnline) SampNativeFunction.deletePlayer3DTextLabel(player.id, id)

        val destroyEvent = DestroyEvent(this)
        eventManager.dispatchEvent(destroyEvent, this)

        id = PlayerLabel.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() {
            return id == PlayerLabel.INVALID_ID
        }

    override fun attach(player: Player, offset: Vector3D) = attach(player, offset.x, offset.y, offset.z)
    override fun attach(vehicle: Vehicle, offset: Vector3D) = attach(vehicle, offset.x, offset.y, offset.z)

    override fun update(color: Color, text: String) {
        if (isDestroyed) return

        SampNativeFunction.updatePlayer3DTextLabelText(player.id, id, color.value, text)
        this.color.set(color)
        this.text = text
    }
}
