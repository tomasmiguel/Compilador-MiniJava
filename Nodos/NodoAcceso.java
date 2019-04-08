package Nodos;
import Componentes.Token;

public abstract class NodoAcceso extends NodoPrimario{
	protected boolean ladoIzq;
	
	protected NodoAcceso(NodoEncadenado ne,Token tok, Boolean lI) {
		super(ne, tok);
		ladoIzq=lI;
	}
}
