package Nodos;
import Componentes.Token;

public abstract class NodoIf extends NodoSentencia {
	protected NodoExpresion condicion;
	protected NodoSentencia sentenciaThen;
	
	
	protected NodoIf(NodoExpresion c, NodoSentencia s, Token tok) {
		super(tok);
		condicion=c;
		sentenciaThen=s;
	}
	
	
	
	public abstract void generar()throws Exception; 
}
