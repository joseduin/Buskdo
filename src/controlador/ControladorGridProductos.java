/*
 * INTEGRANTES:
 * Darling Gimenez  CI: 20.926.765
 * Jose Miguel Duin CI: 21.142.293
 * Patricia Freitez CI: 21.526.571
 *
 * Laboratorio II
 *
 * Febrero 2016
 *
 * Copyright (c)
 */

package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Vbox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;

import modelo.Producto;
import modelo.Usuario;
import servicio.HistorialDao;
import servicio.Meli;

/*		INICIAR APLICACION POR 1RA VEZ
  * 
  * Se redireccionará a una pag de mercadolibre, para colocar usuario y clave
  * 	Usuario:	josemiguelduin@hotmail.com
  * 	clave: 		antiadherente
  * */
public class ControladorGridProductos extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 1L;
	
	@Wire private Listbox datosProductos;
	@Wire private Label code;
	@Wire private Label precio;
	@Wire private Html DescAnalisis;
	@Wire private Html DescPrecio;
	@Wire private Html DescConver;
	@Wire private Label categoria;
	@Wire private Label ubicacion;
	@Wire private Image imgImagenPrevia;
	@Wire private Combobox portalbox;
	@Wire private Combobox historial;
	@Wire private Combobox comboOrden;
	@Wire private Combobox comboCategoria;
	@Wire private Combobox comboUbicacion;
	@Wire private Vbox filtro;
	@Wire private Hbox analisis;
	@Wire private Hbox hboxPrecio;
	@Wire private Hbox hboxPrecio1;
	@Wire private Hbox hboxUbicacion;
	@Wire private Hbox hboxCategoria;
	@Wire private Hlayout contenedor;
	@Wire private Div productosImagen;
	@Wire private Button btnLista;
	@Wire private Button btnGaleria;
	@Wire private Button anteriorGaleria;
	@Wire private Button siguienteGaleria;
	@Wire private Doublebox min;
	@Wire private Doublebox max;
	
// Variables ----------------------------------------------------------------------------
	private ControladorDatosVistas datos = new ControladorDatosVistas();
	private HistorialDao histori = new HistorialDao();
	
	private static final int cantProductos = 4;		// GALERIA: 4 productos
	private int contador = 0;
	private int paginaGaleria = 0;
	
	private String codigo;
	private static List<Producto> productosLista = new ArrayList<Producto>();
	private FluentStringsMap params = new FluentStringsMap();
	private Meli m = new Meli("7892861532650836", "ZEy1f3HtMODEhPpOMqEfzFl5bKEpcJFQ");
	
//---------------------------------------------------------------------------------------	
	
	/* INICIALIZAR PANTALLA
	 * 		Este metodo permite cargar componentes apenas
	 * 		se crea la vista
	 * */
	 @Override
	 public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if(datos.miSession.getAttribute("credenciales") != null) {
			Usuario usu = datos.getUsuario();
			historial.setModel(histori.ConsultarHistorial(usu.getNick()));
			 
			String cod = code.getValue().replaceFirst("code=", "");
			codigo = cod.replaceFirst("-200970490", "");	
			
			String[] orden = new String[] {"Mas relevante","Mayor precio", "Menor precio"};
			comboOrden.setModel(new ListModelList<String>(orden));
			
			params.clear();
	//productosLista.clear();
			portalbox.setModel(datos.getComboPortal());
			analisis.setVisible(false);
			contenedor.setVisible(false);
		}
	 }
	 
	 
	 
