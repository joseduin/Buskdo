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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import modelo.VentanaMensaje;
import modelo.VentanaMensaje.ClickEvent;

public class ControladorCabecera extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	@Wire private Label carpetaActualC;
	
	private ControladorDatosVistas datos = new ControladorDatosVistas();
	
	@Listen("onClick = #imglogo")
	public void inicio() {
		String pagina = carpetaActualC.getValue();
		if (pagina.equals("/index.zul")
				|| pagina.equals("/administrador/administracion.zul")
				|| pagina.equals("/clave/olvidoClave.zul")
				|| pagina.equals("/clave/recuperarClave.zul")) {
			return;
		} else {
			datos.redireccion("menu.zul");
		}		
	}

	@Listen("onClick = #btnSalir")
	public void cerrarSesion(){	
		EventListener<ClickEvent> clickListener = new EventListener<VentanaMensaje.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {
				if(VentanaMensaje.Button.YES.equals(event.getButton())) {
					datos.logout();
				}
			}
		};
		VentanaMensaje.show("¿Cerrar Session?", "Confirmacion", new VentanaMensaje.Button[]{
				VentanaMensaje.Button.YES, VentanaMensaje.Button.NO },VentanaMensaje.QUESTION,clickListener);        
	} 
	
}