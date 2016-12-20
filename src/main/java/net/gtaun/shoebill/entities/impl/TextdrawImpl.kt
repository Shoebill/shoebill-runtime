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
import net.gtaun.shoebill.constant.TextDrawAlign
import net.gtaun.shoebill.constant.TextDrawFont
import net.gtaun.shoebill.data.Color
import net.gtaun.shoebill.data.ModelRotation
import net.gtaun.shoebill.data.Vector2D
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.Textdraw
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
class TextdrawImpl @Throws(CreationFailedException::class)
constructor(private val rootEventManager: EventManager, store: SampObjectStoreImpl, x: Float,
                          y: Float, text: String) : Textdraw() {

    override var alignment: TextDrawAlign = TextDrawAlign.LEFT
        get() = field
        set(value) {
            SampNativeFunction.textDrawAlignment(id, value.value)
            field = value
        }

    override var backgroundColor: Color = Color.BLACK
        get() = field
        set(value) {
            SampNativeFunction.textDrawBackgroundColor(id, value.value)
            field = value
        }

    override var boxColor: Color = Color.BLACK
        get() = field
        set(value) {
            SampNativeFunction.textDrawBoxColor(id, value.value)
            field = value
        }

    override var color: Color = Color.BLACK
        get() = field
        set(value) {
            SampNativeFunction.textDrawColor(id, value.value)
            field = value
        }

    override var font: TextDrawFont = TextDrawFont.FONT2
        get() = field
        set(value) {
            SampNativeFunction.textDrawFont(id, value.value)
            field = value
        }

    override var isProportional: Boolean = false
        get() = field
        set(value) {
            SampNativeFunction.textDrawSetProportional(id, if (value) 1 else 0)
            field = value
        }

    override var isSelectable: Boolean = false
        get() = field
        set(value) {
            SampNativeFunction.textDrawSetSelectable(id, if (value) 1 else 0)
            field = value
        }

    override var isUseBox: Boolean = false
        get() = field
        set(value) {
            SampNativeFunction.textDrawUseBox(id, value)
            field = value
        }

    override var letterSize: Vector2D = Vector2D(1f, 1f)
        get() = field
        set(value) {
            SampNativeFunction.textDrawLetterSize(id, value.x, value.y)
            field = value
        }

    override var outlineSize: Int = 1
        get() = field
        set(value) {
            SampNativeFunction.textDrawSetOutline(id, value)
            field = value
        }

    override var previewModel: Int = 0
        get() = field
        set(value) {
            SampNativeFunction.textDrawSetPreviewModel(id, value)
            field = value
        }

    override var previewModelRotation: ModelRotation = ModelRotation(0f, 0f, 0f)
        get() = field
        set(value) {
            SampNativeFunction.textDrawSetPreviewRot(id, value.x, value.y, value.z, value.zoom)
            field = value
        }

    override var shadowSize: Int = 0
        get() = field
        set(value) {
            SampNativeFunction.textDrawSetShadow(id, value)
            field = value
        }

    override var textSize: Vector2D = Vector2D(1f, 1f)
        get() = field
        set(value) {
            SampNativeFunction.textDrawTextSize(id, value.x, value.y)
            field = value
        }

    override var text: String = text
        set(text) {
            var newText = text
            if(StringUtils.isEmpty(text)) newText = " "
            SampNativeFunction.textDrawSetString(id, newText)
            field = newText
        }

    override var id = SampNativeFunction.textDrawCreate(x, y, this.text)
        private set

    override val position: Vector2D = Vector2D(x, y)
        get() = field.clone()

    private val shownForPlayer = BooleanArray(SampObjectStoreImpl.MAX_PLAYERS)

    init {
        if (id == Textdraw.INVALID_ID) throw CreationFailedException()
        store.setTextdraw(id, this)
    }

    override fun destroy() {
        if (isDestroyed) return

        SampNativeFunction.textDrawDestroy(id)

        val destroyEvent = DestroyEvent(this)
        rootEventManager.dispatchEvent(destroyEvent, this)
        id = Textdraw.INVALID_ID
    }

    override val isDestroyed: Boolean
        get() = id == Textdraw.INVALID_ID

    override fun setPreviewVehicleColor(color1: Int, color2: Int) =
            SampNativeFunction.textDrawSetPreviewVehCol(id, color1, color2)

    override fun show(player: Player) {
        if (!player.isOnline) return

        SampNativeFunction.textDrawShowForPlayer(player.id, id)
        shownForPlayer[player.id] = true
    }

    override fun hide(player: Player) {
        if (!player.isOnline) return

        SampNativeFunction.textDrawHideForPlayer(player.id, id)
        shownForPlayer[player.id] = false
    }

    override fun showForAll() {
        SampNativeFunction.textDrawShowForAll(id)
        for (i in shownForPlayer.indices)
            shownForPlayer[i] = true
    }

    override fun hideForAll() {
        SampNativeFunction.textDrawHideForAll(id)
        for (i in shownForPlayer.indices)
            shownForPlayer[i] = false
    }

    override fun isShownForPlayer(player: Player): Boolean {
        return !isDestroyed && shownForPlayer[player.id]
    }
}
