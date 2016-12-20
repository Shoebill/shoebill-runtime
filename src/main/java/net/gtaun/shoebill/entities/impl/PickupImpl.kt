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
import net.gtaun.shoebill.data.Location
import net.gtaun.shoebill.entities.Pickup
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.event.player.PlayerPickupEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventHandler
import net.gtaun.util.event.EventManager

/**
 * @author MK124
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class PickupImpl @Throws(CreationFailedException::class)
@JvmOverloads constructor(private val rootEventManager: EventManager, store: SampObjectStoreImpl,
                          override val modelId: Int, override val type: Int, loc: Location,
                          val pickupHandler: EventHandler<PlayerPickupEvent>?,
                          override val isStatic: Boolean = false) : Pickup() {

    override val location: Location = loc
        get() = field.clone()

    override var id =
            if(!isStatic) SampNativeFunction.createPickup(modelId, type, loc.x, loc.y, loc.z, loc.worldId)
            else SampNativeFunction.addStaticPickup(modelId, type, loc.x, loc.y, loc.z, loc.worldId)
        private set

    init {
        if (id == Pickup.INVALID_ID) throw CreationFailedException()
        store.setPickup(id, this)
    }

    override fun destroy() {
        if (isDestroyed) return

        SampNativeFunction.destroyPickup(id)
        val destroyEvent = DestroyEvent(this)
        rootEventManager.dispatchEvent(destroyEvent, this)

        id = Pickup.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() = id == Pickup.INVALID_ID
}
