package org.dml.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dml.core.matchgraph.Entity;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.neo4j.driver.v1.types.Node;
import org.springframework.stereotype.Component;

/**
 * 实体仓库操作实现类
 * 
 * @author HuangBo
 *
 */
@Component
public class EntityRepositoryImpl implements EntityRepository, AutoCloseable {

	private final String idDes = "identifier";
	private final Driver driver;

	public EntityRepositoryImpl(Driver driver) {
		this.driver = driver;
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	private boolean createEntityFunc(Transaction tx, final Entity crtEntity) {
		StringBuilder strBuilder = new StringBuilder();
		// check if exist
		if (isContainEntity(crtEntity.getIdentifier())) {
			System.err.println("creat entity failed for:already exist!!");
			return false;
		}
		strBuilder.append("MERGE (entity");
		// add label
		if (crtEntity.getLabels() != null && crtEntity.getLabels().size() > 0) {
			for (String label : crtEntity.getLabels()) {
				strBuilder.append(":" + label);
			}
		}
		// add identifier
		strBuilder.append("{" + this.idDes + ":\'" + crtEntity.getIdentifier() + "\'");
		// add attributes
		if (crtEntity.getAttributes() != null && crtEntity.getAttributes().size() > 0) {
			for (Map.Entry<String, String> entry : crtEntity.getAttributes().entrySet()) {
				strBuilder.append("," + entry.getKey() + ":\'" + entry.getValue() + "\'");
			}
		}
		strBuilder.append("})");
		String cypher = strBuilder.toString();
		// System.out.println(cypher);
		tx.run(cypher);
		return true;
	}

	@Override
	public boolean createEntity(final Entity crtEntity) {
		try {
			try (Session session = driver.session()) {
				boolean isCreat = session.writeTransaction(new TransactionWork<Boolean>() {
					@Override
					public Boolean execute(Transaction tx) {
						boolean isCreat = createEntityFunc(tx, crtEntity);
						return isCreat;
					}
				});
				return isCreat;
			}
		} catch (Exception e) {
			System.err.println("create node failed for:" + e.getMessage());
			return false;
		}

	}

	private void updateEntityFunc(Transaction tx, final Entity srcEntity, final Entity dstEntity) {
		// clean source information
		StringBuilder srcStrBuilder = new StringBuilder();
		srcStrBuilder.append("MATCH (entity{" + this.idDes + ":\'" + srcEntity.getIdentifier() + "\'}) ");
		if (srcEntity.getLabels() != null && srcEntity.getLabels().size() > 0) {
			srcStrBuilder.append("REMOVE entity");
			for (String label : srcEntity.getLabels()) {
				srcStrBuilder.append(":" + label);
			}
		}
		srcStrBuilder.append(" ");
		srcStrBuilder.append("SET entity = {" + this.idDes + ":\'" + srcEntity.getIdentifier() + "\'}");
		final String srcCypher = srcStrBuilder.toString();
		// System.out.println(srcCypher);
		tx.run(srcCypher);

		// update destination information
		StringBuilder dstStrBuilder = new StringBuilder();
		dstStrBuilder.append("MATCH (entity{" + this.idDes + ":\'" + dstEntity.getIdentifier() + "\'}) ");
		if (dstEntity.getLabels() != null && dstEntity.getLabels().size() > 0) {
			dstStrBuilder.append("SET entity");
			for (String label : dstEntity.getLabels()) {
				dstStrBuilder.append(":" + label);
			}
		}
		if (dstEntity.getAttributes() != null && dstEntity.getAttributes().size() > 0) {
			dstStrBuilder.append(",entity += {" + this.idDes + ":\'" + dstEntity.getIdentifier() + "\'");
			for (Map.Entry<String, String> entry : dstEntity.getAttributes().entrySet()) {
				dstStrBuilder.append("," + entry.getKey() + ":\'" + entry.getValue() + "\'");
			}
			dstStrBuilder.append("}");
		}
		final String dstCypher = dstStrBuilder.toString();
		// System.out.println(dstCypher);
		tx.run(dstCypher);
	}

	@Override
	public boolean updateEntity(final Entity srcEntity, final Entity dstEntity) {
		// srcEntity do not exist
		if (!isContainEntity(srcEntity.getIdentifier())) {
			System.err.println("the node(" + srcEntity.getIdentifier() + ") do no exist");
			return true;
		}
		try {
			try (Session session = driver.session()) {
				session.writeTransaction(new TransactionWork<Void>() {
					@Override
					public Void execute(Transaction tx) {
						updateEntityFunc(tx, srcEntity, dstEntity);
						return null;
					}
				});
			}
			return true;
		} catch (Exception e) {
			System.err.println("update node failed for:" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteEntity(final String entityId) {
		// node do not exist
		if (!isContainEntity(entityId)) {
			System.err.println("the node(" + entityId + ") do not exist");
			return true;
		}
		try {
			try (Session session = driver.session()) {
				session.writeTransaction(new TransactionWork<Void>() {
					@Override
					public Void execute(Transaction tx) {
						String cypher = "MATCH(entity{" + idDes + ":\'" + entityId + "\'})" + "DETACH DELETE entity";
						tx.run(cypher);
						return null;
					}
				});
			}
			return true;
		} catch (Exception e) {
			System.err.println("delete node failed for:" + e.getMessage());
			return false;
		}

	}
	
	@Override
	public String findIdByGraphId(Long graphId) {
		try {
			try(Session session = driver.session()) {
				String identifier = session.readTransaction(new TransactionWork<String>() {
					@Override
					public String execute(Transaction tx) {
						String cypher = "START entity = NODE(" + graphId + ") RETURN entity";
						Node node = tx.run(cypher).single().get("entity").asNode();
						String identifier = node.get(idDes).asString();
						return identifier;
					}
				});
				return identifier;
			}
		} catch (Exception e) {
			System.err.println("get graph node's identifier failed for:" + e.getMessage());
			return null;
		}
		
	}

	@Override
	public Entity findByGraphId(Long graphId) {
		try {
			try (Session session = driver.session()) {
				Entity entity = session.readTransaction(new TransactionWork<Entity>() {
					@Override
					public Entity execute(Transaction tx) {
						String cypher = "START entity = NODE(" + graphId + ") RETURN entity";
						StatementResult st = tx.run(cypher);
						try {
							Node node = st.single().get("entity").asNode();
							// get labels
							List<String> labels = new ArrayList<>();
							for (String label : node.labels()) {
								labels.add(label);
							}
							// get identifier
							String identifier = node.get(idDes).asString();
							// get attributes
							Map<String, String> attributes = new HashMap<>();
							for (String attriKey : node.keys()) {
								if (attriKey.compareToIgnoreCase(idDes) == 0)
									continue;
								String attriValue = node.get(attriKey).asString();
								attributes.put(attriKey, attriValue);
							}
							Entity entity = new Entity(identifier, labels, attributes);
							return entity;
						} catch (Exception e) {
							System.err.println("get graph node by id failed for:" + e.getMessage());
							return null;
						}
					}
				});
				return entity;
			}
		} catch (Exception e) {
			System.err.println("get graph node by id failed for:" + e.getMessage());
			return null;
		}
	}

	private Entity findByIdFunc(Transaction tx, final String identifier) {
		String cypher = "MATCH(entity {" + this.idDes + ":\'" + identifier + "\'}) return entity";
		StatementResult result = tx.run(cypher);
		try {
			Record record = result.single();
			Node node = record.get("entity").asNode();
			// get labels
			List<String> labelList = new ArrayList<>();
			for (String label : node.labels()) {
				labelList.add(label);
			}
			// get attributes
			Map<String, String> attributeMap = new HashMap<>();
			for (String attriKey : node.keys()) {
				String attriValue = node.get(attriKey).asString();
				attributeMap.put(attriKey, attriValue);
			}
			Entity entity = new Entity(identifier, labelList, attributeMap);
			return entity;
		} catch (NoSuchRecordException e) {
			return null;
		} catch (Exception e) {
			System.err.println("retrieve node failed for:" + e.getMessage());
			return null;
		}
	}

	@Override
	public Entity findById(final String identifier) {
		try {
			try (Session session = driver.session()) {
				Entity entity = session.readTransaction(new TransactionWork<Entity>() {
					@Override
					public Entity execute(Transaction tx) {
						return findByIdFunc(tx, identifier);
					}
				});
				return entity;
			}
		} catch (Exception e) {
			System.err.println("find entity failed for:" + e.getMessage());
			return null;
		}

	}

	@Override
	public boolean isContainEntity(final String identifier) {
		try {
			try (Session session = driver.session()) {
				final String cypher = "MATCH(entity {" + this.idDes + ":\'" + identifier + "\'}) "
						+ "RETURN COUNT(entity) > 0 AS isContain";
				boolean isContain = session.readTransaction(new TransactionWork<Boolean>() {
					@Override
					public Boolean execute(Transaction tx) {
						StatementResult result = tx.run(cypher);
						try {
							boolean isConatin = result.single().get("isContain").asBoolean();
							return isConatin;
						} catch (NoSuchRecordException e) {
							return false;
						} catch (Exception e) {
							System.err.println("check node if exist error for:" + e.getMessage());
							return false;
						}
					}
				});
				return isContain;
			}
		} catch (Exception e) {
			System.err.println("check node if exist failed for" + e.getMessage());
			return false;
		}
	}

	@Override
	public List<Entity> findByLabels(List<String> labels) {
		return findByLablesAndAttibutes(labels, null);
	}

	@Override
	public List<Entity> findByAttributes(Map<String, String> attributes) {
		return findByLablesAndAttibutes(null, attributes);
	}

	@Override
	public List<Entity> findByLablesAndAttibutes(List<String> labels, Map<String, String> attributes) {
		try {
			try (Session session = driver.session()) {
				List<Entity> result = session.readTransaction(new TransactionWork<List<Entity>>() {
					@Override
					public List<Entity> execute(Transaction tx) {
						List<Entity> result = findFunc(tx, labels, attributes);
						return result;
					}
				});
				return result;
			}
		} catch (Exception e) {
			System.err.println("retrieve gragh node failed for:" + e.getMessage());
			return null;
		}
	}

	private List<Entity> findFunc(Transaction tx, List<String> labels, Map<String, String> attributes) {
		List<Entity> result = new ArrayList<>();
		// construct cypher
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("MATCH (entity");
		if (labels != null && labels.size() > 0) {
			for (String label : labels) {
				strBuilder.append(":" + label);
			}
		}
		if (attributes != null && attributes.size() > 0) {
			strBuilder.append(" {");
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				strBuilder.append(entry.getKey() + ":\'" + entry.getValue() + "\',");
			}
			// delete the last commas
			strBuilder.deleteCharAt(strBuilder.length() - 1);
			strBuilder.append("}");
		}
		strBuilder.append(") RETURN DISTINCT entity." + this.idDes + ",entity;");
		final String cypher = strBuilder.toString();
		// System.out.println(cypher);
		// get result
		StatementResult st = tx.run(cypher);
		try {
			while (st.hasNext()) {
				Entity entity = new Entity();
				Record record = st.next();
				// get entity identifier
				String entityIdentifier = record.get("entity." + this.idDes).asString();
				entity.setIdentifier(entityIdentifier);
				// get node
				Node node = record.get("entity").asNode();
				// get labels information
				List<String> entityLabels = new ArrayList<>();
				for (String entityLabel : node.labels()) {
					entityLabels.add(entityLabel);
				}
				entity.setLabels(entityLabels);
				// get attibutes information(exclude identifier)
				Map<String, String> entityAttributes = new HashMap<>();
				for (String attriKey : node.keys()) {
					if (attriKey.equals(this.idDes))
						continue;
					String attriValue = node.get(attriKey).asString();
					entityAttributes.put(attriKey, attriValue);
				}
				entity.setAttributes(entityAttributes);
				// add entity to result
				result.add(entity);
			}
			return result;
		} catch (Exception e) {
			System.err.println("retrieve entity failed for:" + e.getMessage());
			return null;
		}

	}

}
