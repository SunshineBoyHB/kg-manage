package org.dml.backend.service;

import java.util.Arrays;
import java.util.List;

import org.dml.core.matchgraph.Entity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrateServiceTestConfig.class)
public class IntegrateServiceTest {
	
	@Autowired
	IntegrateService service;
	
	@Test
	public void graphServiceTest() {
		String localhost = "222.20.74.204";
		
		String eId = "apple1";
		List<String> eLabels = Arrays.asList("fruit", "sweet");
		Entity apple = new Entity(eId, eLabels, null);
		
		if(service.createEntity(localhost, apple))
			System.out.println("create entity success!");
	}

}
