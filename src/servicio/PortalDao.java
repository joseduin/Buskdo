package servicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.Conexion;
import modelo.ClassConexionDAO;
import modelo.Portales;

public class PortalDao extends ClassConexionDAO{
	
	public PortalDao(){
		super();
	}

	public ArrayList<Portales> ConsultarPortal() {
  		ArrayList<Portales> portales = new ArrayList<Portales>();
		String tiraSQL = "SELECT * FROM portales";
		ResultSet resultSet = Conexion.consultar(tiraSQL);		
		try {
			while (resultSet.next()) {
				String cp = resultSet.getString("cod_portal");
				String p = resultSet.getString("portal");
				boolean e = resultSet.getBoolean("estatus");
				String pais = resultSet.getString("pais");
				String dom = resultSet.getString("dominio");

				Portales portal = new Portales(cp, p, e, pais, dom);
				portales.add(portal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return portales;
    } 
  	public void ModificarDolarOtras(String cod_portal, String portal, boolean estatus, String pais, String dominio) {
		 String Sql;
	    Sql = "UPDATE portales SET cod_portal='" + cod_portal + "', portal='" + portal.trim() + 
	    	  "' ,estatus = " + estatus + ", pais='" + pais.trim() + "', dominio='" + dominio.trim() +
	    		"' WHERE portal='" + portal.trim() + "';";

	    Conexion.ejecutar(Sql);
	 }
  	public void ResgistroPortal(String cod_portal, String portal, boolean estatus, String pais, String dominio) {
		 String Sql;
	    Sql = "INSERT INTO portales(cod_portal, portal, estatus, pais, dominio) " +
	    	  "VALUES ('" + cod_portal + "', '" + portal + "', " + estatus + ", '" + pais + "', '" + dominio + "');"; 

	    Conexion.ejecutar(Sql);
	 }
}
