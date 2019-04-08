package Nodos;
import Componentes.Token;
import Tipos_Datos.Tipo;

public class NodoExpresionParentizada extends NodoPrimario {
	protected NodoExpresion nodoExpresion;
	
	public NodoExpresionParentizada(NodoExpresion ne, NodoEncadenado nen, Token tok) {
		super(nen,tok);
		nodoExpresion= ne;
	}

	
	public Tipo chequear() throws Exception {
		Tipo tExp= nodoExpresion.chequear();
		
		//nodoEncadeandoVacio no hace nada en chequear
		return nodoEncadenado.chequear(tExp);
	}
	
	public void generar() throws Exception{
		nodoExpresion.generar();
		nodoEncadenado.generar();
	}
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Expresion parentizada");
		nodoExpresion.imprimir(n+1);
		nodoEncadenado.imprimir(n+1);
	}

}
