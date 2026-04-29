# Saardew Valley

## Overview
This repository contains my contributions to the "Saardew Valley" organic farming simulation, a massive group project. The project simulates a deterministic world involving farmers, machines, weather patterns (clouds, rain, sunlight), plant growth cycles, and random incidents across a coordinate-based map.

The project was divided into two distinct phases to simulate a real-world software engineering lifecycle: a volatile **Design Phase** where requirements changed mid-way, followed by a rigorous **Implementation Phase** focused on code quality, testing, and surviving injected code mutants.

Below is a summary of the two phases and my individual contributions to each.

---

### [Phase 1: System Design](./group18-design/)

The design phase focused on creating a highly modular, robust, and extensible software architecture using UML class, state, and sequence diagrams. The design had to accommodate sudden, unannounced changes to the project specification.

**My Core Contributions:**
* **UML Class Diagram Design:** Collaborated on defining the class hierarchy, focusing on the map structure, tile system, and interactions between simulation entities.
* **Parser Architecture:** Defined the structural design for the `MapParser` and `TileFactory`, establishing how JSON configurations would be parsed into runtime objects with strict validation constraints.
* **System Flexibility Planning:** Ensured the design was modular enough to handle volatile specification changes introduced after the initial design review.

---

### [Phase 2: Implementation & Testing](./group18-implementation/)

The implementation phase involved building the fully functional simulation engine in Java based on our previous design. The codebase required strict adherence to clean code principles, extensive unit/integration/system testing, and the ability to defeat automated mutation testing.

**My Core Contributions:**
* **Map Parser & Tile Factory:** Implemented the `MapParser` to read and strictly validate JSON configurations, and developed the `TileFactory` to instantiate the simulation map.
* **Cross-Validation Logic:** Wrote extensive validation methods (`validateTileProperties`, `validateAdjoiningTiles`) to ensure map integrity, boundary rules, and valid plant types for fields and plantations.
* **Coordinate & System Registration:** Implemented translation logic to map coordinates to tiles, set up directional logic, and integrated machine action systems into the broader simulation.
* **Testing & Debugging:** Designed and implemented massive, complex system tests (e.g., "Cloudzilla") to validate the entire simulation lifecycle under stress, focusing on adjoining tiles, machine actions, city expansion incidents, and harvest estimations.

---

## Tech Stack
* Java
* UML (Class, State, Sequence Diagrams)
* JUnit (Unit, Integration, and System Testing)
* Detekt (Code Quality Analysis)
* JSON parsing and validation
