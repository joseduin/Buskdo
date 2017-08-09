package modelo;

public class Convertidor {
	
	private String pais;
	private String valor;
	private String moneda;
	
	public Convertidor (String pais, String valor, String moneda) {
		this.pais = pais;
		this.valor = valor;
		this.moneda = moneda;
	}
	public Convertidor (){
		
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

}
