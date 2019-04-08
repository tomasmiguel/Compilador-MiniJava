package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Token;
import Excepciones.AsignacionNoValidaThis;
import Excepciones.AsignacionThisSolo;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoIdClase;

public class NodoAccesoThis extends NodoAcceso {
	
	public NodoAccesoThis(NodoEncadenado ne, Token tok, boolean lI) {
		super(ne,tok,lI);
	}

	
	public Tipo chequear() throws Exception {
		//chequear inline de at de inst
		//hacer
		//
		//

		
		//si es ladoIzq y encadenado es vacio -> error
		if (ladoIzq && (nodoEncadenado instanceof NodoEncadenadoVacio) )
			throw new AsignacionThisSolo(Analizador_Sintactico.TS.getUnidadActual().getNombreAux(),token.getNroLinea(),token.getNroColumna());
		//sino
		
		//si es un metodo estatico-> error
		if (Analizador_Sintactico.TS.getUnidadActual().getModificador().equals("static"))
			throw new AsignacionNoValidaThis(Analizador_Sintactico.TS.getUnidadActual().getNombreAux(),token.getNroLinea(),token.getNroColumna());
		
		//sino -> chequeo el tipo de la clase actual
		return nodoEncadenado.chequear(new TipoIdClase(Analizador_Sintactico.TS.getClaseActual().getNombre()));
	}

	
	public void generar() throws Exception {
		Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
		nodoEncadenado.generar();
	}
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Acceso This");
		tabs(n+1); System.out.println("Nombre: "+token.getLexema()+"");
		tabs(n+1); System.out.println("Lado Izquierdo: "+ladoIzq+"");
		nodoEncadenado.imprimir(n+1);
	}
}
