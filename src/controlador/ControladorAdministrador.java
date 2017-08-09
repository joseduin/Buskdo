
package controlador;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;

public class ControladorAdministrador extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	private ControladorDatosVistas datos = new ControladorDatosVistas();

	@Wire private  Include urls;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		bolivar();
	}

	@Listen("onClick = navitem#bolivarDolar")
	public void bolivar() {
		urls.setSrc("bolivarDolar.zul");
	}
	
	@Listen("onClick = navitem#portales")
	public void portales() {
		urls.setSrc("portal.zul");
	}
	
	@Listen("onClick = navitem#dolarOtras")
	public void convertir() {
		urls.setSrc("convertidor.zul");
	}
	
	@Listen("onClick = navitem#admUsuarios")
	public void usuarios() {
		urls.setSrc("usuarios.zul");
	}
	
	@Listen("onClick = navitem#vistaUsuario")
	public void vistaUsuario() {
		datos.redireccion("../menu.zul");
	}
}
