package org.dml.backend.metedata;

import java.util.Arrays;
import java.util.List;

import org.dml.backend.repository.MetaInfoRepo;
import org.dml.core.cluster.ClusterInfo;
import org.dml.core.metadata.EntityMetaInfo;
import org.dml.core.metadata.RelationshipMetaInfo;
import org.h2.engine.MetaRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MetaInfoConfig.class)
public class MetaInfoTest {
	
	@Autowired
	private MetaInfoRepo metaInfoRepo;
	
	/*@Test
	public void entityMetaTest() {
		String e = "e1";
		String core = "h1";
		List<String> extend = Arrays.asList("h2", "h3");
		
		EntityMetaInfo entityMeta = new EntityMetaInfo(e, core, extend);
		if(metaInfoRepo.insertEntityMeta(entityMeta))
			System.out.println("add entity metadata success");
		
		EntityMetaInfo getE = metaInfoRepo.findEntityMetaById(e);
		if(null != getE )
				System.out.println("get entity meta success:"
						+ "\n\tid=" + getE.getEntitiId()
						+ "\n\tcore=" + getE.getCoreHost()
						+ "\n\textend=" + getE.getExtendHosts());
		
		if(metaInfoRepo.updateEntityMeta(e, new EntityMetaInfo(e, "h4", extend)))
			System.out.println("update entity metadata success");
		
		if(metaInfoRepo.deleteEntityMeta(e))
			System.out.println("delete entity metada success");
	}
	
	@Test
	public void relaMetaTest() {
		String r = "e1";
		String core = "h1";
		List<String> extend = Arrays.asList("h2", "h3");
		
		RelationshipMetaInfo relaInfo = new RelationshipMetaInfo(r, core, extend);
		if(metaInfoRepo.insertRelaMeta(relaInfo))
			System.out.println("add relationship metadata success");
		
		if(null != metaInfoRepo.findRelaMetaById(r))
				System.out.println("get relationship meta success");
		
		if(metaInfoRepo.updateRelaMeta(r, new RelationshipMetaInfo(r, "h4", extend)))
			System.out.println("update relationship metadata success");
		
		if(metaInfoRepo.deleteRelaMeta(r))
			System.out.println("delete relationship metada success");
	}*/
	
	@Test
	public void clusterInfoTest() {
		String host = "localhost";
		long eNum = 100;
		long rNum = 1000;
		
		ClusterInfo clusterInfo = new ClusterInfo(host, eNum, rNum);
		if(metaInfoRepo.addClusterInfo(clusterInfo))
			System.out.println("add cluster metadata success");
		
		ClusterInfo getC = metaInfoRepo.findClusterInfo(host); 
		if(getC != null)
			System.out.println("get cluster metedata success"
					+ "\n\thost=" + getC.getHost()
					+ "\n\teNum=" + getC.getEntityNum()
					+ "\n\trNum=" + getC.getRelaNum());
		
		if(metaInfoRepo.deleteClusterInfo(host))
			System.out.println("delete cluster metadata success");
	}

}
