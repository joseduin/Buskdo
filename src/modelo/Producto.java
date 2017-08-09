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

public class Producto {
	private String nombre;
	private String imagen;
	private String ubicacion;
	private String condicion;
	private double precio;
	private String moneda;
	private String id_moneda;
	private int vendido;
	private int disponible;

	public  Producto(String nombre, String imagen, String ubicacion, String condicion, 
			double precio, String moneda, String id_moneda, int vendido, int disponible) {
		this.nombre = nombre;
		this.imagen = imagen;
		this.ubicacion = ubicacion;
		this.condicion = condicion;
		this.precio = precio;
		this.moneda = moneda;
		this.id_moneda = id_moneda;
		this.vendido = vendido;
		this.disponible = disponible;
	}
	
	public Producto(){}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getVendido() {
		return vendido;
	}

	public void setVendido(int vendido) {
		this.vendido = vendido;
	}

	public int getDisponible() {
		return disponible;
	}

	public void setDisponible(int disponible) {
		this.disponible = disponible;
	}

	public String getId_moneda() {
		return id_moneda;
	}

	public void setId_moneda(String id_moneda) {
		this.id_moneda = id_moneda;
	}
	
}