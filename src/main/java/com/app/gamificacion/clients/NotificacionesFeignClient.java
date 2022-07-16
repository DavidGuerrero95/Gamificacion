package com.app.gamificacion.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "app-notificaciones")
public interface NotificacionesFeignClient {

	@PutMapping("/notificaciones/gamificacion/proyecto/notificacion-ganadores/")
	public void enviarNotificacionGanador(@RequestParam("nombre") List<String> nombre,
			@RequestParam("mensajeGanador") String mensajeGanador);
}
