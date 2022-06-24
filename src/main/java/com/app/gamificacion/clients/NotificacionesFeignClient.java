package com.app.gamificacion.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "app-notificaciones")
public interface NotificacionesFeignClient {

	@PutMapping("/notificaciones/gamificacion/proyecto/notificacion-ganadores/")
	public void enviarNotificacionGanador(@RequestParam("nombre") String nombre);

}
