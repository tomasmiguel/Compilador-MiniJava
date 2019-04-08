package Tipos_Datos;

public class TipoArregloChar extends Arreglo {
	
	
	public TipoArregloChar(String n) {
		nombre= "arregloChar";
	}
	
	public String getTipo() {
		return nombre;
	}
	
	public boolean compatible(Tipo tipoD) {
		//mismo tipo -> true
		return (tipoD.getTipo().equals(nombre) || tipoD.getTipo().equals("null"));
	}
	
	
	
	public void imprimir(){
		System.out.print("TipoArregloChar");
	}
}