package Nodos;
import Componentes.EntradaVarInst;
import Componentes.Token;

public class NodoAsignacionDec extends NodoSentencia {
	protected NodoDecVarLocal nodoDecVar; //lista de declaraciones de variables
	protected NodoExpresion n;
	protected Token tokenIgual;
	
	
	public NodoAsignacionDec(NodoDecVarLocal nd, NodoExpresion n,Token tokenIgual) {
		super(n.getToken());
		nodoDecVar= nd;
		this.n= n;
		this.tokenIgual= tokenIgual;
	}
	
	
	public void chequear() throws Exception {
		//chequeo nodo declaracion de variable local
		nodoDecVar.chequear();
		
		for (EntradaVarInst v: nodoDecVar.getListaVars()) {
			NodoAsignacion asig= new NodoAsignacion(new NodoAccesoVar(new NodoEncadenadoVacio(null,null,false),v.getToken(),true),n,tokenIgual);
			asig.chequear();
		}
	}

	
	public void generar() throws Exception {
		nodoDecVar.generar();
		n.generar();
	}
	
	
	
	
	
	

	@Override
	public void imprimir(int n) {
		for (EntradaVarInst v: nodoDecVar.getListaVars()) {
			v.imprimirAST();
		}
	}

}
