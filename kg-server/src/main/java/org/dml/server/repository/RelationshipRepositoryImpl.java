package org.dml.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dml.core.matchgraph.Entity;
import org.dml.core.matchgraph.Relationship;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.springframework.stereotype.Component;

/**
 * 关系仓库操作实现类
 * 
 * @author HuangBo
 *
 */
@Component
public class RelationshipRepositoryImpl implements RelationshipRepository, AutoCloseable {

	private final String IdDes = "identifier";
	private final Driver driver;
	private final EntityRepository entityRepo;

	public RelationshipRepositoryImpl(Driver driver, EntityRepository entityRepo) {
		this.driver = driver;
		this.entityRepo = entityRepo;
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	@Override
	public boolean createRelationship(Relationship relationship) {
		try {
			try (Session session = driver.session()) {
				boolean isCreat = session.writeTransaction(new TransactionWork<Boolean>() {
					@Override
					public Boolean execute(Transaction tx) {
						return createRelaFunc(tx, relationship);
					}
				});
				return isCreat;
			}
		} catch (Exception e) {
			System.err.println("add realtionship to database failed for:" + e.getMessage());
			return false;
		}
	}

	private boolean createRelaFunc(Transaction tx, final Relationship relationship) {
		StringBuilder strBuilder = new StringBuilder();
		// check identifier if exist
		if (isContainRelationship(relationship.getIdentifier())) {
			System.err.println("creat relationship failed for:identifier is not unique");
			return false;
		}
		// check source node and destination node if exist
		if (!entityRepo.isContainEntity(relationship.getStart())
				|| !entityRepo.isContainEntity(relationship.getEnd())) {
			System.err.println("creat relationship failed for:relationship source node or end node do not exist");
			return false;
		}
		// construct cypher
		String srcNodeId = relationship.getStart();
		String dstNodeId = relationship.getEnd();
		strBuilder.append("MATCH (start{" + this.IdDes + ":\'" + srcNodeId + "\'})," + "(end{" + this.IdDes + ":\'"
				+ dstNodeId + "\'}) ");
		strBuilder.append("CREATE (start)-[rela");
		if (relationship.getLabel() != null) {
			strBuilder.append(":" + relationship.getLabel() + " ");
		}
		strBuilder.append("{" + this.IdDes + ":\'" + relationship.getIdentifier() + "\'");
		if (relationship.getAttributes() != null && relationship.getAttributes().size() > 0) {
			for (Map.Entry<String, String> entry : relationship.getAttributes().entrySet()) {
				strBuilder.append("," + entry.getKey() + ":\'" + entry.getValue() + "\'");
			}
		}
		strBuilder.append("}]->(end);");
		final String cypher = strBuilder.toString();
		// System.out.println(cypher);
		tx.run(cypher);
		return true;
	}

	@Override
	public boolean updateRelationship(Relationship srcRela, Relationship dstRela) {
		try {
			try (Session session = driver.session()) {
				boolean isUpdate = session.writeTransaction(new TransactionWork<Boolean>() {
					@Override
					public Boolean execute(Transaction tx) {
						boolean isUpdate = updateRelaFunc(tx, srcRela, dstRela);
						return isUpdate;
					}
				});
				return isUpdate;
			}
		} catch (Exception e) {
			System.err.println("update relationship failed for:" + e.getMessage());
			return false;
		}
	}

	private boolean updateRelaFunc(Transaction tx, Relationship srcRela, Relationship dstRela) {
		// check source relation if exist
		if (!isContainRelationship(srcRela.getIdentifier())) {
			System.err.println("update  relationship failed for:source relationship don't exist");
			return false;
		}
		if (!deleteRelationship(srcRela.getIdentifier())) {
			System.err.println("update relationship failed for:delete source relationship");
			return false;
		}
		if (!createRelationship(dstRela)) {
			System.err.println("update relationship failed for:create destination relationship");
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteRelationship(String relationshipId) {
		try {
			try (Session session = driver.session()) {
				session.readTransaction(new TransactionWork<Void>() {
					@Override
					public Void execute(Transaction tx) {
						String cypher = "MATCH ()-[rela{" + IdDes + ":\'" + relationshipId + "\'}]->()"
								+ " DETACH DELETE rela;";
						tx.run(cypher);
						return null;
					}
				});
				return true;
			}
		} catch (Exception e) {
			System.err.println("check relationship if exist failed");
			return false;
		}
	}

	@Override
	public boolean isContainRelationship(String relationshipId) {
		try {
			try (Session session = driver.session()) {
				boolean isExist = session.readTransaction(new TransactionWork<Boolean>() {
					@Override
					public Boolean execute(Transaction tx) {
						String cypher = "MATCH ()-[rela{" + IdDes + ":\'" + relationshipId + "\'}]->() "
								+ "RETURN COUNT(rela) > 0 AS isContain;";
						StatementResult st = tx.run(cypher);
						try {
							return st.single().get("isContain").asBoolean();
						} catch (NoSuchRecordException e) {
							return false;
						} catch (Exception e) {
							System.err.println("check relationship if exist failed for:" + e.getMessage());
							return false;
						}
					}
				});
				return isExist;
			}
		} catch (Exception e) {
			System.err.println("check relationship if exist failed");
			return false;
		}
	}

	@Override
	public Relationship findById(String identifier) {
		try {
			try (Session session = driver.session()) {
				Relationship rela = session.readTransaction(new TransactionWork<Relationship>() {
					@Override
					public Relationship execute(Transaction tx) {
						Relationship rela = findByIdFunc(tx, identifier);
						return rela;
					}
				});
				return rela;
			}
		} catch (Exception e) {
			System.err.println("retrieve relationship failed for:" + e.getMessage());
			return null;
		}
	}

	private Relationship findByIdFunc(Transaction tx, String identifier) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("MATCH () -[rela{" + this.IdDes + ":\'" + identifier + "\'}]->() " + "RETURN DISTINCT rela;");
		String cypher = strBuilder.toString();
		// System.out.println(cypher);
		StatementResult st = tx.run(cypher);
		try {
			org.neo4j.driver.v1.types.Relationship rela = st.single().get("rela").asRelationship();
			// get relationship label
			String relaLabel = rela.type();
			// get relationship attributes
			Map<String, String> relaAttributes = new HashMap<>();
			for (String relaAttriKey : rela.keys()) {
				if (relaAttriKey.compareToIgnoreCase(IdDes) == 0)
					continue;
				String relaAttriValue = rela.get(relaAttriKey).asString();
				relaAttributes.put(relaAttriKey, relaAttriValue);
			}
			// get source node and destination node
			long srcNode = rela.startNodeId();
			long dstNodeId = rela.endNodeId();
			String start = entityRepo.findIdByGraphId(srcNode);
			String end = entityRepo.findIdByGraphId(dstNodeId);
			// construct relationship
			Relationship result = new Relationship(identifier, relaLabel, relaAttributes, start, end);
			return result;
		} catch (NoSuchRecordException e) {
			return null;
		} catch (Exception e) {
			System.err.println("retrieve relationship failed for:" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<Relationship> findyByLabels(String label) {
		return findByLabelsAndAttibutes(label, null);
	}

	@Override
	public List<Relationship> findByAttributes(Map<String, String> attributes) {
		return findByLabelsAndAttibutes(null, attributes);
	}

	@Override
	public List<Relationship> findByLabelsAndAttibutes(String label, Map<String, String> attributes) {
		try {
			try (Session session = driver.session()) {
				List<Relationship> relationships = session.readTransaction(new TransactionWork<List<Relationship>>() {
					@Override
					public List<Relationship> execute(Transaction tx) {
						List<Relationship> relationships = findByLabelAndAttriFunc(tx, label, attributes);
						return relationships;
					}
				});
				return relationships;
			}
		} catch (Exception e) {
			System.err.println("retieve relationship failed for:" + e.getMessage());
			return null;
		}
	}

	private List<Relationship> findByLabelAndAttriFunc(Transaction tx, String label, Map<String, String> attributes) {
		List<Relationship> relationships = new ArrayList<>();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("MATCH ()-[rela");
		if (label != null)
			strBuilder.append(":" + label);
		if (attributes != null && attributes.size() > 0) {
			strBuilder.append("{");
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				strBuilder.append(entry.getKey() + ":\'" + entry.getValue() + "\',");
			}
			// delete last commas
			strBuilder.deleteCharAt(strBuilder.length() - 1);
			strBuilder.append("}");
		}
		strBuilder.append("]->() RETURN DISTINCT rela;");
		String cypher = strBuilder.toString();
		// System.out.println(cypher);
		try {
			StatementResult st = tx.run(cypher);
			while (st.hasNext()) {
				org.neo4j.driver.v1.types.Relationship record = st.next().get("rela").asRelationship();
				// get label
				String relaLabel = record.type();
				// get identifier(not null)
				String identifier = record.get(this.IdDes).asString();
				// get attributes
				Map<String, String> relaAtttibutes = new HashMap<>();
				for (String attriKey : record.keys()) {
					if (attriKey.compareToIgnoreCase(IdDes) == 0)
						continue;
					String attriValue = record.get(attriKey).asString();
					relaAtttibutes.put(attriKey, attriValue);
				}
				// get source node and destination node
				long startId = record.startNodeId();
				long endId = record.endNodeId();
				String start = entityRepo.findIdByGraphId(startId);
				String end = entityRepo.findIdByGraphId(endId);
				// construct relationship and add to result
				Relationship relationship = new Relationship(identifier, relaLabel, relaAtttibutes, start, end);
				relationships.add(relationship);
			}
			return relationships;
		} catch (Exception e) {
			System.err.println("retrieve relationship failed for:" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<Relationship> findByStartEntity(Entity start) {
		return findByStartAndEnd(start, null);
	}

	@Override
	public List<Relationship> findByEndEntity(Entity end) {
		return findByStartAndEnd(null, end);
	}

	@Override
	public List<Relationship> findByStartAndEnd(Entity start, Entity end) {
		try {
			try (Session session = driver.session()) {
				List<Relationship> relationships = session.readTransaction(new TransactionWork<List<Relationship>>() {
					@Override
					public List<Relationship> execute(Transaction tx) {
						List<Relationship> relationships = findByStartAndEndFunc(tx, start, end);
						return relationships;
					}
				});
				return relationships;
			}
		} catch (Exception e) {
			System.err.println("retrieve relationships failed for:" + e.getMessage());
			return null;
		}
	}

	private List<Relationship> findByStartAndEndFunc(Transaction tx, Entity start, Entity end) {
		List<Relationship> relationships = new ArrayList<>();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("MATCH (");
		if (start != null)
			strBuilder.append("src{" + this.IdDes + ":\'" + start.getIdentifier() + "\'}");
		strBuilder.append(")-[rela]->");
		strBuilder.append("(");
		if (end != null)
			strBuilder.append("dst{" + this.IdDes + ":\'" + end.getIdentifier() + "\'}");
		strBuilder.append(") RETURN DISTINCT rela;");
		String cypher = strBuilder.toString();
		// System.out.println(cypher);
		try {
			StatementResult st = tx.run(cypher);
			while (st.hasNext()) {
				org.neo4j.driver.v1.types.Relationship record = st.next().get("rela").asRelationship();
				// get identifier(not null)
				String relaId = record.get(this.IdDes).asString();
				// get label
				String relaLabel = record.type();
				// get attributes
				Map<String, String> relaAttributes = new HashMap<>();
				for (String attriKey : record.keys()) {
					if (attriKey.compareToIgnoreCase(IdDes) == 0)
						continue;
					String attriValue = record.get(attriKey).asString();
					relaAttributes.put(attriKey, attriValue);
				}
				// get source node and destination node
				String relaStart = entityRepo.findIdByGraphId(record.startNodeId());
				String relaEnd = entityRepo.findIdByGraphId(record.endNodeId());
				Relationship relationship = new Relationship(relaId, relaLabel, relaAttributes, relaStart, relaEnd);
				relationships.add(relationship);
			}
			return relationships;
		} catch (Exception e) {
			System.err.println("retrieve relationship failed for:" + e.getMessage());
			return null;
		}
	}

}
