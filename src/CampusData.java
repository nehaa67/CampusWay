import java.util.*;

public class CampusData {
    private final Map<String, Location> locations = new LinkedHashMap<>();
    private final Graph graph = new Graph();

    public CampusData() {
        loadLocations();
        loadConnections();
    }

    private void add(Location l) {
        locations.put(l.getId(), l);
        graph.addLocation(l.getId());
    }

    private void loadLocations() {
        // Coordinates are approximate positions on the supplied floor-plan image.
        add(new Location("GATE1", "Gate No. 01 / Campus Check-in", "Ground Floor", "Entrance", "Main campus check-in and staff parking side.", 555, 389));
        add(new Location("GATE2", "Gate No. 02", "Ground Floor", "Entrance", "Campus entrance near the left side of the ground-floor layout.", 38, 150));
        add(new Location("GATE3", "Gate No. 03", "Ground Floor", "Entrance", "Campus entrance near the canteen side.", 38, 45));
        add(new Location("QUAD", "Quad Area", "Ground Floor", "Landmark", "Central open quad area and a major navigation landmark.", 340, 205));
        add(new Location("RECEPTION", "Reception / Admission Enquiry", "Ground Floor", "Office", "Reception and admission enquiry area near the central quad.", 340, 286));
        add(new Location("PRINCIPAL", "Principal Room", "Ground Floor", "Office", "S003: Principal's office.", 380, 330));
        add(new Location("PLACEMENT", "Placement Cell", "Ground Floor", "Office", "S004: Placement Cell.", 295, 332));
        add(new Location("FIRSTAID", "First Aid Room", "Ground Floor", "Health", "S001A: First aid room.", 490, 330));
        add(new Location("FEES", "Fees Counter", "Ground Floor", "Office", "Fees counter near the lower-left side of the quad.", 260, 385));
        add(new Location("IGNOU", "IGNOU Study Centre", "Ground Floor", "Centre", "IGNOU Study Centre 1632.", 445, 385));
        add(new Location("CANTEEN", "Canteen", "Ground Floor", "Food", "Campus canteen near Gate No. 03.", 90, 60));
        add(new Location("GYMKHANA", "Gymkhana", "Ground Floor", "Sports", "Gymkhana and indoor sports activity area.", 700, 80));
        add(new Location("SPORTS", "Multipurpose Sports Complex", "Ground Floor", "Sports", "Sports complex with multiple outdoor/indoor activities.", 720, 170));
        add(new Location("MACHLAB", "Hydraulic / Machinery Labs", "Ground Floor", "Laboratory", "J002/J003 machinery and hydraulic laboratory area.", 475, 90));
        add(new Location("INNOVATION", "Innovation and Research Centre", "Ground Floor", "Research", "P002: Innovation and Research Centre.", 540, 135));
        add(new Location("ITOFFICE", "Department of IT", "Ground Floor", "Department", "R002: Department of IT area.", 205, 245));
        add(new Location("STRENGTH", "Strength of Material Lab", "Ground Floor", "Laboratory", "R003: Strength of Material Lab.", 205, 210));
        add(new Location("WORKSHOP", "Engineering Workshop", "Ground Floor", "Workshop", "T001-T004 engineering workshop area.", 610, 260));
        add(new Location("LIFT_G", "Ground Floor Lift", "Ground Floor", "Transport", "Lift access to upper floors.", 335, 300));
        add(new Location("STAIR_A", "Main Staircase", "Ground Floor", "Transport", "Main staircase connection to upper floors.", 245, 430));
        add(new Location("STAIR_B", "Central Staircase", "Ground Floor", "Transport", "Central staircase used for upper-floor access.", 545, 420));

        add(new Location("ARCHLIB", "Architecture Library", "1st Floor", "Library", "Architecture library area on the first floor.", 175, 610));
        add(new Location("P101", "P101 Computer Lab", "1st Floor", "Laboratory", "Computer laboratory.", 300, 610));
        add(new Location("P100", "P100 Computer Lab", "1st Floor", "Laboratory", "Computer laboratory.", 300, 555));
        add(new Location("P103", "P103 Computer Lab", "1st Floor", "Laboratory", "Computer laboratory.", 300, 500));
        add(new Location("J103", "J103 Automobile & Mechatronics Lab", "1st Floor", "Laboratory", "Automobile and mechatronics laboratory.", 120, 500));
        add(new Location("J101", "J101 Material Tech Lab", "1st Floor", "Laboratory", "Material technology laboratory.", 230, 455));
        add(new Location("P104", "P104 & P105 Dr. Daphne Pillai Lab", "1st Floor", "Laboratory", "Engineering laboratory.", 410, 470));
        add(new Location("T101", "T101 Communication Lab", "1st Floor", "Laboratory", "Departmental laboratory area.", 620, 555));
        add(new Location("T102", "T102 Applied Physics Lab", "1st Floor", "Laboratory", "Applied physics laboratory.", 600, 500));
        add(new Location("S101", "S101 & S102 Department of IT", "1st Floor", "Department", "Department of IT rooms.", 720, 570));
        add(new Location("S103", "S103 & S104 Computer Engineering", "1st Floor", "Department", "Computer Engineering rooms.", 720, 620));
        add(new Location("R103", "R103 Electronics Lab", "1st Floor", "Laboratory", "Electronics laboratory area.", 510, 760));
        add(new Location("R104", "R104 & R105 Electronics Lab", "1st Floor", "Laboratory", "Electronics laboratory area.", 410, 760));
        add(new Location("L102", "L102", "1st Floor", "Classroom", "Room L102.", 310, 690));
        add(new Location("LIBSTAIR", "Staircase to 2nd Floor", "1st Floor", "Transport", "Staircase used to access the second floor.", 455, 850));

        add(new Location("J201", "J201 CAD / FEA Lab", "2nd Floor", "Laboratory", "CAD/FEA laboratory on the second floor.", 330, 965));
        add(new Location("J202", "J202 Lab Area", "2nd Floor", "Laboratory", "Second-floor laboratory area.", 410, 1010));
        add(new Location("MARCHLAB", "March Urban Design / Simulation Lab", "2nd Floor", "Laboratory", "Architecture and design lab area.", 120, 980));
        add(new Location("MSTUDIO", "March Studio", "2nd Floor", "Studio", "Architecture/design studio.", 150, 1050));
        add(new Location("STAIR2", "2nd Floor Staircase", "2nd Floor", "Transport", "Access point from first floor.", 330, 930));
    }

