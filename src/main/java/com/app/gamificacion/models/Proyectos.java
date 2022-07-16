package com.app.gamificacion.models;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Proyectos {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "Name cannot be null")
	@Size(max = 200)
	@Indexed(unique = true)
	private String nombre;

	@Indexed(unique = true)
	private Integer codigoProyecto;

	@NotEmpty(message = "palabras clave cannot be empty")
	private List<String> palabrasClave;

	@NotEmpty(message = "ubicacion cannot be null")
	private List<Double> ubicacion;

	@NotNull(message = "resumen cannot be null")
	private String resumen;

	@NotEmpty(message = "objetivos cannot be null")
	private List<String> objetivos;

	@NotNull(message = "descripcion cannot be null")
	private String descripcion;

	@NotNull(message = "principales itos cannot be null")
	private List<String> hitos;

	@NotNull(message = "presupuesto itos cannot be null")
	private Long presupuesto;

	private List<String> cronograma;

	@NotNull(message = "activo cannot be null")
	private Boolean activo;

	@NotNull(message = "estadoProyecto cannot be null")
	private Integer estadoProyecto;

	@NotNull(message = "proyectoDesarrollo cannot be null")
	private List<Integer> proyectoDesarrollo;
	private Integer muro;

	@NotNull(message = "creador cannot be null")
	private List<String> creador;

	@NotNull
	private Date fechaLanzamiento;

	@NotNull(message = "gamificacion cannot be null")
	private Boolean gamificacion;

	@NotNull(message = "mensajeParticipacion cannot be null")
	private String mensajeParticipacion;

	public Proyectos(String nombre, Integer codigoProyecto, List<String> palabrasClave, List<Double> ubicacion,
			String resumen, List<String> objetivos, String descripcion, List<String> hitos, Long presupuesto,
			List<String> cronograma, Boolean activo, Integer estadoProyecto, List<Integer> proyectoDesarrollo,
			Integer muro, List<String> creador, Date fechaLanzamiento, Boolean gamificacion,
			String mensajeParticipacion) {
		super();
		this.nombre = nombre;
		this.codigoProyecto = codigoProyecto;
		this.palabrasClave = palabrasClave;
		this.ubicacion = ubicacion;
		this.resumen = resumen;
		this.objetivos = objetivos;
		this.descripcion = descripcion;
		this.hitos = hitos;
		this.presupuesto = presupuesto;
		this.cronograma = cronograma;
		this.activo = activo;
		this.estadoProyecto = estadoProyecto;
		this.proyectoDesarrollo = proyectoDesarrollo;
		this.muro = muro;
		this.creador = creador;
		this.fechaLanzamiento = fechaLanzamiento;
		this.gamificacion = gamificacion;
		this.mensajeParticipacion = mensajeParticipacion;
	}
}