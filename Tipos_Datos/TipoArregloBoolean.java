package Tipos_Datos;

public class TipoArregloBoolean extends Arreglo {
	
	
	public TipoArregloBoolean(String n) {
		nombre= "arregloBoolean";
	}
	
	public String getTipo() {
		return nombre;
	}
	
	public boolean compatible(Tipo tipoD) {
		//mismo tipo -> true
		return (tipoD.getTipo().equals(nombre) || tipoD.getTipo().equals("null"));
	}
	
	
	public void imprimir(){
		System.out.print("TipoArregloBoolean");
	}
	
}