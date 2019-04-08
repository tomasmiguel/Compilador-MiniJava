package Nodos;


import java.util.List;

import Componentes.Analizador_Sintactico;
import Componentes.EntradaClase;
import Componentes.EntradaPar;
import Componentes.Token;
import Componentes.Unidad;
import Excepciones.claseNoDeclarada;
import Excepciones.ctorNoDeclarado;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoIdClase;

public class NodoLlamadaCtorClase extends NodoLlamadaCtor {
	private List<NodoExpresion> argsActuales;
	private EntradaClase clase;
	private Unidad ctor;
	
	
	public NodoLlamadaCtorClase(List<NodoExpresion>l, NodoEncadenado ne, Token t) {
		super(ne,t);
		argsActuales=l;
		clase=null;
		ctor=null;
	}
	
	
	
	public Tipo chequear() throws Exception{
		//token.getNombre tiene el nombre de la clase
		//si la clase no existe -> error
		EntradaClase cl= Analizador_Sintactico.TS.esClaseDeclarada(token.getLexema());
		
		if (cl==null)
			throw new claseNoDeclarada(token.getLexema(),token.getNroLinea(),token.getNroColumna());
		
		
		//obtengo var de inst para el generar
		clase= cl;
		
		
		//ctor existe con esa cant de args
		ctor= cl.getEntradaCtor().get(argsActuales.size()+"");
		
		if (ctor==null)
			throw new ctorNoDeclarado(token.getLexema(),token.getNroLinea(),token.getNroColumna());
		
		//sino ver si tipos coinciden
		int pos=0;
		for(EntradaPar p: ctor.getEntradaParametros()) {
			if (p.getTipo().compatible(argsActuales.get(pos).chequear()))
				pos++;
			else
				throw new ctorNoDeclarado(ctor.getNombre(), token.getNroLinea(),token.getNroColumna());
		}
		
		return nodoEncadenado.chequear(new TipoIdClase(cl.getNombre()));
		
	}
	
	
	public void generar() throws Exception {
		//creo el objeto
		Analizador_Sintactico.salida.generar("RMEM 1","espacio de referencia a nuevo objeto clase");
		
		//reservo espacio para cada var de inst	 + 1 de la VT
		Analizador_Sintactico.salida.generar("PUSH "+(clase.getEntradaVar().values().size()+1), "reservo lugar en el CIR para cada variable de inst");
		
		//
		Analizador_Sintactico.salida.generar("PUSH simple_malloc", "hago malloc");
		Analizador_Sintactico.salida.generar("CALL","voy al codigo del Ctor de la clase");
		Analizador_Sintactico.salida.generar("DUP","duplico el this que me dio malloc para no perderlo");
		
		Analizador_Sintactico.salida.generar("PUSH VT_"+clase.getNombre(), "apilo el nombre de la VT");
		Analizador_Sintactico.salida.generar("STOREREF 0","guardo referencia a VT");
		Analizador_Sintactico.salida.generar("DUP","duplico el this que me dio malloc para no perderlo");
		for (NodoExpresion e: argsActuales) {
			e.generar();
			Analizador_Sintactico.salida.generar("SWAP","para no perder el this");
		}
		
		Analizador_Sintactico.salida.generar("PUSH "+ctor.getEtiqueta()," apilo en el tope el nombre del ctor");
		Analizador_Sintactico.salida.generar("CALL", "voy al codigo del Ctor");
		
	
		//para poder hacer   new Casa().hola    ,  hola es var/ metodo/ lo que de
		nodoEncadenado.generar();
	}
	
	
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Llamada Ctor Clase");
		System.out.println("   Tipo: "+token.getLexema()+"");
		for (NodoExpresion e: argsActuales)
			e.imprimir(n+2);
		
		nodoEncadenado.imprimir(n+1);
	}
	
}
