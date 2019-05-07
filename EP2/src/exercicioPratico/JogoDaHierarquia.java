package exercicioPratico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jgrapht.Graph;
import org.jgrapht.GraphTests;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.CSVFormat;
import org.jgrapht.io.CSVImporter;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;

public class JogoDaHierarquia {
	public static void main(String[] args) {
		
		Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		graph = importGraphCSV(graph, "./files/Astronautas.csv", CSVFormat.MATRIX, false, false, true);
		//TODO substituir arquivo
		
		//System.out.println("Is it a tree? " + GraphTests.isTree(graph));  // printa true se for arvore, caso contrario, false
		
		if (GraphTests.isTree(graph)) {
			
		}
		
	}
	
	
	
	
	/**
	 * O Metodo importara um arquivo CSV e o transformara em um objeto grafo.
	 * @param graph Grafo que sera utilizado como base.
	 * @param filename Nome do arquivo CSV.
	 * @param f Formato do arquivo.
	 * @return O Grafo com base no arquivo CSV recebido.
	 */
	public static Graph<String, DefaultEdge> importGraphCSV(Graph<String, DefaultEdge> graph, String filename,
			CSVFormat f) {
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

	/**
	 * O Metodo importara um arquivo CSV e o transformara em um objeto grafo.
	 * @param graph Grafo que sera utilizado como base.
	 * @param filename Nome do arquivo CSV.
	 * @param f Formato do arquivo.
	 * @param pMATRIX_FORMAT_ZERO_WHEN_NO_EDGE
	 * @param pEDGE_WEIGHT
	 * @param pMATRIX_FORMAT_NODEID
	 * @return Um Grafo com base no arquivo CSV.
	 */
	public static Graph<String, DefaultEdge> importGraphCSV(Graph<String, DefaultEdge> graph, String filename,
			CSVFormat f, boolean pMATRIX_FORMAT_ZERO_WHEN_NO_EDGE, boolean pEDGE_WEIGHT,
			boolean pMATRIX_FORMAT_NODEID) {
		VertexProvider<String> vp = (label, attributes) -> label;
		EdgeProvider<String, DefaultEdge> ep = (from, to, label, attributes) -> new DefaultEdge();

		CSVImporter<String, DefaultEdge> csvImporter = new CSVImporter<>(vp, ep);
		csvImporter.setFormat(f);
		csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, pMATRIX_FORMAT_ZERO_WHEN_NO_EDGE);
		csvImporter.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, pEDGE_WEIGHT);
		csvImporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, pMATRIX_FORMAT_NODEID);

		try {
			csvImporter.importGraph(graph, readFile(filename));
		} catch (ImportException e) {
			throw new RuntimeException(e);
		}
		return graph;
	}
	
	/**
	 * Ira ler o arquivo CSV.
	 * @param filename Nome do arquivo.
	 * @return Retornara um Reader.
	 */
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
	
	/**
	 * Printara um grafo a partir de seus vertices e arestas.
	 * @param g Grafo a ser printado.
	 */
	public static <V,E> void printGraph (Graph <V,E> g ) {
        System.out.println(g.vertexSet());
		System.out.println(g.edgeSet()+"\n");
	}
	
	/**
	 * Printara um grafo a partir de seus vertices e arestas.
	 * @param g Grafo a ser printado.
	 * @param title Titulo do grafo.
	 */
	public static <V,E> void printGraph (Graph <V,E> g, String title ) {
		System.out.println(title);
        System.out.println(g.vertexSet());
		System.out.println(g.edgeSet()+"\n");
	}
}
