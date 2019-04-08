package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Generador;
import Componentes.Token;
import Excepciones.ErrorCondicionIf;
import Tipos_Datos.TipoBoolean;

public class NodoIfSolo extends NodoIf {

	public NodoIfSolo(NodoExpresion ne, NodoSentencia ns, Token tok) {
		super(ne,ns, tok);
	}

	
	public void chequear() throws Exception {
		
		//veo si la condicion es correctamente tipada y de tipo boolean
		if (!(condicion.chequear() instanceof TipoBoolean))
			throw new ErrorCondicionIf(token.getNroLinea(),token.getNroColumna());
		
		//si es asi chequeo sentencia then
		sentenciaThen.chequear();
	}
	
	//if sin else
	public void generar() throws Exception{
		condicion.generar(); //en tope tengo 0 o 1
		
		
		@SuppressWarnings("static-access")
		String etiqueta= "et_finIf"+Analizador_Sintactico.salida.contadorIf;
		
		//aumento contador if para proxima etiqueta de algun if
				Generador.contadorIf++;
		Analizador_Sintactico.salida.generar("BF "+etiqueta, "si condicion false, salto al fin del if");
		
		//sino ejecuto then
		sentenciaThen.generar();
		//fin if
		Analizador_Sintactico.salida.generar(""+etiqueta+": NOP", "fin del if");
		
		
	}
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("IF Sin Else");
		condicion.imprimir(n+1);
		sentenciaThen.imprimir(n+1);
	}
}
