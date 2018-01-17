package org.dml.backend.repository;

import org.dml.core.cluster.ClusterInfo;
import org.dml.core.metadata.EntityMetaInfo;
import org.dml.core.metadata.RelationshipMetaInfo;

/**
 * 图谱元数据仓库操作接口
 * 
 * @author HuangBo
 *
 */
public interface MetaInfoRepo {

	/**
	 * 根据实体的唯一标识获取实体元数据
	 * @param entityId 实体唯一标识
	 * @return 实体元数据或者null
	 */
	public EntityMetaInfo findEntityMetaById(String entityId);

	/**
	 * 添加新的实体的元数据
	 * @param entityMeta 待添加的实体元数据
	 * @return 添加成功返回true，失败返回null
	 */
	public boolean insertEntityMeta(EntityMetaInfo entityMeta);

	/**
	 * 删除特定的实体元数据
	 * @param entityId 实体唯一标识
	 * @return 成功返回true，失败返回false
	 */
	public boolean deleteEntityMeta(String entityId);

	/**
	 * 更新实体元数据
	 * @param entityId 实体唯一标识
	 * @param dst 包含更新内容的实体元数据
	 * @return 成功返回true，失败返回false
	 */
	public boolean updateEntityMeta(String entityId, EntityMetaInfo dst);

	/**
	 * 获取关系元数据
	 * @param relaId 关系唯一标识
	 * @return 满足条件的关系元数据或null
	 */
	public RelationshipMetaInfo findRelaMetaById(String relaId);

	/**
	 * 添加关系元数据
	 * @param relaMeta 关系元数据
	 * @return 成功返回true，失败返回false
	 */
	public boolean insertRelaMeta(RelationshipMetaInfo relaMeta);

	/**
	 * 删除关系元数据
	 * @param relaId 关系唯一标识
	 * @return 成功返回true，失败返回false
	 */
	public boolean deleteRelaMeta(String relaId);

	/**
	 * 更新关系元数据
	 * @param relaId 关系唯一标识
	 * @param dst 包含更新内容的关系元数据
	 * @return 成功返回true，失败返回false
	 */
	public boolean updateRelaMeta(String relaId, RelationshipMetaInfo dst);
	
	/**
	 * 获取特定机器的概览信息
	 * @param host 主机名（IP地址）
	 * @return 特定机器上的概览信息
	 */
	public ClusterInfo findClusterInfo(String host);
	
	/**
	 * 添加集群概览信息
	 * @param clusterInfo 某个机器上的概览信息（如果原来存在会替换，不存在会创建）
	 * @return 成功返回true，失败返回false
	 */
	public boolean addClusterInfo(ClusterInfo clusterInfo);
	
	/**
	 * 删除集群概览信息
	 * @param host 主机名（IP地址）
	 * @return 成功返回true，失败返回false
	 */
	public boolean deleteClusterInfo(String host);
	
}
