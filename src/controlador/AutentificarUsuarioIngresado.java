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

/*
 * Usuario Logeado no se puede devolver a la pantalla de index.zul, hasta deslogearse
 * */

import java.util.Map;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AutentificarUsuarioIngresado implements Initiator {
	
	ControladorDatosVistas datos = new ControladorDatosVistas();

	@Override
	public void doInit(Page arg0, Map<String, Object> arg1) throws Exception {	      
		if(datos.miSession.getAttribute("credenciales") != null) {	// Usuario Existe
			if(datos.miSession.getAttribute("credenciales").toString().equals("ADMIN")) {
				datos.redireccion("administrador/administracion.zul");
			} else {
				datos.redireccion("menu.zul");
			}
		}		
	}

}
