package Nodos;
import Componentes.Analizador_Sintactico;
import Componentes.EntradaCtor;
import Componentes.EntradaMetodo;
import Componentes.Token;
import Componentes.Unidad;
import Excepciones.ErrorCtorConReturn;
import Excepciones.ErrorReturn;
import Tipos_Datos.Tipo;

public class NodoReturn extends NodoSentencia {
	protected NodoExpresion retorno;
	protected Unidad unidad;
	
	
	public NodoReturn(NodoExpresion r, Token tok) {
		super(tok);
		retorno=r;
		unidad=null;
	}

	
	public void chequear() throws Exception {
		//chequeo tipo retorno void para ese metodo
		unidad=  Analizador_Sintactico.TS.getUnidadActual();
		
		//si la unidadActual es un Ctor -> error
		if (unidad instanceof EntradaCtor)
			throw new ErrorCtorConReturn(unidad.getNombre(), token.getNroLinea(), token.getNroColumna());
		
		//sino es un metodo
		unidad= (EntradaMetodo) unidad;
		
		//chequeo que retorno es correctamente tipado
		Tipo tRet= retorno.chequear();
		
		//si los tipos no son compatibles o el metodo no tiene un return  -> error
		if ( ! (unidad.getTipoRetorno().compatible(tRet)) )
			throw new ErrorReturn(unidad.getNombre(), tRet.getTipo(), unidad.getTipoRetorno().getTipo(), token.getNroLinea(), token.getNroColumna());
	}
	
	

	public void generar()throws Exception {
		//genero codigo de la expresion retorno
		retorno.generar();
		
		int dinamico=0;
		//guardo lo obtenido en lugar de retorno
		if (unidad.getModificador().equals("dynamic")) {
			Analizador_Sintactico.salida.generar("STORE "+  (4 + unidad.getEntradaParametros().size()), "almaceno el valor de retorno en el lugar de ret del metodo dinamico");
			dinamico=1;
		}
			else
			Analizador_Sintactico.salida.generar("STORE "+  (3 + unidad.getEntradaParametros().size()), "almaceno el valor de retorno en el lugar de ret del metodo estatico");
			
		//limpio memoria de todas las var locales
		Analizador_Sintactico.salida.generar("FMEM "+(unidad.getOffsetVarLocal()* -1),"libero memoria de todas mis variables locales");
		
		//vuelvo al RA anterior
		Analizador_Sintactico.salida.generar("STOREFP","vuelvo al RA anterior");
		//libero memoria de los parametros					+1 si es dinamico por el this
		Analizador_Sintactico.salida.generar("RET "+(unidad.getEntradaParametros().size()+dinamico),"retorno de la unidad, y libero memoria usada por los parametros ");
	}
	
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Return");
		retorno.imprimir(n+1);
	}
}
