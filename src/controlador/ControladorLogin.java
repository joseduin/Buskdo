/*
 * INTEGRANTES:
 * Darling Gimenez  CI: 20.926.765
 * Jose Miguel Duin CI: 21.142.293
 * Patricia Freitez CI: 21.526.571
 *
 * Laboratorio II
 *
 * Febrero 2016
 *
 * Copyright (c)
 */

package controlador;

import modelo.Usuario;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Textbox;

public class ControladorLogin extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 1L;
	@Wire private Textbox lblLogin;
	@Wire private Textbox txtClave;
	@Wire private Textbox txtReClave;
	@Wire private Textbox txtUsuario;
	@Wire private Textbox lblPassword;
	@Wire private Div iniciarSecion;
	@Wire private Div registrarUsuario;
	@Wire private Button btnInicioRegistro;
	
	private ControladorDatosVistas datos = new ControladorDatosVistas();
	
	@Listen("onClick = #recuperarClave")
	public void olvidoClave() {
		datos.redireccion("clave/olvidoClave.zul");
	}
	
	@Listen("onClick = #btnAceptar; onOK = #lblPassword")
	public void autenticar() {
		String usuario = lblLogin.getValue();
		String clave = lblPassword.getValue();
		
		if (usuario.equals("")) {
			datos.campoRequerido(lblLogin);
		} else if (clave.equals("")) {
			datos.campoRequerido(lblPassword);
		} else {
			Usuario usu = datos.buscarUsuario(usuario, clave);	// Coincide Usuario - Clave
			Usuario usu2 = datos.buscarUsuario(usuario, "");	// Coincide Usuario
			
			if ((usu2.getNick() != null)
						&& (!usu2.getEstatus().trim().equals("ACTIVO"))) {
				if (usu2.getEstatus().trim().equals("RESETEADO")) {
					datos.sesion(usu2);
					datos.redireccion("clave/recuperarClave.zul");
				} else if(usu2.getEstatus().trim().equals("PENDIENTE")) {
					datos.alerta("Su pediticion se esta resolviendo.. Por favor espere!", "Recuperar Clave");
				} else {
					datos.mensaje("Cuenta Bloqueada!", 3, iniciarSecion);
				}
				
			} else if(usu.getNick() == null) {
				datos.mensaje("Usuario o clave incorrecta", 2, iniciarSecion);
			} else {
				datos.sesion(usu);
			}
		}
	}
	
	@Listen("onClick = #btnRegistrar; onOK = #txtReClave")
	public void registrarUsuario() {
		String nombre = txtUsuario.getValue();
		String clave = txtClave.getValue();
		String reClave = txtReClave.getValue();
			
		if (nombre.equals("")) {
			datos.campoRequerido(txtUsuario);
		} else if (clave.equals("")) {
			datos.campoRequerido(txtClave);
		} else if (reClave.equals("")) {
			datos.campoRequerido(txtReClave);	
		} else {
			Usuario usu = datos.buscarUsuario(nombre, "");

			if(usu.getNick() != null) {
				datos.mensaje("Nombre de Usuario Existente!", 2, txtUsuario);
			} else {
				if (!clave.equals(reClave)) {
					datos.mensaje("La clave no coincide", 2, txtReClave);
				} else {
					datos.ingresarUsuario(nombre, clave);
					Usuario usuNuevo = datos.buscarUsuario(nombre, clave);
					datos.sesion(usuNuevo);
				}
			}
		}
	}
	
	@Listen("onClick = #btnInicioRegistro")
	public void inicioRegistro() {			
		iniciarSecion.setVisible(registrarUsuario.isVisible());
		registrarUsuario.setVisible(!iniciarSecion.isVisible());
		btnInicioRegistro.setLabel((!registrarUsuario.isVisible()) ? "Nuevo Usuario" : "Iniciar Sesion");
	}	
	
	@Listen("onOK = #lblLogin")
	public void enforcarLblPassword() {
		lblPassword.setFocus(true);
	}
	
	@Listen("onOK = #txtUsuario")
	public void enforcarTxtPassword() {
		txtClave.setFocus(true);
	}
	
	@Listen("onOK = #txtClave")
	public void enforcarTxtRePassword() {
		txtReClave.setFocus(true);
	}
	
	@Listen("onClick = hbox[zclass=grupo_textboxt]")
	public void imagenTextbox(Event e) {
		if (e.getTarget() instanceof Hbox) {
			Hbox hbox = (Hbox) e.getTarget();
			
			Textbox textbox = (Textbox) hbox.getChildren().get(1);
			textbox.setFocus(true);
		}		
	}
	
}
