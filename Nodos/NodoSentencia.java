package Nodos;
import Componentes.Token;

public abstract class NodoSentencia {
	protected Token token;
	
	
	protected NodoSentencia(Token tok) {
		token=tok;
	}
	
	public abstract void chequear()throws Exception;
	public abstract void generar() throws Exception;
	
	
	public Token getToken() {
		return token;
	}
	
	public abstract void imprimir(int n);
	
	public void tabs(int n){
		for(int i = 0; i<n; i++)
			System.out.print("   ");
	}
}
