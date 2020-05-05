package com.epsi.webservice;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.epsi.entity.MemoryFileUpload;

@Path("/data")
public class DataWebService {

	private MemoryFileUpload memoryFile;
	
	@GET
	@Path("/mem")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataUploaded(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		this.memoryFile = appContext.getBean(MemoryFileUpload.class);
		return Response.ok(memoryFile).build();
	}
	
}
