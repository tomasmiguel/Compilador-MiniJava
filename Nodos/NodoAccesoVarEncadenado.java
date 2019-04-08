package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.EntradaClase;
import Componentes.EntradaVarInst;
import Componentes.Token;
import Excepciones.AccesoVarInexistente;
import Excepciones.AccesoVarPrivada;
import Excepciones.ErrorTipoIdClase;
import Excepciones.claseNoDeclarada;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoIdClase;

public class NodoAccesoVarEncadenado extends NodoEncadenado {
	protected EntradaVarInst variable;
	//token.getLexema tiene el nombre de la variable
	
	
	public NodoAccesoVarEncadenado(NodoEncadenado ne, Token t, boolean lI) {
		super(ne,t, lI);
	}


	public Tipo chequear(Tipo t) throws Exception{
		//si t no es de tipo IdClase -> error
		if ( ! (t instanceof TipoIdClase) )
			throw new ErrorTipoIdClase(token.getLexema(), token.getNroLinea(),token.getNroColumna());
		//sino
		//veo si existe la clase
		EntradaClase c= Analizador_Sintactico.TS.getTabla().get(t.getTipo());
		
		variable=null;
		//si existe
		if (c !=null) {
			//veo si existe la variable en la clase
			variable = c.getEntradaVar().get(token.getLexema());
			//si existe
			if (variable != null) {
				//si la variable es publica -> chequeo encadenado
				if (variable.getVisibilidad().equals("public"))
					return nodoEncadenado.chequear(variable.getTipo());
				//si la variable es privada -> error
				throw new AccesoVarPrivada(c.getNombre(),variable.getNombre(),token.getNroLinea(),token.getNroColumna());
				
			}
			//si no existe la variable -> error
			throw new AccesoVarInexistente(c.getNombre(),token.getLexema(),token.getNroLinea(),token.getNroColumna());
		}
		//si no existe la clase -> error
		throw new claseNoDeclarada(token.getLexema(),token.getNroLinea(), token.getNroColumna());
	 
	}
	
	
	public void generar() throws Exception {
		//si o si soy var de Inst
		
		//load 3 ya esta cargado en la pila -----> preguntar Gotti.
		
		//si estoy del lado izq
		if (ladoIzq) {
			//load 3 ya esta en tope
			Analizador_Sintactico.salida.generar("SWAP", "swap para no perder el this");
			Analizador_Sintactico.salida.generar("STOREREF "+variable.getOffset(), "guardo el tope de la pila en la variable de instancia");
		}
		//sino, del lado derecho
		else {
			Analizador_Sintactico.salida.generar("LOADREF "+variable.getOffset(), "apilo valor de at de instancia en tope de pila");
		}
	
		//genero encadenado
		nodoEncadenado.generar();
	}
	
	
	//imprimir
		public void imprimir(int n){
			tabs(n); 
			System.out.println("Acceso Var Encadenado");
			tabs(n+1); System.out.println("Var: "+token.getLexema()); 
			tabs(n+1); System.out.println("Lado Izq.: "+ladoIzq); 
			nodoEncadenado.imprimir(n+1);
		}
	
	
}
