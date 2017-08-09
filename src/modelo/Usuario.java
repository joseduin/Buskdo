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

package modelo;

public class Usuario {
	
	private String nick;
	private String clave;
	private String credenciales;
	private String estatus;

	public Usuario(String nick, String clave, String credenciales, String estatus) {
		this.nick = nick;
		this.clave = clave;
		this.credenciales = credenciales;
		this.estatus = estatus;
	}
	
	public String getCredenciales() {
		return credenciales;
	}

	public void setCredenciales(String credenciales) {
		this.credenciales = credenciales;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Usuario() {
		
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getClave() {
		return clave;
	}
	
	public void setClave(String clave) {
		this.clave = clave;
	}

}