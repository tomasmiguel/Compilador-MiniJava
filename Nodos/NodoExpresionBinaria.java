package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Token;
import Excepciones.ErrorDeTiposExpBin;
import Excepciones.ErrorDeTiposExpBinBoolean;
import Excepciones.ErrorDeTiposExpBinInt;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoBoolean;

public class NodoExpresionBinaria extends NodoExpresion{
	protected NodoExpresion ladoI; //izquierdo
	protected NodoExpresion ladoD; //derecho
	
	
	public NodoExpresionBinaria(NodoExpresion li, NodoExpresion ld, Token op) {
		super(op);
		ladoI= li;
		ladoD=ld;
	}


	public Tipo chequear() throws Exception{
		Tipo tipoD= ladoD.chequear();
		Tipo tipoI= ladoI.chequear();
			
		String operador= token.getLexema();
		
		if (operador.equals("+")|| operador.equals("-")|| operador.equals("*")|| operador.equals("/")) {
			if (tipoI.getTipo().equals("int") && tipoD.getTipo().equals("int"))
				return tipoI;
			else
				throw new ErrorDeTiposExpBinInt(operador,token.getNroLinea(), token.getNroColumna());
			
		}
		
		else if (operador.equals("<")|| operador.equals(">")|| operador.equals("<=")|| operador.equals(">=")) {
			if(tipoI.getTipo().equals("int") && tipoD.getTipo().equals("int")){
				return new TipoBoolean("");
			}
			else
				throw new ErrorDeTiposExpBinInt(operador,token.getNroLinea(), token.getNroColumna());
			
		}
		else if (operador.equals("&&")|| operador.equals("||")) {
			if (tipoI.compatible(tipoD) && tipoI instanceof TipoBoolean && tipoD instanceof TipoBoolean) {
				return new TipoBoolean("");
			}
			else
				throw new ErrorDeTiposExpBinBoolean(operador,token.getNroLinea(), token.getNroColumna());
		}
		else if (operador.equals("==")|| operador.equals("!=")) {
			//ver si son compatibles
			if (tipoI.compatible(tipoD) || tipoD.compatible(tipoI))
				return new TipoBoolean("");
		}
			//sino
			throw new ErrorDeTiposExpBin(operador,token.getNroLinea(), token.getNroColumna());	
	}//fin chequear
	
	
	
	public void generar() throws Exception {
		ladoI.generar();
		ladoD.generar();
		
		//operador
		String operador= token.getLexema();
		
		switch(operador) {
		
			case("+"):{
				Analizador_Sintactico.salida.generar("ADD","sumo tope-1 y tope");
				break;
			}
			case("-"):{
				Analizador_Sintactico.salida.generar("SUB","resto tope-1 y tope");		
						break;
					}
			case("*"):{
				Analizador_Sintactico.salida.generar("MUL","multiplico tope-1 y tope");
				break;
			}
			case("/"):{
				Analizador_Sintactico.salida.generar("DIV","divido tope-1 y tope");
				break;
			}
			case("<"):{
				Analizador_Sintactico.salida.generar("LT","comparo tope-1 < tope");
				break;
			}
			case(">"):{
				Analizador_Sintactico.salida.generar("GT","comparo tope-1 > tope");
				break;
			}
			case("<="):{
				Analizador_Sintactico.salida.generar("LE","comparo tope-1 <= tope");
				break;
			}
			case(">="):{
				Analizador_Sintactico.salida.generar("GE","comparo tope-1 >= tope");
				break;
			}
			case("&&"):{
				Analizador_Sintactico.salida.generar("AND","compara tope-1 y tope");
				break;
			}
			case("||"):{
				Analizador_Sintactico.salida.generar("OR","comparo tope-1 y tope");
				break;
			}
			case("=="):{
				Analizador_Sintactico.salida.generar("EQ","comparo tope-1 == tope");
				break;
			}
			case("!="):{
				Analizador_Sintactico.salida.generar("NE","comparo tope-1 != tope");
				break;
			}
			
		}//fin switch
	}//fin generar
	
	
	
	
	//imprimir
		public void imprimir(int n){
			tabs(n); 
			System.out.println("Expresion Binaria");
			tabs(n+1); System.out.println("Valor: "+token.getLexema()+"");
			ladoI.imprimir(n+1);
			ladoD.imprimir(n+1);
		}
	
	
}
