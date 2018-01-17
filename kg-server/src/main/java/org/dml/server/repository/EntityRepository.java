package org.dml.server.repository;

import java.util.List;
import java.util.Map;

import org.dml.core.matchgraph.Entity;

/**
 * 实体仓库操作接口
 * 
 * @author HuangBo
 *
 */
public interface EntityRepository {

	/**
	 * add the graph node to the graph database
	 * 
	 * @param crtEntity
	 *            graph node
	 * @return true if success,else false
	 */
	public boolean createEntity(final Entity crtEntity);

	/**
	 * update the srcEntity's information
	 * 
	 * @param srcEntity
	 *            source node
	 * @param dstEntity
	 *            destination node
	 * @return true if success,else false
	 */
	public boolean updateEntity(final Entity srcEntity, final Entity dstEntity);

	/**
	 * delete the node
	 * 
	 * @param entityId
	 *            delete node's unique id
	 * @return true if exist, or false
	 */
	public boolean deleteEntity(final String entityId);

	/**
	 * check graph database if contain node with the specific
	 * 
	 * @param identifier
	 *            graph node filed
	 * @return true if exist or false
	 */
	public boolean isContainEntity(final String identifier);

	/**
	 * get the identifier of the entity according to the graphId
	 * 
	 * @param graphId
	 *            graph inner identifier(differ from user identifier)
	 * @return graph entity's identifier attribute
	 */
	public String findIdByGraphId(final Long graphId);

	/**
	 * get the graph node according to the id in the graph database
	 * 
	 * @param graphId
	 *            graph inner identifier(differ from user identifier)
	 * @return graph node
	 */
	public Entity findByGraphId(final Long graphId);

	/**
	 * get the graph node with the specific identifier in the graph database
	 * 
	 * @param identifier
	 *            node's identifier
	 * @return graph node
	 */
	public Entity findById(final String identifier);

	/**
	 * get the nodes with the specific labels
	 * 
	 * @param labls
	 *            node labels
	 * @return graph nodes
	 */
	public List<Entity> findByLabels(List<String> labls);

	/**
	 * get the nodes satisfy the attributes
	 * 
	 * @param attributes
	 *            node's attributes constrain
	 * @return graph nodes
	 */
	public List<Entity> findByAttributes(Map<String, String> attributes);

	/**
	 * get the nodes satisfy the labels and attributes
	 * 
	 * @param labels
	 *            node's label constrain
	 * @param attributes
	 *            node's attributes constrain
	 * @return graph nodes
	 */
	public List<Entity> findByLablesAndAttibutes(List<String> labels, Map<String, String> attributes);

}
