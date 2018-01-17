package org.dml.backend.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dml.backend.service.IntegrateService;
import org.dml.backend.util.KgResponseBody;
import org.dml.core.matchgraph.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/relationship")
public class RelationshipController {

	@Autowired
	private IntegrateService integrateService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = {
	"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public KgResponseBody<List<Relationship>> searchRela(@RequestBody Map<String, Object> requestBody,
			HttpServletResponse response) {
		String label = (String) requestBody.get("label");
		Map<String, String> attributes = (Map<String, String>) requestBody.get("attributes");
		
		return new KgResponseBody<>(200, "OK", null);
	}

}
