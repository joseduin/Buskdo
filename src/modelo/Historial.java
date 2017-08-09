package modelo;

public class Historial {
	
	private String nick_name;
	private String descripcion;

	public Historial(String nick_name, String descripcion) {
		this.nick_name = nick_name;
		this.descripcion = descripcion;
	}
	public Historial(){}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
