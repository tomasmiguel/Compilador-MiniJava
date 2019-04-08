package Nodos;


import java.util.List;

import Componentes.Analizador_Sintactico;
import Componentes.EntradaMetodo;
import Componentes.EntradaPar;
import Componentes.Token;
import Excepciones.InvocacionInvalida;
import Excepciones.ctorNoDeclarado;
import Excepciones.metodoInexistente;
import Tipos_Datos.Tipo;

public class NodoLlamadaMetodo extends NodoPrimario {
	protected List<NodoExpresion> argsActuales;
	protected boolean ladoIzq;
	protected EntradaMetodo m;
	
	public NodoLlamadaMetodo(List<NodoExpresion>l, NodoEncadenado n, Token t, boolean lI) {
		super(n,t);
		argsActuales=l;
		ladoIzq=lI;
		m=null;
	}
	
	public List<NodoExpresion> getArgsActuales(){
		return argsActuales;
	}
	
	public NodoEncadenado getEncadenado() {
		return nodoEncadenado;
	}
	
	public Token getToken() {
		return token;
	}

	public boolean esLadoIzq() {
		return ladoIzq;
	}
	
	

	public Tipo chequear() throws Exception {
	
		//existe metodo ? ver en la TS si clase actual tiene ese metodo
		m= Analizador_Sintactico.TS.getClaseActual().getEntradaMetodo().get(token.getLexema()+"$"+argsActuales.size());
		
		if (m == null)
			throw new metodoInexistente(token.getLexema(), token.getNroLinea(), token.getNroColumna());
		
		//si el met actual de mi TS es estatico..
		if (Analizador_Sintactico.TS.getUnidadActual().getModificador().equals("static"))	
			//solo puedo llamar a met estaticos -> sino error
			if (! m.getModificador().equals("static"))
				throw new InvocacionInvalida(token.getLexema(),token.getNroLinea(), token.getNroColumna());
			
		
		//cheq parametros
		//si existe:  ver si tipos de parametros coinciden
				int pos=0;
				for(EntradaPar p: m.getEntradaParametros()) {
					//formal.compatible(actual)
					if (p.getTipo().compatible(argsActuales.get(pos).chequear()))
						pos++;
					else
						throw new ctorNoDeclarado(m.getNombreAux(), token.getNroLinea(),token.getNroColumna());
				}
		//si encadenado es vacio -> retorno el tipo del metodo
		if (nodoEncadenado instanceof NodoEncadenadoVacio && ladoIzq)
			return m.getTipoRetorno();
	   
		//si hay encadenado, lo chequeo y devuelvo
		return nodoEncadenado.chequear(m.getTipoRetorno());
	}
	
	//ejemplo
	/*class A
		public int x
		static void ms() {
			int v;
			v= md(); // -> error -> equiv a v= this.md()
			v= x+1 //-> error -> equiv a this.x
			x= v+1 // mal
			x= md() //mal
			
			//this dentro de met estatico -> error
			
		}
		dynamic	int md() {
			
			
		}
	
	}*/
	
	
	public void generar() throws Exception{
		//si el metodo es dinamico
		if (m.getModificador().equals("dynamic")) {
					Analizador_Sintactico.salida.generar("LOAD 3", "apilo la referencia al objeto");	
					
					//si retorno_metodo no es void -> reservo espacio para el valor de retorno
					if ( ! (m.getTipoRetorno().getTipo().equals("void"))) {
						Analizador_Sintactico.salida.generar("RMEM 1","reservo memoria para el retorno del metodo");
						//swap para conservar el this
						Analizador_Sintactico.salida.generar("SWAP ","mantengo la referencia al this en el tope");
					}

					//para cada parametro
					for(NodoExpresion n: argsActuales) {
						n.generar();
						//swap para conservar el this
						Analizador_Sintactico.salida.generar("SWAP ","mantengo la referencia al this en el tope");
					}
					
					//duplico this para acceder a la VT
					Analizador_Sintactico.salida.generar("DUP","duplico la referencia al this para acceder a la VT");
					//accedo a la VT
					Analizador_Sintactico.salida.generar("LOADREF 0","accedo a la VT de mi objeto");
					
					//cargo el offset del metodo
					Analizador_Sintactico.salida.generar("LOADREF "+m.getOffsetMetodo(),"obtengo la direccion del metodo en la VT");
					
					//call al metodo
					Analizador_Sintactico.salida.generar("CALL", "llamo al metodo "+m.getNombreAux());
			
		}
		//si es estatico
		else {
			//aca no hay this
			
			//si retorno_metodo no es void -> reservo espacio para el valor de retorno
			if ( ! (m.getTipoRetorno().getTipo().equals("void"))) 
				Analizador_Sintactico.salida.generar("RMEM 1","reservo memoria para el retorno del metodo");
			
			//para cada parametro
			for(NodoExpresion n: argsActuales) 
				n.generar();
			
			
			//cargo la etiqueta del metodo
			Analizador_Sintactico.salida.generar("PUSH "+m.getEtiqueta(),"obtengo la direccion del metodo en la VT");
			
			//call al metodo
			Analizador_Sintactico.salida.generar("CALL", "llamo al metodo "+m.getNombreAux());
		}
		
		//genero encadenado    A.m1() ||  obj.m1()
		nodoEncadenado.generar();
		
	}
	
	
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Llamada Metodo");
		for (NodoExpresion e: argsActuales)
			e.imprimir(n+2);
		
		nodoEncadenado.imprimir(n+1);
	}
	
	
}
