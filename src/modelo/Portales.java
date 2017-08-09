package modelo;

public class Portales {
	
	private String cod_portal;
	private String portal;
	private String pais;
	private String dominio;
	private boolean estatus;
	
	public Portales(String cod_portal, String portal, boolean estatus, String pais, String dominio) {
		this.cod_portal = cod_portal;
		this.portal = portal;
		this.pais = pais;
		this.dominio = dominio;
		this.estatus = estatus;
	}
	public Portales() {
		
	}
	public String getCod_portal() {
		return cod_portal;
	}
	public void setCod_portal(String cod_portal) {
		this.cod_portal = cod_portal;
	}
	public String getPortal() {
		return portal;
	}
	public void setPortal(String portal) {
		this.portal = portal;
	}
	public boolean isEstatus() {
		return estatus;
	}
	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

}
