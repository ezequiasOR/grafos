package exercicioPratico1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.CSVFormat;
import org.jgrapht.io.CSVImporter;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;

public class Questao1 {
	public static void main(String[] args) {

		Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

		graph = importGraphCSV(
				graph, 
				"../files/Astronautas.csv",
				CSVFormat.MATRIX, 
				false,
				false,
				true); // MATRIX_FORMAT_NODEID
		
		System.out.println("Vertices: " + graph.vertexSet());
		System.out.println("Arestas: " + graph.edgeSet());
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
			Graph<String,DefaultEdge> graph, 
			String filename, 
			CSVFormat f,
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
}
