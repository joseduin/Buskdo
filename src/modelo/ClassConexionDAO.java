
package modelo;

import bean.Conexion;

public class ClassConexionDAO {	
   
public ClassConexionDAO() 
     {
       
	super();	
       
	Conexion.establecerPropiedadesConexion("BDProperties",
                                              "jdbc.driver",
                                              "jdbc.url",
                                              "jdbc.nombrebd",
                                              "jdbc.usuario",
                                              "jdbc.password");
       }	
   
}