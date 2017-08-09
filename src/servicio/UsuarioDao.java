package servicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.Conexion;
import modelo.ClassConexionDAO;
import modelo.Usuario;

public class UsuarioDao extends ClassConexionDAO {

	public UsuarioDao(){
		super();
	}
//crea un arreglo para guardas todos los valores obtenidos en la consulta 
  	public ArrayList<Usuario> ConsultarUsuarios() {
  		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
//hace una consulta de todos los valores que existen en la tabla
		String tiraSQL = "SELECT * FROM usuario";
		ResultSet resultSet = Conexion.consultar(tiraSQL);		
//agarra un posible error y lo muestra en pantalla
		try {
			while (resultSet.next()) {
				String nick = resultSet.getString("nick_name");
				String cla = resultSet.getString("clave");
				String cre = resultSet.getString("credenciales");
				String e = resultSet.getString("estatus");

//asigna los valores que se obtuvieron de la consulta al arreglo segun el orden de las columnas				
				Usuario usuario = new Usuario(nick.trim(), cla.trim(), cre.trim(), e.trim());
				usuarios.add(usuario);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuarios;
    } 
  	
  	public void ModificarUsuario(String nick_name, String clave, String credenciales, String estatus) {
		 String Sql;
	    Sql = "UPDATE usuario SET clave='" + clave + "', credenciales='" + credenciales + "' ,estatus = '" + estatus + 
	    	  "' WHERE nick_name='"+ nick_name +"';";

	    Conexion.ejecutar(Sql);
	 }
  	
  	public void ResgistroUsuario(String nick_name, String clave, String credenciales, String estatus) {
		 String Sql;
	    Sql = "INSERT INTO usuario VALUES ('" +
	    		nick_name.toLowerCase() + "', '" + clave + "', '" + credenciales + "', '" + estatus + "');"; 

	    Conexion.ejecutar(Sql);
	 }
  	
}
