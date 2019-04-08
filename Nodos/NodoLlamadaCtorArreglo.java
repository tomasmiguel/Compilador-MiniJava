package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Token;
import Excepciones.ErrorArregloExpresion;
import Tipos_Datos.Arreglo;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoArregloBoolean;
import Tipos_Datos.TipoArregloChar;
import Tipos_Datos.TipoArregloInt;
import Tipos_Datos.TipoBoolean;
import Tipos_Datos.TipoChar;
import Tipos_Datos.TipoInt;

public class NodoLlamadaCtorArreglo extends NodoLlamadaCtor {
	protected NodoExpresion nodoExpresion;
	
	public NodoLlamadaCtorArreglo(NodoExpresion n, NodoEncadenado ne, Token t) {
		super(ne,t);
		nodoExpresion=n;
	}

	
	public Tipo chequear() throws Exception {
		//System.out.println("habeis entrado en NodoLlamCtorArreglo cuando tenia que entrar en llamadaMetodo");
		
		Tipo tExp= nodoExpresion.chequear();
		
		if ( ! tExp.compatible(new TipoInt("")))
			throw new ErrorArregloExpresion(token.getNroLinea(), token.getNroColumna());
		
		Tipo t=null;
		Arreglo tArr=null;
		
		if (token.getLexema().equals("int")) {
			tArr= new TipoArregloInt("");
			t= new TipoInt("");
		}
		else if (token.getLexema().equals("char")) {
			tArr= new TipoArregloChar("");
			t= new TipoChar("");
		}
		else if (token.getLexema().equals("boolean")) {
			tArr= new TipoArregloBoolean("");
			t= new TipoBoolean("");
		}
		
		
		//si encadenado vacio -> devuelvo el tipo del arreglo
		if (nodoEncadenado instanceof NodoEncadenadoVacio) {
			return tArr;
		}
		//sino, chequeo encadenado con tipo del lado izq
		
		return nodoEncadenado.chequear(t); 		
	//EJemplo
	//int[] a = new int[5];
	//int a = new int[9][8];
	}
		
	
	public void generar()throws Exception{
		//creo el objeto
		Analizador_Sintactico.salida.generar("RMEM 1","espacio de referencia a nuevo objeto arreglo");
				
		//genero la expresion
		nodoExpresion.generar(); //obtengo el tamanio del arreglo
		
		//
		Analizador_Sintactico.salida.generar("PUSH simple_malloc", "hago malloc");
		Analizador_Sintactico.salida.generar("CALL","voy al codigo del Ctor del arreglo");
		
		//generar encadenado
		nodoEncadenado.generar();
	}
	
	
	
	
	
	
	//imprimir
		public void imprimir(int n){
			tabs(n); 
			System.out.println("Llamada Ctor arreglo");
			tabs(n+1); System.out.println("   Tipo: "+token.getLexema()+"");
			nodoExpresion.imprimir(n+1);
			nodoEncadenado.imprimir(n+1);
		}
	
}
