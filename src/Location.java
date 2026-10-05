public class Location {
    private final String id;
    private final String name;
    private final String floor;
    private final String zone;
    private final String category;
    private final String description;
    private final int x;
    private final int y;

    // Backward-compatible constructor used by the campus data file.
    public Location(String id, String name, String floor, String category,
                    String description, int x, int y) {
        this(id, name, floor, inferZone(x), category, description, x, y);
    }

    public Location(String id, String name, String floor, String zone, String category,
                    String description, int x, int y) {
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.zone = zone;
        this.category = category;
        this.description = description;
        this.x = x;
        this.y = y;
    }

    private static String inferZone(int x) {
        if (x < 270) return "West Zone";
        if (x < 510) return "Central Zone";
        return "East Zone";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getFloor() { return floor; }
    public String getZone() { return zone; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() {
        return name + "  •  " + floor;
    }
}
