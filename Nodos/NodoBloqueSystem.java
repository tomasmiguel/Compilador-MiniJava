package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Token;

public class NodoBloqueSystem extends NodoBloque{
	
	public NodoBloqueSystem( NodoBloque padre, Token tok) {
		super(padre,tok);
	}
	
	public void generar() {
		String nombreMet= Analizador_Sintactico.TS.getUnidadActual().getNombre();
		
		
		
		switch(nombreMet) {
			case("read$0"):{
				//pido nro por pantalla
				Analizador_Sintactico.salida.generar("READ","leo el valor por pantalla");
				//guardo ese valor leido
				Analizador_Sintactico.salida.generar("STORE 3","guardo el valor leido en tope de pila");
				break;
			}
			case("printB$1"):{
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("BPRINT","imprimo valor booleano");
				break;
			}
			case("printC$1"):{
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("CPRINT","imprimo valor char");
				break;
			}
			case("printI$1"):{
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("IPRINT","imprimo valor entero");
				break;
			}
			case("printS$1"):{
				//
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("SPRINT","imprimo el String");
				break;
			}
			case("println$0"):{
				Analizador_Sintactico.salida.generar("PRNLN","salto de linea");
				break;
			}
			case("printBln$1"):{
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("BPRINT","imprimo valor booleano");
				Analizador_Sintactico.salida.generar("PRNLN","salto de linea");
				break;
			}
			case("printCln$1"):{
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("CPRINT","imprimo valor char");
				Analizador_Sintactico.salida.generar("PRNLN","salto de linea");
				break;
			}
			case("printIln$1"):{
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("IPRINT","imprimo valor entero");
				Analizador_Sintactico.salida.generar("PRNLN","salto de linea");
				break;
			}
			case("printSln$1"):{
				Analizador_Sintactico.salida.generar("LOAD 3","apilo referencia al objeto CIR en tope de pila");
				Analizador_Sintactico.salida.generar("SPRINT","imprimo el String");
				Analizador_Sintactico.salida.generar("PRNLN","salto de linea");
				break;
			}
		}//fin switch
		
		
	}//fin generar()

}
