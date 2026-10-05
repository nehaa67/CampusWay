import java.util.*;

public class Dijkstra {
    private static class Node implements Comparable<Node> {
        String id;
        int distance;

        Node(String id, int distance) {
            this.id = id;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public static RouteResult shortestPath(Graph graph, String start, String target) {
        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>();

        distance.put(start, 0);
        queue.add(new Node(start, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.distance != distance.getOrDefault(current.id, Integer.MAX_VALUE)) {
                continue;
            }
            if (current.id.equals(target)) break;

            for (Edge edge : graph.getNeighbours(current.id)) {
                int newDistance = current.distance + edge.getWeight();
                if (newDistance < distance.getOrDefault(edge.getDestination(), Integer.MAX_VALUE)) {
                    distance.put(edge.getDestination(), newDistance);
                    previous.put(edge.getDestination(), current.id);
                    queue.add(new Node(edge.getDestination(), newDistance));
                }
            }
        }

        if (!distance.containsKey(target)) {
            return new RouteResult(Collections.emptyList(), -1);
        }

        LinkedList<String> path = new LinkedList<>();
        String current = target;
        while (current != null) {
            path.addFirst(current);
            if (current.equals(start)) break;
            current = previous.get(current);
        }

        if (!path.getFirst().equals(start)) {
            return new RouteResult(Collections.emptyList(), -1);
        }
        return new RouteResult(path, distance.get(target));
    }
}
