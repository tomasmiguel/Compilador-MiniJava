package Componentes;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoNull;

public class EntradaCtor extends Unidad{
	
	public EntradaCtor(String n, Token tok) {
		super(tok,n);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getNombreAux() {
		return "";
	}
	
	public String getModificador() {
		return "dynamic";
	}
	
	public Tipo getTipoRetorno() {
		return new TipoNull("");
	}
	
	public String getEtiqueta() {
		return nombre+"$"+entradaParametros.size();
	}
	
	
	public void generar() throws Exception {
		
	 if (! generado) {	
		 
		//comienza el nuevo RA y genero el codigo del cuerpo del ctor
		super.generar();
		
		//vuelvo al registro de activacion anterior
		Analizador_Sintactico.salida.generar("STOREFP","indico el actual RA");
		
		//retorno eliminando el espacio reservado para los parametros + el this
		Analizador_Sintactico.salida.generar("RET "+(entradaParametros.size()+1),"retorno de la unidad dinamica llamada");
	 
		generado=true;
	 }
	 
	//si ya fue generado-> no hago nada
	 	
	}
	
	
	

	
	
	
	public void imprimirAST() {	
		System.out.println("+ "+entradaParametros.size()+" parametros: ");
		if(cuerpo!=null) this.cuerpo.imprimir(0);
	}
}
