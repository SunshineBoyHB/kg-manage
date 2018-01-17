package org.dml.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dml.core.cluster.ClusterDetailInfo;
import org.dml.core.cluster.ClusterInfo;
import org.dml.core.cluster.DbIndexsInfo;
import org.dml.core.matchgraph.Entity;
import org.dml.core.matchgraph.InterMatchResult;
import org.dml.core.matchgraph.Relationship;
import org.dml.core.stwig.Direction;
import org.dml.core.stwig.LeafNode;
import org.dml.core.stwig.RootNode;
import org.dml.core.stwig.STwig;
import org.dml.server.repository.EntityRepository;
import org.dml.server.repository.RelationshipRepository;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.types.Node;
import org.springframework.stereotype.Component;

/**
 * 图操作实现类
 * 
 * @author HuangBo
 *
 */
@Component
public class GraphDataServiceImpl implements GraphDataService {

	private final String identifer = "identifier";
	private final Driver driver;
	private final EntityRepository entityRepo;
	private final RelationshipRepository relaRepo;

	public GraphDataServiceImpl(Driver driver, EntityRepository entityRepo, RelationshipRepository relaRepo) {
		this.driver = driver;
		this.entityRepo = entityRepo;
		this.relaRepo = relaRepo;
	}

	@Override
	public String hello(String msg) {
		return "Hello " + msg;
	}
	
	public Long EntityNum() {
		try {
			try(Session session = driver.session()) {
				long entityNum = session.readTransaction(new TransactionWork<Long>() {
					@Override
					public Long execute(Transaction tx) {
						String cypher = "MATCH (n) RETURN　count(DISTINCT n) AS entityNum";
						Long entityNum = tx.run(cypher).single().get("entityNum").asLong();
						return entityNum;
					}
				});
				return entityNum;
			}
		} catch (Exception e) {
			System.err.println("get entity number failed for:" + e.getMessage());
			return null;
		}
	}
	
	public Long relaNum() {
		try {
			try(Session session = driver.session()) {
				long relaNum = session.readTransaction(new TransactionWork<Long>() {
					@Override
					public Long execute(Transaction tx) {
						String cypher = "MATCH ()-[r]-() RETURN　count(DISTINCT r) AS relaNum";
						Long relaNum = tx.run(cypher).single().get("relaNum").asLong();
						return relaNum;
					}
				});
				return relaNum;
			}
		} catch (Exception e) {
			System.err.println("get relationship number failed for:" + e.getMessage());
			return null;
		}
	}
	
	public List<String> dbLabels() {
		try {
			try(Session session = driver.session()) {
				List<String> labels = session.readTransaction(new TransactionWork<List<String>>() {
					@Override
					public List<String> execute(Transaction tx) {
						String cypher = "CALL db.labels() YIELD label";
						List<String> labels = new ArrayList<>();
						StatementResult result = tx.run(cypher);
						while(result.hasNext()) {
							String label = result.next().get("label").asString();
							labels.add(label);
						}
						return labels;
					}
				});
				return labels;
			}
		} catch (Exception e) {
			System.out.println(e.getClass().getName() + ":" + e.getMessage());
			return null;
		}
		
	}
	
	public List<String> dbPropertyKeys() {
		try {
			try(Session session = driver.session()) {
				List<String> propertyKeys = session.readTransaction(new TransactionWork<List<String>>() {
					@Override
					public List<String> execute(Transaction tx) {
						String cypher = "CALL db.propertyKeys YIELD propertyKey";
						List<String> propertyKeys = new ArrayList<>();
						StatementResult result = tx.run(cypher);
						while(result.hasNext()) {
							Record record = result.next();
							String propertyKey = record.get("propertyKey").asString();
							propertyKeys.add(propertyKey);
						}
						return propertyKeys;
					}
				});
				return propertyKeys;
			}
		} catch (Exception e) {
			System.err.println("get graph property key failed for:" + e.getMessage());
			return null;
		}
	}
	
