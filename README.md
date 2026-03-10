<h1 align="center">ETERNAFALL</h1>

<p align="center">
  <b>Game Development Progress</b>
</p>

<p align="center">
  <img src="https://progress-bar.xyz/32/?width=750&height=28"/>
</p>

<p align="center">
  <b>Version 0.2.3</b>
</p>

<p align="center">
  A pure Java 2D RPG set in a world where gods have fallen, politics dictate survival, and the apocalypse isn't an event — it's a condition.
</p>

---

## 🌑 About

**Eternafall** is a 2D RPG built entirely from scratch using the standard Java library.

No external engines. No frameworks. No plugins. No build tools.

The world of Eternafall is a political-mythological dark fantasy — where divine structures have collapsed, factions war over the remnants of godhood, and every encounter carries the weight of a civilization on the edge of erasure. You move through a world that doesn't wait for you, fight in battles that punish passivity, and carry a burden that the world hasn't named yet.

Design influences:
- Creature system depth inspired by **Pokémon**
- Expedition-driven progression inspired by **Clair Obscur: Expedition 33**
- Strategic difficulty reminiscent of **Elden Ring**
- Encounter tension comparable to **Hades**

All adapted into a structured, original turn-based RPG framework.

---

## 🛠 Tech Stack

<p align="left">
  <img src="https://img.shields.io/badge/Language-Java-orange?logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Graphics-Java%20AWT%20%2B%20Swing-blue" />
  <img src="https://img.shields.io/badge/Architecture-OOP-lightgrey" />
  <img src="https://img.shields.io/badge/Dependencies-None-success" />
</p>

- **Language**: Java
- **Graphics API**: Java AWT + Swing (Java 2D)
- **Architecture**: Object-Oriented Programming
- **Dependencies**: None (pure standard Java library)

---

## 🌍 Core Features

### Exploration
- Real-time top-down movement
- Tile-based world rendering
- Seamless area transitions
- NPC interaction and dialogue
- Encounter trigger system

### ⚔ Combat — AP Economy + Parry System

Eternafall uses a turn-based combat system built around **Action Points (AP)** and a real-time **parry mechanic**.

Each round, the player starts with a pool of AP. Offense and defense share the same resource — what you spend attacking is what you don't have left to defend with. The player always moves first, but that advantage comes with a decision: how much do you commit to offense before you need to survive what comes back.

Defense isn't passive. When the enemy attacks, a timing window opens. Parry within it and the hit is voided — the enemy takes counter damage and loses AP. Miss the window, misread the enemy's intent, or attempt a parry on an unparryable strike, and there are consequences.

Build variables — armor, armament, talisman, and stance — all interact with this economy. A stance might disable parry entirely in exchange for burst potential. A talisman might trade AP for HP. Every build choice reshapes how the AP loop plays out.

### 🧠 Architecture
- Custom fixed timestep game loop
- Explicit state separation
  - `ExplorationState`
  - `CombatState`
  - `MenuState`
- Layered rendering pipeline
- Clean and modular OOP hierarchy

---

## 📌 Status

Active development. Core systems being implemented. Combat logic in design finalization phase.