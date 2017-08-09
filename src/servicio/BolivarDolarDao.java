package servicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.Conexion;
import modelo.ClassConexionDAO;

public class BolivarDolarDao extends ClassConexionDAO {
	public BolivarDolarDao(){
		super();
	}
	
	public ArrayList<Double> ConsultarDolar() {
    	ArrayList<Double> dolar = new ArrayList<Double>();
		String tiraSQL = "SELECT * FROM bolivardolar";
		ResultSet resultSet = Conexion.consultar(tiraSQL);		
		try {
			while (resultSet.next()) {
				Double paralelo = resultSet.getDouble("paralelo");
				Double oficial = resultSet.getDouble("oficial");
	
				dolar.add(paralelo);
				dolar.add(oficial);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dolar;
    }
	 
	 public void ModificarDolar(double paralelo, double oficial) {
		 String Sql;
	    Sql = "UPDATE bolivardolar SET paralelo = " + paralelo + ", oficial = " + oficial +"";
	    	
	    Conexion.ejecutar(Sql);
	 }
}
