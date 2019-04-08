package Componentes;
import Tipos_Datos.Tipo;

public class EntradaPar extends VarMetodo{
	private int ubicacion;
	
	public EntradaPar(String n,Tipo t,int u, Token tok) {
		super(n,t, tok);
		ubicacion= u;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Tipo getTipo() {
		return tipo;
	}
	
	public int getUbicacion() {
		return ubicacion;
	}
	
	public Token getToken() {
		return token;
	}

	
	public void generar() {
		
		
	}
}
