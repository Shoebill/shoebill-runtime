/**
 * Copyright (C) 2011 MK124

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
import net.gtaun.shoebill.constant.PlayerKey
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.PlayerKeyState
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author MK124
 * @author Marvin Haschker
 */
class PlayerKeyStateImpl(override val player: Player, override val keys: Int = 0) : PlayerKeyState {
    override val updownValue: Int = 0
    override val leftrightValue: Int = 0

    internal fun update() = SampNativeFunction.getPlayerKeys(player.id, this)

    override fun isKeyPressed(vararg keys: PlayerKey): Boolean {
        var value = 0
        keys.forEach { value = value or it.value }
        return this.keys and value == value
    }

    override fun isAccurateKeyPressed(vararg keys: PlayerKey): Boolean {
        var value = 0
        keys.forEach { value = value or it.value }
        return this.keys == value
    }

    override fun toString(): String {
        val builder = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
        builder.append("keys", keys)
        builder.append("upDown", updownValue)
        builder.append("leftRight", leftrightValue)
        builder.append("action", if (isKeyPressed(PlayerKey.ACTION)) 1 else 0)
        builder.append("crouch", if (isKeyPressed(PlayerKey.CROUCH)) 1 else 0)
        builder.append("fire", if (isKeyPressed(PlayerKey.FIRE)) 1 else 0)
        builder.append("sprint", if (isKeyPressed(PlayerKey.SPRINT)) 1 else 0)
        builder.append("secondAttack", if (isKeyPressed(PlayerKey.SECONDARY_ATTACK)) 1 else 0)
        builder.append("jump", if (isKeyPressed(PlayerKey.JUMP)) 1 else 0)
        builder.append("lookRight", if (isKeyPressed(PlayerKey.LOOK_RIGHT)) 1 else 0)
        builder.append("handbreak", if (isKeyPressed(PlayerKey.HANDBRAKE)) 1 else 0)
        builder.append("lookLeft", if (isKeyPressed(PlayerKey.LOOK_LEFT)) 1 else 0)
        builder.append("subMission", if (isKeyPressed(PlayerKey.SUBMISSION)) 1 else 0)
        builder.append("lookBehind", if (isKeyPressed(PlayerKey.LOOK_BEHIND)) 1 else 0)
        builder.append("walk", if (isKeyPressed(PlayerKey.WALK)) 1 else 0)
        builder.append("analogUp", if (isKeyPressed(PlayerKey.ANALOG_UP)) 1 else 0)
        builder.append("analogDown", if (isKeyPressed(PlayerKey.ANALOG_DOWN)) 1 else 0)
        builder.append("analogLeft", if (isKeyPressed(PlayerKey.ANALOG_LEFT)) 1 else 0)
        builder.append("analogRight", if (isKeyPressed(PlayerKey.ANALOG_RIGHT)) 1 else 0)
        return builder.build()
    }
}
