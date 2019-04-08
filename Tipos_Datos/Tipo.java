package Tipos_Datos;


public interface Tipo {
	
	public String getTipo();
	public boolean compatible(Tipo tipoD);
	public abstract void imprimir();
}
