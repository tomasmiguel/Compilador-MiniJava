package Componentes;
import Tipos_Datos.Tipo;

public class EntradaMetodo extends Unidad{
	private Tipo tipoRetorno;
	private String modificador; //dynamic o static
	private boolean esEstatico;
	private String nombre;
	private String nombreAux;
	private EntradaClase clase;
	private boolean consolidado;
	
	
	public EntradaMetodo(String n, Tipo tr,String mod, Token tok,EntradaClase c) {
		super(tok,n);
		nombre=n;
		tipoRetorno=tr;
		modificador=mod;
		nombre=n;
		clase=c;
		generado=false;
		
		
		if (mod.equals("static"))
			esEstatico=true;
		else
			esEstatico=false;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public void setNombre(String n) {
		nombre=n;
	}
	
	public void setNombreAux(String n) {
		nombreAux=n;
	}
	public String getNombreAux() {
		return nombreAux;
	}
	
	public void setConsolidado(boolean c) {
		consolidado=c;
	}
	
	public boolean esConsolidado() {
		return consolidado;
	}
	
	public Tipo getTipoRetorno() {
		return tipoRetorno;
	}
	
	public String getModificador() {
		return modificador;
	}
	
	public boolean esEstatico() {
		return esEstatico;
	}
		
	
	public String getEtiqueta() {
		return nombre+"$"+clase.getNombre();
	}
	
	
	public void generar() throws Exception {
		//si el metodo aun no fue generado -> lo genero
		if (!generado) {
			
			//comienza el nuevo RA y genero el codigo del cuerpo del ctor
			super.generar();
			
			//guardo el nuevo RA
			Analizador_Sintactico.salida.generar("STOREFP","indico que el RA actual es este");
	
			//retorno eliminando el espacio reservado para los parametros
			if (modificador.equals("static"))
				Analizador_Sintactico.salida.generar("RET "+entradaParametros.size(),"retorno de la unidad estatica llamada");
			else
				//retorno eliminando el espacio reservado para los parametros + el this
				Analizador_Sintactico.salida.generar("RET "+(entradaParametros.size()+1),"retorno de la unidad dinamica llamada");
		
			generado=true;
		}
		
		//si ya fue generado-> no hago nada
	}
	
	
	
	public void imprimirAST() {	
		System.out.print("+  "+token.getLexema()+" (Tipo: ");
		tipoRetorno.imprimir();
		System.out.println(")");
		if(cuerpo!=null) this.cuerpo.imprimir(0);
	}
}
