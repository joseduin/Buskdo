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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import modelo.Convertidor;
import modelo.Portales;
import modelo.Usuario;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import servicio.BolivarDolarDao;
import servicio.ConvertidorDao;
import servicio.PortalDao;
import servicio.UsuarioDao;

public class ControladorDatosVistas extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 1L;
	@Wire private Window windo;
	
	private static DecimalFormat df = new DecimalFormat("###,###,###,###.00");
	private final static String banderita = "/imagen/bandera_%s.png";

	public Session miSession = Sessions.getCurrent();
	private UsuarioDao usuarioDAO = new UsuarioDao();
	private PortalDao portalDAO = new PortalDao();
	private ConvertidorDao dolarOtrasDAO = new ConvertidorDao();
	private BolivarDolarDao dolares = new BolivarDolarDao();
	
	private ArrayList<Portales> portales = portalDAO.ConsultarPortal();
	private ArrayList<Convertidor> dolarOtras = dolarOtrasDAO.ConsultarConvertidor(true);

	@Listen("onClick = #cerrarVentana")
	public void cerrarVentana() {
		windo.detach();
	}
	
/******************
 * GRID PRODUCTOS *
 ******************/
	public ListModelList<String> getComboPortal() {
		ArrayList<String> combo = new ArrayList<String>();
		
		for (Portales portal : portales) {
			if (portal.isEstatus() == true) {
				combo.add(portal.getPais().trim() + " " + portal.getDominio().trim());
			}
		}
		return new ListModelList<String>(combo);
	}
	public String comboPortalBusqueda(String valor) {
		StringTokenizer token = new StringTokenizer(valor, " ");
		String pais = token.nextToken();
		String dominio = token.nextToken();
		String url = "";
		for (Portales portal : portales) {
			if (portal.getPais().equalsIgnoreCase(pais)
					&& portal.getDominio().equalsIgnoreCase(dominio)) {
				url = portal.getPortal().trim();
				break;
			}
		}
		return url;
	}
	public String portal(String po) {
		String codPortal = "";
		
		for (Portales portal : portales) {
			if (comboPortalBusqueda(po).equals(po)) {
				codPortal = "/" + portal.getCod_portal() + "/";
				break;
			}
		}
		return codPortal;
	}
	
	public String moneda(String pais) {
		String moneda = "";
		
		for (Convertidor dolarotra : dolarOtras) {
			if (dolarotra.getPais().equals(pais)) {
				moneda = dolarotra.getMoneda().trim() ;
				break;
			}
		}
		return moneda; 
	}
	public String conv(double pre, String pais, int dolar) {
    	ArrayList<Double> d = new ArrayList<Double>();
    	d = dolares.ConsultarDolar();		
		double paralelo = d.get(0);
		double oficial = d.get(1);
		double conv = 0.0;

		for (Convertidor dolarotra : dolarOtras) {
			if ((dolarotra.getPais().equals(pais)) && (!dolarotra.getPais().equals("VEF"))) {
				conv = pre * Double.parseDouble(dolarotra.getValor());
				break;
			}
		}
		
		double dol = (dolar == 0) ? oficial * conv : paralelo * conv;		// 0 Oficial, 1 Paralelo
		return (dolar == 0) ? "<ins>Precio Simadi</ins>: Bs " + df.format(dol) : "<ins>Precio Paralelo</ins>: Bs " + df.format(dol);
	}
	
	public String condi(String co) {
		return (co.equals("new"))  ? "Nuevo" : 
			   (co.equals("used")) ? "Usado" : 
			   (co.equals("null")) ? "Sin especificar" : co;
	}
	public String preci(double pre, String mo) {
		return (pre == 0) ? "<strong>Precio a convenir</strong>" :
							"<ins>Precio</ins>: " + mo + " " + df.format(pre);
	}
	public String formato(double num) {
		return df.format(num);
	}
	
	public JsonObject stringToJsonObject(String cadena) {
		JsonParser jp = new JsonParser (); 
		JsonElement je = jp.parse(cadena);
		return JsonElementToJsonObject(je);
	}
	public JsonObject JsonElementToJsonObject(JsonElement je) {
		return je.getAsJsonObject();
	}
	public String buscarJson(JsonElement je, String buscar) {
		JsonObject jo = JsonElementToJsonObject(je);
		return validarJsonObjToString(jo.get(buscar));
	}
	public String validarJsonObjToString(JsonElement jsonElement) {
		String a = jsonElement.toString();
		return a.replaceAll("\"", "");
	}
	public String buscarJson(JsonElement je, String array, String buscar) {
		JsonObject jo = JsonElementToJsonObject(je);
		return buscarJson(jo.get(array), buscar);
	}
	public String imgNormal(String cadena) {		// Por defecto Mercadolibre manda imagenes CHICAS
		return cadena.replace("I.jpg", "Y.jpg");	//	
	}												// Chica:			I.jpg
	public String imgExtraGrande(String cadena) {	// Normal:			Y.jpg
		return cadena.replace("I.jpg", "F.jpg");	// Grande:			O.jpg
	}												// Extra-Grande:	F.jpg
	public String getIcono(String icon) {
		 return String.format(banderita, icon.substring(icon.length() - 2, icon.length()));
	}
	
	public String getBtnGaleria() {
		return "/imagen/galeria.png";
	}
	
	public String getBtnLista() {
		return "/imagen/lista.png";
	}
	
	public String getBtnLupa() {
		return "/imagen/search.png";
	}
	
	public String getBtnFiltro() {
		return "/imagen/filtro.png";
	}
	
