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

import net.gtaun.shoebill.constant.DialogStyle
import net.gtaun.shoebill.entities.DialogId
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.event.dialog.DialogCloseEvent
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.shoebill.event.dialog.DialogShowEvent
import net.gtaun.util.event.EventHandler
import net.gtaun.util.event.EventManager

/**
 * @author MK124
 * @author Marvin Haschker
 */
class DialogIdImpl(private val rootEventManager: EventManager, id: Int,
                   val onResponseHandler: EventHandler<DialogResponseEvent>?,
                   val onShowHandler: EventHandler<DialogShowEvent>?,
                   val onCloseHandler: EventHandler<DialogCloseEvent>?) : DialogId() {

    override var id: Int = id
        private set

    override fun destroy() {
        if (id == INVALID_ID) return

        val destroyEvent = DestroyEvent(this)
        rootEventManager.dispatchEvent(destroyEvent, this)
        super.destroy()

        id = INVALID_ID
    }

    override val isDestroyed: Boolean
        get() = id == INVALID_ID

    override fun show(player: Player, style: DialogStyle, caption: String, text: String, button1: String, button2: String) {
        player.showDialog(this, style, caption, text, button1, button2)
    }

    override fun cancel(player: Player) {
        if (player.dialog !== this) return
        player.cancelDialog()
    }

    companion object {
        @JvmField
        internal val INVALID_ID = -1
    }
}