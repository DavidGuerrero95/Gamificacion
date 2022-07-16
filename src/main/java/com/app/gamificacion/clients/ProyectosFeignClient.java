package com.app.gamificacion.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.gamificacion.models.Proyectos;

@FeignClient(name = "app-proyectos")
public interface ProyectosFeignClient {

	@GetMapping("/proyectos/listar/")
	public List<Proyectos> getProyectos();

	@GetMapping("/proyectos/ver/proyecto/{idProyecto}")
	public Proyectos verProyecto(@PathVariable("idProyecto") Integer idProyecto);

	@GetMapping("/proyectos/existsByCodigoProyecto/")
	public Boolean existNombre(@RequestParam("idProyecto") Integer idProyecto);

	@GetMapping("/proyectos/gamificacion/ver-estado/{idProyecto}")
	public Boolean verEstadoGamificacion(@PathVariable("idProyecto") Integer idProyecto);

}
