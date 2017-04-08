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
import net.gtaun.shoebill.data.Area
import net.gtaun.shoebill.data.Color
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.Zone
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager

/**
 * @author MK124
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class ZoneImpl constructor(store: SampObjectStoreImpl, eventManager: EventManager, minX: Float, minY: Float,
                           maxX: Float, maxY: Float) : Zone() {

    private val eventManagerNode: EventManager = eventManager.createChildNode()

    override var id = SampNativeFunction.gangZoneCreate(minX, minY, maxX, maxY)
        private set

    override var area: Area = Area(minX, minY, maxX, maxY)
        get() = field.clone()
        set(value) = field.set(value)

    private val isShownForPlayer = BooleanArray(SampObjectStoreImpl.MAX_PLAYERS)
    private val isFlashingForPlayer = BooleanArray(SampObjectStoreImpl.MAX_PLAYERS)

    init {
        if (id == Zone.INVALID_ID) throw CreationFailedException()
        store.setZone(id, this)
    }

    override fun destroy() {
        if (isDestroyed) return

        SampNativeFunction.gangZoneDestroy(id)
        val destroyEvent = DestroyEvent(this)
        eventManagerNode.dispatchEvent(destroyEvent, this)

        id = Zone.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() = id == Zone.INVALID_ID

    override fun show(player: Player, color: Color) {
        if (!player.isOnline) return

        SampNativeFunction.gangZoneShowForPlayer(player.id, id, color.value)
        isShownForPlayer[player.id] = true
        isFlashingForPlayer[player.id] = false
    }

    override fun hide(player: Player) {
        if (!player.isOnline) return

        SampNativeFunction.gangZoneHideForPlayer(player.id, id)
        isShownForPlayer[player.id] = false
        isFlashingForPlayer[player.id] = false
    }

    override fun flash(player: Player, color: Color) {
        if (!player.isOnline) return

        if (isShownForPlayer[player.id]) {
            SampNativeFunction.gangZoneFlashForPlayer(player.id, id, color.value)
            isFlashingForPlayer[player.id] = true
        }
    }

    override fun stopFlash(player: Player) {
        if (!player.isOnline) return

        if(isShownForPlayer[player.id]) {
            SampNativeFunction.gangZoneStopFlashForPlayer(player.id, id)
            isFlashingForPlayer[player.id] = false
        }
    }

    override fun showForAll(color: Color) {
        SampNativeFunction.gangZoneShowForAll(id, color.value)
        for (i in isShownForPlayer.indices)
            isShownForPlayer[i] = true
    }

    override fun hideForAll() {
        SampNativeFunction.gangZoneHideForAll(id)
        for (i in isShownForPlayer.indices) {
            isShownForPlayer[i] = false
            isFlashingForPlayer[i] = false
        }
    }

    override fun flashForAll(color: Color) {
        SampNativeFunction.gangZoneFlashForAll(id, color.value)
        isShownForPlayer.indices.filter { isShownForPlayer[it] }
                .forEach { isFlashingForPlayer[it] = true }
    }

    override fun stopFlashForAll() {
        SampNativeFunction.gangZoneStopFlashForAll(id)
        isFlashingForPlayer.indices.forEach { isFlashingForPlayer[it] = false }
    }

    override fun isInRange(pos: Vector3D): Boolean = area.isInRange(pos)
}
