package com.app.gamificacion.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	@PostMapping("/gamificacion/proyectos/crear/{nombre}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> crearGamificacion(@PathVariable("nombre") String nombre,
			@RequestParam(value = "titulo", defaultValue = "Titulo") String titulo,
			@RequestParam(value = "premios", defaultValue = "") List<String> premios,
			@RequestParam(value = "tyc", defaultValue = "Terminos y Condiciones") String tyc,
			@RequestParam(value = "fechaTerminacion", defaultValue = "24/12/2022") String fechaTerminacion,
			@RequestParam(value = "patrocinadores", defaultValue = "") List<String> patrocinadores,
			@RequestParam(value = "habilitado", defaultValue = "false") Boolean habilitado,
			@RequestParam(value = "ganadores", defaultValue = "1") Integer ganadores,
			@RequestParam(value = "mensajeParticipacion", defaultValue = "\n Ya estas participando en la rifa de: ") String mensajeParticipacion,
			@RequestParam(value = "mensajeGanador", defaultValue = "Ganaste el sorteo en el marco del proyecto: ") String mensajeGanador,
			@RequestParam(value = "mensajeBienvenida", defaultValue = "Por participar en este proyecto podras ser parte de una gran rifa") String mensajeBienvenida)
			throws ParseException {
		if (pClient.existNombre(nombre)) {
			if (pClient.verEstadoGamificacion(nombre)) {
				Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaTerminacion);
				ProyectosGamificacion p = new ProyectosGamificacion(nombre, titulo, premios, tyc, fecha, patrocinadores,
						new ArrayList<String>(), new ArrayList<String>(), habilitado, ganadores, mensajeParticipacion,
						mensajeGanador,mensajeBienvenida);
				pgRepository.save(p);
				logger.info("Creacion Correcta Gamificacion");
				return ResponseEntity.ok("Gamificacion creada correctamente");
			}
			return ResponseEntity.badRequest().body("Gamificacion no esta habilitada en el proyecto: " + nombre);
		}
		return ResponseEntity.badRequest().body("proyecto: " + nombre + " no existe");
	}

	@PutMapping("/gamificacion/proyectos/editar/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> editarGamificacionProyecto(@PathVariable("nombre") String nombre,
			@RequestParam("titulo") String titulo, @RequestParam("premios") List<String> premios,
			@RequestParam("tyc") String tyc, @RequestParam("fechaTerminacion") String fecha,
			@RequestParam("patrocinadores") List<String> patrocinadores, @RequestParam("ganadores") Integer ganadores,
			@RequestParam("mensajeParticipacion") String mensajeParticipacion,
			@RequestParam("mensajeGanador") String mensajeGanador) throws ParseException {

		if (pgRepository.existsByNombre(nombre)) {
			ProyectosGamificacion pg = pgRepository.findByNombre(nombre);
			Date date = new Date();
			if (!fecha.isEmpty()) {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
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
		return ResponseEntity.badRequest().body("Proyecto: " + nombre + " No existe");
	}

	@GetMapping("/gamificacion/proyectos/ver/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public ProyectosGamificacion verGamificacionProyectos(@PathVariable("nombre") String nombre) {
		if (pgRepository.existsByNombre(nombre)) {
			return pgRepository.findByNombre(nombre);
		}
		logger.info("No existe el proyecto: " + nombre);
		return null;
	}

	@PutMapping("/gamificacion/proyectos/habilitar/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public void habilitarProyectosGamificacion(@PathVariable("nombre") String nombre) {
		if (pgRepository.existsByNombre(nombre)) {
			ProyectosGamificacion p = pgRepository.findByNombre(nombre);
			p.setHabilitado(true);
			pgRepository.save(p);
			logger.info("Actualizado correctamente: " + nombre);
		} else
			logger.info("No existe el proyecto: " + nombre);
	}

	@PutMapping("/gamificacion/proyectos/deshabilitar/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deshabilitarProyectosGamificacion(@PathVariable("nombre") String nombre) {
		if (pgRepository.existsByNombre(nombre)) {
			ProyectosGamificacion p = pgRepository.findByNombre(nombre);
			p.setHabilitado(false);
			pgRepository.save(p);
			logger.info("Actualizado correctamente: " + nombre);
		} else
			logger.info("No existe el proyecto: " + nombre);
	}

	@PutMapping("/gamificacion/proyectos/agregar-participante/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean agregarParticipante(@PathVariable("nombre") String nombre, @RequestParam("username") String username)
			throws IOException {
		try {
			if (pgRepository.existsByNombre(nombre)) {
				ProyectosGamificacion p = pgRepository.findByNombre(nombre);
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
			throw new IOException("Error al ver habilitado de proyecto: " + nombre);
		}
	}

	@PutMapping("/gamificacion/proyectos/definir-ganadores/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public void definirGanadoresProyecto(@PathVariable("nombre") String nombre) {
		if (pgRepository.existsByNombre(nombre)) {
			ProyectosGamificacion p = pgRepository.findByNombre(nombre);
			List<String> listaParticipantes = p.getUsuariosParticipantes();
			List<String> listaGanadores = new ArrayList<String>();
			Collections.shuffle(listaParticipantes);
			for (int i = 0; i < p.getGanadores(); i++) {
				listaGanadores.add(listaParticipantes.get(i));
			}
			p.setUsuariosGanadores(listaGanadores);
			p.setHabilitado(false);
			pgRepository.save(p);
			logger.info("Lista ganadores del proyecto: " + nombre);
			nClient.enviarNotificacionGanador(nombre);
		} else
			logger.info("No existe el proyecto: " + nombre);
	}

	@GetMapping("/gamificacion/proyectos/ver-ganadores/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<String> verGanadoresProyecto(@PathVariable("nombre") String nombre) {
		if (pgRepository.existsByNombre(nombre)) {
			return pgRepository.findByNombre(nombre).getUsuariosGanadores();
		}
		logger.info("No existe el proyecto: " + nombre);
		return null;
	}

	@DeleteMapping("/gamificacion/proyectos/eliminar/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean eliminarGamificacionProyecto(@PathVariable("nombre") String nombre) throws IOException {
		try {
			pgRepository.deleteById(pgRepository.findByNombre(nombre).getId());
			return true;
		} catch (Exception e) {
			throw new IOException("Error al eliminar el la gamificacion del proyecto: " + nombre);
		}
	}

	@GetMapping("/gamificacion/proyectos/existe/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean existeGamificacionProyecto(@PathVariable("nombre") String nombre) throws IOException {
		try {
			return pgRepository.existsByNombre(nombre);
		} catch (Exception e) {
			throw new IOException("Error al ver habilitado de proyecto: " + nombre);
		}
	}

	@GetMapping("/gamificacion/proyectos/ver-habilitado/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean verHabilitadoProyecto(@PathVariable("nombre") String nombre) throws IOException {
		try {
			return pgRepository.findByNombre(nombre).getHabilitado();
		} catch (Exception e) {
			throw new IOException("Error al ver habilitado de proyecto: " + nombre);
		}
	}

	@PostMapping("/gamificacion/arreglar/")
	@ResponseStatus(code = HttpStatus.OK)
	public void arreglarGamificacion() {
		pgRepository.deleteAll();
	}

}
