package Tipos_Datos;


public class TipoBoolean extends TipoPrimitivo{
	
	
	public TipoBoolean(String n) {
		nombre="boolean";
	}
	
	public String getTipo() {
		return nombre;
	}
	
	public boolean compatible(Tipo tipoD) {
		//mismo tipo -> true
		return (tipoD.getTipo().equals(nombre));
	}
	
	
	public void imprimir(){
		System.out.print("TipoBoolean");
	}
}
