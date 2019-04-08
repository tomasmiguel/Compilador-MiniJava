package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Token;
import Excepciones.ErrorArregloEncadenado;
import Excepciones.ErrorTipoExpresion;
import Tipos_Datos.Arreglo;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoArregloBoolean;
import Tipos_Datos.TipoArregloChar;
import Tipos_Datos.TipoArregloInt;
import Tipos_Datos.TipoBoolean;
import Tipos_Datos.TipoChar;
import Tipos_Datos.TipoInt;

public class NodoAccesoArregloEncadenado extends NodoEncadenado {
	protected NodoExpresion expresion;
	
	public NodoAccesoArregloEncadenado(NodoExpresion ne, NodoEncadenado nen,Token t, boolean lI) {
		super(nen, t,lI);
		expresion=ne;
	}

	
	public Tipo chequear(Tipo t) throws Exception {
		if (! (t instanceof Arreglo) )
			throw new ErrorArregloEncadenado(token.getNroLinea(),token.getNroColumna());
		
		//chequeo expresion
		Tipo tipoI= expresion.chequear();
		//expresion no es entero -> error
		if ( ! (tipoI instanceof TipoInt))	
			throw new ErrorTipoExpresion(token.getNroLinea(),token.getNroColumna());
		
		//si encadenado NO es vacio -> error
		if ( ! (nodoEncadenado instanceof NodoEncadenadoVacio))
			//no se permite arreglo de todo
			throw new ErrorArregloEncadenado(token.getNroLinea(), token.getNroColumna());
		
		
	
		//retorno  
		if  (t instanceof TipoArregloInt) return nodoEncadenado.chequear(new TipoInt(""));
		if  (t instanceof TipoArregloChar) return nodoEncadenado.chequear(new TipoChar(""));
		if  (t instanceof TipoArregloBoolean) return nodoEncadenado.chequear(new TipoBoolean(""));
		 
		return null;
		
	}
	
	
	public void generar()throws Exception {
		//si lado izquierdo
		if (ladoIzq) {
			//generar expresion
			expresion.generar();//devuelve el index del arreglo 
			//en tope-1 tengo la posicion base del arreglo
			Analizador_Sintactico.salida.generar("ADD", "accedo elemento i-esimo del arreglo");
			
			//??? no duplico antes ??
			Analizador_Sintactico.salida.generar("SWAP","swap para no perder el this");
			
			//guardo el valor en la posicion del arreglo
			Analizador_Sintactico.salida.generar("STOREREF 0","guardo el valor en el heap");
		}
		//si lado derecho
		else {
			//generar expresion
			expresion.generar();//devuelve el index del arreglo 
			//en tope-1 tengo la posicion base del arreglo
			Analizador_Sintactico.salida.generar("ADD", "accedo elemento i-esimo del arreglo");
			
			//accedo al valor del arreglo en esa pos
			Analizador_Sintactico.salida.generar("LOADREF 0","accedo al valor del arreglo");	
		}
		
		//genear encadenado
		nodoEncadenado.generar();
	}
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Acceso Arreglo Encadenado");
		tabs(n+1); System.out.println("Lado Izq.: "+ladoIzq); 
		expresion.imprimir(n+1);
		nodoEncadenado.imprimir(n+1);
	}
}
