package Componentes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Nodos.NodoBloque;
import Tipos_Datos.Tipo;

public abstract class Unidad {
	protected List<EntradaPar> entradaParametros;
	protected HashMap<String, VarMetodo> entradaVarsParams;
	protected Token token;
	protected NodoBloque cuerpo;
	protected String nombre;
	
	protected boolean generado;
	protected int offsetMetodo;
	public int offsetVarLocal; //aumenta hacia los negativos -1,-2, etc
	

	protected Unidad( Token tok,String n) {
		token=tok;
		nombre=n;
		entradaParametros = new ArrayList<EntradaPar>();
		entradaVarsParams = new HashMap<String,VarMetodo>();
		offsetVarLocal=0;
		offsetMetodo=0;
		generado=false;
	}

	public abstract String getNombre();
	public abstract String getNombreAux();
	public abstract String getModificador();
	public abstract Tipo getTipoRetorno();

	public List<EntradaPar> getEntradaParametros(){
		return entradaParametros;
	}
	
	public HashMap<String,VarMetodo> getEntradaVarsParams(){
		return entradaVarsParams;
	}
	
	//si existe ese parametro o var local, lo retorna. Sino null
	public EntradaVariable esVarMetodoDeclarado(String idPar) {
		return entradaVarsParams.get(idPar);
	}
	
	public void setOffsetMetodo(int off) {
		offsetMetodo=off;
	}
	
	public int getOffsetMetodo() {
		return offsetMetodo;
	}

	public int getOffsetVarLocal() {
		return offsetVarLocal;
	}
	
	//public void setOffsetVarLocal(int off) {
	//	offsetVarLocal+=off;
	//}
	
	
	
	public Token getToken() {
		return token;
	}
	
	
	public void agregarParametro(String lexemaActual, Tipo tipo, int parametroUbicacion, Token t) {
		entradaParametros.add(new EntradaPar(lexemaActual,tipo,parametroUbicacion,t));
		entradaVarsParams.put(lexemaActual, new EntradaPar(lexemaActual,tipo,parametroUbicacion,t));
	}
	
	
	public void agregarCuerpo(NodoBloque nb) {
		cuerpo=nb;
	}
	
	
	//semantico II
	public void chequearSentencias() throws Exception{
		if (cuerpo != null) {
			cuerpo.chequear();
		}
	}
	
	public abstract String getEtiqueta();
	
	
	
	
	public void generar() throws Exception {
		//en esta instancia, los offset de los parametros ya fueron seteados por la clase que contiene esta unidad.
		
		//genero el codigo para la unidad
		Analizador_Sintactico.salida.generar(this.getEtiqueta()+": NOP","genero codigo para unidad "+nombre);
		
		Analizador_Sintactico.salida.generar("LOADFP","guardo el enlace dinamico del llamador");
		Analizador_Sintactico.salida.generar("LOADSP","comienza el RA de la unidad llamada");
		Analizador_Sintactico.salida.generar("STOREFP","indico el actual RA");
		
		
		// si el cuerpo del metodo es distinto de null -> genero el codigo del bloque
		if (cuerpo != null) {
			Analizador_Sintactico.TS.setBloqueActual(cuerpo);
			cuerpo.generar();
		}
		
	}//fin generar

}
