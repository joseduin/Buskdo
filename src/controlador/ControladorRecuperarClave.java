package controlador;

import modelo.Usuario;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

public class ControladorRecuperarClave extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 1L;
	@Wire private Textbox nick;
	@Wire private Textbox txtClave;
	@Wire private Textbox txtReClave;
	
	ControladorDatosVistas datos = new ControladorDatosVistas();

	
/******************************************************************************
 * @Vista olvidoClave.zul 
 *
 */
	@Listen("onClick = #recuperar; onOK = #nick")
	public void recuperar() {
		if (nick.getValue().trim().equals("")) {
			datos.campoRequerido(nick);
		} else {
			Usuario usu = datos.buscarUsuario(nick.getValue(), "");
			
			if(usu.getNick() == null) {
				datos.mensaje("Usuario Inexistenten!", 2, nick);
			} else if (usu.getEstatus().trim().equals("PENDIENTE")) {
				datos.alerta("Su pediticion se esta resolviendo.. Por favor espere!", "Recuperar Clave");
			} else if (usu.getEstatus().trim().equals("BLOQUEADO")) {
				datos.alerta("Usuario Bloqueado!", "Usuario Bloqueado");
			} else if (usu.getEstatus().trim().equals("RESETEADO")) {
				datos.alerta("Su solicitud fue APROBADA \n\n" +
						"Por favor logeese con cualquier clave!", "Usuario Bloqueado");
			} else {
				usu.setEstatus("PENDIENTE");
				datos.modificarUsuario(usu);
				datos.alerta("Su peticion sera procesada por unos de nuestros administradores. \n" +
						"Su CLAVE sera cambiada por su NICK-NAME \n\n" +
						"Por favor espere...", "Recuperar Clave");
			}
		}
	}
	
	@Listen("onClick = #atras")
	public void atras() {
		datos.redireccion("/");
	}
	
/******************************************************************************
 * @Vista recuperarClave.zul 
 *
 */
	@Listen("onClick = #aceptar; onOK = #txtReClave")
	public void cambioClave() {
		String clave = txtClave.getValue();
		String reClave = txtReClave.getValue();
		Usuario usu = datos.getUsuario();
		
		if (clave.equals("")) {
			datos.campoRequerido(txtClave);
		} else if (reClave.equals("")) {
			datos.campoRequerido(txtReClave);	
		} if (!clave.equals(reClave)) {
			datos.mensaje("La clave no coincide", 2, txtReClave);
		} else {
			usu.setClave(clave);
			usu.setEstatus("ACTIVO");
			datos.modificarUsuario(usu);
			datos.sesion(usu);
		}
	}
	
	@Listen("onOK = #txtClave")
	public void focusTxtReClave() {
		txtReClave.setFocus(true);
	}
	
}
