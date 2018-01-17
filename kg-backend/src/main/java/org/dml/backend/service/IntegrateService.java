package org.dml.backend.service;

import java.util.List;

import org.dml.core.cluster.ClusterDetailInfo;
import org.dml.core.cluster.ClusterInfo;
import org.dml.core.matchgraph.Entity;
import org.dml.core.matchgraph.MatchGraph;
import org.dml.core.matchgraph.Relationship;
import org.dml.core.querygraph.QueryGraph;

/**
 * RMI集成服务接口
 * 
 * @author HuangBo
 *
 */
public interface IntegrateService {

	/**
	 * 获取集群的概览信息
	 * 
	 * @return 集群存储概览信息
	 */
	public List<ClusterInfo> getClusterInfo();

	/**
	 * 获取集群中特定主机图谱存储详细信息
	 * 
	 * @param host
	 *            主机名（IP地址）
	 * @return 特定主机上图谱的详细存储信息
	 */
	public ClusterDetailInfo getClusterDetailInfo(String host);

	/**
	 * 查询图匹配
	 * 
	 * @param queryGraph
	 *            查询图
	 * @return 满足匹配条件的数据图，或者null（没有匹配结果）
	 */
	public List<MatchGraph> graphMatch(QueryGraph queryGraph);

	/**
	 * 在host机器上创建实体
	 * 
	 * @param host
	 *            机器IP地址
	 * @param crtEntity
	 *            创建的实体
	 * @return 成功返回true，失败返回false
	 */
	public boolean createEntity(final String host, final Entity crtEntity);

	/**
	 * 更新host机器上的实体（实体的标识不能修改）
	 * 
	 * @param host
	 *            机器IP地址
	 * @param srcEntity
	 *            需要更新的实体
	 * @param dstEntity
	 *            包含更新内容的实体
	 * @return 成功返回true，失败返回false
	 */
	public boolean updateEntity(final String host, final Entity srcEntity, final Entity dstEntity);

	/**
	 * 删除host机器上的实体
	 * 
	 * @param host
	 *            机器IP地址
	 * @param entityId
	 *            实体唯一标识
	 * @return 成功返回true，失败返回false
	 */
	public boolean deleteEntity(final String host, final String entityId);

	/**
	 * 在host机器上创建实体
	 * 
	 * @param host
	 *            机器的IP地址
	 * @param crtRela
	 *            创建的目标关系
	 * @return 成功返回true，失败返回false
	 */
	public boolean createRela(final String host, final Relationship crtRela);

	/**
	 * 更新host机器上的关系（关系的标识不能修改）
	 * 
	 * @param host
	 *            机器的IP地址
	 * @param srcRela
	 *            需要更新的关系
	 * @param dstRela
	 *            包含更新内容的关系
	 * @return 成功返回true，失败返回false
	 */
	public boolean updateRela(final String host, final Relationship srcRela, final Relationship dstRela);

	/**
	 * 删除host机器上的关系
	 * 
	 * @param host
	 *            机器的IP地址
	 * @param relaId
	 *            关系的唯一标识
	 * @return 成功返回true，失败返回false
	 */
	public boolean deleteRela(final String host, final String relaId);
}
