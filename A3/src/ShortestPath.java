import sim.SYSimulation;

import java.util.*;

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; // Distanz für jeden Knoten
	Map<V,V> pred; // Vorgänger für jeden Knoten
    DirectedGraph<V> graph;
    Heuristic<V> heuristic;
    V destVertex;
	// ...

	/**
	 * Berechnet im Graph g kürzeste Wege nach dem A*-Verfahren.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		graph = g;
		heuristic = h;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
        destVertex = g;
        LinkedList<V> kl = new LinkedList<>();

        for (V v : graph.getVertexSet()) {
            dist.put(v,Double.POSITIVE_INFINITY);
            pred.put(v,null);
        }

        kl.add(s);
        dist.replace(s,0.0);

        while (!kl.isEmpty()){
			V current;
			if(heuristic == null)
            	current = dist.entrySet().stream().filter(x -> kl.contains(x.getKey())).min(Comparator.comparingDouble(Map.Entry::getValue)).get().getKey();
			else
				current = dist.entrySet().stream().filter(x -> kl.contains(x.getKey())).min(Comparator.comparingDouble(x -> (x.getValue()+heuristic.estimatedCost(x.getKey(), g)))).get().getKey();


			System.out.println("Visited: " + current);

            kl.remove(current);

            if (heuristic != null && current.equals(destVertex))
                return;

            for (V v : graph.getSuccessorVertexSet(current)) {
                if (dist.get(v) == Double.POSITIVE_INFINITY)
                    kl.add(v);
                if (dist.get(current) + graph.getWeight(current, v) < dist.get(v)){
                    dist.replace(v, dist.get(current) + graph.getWeight(current, v));
                    pred.replace(v,current);
                }
            }
        }
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
        LinkedList<V> ret = new LinkedList<>();
        V v = destVertex;
        while (true) {
			ret.add(v);
            if(dist.get(ret.getLast()) == 0.0)
                break;
            v = pred.get(v);
        }
		Collections.reverse(ret);
        return ret;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		return dist.get(destVertex);
	}

}
