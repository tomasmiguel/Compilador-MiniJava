package Nodos;
import Componentes.Token;
import Tipos_Datos.Tipo;

public abstract class NodoPrimario extends NodoOperando {
	protected NodoEncadenado nodoEncadenado;
	
	
	protected NodoPrimario(NodoEncadenado ne, Token tok) {
		super(tok);
		nodoEncadenado= ne;
	}
	
	public abstract Tipo chequear()throws Exception;
}
