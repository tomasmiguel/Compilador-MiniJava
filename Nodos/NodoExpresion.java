package Nodos;
import Componentes.Token;
import Tipos_Datos.Tipo;

public abstract class NodoExpresion {
	protected Token token; //u operador
	
	public NodoExpresion(Token tok) {
		token= tok;
	}
	
	
	public abstract Tipo chequear()throws Exception;
	
	public abstract void generar() throws Exception;
	
	
	public Token getToken() {
		return token;
	}
	
	
	public void tabs(int n){
		for(int i = 0; i<n; i++)
			System.out.print("   ");
	}
	
	public void imprimir(int n){}
}
