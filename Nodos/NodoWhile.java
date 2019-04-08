package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Generador;
import Componentes.Token;
import Excepciones.ErrorCondicionWhile;
import Tipos_Datos.TipoBoolean;

public class NodoWhile extends NodoSentencia{
	protected NodoExpresion condicion;
	protected NodoSentencia cuerpo;
	
	public NodoWhile(NodoExpresion ne, NodoSentencia ns, Token tok) {
		super(tok);
		condicion=ne;
		cuerpo= ns;
	}
	
	public void chequear() throws Exception{
		if (! (condicion.chequear() instanceof TipoBoolean))
			throw new ErrorCondicionWhile(condicion.getToken().getNroLinea(),condicion.getToken().getNroColumna());
		//sino
			cuerpo.chequear();
	}
	
	public void generar() throws Exception {
		
		//etiqueta condicion del while
		String etiqueta1= "et_comienzoWhile"+Generador.contadorWhile;
		
		//declaro la etiqueta
		Analizador_Sintactico.salida.generar(""+etiqueta1+": NOP", "comienzo del while");
		
		condicion.generar(); //en tope tengo 0 o 1
		

		String etiqueta2= "et_finWhile"+Generador.contadorWhile;
		Analizador_Sintactico.salida.generar("BF "+etiqueta2, "si condicion false, salto al fin del while");
		
		//aumento contador if para proxima etiqueta de algun if
				Generador.contadorWhile++;
		//sino ejecuto cuerpo
		cuerpo.generar();
		//saltar a evaluar condicion de while
		Analizador_Sintactico.salida.generar("JUMP "+etiqueta1, "salto a evaluar la condicion del while nuevamente");
		
		//fin while
		Analizador_Sintactico.salida.generar(""+etiqueta2+": NOP", "fin del while");
		
		
	}
	
	
	
	
	//imprimir
		public void imprimir(int n){
			tabs(n); 
			System.out.println("While");
			condicion.imprimir(n+1);
			cuerpo.imprimir(n+1);
		}
}
