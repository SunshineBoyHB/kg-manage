package org.dml.backend.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dml.backend.service.IntegrateService;
import org.dml.backend.util.KgResponseBody;
import org.dml.core.cluster.ClusterDetailInfo;
import org.dml.core.cluster.ClusterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/clusters")
public class CluserInfoController {

	@Autowired
	private IntegrateService integrateService;

	@RequestMapping(method = RequestMethod.GET, consumes = { "application/json;charset=UTF-8" }, produces = {
			"application/json;charset=UTF-8" })
	public KgResponseBody<List<ClusterInfo>> clusters(HttpServletResponse response) {
		List<ClusterInfo> clusterInfos = integrateService.getClusterInfo();
		if(clusterInfos != null) {
			return new KgResponseBody<List<ClusterInfo>>(200, "OK", clusterInfos);
		}
		else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new KgResponseBody<List<ClusterInfo>>(500, "Not found Resource", null);
		} 
	}

	@RequestMapping(value = "/{hostName}", method = RequestMethod.GET, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public KgResponseBody<ClusterDetailInfo> cluster(@PathVariable String hostName, HttpServletResponse response) {
		ClusterDetailInfo clusterDetailInfo = integrateService.getClusterDetailInfo(hostName);
		if(clusterDetailInfo != null) {
			return new KgResponseBody<ClusterDetailInfo>(200, "OK", clusterDetailInfo);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new KgResponseBody<ClusterDetailInfo>(500, "Not Found Resource", null);
		}
	}

}