/*******************************************************
* 	CATEGORIA UBICACION
* 	@param Event
**/ 
	@Listen("onSelect = #comboCategoria; onSelect = #comboUbicacion")
	public void buscarCategoria(Event e) {		
		productosLista.clear();
		if (e.getTarget() instanceof Combobox) {
			Combobox combo = (Combobox) e.getTarget();
			Comboitem combito = combo.getSelectedItem();
			
			if (combo.getId().equals("comboCategoria")) {
				hboxCategoria.setVisible(true);
				categoria.setValue(combito.getLabel().toString());
				params.add("category", combito.getValue().toString());
			} else {
				hboxUbicacion.setVisible(true);
				ubicacion.setValue(combito.getLabel().toString());
				params.add("state", combito.getValue().toString());
			}
			
			combo.setVisible(false);
			iniciarBusqueda();
		}
	}
	@Listen("onClick = #btnIr; onOK = #max")
	public void ir() {
		productosLista.clear();
		if (min.getText().trim().equals("")) {
			datos.campoRequerido(min);			
		} else if (max.getText().trim().equals("")) {
			datos.campoRequerido(max);
		} else {
			double minimo = min.getValue();
			double maximo = max.getValue();
			
			if (minimo < maximo) {
				hboxPrecio.setVisible(true);
				hboxPrecio1.setVisible(false);
				
				params.add("price",minimo + "-" + maximo);
				precio.setValue(minimo + " - " + maximo);
			
				iniciarBusqueda();
			} else {
				datos.mensaje("Valores Invalidos", 2, null);
			}
			
		}
	}
	
	@Listen("onClick = #cerrarCategoria; onClick = #cerrarUbicacion; onClick = #cerrarPrecio")
	public void eliminarCategoria(Event e) {		
		productosLista.clear();
		if (e.getTarget() instanceof Label) {
			Label label = (Label) e.getTarget();
			
			if (label.getId().equals("cerrarCategoria")) {
				eliminarCategoriaParam(hboxCategoria, "category", comboCategoria);
				
			} else if (label.getId().equals("cerrarUbicacion")) {
				eliminarCategoriaParam(hboxUbicacion, "state", comboUbicacion);
				
			} else {
				eliminarCategoriaParam(hboxPrecio, "price", hboxPrecio1);
			}
			iniciarBusqueda();
		}
	}
	private void eliminarCategoriaParam(Hbox hbox, String eliminar, Component componet) {
		hbox.setVisible(false);
		params.delete(eliminar);
		componet.setVisible(true);
	}
	
	@Listen("onSelect = #comboOrden")
	public void orden(Event e) {
		productosLista.clear();
		String valor = comboOrden.getSelectedItem().getValue();	
		
		params.delete("sort");
		String sort = (valor.equals("Mas relevante")) ? "relevance"  :
					  (valor.equals("Mayor precio"))  ? "price_desc" : "price_asc";
		
		params.add("sort", sort);
		
		iniciarBusqueda();
		
	}
	
	/* CARGAR FILTROS - LADO IZQUIERDO
	 * 		Se cargan los filtros de la busqueda en curso
	 * 		a partir del RESPONSE
	 * 			CATEGORIA
	 * 			UBICACION
	 * */
	private void cargarFiltros(JsonObject jo) {
		removerTodo(comboCategoria);
		removerTodo(comboUbicacion);
		JsonArray filtrosDisponibles = jo.getAsJsonArray("available_filters");
		for (JsonElement e : filtrosDisponibles) {
			if (datos.buscarJson(e, "id").equals("category")) {
				cargarComboFiltros(e, comboCategoria);
				
			} else if (datos.buscarJson(e, "id").equals("state")) {
				cargarComboFiltros(e, comboUbicacion);
			}
		}
		crearComponente(comboCategoria);
		crearComponente(comboUbicacion);
	}
	private void cargarComboFiltros(JsonElement e, Combobox combo) {
		JsonObject json = datos.JsonElementToJsonObject(e);
		JsonArray array = json.getAsJsonArray("values");
		for(JsonElement a : array) {
			combo.appendChild(comboHijo(a));
		}
	}
	private Comboitem comboHijo(JsonElement e) {
		Comboitem comboHijo = new Comboitem();
		comboHijo.setLabel(datos.buscarJson(e, "name"));
		comboHijo.setValue(datos.buscarJson(e, "id"));
		return comboHijo;
	}

	/*	CARGAR PRODUCTOS
	 * 		Se cargan los productos a
	 * 		partir del RESPONSE
	 **/
	private void cargarProductos(JsonObject jo) {
		JsonArray tags = jo.getAsJsonArray("results");
		
		for (JsonElement e : tags) {
			String precio =  (datos.buscarJson(e, "price").equals("null")) 
					          ? "0" : datos.buscarJson(e, "price");
			String condicion = (datos.buscarJson(e, "condition").equals("null")) 
					           ? "Sin definir": datos.buscarJson(e, "condition");
			
			productosLista.add(new Producto(datos.buscarJson(e, "title"), 
											 datos.imgNormal(datos.buscarJson(e, "thumbnail")), 
											 datos.buscarJson(e, "address", "state_name"), 
											 condicion, 
											 Double.parseDouble(precio), 
											 datos.moneda(datos.buscarJson(e, "currency_id")),
											 datos.buscarJson(e, "currency_id"),
											 Integer.parseInt(datos.buscarJson(e, "sold_quantity")),
											 Integer.parseInt(datos.buscarJson(e, "available_quantity"))));
		}
	}
	
	/*	BUSQUEDA
	 * 		Busqueda sencilla en Mercadolibre
	 * */
	@Listen("onClick = #btnBusqueda; onOK = #historial;" +
			"onOK = #portalbox")
	public void busqueda() {				
		if (portalbox.getSelectedIndex() == -1) {
			datos.campoRequerido(portalbox);
		} else if (historial.getValue().equals("")) {
			datos.campoRequerido(historial);
		} else {
			try {
				Usuario usu = datos.getUsuario();
				histori.ResgistroHistorial(usu.getNick().toUpperCase(), historial.getValue().toUpperCase());
				historial.setModel(histori.ConsultarHistorial(usu.getNick()));

				productosLista.clear();
				params.clear();
				
				params.add(codigo, m.getAccessToken());
				params.add("q", historial.getValue().trim());
				params.add("offset", Integer.toString(0));	
				params.add("limit", Integer.toString(200));
				
				comboOrden.setSelectedIndex(0);
				
				iniciarBusqueda();
			} catch (Exception e) {
				datos.mensaje("Revise su conexion!", 3, null);
			}
			
		}
	}
	
	private void iniciarBusqueda() {
		Response response;
		try {
			response = m.get("/sites" + 
					   datos.portal(portalbox.getSelectedItem().getLabel()) + 
					   "search?", params);
			JsonObject jo = datos.stringToJsonObject(response.getResponseBody());

//			JsonParser parser = new JsonParser();		
//			FileReader fr = new FileReader("C:/Users/Jose/Documents/perro.json");
//		    JsonObject jo = parser.parse(fr).getAsJsonObject();

			cargarProductos(jo);
			if (productosLista.size() > 0) {
				filtro.setVisible(true);
				cargarFiltros(jo);
				datosProductos.setModel(new ListModelList<Producto> (productosLista));
				
				contador = 0;
				paginaGaleria = 0;			
				
				analisis.setVisible(false);
				anteriorGaleria.setVisible(false);
				contenedor.setVisible(true);

				galeria();
			} else {
				datos.mensaje("No hay publicaciones que coincidan con tu busqueda.", 1, null);
			}
		} catch (Exception e) {
			datos.mensaje("Revise su conexion.", 3, null);
		}
	}



	public void galeria() {		// INICIALIZA GALERIA
								//	   1        2        3			Paginas
								// [0 1 2 3][4 5 6 9][8 9 10 11]	Productos
		int cont = contador;
		removerTodo(productosImagen);
		List<Producto> pro = productosLista;		

		for (int i = 0; i < cantProductos; i++) {
			if ((cont < ((paginaGaleria + 1)*cantProductos)) && (cont < pro.size())) {
				HashMap<String, Object> arg  = new  HashMap<String, Object>();

				arg.put("imagen", String.valueOf(pro.get(contador).getImagen()));
				arg.put("nombre", String.valueOf(pro.get(contador).getNombre()));
				arg.put("precio", String.valueOf(pro.get(contador).getPrecio()));
				arg.put("moneda", String.valueOf(pro.get(contador).getMoneda()));
				arg.put("condicion", String.valueOf(pro.get(contador).getCondicion()));
				arg.put("id", String.valueOf(contador++));

				Component component = Executions.createComponents("galeria.zul", productosImagen, arg);
				crearComponente(component);
				cont++;
			}
		}
		
		if (productosImagen.isVisible() == true) {
			if ((productosLista.size() < (cantProductos + 1)) || (contador == pro.size())) {
				siguienteGaleria.setVisible(false);
			} else {
				siguienteGaleria.setVisible(true);
			}
		}
		paginaGaleria++;
	}
	private void crearComponente(Component component) {
		Selectors.wireComponents(component, this, false);
		Selectors.wireEventListeners(component, this);
	}
	private void removerTodo(Component componente) {		// Elimina los hijos
		List<Component> componentes = componente.getChildren();
		for (int i = componentes.size() - 1; i >= 0; i--) {
			componente.removeChild(componentes.get(i));
		}
	}
	
	@Listen("onClick = #siguienteGaleria; onClick = #anteriorGaleria")
	public void galeriaSiguienteAnteriorPagina(Event e) {		// BOTONES DE GALERIA << - >>
		if (e.getTarget() instanceof Button) {
			Button btn = (Button) e.getTarget();
			
			if (btn.getId().equals("siguienteGaleria")) {
				anteriorGaleria.setVisible(true);
			} else {
				contador = 0;
				paginaGaleria = paginaGaleria - 2;
				for (int i = 0; i < paginaGaleria; i++) {
					for (int j = 0; j < cantProductos; j++) {
						contador++;
					}
				}
				siguienteGaleria.setVisible(true);
				if(paginaGaleria == 0) {
					anteriorGaleria.setVisible(false);
				}
			}
			galeria();
		}
	}
	
	@Listen("onClick = #btnGaleria; onClick = #btnLista")
	public void mostrarGaleria(Event e){			// Visibilidad de GALERIA - LISTADO PRODUCTO
		analisis.setVisible(false);
		anteriorGaleria.setVisible(false);
		
		if (e.getTarget() instanceof Button) {
			Button btn = (Button) e.getTarget();
			boolean valor = (btn.getId().equals("btnGaleria")) ? false: true; 
				
													//	GALERIA:	LISTA:
			datosProductos.setVisible(valor);		//   FALSE   -   TRUE
			btnGaleria.setVisible(valor);			//   FALSE   -   TRUE
			productosImagen.setVisible(!valor);		//   TRUE    -   FALSE
			btnLista.setVisible(!valor);			//   TRUE    -   FALSE
			siguienteGaleria.setVisible(!valor);	//   TRUE    -   FALSE
			
			contador = 0;
			paginaGaleria = 0;
			galeria();
		}
	}
	
	@Listen("onSelect = #datosProductos; onClick = div")
	public void mostrarDetallesAnalisis(Event e) {
		try {
			Producto selected = new Producto();
			if (e.getTarget() instanceof Div) {
				Div div = (Div) e.getTarget();
				if (div.getId().equals("")) {
					return;
				}
				List<Producto> pro = productosLista;						// Galeria
				selected = pro.get(Integer.parseInt(div.getId()));  
			} else {
				selected = datosProductos.getSelectedItem().getValue();		// ListBox
			}			

			analisis.setVisible(true);
			imgImagenPrevia.setSrc(datos.imgExtraGrande(selected.getImagen()));

			DescAnalisis.setContent("<ins>Ubicacion</ins>: " + selected.getUbicacion() + "<br>" +
									"<ins>Condicion</ins>: " + datos.condi(selected.getCondicion()) + "<br>" +
									"<ins>Articulos disponibles</ins>: " + selected.getDisponible() + "<br>" +
									"<ins>Articulos vendidos</ins>: " + selected.getVendido() + "<br>" +
									"<ins>Nombre</ins>: " + selected.getNombre() + "<br>");
			
			DescPrecio.setContent(datos.preci(selected.getPrecio(), selected.getMoneda()) + "<br>");
			if (!selected.getId_moneda().equals("VEF")) {									//  Conversiones a otras monedas
				DescConver.setVisible(true);
				DescConver.setContent("<blockquote>" +
									  	datos.conv(selected.getPrecio(), selected.getId_moneda(), 0) + "<br>" +
									  	datos.conv(selected.getPrecio(), selected.getId_moneda(), 1) + 
									  "</blockquote>");
			} else {
				DescConver.setVisible(false);
			}
		} catch (Exception ex) {}
	}
	
}