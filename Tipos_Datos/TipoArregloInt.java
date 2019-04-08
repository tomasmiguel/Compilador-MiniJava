package Tipos_Datos;

public class TipoArregloInt extends Arreglo {
	
	
	public TipoArregloInt(String n) {
		nombre= "arregloInt";
	}
		
	public String getTipo() {
		return nombre;
	}
	
	public boolean compatible(Tipo tipoD) {
		//mismo tipo -> true
		return (tipoD.getTipo().equals(nombre) || tipoD.getTipo().equals("null"));
	}

	public void imprimir(){
		System.out.print("TipoArregloInt");
	}


}
