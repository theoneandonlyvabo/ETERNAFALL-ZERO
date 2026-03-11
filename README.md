<div align="center">

# ETERNAFALL

**Game Development Progress**

![Progress](https://progress-bar.xyz/32/?width=750&height=28)

**Version 0.2.4**

*A top-down RPG where turn-based combat has a real-time parry, exploration never locks you out, and the world's lore earns its weight.*

[![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![AWT + Swing](https://img.shields.io/badge/Graphics-AWT%20%2B%20Swing-4A90D9?logo=java)](https://docs.oracle.com/javase/8/docs/api/java/awt/package-summary.html)
[![No Dependencies](https://img.shields.io/badge/Dependencies-None-brightgreen)]()
[![Status](https://img.shields.io/badge/Status-Active%20Development-yellow)]()

</div>

---

## 🌑 About

**Eternafall** is a 2D RPG built entirely from scratch in pure Java — no engines, no frameworks, no dependencies.

The world is a political-mythological dark fantasy. Divine order has collapsed. Factions war over what godhood left behind. You move through a world that doesn't pause for you, fight in systems that punish passivity, and carry a burden the world hasn't named yet.

Every design decision in Eternafall comes from a genuine relationship with games — what works, what doesn't, and what hasn't been tried yet. The result is a system that borrows from the best and belongs to none of them.

---

## 🎮 Design Influences

| | Game | What It Contributes |
|---|---|---|
| 🌿 | **Pokémon** | Organic exploration. Semi-open world that opens gradually — every route has a reason. Modernized in scope and design language. |
| 🔥 | **Dark Souls** | Bonfire-style checkpoint and fast travel system. Progression feels earned, and the world feels connected. |
| ⚔️ | **Divinity: Original Sin 2** | The AP economy. Every action costs something shared between offense and defense. |
| 🎯 | **Clair Obscur: Expedition 33** | Real-time parry inside a turn-based loop. Defense is an active read, not passive waiting. |

> All of it filtered through the creator's own taste. The goal isn't to recreate any of these — it's to take what makes each of them work and build something original.

---

## 🛠 Tech Stack

```
Language     →  Java (pure standard library)
Graphics     →  Java AWT + Swing (Java 2D)
Architecture →  Object-Oriented Programming
Dependencies →  None
```

---

## ⚔️ Combat — AP Economy + Real-Time Parry

> Turn-based that thinks like chess, reacts like a fighting game.

Eternafall's combat is turn-based, but it doesn't play like one.

Each round opens with an **AP pool shared between offense and defense**. What you spend attacking is what you don't have left to absorb. The player always moves first — but that's a pressure, not a gift. Commit too hard and the enemy's response will cost you.

When an enemy attacks, a **real-time timing window opens**. Read the intent correctly, parry within the window — the hit is voided, the enemy takes counter damage, loses AP. Misread it, miss the timing, or attempt a parry on an unparryable strike — there are consequences.

Build variables interact with this loop:

- 🛡️ **Armor** — 3 slots with set bonuses
- ⚔️ **Armament** — shapes your offensive options
- 💎 **Talisman** — passive tradeoffs, AP for HP and beyond
- 🔄 **Stance** — locked mid-battle; might disable parry entirely for burst potential

---

## 🌍 Exploration

- Real-time top-down free movement
- Tile-based world rendering with culling optimization
- Semi-open world — zones connect naturally, progression gates are spatial not arbitrary
- Bonfire-style checkpoint and fast travel system
- NPC interaction with contextual dialogue
- Encounter trigger system

---

## 🧠 Architecture

- Custom fixed timestep game loop
- Explicit state separation — `ExplorationState` · `CombatState` · `MenuState`
- Layered rendering pipeline
- Modular OOP hierarchy

---

<div align="center">

**Active development** · Core systems in implementation · Combat logic in design finalization

</div>
