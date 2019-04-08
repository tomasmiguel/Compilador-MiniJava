package Nodos;
import Componentes.Token;
import Tipos_Datos.Tipo;

public class NodoEncadenadoVacio extends NodoEncadenado {

	public NodoEncadenadoVacio(NodoEncadenado n, Token tok, boolean ladoIzq) {
		super(n, tok,ladoIzq);
	}

	
	public Tipo chequear(Tipo t) {
		return t;
	}


	
	
	public void generar() {
		//no hago nada
	}
	
	
	public void imprimir(int n) {
		//nada
	}
}
