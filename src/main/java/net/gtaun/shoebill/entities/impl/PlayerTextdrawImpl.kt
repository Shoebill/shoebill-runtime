/**
 * Copyright (C) 2012-2014 MK124

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
import net.gtaun.shoebill.constant.TextDrawAlign
import net.gtaun.shoebill.constant.TextDrawFont
import net.gtaun.shoebill.data.Color
import net.gtaun.shoebill.data.ModelRotation
import net.gtaun.shoebill.data.Vector2D
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.PlayerTextdraw
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author MK124
 * @author Marvin Haschker
 */
class PlayerTextdrawImpl @Throws(CreationFailedException::class)
constructor(private val rootEventManager: EventManager, override val player: Player, x: Float, y: Float,
            text: String, store: SampObjectStoreImpl) : PlayerTextdraw() {

    override var alignment: TextDrawAlign = TextDrawAlign.LEFT
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawAlignment(player.id, id, value.value)
            field = value
        }

    override var backgroundColor: Color = Color.BLACK
        get() = field.clone()
        set(value) {
            SampNativeFunction.playerTextDrawBackgroundColor(player.id, id, value.value)
            field = value
        }

    override var boxColor: Color = Color.BLACK
        get() = field.clone()
        set(value) {
            SampNativeFunction.playerTextDrawBoxColor(player.id, id, value.value)
            field = value
        }

    override var color: Color = Color.BLACK
        get() = field.clone()
        set(value) {
            SampNativeFunction.playerTextDrawColor(player.id, id, value.value)
            field = value
        }

    override var font: TextDrawFont = TextDrawFont.FONT2
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawFont(player.id, id, value.value)
            field = value
        }

    override var isProportional: Boolean = false
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawSetProportional(player.id, id, if (value) 1 else 0)
            field = value
        }

    override var isSelectable: Boolean = false
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawSetSelectable(player.id, id, if (value) 1 else 0)
            field = value
        }

    override var isUseBox: Boolean = false
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawUseBox(player.id, id, if (value) 1 else 0)
            field = value
        }

    override var letterSize: Vector2D = Vector2D(1f, 1f)
        get() = field.clone()
        set(value) {
            SampNativeFunction.playerTextDrawLetterSize(player.id, id, value.x, value.y)
            field = value
        }

    override var outlineSize: Int = 1
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawSetOutline(player.id, id, value)
            field = value
        }

    override var previewModel: Int = 0
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawSetPreviewModel(player.id, id, value)
            field = value
        }

    override var previewModelRotation: ModelRotation = ModelRotation(0f, 0f, 0f)
        get() = field.clone()
        set(value) {
            SampNativeFunction.playerTextDrawSetPreviewRot(player.id, id, value.x, value.y, value.z, value.zoom)
            field = value
        }

    override var shadowSize: Int = 0
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawSetShadow(player.id, id, value)
            field = value
        }

    override var textSize: Vector2D = Vector2D()
        get() = field
        set(value) {
            SampNativeFunction.playerTextDrawTextSize(player.id, id, value.x, value.y)
            field = value
        }

    override var text: String = text
        set(text) {
            var newValue = text
            if (StringUtils.isEmpty(text)) newValue = " "

            SampNativeFunction.playerTextDrawSetString(player.id, id, newValue)
            field = newValue
        }

    override var id: Int = SampNativeFunction.createPlayerTextDraw(player.id, x, y, this.text)
        private set

    override val position: Vector2D = Vector2D(x, y)
        get() = field.clone()

    override var isShown = false
        private set

    init {
        if (id == PlayerTextdraw.INVALID_ID) throw CreationFailedException()
        store.setPlayerTextdraw(player, id, this)
    }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("player", player)
            .append("id", id).toString()

    override fun destroy() {
        if (isDestroyed) return
        if (player.isOnline)
            SampNativeFunction.playerTextDrawDestroy(player.id, id)

        val destroyEvent = DestroyEvent(this)
        rootEventManager.dispatchEvent(destroyEvent, this)
        id = PlayerTextdraw.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() {
            if (!player.isOnline && id != PlayerTextdraw.INVALID_ID) destroy()
            return id == PlayerTextdraw.INVALID_ID
        }

    override fun setPreviewVehicleColor(color1: Int, color2: Int) {
        if (isDestroyed) return

        SampNativeFunction.playerTextDrawSetPreviewVehCol(player.id, id, color1, color2)
    }

    override fun show() {
        if (isDestroyed) return
        SampNativeFunction.playerTextDrawShow(player.id, id)
        isShown = true
    }

    override fun hide() {
        if (isDestroyed) return
        SampNativeFunction.playerTextDrawHide(player.id, id)
        isShown = false
    }
}
