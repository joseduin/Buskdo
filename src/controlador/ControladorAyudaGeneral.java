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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Cardlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class ControladorAyudaGeneral extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	@Wire private Div botonera;
	@Wire private Cardlayout card;
	
	private static final String NORMAL = "btn ot";
	private static final String ACTIVO = "btn btn-danger";
	
	@Override
	 public void doAfterCompose(Component comp) throws Exception {
	   super.doAfterCompose(comp);
	   card.setSelectedIndex(0);
	 }
	
	@Listen("onClick = #btnSiguiente")
	public void cardSiguiente() {
		card.next();
		btnEstilo(card.getSelectedIndex());
	}
	@Listen("onClick = #btnAnterior")
	public void cardAnterior() {
		card.previous();
		btnEstilo(card.getSelectedIndex());
	}

	@Listen("onClick = #botonera button")
	public void card(Event e) {
		if (e.getTarget() instanceof Button) {
			Button btn = (Button) e.getTarget();
			int id = Integer.parseInt(btn.getId());
			
			card.setSelectedIndex(id);
			btnEstilo(id);
		}
	}
	
	private void btnEstilo(int boton) {
		for (int i = 0; i < card.getChildren().size(); i++) {
			Button btn = (Button) botonera.getChildren().get(i);
			btn.setZclass("btnCircular " + estilo(i, boton));
		}
	}
	
	private static String estilo(int i, int cond) {
		return (i == cond) ? ACTIVO : NORMAL ;
	}
	
	public static ArrayList<String> botonera(int hijos) {
		ArrayList<String> botones = new ArrayList<String>();
		for(int i = 0; i < hijos; i++) {
				botones.add("btnCircular " + estilo(i, 0));
		}
		return botones;
	}
	
}