	public List<String> dbRelaTypes() {
		try {
			try(Session session = driver.session()) {
				List<String> relaTypes = session.readTransaction(new TransactionWork<List<String>>() {
					@Override
					public List<String> execute(Transaction tx) {
						String cypher = "CALL db.relationshipTypes YIELD relationshipType";
						List<String> relaTypes = new ArrayList<>();
						StatementResult result = tx.run(cypher);
						while(result.hasNext()) {
							Record record = result.next();
							String relaType = record.get("relationshipType").asString();
							relaTypes.add(relaType);
						}
						return relaTypes;
					}
				});
				return relaTypes;
			}
		} catch (Exception e) {
			System.err.println("get graph relationship types failed for:" + e.getMessage());
			return null;
		}
	}
	
	public List<DbIndexsInfo> dbIndexs() {
		try {
			try(Session session = driver.session()) {
				List<DbIndexsInfo> dbIndexs = session.readTransaction(new TransactionWork<List<DbIndexsInfo>>() {
					@Override
					public List<DbIndexsInfo> execute(Transaction tx) {
						String cypher = "CALL db.indexes YIELD description, state, type";
						List<DbIndexsInfo> dbIndexs = new ArrayList<>();
						StatementResult result = tx.run(cypher);
						while(result.hasNext()) {
							Record record = result.next();
							String indexDes = record.get("description").asString();
							String indexState = record.get("state").asString();
							String indexType = record.get("type").asString();
							DbIndexsInfo dbIndex = new DbIndexsInfo(indexDes, indexState, indexType);
							dbIndexs.add(dbIndex);
						}
						return dbIndexs;
					}
				});
				return dbIndexs;
			}
		} catch (Exception e) {
			System.err.println("get graph index failed for:" + e.getMessage());
			return null;
		}
	}
	
	@Override
	public ClusterDetailInfo getClusterDetailInfo() {
		long entityNum = EntityNum();
		long relaNum = relaNum();
		List<String> entityLabels = dbLabels();
		List<String> relaLabels = dbRelaTypes();
		List<String> propertyKeys = dbPropertyKeys();
		List<DbIndexsInfo> indexs = dbIndexs();
		
		return new ClusterDetailInfo(null, entityNum, relaNum, entityLabels, relaLabels, propertyKeys, indexs);
	}
	

	private List<InterMatchResult> stwigLabelMatchFunc(Transaction tx, STwig stwig) {
		RootNode rootNode = stwig.getRoot();
		List<LeafNode> leafNodes = stwig.getLeaves();
		StringBuilder strBuilder = new StringBuilder("MATCH ");

		// construct root node condition
		String rootVar = "root";
		strBuilder.append("(" + rootVar);
		for (String rootLabel : rootNode.getNodeLabels()) {
			strBuilder.append(":" + rootLabel);
		}
		strBuilder.append(")");

		// construct leaves condition
		for (LeafNode leafNode : leafNodes) {
			String leafVar = "leaf" + leafNode.getQueryId();
			strBuilder.append(", (" + leafVar);
			for (String leafLabel : leafNode.getNodeLabels()) {
				strBuilder.append(":" + leafLabel);
			}
			strBuilder.append(")");
		}

		// construct edge condition
		strBuilder.append(" WHERE ");
		for (LeafNode leafNode : leafNodes) {
			String leafVar = "leaf" + leafNode.getQueryId();
			if (leafNode.getDirection() == Direction.STWIG_EDGE_FORWARD) { // edge forward
				strBuilder.append("(" + rootVar + ")-[:" + leafNode.getEdgeLabel() + "]->(" + leafVar + ") AND ");
			} else { // edge reverse
				strBuilder.append("(" + rootVar + ")<-[:" + leafNode.getEdgeLabel() + "]->(" + leafVar + ") AND ");
			}
		}
		// remove redundant "AND"
		strBuilder.delete(strBuilder.length() - 4, strBuilder.length());

		// construct return syntax
		strBuilder.append("RETURN DISTINCT " + rootVar);
		for (LeafNode leafNode : leafNodes) {
			String leafVar = "leaf" + leafNode.getQueryId();
			strBuilder.append(", " + leafVar);
		}
		strBuilder.append(";");

		// get cypher
		String cypher = strBuilder.toString();
		// System.out.println(cypher);

		// execute cypher
		List<InterMatchResult> interResults = new ArrayList<>();
		try {
			StatementResult st = tx.run(cypher);
			while (st.hasNext()) {
				Record record = st.next();
				InterMatchResult interResult = new InterMatchResult();
				// get RootNode mapping
				int rootqueryId = rootNode.getQueryId();
				Node rootMapNode = record.get(rootVar).asNode();
				String rootMapId = rootMapNode.get(identifer).asString();
				interResult.addMapping(rootqueryId, rootMapId);
				// get LeafNode mapping
				for (LeafNode leafNode : leafNodes) {
					int queryId = leafNode.getQueryId();
					String leafVar = "leaf" + queryId;
					Node node = record.get(leafVar).asNode();
					String entityId = node.get(this.identifer).asString();
					// String entityId = String.valueOf(node.get("id").asInt());
					interResult.addMapping(queryId, entityId);
				}
				interResults.add(interResult);
			}
			return interResults;
		} catch (Exception e) {
			System.err.println("STwig laebl match failed for:" + e.getMessage());
			return null;
		}

	}

