import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addLocation(String id) {
        adjacencyList.putIfAbsent(id, new ArrayList<>());
    }

    public void addBidirectionalEdge(String a, String b, int weight) {
        addLocation(a);
        addLocation(b);
        adjacencyList.get(a).add(new Edge(b, weight));
        adjacencyList.get(b).add(new Edge(a, weight));
    }

    public List<Edge> getNeighbours(String id) {
        return adjacencyList.getOrDefault(id, Collections.emptyList());
    }
}
