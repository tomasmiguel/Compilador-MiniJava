package Componentes;
import Tipos_Datos.Tipo;

public class EntradaVarInst extends EntradaVariable{
	
	private String visibilidad;
	
	public EntradaVarInst(String n,Tipo t,String v,Token tok) {
		super(n,t,tok);
		visibilidad=v;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Tipo getTipo() {
		return tipo;
	}
	
	public String getVisibilidad() {
		return visibilidad;
	}
	
	public Token getToken() {
		return token;
	}
	
	
	
	
	public void imprimirAST(){
		System.out.println(token.getLexema()+"");
		//exp.imprimir(1);
		//at INLINE
	}
}
