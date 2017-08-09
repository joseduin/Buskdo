package servicio;

import org.zkoss.mesg.Messages;
 
 import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.EventListener;
 
 import org.zkoss.zul.Window;
import modelo.VentanaMensaje;
import modelo.VentanaMensaje.ClickEvent;
 
 public class VentanaMensajeDlg extends Window {
	 
	 private static final long serialVersionUID = 1L;
	 private VentanaMensaje.Button[] _buttons;
     private VentanaMensaje.Button _result;
     private EventListener<ClickEvent> _listener;
 
     public void onOK() throws Exception {
         if (contains(VentanaMensaje.Button.OK)) endModal(VentanaMensaje.Button.OK);
         else if (contains(VentanaMensaje.Button.YES)) endModal(VentanaMensaje.Button.YES);
         else if (contains(VentanaMensaje.Button.RETRY)) endModal(VentanaMensaje.Button.RETRY);
     }
     public void onCancel() throws Exception {
         if (_buttons.length == 1 && _buttons[0] == VentanaMensaje.Button.OK) endModal(VentanaMensaje.Button.OK);
         else if (contains(VentanaMensaje.Button.CANCEL)) endModal(VentanaMensaje.Button.CANCEL);
         else if (contains(VentanaMensaje.Button.NO)) endModal(VentanaMensaje.Button.NO);
         else if (contains(VentanaMensaje.Button.ABORT)) endModal(VentanaMensaje.Button.ABORT);
     }
     private boolean contains(VentanaMensaje.Button button) {
         for (int j = 0; j < _buttons.length; ++j)
             if (_buttons[j] == button)
                 return true;
         return false;
     }
 
     public void setButtons(VentanaMensaje.Button[] buttons, String[] btnLabels) {
         _buttons = buttons;
 
         final Component parent = getFellowIfAny("buttons");
         if (parent != null && parent.getFirstChild() == null) {

             final String sclass = (String)parent.getAttribute("button.sclass");
             for (int j = 0; j < _buttons.length; ++j) {
                 final Button mbtn = new Button();
                 mbtn.setButton(_buttons[j],
                     btnLabels != null && j < btnLabels.length ? btnLabels[j]: null);
 
                 mbtn.setZclass(sclass);
                 mbtn.setAutodisable("self");
                 parent.appendChild(mbtn);
             }
         }
     }
 
     public void setEventListener(EventListener<ClickEvent> listener) {
         _listener = listener;
     }
     public void setFocus(VentanaMensaje.Button button) {
         if (button != null) {
             final Button btn = (Button)getFellowIfAny("btn" + button.id);
             if (btn != null)
                 btn.focus();
         }
     }
 
     public void endModal(VentanaMensaje.Button button) throws Exception {
         _result = button;
         if (_listener != null) {
             final ClickEvent evt = new ClickEvent(button.event, this, button);
             _listener.onEvent(evt);
             if (!evt.isPropagatable())
                 return; //no more processing
         }
         detach();
     }
     public VentanaMensaje.Button getResult() {
         return _result;
     }
 
     //Override//
     public void onClose() {
         if (_listener != null) {
             final ClickEvent evt = new ClickEvent(Events.ON_CLOSE, this, null);
             try {
                 _listener.onEvent(evt);
                 if (!evt.isPropagatable())
                     return; //no more processing
             } catch (Exception ex) {
                 throw UiException.Aide.wrap(ex);
             }
         }
         super.onClose();
     }
 
     public static class Button extends org.zkoss.zul.Button {

		private static final long serialVersionUID = 1L;
		private VentanaMensaje.Button _button;
 
         public void setButton(VentanaMensaje.Button button, String label) {
             _button = button;
             setLabel(label != null ? label: Messages.get(_button.label));
             setId("btn" + _button.id);
             if (label != null && label.length() > 7) //dirty trick (since there is a default width)
                 setWidth("auto");
         }
         public void setButton(VentanaMensaje.Button button) {
             setButton(button, null);
         }
         public void onClick() throws Exception {
             ((VentanaMensajeDlg)getSpaceOwner()).endModal(_button);
         }
         protected String getDefaultMold(@SuppressWarnings("rawtypes") Class klass) {
             return super.getDefaultMold(org.zkoss.zul.Button.class);
         }
 
         public void setIdentity(int button) {
             switch (button) {
             case VentanaMensaje.YES:
                 setButton(VentanaMensaje.Button.YES);
                 break;
             case VentanaMensaje.NO:
                 setButton(VentanaMensaje.Button.NO);
                 break;
             case VentanaMensaje.RETRY:
                 setButton(VentanaMensaje.Button.RETRY);
                 break;
             case VentanaMensaje.ABORT:
                 setButton(VentanaMensaje.Button.ABORT);
                 break;
             case VentanaMensaje.IGNORE:
                 setButton(VentanaMensaje.Button.IGNORE);
                 break;
             case VentanaMensaje.CANCEL:
                 setButton(VentanaMensaje.Button.CANCEL);
                 break;
             default:
                 setButton(VentanaMensaje.Button.OK);
                 break;
             }
         }
     }
 }