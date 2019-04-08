package Componentes;


// clase que define la estructura de un Token valido
public class Token {
	private String nombre;
	private String lexemaActual;
	private int lineaArchFuente, columnaArchFuente;
	
	//Constructor
	public Token(String n,String l,int linea,int columna) {
		nombre= n;
		lexemaActual=l;
		lineaArchFuente=linea;
		columnaArchFuente=columna;
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public String getLexema() {
		return lexemaActual;
	}
	public int getNroLinea() {
		return lineaArchFuente;
	}
	public int getNroColumna() {
		return columnaArchFuente;
	}
	
}
