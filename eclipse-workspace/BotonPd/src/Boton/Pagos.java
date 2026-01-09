package Boton;

import java.time.LocalDate;

/**
 * Representa un pago realizado por un huésped del hotel.
 */
public class Pagos {
	private int idPago;
	private String nombreHuesped;
	private double monto;
	private String metodoPago;
	private LocalDate fechaPago;
	private String numeroHabitacion;
	
	public Pagos(int idPago, String nombreHuesped, double monto, String metodoPago, LocalDate fechaPago, String numeroHabitacion) {
		this.idPago = idPago;
		this.nombreHuesped = nombreHuesped;
		this.monto = monto;
		this.metodoPago = metodoPago;
		this.fechaPago = fechaPago;
		this.numeroHabitacion = numeroHabitacion;
	}
	
	// Getters y Setters
	public int getIdPago() {
		return idPago;
	}
	
	public void setIdPago(int idPago) {
		this.idPago = idPago;
	}
	
	public String getNombreHuesped() {
		return nombreHuesped;
	}
	
	public void setNombreHuesped(String nombreHuesped) {
		this.nombreHuesped = nombreHuesped;
	}
	
	public double getMonto() {
		return monto;
	}
	
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	public String getMetodoPago() {
		return metodoPago;
	}
	
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	public LocalDate getFechaPago() {
		return fechaPago;
	}
	
	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}
	
	public String getNumeroHabitacion() {
		return numeroHabitacion;
	}
	
	public void setNumeroHabitacion(String numeroHabitacion) {
		this.numeroHabitacion = numeroHabitacion;
	}
	
	@Override
	public String toString() {
		return "Pago #" + idPago + " - " + nombreHuesped + " - $" + monto + " (" + metodoPago + ")";
	}
}
