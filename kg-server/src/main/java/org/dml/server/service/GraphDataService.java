package org.dml.server.service;

import java.util.List;
import java.util.Map;

import org.dml.core.cluster.ClusterDetailInfo;
import org.dml.core.cluster.ClusterInfo;
import org.dml.core.cluster.DbIndexsInfo;
import org.dml.core.matchgraph.Entity;
import org.dml.core.matchgraph.InterMatchResult;
import org.dml.core.matchgraph.Relationship;
import org.dml.core.stwig.STwig;

/**
 * 图操作接口
 * 
 * @author HuangBo
 *
 */
public interface GraphDataService {

	/**
	 * 测试函数
	 * 
	 * @param msg
	 * @return
	 */
	public String hello(String msg);

	/**
	 * 获取本机存储的实体数
	 * 
	 * @return 本机上的实体数
	 */
	public Long EntityNum();

	/**
	 * 获取本机存储的关系树
	 * 
	 * @return 本机上的关系数
	 */
	public Long relaNum();

	/**
	 * 获取本机上节点所有的标签
	 * 
	 * @return 本机上所有实体标签
	 */
	public List<String> dbLabels();

	/**
	 * 获取本机上节点所有的属性键值
	 * 
	 * @return 本机上所有的属性键
	 */
	public List<String> dbPropertyKeys();

	/**
	 * 获取本机上所有的关系标签
	 * 
	 * @return 本机上所有的关系标签
	 */
	public List<String> dbRelaTypes();

	/**
	 * 获取本机上所有的索引信息
	 * 
	 * @return 本机上所有的索引信息
	 */
	public List<DbIndexsInfo> dbIndexs();

	/**
	 * 获取本机图谱存储详细信息
	 * 
	 * @return
	 */
	public ClusterDetailInfo getClusterDetailInfo();

	/**
	 * 在本地单机上进行查询图的子查询（STwig）匹配
	 * 
	 * @param stwig
	 *            查询图分解后的子查询
	 * @return 子查询的匹配结果
	 */
	public List<InterMatchResult> stwigLabelMatch(STwig stwig);

	public boolean createEntity(final Entity crtEntity);

	public boolean updateEntity(final Entity srcEntity, final Entity dstEntity);

	public boolean deleteEntity(final String entityId);

	public boolean isContainEntity(final String entityId);

	public Entity findEntityByGraphId(final long graphId);

	public Entity findEntityByEntityId(final String entityId);

	public List<Entity> findEntityByLabels(final List<String> labels);

	public List<Entity> findEntityByAttributes(final Map<String, String> attributes);

	public List<Entity> findEntityByLabelsAndAttributes(final List<String> labels, final Map<String, String> attribues);

	public boolean createRela(final Relationship crtRela);

	public boolean updateRela(final Relationship srcRela, final Relationship dstRela);

	public boolean deleteRela(final String relaId);

	public boolean isContainRela(final String relaId);

	public Relationship findRelaByRelaId(final String relaId);

	public List<Relationship> findRelaByLabel(final String label);

	public List<Relationship> findRelaByAttributes(final Map<String, String> attributes);

	public List<Relationship> findyRelaByLabelAndAttibutes(final String label, final Map<String, String> attributes);

	public List<Relationship> findRelaByStartEntity(final Entity startEntity);

	public List<Relationship> findRelaByEndEntity(final Entity endEntity);

	public List<Relationship> findRelaByStartAndEntity(final Entity startEntity, final Entity endEntity);
}
