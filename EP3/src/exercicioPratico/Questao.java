package exercicioPratico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.AlphaCentrality;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.GmlImporter;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;

public class Questao {
	public static void main(String[] args) {
		Graph<DefaultVertex, RelationshipEdge> graph = new Multigraph<>(RelationshipEdge.class);
		importGraphGML(graph, "./files/email-Eu-core-parte2.gml");
		printGraph(graph);
		
		System.out.println();
		System.out.println("Alpha Centrality");
  	   	AlphaCentrality <DefaultVertex, RelationshipEdge> alphaCentrality = new AlphaCentrality <> (graph,0.001);
  	   	printOrderedVertexMeasures (alphaCentrality.getScores(),0,true);
		
  	   	System.out.println();
		System.out.println("Betweenness Centrality");
  	   	BetweennessCentrality <DefaultVertex, RelationshipEdge> betweennessCentrality = new BetweennessCentrality <> (graph,true);
  	   	printOrderedVertexMeasures (betweennessCentrality.getScores(),0,true);
	}
	
	public static Graph<DefaultVertex,RelationshipEdge> importGraphGML (Graph<DefaultVertex,RelationshipEdge> graph, String filename) {
		VertexProvider<DefaultVertex> vp1 = (label, attributes) -> new DefaultVertex(label, attributes);
		EdgeProvider<DefaultVertex, RelationshipEdge> ep1 = (from, to, label, attributes) -> new RelationshipEdge(from,
				to, attributes);
		GmlImporter<DefaultVertex, RelationshipEdge> gmlImporter = new GmlImporter<>(vp1, ep1);
		try {
			gmlImporter.importGraph(graph, readFile(filename));
		} catch (ImportException e) {
			throw new RuntimeException(e);
		}
		return graph;
	}
	
    static Reader readFile(String filename) {
		StringBuilder contentBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				contentBuilder.append(sCurrentLine).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringReader readergml = new StringReader(contentBuilder.toString());
		return readergml;
	}
    
    static <V> void printOrderedVertexMeasures (Map <V,Double> M, int count, boolean descending) {
		// count é a quantidade de elementos que devem ser exibidos em ordem decrescente do score. Se count for igual
    	//a 0, entao todos serao exibidos
        Set<Entry<V, Double>> set = M.entrySet();
        List<Entry<V, Double>> list = new ArrayList<Entry<V, Double>>(set);
        if (descending) {
        	Collections.sort( list, new Comparator<Map.Entry<V, Double>>()
        		{
        			public int compare( Map.Entry<V, Double> o1, Map.Entry<V, Double> o2 ) {
        				return (o2.getValue()).compareTo( o1.getValue() );
        			}
        		} );
        } else {
        	Collections.sort( list, new Comparator<Map.Entry<V, Double>>()
    		{
    			public int compare( Map.Entry<V, Double> o1, Map.Entry<V, Double> o2 ) {
    				return (o1.getValue()).compareTo( o2.getValue() );
    			}
    		} );
        }
        if (count == 0) {
        	count = list.size();
        }
        for (int i = 0; i<count; i++) {
        	Entry<V,Double> e = list.get(i);
        	System.out.print(e.getKey()+": "+ String.format("%.2f",(e.getValue()))+ "; ");
        }
	}
    
    public static <V,E> void printGraph (Graph <V,E> g ) {
        System.out.println(g.vertexSet());
		System.out.println(g.edgeSet()+"\n");
	}
	
	public static <V,E> void printGraph (Graph <V,E> g, String title ) {
		System.out.println(title);
        System.out.println(g.vertexSet());
		System.out.println(g.edgeSet()+"\n");
	}
}
