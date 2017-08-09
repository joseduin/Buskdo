package controlador;

import java.util.ArrayList;

import modelo.Usuario;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import servicio.UsuarioDao;

public class ControladorAdministracionUsuarios extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	@Wire private Listbox listaUsuarios;
	@Wire private Div listaUsuario;
	@Wire private Div modificar;
	@Wire private Label txtNick;
	@Wire private Combobox comboCredenciales;
	@Wire private Combobox comboEstatus;
	
	UsuarioDao usuarios = new UsuarioDao();
	ControladorDatosVistas datos = new ControladorDatosVistas();
	
	private ArrayList<Usuario> usuario = new ArrayList<Usuario>();

	//este metodo hace que se cargue apenas se abra la pantalla
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comboCredenciales.setModel(comboCredenciales());
		comboEstatus.setModel(comboEstatus());
		
		cargarArray();
		inicializar();
	}
	public void inicializar(){
		listaUsuarios.setModel(new ListModelList<Usuario> (usuario));
	}
	
	@Listen("onClick = #guardarUsuario")
	public void guardarUsuario() {
		Usuario usu = listaUsuarios.getSelectedItem().getValue();
		
		usu.setCredenciales(comboToString(comboCredenciales));
		usu.setEstatus(comboToString(comboEstatus));
		datos.modificarUsuario(usu);
		datos.mensaje("Usuario Modificado Exitosamente", 1, null);
		cargarArray();
		inicializar();
		cancelar();
	}
	
	@Listen("onClick = #modificarUsuario")
	public void modificar() {
		
		if (listaUsuarios.getSelectedIndex() == -1) {
			datos.mensaje("Seleccione Usuario a Modificar", 2, listaUsuarios);
		} else {
			cancelar();
			
			Usuario usu = listaUsuarios.getSelectedItem().getValue();
			txtNick.setValue(usu.getNick());
			comboCredenciales.setSelectedIndex(credencial(usu.getCredenciales()));
			comboEstatus.setSelectedIndex(estatus(usu.getEstatus()));
		}
	}	 
	
	@Listen("onClick = #cancelarUsuario")
	public void cancelar() {
		modificar.setVisible(listaUsuario.isVisible());
		listaUsuario.setVisible(!listaUsuario.isVisible());
	}
	public ListModelList<String> comboCredenciales() {
		ArrayList<String> combo = new ArrayList<String>();
		combo.add("ADMIN");
		combo.add("USUARIO");
		return new ListModelList<String>(combo);
	}
	public int credencial(String cre) {
		return (cre.trim().equals("ADMIN")) ? 0 : 1;
	}
	public ListModelList<String> comboEstatus() {
		ArrayList<String> combo = new ArrayList<String>();
		combo.add("ACTIVO");
		combo.add("BLOQUEADO");
		combo.add("PENDIENTE");
		combo.add("RESETEADO");
		return new ListModelList<String>(combo);
	}
	public int estatus(String est) {
		return ((est.trim().equals("ACTIVO"))   ? 0 : 
			   (est.trim().equals("BLOQUEADO")) ? 1 :
			   (est.trim().equals("PENDIENTE")) ? 2 : 3);
	}
	public void cargarArray() {
		usuario = usuarios.ConsultarUsuarios();
	}
	public String comboToString(Combobox com) {
		return com.getSelectedItem().getLabel();
	}
}
