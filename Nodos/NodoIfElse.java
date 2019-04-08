package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Generador;
import Componentes.Token;
import Excepciones.ErrorCondicionIf;
import Tipos_Datos.TipoBoolean;

public class NodoIfElse extends NodoIf {
	protected NodoSentencia sentenciaElse;
	
	public NodoIfElse(NodoExpresion ne, NodoSentencia ns, NodoSentencia nsElse, Token tok) {
		super(ne,ns,tok);
		sentenciaElse= nsElse;
	}

	
	public void chequear() throws Exception {

		//veo si la condicion es correctamente tipada y de tipo boolean
		if (!(condicion.chequear() instanceof TipoBoolean))
			throw new ErrorCondicionIf(token.getNroLinea(),token.getNroColumna());
		
		//si es asi chequeo sentencia then
		sentenciaThen.chequear();
		//y sentencia else
		sentenciaElse.chequear();
		
	}
	
	//if con else
	public void generar() throws Exception {
		condicion.generar(); //en tope tengo 0 o 1
		
		
		String etiquetaIf= "et_finIf"+Generador.contadorIf;
		String etiquetaElse= "et_else"+Generador.contadorIf;
		//aumento contador if para proxima etiqueta de algun if
				Generador.contadorIf++;
		
		Analizador_Sintactico.salida.generar("BF "+etiquetaElse, "si condicion false, salto al else");
		//sino ejecuto then
		sentenciaThen.generar();
		Analizador_Sintactico.salida.generar("JUMP "+etiquetaIf, "termine then, salto a fin del if");
		
		//else
		Analizador_Sintactico.salida.generar(""+etiquetaElse+": NOP", "comienzo del else");
		sentenciaElse.generar();
		//fin if
		Analizador_Sintactico.salida.generar(""+etiquetaIf+": NOP", "fin del if");
		
		
		
	}
	
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("If Con Else");
		condicion.imprimir(n+1);
		sentenciaThen.imprimir(n+1);
		sentenciaElse.imprimir(n+1);
	}
}
