package Excepciones;



@SuppressWarnings("serial")
public class parametroDeclaradoInvalido extends Exception {
	public parametroDeclaradoInvalido(String parametro,int linea, int columna){
		super("Error Semantico: El parametro/variable local '"+parametro+"' declarada ya existe, error en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}