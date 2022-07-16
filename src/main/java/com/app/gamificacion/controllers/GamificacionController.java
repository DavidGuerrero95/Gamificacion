package com.app.gamificacion.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.gamificacion.clients.NotificacionesFeignClient;
import com.app.gamificacion.clients.ProyectosFeignClient;
import com.app.gamificacion.models.ProyectosGamificacion;
import com.app.gamificacion.repository.ProyectosGamificacionRepository;

@RestController
public class GamificacionController {

	private final Logger logger = LoggerFactory.getLogger(GamificacionController.class);

	@Autowired
	ProyectosGamificacionRepository pgRepository;

	@Autowired
	ProyectosFeignClient pClient;

	@Autowired
	NotificacionesFeignClient nClient;

//  ****************************	GAMIFICACION 	***********************************  //

	// CREAR GAMIFICACION
	@PostMapping("/gamificacion/proyectos/crear/{idProyecto}")
	public Boolean crearGamificacion(@PathVariable("idProyecto") Integer idProyecto,
			@RequestParam(value = "titulo", defaultValue = "Titulo") String titulo,
			@RequestParam(value = "premios", defaultValue = "") List<String> premios,
			@RequestParam(value = "tyc", defaultValue = "Terminos y Condiciones") String tyc,
			@RequestParam(value = "fechaTerminacion") Date fechaTerminacion,
			@RequestParam(value = "patrocinadores", defaultValue = "") List<String> patrocinadores,
			@RequestParam(value = "habilitado", defaultValue = "false") Boolean habilitado,
			@RequestParam(value = "ganadores", defaultValue = "1") Integer ganadores,
			@RequestParam(value = "mensajeParticipacion", defaultValue = "\n Ya estas participando en la rifa de: ") String mensajeParticipacion,
			@RequestParam(value = "mensajeGanador", defaultValue = "Ganaste el sorteo en el marco del proyecto: ") String mensajeGanador,
			@RequestParam(value = "mensajeBienvenida", defaultValue = "Por participar en este proyecto podras ser parte de una gran rifa") String mensajeBienvenida)
			throws IOException {
		if (pClient.existNombre(idProyecto)) {
			if (pClient.verEstadoGamificacion(idProyecto)) {
				if (fechaTerminacion == null)
					fechaTerminacion = new Date();
				ProyectosGamificacion p = new ProyectosGamificacion(idProyecto, titulo, premios, tyc, fechaTerminacion,
						patrocinadores, new ArrayList<String>(), new ArrayList<String>(), habilitado, ganadores,
						mensajeParticipacion, mensajeGanador, mensajeBienvenida);
				pgRepository.save(p);
				logger.info("Creacion Correcta Gamificacion");
				return true;
			}
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El proyecto no existe");
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto no existe");
	}

	// EDITAR GAMIFICACION
	@PutMapping("/gamificacion/proyectos/editar/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> editarGamificacionProyecto(@PathVariable("idProyecto") Integer idProyecto,
			@RequestParam("titulo") String titulo, @RequestParam("premios") List<String> premios,
			@RequestParam("tyc") String tyc, @RequestParam("fechaTerminacion") String fecha,
			@RequestParam("patrocinadores") List<String> patrocinadores, @RequestParam("ganadores") Integer ganadores,
			@RequestParam("mensajeParticipacion") String mensajeParticipacion,
			@RequestParam("mensajeGanador") String mensajeGanador) throws IOException {

		if (pgRepository.existsByIdProyecto(idProyecto)) {
			ProyectosGamificacion pg = pgRepository.findByIdProyecto(idProyecto);
			Date date = new Date();
			if (!fecha.isEmpty()) {
				date = new Date();
				pg.setFechaTerminacion(date);
			}
			if (!titulo.isEmpty())
				pg.setTitulo(titulo);
			if (!premios.isEmpty())
				pg.setPremios(premios);
			if (!tyc.isEmpty())
				pg.setTyc(tyc);
			if (!patrocinadores.isEmpty())
				pg.setPatrocinadores(patrocinadores);
			if (ganadores != null)
				pg.setGanadores(ganadores);
			if (!mensajeParticipacion.isEmpty())
				pg.setMensajeParticipacion(mensajeParticipacion);
			if (!mensajeGanador.isEmpty())
				pg.setMensajeGanador(mensajeGanador);

			pgRepository.save(pg);
			return ResponseEntity.ok("Edicion correcta de gamificacion");
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto no existe");

	}

	// VER GAMIFICACION
	@GetMapping("/gamificacion/proyectos/ver/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public ProyectosGamificacion verGamificacionProyectos(@PathVariable("idProyecto") Integer idProyecto) {
		if (pgRepository.existsByIdProyecto(idProyecto)) {
			return pgRepository.findByIdProyecto(idProyecto);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto no existe");
	}

	// CAMBIAR ESTADO GAMIFICACION
	@PutMapping("/gamificacion/proyectos/cambiar-estado/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public void habilitarProyectosGamificacion(@PathVariable("idProyecto") Integer idProyecto) {
		if (pgRepository.existsByIdProyecto(idProyecto)) {
			ProyectosGamificacion p = pgRepository.findByIdProyecto(idProyecto);
			if (p.getHabilitado())
				p.setHabilitado(false);
			else
				p.setHabilitado(true);

			pgRepository.save(p);
			logger.info("Actualizado correctamente: " + idProyecto);
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto no existe");
	}

	// AGREGAR PARTICIPANTE
	@PutMapping("/gamificacion/proyectos/agregar-participante/{idProyecto}")
	public Boolean agregarParticipante(@PathVariable("idProyecto") Integer idProyecto,
			@RequestParam("username") String username) throws IOException {
		try {
			if (pgRepository.existsByIdProyecto(idProyecto)) {
				ProyectosGamificacion p = pgRepository.findByIdProyecto(idProyecto);
				if (p.getHabilitado()) {
					List<String> listaP = p.getUsuariosParticipantes();
					listaP.add(username);
					p.setUsuariosParticipantes(listaP);
					pgRepository.save(p);
				}
				return true;
			} else
				return false;
		} catch (Exception e) {
			throw new IOException("Error al ver habilitado de proyecto: " + idProyecto);
		}
	}

	// DEFINIR GANADORES
	@PutMapping("/gamificacion/proyectos/definir-ganadores/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public void definirGanadoresProyecto(@PathVariable("idProyecto") Integer idProyecto) {
		if (pgRepository.existsByIdProyecto(idProyecto)) {
			ProyectosGamificacion p = pgRepository.findByIdProyecto(idProyecto);
			List<String> listaParticipantes = p.getUsuariosParticipantes();
			List<String> listaGanadores = new ArrayList<String>();
			Collections.shuffle(listaParticipantes);
			for (int i = 0; i < p.getGanadores(); i++) {
				listaGanadores.add(listaParticipantes.get(i));
			}
			p.setUsuariosGanadores(listaGanadores);
			p.setHabilitado(false);
			pgRepository.save(p);
			nClient.enviarNotificacionGanador(listaGanadores, p.getMensajeGanador());
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto no existe");
	}

	// VER GANADORES
	@GetMapping("/gamificacion/proyectos/ver-ganadores/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<String> verGanadoresProyecto(@PathVariable("idProyecto") Integer idProyecto) {
		if (pgRepository.existsByIdProyecto(idProyecto)) {
			return pgRepository.findByIdProyecto(idProyecto).getUsuariosGanadores();
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto no existe");
	}

	// ELIMINAR GAMIFICACION
	@DeleteMapping("/gamificacion/proyectos/eliminar/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean eliminarGamificacionProyecto(@PathVariable("idProyecto") Integer idProyecto) throws IOException {
		try {
			pgRepository.deleteById(pgRepository.findByIdProyecto(idProyecto).getId());
			return true;
		} catch (Exception e) {
			throw new IOException("Error al eliminar el la gamificacion del proyecto: ");
		}
	}

	// PREGUNTA SI GAMIFICACION EXISTE
	@GetMapping("/gamificacion/proyectos/existe/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean existeGamificacionProyecto(@PathVariable("idProyecto") Integer idProyecto) throws IOException {
		try {
			return pgRepository.existsByIdProyecto(idProyecto);
		} catch (Exception e) {
			throw new IOException("Error al ver habilitado de proyecto: ");
		}
	}

	// VER SI ESTA O NO HABILITADO
	@GetMapping("/gamificacion/proyectos/ver-habilitado/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean verHabilitadoProyecto(@PathVariable("idProyecto") Integer idProyecto) throws IOException {
		try {
			return pgRepository.findByIdProyecto(idProyecto).getHabilitado();
		} catch (Exception e) {
			throw new IOException("Error al ver habilitado de proyecto: ");
		}
	}

	@PostMapping("/gamificacion/arreglar/")
	@ResponseStatus(code = HttpStatus.OK)
	public void arreglarGamificacion() {
		pgRepository.deleteAll();
	}

	// VER MENSAJE PARTICIPACION
	@GetMapping("/gamificacion/proyectos/mensaje/participacion/{idProyecto}")
	public String verMensajeParticipacionGamificacionProyectos(@PathVariable("idProyecto") Integer idProyecto)
			throws IOException, ResponseStatusException {
		try {
			if (pgRepository.existsByIdProyecto(idProyecto)) {
				return pgRepository.findByIdProyecto(idProyecto).getMensajeParticipacion();
			}
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto no existe");
		} catch (Exception e) {
			throw new IOException("Error al ver mensaje participacion del proyecto: ");
		}
	}

}
