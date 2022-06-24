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

	@GetMapping("/proyectos/ver/proyecto/{nombre}")
	public Proyectos verProyecto(@PathVariable("nombre") String nombre);

	@GetMapping("/proyectos/existsByNombre")
	public Boolean existNombre(@RequestParam("nombre") String nombre);
	
	@GetMapping("/proyectos/gamificacion/ver-estado/{nombre}")
	public Boolean verEstadoGamificacion(@PathVariable("nombre") String nombre);

}
