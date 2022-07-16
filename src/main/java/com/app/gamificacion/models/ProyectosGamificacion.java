package com.app.gamificacion.models;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "gamificacion-proyectos")
@Data
@NoArgsConstructor
public class ProyectosGamificacion {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "id proyecto cannot be null")
	@Indexed(unique = true)
	private Integer idProyecto;

	@NotNull(message = "premios proyecto cannot be null")
	private String titulo;
	private List<String> premios;
	
	@NotNull(message = "tyc proyecto cannot be null")
	private String tyc;
	
	@NotNull(message = "fechaTerminacion proyecto cannot be null")
	private Date fechaTerminacion;
	
	@NotEmpty(message = "patrocinadores clave cannot be empty")
	private List<String> patrocinadores;
	
	@NotEmpty(message = "usuariosParticipantes clave cannot be empty")
	private List<String> usuariosParticipantes;
	
	@NotEmpty(message = "usuariosGanadores clave cannot be empty")
	private List<String> usuariosGanadores;
	
	@NotNull(message = "habilitado proyecto cannot be null")
	private Boolean habilitado;
	@NotNull(message = "ganadores")
	private Integer ganadores;
	@NotNull(message = "mensajeParticipacion proyecto cannot be null")
	private String mensajeParticipacion;
	@NotNull(message = "mensajeGanador proyecto cannot be null")
	private String mensajeGanador;
	@NotNull(message = "mensajeBienvenida proyecto cannot be null")
	private String mensajeBienvenida;

	public ProyectosGamificacion(Integer idProyecto, String titulo, List<String> premios, String tyc,
			Date fechaTerminacion, List<String> patrocinadores, List<String> usuariosParticipantes,
			List<String> usuariosGanadores, Boolean habilitado, Integer ganadores, String mensajeParticipacion,
			String mensajeGanador, String mensajeBienvenida) {
		super();
		this.idProyecto = idProyecto;
		this.titulo = titulo;
		this.premios = premios;
		this.tyc = tyc;
		this.fechaTerminacion = fechaTerminacion;
		this.patrocinadores = patrocinadores;
		this.usuariosParticipantes = usuariosParticipantes;
		this.usuariosGanadores = usuariosGanadores;
		this.habilitado = habilitado;
		this.ganadores = ganadores;
		this.mensajeParticipacion = mensajeParticipacion;
		this.mensajeGanador = mensajeGanador;
		this.mensajeBienvenida = mensajeBienvenida;
	}

}
