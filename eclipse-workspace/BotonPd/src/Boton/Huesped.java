package Boton;

import java.time.LocalDate;

/**
 * Representa un huésped del hotel con sus datos personales y asignación de habitación.
 */
public class Huesped {
	private String nombre;
	private String apellido;
	private String documento;
	private String telefono;
	private String email;
	private LocalDate fechaEntrada;
	private LocalDate fechaSalida;
	private String habitacionAsignada;
	
	public Huesped(String nombre, String apellido, String documento, String telefono, String email) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.documento = documento;
		this.telefono = telefono;
		this.email = email;
		this.fechaEntrada = null;
		this.fechaSalida = null;
		this.habitacionAsignada = "";
	}
	
	// Getters y Setters
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDate getFechaEntrada() {
		return fechaEntrada;
	}
	
	public void setFechaEntrada(LocalDate fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	
	public LocalDate getFechaSalida() {
		return fechaSalida;
	}
	
	public void setFechaSalida(LocalDate fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	
	public String getHabitacionAsignada() {
		return habitacionAsignada;
	}
	
	public void setHabitacionAsignada(String habitacionAsignada) {
		this.habitacionAsignada = habitacionAsignada;
	}
	
	public String getNombreCompleto() {
		return nombre + " " + apellido;
	}
	
	@Override
	public String toString() {
		return getNombreCompleto() + " - " + documento;
	}
}
