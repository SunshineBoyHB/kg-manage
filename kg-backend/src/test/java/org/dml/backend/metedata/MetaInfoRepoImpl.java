package org.dml.backend.metedata;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dml.backend.repository.MetaInfoRepo;
import org.dml.core.cluster.ClusterInfo;
import org.dml.core.metadata.EntityMetaInfo;
import org.dml.core.metadata.RelationshipMetaInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 图谱元数据仓库操作实现类(基于h2数据库，测试)
 * 
 * @author HuangBo
 *
 */
public class MetaInfoRepoImpl implements MetaInfoRepo {

	private JdbcTemplate jdbc;

	public MetaInfoRepoImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	/**
	 * Entity metadata information row mapper for EntityMetadata table
	 */
	private static final class EntityRowMapper implements RowMapper<EntityMetaInfo> {
		@Override
		public EntityMetaInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			String entityID = rs.getString("Id");
			String coreHost = rs.getString("CoreHost");
			Object[] hosts = (Object[]) rs.getArray("ExtendHosts").getArray();
			List<String> extendHosts = new ArrayList<String>();
			for (Object host : hosts) {
				extendHosts.add((String) host);
			}
			return new EntityMetaInfo(entityID, coreHost, extendHosts);
		}
	}

	/**
	 * Relationship metadata information row mapper for RelationshipMetadata table
	 */
	private static final class RelationshipRowMapper implements RowMapper<RelationshipMetaInfo> {
		@Override
		public RelationshipMetaInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			String relaId = rs.getString("Id");
			String coreHost = rs.getString("CoreHost");
			Object[] hosts = (Object[]) rs.getArray("ExtendHosts").getArray();
			List<String> exdtendHosts = new ArrayList<>();
			for (Object host : hosts) {
				exdtendHosts.add((String) host);
			}
			return new RelationshipMetaInfo(relaId, coreHost, exdtendHosts);
		}
	}

	@Override
	public EntityMetaInfo findEntityMetaById(String entityId) {
		try {
			String sql = "SELECT Id, CoreHost, ExtendHosts FROM EntityMetadata Where Id=?";
			return jdbc.queryForObject(sql, new EntityRowMapper(), entityId);
		} catch (Exception e) {
			System.err.println("find entity metadata error for :" + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean insertEntityMeta(EntityMetaInfo entityMeta) {
		try {
			String sql = "INSERT INTO EntityMetadata VALUES(?, ?, ?)";
			String entityId = entityMeta.getEntitiId();
			String coreHost = entityMeta.getCoreHost();
			String[] extendHosts = (String[]) entityMeta.getExtendHosts().toArray();
			Array extendHostsArr = jdbc.getDataSource().getConnection().createArrayOf("VARCHAR", extendHosts);
			if (jdbc.update(sql, entityId, coreHost, extendHostsArr) >= 1) {
				extendHostsArr.free();
				return true;
			}
			extendHostsArr.free();
			return false;
		} catch (Exception e) {
			System.err.println("insert entity metadata error for :" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteEntityMeta(String entityId) {
		try {
			String sql = "DELETE FROM EntityMetadata WHERE Id=?";
			if (jdbc.update(sql, entityId) >= 1)
				return true;
			return false;
		} catch (Exception e) {
			System.err.println("delete entity metadata error for :" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateEntityMeta(String entityId, EntityMetaInfo dst) {
		try {
			String sql = "UPDATE EntityMetadata SET CoreHost=?, ExtendHosts=? WHERE Id=?";
			String coreHost = dst.getCoreHost();
			String[] extendHosts = (String[]) dst.getExtendHosts().toArray();
			Array extendHostsArr = jdbc.getDataSource().getConnection().createArrayOf("VARCHAR", extendHosts);
			if (jdbc.update(sql, coreHost, extendHostsArr, entityId) >= 1) {
				extendHostsArr.free();
				return true;
			}
			extendHostsArr.free();
			return false;
		} catch (Exception e) {
			System.err.println("update entity metadata error for :" + e.getMessage());
			return false;
		}
	}

	@Override
	public RelationshipMetaInfo findRelaMetaById(String relaId) {
		try {
			String sql = "SELECT * FROM RelationshipMetadata WHERE Id = ?";
			return jdbc.queryForObject(sql, new RelationshipRowMapper(), relaId);
		} catch (Exception e) {
			System.err.println("find relationship metadata error for :" + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean insertRelaMeta(RelationshipMetaInfo relaMeta) {
		try {
			String sql = "INSERT INTO RelationshipMetadata VALUES(?, ?, ?)";
			String relaId = relaMeta.getRelationshipId();
			String coreHost = relaMeta.getCoreHost();
			String[] extendHosts = (String[]) relaMeta.getExtendHosts().toArray();
			Array extendHostsArr = jdbc.getDataSource().getConnection().createArrayOf("VARCHAR", extendHosts);
			if (jdbc.update(sql, relaId, coreHost, extendHostsArr) >= 1) {
				extendHostsArr.free();
				return true;
			}
			extendHostsArr.free();
			return false;
		} catch (Exception e) {
			System.err.println("insert relationship metadata error for :" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteRelaMeta(String relaId) {
		try {
			String sql = "DELETE FROM RelationshipMetadata WHERE Id = ?";
			if (jdbc.update(sql, relaId) >= 1)
				return true;
			return false;
		} catch (Exception e) {
			System.err.println("delete relationship metadata error for :" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateRelaMeta(String relaId, RelationshipMetaInfo dst) {
		try {
			String sql = "UPDATE RelationshipMetadata SET CoreHost = ?, ExtendHosts = ? WHERE Id = ?";
			String coreHost = dst.getCoreHost();
			String[] extendHosts = (String[]) dst.getExtendHosts().toArray();
			Array extendHostsArr = jdbc.getDataSource().getConnection().createArrayOf("VARCHAR", extendHosts);
			if (jdbc.update(sql, coreHost, extendHostsArr, relaId) >= 1) {
				extendHostsArr.free();
				return true;
			}
			extendHostsArr.free();
			return false;
		} catch (Exception e) {
			System.err.println("delete relationship metadata error for :" + e.getMessage());
			return false;
		}
	}

	@Override
	public ClusterInfo findClusterInfo(String host) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addClusterInfo(ClusterInfo clusterInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteClusterInfo(String host) {
		// TODO Auto-generated method stub
		return false;
	}

}
