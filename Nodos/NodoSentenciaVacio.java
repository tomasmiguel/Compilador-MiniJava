package Nodos;
import Componentes.Token;

public class NodoSentenciaVacio extends NodoSentencia {

	public NodoSentenciaVacio(Token t) {
		super(t);
	}
	
	
	public void chequear() throws Exception{
		//no hago nada
	}
	
	public void generar() throws Exception{
		//de nada..
	}
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Nodo sentencia vacio");
	}
}
