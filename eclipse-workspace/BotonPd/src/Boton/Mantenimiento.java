package Boton;

import java.time.LocalDate;

/**
 * Representa un registro de mantenimiento/limpieza de una habitación del hotel.
 */
public class Mantenimiento {
	private int idLimpieza;
	private String numeroHabitacion;
	private LocalDate fechaLimpieza;
	
	public Mantenimiento(int idLimpieza, String numeroHabitacion, LocalDate fechaLimpieza) {
		this.idLimpieza = idLimpieza;
		this.numeroHabitacion = numeroHabitacion;
		this.fechaLimpieza = fechaLimpieza;
	}
	
	// Getters y Setters
	public int getIdLimpieza() {
		return idLimpieza;
	}
	
	public void setIdLimpieza(int idLimpieza) {
		this.idLimpieza = idLimpieza;
	}
	
	public String getNumeroHabitacion() {
		return numeroHabitacion;
	}
	
	public void setNumeroHabitacion(String numeroHabitacion) {
		this.numeroHabitacion = numeroHabitacion;
	}
	
	public LocalDate getFechaLimpieza() {
		return fechaLimpieza;
	}
	
	public void setFechaLimpieza(LocalDate fechaLimpieza) {
		this.fechaLimpieza = fechaLimpieza;
	}
	
	@Override
	public String toString() {
		return "Limpieza #" + idLimpieza + " - Hab. " + numeroHabitacion + " (" + fechaLimpieza + ")";
	}
}
