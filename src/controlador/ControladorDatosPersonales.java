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

/* LOS USUARIOS PRINCIPALES NO SE PUEDEN BORRAR NI EDITAR;
 * REVISAR servicio.UsuarioServicio.java 	
 */

import modelo.Usuario;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import modelo.VentanaMensaje;
import modelo.VentanaMensaje.ClickEvent;
import org.zkoss.zul.Textbox;

public class ControladorDatosPersonales extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	@Wire private Textbox password;
	@Wire private Textbox rePassword;
	@Wire private Label lblRePassword;
	@Wire private Label lblClave;
	@Wire private Label nick;
	@Wire private Label lblPassword; 
	@Wire private Button btnEditar;
	@Wire private Button btnGuardar;
	@Wire private Button btnCancelar;
	@Wire private Button btnEliminar;

	private ControladorDatosVistas datos = new ControladorDatosVistas();
	private Usuario usuario = datos.getUsuario();

	 @Override
	 public void doAfterCompose(Component comp) throws Exception {
		 super.doAfterCompose(comp);
		 nick.setValue(usuario.getNick());
		 lblRePassword.setValue("Confirmar Clave");
		 lblClave.setValue("Clave");
		 
		 password.setVisible(false);
		 rePassword.setVisible(false);
		 lblRePassword.setVisible(false);
		 
		 setBoton(btnEditar, "Editar", "btn btn-primary", true);
		 setBoton(btnGuardar, "Guardar", "btn btn-success", false);
		 setBoton(btnCancelar, "Cancelar", "btn btn-danger", false);
		 setBoton(btnEliminar, "Eliminar Cuenta", "btn btn-danger", true);
	 }
	 
	 private void setBoton(Button btn, String nombre, String estilo, boolean visible) {
		 btn.setLabel(nombre);
		 btn.setZclass("btnDato " + estilo);
		 btn.setVisible(visible);
	 }
	 
	@Listen("onClick = #btnEliminar")		// BOTON ELIMINAR USUARIO
	public void eliminarUsuario(){	
		EventListener<ClickEvent> clickListener = new EventListener<VentanaMensaje.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {
				if(VentanaMensaje.Button.YES.equals(event.getButton())) {
	            	eliminarCuenta();
				}
			}
		};
		VentanaMensaje.show("¿Eliminar La Cuenta?", "Confirmacion", new VentanaMensaje.Button[]{
				VentanaMensaje.Button.YES, VentanaMensaje.Button.NO },VentanaMensaje.QUESTION,clickListener);        
	}
	
	public void eliminarCuenta() {		// AGARRA AL USUARIO DE LA SESION Y LO ELIMINA 
	    datos.eliminarUsuario(usuario);
	}
	
	@Listen("onClick = #btnEditar; onClick = #btnCancelar")	// BOTON EDITAR - CANCELAR
	public void editarUsuario(Event e) {
		if (e.getTarget() instanceof Button) {
			Button btn = (Button) e.getTarget();
			boolean valor = (btn.getId().equals("btnEditar")) ? true : false;
			
			validarBotones(valor);
		}
	}

	private void validarBotones(boolean valor) {	// btnEditar	btnCancelar
		password.setVisible(valor);					// 	TRUE  	 - 	   FALSE
		rePassword.setVisible(valor);				// 	TRUE  	 - 	   FALSE
		lblRePassword.setVisible(valor);			// 	TRUE  	 - 	   FALSE
		btnGuardar.setVisible(valor);				// 	TRUE  	 - 	   FALSE
		btnCancelar.setVisible(valor);				// 	TRUE  	 - 	   FALSE
		btnEditar.setVisible(!valor);				// 	TRUE  	 - 	   FALSE
		lblPassword.setVisible(!valor);				// 	FALSE 	 - 	   TRUE
		btnEliminar.setVisible(!valor);
		
		String clave = (valor == true) ? "Nueva Clave" : "Clave";
		lblClave.setValue(clave);
	}
	
	@Listen("onClick = #btnGuardar; onOK = #rePassword")	// BOTON ACTUALIZAR CLAVE
	public void guardarNuevaClave() {
		String claveViaje = usuario.getClave();
		String nuevaClave = password.getText();
		String reClave = rePassword.getText();
		
		if ((claveViaje.equals(nuevaClave)) 							// No sea la clave anterior
				&& (claveViaje.equals(reClave))) {
			
			datos.mensaje("Ingrese una nueva clave", 2, password);
		} else if ((nuevaClave.equals("")) || (reClave.equals(""))) {	// Txtbox NO vacios
			
			if (nuevaClave.equals("")) {
				datos.campoRequerido(password);
			} else {
				datos.campoRequerido(rePassword);
			}
		} else if (!nuevaClave.equals(reClave)) {						// Coincidencia claves
					
			datos.mensaje("Las claves no coinciden!", 2, rePassword);
		} else {
			
			usuario.setClave(nuevaClave);
			datos.modificarUsuario(usuario);
			datos.mensaje("La Clave ha cambiado satisfactoriamente", 1, null);
			validarBotones(false);
		}
	}

	@Listen("onOK = #password")
	public void focusTxtPassword() {
		rePassword.focus();
	}
	 
}
