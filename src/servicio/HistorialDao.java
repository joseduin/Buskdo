package servicio;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.zkoss.zul.ListModelList;

import bean.Conexion;
import modelo.ClassConexionDAO;

public class HistorialDao extends ClassConexionDAO {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa");
	
	public HistorialDao (){
		super ();
	}
   
 //crea un arreglo para guardas todos los valores obtenidos en la consulta 
 	public ListModelList<String> ConsultarHistorial(String nick_name) {
 		ArrayList<String> historial = new ArrayList<String>(); 		
		String tiraSQL = "SELECT DISTINCT descripcion, fecha FROM historial " +
						 "WHERE nick_name = '" + nick_name.toUpperCase() + "' " +
						 "ORDER BY fecha DESC;";
		ResultSet resultSet = Conexion.consultar(tiraSQL);		
		try {
			int i = 0;		// controla la cantidad de historial que se muestra
			while (resultSet.next() && i < 3) {
				String desc = resultSet.getString("descripcion");
				if (historial.isEmpty()) {
					historial.add(desc);
					i++;
				} else {
					if (!historial.contains(desc)) {
						historial.add(desc);
						i++;
					}
				}
			}
		} catch (Exception e) {}
		return new ListModelList<String>(historial);
   } 
 	
 	public void ResgistroHistorial(String nick_name, String descripcion) {
 		Calendar calendar = Calendar.getInstance();
 		String Sql = "INSERT INTO historial VALUES " +
 				"('" + nick_name + "', '" + descripcion.toUpperCase() + "', " +
 				"'"+ sdf.format(calendar.getTime()) +"');"; 
    	Conexion.ejecutar(Sql);
 	}
 	
}
