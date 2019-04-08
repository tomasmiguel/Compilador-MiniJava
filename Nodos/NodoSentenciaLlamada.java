package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.Token;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoVoid;

public class NodoSentenciaLlamada extends NodoSentencia {
	protected NodoPrimario primario;
	protected Tipo tipo;
	
	public NodoSentenciaLlamada(NodoPrimario p, Token tok) {
		super(tok);
		primario=p;
	}


	public void chequear() throws Exception {
		
		tipo= primario.chequear();
	}
	
	
	
	public void generar() throws Exception{
		primario.generar();
		
		//si primario devuelve algo, lo elimino con pop. No me interesa esa sentencia 		(sentencia);
		if ( ! (tipo instanceof TipoVoid)) {
			Analizador_Sintactico.salida.generar("POP", "elimino el valor de retorno de la sentencia llamada");
		}
	}
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Sentencia Llamada");
		primario.imprimir(n+1);
	}
	
}
