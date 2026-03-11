<h1 align="center">ETERNAFALL</h1>

<p align="center"><b>Game Development Progress</b></p>

<p align="center">
  <img src="https://progress-bar.xyz/32/?width=750&height=28"/>
</p>

<p align="center"><b>Version 0.2.3</b></p>

<p align="center">
  A world where gods have already lost. What's left is politics, survival, and an apocalypse that never ended.
</p>

---

## About

**Eternafall** is a 2D RPG built entirely from scratch in pure Java — no engines, no frameworks, no dependencies.

The world is a political-mythological dark fantasy. Divine order has collapsed. Factions war over what godhood left behind. You move through a world that doesn't pause for you, fight in systems that punish passivity, and carry a burden the world hasn't named yet.

Every design decision in Eternafall comes from a genuine relationship with games — what works, what doesn't, and what hasn't been tried yet. The result is a system that borrows from the best and belongs to none of them.

---

## Design Influences

**Pokémon** — the organic feel of exploration, where the world opens gradually and every route has a reason. Semi-open world structure with broader scope and modernized design language.

**Elden Ring / Dark Souls** — progression built around deliberate checkpoints. A bonfire-style rest and fast travel system that makes the world feel connected, not segmented.

**Divinity: Original Sin 2** — the AP economy. Every action costs something. Offense and defense share the same pool, which means every choice has a tradeoff.

**Clair Obscur: Expedition 33** — parry in a turn-based context. Defense isn't passive waiting, it's an active read on the enemy's intent with a real-time execution window.

All of it filtered through the creator's own taste. The goal isn't to recreate any of these — it's to take what makes each of them work and build something original.

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
- **Dependencies**: None

---

## Core Features

### Exploration

- Real-time top-down free movement
- Tile-based world rendering with culling optimization
- Semi-open world structure — zones connect naturally, progression gates are spatial not arbitrary
- Bonfire-style checkpoint and fast travel system
- NPC interaction and contextual dialogue
- Encounter trigger system

### Combat — AP Economy + Real-Time Parry

Eternafall's combat is turn-based, but it doesn't play like one.

Each round opens with an AP pool shared between offense and defense. What you spend attacking is what you don't have left to absorb. The player always moves first — but that's a pressure, not a gift. Commit too hard and the enemy's response will cost you.

When an enemy attacks, a real-time timing window opens. Read the intent correctly and parry within the window — the hit is voided, the enemy takes counter damage, loses AP. Misread it, miss the timing, or attempt a parry on an unparryable strike — there are consequences.

The result is combat that thinks like chess and reacts like a fighting game. Every round is a resource problem and a reflex test at the same time.

Build variables — armor, armament, talisman, and stance — all interact with this loop. A stance might disable parry entirely in exchange for burst potential. A talisman might trade AP for HP. Every build choice reshapes how the economy plays out.

### Architecture

- Custom fixed timestep game loop
- Explicit state separation — `ExplorationState`, `CombatState`, `MenuState`
- Layered rendering pipeline
- Modular OOP hierarchy

---

## Status

Active development. Core systems in implementation. Combat logic in design finalization.
