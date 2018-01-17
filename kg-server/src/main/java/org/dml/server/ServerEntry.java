package org.dml.server;

import org.dml.server.config.KgServerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ServerEntry {

	public static void main(String[] args) {
		AbstractApplicationContext context = 
				new AnnotationConfigApplicationContext(KgServerConfig.class);
		System.out.println("knowledge graph manager server running....");
		
		//add shutdown hook
		context.registerShutdownHook();
	}

}