/********************
 * DATOS PERSONALES *
 ********************/
	public Usuario getUsuario() {
		Usuario usu = new Usuario(miSession.getAttribute("nick").toString(),
								  miSession.getAttribute("clave").toString(),
								  miSession.getAttribute("credenciales").toString(),
								  miSession.getAttribute("estatus").toString());
		
    	return usu;
    	
	}
	
	public String getClaveEncriptada() {
		String clave = "";
			for (int i = 0;i < getUsuario().getClave().length(); i++) {
				clave += "*";
		}
		return clave;
	}
	
	public void logout() {
		miSession.invalidate();
		redireccion("/");
	}
	
/******************
 **** ACERCA DE ***
 ******************/
	public ArrayList<Usuario> getUsuariosCedula() {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(new Usuario("Darling Gimenez", "CI: 20.926.765", null, null));
		usuarios.add(new Usuario("Jose Miguel Duin", "CI: 21.142.293", null, null));		
		usuarios.add(new Usuario("Patricia Freitez", "CI: 21.526.571", null, null));
		//usuarios.add(new Usuario("Richard Nieto", "CI: 21.142.293", nulll, null));

		return usuarios;
	}

	public String getInteStyle() {
		return "integrantes asbalto-oscuro";
	}
	
	public String getCedStyle() {
		return "integrantes belice";
	}
	
	public String getNombreStyle() {
		return "integrantes verde-oseano";
	}
	
/*******************
 * AYUDA GENERALES *
 *******************/
	public String getAyudaGeneral() {
		return "<p class=\"text-justify\"> Aunque la web, el marketing online y " +
			   "la publicidad han sufrido una revolucion sin precedentes en " +
			   "los ultimos siglos, los metodos utilizados en la recogida y el " +
			   "analisis de datos apenas habian cambiado hasta la reciente " +
			   "llegada de BUSKDO web.</p>" +
			   "<p class=\"text-justify\"> BUSKDO ha abierto todo un campo de " +
			   "posibilidades para analizar los precios de productos/servicios " +
			   "y ha permitido que empresas y profesionales empiecen a tomar " +
			   "decisiones inteligentes basadas en el estudio analitico de los datos.</p>" +
			   "<p class=\"text-center\"><strong>Conceptos Basicos:</strong></p>" +
			   "<ul>" +
				   "<li class=\"text-justify\"><ins>SIMADI: (Sistema Marginal de Divisas)</ins>: " +
				   "Los Dolares SIMADI se podrin adquiridos por personas juridicas y" +
				   " naturales que tengan cuentas en dolares en los bancos nacionales. " +
				   "El tope maximo para compras de efectivo en las casas de bolsa y bancos " +
				   "es de $300 diarios, $2.000 al mes o $10.000 por annio calendario.</li>" +
				   "<li class=\"text-justify\"><ins>DOLAR PARALELO</ins>: El precio es marcado " +
				   "usando el promedio de las negociaciones que se logran para comprar/vender " +
				   "dolares por transferencia o efectivo DENTRO de Venezuela fuera de las " +
				   "tasas oficiales.</li>" +
			   "</ul>";
	}
	
/******************
 ***** VARIOS *****
 ******************/
	public void redireccion(String url) {
		Executions.sendRedirect(url);
	}
	
	public void sesion(Usuario usu) {
		miSession.setAttribute("nick",usu.getNick());
		miSession.setAttribute("clave",usu.getClave());
		miSession.setAttribute("credenciales",usu.getCredenciales());
		miSession.setAttribute("estatus", usu.getEstatus());
		
		if (usu.getEstatus().equals("ACTIVO")) {
			String url = (usu.getCredenciales().equals("USUARIO")) 
					? "/" : "administrador/administracion.zul";
			redireccion(url);
		}
	}
	
	public String getEfectoWindows() {
		// Efectos de windows [slideDown - slideUp - slideIn - slideOut]  
		return "show: slideDown({duration: 300}); hide: slideUp({duration: 100})";
	}
	
	public void eliminarUsuario(Usuario usu) {
		usu.setEstatus("BLOQUEADO");
		modificarUsuario(usu);
		logout();
	}
	
	public void modificarUsuario(Usuario usu) {
		usuarioDAO.ModificarUsuario(usu.getNick(), usu.getClave(), usu.getCredenciales(), usu.getEstatus());
	}
	
	public void ingresarUsuario(String nick, String clave) {
		usuarioDAO.ResgistroUsuario(nick, clave, "USUARIO", "ACTIVO");
	}
	
	public Usuario buscarUsuario(String nombre, String contrasennia) {
		Usuario usuario = new Usuario();
		for (Usuario usu : usuarioDAO.ConsultarUsuarios()) {
			if((usu.getNick().equalsIgnoreCase(nombre)) 
				&& ((usu.getClave().equals(contrasennia)) 
				|| (contrasennia.equals("")))) {
				
				usuario = new Usuario(usu.getNick().trim(),
									  usu.getClave().trim(),
									  usu.getCredenciales().trim(),
									  usu.getEstatus());
			}
		}
		return usuario;
	}
	
	public static String decimal(double numero) {
        return df.format(numero);
    }
	
/* MENSAJES */
	public void mensaje(String mensaje, int tipo, Component com) {
		String t = (tipo == 1) ? "info" : 
				   (tipo == 2) ? "warning" : "error";
		Clients.showNotification(mensaje, t, com, null, 2000, true);
	}
	public void alerta(String mensaje, String titulo) {
		Clients.alert(mensaje, titulo, null);
	}
	
	public void campoRequerido(Component comp) {
		mensaje("Campo Obligatorio", 2, comp);
	}
	
}
