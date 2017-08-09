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

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;
import org.zkoss.zul.Image;

import servicio.Meli;

public class ControladorMenu extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 1L; 
	@Wire private Image oferta;
	@Wire private Label scheme;
	@Wire private Label nombrePuerto;
	@Wire private Label puerto;
	@Wire private Label nombreProyecto;
	@Wire private Label carpetaActual;
	
	private int pag = 0;
	private static final String[] OFERTAS = {"oferta0.png",
											 "oferta1.png",
											 "oferta2.png",
											 "oferta3.png",
											 "oferta4.png"};
	
	private ControladorDatosVistas datos = new ControladorDatosVistas();


/******************
 * BANER IMAGENES *
 ******************/
	@Listen("onTimer = timer")		// CONTROLA LAS IMAGENES EN EL BANNER MENU
	public void pasarOfertas() {	// SE CAMBIA CADA 4 SEG, VER menu.zul
		oferta.setSrc("/imagen/" + OFERTAS[pag]);
		pag = (pag == (OFERTAS.length - 1)) ? 0 : pag + 1;
	}
	
	public String getImagen() {
		return "/imagen/" + OFERTAS[0];
	}

/******************
 *	 NAV BAR 
 ******************/
	@Listen("onClick = navitem#gridProductos")
	public void mostrarGridEstudiantes() {
		// Hacemos la 1ra conexion con MLV para que nos regresen el token
		Meli m = new Meli("7892861532650836", "ZEy1f3HtMODEhPpOMqEfzFl5bKEpcJFQ");
		
		String redirectUrl = m.getAuthUrl(scheme.getValue()+"://"+
										  nombrePuerto.getValue()+":"+
										  puerto.getValue()+
										  nombreProyecto.getValue()+
										  "/grid_producto.zul", Meli.AuthUrls.MLV);
		
		Executions.sendRedirect(redirectUrl);

// 		Executions.sendRedirect("grid_producto.zul");
	}
	
	@Listen("onClick = navitem#acercaDe")
	public void menuAcercaDe() {
		Window window = (Window)Executions.createComponents("ventanaEmergente.zul",null,
						 argumentos("acercaDe.zul", "Integrantes", 350, "best.png"));
		window.doModal();
	}
	
	@Listen("onClick = navitem#ayudaGenerales")
	public void menuAyudaGenerales() {	
		Window window = (Window)Executions.createComponents("ventanaEmergente.zul",null,
						 argumentos("/ayudaGeneral.zul","Ayuda Generales", 550, "ayuda.png"));
		window.doModal();
	}
	
	@Listen("onClick = navitem#DatosPersonales")
	public void menuDatosPersonales() {		
		Window window = (Window)Executions.createComponents("ventanaEmergente.zul",null,
						 argumentos("datosPersonales.zul","Mi Perfil", 400, "perfil.png"));
		window.doModal();
	}
	
	@Listen("onClick = navitem#vistaAdmin")
	public void vistaAdmin() {
		datos.redireccion("administrador/administracion.zul");
	}

	private HashMap<String, String> argumentos(String zul, String titulo,int ancho, 
											   String icono) {
		HashMap<String, String> arg  = new  HashMap<String, String>();
		
		// ARGUMENTOS COMUNES
		arg.put("url", "/ventanaEmergente/" + zul);
		arg.put("titulo", titulo);
		arg.put("ancho", ancho + "px");
		arg.put("efecto", datos.getEfectoWindows());
		arg.put("icono", "/imagen/" + icono);
		return arg;
	}
	
}
