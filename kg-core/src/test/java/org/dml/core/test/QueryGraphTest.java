package org.dml.core.test;

import java.util.Arrays;
import java.util.List;

import org.dml.core.querygraph.QueryEdge;
import org.dml.core.querygraph.QueryGraph;
import org.dml.core.querygraph.QueryNode;
import org.dml.core.stwig.STwig;
import org.junit.Before;
import org.junit.Test;

public class QueryGraphTest {
	
	private QueryGraph qGraph;
	
	@Before
	public void init() {
		//query node
		QueryNode nodeA = new QueryNode(1, Arrays.asList("a"));
		QueryNode nodeB = new QueryNode(2, Arrays.asList("b"));
		QueryNode nodeC = new QueryNode(3, Arrays.asList("c"));
		QueryNode nodeD = new QueryNode(4, Arrays.asList("d"));
		QueryNode nodeE = new QueryNode(5, Arrays.asList("e"));
		QueryNode nodeF = new QueryNode(6, Arrays.asList("f"));
		
		//query edge
		QueryEdge edgeAB = new QueryEdge(1, 1, 2, "ab");
		QueryEdge edgeCA = new QueryEdge(2, 3, 1, "ca");
		QueryEdge edgeEB = new QueryEdge(3, 5, 2, "eb");
		QueryEdge edgeBD = new QueryEdge(4, 2, 4, "bd");
		QueryEdge edgeDC = new QueryEdge(5, 4, 3, "dc");
		QueryEdge edgeCF = new QueryEdge(6, 3, 6, "cf");
		QueryEdge edgeDE = new QueryEdge(7, 4, 5, "de");
		QueryEdge edgeFD = new QueryEdge(8, 6, 4, "fd");
		
		//construct query graph
		qGraph = new QueryGraph();
		qGraph.addNode(nodeA);
		qGraph.addNode(nodeB);
		qGraph.addNode(nodeC);
		qGraph.addNode(nodeD);
		qGraph.addNode(nodeE);
		qGraph.addNode(nodeF);
		qGraph.addEdge(edgeAB);
		qGraph.addEdge(edgeCA);
		qGraph.addEdge(edgeEB);
		qGraph.addEdge(edgeBD);
		qGraph.addEdge(edgeDC);
		qGraph.addEdge(edgeCF);
		qGraph.addEdge(edgeDE);
		qGraph.addEdge(edgeFD);
	}
	
	@Test
	public void decompTest() {
		System.out.println("Query graph decomp:");
		List<STwig> subQueries = qGraph.decompGraph();
		System.out.println(subQueries);
	}

}
