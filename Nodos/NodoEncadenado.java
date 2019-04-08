package Nodos;
import Componentes.Token;
import Tipos_Datos.Tipo;

public abstract class NodoEncadenado {
	protected NodoEncadenado nodoEncadenado;
	protected Token token;
	protected boolean ladoIzq;
	
	protected NodoEncadenado(NodoEncadenado ne, Token t, boolean lI) {
		nodoEncadenado= ne;
		token= t;
		ladoIzq=lI;
	}
	
	public abstract Tipo chequear(Tipo t) throws Exception;
		
	//imprimir
	public abstract void imprimir(int n);
	
	public abstract void generar()throws Exception;
	
	
	
	

	public void tabs(int n){
		for(int i = 0; i<n; i++)
			System.out.print("   ");
	}
	
	

	public Token getToken() {
		return token;
	}
}
