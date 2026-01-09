package Boton;

/**
 * Representa una habitación del hotel con sus propiedades básicas.
 */
public class Habitacion {
	private String numero;
	private String categoria;
	private String estado; // "DISPONIBLE", "OCUPADA", "MANTENIMIENTO"
	private double precio;
	private String huesped;
	
	public Habitacion(String numero, String categoria, String estado, double precio) {
		this.numero = numero;
		this.categoria = categoria;
		this.estado = estado;
		this.precio = precio;
		this.huesped = "";
	}
	
	// Getters y Setters
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	public String getHuesped() {
		return huesped;
	}
	
	public void setHuesped(String huesped) {
		this.huesped = huesped;
	}
	
	@Override
	public String toString() {
		return numero + " - " + categoria + " (" + estado + ")";
	}
}
