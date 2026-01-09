package Boton;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reserva {
	private int id;
	private String huespedNombre;
	private String huespedDocumento;
	private String habitacionNumero;
	private LocalDate fechaEntrada;
	private LocalDate fechaSalida;
	private String estado; // "PENDIENTE", "CONFIRMADA", "CANCELADA", "COMPLETADA"
	private double precioTotal;
	private String metodoPago;
	private String observaciones;
	
	public Reserva(int id, String huespedNombre, String huespedDocumento, String habitacionNumero,
			LocalDate fechaEntrada, LocalDate fechaSalida, String estado, double precioTotal) {
		this.id = id;
		this.huespedNombre = huespedNombre;
		this.huespedDocumento = huespedDocumento;
		this.habitacionNumero = habitacionNumero;
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
		this.estado = estado;
		this.precioTotal = precioTotal;
		this.metodoPago = "";
		this.observaciones = "";
	}
	
	public long getNochesReservadas() {
		if (fechaEntrada != null && fechaSalida != null) {
			return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
		}
		return 0;
	}
	
	// Getters y Setters
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getHuespedNombre() {
		return huespedNombre;
	}
	
	public void setHuespedNombre(String huespedNombre) {
		this.huespedNombre = huespedNombre;
	}
	
	public String getHuespedDocumento() {
		return huespedDocumento;
	}
	
	public void setHuespedDocumento(String huespedDocumento) {
		this.huespedDocumento = huespedDocumento;
	}
	
	public String getHabitacionNumero() {
		return habitacionNumero;
	}
	
	public void setHabitacionNumero(String habitacionNumero) {
		this.habitacionNumero = habitacionNumero;
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
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public double getPrecioTotal() {
		return precioTotal;
	}
	
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}
	
	public String getMetodoPago() {
		return metodoPago;
	}
	
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	@Override
	public String toString() {
		return "Reserva #" + id + " - " + huespedNombre + " (" + habitacionNumero + ")";
	}
}
