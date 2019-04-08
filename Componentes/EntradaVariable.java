package Componentes;
import Tipos_Datos.Tipo;

public abstract class EntradaVariable {
	protected String nombre;
	protected Tipo tipo;
	protected Token token;
	protected int offsetVar;
	
	protected EntradaVariable(String n, Tipo t, Token tok) {
		nombre=n;
		tipo=t;
		token= tok;
		offsetVar=0;
	}
	
	public abstract String getNombre();
	
	public abstract Tipo getTipo();

	public int getOffset() {
		return offsetVar;
	}
	
	public void setOffset(int off) {
		offsetVar=off;
	}
}
