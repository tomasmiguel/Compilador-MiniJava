package Componentes;
import Tipos_Datos.Tipo;

public abstract class VarMetodo extends EntradaVariable{
	
	
	protected VarMetodo(String n, Tipo t, Token tok) {
		super(n,t,tok);
	}
	
	public abstract void generar();
	
}
