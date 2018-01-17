package org.dml.backend.property;

import org.dml.backend.util.BackendConfigHolder;
import org.dml.backend.util.IntegrateServiceProxy;
import org.dml.server.service.GraphDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UtilTestConfig.class)
public class UtilTest {
	
	@Autowired
	BackendConfigHolder configHolder;
	
	@Autowired
	IntegrateServiceProxy rmiProxies;
	
	@Test
	public void configInfo() {
		System.out.println("BackendConfigHolder["
				+ "\nserviceNum=" + configHolder.getRmiServiceNum()
				+ "\nserviceName=" + configHolder.getRmiServiceName()
				+ "\nservicePort=" + configHolder.getRmiServicePort()
				+ "\nserviceHosts=" + configHolder.getRmiServiceHosts()
				+ "\n]");
		System.out.println();
	}
	
	@Test
	public void rmiProxiesInfo() {
		System.out.println("IntegrateServiceProxy["
				+ "\nserviceNum=" + rmiProxies.getServiceNum()
				+ "\nproxyServices=" + rmiProxies.getServiceProxys()
				+"\n]");
		System.out.println();
	}
	
	@Test
	public void rmiServiceTest() {
		String localhost = "222.20.74.204";
		GraphDataService service = rmiProxies.getGraphServiceByHost(localhost);
		
		String testMsg = "Test Message";
		System.out.println("rmi test response for hello:"+ service.hello(testMsg));
	}

}
