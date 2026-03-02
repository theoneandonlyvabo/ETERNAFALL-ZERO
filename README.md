# ETERNAFALL : The Birth of the First Mark

![Progress](https://progress-bar.xyz/15/?title=Development&width=800)

A pure Java 2D RPG featuring free overworld exploration and deterministic turn-based combat.

EternaFall is built entirely using the standard Java library.  
No external engines. No frameworks. No plugins. No build tools.

All core systems including rendering, input handling, state management, combat logic, and map systems are implemented from scratch using Java AWT and Swing.

---

## Overview

EternaFall combines:

- Free top-down exploration
- Structured turn-based battles
- Modular OOP architecture
- Explicit GameState-driven flow
- Zero external dependencies

Design inspirations include creature systems similar to Pokémon, expedition-style progression inspired by Expedition 33, strategic difficulty philosophy reminiscent of Elden Ring, and encounter intensity comparable to Hades — adapted into a clean turn-based framework.

---

## Core Features

### Exploration
- Real-time top-down movement
- Tile-based world rendering
- Seamless area transitions
- NPC interaction and dialogue
- Encounter trigger system

### Turn-Based Combat
- Initiative-based turn order
- Action abstraction layer
- Skill execution system
- Status effects and buffs/debuffs
- Companion-assisted combat

### Architecture
- Custom fixed timestep game loop
- Explicit state separation (ExplorationState, CombatState, MenuState)
- Layered rendering pipeline
- Clean OOP hierarchy

---

## Tech Stack

- Language: Java
- Graphics: Java AWT + Swing (Java 2D API)
- Architecture: Object-Oriented Programming
- Dependencies: None (pure standard Java)