package exercicioPratico1;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.CSVFormat;

public class Questao1 {
	public static void main(String[] args) {

		Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

		graph = MyJGraphTUtil.importGraphCSV(
				graph, 
				"./src/main/java/graphs/5-3regular.csv",
				CSVFormat.MATRIX, 
				false,
				false,
				true); // MATRIX_FORMAT_NODEID
		
		System.out.println("Vertices: " + graph.vertexSet());
		System.out.println("Arestas: " + graph.edgeSet());
	}
}
