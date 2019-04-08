package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Generador;
import Componentes.Token;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoString;

public  class NodoLiteral extends NodoOperando {
	protected Tipo tipo;

	public NodoLiteral(Tipo t,Token tok) {
		super(tok);
		tipo=t;
	}

	
	public Tipo chequear() throws Exception {
		return tipo;	
	}

	public void generar() {
		if (token.getLexema().equals("false"))
			Analizador_Sintactico.salida.generar("PUSH 0","apilo el valor del literal false");
		else if (token.getLexema().equals("true"))
			Analizador_Sintactico.salida.generar("PUSH 1","apilo el valor del literal true");
		
		else if (token.getLexema().equals("null"))
			Analizador_Sintactico.salida.generar("PUSH 0","apilo el valor del literal null -> 0");
		
		else if (tipo instanceof TipoString) {
			Analizador_Sintactico.salida.generar(".DATA","agrego mi string estaticamente (forma cabeza)");
			
			String etiquetaString= "et_string"+Generador.contadorString;
			Analizador_Sintactico.salida.generar(etiquetaString+": DW "+token.getLexema()+", 0","agrego string a .DATA");
			Analizador_Sintactico.salida.generar(".CODE","agrego el codigo de mi string");
			Analizador_Sintactico.salida.generar("PUSH "+etiquetaString,"apilo la etiqueta del string");
			
			Generador.contadorString++;
		}
		else
			Analizador_Sintactico.salida.generar("PUSH "+token.getLexema(),"apilo el valor del literal");
	}
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Literal");
		tabs(n+1); System.out.println("   Tipo: "+tipo.getTipo()+"");
		tabs(n+1); System.out.println("   Valor: "+token.getLexema()+"");
	}
}
