# ETERNAFALL : The Birth of the First Mark

<p align="center">
    Development Progress
  <img src="https://progress-bar.xyz/15/?width=750&height=28" />
</p>

<p align="center">
  <b>A pure Java 2D RPG featuring free overworld exploration and deterministic turn-based combat.</b>
</p>

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

## 📖 Overview

EternaFall is built entirely using the standard Java library.  

No external engines.  
No frameworks.  
No plugins.  
No build tools.  

All core systems including rendering, input handling, state management, combat logic, and world systems are implemented from scratch.

The design blends:

- Creature system philosophy inspired by Pokémon  
- Expedition-driven progression inspired by Expedition 33  
- Strategic difficulty influence reminiscent of Elden Ring  
- Encounter tension comparable to Hades  

All adapted into a structured turn-based RPG framework.

---

## 🌍 Core Features

### Exploration
- Real-time top-down movement  
- Tile-based world rendering  
- Seamless area transitions  
- NPC interaction and dialogue  
- Encounter trigger system  

### ⚔ Turn-Based Combat
- Initiative-based turn order  
- Action abstraction layer  
- Skill execution system  
- Status effects (buffs / debuffs)  
- Companion-assisted combat  

### 🧠 Architecture
- Custom fixed timestep game loop  
- Explicit state separation  
  - ExplorationState  
  - CombatState  
  - MenuState  
- Layered rendering pipeline  
- Clean and modular OOP hierarchy  