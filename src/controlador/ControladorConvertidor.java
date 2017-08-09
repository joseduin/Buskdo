package controlador;

import modelo.Convertidor;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import servicio.ConvertidorDao;

public class ControladorConvertidor  extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	@Wire private Listbox listaConvertidor;
	ConvertidorDao conv = new ConvertidorDao();
	ControladorDatosVistas datos = new ControladorDatosVistas();

//este metodo hace que se cargue apenas se abra la pantalla
	 @Override
	 public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		inicializar();
	 }
	 public void inicializar(){
			listaConvertidor.setModel(new ListModelList<Convertidor> (conv.ConsultarConvertidor(false)));

	 }
	//guarda los nuevos valores de los dolares q se insertan en la texbox a la bd
		@Listen("onClick = #guardarLista")
		public void guardarLista(){		
			int contador = 0;
	//recorre la lista segun su estruccturan desde la posicion 1 para entrar a obtener de las filas los datos q cada una contiene y van de un label, un textbox y un doublebox 
			for (int i = 1; i < (listaConvertidor.getItemCount() + 1); i++) {
				
				Listitem listItem = (Listitem) listaConvertidor.getChildren().get(i);
				
				Listcell listCellLabel = (Listcell) listItem.getChildren().get(0);
				Label label = (Label) listCellLabel.getChildren().get(0);
	
				Listcell listCellTextbox = (Listcell) listItem.getChildren().get(1);
				Textbox texbox = (Textbox) listCellTextbox.getChildren().get(0);
				
				Listcell listCellDoublebox = (Listcell) listItem.getChildren().get(2);
				Doublebox doublebox	= (Doublebox)listCellDoublebox.getChildren().get(0);
				
				//valida que los textbox no esten vacios
				if (texbox.getValue().equals("")) {
					datos.campoRequerido(texbox);
				} else if (doublebox.getValue().toString().equals("")) {
					datos.campoRequerido(doublebox);
				} else {
					conv.ModificarDolarOtras(label.getValue(), texbox.getValue(), doublebox.getValue().toString());
					contador ++;
				}
			}
			if (listaConvertidor.getItemCount() == contador) {
				datos.mensaje("Capos Modificados Exitosamente!", 1, null);
			}
		}
		
		@Listen("onClick = #calcelarLista")
		public void cancelar(){
			inicializar();
		}
}
