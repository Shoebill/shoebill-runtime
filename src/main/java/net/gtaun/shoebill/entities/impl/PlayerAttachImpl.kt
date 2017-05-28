/**
 * Copyright (C) 2011 JoJLlmAn
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
import net.gtaun.shoebill.constant.PlayerAttachBone
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.PlayerAttach
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author JoJLlmAn
 * @author MK124
 * @author Marvin Haschker
 */
class PlayerAttachImpl internal constructor(override val player: Player) : PlayerAttach() {

    private val slots: Array<PlayerAttachSlot>

    init {
        val list = mutableListOf<PlayerAttachSlot>()
        (0..PlayerAttach.MAX_ATTACHED_OBJECTS - 1).forEach { i -> list.add(i, PlayerAttachSlotImpl(i)) }
        slots = list.toTypedArray()
    }

    override fun getSlotByBone(bone: PlayerAttachBone): PlayerAttach.PlayerAttachSlot? =
            slots.filter { it.bone == bone }.firstOrNull()

    override fun get(value: Int): PlayerAttachSlot? = slots[value]

    override fun get(): Collection<PlayerAttachSlot> = slots.asList()

    inner class PlayerAttachSlotImpl internal constructor(override val slot: Int) : PlayerAttach.PlayerAttachSlot {

        override var bone = PlayerAttachBone.NOT_USABLE
            private set
        override var modelId = 0
            private set
        override var offset: Vector3D = Vector3D()
        override var rotation: Vector3D = Vector3D()
        override var scale: Vector3D = Vector3D()

        override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("player", player)
                .append("slot", slot)
                .append("bone", bone)
                .append("modelId", modelId).toString()

        override val player: Player
            get() = this@PlayerAttachImpl.player

        override fun set(bone: PlayerAttachBone, modelId: Int, offset: Vector3D, rotation: Vector3D, scale: Vector3D,
                         materialColor1: Int, materialColor2: Int): Boolean {
            if (!player.isOnline) return false
            if (bone == PlayerAttachBone.NOT_USABLE) return false

            if(!SampNativeFunction.setPlayerAttachedObject(player.id, slot, modelId, bone.value, offset.x, offset.y,
                    offset.z, rotation.x, rotation.y, rotation.z, scale.x, scale.y, scale.z,
                    materialColor1, materialColor2)) return false

            this.bone = bone
            this.modelId = modelId

            this.offset = Vector3D(offset)
            this.rotation = Vector3D(rotation)
            this.scale = Vector3D(scale)

            return true
        }

        override fun remove(): Boolean {
            if (!player.isOnline) return false
            if (bone == PlayerAttachBone.NOT_USABLE) return false

            SampNativeFunction.removePlayerAttachedObject(player.id, slot)

            bone = PlayerAttachBone.NOT_USABLE
            modelId = 0

            offset = Vector3D()
            rotation = Vector3D()
            scale = Vector3D()

            return true
        }

        override val isUsed: Boolean
            get() = player.isOnline && bone != PlayerAttachBone.NOT_USABLE

        override fun edit(): Boolean {
            return player.isOnline && SampNativeFunction.editAttachedObject(player.id, slot)
        }
    }
}
