package Nodos;


import java.util.List;

import Componentes.Analizador_Sintactico;
import Componentes.EntradaClase;
import Componentes.EntradaMetodo;
import Componentes.EntradaPar;
import Componentes.Token;
import Excepciones.AsignacionNoValidaMetodo;
import Excepciones.ErrorArregloNoIdClase;
import Excepciones.claseNoDeclarada;
import Excepciones.ctorNoDeclarado;
import Excepciones.metodoNoDeclarado;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoIdClase;

public class NodoLlamadaMetodoEncadenado extends NodoEncadenado {
	protected List<NodoExpresion> argsActuales;
	protected EntradaMetodo m;
	//en el token.getNombre tengo el nombre del metodo
	
	public NodoLlamadaMetodoEncadenado(List<NodoExpresion>l, NodoEncadenado ne,Token t,boolean lI) {
		super(ne, t,lI);
		argsActuales=l;
		m=null;
	}
	
	
	public Tipo chequear(Tipo t) throws Exception{
		
		//si t No es de tipoClase -> error
		if ( ! (t instanceof TipoIdClase))
			throw new ErrorArregloNoIdClase(token.getNroLinea(), token.getNroColumna());
			
		
		//pido la clase a la TS
		EntradaClase c= Analizador_Sintactico.TS.esClaseDeclarada(t.getTipo());
		//si la clase no existe -> error
		if (c== null)
			throw new claseNoDeclarada(t.getTipo(), token.getNroLinea(), token.getNroColumna());
		
		//veo si el metodo con esa aridad existe en la clase
		m= c.getEntradaMetodo().get(token.getLexema()+"$"+argsActuales.size());
		//si no existe-> error
		
		
		if (m==null)
			throw new metodoNoDeclarado(c.getNombre(),token.getLexema(), argsActuales.size(), token.getNroLinea(), token.getNroColumna());
					
		//si existe:  ver si tipos de parametros coinciden
		int pos=0;
		for(EntradaPar p: m.getEntradaParametros()) {
			//formal.compatible(actual)
			if (p.getTipo().compatible(argsActuales.get(pos).chequear()))
				pos++;
			else
				throw new ctorNoDeclarado(m.getNombreAux(), token.getNroLinea(),token.getNroColumna());
		}	
	
		//si es ladoIzq y encadenado es vacio -> error
		if (ladoIzq && (nodoEncadenado instanceof NodoEncadenadoVacio) )
			throw new AsignacionNoValidaMetodo(m.getNombreAux(),token.getNroLinea(),token.getNroColumna()+token.getLexema().length()+2);
		
		//sino -> chequeo el tipo de retorno del metodo
		return nodoEncadenado.chequear(m.getTipoRetorno());
	
	}
	
	
	public void generar() throws Exception{
		//si el metodo es dinamico
				if (m.getModificador().equals("dynamic")) {
							//el this ya esta en la pila   --------> gotti
							
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
					Analizador_Sintactico.salida.generar("LOADREF "+m.getOffsetMetodo(),"obtengo la direccion del metodo en la VT"+ m.getNombreAux());
					
					//call al metodo
					Analizador_Sintactico.salida.generar("CALL", "llamo al metodo "+m.getNombreAux());
					
				}
				//si es estatico
				else {
					//aca no hay this, pero lo tengo en la pila -> lo elimino
					Analizador_Sintactico.salida.generar("POP ","elimino el this pues accedo a metodo estatico");
					
					
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
		System.out.println("Llamada Metodo Encadenado");
		tabs(n+1); System.out.println("Lado Izq.: "+ladoIzq); 
		for(NodoExpresion e: argsActuales)
			e.imprimir(n+2);
		nodoEncadenado.imprimir(n+1);
	}
	
}
