package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.EntradaClase;
import Componentes.EntradaVarInst;
import Componentes.EntradaVariable;
import Componentes.Token;
import Excepciones.AccesoVarInexistente;
import Excepciones.AccesoVarPrivada;
import Excepciones.AsignacionNoValidaThisInstancia;
import Tipos_Datos.Tipo;

public class NodoAccesoVar extends NodoAcceso {
	protected EntradaVariable variable;
	protected boolean soyDeInstancia;
	
	public NodoAccesoVar(NodoEncadenado ne, Token tok, boolean lI) {
		super(ne,tok,lI);
		soyDeInstancia=false;
	}
	
	public Tipo chequear() throws Exception {
		variable=null;
		EntradaVarInst vI=null;
		EntradaClase clase= Analizador_Sintactico.TS.getClaseActual();
		
		//busco si es una var local o parametro
		variable= Analizador_Sintactico.TS.getUnidadActual().esVarMetodoDeclarado(token.getLexema());
		
		//si no es -> busco si es at de instancia
		if (variable == null) {
			//si tampoco es at de instancia -> error
			vI= clase.esVarDeclarada(token.getLexema());
			
			if (vI == null )//|| vI.getVisibilidad().equals("private")) //y que vI sea heredada...
				throw new AccesoVarInexistente(Analizador_Sintactico.TS.getClaseActual().getNombre(),nodoEncadenado.getToken().getLexema(), token.getNroLinea(), token.getNroColumna());
			//si es at de instancia
			//veo si es privado y al menos mi padre lo tiene -> no lo puedo usar
			if (vI.getVisibilidad().equals("private") && (Analizador_Sintactico.TS.getTabla().get(clase.getHerencia()).getEntradaVar().get(vI.getNombre()) != null))
				throw new AccesoVarPrivada(clase.getNombre(),vI.getNombre(),token.getNroLinea(),token.getNroColumna());
			
			
			//si es metodo estatico -> error
			if (Analizador_Sintactico.TS.getUnidadActual().getModificador().equals("static"))
				throw new AsignacionNoValidaThisInstancia(Analizador_Sintactico.TS.getUnidadActual().getNombre(), token.getNroLinea(),token.getNroColumna());
			
			
			//sino (at de instancia y metodo dinamico) -> chequeo encadenado con tipo de vI
			//antes guardo mi variable para el generar()
			variable= vI;
			soyDeInstancia=true;
			return nodoEncadenado.chequear(vI.getTipo());
			
		}
		//sino (var local/parametro) -> chequeo encadenado con tipo de v
		return nodoEncadenado.chequear(variable.getTipo());
	}
	
	
	public void generar() throws Exception {
		//si estoy en ladoIzq y encadenado es vacio
			//si soy var de Instancia
			if (soyDeInstancia) {
				Analizador_Sintactico.salida.generar("LOAD 3", "apilo referencia al objeto CIR en tope de pila");
				
				if ( !ladoIzq || ( ! (nodoEncadenado instanceof NodoEncadenadoVacio))) {
					
					Analizador_Sintactico.salida.generar("LOADREF "+variable.getOffset(), "apilo valor de at de instancia en tope de pila");

				}
				//si soy lado derecho o tengo encadenado
				else {

					Analizador_Sintactico.salida.generar("SWAP ", "intercambio tope con tope-1 para hacer storeRef");
					Analizador_Sintactico.salida.generar("STOREREF "+variable.getOffset(), "guardo valor del tope en variable de instancia");
				}
				
			}	
			//si estoy en lado derecho o hay encadenado
			else {
				if (!ladoIzq || ( ! (nodoEncadenado instanceof NodoEncadenadoVacio))) {
					Analizador_Sintactico.salida.generar("LOAD "+variable.getOffset(), "apilo valor de at de instancia en tope de pila");

					
				}
				//si soy lado derecho o tengo encadenado
				else {
					//Analizador_Sintactico.salida.generar("SWAP", "intercambio tope con tope-1 para hacer storeRef");
					Analizador_Sintactico.salida.generar("STORE "+variable.getOffset(), "guardo valor del tope en variable de instancia");
				}
				
			}
	
			//generar encadenado
			nodoEncadenado.generar();
	}//fin generar
	
	
	
	
	
	
	//imprimir
		public void imprimir(int n){
			tabs(n); 
			System.out.println("Acceso Var");
			tabs(n+1); System.out.println("Nombre: "+token.getLexema()+"");
			tabs(n+1); System.out.println("Lado Izquierdo: "+ladoIzq+"");
			nodoEncadenado.imprimir(n+1);
		}

	
}
