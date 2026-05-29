# UNIFIED PRIOR KNOWLEDGE BASE: HOTEL PRICING SYSTEM (HPS)

## SECTION 1: ATTRIBUTE-DRIVEN DESIGN (ADD) 3.0 METHODOLOGY
The architectural design process must strictly follow the 7 steps of the ADD 3.0 method sequentially for each design round or iteration:

- **Step 1 Review Inputs**: Review inputs and identify which requirements will be considered as architectural drivers.
- **Step 2 Establish the Iteration Goal by Selecting Drivers**: Define the specific focus of the iteration by selecting a subset of architectural drivers.
- **Step 3 Choose One or More Elements of the System to Refine**: 
  - For greenfield development, start by establishing the system context and selecting the system itself for refinement by decomposition.
  - For subsequent iterations, choose to refine elements identified in prior iterations.
- **Step 4 Choose One or More Design Concepts That Satisfy the Selected Drivers**: Identify alternative design concepts (architectural patterns, tactics) and select the most appropriate one.
- **Step 5 Instantiate Architectural Elements, Allocate Responsibilities, and Define Interfaces**: Create element instances based on selected design concepts, assign explicit responsibilities, and establish relationships/interfaces for collaboration.
- **Step 6 Sketch Views and Record Design Decisions**: Preserved representations of structures (using Mermaid or PlantUML) and record significant decisions with their rationale.
- **Step 7 Perform Analysis of Current Design and Review Iteration Goal**: Verify if the partial design satisfies the iteration goal and determine if additional iterations are required.

---

## SECTION 2: HOTEL PRICING SYSTEM (HPS) CASE STUDY DESCRIPTION

### Design Purpose
This project is a greenfield development involving the complete replacement of an existing legacy system. The purpose is to make initial decisions to support building the system from scratch.

### Primary Functionality (Use Case Descriptions)
- **HPS-1: Log In**: A user (commercial or administrator) provides credentials in a login window. The system checks credentials against a user identity service. If successful, access is granted. Users can only query and change hotels they are authorized for.
- **HPS-2: Change Prices**: A user selects an authorized hotel and dates to change prices to a base rate or a fixed rate. All rates calculated from the base rate are computed at that point. Simulated price changes are allowed before actual commitment. Committed prices are pushed to the Channel Management System for external queries.
- **HPS-3: Query Prices**: A user or external system queries prices for a given hotel through the user interface or a query API.
- **HPS-4: Manage Hotels**: An administrator adds, changes, or modifies hotel information, including hotel tax rates, available rates, and room types.
- **HPS-5: Manage Rates**: An administrator adds, changes, or modifies rates, including defining the calculation business rules for different rates.
- **HPS-6: Manage Users**: An administrator changes permissions for a given user.

---

## SECTION 3: QUALITY ATTRIBUTE SCENARIOS (QA)
- **QA-1 (Performance)**: A base rate price is changed for a specific hotel and date during normal operation; the prices for all the rates and room types for the hotel are published (ready for query) in less than 100 ms. (Associated Use Case: HPS-2 | Importance: High | Difficulty: High)
- **QA-2 (Reliability)**: A user performs multiple price changes on a given hotel: 100% of the price changes are published successfully and received by the Channel Management System. (Associated Use Case: HPS-2 | Importance: High | Difficulty: High)
- **QA-3 (Availability)**: Pricing queries uptime SLA must be 99.9% outside of maintenance windows. (Associated Use Case: All | Importance: High | Difficulty: High)
- **QA-4 (Scalability)**: The system will initially support a minimum of 100,000 price queries per day through its API and should be capable of handling up to 1,000,000 without decreasing average latency by more than 20%. (Associated Use Case: HPS-3 | Importance: High | Difficulty: High)
- **QA-5 (Security)**: A user logs into the system through the front-end. Credentials validated against the User Identity Service, and users are presented only with authorized functions. (Associated Use Case: All | Importance: High | Difficulty: Medium)
- **QA-6 (Modifiability)**: Support for a price query endpoint with a different protocol than REST (e.g., gRPC) is added to the system. The new endpoint does not require changes to core components. (Associated Use Case: All | Importance: Medium | Difficulty: Medium)
- **QA-7 (Deployability)**: The application is moved between nonproduction environments as part of the development process. No changes in the code are needed. (Associated Use Case: All | Importance: Medium | Difficulty: Medium)
- **QA-8 (Monitorability)**: An operator wishes to measure the performance and reliability of price publication during operation. The system provides a mechanism to collect 100% of these measures as needed. (Associated Use Case: HPS-2 | Importance: Medium | Difficulty: Medium)
- **QA-9 (Testability)**: 100% of the system and its elements should support integration testing independently of external systems. (Associated Use Case: All | Importance: Medium | Difficulty: Medium)

---

## SECTION 4: ARCHITECTURAL CONCERNS (CRN) & CONSTRAINTS
- **CRN-1**: Establish an overall initial system structure.
- **CRN-2**: Leverage the team's explicit knowledge about **Java technologies**, the **Angular framework**, and **Kafka**.
- **CRN-3**: Allocate work to members of the development team.
- **CRN-4**: Avoid introducing technical debt.
- **CRN-5**: Set up a continuous deployment infrastructure.

---

## SECTION 5: STRICT SYSTEM OPERATIONAL CONSTRAINTS
1. **No External Knowledge**: No external domain knowledge beyond this provided prior knowledge base is allowed.
2. **No Few-Shot Examples**: No handcrafted demonstration outputs or examples are allowed in reasoning.
3. **No Augmentation**: No additional task reinterpretation or requirement augmentation is permitted.
4. **Explicit Derivation**: All architectural decisions must be explicitly derived from these instructions; no implicit rule bases are permitted.
5. **Diagram Output Requirement**: All structural representations must be generated natively using raw Mermaid or PlantUML code blocks.
