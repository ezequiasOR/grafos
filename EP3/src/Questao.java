


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
import java.util.HashMap;
import java.io.Serializable;

import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.AlphaCentrality;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.GmlImporter;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.AttributeType;
import org.jgrapht.io.DefaultAttribute;
import org.jgrapht.graph.DefaultEdge;

public class Questao {
	public static void main(String[] args) {
		Graph<DefaultVertex, RelationshipEdge> graph = new Multigraph<>(RelationshipEdge.class);
		importGraphGML(graph, "./files/email-Eu-core-parte2.gml");
		System.out.println("Imprimindo grafo");
		printGraph(graph);
		
		System.out.println("Alpha Centrality");
  	   	AlphaCentrality <DefaultVertex, RelationshipEdge> alphaCentrality = new AlphaCentrality <> (graph,0.001);
  	   	printOrderedVertexMeasures (alphaCentrality.getScores(),0,true);
		
  	   	System.out.println("\n");
		System.out.println("Betweenness Centrality");
  	   	BetweennessCentrality <DefaultVertex, RelationshipEdge> betweennessCentrality = new BetweennessCentrality <> (graph,true);
  	   	printOrderedVertexMeasures (betweennessCentrality.getScores(),0,true);
  	   	
  	   	System.out.println("\n");
  	   	System.out.println("Assortatividade");
  	   	System.out.println(assortativityCoefficient(graph));
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
    
    static <V,E> double assortativityCoefficient (Graph <V, E> graph) {
    	double edgeCount = graph.edgeSet().size();
        double n1 = 0, n2 = 0, dn = 0;

        for (E e : graph.edgeSet()) {
            int d1 = graph.degreeOf(graph.getEdgeSource(e));
            int d2 = graph.degreeOf(graph.getEdgeTarget(e));

            n1 += d1 * d2;
            n2 += d1 + d2;
            dn += d1 * d1 + d2 * d2;
        }
        n1 /= edgeCount;
        n2 = (n2 / (2 * edgeCount)) * (n2 / (2 * edgeCount));
        dn /= (2 * edgeCount);
        
        return (n1 - n2) / (dn - n2);
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

class RelationshipEdge {
	private static final long serialVersionUID = 8238755873387699328L;
	private Object v1;
	private Object v2;
	private Map<String, Attribute> att;

	// Construtores
	public RelationshipEdge(Object v1, Object v2, Map<String, Attribute> att) {
		this.v1 = v1;
		this.v2 = v2;
		this.att = att;
	}
	public RelationshipEdge(Object v1, Object v2, String label) {
		this.v1 = v1;
		this.v2 = v2;
		att = new HashMap <String,Attribute> ();
		att.put("label",new DefaultAttribute<String>(label,AttributeType.STRING));		
	}

    // Métodos de Acesso	
	public String getLabel() {
		Object o = att.get("label"); 
		if (o == null) { 
			return ("{" + v1 + "," + v2 + "}"); 
		} else 
			return o.toString(); 
	}
	public Object getNeighbor(Object v) {
		if (v.equals(v1)) { 
			return v2; 
		} else 
			return v1; 
	}
	public Object getV1() {
		return v1;
	}
	public Object getV2() {
		return v2;
	}
	
	public Object getEdgeSource() {
		return v1;
	}
	public Object getEdgeTarget() {
		return v2;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
		result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
		return result;
	}

	public boolean equals(RelationshipEdge e) {
		return (this.getLabel()).equals(e.getLabel());
	}

	public String toString() {
		Object o = att.get("label"); // captura o lable da aresta
		if (o == null) { // analisa se este eh nulo. se for...
			return ("{" + v1 + "," + v2 + "}"); // retorna uma representacao no formato "{v1,v2}"
		} else // caso contrario...
			return (att.get("label")).toString() + "->{" + v1 + "," + v2 + "}"; // retorna uma representacao no formato "lable->{v1,v2}"
	}

}

class DefaultVertex {
	private static final long serialVersionUID = -4861285584479124799L;
	private String id;
	private Map<String, Attribute> att;

    // Construtores
	public DefaultVertex(String id, Map<String, Attribute> att) {
		this.id = id;
		this.att = att;
	}
	public DefaultVertex(String id) {
		this.id = id;
		this.att = new HashMap <String,Attribute> ();
	}

	// Métodos de Acesso
	public String getId() {
		return id;
	}

	public String getLabel() {
		String label;
		try {
		   label = (att.get("label")).toString(); 
	    } catch (Exception e) {
		   label = id;
	    }
		return label;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(DefaultVertex v) {
		String s1 = this.getId();
		String s2 = v.getId();
		if (s2 instanceof String)
		return s1.equals(s2);
		else return false;
	}

	public String toString() {
		return this.getLabel();
	}
}
