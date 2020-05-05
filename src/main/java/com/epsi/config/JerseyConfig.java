package com.epsi.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.epsi.webservice.DataWebService;


@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig{

	public JerseyConfig() {
		register(DataWebService.class);
	}
}
