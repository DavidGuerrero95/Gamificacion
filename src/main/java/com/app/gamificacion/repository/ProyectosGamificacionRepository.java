package com.app.gamificacion.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.gamificacion.models.ProyectosGamificacion;


public interface ProyectosGamificacionRepository extends MongoRepository<ProyectosGamificacion, String> {
	
	@RestResource(path = "buscar-name")
	public ProyectosGamificacion findByNombre(@Param("nombre") String nombre);
	
	@RestResource(path = "existNombre")
	public Boolean existsByNombre(@Param("nombre") String nombre);

}
