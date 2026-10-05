# CampusWay v2.0

CampusWay is a Java Swing micro-project for indoor campus navigation. It models campus locations as a weighted graph and uses Dijkstra's shortest-path algorithm to calculate routes.

## What's improved in v2
- Cleaner two-panel interface with less visual clutter.
- Separate floor views: All Floors, Ground Floor, 1st Floor, 2nd Floor.
- Route length and number of stops shown as compact statistics.
- Step-by-step directions.
- Search across classrooms, labs, offices, facilities, floors and navigation zones.
- Location details panel.
- Route markers and labels on the supplied campus floor-plan.
- Reset and Swap controls.

## Technologies
- Java 17+
- Java Swing / AWT
- Object-Oriented Programming
- Graph data structure using adjacency lists
- Dijkstra shortest-path algorithm

## Windows PowerShell
From the inner `CampusWay` folder:

```powershell
javac -d out (Get-ChildItem src\*.java).FullName
java -cp out Main
```

Or:

```powershell
.\compile-and-run.bat
```

## Important accuracy note
The supplied floor-plan is a combined image showing the Ground Floor, 1st Floor and part of the 2nd Floor. The displayed floor ranges, route coordinates and graph connections are a prototype based on that supplied plan. Verify exact corridor and staircase connections before presenting the route as a real-time operational navigation system.

Navigation zones in the UI are approximate display zones, not official building-wing names.

## Viva explanation
The campus is represented as a weighted graph. Each location is a vertex and each walkable connection is a weighted edge. Dijkstra's algorithm finds the minimum-cost path between the selected source and destination. The Swing GUI converts the returned path into step-by-step directions and overlays the route on the selected floor view.