	@Override
	public List<InterMatchResult> stwigLabelMatch(STwig stwig) {
		try {
			try (Session session = driver.session()) {
				List<InterMatchResult> interMatchResults = session
						.readTransaction(new TransactionWork<List<InterMatchResult>>() {
							@Override
							public List<InterMatchResult> execute(Transaction tx) {
								return stwigLabelMatchFunc(tx, stwig);
							}
						});
				return interMatchResults;
			}
		} catch (Exception e) {
			System.err.println("STwig laebl match failed for:" + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean createEntity(Entity crtEntity) {
		return entityRepo.createEntity(crtEntity);
	}

	@Override
	public boolean updateEntity(Entity srcEntity, Entity dstEntity) {
		return entityRepo.updateEntity(srcEntity, dstEntity);
	}

	@Override
	public boolean deleteEntity(String entityId) {
		return entityRepo.deleteEntity(entityId);
	}

	@Override
	public boolean isContainEntity(String entityId) {
		return entityRepo.isContainEntity(entityId);
	}

	@Override
	public Entity findEntityByGraphId(long graphId) {
		return entityRepo.findByGraphId(graphId);
	}

	@Override
	public Entity findEntityByEntityId(String entityId) {
		return entityRepo.findById(entityId);
	}

	@Override
	public List<Entity> findEntityByLabels(List<String> labels) {
		return entityRepo.findByLabels(labels);
	}

	@Override
	public List<Entity> findEntityByAttributes(Map<String, String> attributes) {
		return entityRepo.findByAttributes(attributes);
	}

	@Override
	public List<Entity> findEntityByLabelsAndAttributes(List<String> labels, Map<String, String> attributes) {
		return entityRepo.findByLablesAndAttibutes(labels, attributes);
	}

	@Override
	public boolean createRela(Relationship crtRela) {
		return relaRepo.createRelationship(crtRela);
	}

	@Override
	public boolean updateRela(Relationship srcRela, Relationship dstRela) {
		return relaRepo.updateRelationship(srcRela, dstRela);
	}

	@Override
	public boolean deleteRela(String relaId) {
		return relaRepo.deleteRelationship(relaId);
	}

	@Override
	public boolean isContainRela(String relaId) {
		return relaRepo.isContainRelationship(relaId);
	}

	@Override
	public Relationship findRelaByRelaId(String relaId) {
		return relaRepo.findById(relaId);
	}

	@Override
	public List<Relationship> findRelaByLabel(String label) {
		return relaRepo.findyByLabels(label);
	}

	@Override
	public List<Relationship> findRelaByAttributes(Map<String, String> attributes) {
		return relaRepo.findByAttributes(attributes);
	}

	@Override
	public List<Relationship> findyRelaByLabelAndAttibutes(String label, Map<String, String> attributes) {
		return relaRepo.findByLabelsAndAttibutes(label, attributes);
	}

	@Override
	public List<Relationship> findRelaByStartEntity(Entity startEntity) {
		return relaRepo.findByStartEntity(startEntity);
	}

	@Override
	public List<Relationship> findRelaByEndEntity(Entity endEntity) {
		return relaRepo.findByEndEntity(endEntity);
	}

	@Override
	public List<Relationship> findRelaByStartAndEntity(Entity startEntity, Entity endEntity) {
		return relaRepo.findByStartAndEnd(startEntity, endEntity);
	}

}
