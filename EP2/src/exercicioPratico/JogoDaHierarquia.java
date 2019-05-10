package exercicioPratico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.jgrapht.Graph;
import org.jgrapht.GraphTests;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.CSVFormat;
import org.jgrapht.io.CSVImporter;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;

public class JogoDaHierarquia {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		graph = importGraphCSV(graph, "./files/grafo.csv", CSVFormat.MATRIX, false, false, true);   //TODO substituir arquivo ********AQUI********
		
		int n = Integer.parseInt(sc.nextLine());   // Lendo a qunatidade de chaces que que o jogador tem
		
		//System.out.println("Is it a tree? " + GraphTests.isTree(graph));  // printa true se for arvore, caso contrario, false  (essa linha nao importa
		
		if (GraphTests.isTree(graph)) {
			String root = getRoot(graph);  //TODO achar a raiz do grafo e armazenar em uma variavel  (acho que ta feito)
			
			int i = 0;
			boolean guard = true;
			
			while (guard && i < n) {
				//TODO receber o chute do jogador e verificar se eh igual a raiz do grafo  (acho que ta feito)
				String guessing = sc.nextLine();
				if (guessing.equals(root)) {
					guard = false;
					System.out.println("Você acertou!");
					//TODO imprimir a árvore enraizada.   **********TA FALTANDO AQUI**************** (acho que ta feito agora)
					printGraph(graph, "Você acertou!");
				} else {
					if (guard) {
						// TODO printar a responsta com o pai do vertice e os seus filhos		(acho que ta feito)
						List<String> pai = Graphs.predecessorListOf(graph, guessing);
						List<String> filhos = Graphs.successorListOf(graph, guessing);
						System.out.println(guessing + " não é raiz. O pai de " + guessing + " é " + pai + " e os filhos de " + guessing + " são " + filhos);

					}
				}
				i++;
			}
			if (!guard) {
				System.out.println("Número de tentativas excedido!");
			}
			
		}
		
		sc.close();
		
	}
	
	private static String getRoot(Graph<String, DefaultEdge> graph) {
		Iterator <String> vertex = graph.vertexSet().iterator();
		String root = "";
		while (vertex.hasNext()) {
			//TODO ver qual é o vertice raiz e retornar ele pra ser usado na linha 33  (acho que ta feito)
			String v = vertex.next();
			if (Graphs.predecessorListOf(graph, v).equals(null)){
				root = v;
				return root;
			}
		}
		return null;
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
