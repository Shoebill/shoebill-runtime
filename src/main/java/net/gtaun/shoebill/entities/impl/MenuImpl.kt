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
import net.gtaun.shoebill.data.Vector2D
import net.gtaun.shoebill.entities.Menu
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author MK124
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class MenuImpl @Throws(CreationFailedException::class)
constructor(private val rootEventManager: EventManager, store: SampObjectStoreImpl, title: String,
                          override val columns: Int, x: Float, y: Float, override val column1Width: Float,
                          override val column2Width: Float) : Menu() {

    override val position: Vector2D = Vector2D(x, y)
        get() = field.clone()

    override var id = SampNativeFunction.createMenu(title, columns, position.x, position.y, column1Width, column1Width)
        private set

    override var title: String = title
        private set(value) {
            if(StringUtils.isEmpty(value)) field = " "
            else field = value
        }

    override val columnHeader: Array<String> = arrayOf("", "")

    init {
        if (id == Menu.INVALID_ID) throw CreationFailedException()
        store.setMenu(id, this)
    }

    override fun destroy() {
        if (isDestroyed) return

        SampNativeFunction.destroyMenu(id)

        val destroyEvent = DestroyEvent(this)
        rootEventManager.dispatchEvent(destroyEvent, this)

        id = Menu.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() = id == Menu.INVALID_ID

    override fun addItem(column: Int, text: String) = SampNativeFunction.addMenuItem(id, column, text)

    override fun setColumnHeader(column: Int, text: String) {
        SampNativeFunction.setMenuColumnHeader(id, column, text)
        columnHeader[column] = text
    }

    override fun disable() = SampNativeFunction.disableMenu(id)

    override fun disableRow(row: Int) = SampNativeFunction.disableMenuRow(id, row)

    override fun show(player: Player) {
        if (!player.isOnline) return

        SampNativeFunction.showMenuForPlayer(id, player.id)
    }

    override fun hide(player: Player) {
        if (!player.isOnline) return

        SampNativeFunction.hideMenuForPlayer(id, player.id)
    }
}
