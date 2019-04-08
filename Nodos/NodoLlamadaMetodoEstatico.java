package Nodos;


import java.util.List;

import Componentes.Analizador_Sintactico;
import Componentes.EntradaClase;
import Componentes.EntradaMetodo;
import Componentes.EntradaPar;
import Componentes.Token;
import Excepciones.InvocacionInvalida;
import Excepciones.claseNoDeclarada;
import Excepciones.ctorNoDeclarado;
import Excepciones.metodoInexistente;
import Tipos_Datos.Tipo;

public class NodoLlamadaMetodoEstatico extends NodoPrimario {
	protected List<NodoExpresion> argsActuales;
	protected Token idMetVar;
	protected NodoLlamadaMetodo llamadaMet;
	protected EntradaMetodo m;
	
	public NodoLlamadaMetodoEstatico( NodoEncadenado encadMetEstatico, Token t, NodoLlamadaMetodo n) {
		super(encadMetEstatico,t);
		llamadaMet= n;
		idMetVar= n.getToken();
		argsActuales= n.getArgsActuales();
	}


	public Tipo chequear() throws Exception {
		
			//existe clase?
			EntradaClase c= Analizador_Sintactico.TS.getTabla().get(token.getLexema());
			//si no existe clase -> error
			if (c==null)
				throw new claseNoDeclarada(token.getLexema(),token.getNroLinea(),token.getNroColumna());
			
			
			//existe metodo ? ver en la TS si clase actual tiene ese metodo
			m= c.getEntradaMetodo().get(idMetVar.getLexema()+"$"+argsActuales.size());
			//si no existe metodo -> error
			if (m == null)
				throw new metodoInexistente(idMetVar.getLexema(), idMetVar.getNroLinea(), idMetVar.getNroColumna());
			
			
			//solo puedo llamar a met estaticos -> sino error
			if (! m.getModificador().equals("static"))
				throw new InvocacionInvalida(idMetVar.getLexema(),idMetVar.getNroLinea(), idMetVar.getNroColumna());
			
			
			//cheq parametros
			//si existe:  ver si tipos de parametros coinciden
					int pos=0;
					for(EntradaPar p: m.getEntradaParametros()) {
						//formal.compatible(actual)
						if (p.getTipo().compatible(argsActuales.get(pos).chequear()))
							pos++;
						else
							throw new ctorNoDeclarado(m.getNombreAux(), idMetVar.getNroLinea(),idMetVar.getNroColumna());
					}
	
			//si hay encadenado, lo chequeo y devuelvo
			return nodoEncadenado.chequear(m.getTipoRetorno());
	}
	
	
	public void generar() throws Exception {
		//no tengo cargado el load 3
		
		//si retorno_metodo no es void -> reservo espacio para el valor de retorno
		if ( ! (m.getTipoRetorno().getTipo().equals("void")))
			Analizador_Sintactico.salida.generar("RMEM 1","reservo memoria para el retorno del metodo");
		
		//para cada parametro
		for(NodoExpresion n: argsActuales) {
			n.generar();
		}
		
		//agrego etiqueta metodo
		Analizador_Sintactico.salida.generar("PUSH "+m.getEtiqueta(),"apilo etiqueta metodo ");
		
		//call al metodo
		Analizador_Sintactico.salida.generar("CALL", "llamo al metodo "+m.getNombreAux());
		
		
		//generar encadenado
		nodoEncadenado.generar();
	}
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Llamada Metodo etatico");
		System.out.println("   Tipo ("+token.getLexema()+")");
		System.out.println("   Lado Izq ("+llamadaMet.esLadoIzq()+")");
		for (NodoExpresion e: argsActuales)
			e.imprimir(n+2);
		nodoEncadenado.imprimir(n+1);
	}

}
