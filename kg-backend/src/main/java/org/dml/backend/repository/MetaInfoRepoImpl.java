package org.dml.backend.repository;

import javax.annotation.Resource;

import org.dml.core.cluster.ClusterInfo;
import org.dml.core.metadata.EntityMetaInfo;
import org.dml.core.metadata.RelationshipMetaInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 图谱元数据操作实现类（基于redis数据库）
 * 
 * @author HuangBo
 *
 */
@Component
public class MetaInfoRepoImpl implements MetaInfoRepo {

	@Resource(name = "entityRedisTemplate")
	private RedisTemplate<String, EntityMetaInfo> entityTemplate;

	@Resource(name = "relationshipRedisTemplate")
	private RedisTemplate<String, RelationshipMetaInfo> relaTemplate;
	
	@Resource(name = "clusterInfoTemplate")
	private RedisTemplate<String, ClusterInfo> clusterInfoTemplate;

	@Override
	public EntityMetaInfo findEntityMetaById(String entityId) {
		try {
			return entityTemplate.opsForValue().get(entityId);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean insertEntityMeta(EntityMetaInfo entityMeta) {
		try {
			if(entityTemplate.hasKey(entityMeta.getEntitiId()))
				return false;
			entityTemplate.opsForValue().set(entityMeta.getEntitiId(), entityMeta);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteEntityMeta(String entityId) {
		try {
			entityTemplate.delete(entityId);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateEntityMeta(String entityId, EntityMetaInfo dst) {
		try {
			entityTemplate.opsForValue().set(entityId, dst);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}

	@Override
	public RelationshipMetaInfo findRelaMetaById(String relaId) {
		try {
			return relaTemplate.opsForValue().get(relaId);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean insertRelaMeta(RelationshipMetaInfo relaMeta) {
		try {
			if(relaTemplate.hasKey(relaMeta.getRelationshipId()))
				return false;
			relaTemplate.opsForValue().set(relaMeta.getRelationshipId(), relaMeta);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteRelaMeta(String relaId) {
		try {
			relaTemplate.delete(relaId);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateRelaMeta(String relaId, RelationshipMetaInfo dst) {
		try {
			relaTemplate.opsForValue().set(relaId, dst);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}
	
	@Override
	public ClusterInfo findClusterInfo(String host) {
		try {
			return clusterInfoTemplate.opsForValue().get(host);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return null;
		}
	}
	
	@Override
	public boolean addClusterInfo(ClusterInfo clusterInfo) {
		try {
			clusterInfoTemplate.opsForValue().set(clusterInfo.getHost(), clusterInfo);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean deleteClusterInfo(String host) {
		try {
			clusterInfoTemplate.delete(host);
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			return false;
		}
	}

}
