package exercicioPratico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Comparator;
import java.util.Iterator;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.CSVFormat;
import org.jgrapht.io.CSVImporter;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;

public class Questao2 {
	public static void main(String[] args) {
		Graph<String, DefaultEdge> graph1 = new SimpleGraph<>(DefaultEdge.class);

		graph1 = importGraphCSV(graph1, "./files/grafo1.csv", CSVFormat.MATRIX, 
				false, false, true);
		printGraph(graph1,"Base Graph:");
		
		Graph<String, DefaultEdge> graph2 = new SimpleGraph<>(DefaultEdge.class);

		graph2 = importGraphCSV(graph2, "./files/grafo2.csv", CSVFormat.MATRIX, 
				false, false, true);
		printGraph(graph2,"Graph 2:");
		
		VF2SubgraphIsomorphismInspector <String, DefaultEdge> embeddingChecker = 
        		new VF2SubgraphIsomorphismInspector <> (graph1,graph2);
	    Iterator <GraphMapping <String,DefaultEdge>> it = embeddingChecker.getMappings();
	    
	    embeddingChecker = new VF2SubgraphIsomorphismInspector <> (graph1,graph2);
	    it = embeddingChecker.getMappings();
	    if (it.hasNext()) {
	    	System.out.println("Os grupos dos quarteirões que não podem ser separados são: ");
	    	GraphMapping <String,DefaultEdge> m = it.next();
	    	Iterator <String> it2 = graph2.vertexSet().iterator();
	    	while (it2.hasNext()) {
	    		String v = it2.next();
	    		System.out.print(v + "->" + m.getVertexCorrespondence(v,false) + " ");
	    	}
	    } else {
	    	System.out.println("Não há grupos nos quarteirões que não podessam ser separados.");
	    }
	}
	
	public static Graph<String,DefaultEdge> importGraphCSV (Graph<String,DefaultEdge> graph, String filename, CSVFormat f) {
		VertexProvider<String> vp = (label, attributes) -> label;
		EdgeProvider<String, DefaultEdge> ep = (from, to, label, attributes) -> new DefaultEdge();

		CSVImporter<String, DefaultEdge> csvImporter = new CSVImporter<>(vp, ep);
		csvImporter.setFormat(f); 
		
		try {
			csvImporter.importGraph(graph, readFile(filename)); 
		} catch (ImportException e) { 
			throw new RuntimeException(e); 
		}
		return graph;
	}
	
	public static Graph<String,DefaultEdge> importGraphCSV (
			Graph<String,DefaultEdge> graph, String filename, CSVFormat f,
			boolean pMATRIX_FORMAT_ZERO_WHEN_NO_EDGE,
			boolean pEDGE_WEIGHT,
			boolean pMATRIX_FORMAT_NODEID) {
		VertexProvider<String> vp = (label, attributes) -> label;
		EdgeProvider<String, DefaultEdge> ep = (from, to, label, attributes) -> new DefaultEdge();

		CSVImporter<String, DefaultEdge> csvImporter = new CSVImporter<>(vp, ep);
		csvImporter.setFormat(f);
	    csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE,pMATRIX_FORMAT_ZERO_WHEN_NO_EDGE);
	    csvImporter.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, pEDGE_WEIGHT);
	    csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, pMATRIX_FORMAT_NODEID);
		
		try {
			csvImporter.importGraph(graph, readFile(filename)); 
		} catch (ImportException e) { 
			throw new RuntimeException(e); 
		}
		return graph;
	}
	
	public static Reader readFile(String filename) {
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
