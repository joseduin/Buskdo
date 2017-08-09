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
 * Usuario sin logearse no puede acceder a otras paginas que no sea index.zul
 * */

import java.util.Map;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AutentificarUsuario implements Initiator {

	ControladorDatosVistas datos = new ControladorDatosVistas();

	@Override
	public void doInit(Page arg0, Map<String, Object> arg1) throws Exception {		
		if(datos.miSession.getAttribute("credenciales") == null) {	// NO existe Usuario
			datos.redireccion("/");
		} else {
			String name = (String)arg1.get("name");
			if(!datos.miSession.getAttribute("estatus").toString().equals("ACTIVO")) {
				// Usuario diferente a estatus = ACTIVO
				datos.logout();
			} else if (!datos.miSession.getAttribute("credenciales").toString().equals("ADMIN") 
					&& name.equals("administrador")) {
				datos.redireccion("/");
			}
		}		
	}

}
