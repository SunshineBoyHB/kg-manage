package org.dml.server.repository;

import java.util.List;
import java.util.Map;

import org.dml.core.matchgraph.Entity;
import org.dml.core.matchgraph.Relationship;

/**
 * 关系仓库操作接口
 * 
 * @author HuangBo
 *
 */
public interface RelationshipRepository {

	/**
	 * add relationship to graph database
	 * 
	 * @param relationship
	 *            graph edge
	 * @return true if success, or false
	 */
	public boolean createRelationship(final Relationship relationship);

	/**
	 * update the information of the edge
	 * 
	 * @param srcRela
	 *            source graph edge
	 * @param dstRela
	 *            destination graph node
	 * @return true if success, or false
	 */
	public boolean updateRelationship(final Relationship srcRela, final Relationship dstRela);

	/**
	 * delete the edge according to the edge's unique identifier
	 * 
	 * @param relationshipId
	 *            edge's unique description
	 * @return true if success, or false
	 */
	public boolean deleteRelationship(final String relationshipId);

	/**
	 * check if graph database contain the specific edge
	 * 
	 * @param relationshipId
	 *            relationshipId edge's unique description
	 * @return true if exist, or false
	 */
	public boolean isContainRelationship(final String relationshipId);

	/**
	 * search edge by identifier
	 * 
	 * @param identifier
	 *            graph edge's unique filed
	 * @return graph edges
	 */
	public Relationship findById(final String identifier);

	/**
	 * search edge by edge's label
	 * 
	 * @param labels
	 *            edge's label
	 * @return graph edges
	 */
	public List<Relationship> findyByLabels(final String label);

	/**
	 * search edge by edge's attibutes
	 * 
	 * @param attibutes
	 *            edge's attributes
	 * @return graph edges
	 */
	public List<Relationship> findByAttributes(final Map<String, String> attibutes);

	/**
	 * search edge by edge's label and attributes
	 * 
	 * @param labels
	 *            edge's label
	 * @param attributes
	 *            edge's attributes
	 * @return graph edges
	 */
	public List<Relationship> findByLabelsAndAttibutes(final String label, final Map<String, String> attributes);

	/**
	 * search edge by edge source node
	 * 
	 * @param start
	 *            source node of the edge
	 * @return graph edges
	 */
	public List<Relationship> findByStartEntity(final Entity start);

	/**
	 * search edge by edge destination node
	 * 
	 * @param end
	 *            destination node of the edge
	 * @return graph edges
	 */
	public List<Relationship> findByEndEntity(final Entity end);

	/**
	 * search edge by the nodes of the edge
	 * 
	 * @param start
	 *            edge's source node
	 * @param end
	 *            edge's destination node
	 * @return graph edges
	 */
	public List<Relationship> findByStartAndEnd(final Entity start, final Entity end);

}
