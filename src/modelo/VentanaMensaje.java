package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.mesg.Messages;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.mesg.MZul;

import servicio.VentanaMensajeDlg;
 
public class VentanaMensaje {
     private static final Logger log = LoggerFactory.getLogger(VentanaMensaje.class);
     private static String _templ = "/ventanaEmergente/Mensaje.zul";
 
     public static final String QUESTION = "z-messagebox-icon z-messagebox-question";
     public static final String EXCLAMATION  = "z-messagebox-icon z-messagebox-exclamation";
     public static final String INFORMATION = "z-messagebox-icon z-messagebox-information";
     public static final String ERROR = "z-messagebox-icon z-messagebox-error";      
     public static final String NONE = null;
 
     public static final int OK = 0x0001;
     public static final int CANCEL = 0x0002;
     public static final int YES = 0x0010;
     public static final int NO = 0x0020;
     public static final int ABORT = 0x0100;
     public static final int RETRY = 0x0200;
     public static final int IGNORE = 0x0400;
 
     public static final String ON_YES = "onYes";
     public static final String ON_NO = "onNo";
     public static final String ON_RETRY = "onRetry";
     public static final String ON_ABORT = "onAbort";
     public static final String ON_IGNORE = "onIgnore";
     public static final String ON_OK = Events.ON_OK;
     public static final String ON_CANCEL = Events.ON_CANCEL;
 
     public static Button show(String message, String title,
     Button[] buttons, String[] btnLabels, String icon,
     Button focus, EventListener<ClickEvent> listener, Map<String, String> params) {
         final Map<String, Object> arg = new HashMap<String, Object>();
         arg.put("message", message);
         arg.put("icon", icon);         
 		 arg.put("title", title != null ? title :
 			 Executions.getCurrent().getDesktop().getWebApp().getAppName());
 
         if (buttons == null)
             buttons = DEFAULT_BUTTONS;
 
         int btnmask = 0;
         for (int j = 0; j < buttons.length; ++j) {
             if (buttons[j] == null)
                 throw new IllegalArgumentException("The "+j+"-th button is null");
 
             //Backward compatible to ZK 5: put buttons and id to arg
             btnmask += buttons[j].id;
             arg.put(buttons[j].stringId, buttons[j].id);
         }
         arg.put("buttons", btnmask);
 
         if (params != null)
             arg.putAll(params);
 
         final VentanaMensajeDlg dlg = (VentanaMensajeDlg)
             Executions.createComponents(_templ, null, arg);
         dlg.setEventListener(listener);
         dlg.setButtons(buttons, btnLabels);
         if (focus != null) dlg.setFocus(focus);
 
         if (dlg.getDesktop().getWebApp().getConfiguration().isEventThreadEnabled()) {
             try {
                 dlg.doModal();
             } catch (Throwable ex) {
                 try {
                     dlg.detach();
                 } catch (Throwable ex2) {
                     log.warn("Failed to detach when recovering from an error", ex2);
                 }
                 throw UiException.Aide.wrap(ex);
             }
             return dlg.getResult();
         } else {
             dlg.doHighlighted();
             return Button.OK;
         }
     }
     public static Button show(String message, String title,
     Button[] buttons, String[] btnLabels, String icon,
     Button focus, EventListener<ClickEvent> listener) {
         return show(message, title, buttons, btnLabels, icon, focus, listener, null);
     }
     private static final VentanaMensaje.Button[] DEFAULT_BUTTONS
         = new VentanaMensaje.Button[] {VentanaMensaje.Button.OK};
 
     public static Button show(String message, String title,
     Button[] buttons, String icon,
     Button focus, EventListener<ClickEvent> listener) {
         return show(message, title, buttons, null, icon, focus, listener, null);
     }
     public static Button show(String message, String title,
     Button[] buttons, String icon,
     EventListener<ClickEvent> listener) {
         return show(message, title, buttons, null, icon, null, listener, null);
     }
     public static Button show(String message,
     Button[] buttons, EventListener<ClickEvent> listener) {
         return show(message, null, buttons, null, INFORMATION, null, listener, null);
     }
 
