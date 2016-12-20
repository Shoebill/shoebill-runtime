package net.gtaun.shoebill

import net.gtaun.shoebill.entities.DialogId
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.impl.DialogIdImpl
import net.gtaun.shoebill.event.dialog.DialogCloseEvent
import net.gtaun.shoebill.event.dialog.DialogCloseEvent.DialogCloseType
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.shoebill.event.dialog.DialogShowEvent
import net.gtaun.util.event.EventManager

object DialogEventUtils {

    @JvmStatic
    internal fun dispatchResponseEvent(eventManager: EventManager, dialog: DialogId, player: Player, response: Int,
                                       listitem: Int, inputtext: String): Int {
        try {
            val event = DialogResponseEvent(dialog, player, response, listitem, inputtext)
            if (dialog is DialogIdImpl) {
                val onResponseHandler = dialog.onResponseHandler
                onResponseHandler?.handleEvent(event)
            }
            eventManager.dispatchEvent(event, dialog, player)
            return event.response
        } catch (ex: Throwable) {
            ex.printStackTrace()
            return 0
        }

    }

    @JvmStatic
    fun dispatchShowEvent(eventManager: EventManager, dialog: DialogId, player: Player) {
        try {
            val dialogCloseEvent = DialogShowEvent(dialog, player)
            if (dialog is DialogIdImpl) {
                val onShowHandler = dialog.onShowHandler
                onShowHandler?.handleEvent(dialogCloseEvent)
            }

            eventManager.dispatchEvent(dialogCloseEvent, dialog, player)
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }

    }

    @JvmStatic
    fun dispatchCloseEvent(eventManager: EventManager, dialog: DialogId, player: Player, type: DialogCloseType) {
        try {
            val dialogCloseEvent = DialogCloseEvent(dialog, player, type)
            if (dialog is DialogIdImpl) {
                val onCloseHandler = dialog.onCloseHandler
                onCloseHandler?.handleEvent(dialogCloseEvent)
            }

            eventManager.dispatchEvent(dialogCloseEvent, dialog, player)
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }

    }
}
