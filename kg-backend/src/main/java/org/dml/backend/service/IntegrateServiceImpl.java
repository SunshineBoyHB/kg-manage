package org.dml.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dml.backend.repository.MetaInfoRepo;
import org.dml.backend.util.IntegrateServiceProxy;
import org.dml.core.cluster.ClusterDetailInfo;
import org.dml.core.cluster.ClusterInfo;
import org.dml.core.matchgraph.Entity;
import org.dml.core.matchgraph.InterMatchResult;
import org.dml.core.matchgraph.MatchGraph;
import org.dml.core.matchgraph.Relationship;
import org.dml.core.querygraph.QueryGraph;
import org.dml.core.stwig.STwig;
import org.dml.server.service.GraphDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RMI集成服务实现类
 * 
 * @author HuangBo
 *
 */
@Component
public class IntegrateServiceImpl implements IntegrateService {

	/**
	 * RMI集成服务代理
	 */
	@Autowired
	private IntegrateServiceProxy serviceProxy;
	
	/**
	 * 图谱元数据仓库句柄，用于更新图谱元数据
	 */
	@Autowired
	private MetaInfoRepo metaInfoRepo;

	public IntegrateServiceImpl() { }

	@Override
	public List<ClusterInfo> getClusterInfo() {
		//获取集群概览信息
		List<ClusterInfo> clusterInfos = new ArrayList<>();
		for(String host : serviceProxy.getServiceProxys().keySet()) {
			GraphDataService graphService = serviceProxy.getGraphServiceByHost(host);
			Long entityNum = graphService.EntityNum();
			Long relaNum = graphService.relaNum();
			ClusterInfo clusterInfo = new ClusterInfo(host, entityNum, relaNum);
			clusterInfos.add(clusterInfo);
			
			//在缓存中更新集群概览信息
			metaInfoRepo.addClusterInfo(clusterInfo);
		}
		
		return clusterInfos;
	}
	
	@Override
	public ClusterDetailInfo getClusterDetailInfo(String host) {
		//获取集群详细信息
		GraphDataService graphDataService = serviceProxy.getGraphServiceByHost(host);
		return graphDataService.getClusterDetailInfo();
	}

	private List<InterMatchResult> stwigLabelMatch(STwig sTwig) {
		List<InterMatchResult> interResults = new ArrayList<>();

		// get sub query match result from every host
		for (GraphDataService rmiServer : serviceProxy.getServiceProxys().values()) {
			List<InterMatchResult> interResultsForHost = rmiServer.stwigLabelMatch(sTwig);
			interResults.addAll(interResultsForHost);
		}

		return interResults;
	}

	private List<MatchGraph> interResultsMerge(List<List<InterMatchResult>> subQueriesResults) {
		// validate initial condition
		if (subQueriesResults == null || subQueriesResults.size() <= 0)
			return null;

		List<MatchGraph> matchGraphs = new ArrayList<>();
		int subQueryNum = subQueriesResults.size();
		MatchGraph matchGraph = new MatchGraph();
		recursiveMerge(0, subQueryNum, matchGraph, matchGraphs, subQueriesResults);

		return matchGraphs;
	}

	private void recursiveMerge(int curDepth, int maxDepth, MatchGraph matchGraph, List<MatchGraph> matchGraphs,
			List<List<InterMatchResult>> subQueriesResults) {
		// max depth return result;
		if (curDepth == maxDepth) {
			MatchGraph copyResult = matchGraph.copy();
			matchGraphs.add(copyResult);
			return;
		}

		// first depth should initialize matchGraph
		if (curDepth == 0) {
			List<InterMatchResult> firstInterResults = subQueriesResults.get(0);
			for (InterMatchResult firstInterResult : firstInterResults) {
				matchGraph.addSubResult(firstInterResult);
				recursiveMerge(curDepth + 1, maxDepth, matchGraph, matchGraphs, subQueriesResults);
			}
		}

		// traversal by layer by layer
		List<InterMatchResult> interResults = subQueriesResults.get(curDepth);
		for (InterMatchResult interResult : interResults) {
			if (matchGraph.tryMerge(interResult)) {
				// recursive merge
				recursiveMerge(curDepth + 1, maxDepth, matchGraph, matchGraphs, subQueriesResults);
				// back to previous state
				matchGraph.removeSubResult(interResult);
			}
		}
	}

	@Override
	public List<MatchGraph> graphMatch(QueryGraph queryGraph) {
		// decompose query graph
		List<STwig> subQueries = queryGraph.decompGraph();

		// sub queries match
		List<List<InterMatchResult>> subQueriesResults = new ArrayList<>();
		for (STwig sTwig : subQueries) {
			// get every sub query match result
			List<InterMatchResult> subQueryResults = stwigLabelMatch(sTwig);
			subQueriesResults.add(subQueryResults);
		}

		// merge sub queries match result
		List<MatchGraph> matchGraphs = interResultsMerge(subQueriesResults);

		return matchGraphs;
	}

	@Override
	public boolean createEntity(String host, Entity crtEntity) {
		GraphDataService graphSrv = serviceProxy.getGraphServiceByHost(host);
		return graphSrv.createEntity(crtEntity);
	}

	@Override
	public boolean updateEntity(String host, Entity srcEntity, Entity dstEntity) {
		GraphDataService graphSrv = serviceProxy.getGraphServiceByHost(host);
		return graphSrv.updateEntity(srcEntity, dstEntity);
	}

	@Override
	public boolean deleteEntity(String host, String entityId) {
		GraphDataService graphSrv = serviceProxy.getGraphServiceByHost(host);
		return graphSrv.deleteEntity(entityId);
	}

	@Override
	public boolean createRela(String host, Relationship crtRela) {
		GraphDataService graphSrv = serviceProxy.getGraphServiceByHost(host);
		return graphSrv.createRela(crtRela);
	}

	@Override
	public boolean updateRela(String host, Relationship srcRela, Relationship dstRela) {
		GraphDataService graphSrv = serviceProxy.getGraphServiceByHost(host);
		return graphSrv.updateRela(srcRela, dstRela);
	}

	@Override
	public boolean deleteRela(String host, String relaId) {
		GraphDataService graphSrv = serviceProxy.getGraphServiceByHost(host);
		return graphSrv.deleteRela(relaId);
	}

}
