package org.dml.backend.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dml.backend.service.IntegrateService;
import org.dml.backend.util.KgResponseBody;
import org.dml.core.matchgraph.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/entity")
public class EntityController {

	@Autowired
	private IntegrateService integrateService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public KgResponseBody<List<Entity>> searchEntity(@RequestBody Map<String, Object> requestBody, HttpServletResponse response) {
		List<String> labels = (List<String>) requestBody.get("labels");
		Map<String, String> attributes = (Map<String, String>) requestBody.get("attributes");
		
		System.out.println("requestBody:"
				+ "\n\tlabels=" + labels 
				+ "\n\tattributes=" + attributes);
		
		return new KgResponseBody<List<Entity>>(0, "OK", null);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {
	"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public KgResponseBody<Void> createEntity(@RequestBody Entity entity, HttpServletResponse response) {
		System.out.println("Entity Info:" 
				+ "\n\tid=" + entity.getIdentifier()
				+ "\n\tlabel=" + entity.getLabels()
				+ "\n\tattributes=" + entity.getAttributes());
		
		return new KgResponseBody<Void>(0, "OK", null);
	}
	
	@RequestMapping(value = "/{entityId}", method = RequestMethod.POST, consumes = {
	"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public KgResponseBody<Void> updateEntity(@PathVariable String entityId, @RequestBody Entity entity, HttpServletResponse response) {
		System.out.println("Query Parameters:"
				+ "\n\tentityId=" + entityId
				+ "\n\tentity="
				+ "\n\t\tid=" + entity.getIdentifier()
				+ "\n\t\tlabels=" + entity.getLabels()
				+ "\n\t\tattributes=" + entity.getAttributes());
		
		return new KgResponseBody<Void>(200, "OK", null);
	}
	
	@RequestMapping(value = "/{entityId}", method = RequestMethod.DELETE, consumes = {
	"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public KgResponseBody<Void> deleteEntity(@PathVariable String entityId, HttpServletResponse response) {
		System.out.println("Query Parameters:" + "\n\tentityId=" + entityId);
		
		return new KgResponseBody<Void>(200, "OK", null);
	}

}
