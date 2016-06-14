/**
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
import net.gtaun.shoebill.constant.WeaponSkill
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.PlayerWeaponSkill
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class PlayerWeaponSkillImpl internal constructor(override val player: Player) : PlayerWeaponSkill {

    private val skills = IntArray(WeaponSkill.values().size)

    init {
        (0..WeaponSkill.values().size - 1).forEachIndexed { index, i -> skills[index] = i }
    }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("player", player)
            .append("skills", skills)
            .toString()

    override fun setLevel(type: WeaponSkill, level: Int) {
        var newLevel = level
        if (!player.isOnline) return

        if (level > 999)
            newLevel = 999
        else if (level < 0) newLevel = 0

        SampNativeFunction.setPlayerSkillLevel(player.id, type.value, newLevel)
        skills[type.value] = level
    }

    override fun getLevel(type: WeaponSkill): Int {
        if (!player.isOnline) return 0

        return skills[type.value]
    }
}