     public static 
     int show(String message, String title, int buttons, String icon) {
         return show(message, title, buttons, icon, 0, null);
     }
     public static int show(String message, String title, int buttons, String icon,
     EventListener<Event> listener) {
         return show(message, title, buttons, icon, 0, listener);
     }
     public static int show(String message, String title, int buttons, String icon, int focus) {
         return show(message, title, buttons, icon, focus, null);
     }
     public static int show(String message, String title, int buttons, String icon,
     int focus, EventListener<Event> listener) {
         Button res = show(message, title, toButtonTypes(buttons), null, icon,
                 focus != 0 ? toButtonType(focus): null, toButtonListener(listener), null);
         return res != null ? res.id : 0; // B60-ZK-946: NPE
     }
     private static Button toButtonType(int btn) {
         switch (btn) {
         case CANCEL: return Button.CANCEL;
         case YES: return Button.YES;
         case NO: return Button.NO;
         case ABORT: return Button.ABORT;
         case RETRY: return Button.RETRY;
         case IGNORE: return Button.IGNORE;
         default: return Button.OK;
         }
     }
     private static Button[] toButtonTypes(int buttons) {
         final List<Button> btntypes = new ArrayList<Button>();
         if ((buttons & OK) != 0)
             btntypes.add(toButtonType(OK));
         if ((buttons & CANCEL) != 0)
             btntypes.add(toButtonType(CANCEL));
         if ((buttons & YES) != 0)
             btntypes.add(toButtonType(YES));
         if ((buttons & NO) != 0)
             btntypes.add(toButtonType(NO));
         if ((buttons & RETRY) != 0)
             btntypes.add(toButtonType(RETRY));
         if ((buttons & ABORT) != 0)
             btntypes.add(toButtonType(ABORT));
         if ((buttons & IGNORE) != 0)
             btntypes.add(toButtonType(IGNORE));
         return btntypes.toArray(new Button[btntypes.size()]);
     }
     private static
     EventListener<ClickEvent> toButtonListener(EventListener<Event> listener) {
         return listener != null ? new ButtonListener(listener): null;
     }
 
     public static int show(String message) {
         return show(message, null, OK, INFORMATION, 0, null);
     }
     public static int show(int messageCode, Object[] args, int titleCode, int buttons,
     String icon) {
         return show(messageCode, args, titleCode, buttons, icon, 0, null);
     }
     public static int show(int messageCode, Object[] args, int titleCode, int buttons,
     String icon, int focus) {
         return show(messageCode, args, titleCode, buttons, icon, focus, null);
     }
     public static int show(int messageCode, Object[] args, int titleCode, int buttons,
     String icon, int focus, EventListener<Event> listener) {
         return show(Messages.get(messageCode, args),
             titleCode > 0 ? Messages.get(titleCode): null, buttons,
             icon, focus, listener);
     }
     public static int show(int messageCode, Object arg, int titleCode, int buttons, String icon) {
         return show(messageCode, arg, titleCode, buttons, icon, 0, null);
     }
     public static int show(int messageCode, Object arg, int titleCode, int buttons,
     String icon, int focus) {
         return show(messageCode, arg, titleCode, buttons, icon, focus, null);
     }
     public static int show(int messageCode, Object arg, int titleCode, int buttons,
     String icon, int focus, EventListener<Event> listener) {
         return show(Messages.get(messageCode, arg),
             titleCode > 0 ? Messages.get(titleCode): null, buttons,
             icon, focus, listener);
     }
     public static int show(int messageCode, int titleCode, int buttons, String icon) {
         return show(messageCode, titleCode, buttons, icon, 0);
     }
     public static int show(int messageCode, int titleCode, int buttons, String icon,
     int focus) {
         return show(messageCode, titleCode, buttons, icon, focus, null);
     }
     public static int show(int messageCode, int titleCode, int buttons, String icon,
     int focus, EventListener<Event> listener) {
         return show(Messages.get(messageCode),
             titleCode > 0 ? Messages.get(titleCode): null, buttons,
             icon, focus, listener);
     }
 
     public static void setTemplate(String uri) {
         if (uri == null || uri.length() == 0)
             throw new IllegalArgumentException("empty");
         _templ = uri;
     }
     public static String getTemplate() {
         return _templ;
     }
 
     public static enum Button {
         OK (VentanaMensaje.OK, ON_OK, MZul.OK, "OK"),
         CANCEL (VentanaMensaje.CANCEL, ON_CANCEL, MZul.CANCEL, "CANCEL"),
         YES (VentanaMensaje.YES, ON_YES, MZul.YES, "YES"),
         NO (VentanaMensaje.NO, ON_NO, MZul.NO, "NO"),
         ABORT (VentanaMensaje.ABORT, ON_ABORT, MZul.ABORT, "ABORT"),
         RETRY (VentanaMensaje.RETRY, ON_RETRY, MZul.RETRY, "RETRY"),
         IGNORE (VentanaMensaje.IGNORE, ON_IGNORE, MZul.IGNORE, "IGNORE");
 
         public final int id;
         public final String event;
         public final int label;
         private final String stringId;
 
         private Button(int id, String event, int label, String stringId) {
             this.id = id;
             this.event = event;
             this.label = label;
             this.stringId = stringId;
         }
     }
     public static class ClickEvent extends Event {

		private static final long serialVersionUID = 1L;
		public ClickEvent(String name, Component target, Button button) {
             super(name, target, button);
         }
         public Button getButton() {
             return (Button)getData();
         }
     }
 
     private static class ButtonListener implements SerializableEventListener<ClickEvent> {
 
		private static final long serialVersionUID = 1L;
		private final EventListener<Event> _listener;
         private ButtonListener(EventListener<Event> listener) {
             _listener = listener;
         }
         public void onEvent(ClickEvent event) throws Exception {
             final Button btn = event.getButton();
             _listener.onEvent(
                 new Event(event.getName(), event.getTarget(),
                     btn != null ? btn.id: -1));
         }
         public String toString() {
             return _listener.toString();
         }
     }
}