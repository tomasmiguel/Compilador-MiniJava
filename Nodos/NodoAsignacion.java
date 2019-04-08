package Nodos;
import Componentes.Token;
import Excepciones.ErrorTiposAsignacion;
import Tipos_Datos.Tipo;

public class NodoAsignacion extends NodoSentencia{
	protected NodoAcceso ladoIzq;
	protected NodoExpresion ladoDer;
	
	
	
	public NodoAsignacion(NodoAcceso na, NodoExpresion ld, Token tok) {
		super(tok);
		ladoIzq=na;
		ladoDer= ld;
		
	}

	
	public void chequear() throws Exception {
		Tipo tipoD= ladoDer.chequear();
		Tipo tipoI= ladoIzq.chequear();
		
		if (!tipoI.compatible(tipoD))
			throw new ErrorTiposAsignacion(tipoI.getTipo(), tipoD.getTipo(), token.getNroLinea(),token.getNroColumna());
	}
	
	
	public void generar() throws Exception {
		ladoDer.generar();
		ladoIzq.generar();
	}
	
	
	
	
	
	
	
	
	//imprimir
		public void imprimir(int n){
			tabs(n); 
			System.out.println("Asignacion");
			ladoIzq.imprimir(n+1);
			ladoDer.imprimir(n+1);
		}
}
