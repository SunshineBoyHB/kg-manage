package org.kg.server.test;

import java.util.List;

import org.dml.core.cluster.DbIndexsInfo;
import org.dml.server.config.KgServerConfig;
import org.dml.server.service.GraphDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=KgServerConfig.class)
public class ClusterInfoTest {
	
	@Autowired
	private GraphDataService graphService;
	
	@Test
	public void countTest() {
		System.out.println("Count Test");
		long entityNum = graphService.EntityNum();
		long relaNum = graphService.relaNum();
		System.out.println("EntityNum=" + entityNum + ", RelaNum=" + relaNum);
	}
	
	@Test
	public void labelTest() {
		System.out.println("Label Test");
		List<String> labels = graphService.dbLabels();
		System.out.println("labels=" + labels);
	}
	
	@Test
	public void propertyKeyTest() {
		System.out.println("PropertyKey Test");
		List<String> propertyKeys = graphService.dbPropertyKeys();
		System.out.println("property keys=" + propertyKeys);
	}
	
	@Test
	public void relaTypeTest() {
		System.out.println("Relationship Types Test");
		List<String> relaTypes = graphService.dbRelaTypes();
		System.out.println("relationship types=" + relaTypes);
	}
	
	@Test
	public void indexTest() {
		System.out.println("Index Test");
		List<DbIndexsInfo> dbIndexs = graphService.dbIndexs();
		System.out.println("Indexs=" + dbIndexs);
	}

}
