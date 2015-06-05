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
import net.gtaun.util.event.EventHandler;
import net.gtaun.util.event.EventManager;

public class DialogEventUtils {
    private DialogEventUtils() {

    }

    public static int dispatchResponseEvent(EventManager eventManager, DialogId dialog, Player player, int response, int listitem, String inputtext)
    {
		try
		{
			DialogResponseEvent event = new DialogResponseEvent(dialog, player, response, listitem, inputtext);
			if (dialog instanceof DialogIdImpl) {
				DialogIdImpl impl = (DialogIdImpl) dialog;
				EventHandler<DialogResponseEvent> onResponseHandler = impl.getOnResponseHandler();
				if (onResponseHandler != null) onResponseHandler.handleEvent(event);
			}
			eventManager.dispatchEvent(event, dialog, player);
			return event.getResponse();
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
			return 0;
		}
    }

    public static void dispatchShowEvent(EventManager eventManager, DialogId dialog, Player player)
	{
		try
		{
			DialogShowEvent dialogCloseEvent = new DialogShowEvent(dialog, player);
			if (dialog instanceof DialogIdImpl) {
				DialogIdImpl impl = (DialogIdImpl) dialog;
				EventHandler<DialogShowEvent> onShowHandler = impl.getOnShowHandler();
				if (onShowHandler != null) onShowHandler.handleEvent(dialogCloseEvent);
			}

			eventManager.dispatchEvent(dialogCloseEvent, dialog, player);
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
		}
    }

    public static void dispatchCloseEvent(EventManager eventManager, DialogId dialog, Player player, DialogCloseType type)
	{
		try
		{
			DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(dialog, player, type);
			if (dialog instanceof DialogIdImpl) {
				DialogIdImpl impl = (DialogIdImpl) dialog;
				EventHandler<DialogCloseEvent> onCloseHandler = impl.getOnCloseHandler();
				if (onCloseHandler != null) onCloseHandler.handleEvent(dialogCloseEvent);
			}

			eventManager.dispatchEvent(dialogCloseEvent, dialog, player);
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
		}
    }
}
