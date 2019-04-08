package Excepciones;


@SuppressWarnings("serial")
public class ErrorSintactico extends Exception {
	public ErrorSintactico(String mensaje){
		super(mensaje);
	}
}