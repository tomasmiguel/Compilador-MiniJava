package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Token;
import Excepciones.ErrorDeTiposExpUn;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoBoolean;
import Tipos_Datos.TipoInt;

public class NodoExpresionUnaria extends NodoExpresion {
	protected NodoExpresion ladoD;//lado derecho
	
	public NodoExpresionUnaria(NodoExpresion ld, Token tok) {
		super(tok);
		ladoD= ld;
	}
	
	
	public Tipo chequear() throws Exception{
		Tipo tipoD= ladoD.chequear();
			
		String operador= token.getLexema();
		
		if (operador.equals("+")|| operador.equals("-")){
			if ( tipoD.getTipo().equals("int"))
				return new TipoInt("");
			else
				throw new ErrorDeTiposExpUn(operador,token.getNroLinea(), token.getNroColumna());	
		}
		else if (operador.equals("!")) {
			if (tipoD.getTipo().equals("boolean")) {
				return new TipoBoolean("");
			}
		}
		//sino, cualquier otra cosa -> error de algun tipo
			throw new ErrorDeTiposExpUn(operador,token.getNroLinea(), token.getNroColumna());
	}
	
	
	
	public void generar()throws Exception{
		ladoD.generar();
		
		//operador
				String operador= token.getLexema();
				
				switch(operador) {
				
					case("+"):{
						//nada , 5 es igual a +5
						break;
					}
					case("-"):{
						Analizador_Sintactico.salida.generar("NEG","negativo del numero");		
								break;
							}
					case("!"):{
						Analizador_Sintactico.salida.generar("NOT","negativo booleano");
						break;
					}
				}
		
	}
	
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Expresion Unaria");
		tabs(n+1); System.out.println("Token: "+token.getLexema()+"");
		ladoD.imprimir(n+1);
	}
	
	
	
}
