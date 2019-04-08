package Nodos;
import Componentes.Token;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoVoid;

public class NodoExpresionVacio extends NodoExpresion {

	public NodoExpresionVacio(Token tok) {
		super(tok);
	}


	
	public Tipo chequear() throws Exception {
		return new TipoVoid("");
	}
	
	public void generar() throws Exception{
		//no hago nada
	}

	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Expresion Vacia");
	}
}
