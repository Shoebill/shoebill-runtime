package net.gtaun.shoebill;

import net.gtaun.shoebill.event.dialog.DialogCloseEvent;
import net.gtaun.shoebill.event.dialog.DialogCloseEvent.DialogCloseType;
import net.gtaun.shoebill.event.dialog.DialogResponseEvent;
import net.gtaun.shoebill.event.dialog.DialogShowEvent;
import net.gtaun.shoebill.object.DialogId;
import net.gtaun.shoebill.object.DialogId.OnCloseHandler;
import net.gtaun.shoebill.object.DialogId.OnResponseHandler;
import net.gtaun.shoebill.object.DialogId.OnShowHandler;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.impl.DialogIdImpl;
import net.gtaun.util.event.EventManager;

public class DialogEventUtils {
    private DialogEventUtils() {

    }

    public static int dispatchResponseEvent(EventManager eventManager, DialogId dialog, Player player, int response, int listitem, String inputtext) {
        DialogResponseEvent event = new DialogResponseEvent(dialog, player, response, listitem, inputtext);
        if (dialog instanceof DialogIdImpl) {
            DialogIdImpl impl = (DialogIdImpl) dialog;
            OnResponseHandler onResponseHandler = impl.getOnResponseHandler();
            if (onResponseHandler != null && onResponseHandler.onResponse(dialog, player, response, listitem, inputtext))
                event.setProcessed();
        }
        eventManager.dispatchEvent(event, dialog, player);
        return event.getResponse();
    }

    public static void dispatchShowEvent(EventManager eventManager, DialogId dialog, Player player) {
        if (dialog instanceof DialogIdImpl) {
            DialogIdImpl impl = (DialogIdImpl) dialog;
            OnShowHandler onShowHandler = impl.getOnShowHandler();
            if (onShowHandler != null) onShowHandler.onShow(dialog, player);
        }

        DialogShowEvent dialogCloseEvent = new DialogShowEvent(dialog, player);
        eventManager.dispatchEvent(dialogCloseEvent, dialog, player);
    }

    public static void dispatchCloseEvent(EventManager eventManager, DialogId dialog, Player player, DialogCloseType type) {
        if (dialog instanceof DialogIdImpl) {
            DialogIdImpl impl = (DialogIdImpl) dialog;
            OnCloseHandler onCloseHandler = impl.getOnCloseHandler();
            if (onCloseHandler != null) onCloseHandler.onClose(dialog, player, type);
        }

        DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(dialog, player, type);
        eventManager.dispatchEvent(dialogCloseEvent, dialog, player);
    }
}
