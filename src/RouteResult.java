import java.util.List;

public class RouteResult {
    private final List<String> path;
    private final int distance;

    public RouteResult(List<String> path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<String> getPath() { return path; }
    public int getDistance() { return distance; }
}
