package org.kg.server.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dml.core.matchgraph.Entity;
import org.dml.core.matchgraph.InterMatchResult;
import org.dml.core.matchgraph.Relationship;
import org.dml.core.stwig.Direction;
import org.dml.core.stwig.LeafNode;
import org.dml.core.stwig.RootNode;
import org.dml.core.stwig.STwig;
import org.dml.server.config.KgServerConfig;
import org.dml.server.repository.EntityRepository;
import org.dml.server.repository.RelationshipRepository;
import org.dml.server.service.GraphDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.v1.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=KgServerConfig.class)
public class GraphServiceTest {
	
	/*@Autowired
	private ApplicationContext context;*/
	
	@Autowired
	private EntityRepository rentityRepo;
	
	@Autowired
	private RelationshipRepository relaRepo;
	
	@Autowired
	private GraphDataService graphService;

	/**
	 * 生成测试实体
	 * @return
	 */
	private Entity testEntity() {
		String identifier = "123456";
		List<String> labels = Arrays.asList("label1", "label2", "label3");
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("attri1", "value1");
		attributes.put("attri2", "value2");
		attributes.put("attri3", "value3");
		Entity entity = new Entity(identifier, labels, attributes);
		return entity;
	}
	
	/**
	 * 生成测试关系
	 * @return
	 */
	private Relationship testRela() {
		String srcNodeId = "n1";
		String dstNodeId = "n2";
		String relaId = "r1";
		List<String> srcNodeLabels = Arrays.asList("nLabel1", "nLabel2");
		List<String> dstNodeLabels = Arrays.asList("nLabel3", "nLabel4");
		String relaLabel = "rLabel1";
		Map<String, String> srcAttri = new HashMap<>();
		Map<String, String> dstAttri = new HashMap<>();
		Map<String, String> relaAttri = new HashMap<>();
		srcAttri.put("k1", "v1");
		srcAttri.put("k2", "v2");
		dstAttri.put("k3", "v3");
		dstAttri.put("k4", "v4");
		relaAttri.put("k5", "v5");
		relaAttri.put("k6", "v6");
		
		Entity srcEn = new Entity(srcNodeId, srcNodeLabels, srcAttri);
		Entity dstEn = new Entity(dstNodeId, dstNodeLabels, dstAttri);
		Relationship relationship = new Relationship(relaId, relaLabel, relaAttri, srcNodeId, dstNodeId);
		
		return relationship;
	}
	
	/**
	 * 生成测试STwig
	 * @return
	 */
	private STwig testSTwig() {
		STwig sTwig = new STwig();
		RootNode root = new RootNode(0, Arrays.asList("d"));
		LeafNode leaf1 = 
				new LeafNode(1, Direction.STWIG_EDGE_FORWARD, "de", Arrays.asList("e"));
		LeafNode leaf2 = 
				new LeafNode(2, Direction.STWIG_EDGE_FORWARD, "dc", Arrays.asList("c"));
		LeafNode leaf3 = 
				new LeafNode(3, Direction.STWIG_EDGE_REVERSE, "bd", Arrays.asList("b"));
		LeafNode leaf4 = 
				new LeafNode(4, Direction.STWIG_EDGE_REVERSE, "fd", Arrays.asList("f"));
		sTwig.setRoot(root);
		sTwig.addLeaf(leaf1);
		sTwig.addLeaf(leaf2);
		sTwig.addLeaf(leaf3);
		sTwig.addLeaf(leaf4);
		return sTwig;
	}
	