    private void loadConnections() {
        connect("GATE1", "RECEPTION", 35);
        connect("GATE2", "ITOFFICE", 35);
        connect("GATE3", "CANTEEN", 20);
        connect("CANTEEN", "QUAD", 55);
        connect("GATE2", "QUAD", 80);
        connect("QUAD", "RECEPTION", 45);
        connect("QUAD", "PRINCIPAL", 55);
        connect("QUAD", "PLACEMENT", 55);
        connect("QUAD", "FIRSTAID", 65);
        connect("RECEPTION", "LIFT_G", 25);
        connect("LIFT_G", "STAIR_A", 40);
        connect("LIFT_G", "STAIR_B", 45);
        connect("QUAD", "FEES", 55);
        connect("FEES", "IGNOU", 65);
        connect("QUAD", "INNOVATION", 90);
        connect("INNOVATION", "MACHLAB", 45);
        connect("QUAD", "GYMKHANA", 130);
        connect("GYMKHANA", "SPORTS", 70);
        connect("QUAD", "WORKSHOP", 100);
        connect("ITOFFICE", "STRENGTH", 35);
        connect("STRENGTH", "QUAD", 90);
        connect("STAIR_A", "J101", 60);
        connect("STAIR_B", "P104", 65);
        connect("STAIR_B", "T102", 85);
        connect("STAIR_A", "ARCHLIB", 110);
        connect("J101", "J103", 70);
        connect("J101", "P103", 85);
        connect("P103", "P100", 55);
        connect("P100", "P101", 55);
        connect("P101", "L102", 80);
        connect("P104", "T102", 100);
        connect("T102", "T101", 55);
        connect("T101", "S101", 55);
        connect("S101", "S103", 60);
        connect("S103", "R103", 90);
        connect("R103", "R104", 55);
        connect("R104", "LIBSTAIR", 75);
        connect("ARCHLIB", "J103", 75);
        connect("LIBSTAIR", "STAIR2", 10);
        connect("STAIR2", "J201", 55);
        connect("J201", "J202", 65);
        connect("J201", "MARCHLAB", 120);
        connect("MARCHLAB", "MSTUDIO", 70);
    }

    private void connect(String a, String b, int weight) {
        graph.addBidirectionalEdge(a, b, weight);
    }

    public Map<String, Location> getLocations() { return locations; }
    public Graph getGraph() { return graph; }
}
