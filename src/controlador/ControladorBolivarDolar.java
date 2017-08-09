package controlador;

import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Doublebox;

import servicio.BolivarDolarDao;

public class ControladorBolivarDolar extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
//se hace instancias de otros controladores para utilizar sus metodos
	BolivarDolarDao bolidol = new BolivarDolarDao();
	ControladorDatosVistas datos = new ControladorDatosVistas();
//se llaman los textbox para inertar sus valores
	@Wire private Doublebox paralelo;
	@Wire private Doublebox oficial;
	
//este metodo hace que se cargue apenas se abra la pantalla
	 @Override
	 public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		inicializar();
		

	}
//este metodo hace hace la consulta con la base de datos lo guarda en un arreglo para se llamado cuando se necesite	 
	 public void inicializar(){
		//se crea un arreglo que guardara los valores de las columnas de dolar que se consultan
			ArrayList<Double> dolar = new ArrayList<Double>();
			dolar = bolidol.ConsultarDolar();
	//asigna a los texbox los valores de dolares que se consultan en la bd y se convierten a string
			paralelo.setValue(dolar.get(0));
			oficial.setValue(dolar.get(1));
	 }
//guarda los nuevos valores de los dolares q se insertan en la texbox a la bd
	@Listen  ("onClick = #guardar")
	public void guardar(){
		//valida que los textbox no esten vacios
		if (paralelo.getValue().toString().equals("")) {
			datos.campoRequerido(paralelo);
		} else if (oficial.getValue().toString().equals("")) {
			datos.campoRequerido(oficial);
		} else {
			double nuevoParalelo = paralelo.getValue();
			double nuevoOficial = oficial.getValue();
			
			bolidol.ModificarDolar(nuevoParalelo, nuevoOficial);
			// manda un mensaje de exito
			datos.mensaje("actualizado con exito", 1, null);
		}
	}
	
	@Listen  ("onClick = #cancelar")
	public void cancelar(){
		inicializar();
	}
}
