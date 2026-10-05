import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CampusWayGUI extends JFrame {
    private final CampusData data = new CampusData();
    private final JComboBox<Location> fromBox = new JComboBox<>();
    private final JComboBox<Location> toBox = new JComboBox<>();
    private final JComboBox<String> floorBox = new JComboBox<>(new String[]{"All Floors", "Ground Floor", "1st Floor", "2nd Floor"});
    private final JTextArea routeArea = new JTextArea();
    private final JLabel distanceLabel = new JLabel("--");
    private final JLabel stepsLabel = new JLabel("--");
    private final JLabel statusLabel = new JLabel("Select a starting point and destination to begin.");
    private final JLabel detailTitle = new JLabel("Campus location");
    private final JLabel detailMeta = new JLabel("Select a location from the list.");
    private final JTextArea detailText = new JTextArea();
    private final MapPanel mapPanel = new MapPanel(data.getLocations());
    private final JTextField searchField = new JTextField();
    private final JPanel resultCards = new JPanel();

    public CampusWayGUI() {
        setTitle("CampusWay | Smart Indoor Campus Navigation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 820);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1100, 700));
        buildUI();
        populateLocations();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBorder(new EmptyBorder(14, 14, 12, 14));
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildMain(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(15, 8));
        header.setBackground(new Color(24, 38, 63));
        header.setBorder(new EmptyBorder(17, 22, 17, 22));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("CampusWay");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        JLabel subtitle = new JLabel("Smart Indoor Campus Navigation");
        subtitle.setForeground(new Color(210, 222, 240));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(subtitle);

        JPanel badge = new JPanel();
        badge.setOpaque(false);
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        JLabel campus = new JLabel("PILLAI CAMPUS");
        campus.setForeground(Color.WHITE);
        campus.setFont(new Font("SansSerif", Font.BOLD, 13));
        campus.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel version = new JLabel("Java Micro Project • v2.0");
        version.setForeground(new Color(175, 194, 220));
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setAlignmentX(Component.RIGHT_ALIGNMENT);
        badge.add(campus);
        badge.add(Box.createVerticalStrut(4));
        badge.add(version);

        header.add(left, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout(14, 14));
        main.setOpaque(false);
        main.add(buildNavigationPanel(), BorderLayout.WEST);
        main.add(buildMapPanel(), BorderLayout.CENTER);
        return main;
    }

    private JPanel buildNavigationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(375, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 237)),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Where do you want to go?");
        heading.setFont(new Font("SansSerif", Font.BOLD, 20));
        heading.setForeground(new Color(31, 41, 55));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(heading);
        top.add(Box.createVerticalStrut(4));

        JLabel hint = new JLabel("Choose any campus location and get the shortest route.");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(new Color(107, 114, 128));
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(hint);
        top.add(Box.createVerticalStrut(15));

        top.add(sectionLabel("FROM"));
        fromBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        top.add(fromBox);
        top.add(Box.createVerticalStrut(9));

        top.add(sectionLabel("TO"));
        toBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        top.add(toBox);
        top.add(Box.createVerticalStrut(11));

        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 0));
        buttons.setOpaque(false);
        JButton routeButton = primaryButton("Find Route");
        routeButton.addActionListener(e -> findRoute());
        JButton swapButton = secondaryButton("⇄ Swap");
        swapButton.addActionListener(e -> swapLocations());
        buttons.add(routeButton);
        buttons.add(swapButton);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(buttons);
        top.add(Box.createVerticalStrut(13));

        JPanel stats = new JPanel(new GridLayout(1, 2, 8, 0));
        stats.setOpaque(false);
        stats.add(statCard("ROUTE LENGTH", distanceLabel));
        stats.add(statCard("STOPS", stepsLabel));
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(stats);

        panel.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setOpaque(false);
        JLabel routeHeading = sectionLabel("DIRECTIONS");
        center.add(routeHeading, BorderLayout.NORTH);

        routeArea.setEditable(false);
        routeArea.setLineWrap(true);
        routeArea.setWrapStyleWord(true);
        routeArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        routeArea.setForeground(new Color(51, 65, 85));
        routeArea.setBackground(new Color(248, 250, 252));
        routeArea.setBorder(new EmptyBorder(11, 11, 11, 11));
        JScrollPane routeScroll = new JScrollPane(routeArea);
        routeScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        center.add(routeScroll, BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(sectionLabel("EXPLORE LOCATIONS"));
        bottom.add(Box.createVerticalStrut(6));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        searchField.putClientProperty("JTextField.placeholderText", "Search room, lab, office...");
        searchField.addActionListener(e -> searchLocations());
        bottom.add(searchField);
        bottom.add(Box.createVerticalStrut(6));

        resultCards.setLayout(new BoxLayout(resultCards, BoxLayout.Y_AXIS));
        resultCards.setBackground(Color.WHITE);
        JScrollPane cardsScroll = new JScrollPane(resultCards);
        cardsScroll.setPreferredSize(new Dimension(330, 130));
        cardsScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        bottom.add(cardsScroll);
        bottom.add(Box.createVerticalStrut(8));

        detailTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        detailTitle.setForeground(new Color(31, 41, 55));
        detailMeta.setFont(new Font("SansSerif", Font.PLAIN, 10));
        detailMeta.setForeground(new Color(100, 116, 139));
        detailText.setEditable(false);
        detailText.setLineWrap(true);
        detailText.setWrapStyleWord(true);
        detailText.setFont(new Font("SansSerif", Font.PLAIN, 10));
        detailText.setForeground(new Color(71, 85, 105));
        detailText.setOpaque(false);
        bottom.add(detailTitle);
        bottom.add(detailMeta);
        bottom.add(detailText);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel statCard(String label, JLabel value) {
        JPanel card = new JPanel();
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(new EmptyBorder(7, 9, 7, 9));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 8));
        l.setForeground(new Color(100, 116, 139));
        value.setFont(new Font("SansSerif", Font.BOLD, 12));
        value.setForeground(new Color(30, 64, 175));
        card.add(l);
        card.add(Box.createVerticalStrut(4));
        card.add(value);
        return card;
    }

    private JPanel buildMapPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 237)),
                new EmptyBorder(10, 10, 10, 10)));

        JPanel toolbar = new JPanel(new BorderLayout(8, 0));
        toolbar.setOpaque(false);
        JLabel title = new JLabel("Campus map");
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        title.setForeground(new Color(31, 41, 55));
        toolbar.add(title, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        controls.setOpaque(false);
        floorBox.setPreferredSize(new Dimension(135, 30));
        floorBox.addActionListener(e -> mapPanel.setSelectedFloor((String) floorBox.getSelectedItem()));
        controls.add(new JLabel("VIEW"));
        controls.add(floorBox);
        JButton reset = secondaryButton("Reset");
        reset.addActionListener(e -> {
            floorBox.setSelectedItem("All Floors");
            mapPanel.setRoute(List.of());
            routeArea.setText("");
            distanceLabel.setText("--");
            stepsLabel.setText("--");
            statusLabel.setText("Map reset. Select a route to begin.");
        });
        controls.add(reset);
        toolbar.add(controls, BorderLayout.EAST);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(mapPanel, BorderLayout.CENTER);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        legend.setOpaque(false);
        legend.add(legendItem(new Color(22, 163, 74), "Start"));
        legend.add(legendItem(new Color(220, 38, 38), "Destination"));
        legend.add(legendItem(new Color(37, 99, 235), "Shortest route"));
        JLabel note = new JLabel("Floor tabs help you inspect each level separately.");
        note.setFont(new Font("SansSerif", Font.PLAIN, 10));
        note.setForeground(new Color(100, 116, 139));
        legend.add(note);
        panel.add(legend, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel legendItem(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JPanel dot = new JPanel();
        dot.setBackground(color);
        dot.setPreferredSize(new Dimension(10, 10));
        p.add(dot);
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 10));
        p.add(l);
        return p;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(71, 85, 105));
        footer.add(statusLabel, BorderLayout.WEST);
        JLabel tech = new JLabel("JAVA  •  OOP  •  GRAPH  •  DIJKSTRA");
        tech.setFont(new Font("SansSerif", Font.BOLD, 10));
        tech.setForeground(new Color(71, 85, 105));
        footer.add(tech, BorderLayout.EAST);
        return footer;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 9));
        l.setForeground(new Color(100, 116, 139));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(37, 99, 235));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return b;
    }

    private JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 11));
        b.setForeground(new Color(51, 65, 85));
        b.setBackground(new Color(248, 250, 252));
        b.setFocusPainted(false);
        return b;
    }

    private void populateLocations() {
        List<Location> list = new ArrayList<>(data.getLocations().values());
        list.sort(Comparator.comparing(Location::getFloor).thenComparing(Location::getName));
        for (Location l : list) {
            fromBox.addItem(l);
            toBox.addItem(l);
        }
        if (fromBox.getItemCount() > 0) fromBox.setSelectedIndex(0);
        if (toBox.getItemCount() > 1) toBox.setSelectedIndex(1);
        refreshCards(list);
    }

    private void swapLocations() {
        Location a = (Location) fromBox.getSelectedItem();
        Location b = (Location) toBox.getSelectedItem();
        fromBox.setSelectedItem(b);
        toBox.setSelectedItem(a);
    }

    private void findRoute() {
        Location start = (Location) fromBox.getSelectedItem();
        Location target = (Location) toBox.getSelectedItem();
        if (start == null || target == null) return;

        if (start.getId().equals(target.getId())) {
            routeArea.setText("You are already at this location. No walking route is required.");
            distanceLabel.setText("0 units");
            stepsLabel.setText("1");
            mapPanel.setRoute(List.of(start.getId()));
            floorBox.setSelectedItem(start.getFloor());
            statusLabel.setText("Start and destination are the same location.");
            showDetails(start);
            return;
        }

        RouteResult result = Dijkstra.shortestPath(data.getGraph(), start.getId(), target.getId());
        if (result.getPath().isEmpty()) {
            routeArea.setText("No route is currently available between these locations.");
            distanceLabel.setText("--");
            stepsLabel.setText("--");
            mapPanel.setRoute(List.of());
            statusLabel.setText("No route found. Try another pair of locations.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("START\n").append(start.getName()).append("\n\n");
        for (int i = 0; i < result.getPath().size(); i++) {
            Location l = data.getLocations().get(result.getPath().get(i));
            sb.append(String.format("%02d  %s", i + 1, l.getName()));
            if (i < result.getPath().size() - 1) sb.append("\n     ↓\n");
            else sb.append("\n");
        }
        sb.append("\nDESTINATION\n").append(target.getName());
        routeArea.setText(sb.toString());

        distanceLabel.setText(result.getDistance() + " units");
        stepsLabel.setText(String.valueOf(result.getPath().size()));
        statusLabel.setText("Shortest route calculated using Dijkstra's algorithm.");
        mapPanel.setRoute(result.getPath());
        floorBox.setSelectedItem(target.getFloor());
        showDetails(target);
    }

    private void searchLocations() {
        String q = searchField.getText().trim().toLowerCase();
        List<Location> filtered = new ArrayList<>();
        for (Location l : data.getLocations().values()) {
            if (q.isEmpty() || l.getName().toLowerCase().contains(q)
                    || l.getCategory().toLowerCase().contains(q)
                    || l.getFloor().toLowerCase().contains(q)
                    || l.getZone().toLowerCase().contains(q)) {
                filtered.add(l);
            }
        }
        refreshCards(filtered);
    }

    private void refreshCards(List<Location> list) {
        resultCards.removeAll();
        int count = 0;
        for (Location l : list) {
            if (count++ >= 14) break;
            JButton card = new JButton("<html><b>" + escape(l.getName()) + "</b><br>"
                    + "<font color='#64748B'>" + escape(l.getFloor()) + " • " + escape(l.getZone()) + "</font></html>");
            card.setHorizontalAlignment(SwingConstants.LEFT);
            card.setFont(new Font("SansSerif", Font.PLAIN, 11));
            card.setBackground(Color.WHITE);
            card.setFocusPainted(false);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(235, 238, 242)),
                    new EmptyBorder(7, 8, 7, 8)));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            card.addActionListener(e -> {
                toBox.setSelectedItem(l);
                showDetails(l);
                floorBox.setSelectedItem(l.getFloor());
            });
            resultCards.add(card);
            resultCards.add(Box.createVerticalStrut(4));
        }
        resultCards.revalidate();
        resultCards.repaint();
    }

    private void showDetails(Location l) {
        if (l == null) return;
        detailTitle.setText(l.getName());
        detailMeta.setText(l.getFloor() + "  •  " + l.getZone() + "  •  " + l.getCategory());
        detailText.setText(l.getDescription());
    }

    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
