<div align="center">

# ETERNAFALL

**Game Development Progress**

![Progress](https://progress-bar.xyz/47/?width=750&height=28)

**Version 0.3.6 - Inventory System**

*A top-down RPG where turn-based combat has a real-time parry, exploration never locks you out, and the world's lore earns its weight.*

[![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![AWT + Swing](https://img.shields.io/badge/Graphics-AWT%20%2B%20Swing-4A90D9?logo=java)](https://docs.oracle.com/javase/8/docs/api/java/awt/package-summary.html)
[![No Dependencies](https://img.shields.io/badge/Dependencies-None-brightgreen)]()
[![Status](https://img.shields.io/badge/Status-Active%20Development-yellow)]()

</div>

---

## 🌑 About

Games have been the constant. Long before the code, before the design theory, before knowing what a game loop was — the love was already there. It took a while to get here. Information systems, OOP, graphic design, years of building the actual skillset. The first real attempt went sideways — a 3D soulslike that spent more time in asset pipelines than in logic. Wrong direction. Came back to what actually mattered: the systems, the design, the craft of making something that plays. Pure Java. No engine. Every system written from scratch. Not a limitation — a choice. If it ships, every line of it was earned.

This repo is the full development record — custom game loop, tile-based renderer, collision, interaction, inventory, and combat. Architecture decisions, system design, and every iteration documented as it happens.

---

## 🌍 What Remains of Witherfeld

> Witherfeld is what remains after godhood stopped being rhetorical. The Crater didn't end anything — it redistributed. Power scattered into shards, and everything that breathes has been reaching for them ever since. Three deities still stand. One built an order on light that was never honest. One learned to speak in dragon blood and called it progress. One watched everything burn and decided that was pragmatism. They are not villains. They are conclusions.
>
> Hearthshore still smells like a beginning. Greymark is a wound three armies argue over. Solvarr Citadel holds its golden silence. Dreadcoast builds and pretends it isn't afraid. The one who walks through all of it starts small — the world won't notice for a while. Some things are already known about where this ends. Where it always ends. *Humans, will be* — for as long as anything exists to witness it. That much, at least, hasn't changed.

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
- 💎 **Accessory** — passive tradeoffs, AP for HP and beyond
- 🔄 **Path** — locked mid-battle; might disable parry entirely for burst potential

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
- Explicit state separation — `ExplorationState` · `CombatState` · `pausedState`
- Layered rendering pipeline
- Modular OOP hierarchy

---

## ☕ Support

This project is built solo, from scratch, in pure Java. No engine, no shortcuts.

If you want to support the development, any contribution helps keep it going.

<div align="center">

![Saweria](https://saweria.co/widgets/qr?streamKey=aac4bbd7609515e54b33a8977fd19bea)

**[saweria.co/support](https://saweria.co)**

</div>

---

<div align="center">

**Active development** · Core systems in implementation · Combat logic in design finalization

</div>
