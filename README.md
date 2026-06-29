# A rule-based expert system for analyzing the state and attainability of goals in Terraforming Mars

This project is a rule-based strategic advisor for the board game **Terraforming Mars**, developed using **Drools**. It analyzes the current game state and provides strategic recommendations by combining _Forward Chaining_ (FC), _Backward Chaining_ (BC), and _Complex Event Processing_ (CEP).

## Project Structure

The backend is organized into three Maven projects:

- **model** – shared domain model, facts, enums, and outputs.
- **kjar** – Drools knowledge module containing all business rules (FC, BC, CEP, and templates).
- **service** – Spring Boot application responsible for loading the knowledge base, initializing the game state, and exposing the backend API.

The frontend is implemented as a separate **React** application.

## Project Proposal

The accompanying PDF document contains the project proposal, including the problem description, methodology, and planned implementation of all rules. Furthermore, it includes the class diagram as well.

---

Author: David Makan
