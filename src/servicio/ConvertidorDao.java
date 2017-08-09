package servicio;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.ClassConexionDAO;
import modelo.Convertidor;
import bean.Conexion;

public class ConvertidorDao extends ClassConexionDAO{
	
	public ConvertidorDao(){
		super();
	}
//crea un arreglo para guardas todos los valores obtenidos en la consulta 
  	public ArrayList<Convertidor> ConsultarConvertidor(boolean full) {
  		ArrayList<Convertidor> convertidores = new ArrayList<Convertidor>();
//hace una consulta de todos los valores que existen en la tabla
  		String tiraSQL;
  		if (full == true) {
  			tiraSQL = "SELECT * FROM convertidor";
  		} else {
  			tiraSQL = "SELECT cod_pais, moneda, valor_dolar " +
  					  "FROM convertidor " +
  					  "WHERE cod_pais NOT IN ('VEF')";
  		}
		ResultSet resultSet = Conexion.consultar(tiraSQL);		
//agarra un posible error y lo muestra en pantalla
		try {
			while (resultSet.next()) {
				String cp = resultSet.getString("cod_pais");
				String m = resultSet.getString("valor_dolar");
				String v = resultSet.getString("moneda");
//asigna los valores que se obtuvieron de la consulta al arreglo segun el orden de las columnas				
				Convertidor convertidor = new Convertidor(cp,m,v);
				convertidores.add(convertidor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return convertidores;
    } 
  	public void ModificarDolarOtras(String cod_pais, String moneda, String valor_dolar) {
		 String Sql;
	    Sql = "UPDATE convertidor SET moneda='" + moneda + "', valor_dolar=" + valor_dolar + " " +
	    	  "WHERE cod_pais='"+ cod_pais +"';";

	    Conexion.ejecutar(Sql);
	 }

}
