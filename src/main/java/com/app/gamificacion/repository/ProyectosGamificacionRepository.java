package com.app.gamificacion.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.gamificacion.models.ProyectosGamificacion;

public interface ProyectosGamificacionRepository extends MongoRepository<ProyectosGamificacion, String> {

	@RestResource(path = "buscar-codigo")
	public ProyectosGamificacion findByIdProyecto(@Param("idProyecto") Integer idProyecto);

	@RestResource(path = "exist-codigo")
	public Boolean existsByIdProyecto(@Param("idProyecto") Integer idProyecto);

	@RestResource(path = "delete-codigo")
	public Boolean deleteByIdProyecto(@Param("idProyecto") Integer idProyecto);

}
