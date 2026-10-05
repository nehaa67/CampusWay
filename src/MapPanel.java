import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel {
    private BufferedImage mapImage;
    private List<String> route;
    private Map<String, Location> locations;
    private String selectedFloor = "All Floors";
    private boolean showLabels = true;

    // The supplied map is a single vertical floor-plan image.
    // These ranges are approximate display regions, not architectural measurements.
    private static final int GROUND_TOP = 0;
    private static final int GROUND_BOTTOM = 450;
    private static final int FIRST_TOP = 430;
    private static final int FIRST_BOTTOM = 825;
    private static final int SECOND_TOP = 790;
    private static final int SECOND_BOTTOM = 1024;

    public MapPanel(Map<String, Location> locations) {
        this.locations = locations;
        setBackground(new Color(247, 249, 252));
        setPreferredSize(new Dimension(760, 640));
        loadMap();
    }

    private void loadMap() {
        try {
            mapImage = ImageIO.read(new File("assets/campus-floor-map.png"));
        } catch (IOException e) {
            mapImage = null;
        }
    }

    public void setRoute(List<String> route) {
        this.route = route;
        repaint();
    }

    public void setSelectedFloor(String floor) {
        this.selectedFloor = floor;
        repaint();
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
        repaint();
    }

    private int cropTop() {
        return switch (selectedFloor) {
            case "Ground Floor" -> GROUND_TOP;
            case "1st Floor" -> FIRST_TOP;
            case "2nd Floor" -> SECOND_TOP;
            default -> 0;
        };
    }

    private int cropBottom() {
        return switch (selectedFloor) {
            case "Ground Floor" -> GROUND_BOTTOM;
            case "1st Floor" -> FIRST_BOTTOM;
            case "2nd Floor" -> SECOND_BOTTOM;
            default -> mapImage == null ? 1 : mapImage.getHeight();
        };
    }

    private boolean belongsToSelectedFloor(Location l) {
        return "All Floors".equals(selectedFloor) || selectedFloor.equals(l.getFloor());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (mapImage == null) {
            g2.setColor(new Color(70, 80, 95));
            g2.drawString("Campus map image not found. Keep campus-floor-map.png inside assets/.", 30, 40);
            g2.dispose();
            return;
        }

        int top = cropTop();
        int bottom = cropBottom();
        int cropHeight = bottom - top;

        double scale = Math.min((double) (getWidth() - 34) / mapImage.getWidth(),
                (double) (getHeight() - 64) / cropHeight);
        int w = (int) (mapImage.getWidth() * scale);
        int h = (int) (cropHeight * scale);
        int ox = (getWidth() - w) / 2;
        int oy = 42 + Math.max(0, (getHeight() - 64 - h) / 2);

        // Map card background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 18, 18);
        g2.setColor(new Color(225, 230, 238));
        g2.drawRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 18, 18);

        g2.setColor(new Color(30, 43, 68));
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("".equals(selectedFloor) ? "Campus map" : selectedFloor, 28, 33);

        g2.drawImage(mapImage,
                ox, oy, ox + w, oy + h,
                0, top, mapImage.getWidth(), bottom,
                null);

        drawRoute(g2, scale, ox, oy, top);
        if (showLabels && route != null) drawRouteLabels(g2, scale, ox, oy, top);

        g2.dispose();
    }

    private int screenX(Location l, double scale, int ox) {
        return ox + (int) (l.getX() * scale);
    }

    private int screenY(Location l, double scale, int oy, int top) {
        return oy + (int) ((l.getY() - top) * scale);
    }

    private void drawRoute(Graphics2D g2, double scale, int ox, int oy, int top) {
        if (route == null || route.isEmpty()) return;

        // Draw only same-floor segments. Floor changes are represented by the floor tabs.
        g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(37, 99, 235, 185));
        for (int i = 0; i < route.size() - 1; i++) {
            Location a = locations.get(route.get(i));
            Location b = locations.get(route.get(i + 1));
            if (a == null || b == null || !belongsToSelectedFloor(a) || !belongsToSelectedFloor(b)) continue;
            g2.drawLine(screenX(a, scale, ox), screenY(a, scale, oy, top),
                    screenX(b, scale, ox), screenY(b, scale, oy, top));
        }

        for (int i = 0; i < route.size(); i++) {
            Location p = locations.get(route.get(i));
            if (p == null || !belongsToSelectedFloor(p)) continue;
            int x = screenX(p, scale, ox);
            int y = screenY(p, scale, oy, top);
            Color fill = i == 0 ? new Color(22, 163, 74) :
                    i == route.size() - 1 ? new Color(220, 38, 38) : Color.WHITE;
            g2.setColor(fill);
            g2.fillOval(x - 8, y - 8, 16, 16);
            g2.setColor(new Color(30, 41, 59));
            g2.drawOval(x - 8, y - 8, 16, 16);
        }
    }

    private void drawRouteLabels(Graphics2D g2, double scale, int ox, int oy, int top) {
        if (route == null) return;
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        for (int i = 0; i < route.size(); i++) {
            Location p = locations.get(route.get(i));
            if (p == null || !belongsToSelectedFloor(p)) continue;
            int x = screenX(p, scale, ox);
            int y = screenY(p, scale, oy, top);
            String label = (i + 1) + ". " + p.getName();
            int tw = g2.getFontMetrics().stringWidth(label);
            int bx = Math.min(Math.max(5, x + 12), getWidth() - tw - 12);
            int by = Math.max(55, y - 10);
            g2.setColor(new Color(255, 255, 255, 225));
            g2.fillRoundRect(bx - 5, by - 12, tw + 10, 19, 8, 8);
            g2.setColor(new Color(70, 80, 95));
            g2.drawString(label, bx, by + 2);
        }
    }
}