	/**
	 * 实体操作测试
	 */
	@Test
	public void entitiRepoTest() {
		/* relationship test
		//create relationship
		Relationship srcRelationship = testRela();
		entityRepo.createEntity(srcRelationship.getStart());
		entityRepo.createEntity(srcRelationship.getEnd());
		if(relaRepo.createRelationship(srcRelationship))
			System.out.println("add relationship success");*/
		
		/*//delete relationship
		if(relaRepo.deleteRelationship(srcRelationship.getIdentifier()))
			System.out.println("delete relationship success");*/
		
		/*//update relationship
		Relationship dstRelationship = testRela();
		dstRelationship.setLabel("rL2");
		if(relaRepo.updateRelationship(srcRelationship, dstRelationship))
			System.out.println("update relationship success");*/
		
		/*//retrieve relationship by id
		Relationship getRela = relaRepo.findById(srcRelationship.getIdentifier());
		System.out.println("relationship result information:");
		System.out.println("\tid=" + getRela.getIdentifier());
		System.out.println("\tlabel=" + getRela.getLabel());
		System.out.println("\tattibutes=" + getRela.getAttributes());
		System.out.println("\tsrc&dst=" 
				+ getRela.getStart().getIdentifier() + "," 
				+ getRela.getEnd().getIdentifier());*/
		
		/*//retrieve relationship by label and attributes
		String queryLabel = "rLabel1";
		Map<String, String> queryAttri = new HashMap<>();
		queryAttri.put("k5", "v5");
		List<Relationship> getRelas = 
				relaRepo.findByLabelsAndAttibutes(queryLabel, queryAttri);
		for(Relationship getRela : getRelas) {
			System.out.println("relationship result information:");
			System.out.println("\tid=" + getRela.getIdentifier());
			System.out.println("\tlabel=" + getRela.getLabel());
			System.out.println("\tattibutes=" + getRela.getAttributes());
			System.out.println("\tsrc&dst=" 
					+ getRela.getStart().getIdentifier() + "," 
					+ getRela.getEnd().getIdentifier());
		}*/
		
		/*//retrieve relationship by node
		Entity querySrcEntity = new Entity("n1");
		Entity queryDstEntity = new Entity("n2");
		List<Relationship> getRelas = 
				relaRepo.findByStartAndEnd(querySrcEntity, queryDstEntity);
		for(Relationship getRela : getRelas ) {
			System.out.println("relationship info:");
			System.out.println("\tid=" + getRela.getIdentifier());
			System.out.println("\tlabel=" + getRela.getLabel());
			System.out.println("\tattributes=" + getRela.getAttributes());
		}
		*/
	}
	
	/**
	 * 关系操作测试
	 */
	@Test
	public void releRepoTest() {
		/*//entity test
		//create node
		Entity srcEntity = testEntity();
		if(entityRepository.createEntity(srcEntity);)
			System.out.println("add entity success");;
		
		//get retrieve by graph id
		Entity idEntity = entityRepo.findByGraphId(10l);
		System.out.println(idEntity.getIdentifier());
		
		//retrieve node by node id
		Entity retrieveEntity = entityRepository.findById(srcEntity.getIdentifier());
		if(retrieveEntity != null) {
			System.out.println("get node success:nodeLabels=" + retrieveEntity.getLabels());
		} else {
			System.out.println("get node failed");
		}
		
		//retrieve node by label
		List<String> labels = Arrays.asList("label1", "label2");
		Map<String, String> attributes = new HashMap<>();
		attributes.put("attri1", "value1");
		attributes.put("attri2", "value2");
		List<Entity> entities1 = entityRepository.findByLabels(labels);
		System.out.println(entities1.get(0).getIdentifier());
		List<Entity> entities2 = entityRepository.findByAttributes(attributes);
		System.out.println(entities2.get(0).getIdentifier());
		List<Entity> entities3 = entityRepository.findByLablesAndAttibutes(labels, attributes);
		System.out.println(entities3.get(0).getIdentifier());
		
		//update node
		Entity dstEntity = testEntity();
		dstEntity.setLabels(Arrays.asList("UpdateLabel1", "UpdateLabel2"));
		boolean isUpdate = entityRepository.updateEntity(srcEntity, dstEntity);
		if(isUpdate) {
			System.out.println("update node success");
		} else {
			System.out.println("update node failed");
		}
		
		//delete node
		boolean isDelete = entityRepository.deleteEntity(srcEntity.getIdentifier());
		if(isDelete) {
			System.out.println("delete node success");
		} else {
			System.out.println("delete node failed");
		}
		*/
	}

	@Test
	public void stwigMatchTest() {
		STwig stwig = testSTwig();
		List<InterMatchResult> interResults = 
				graphService.stwigLabelMatch(stwig);
		System.out.println(interResults);
	}
	
}
