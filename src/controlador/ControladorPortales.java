package controlador;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;

import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import modelo.Portales;
import servicio.PortalDao;

public class ControladorPortales extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	@Wire private Div contieneLista;
	@Wire private Div contieneRegistrar;
	@Wire private Textbox cod_portal;
	@Wire private Textbox portal;
	@Wire private Textbox pais;
	@Wire private Textbox dominio;
	@Wire private Checkbox estatus;
	@Wire private Listbox listaPortales;
	PortalDao conv = new PortalDao();
	ControladorDatosVistas dato = new ControladorDatosVistas();

//este metodo hace que se cargue apenas se abra la pantalla
	 @Override
	 public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		inicializar();
	 }
	 public void inicializar(){
		 listaPortales.setModel(new ListModelList<Portales> (conv.ConsultarPortal()));

	 }
	//guarda los nuevos valores de los dolares q se insertan en la texbox a la bd
	@Listen("onClick = #guardarPortal")
	public void guardarPortal(){		
		int contador = 0;
	//recorre la lista segun su estruccturan desde la posicion 1 para entrar a obtener de las filas los datos q cada una contiene y van de un label, un textbox y un doublebox 
		for (int i = 1; i < (listaPortales.getItemCount() + 1); i++) {
				
			Listitem listItem = (Listitem) listaPortales.getChildren().get(i);
				
			Listcell listCellDominio = (Listcell) listItem.getChildren().get(0);
			Textbox dom = (Textbox) listCellDominio.getChildren().get(0);
	
			Listcell listCellCodPortal = (Listcell) listItem.getChildren().get(1);
			Textbox codi_portal = (Textbox) listCellCodPortal.getChildren().get(0);
			
			Listcell listCellDirec = (Listcell) listItem.getChildren().get(2);
			Textbox dir = (Textbox) listCellDirec.getChildren().get(0);
	
			Listcell listCellPais = (Listcell) listItem.getChildren().get(3);
			Textbox pais = (Textbox) listCellPais.getChildren().get(0);
				
			Listcell listCellCheckbox = (Listcell) listItem.getChildren().get(4);
			Checkbox checkbox	= (Checkbox)listCellCheckbox.getChildren().get(0);
			
			//valida que los textbox no esten vacios
			if (dom.getValue().equals("")) {
				dato.campoRequerido(dom);
			} else if (codi_portal.getValue().equals("")) {
				dato.campoRequerido(codi_portal);
			} else if (dir.getValue().equals("")) {
				dato.campoRequerido(dir);
			} else if (pais.getValue().equals("")) {
				dato.campoRequerido(pais);
			} else {
				conv.ModificarDolarOtras(codi_portal.getValue(), dir.getValue(), 
										 checkbox.isChecked(), pais.getValue(), dom.getValue());
				contador ++;
			}
		}
		if (listaPortales.getItemCount() == contador) {
			dato.mensaje("Capos Modificados Exitosamente!", 1, null);
		}
	}
		
	@Listen("onClick = #calcelarPortal")
	public void cancelar(){
		inicializar();
	}
	@Listen("onClick = #registrarPortal; onClick = #cancelarRegistro")
	public void registrar(){
		contieneRegistrar.setVisible(contieneLista.isVisible());
		contieneLista.setVisible(!contieneLista.isVisible());
	}
		
	@Listen("onClick = #guardarRegistro")
	public void guardar() {			
//valida que los textbox no esten vacios
		if (cod_portal.getValue().trim().equals("")) {
			dato.campoRequerido(cod_portal);
		} else if (portal.getValue().trim().equals("")) {
			dato.campoRequerido(portal);
		} else if (pais.getValue().trim().equals("")) {
			dato.campoRequerido(pais);
		} else if (dominio.getValue().trim().equals("")) {
			dato.campoRequerido(dominio);
		} else {
			conv.ResgistroPortal(cod_portal.getValue().toUpperCase(), portal.getValue(),
								 estatus.isChecked(), pais.getValue().toUpperCase(),
								 dominio.getValue().toUpperCase());
			dato.mensaje("Nuevo Portal " + portal.getValue() + " Agregado Exitosamente!" , 1, null);
			inicializar();
			registrar();
		}	
	}

}
