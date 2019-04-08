package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorArregloNoIdClase extends Exception {
		public ErrorArregloNoIdClase(int linea,int columna){
			super("Error Semantico: no se puede invocar a un metodo desde una entidad que no sea de tipo idClase, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
