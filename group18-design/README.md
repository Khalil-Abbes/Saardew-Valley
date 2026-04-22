# Software Engineering Lab: Design Phase

**Course:** Software Engineering Lab (Software-Praktikum), Saarland University  
**Instructors:** Prof. Dr. Sven Apel, Dr. Norman Peitek  

## Project Context
This repository contains the design phase deliverables for "Saardew Valley," a complex organic farming simulation. The design phase required modeling a highly modular and flexible software architecture capable of simulating time ticks, farming actions, weather events (clouds/rain), and various random incidents across a coordinate-based map. The design had to be robust enough to accommodate unannounced requirement changes introduced halfway through the phase.

## My Contributions
During the design phase, I contributed to the core architectural planning and documentation of the simulation system:
* **UML Class Diagram Design:** Collaborated on defining the class hierarchy, particularly focusing on the map structure, tile system, and the interactions between different simulation entities (farms, machines, clouds, and incidents).
* **System Flexibility Planning:** Ensured the design of the Tile and Map parsers was modular and easily extensible to accommodate the volatile specification changes introduced by the instructors after the initial design review.
* **Parser Architecture:** Defined the structural design for the `MapParser` and `TileFactory`, deciding how JSON configurations would be parsed into runtime objects with strict validation constraints.
* **Testing & Integration Strategy:** Co-designed the parser test suite structure with team members to ensure robust cross-validation of map coordinates, tile adjacency, and farm boundaries.
