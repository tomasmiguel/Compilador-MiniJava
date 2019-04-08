package Nodos;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Componentes.Analizador_Sintactico;
import Componentes.Token;
import Componentes.Unidad;
import Componentes.VarMetodo;

public class NodoBloque extends NodoSentencia {
	private List<NodoSentencia> listaSentencias;
	protected NodoBloque padre; //bloque que lo contiene
	protected HashMap<String,VarMetodo> variables;//variables que se declaran en este bloque
	protected Unidad unidad;
	public int cantVarLocales;
	
	
	public NodoBloque(NodoBloque p, Token tok) {
		super(tok);
		padre=p;
		listaSentencias=new ArrayList<NodoSentencia>();
		variables = new HashMap <String,VarMetodo>();
		cantVarLocales=0;
	}
	
	
	public void agregarSentencia(NodoSentencia s) {
		listaSentencias.add(s);
	}
	
	public NodoBloque getBloquePadre() {
		return padre;
	}

	public Unidad getUnidad() {
		return unidad;
	}
	
	public void agregarVariableLocal(String nombre,VarMetodo v) {
		variables.put(nombre, v);
	}

	public List<NodoSentencia> getListaSentencias(){
		return listaSentencias;
	}

	public HashMap<String,VarMetodo> getListaVariables(){
		return variables;
	}
	

	
	
	public void chequear() throws Exception {
	
		//seteo bloqueActual this
		Analizador_Sintactico.TS.setBloqueActual(this);
		
		//chequeo cada sentencia de mi bloque
		for(NodoSentencia s: listaSentencias)
			s.chequear();

		unidad= Analizador_Sintactico.TS.getUnidadActual();
		
		//contadro de var locales
		cantVarLocales= variables.size();
		
		//elimino cada v de mi varsParams de metodo
		for(String s: variables.keySet()) {
			unidad.getEntradaVarsParams().remove(s);
		}
		
		//seteo bloqueActual mi padre
		Analizador_Sintactico.TS.setBloqueActual(padre);	 
	}
	
	
	

	public void generar() throws Exception {
		
		for(NodoSentencia s: listaSentencias) {
			s.generar();	
		}
		
		//liberar var locales definidas en este bloque
		Analizador_Sintactico.salida.generar("FMEM "+(cantVarLocales), "libero memoria de las variables declaradas en este bloque");		
		
		
		//actualizo variables locales de mi metodo
		//unidad.setOffsetVarLocal((unidad.getOffsetVarLocal()* -1) - cantVarLocales);
		unidad.offsetVarLocal -= this.cantVarLocales;
		
		
		//simulo el comportamiento de chequear, eliminando las variables de este bloque	
		for(String s: variables.keySet()) {
			unidad.getEntradaVarsParams().remove(s);
		}
		
		//seteo bloqueActual mi padre
		Analizador_Sintactico.TS.setBloqueActual(padre);	
	}
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Bloque ("+token.getLexema()+") hijo de ("+padre+")");
		for(NodoSentencia s: listaSentencias)
			s.imprimir(n+1);
	}
	
	
}
