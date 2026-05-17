<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:1a1a2e,50:16213e,100:0f3460&height=180&section=header&text=VotingSystem&fontSize=52&fontColor=e94560&animation=fadeIn&fontAlignY=55&desc=Production-ready%20voting%20plugin%20for%20Minecraft%20servers&descAlignY=75&descSize=16&descColor=8b9cbe" />

<br/>

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-62B47A?style=for-the-badge&logo=minecraft&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Paper](https://img.shields.io/badge/Paper%20%2F%20Purpur-supported-5865F2?style=for-the-badge&logo=buffer&logoColor=white)
![Version](https://img.shields.io/badge/version-1.0.0-e94560?style=for-the-badge&logo=semanticrelease&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-f1e05a?style=for-the-badge&logo=opensourceinitiative&logoColor=black)

</div>

---

## ✨ Features

> Everything you need to run fair, beautiful votings on your server — out of the box.

| | Feature |
|---|---|
| 🗳️ | Multiple voting definitions in `config.yml` |
| 🔒 | Only one active voting at a time |
| 🖥️ | Player voting GUI via `/vote` |
| 🔢 | Dynamic `{votes}` placeholder in lore |
| 🚫 | One vote per player — no revoting |
| 🎲 | Tie handling with random winner among leaders |
| ⚙️ | No-votes modes: `NOTHING` and `RANDOM` |
| 🔌 | Extendable action system: `[console]`, `[message]` |
| 🎨 | Adventure API messages with full HEX color support |
| 📁 | Separate `messages.yml` and `sounds.yml` |

---

## 🔄 How it works

```
/vote forcestart <id>
        │
        ▼
  Voting starts (duration from config)
        │
        ▼
  Players open GUI via /vote and click an option
        │
        ├─ one vote per player, no revoting
        │
        ▼
  Timer ends → winner determined
        │
        ├─ tie?      → random winner among leaders
        ├─ no votes? → NOTHING or RANDOM (configurable)
        │
        ▼
  actions_on_win execute → cooldown starts
```

---

## ⚙️ Configuration

<details>
<summary><b>config.yml</b> — global settings, GUI size, voting definitions</summary>
<br>

```yaml
settings:
  voting-duration-seconds: 60        # how long each voting lasts
  delay-between-votings-seconds: 300 # cooldown between votings

gui:
  size: 27 # inventory size — must be a multiple of 9

votings:
  voting1:
    title: "&aVoting title"
    items:
      item1:
        title: "&aOption #1"
        slot: 10                     # GUI slot index (0–26 for size 27)
        material: SHULKER_BOX
        lore:
          - ""
          - "&3Description of this option"
          - ""
          - "&eClick to vote!"
          - ""
          - "&7Votes: {votes}"       # live vote counter
        actions_on_win:
          - "[console] time set day"
          - "[message] &aOption #1 won!"
```

</details>

<details>
<summary><b>Action types</b></summary>
<br>

| Action | Syntax | Description |
|--------|--------|-------------|
| Console command | `[console] <command>` | Runs as server console |
| Server broadcast | `[message] <text>` | Sends message to all online players |

HEX colors are supported everywhere via `&#RRGGBB` format.

</details>

<details>
<summary><b>Example — weather voting (4 options)</b></summary>
<br>

The built-in example lets players vote on time of day and weather:

| Slot | Material | Option | Actions on win |
|------|----------|--------|----------------|
| 10 | `SHULKER_BOX` | ☀️ Day + Clear | `time set day` · `weather clear` |
| 12 | `BEETROOT` | ⛈️ Day + Thunder | `time set day` · `weather thunder` |
| 14 | `DRIED_KELP` | 🌙 Night + Clear | `time set night` · `weather clear` |
| 16 | `SCULK_SENSOR` | 🌩️ Night + Thunder | `time set night` · `weather thunder` |

</details>

---

## 🕹️ Commands

```
/vote                        — open active voting GUI
/vote forcestart <id>        — force start a voting by ID
/vote forceend               — force end the active voting
/vote reload                 — reload all configs
```

---

## 🔑 Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `votingsystem.use` | everyone | Open `/vote` GUI |
| `votingsystem.admin.forcestart` | op | Force start a voting |
| `votingsystem.admin.forceend` | op | Force end active voting |
| `votingsystem.admin.reload` | op | Reload plugin configs |

---

## 🔨 Build

```bash
gradle build
```

Output:

```
build/libs/VotingSystem-1.0.0.jar
```

Drop the jar into your server's `plugins/` folder and restart.

---

<div align="center">

[![](https://img.shields.io/badge/made%20by-stompingcrit-e94560?style=for-the-badge&logo=github&logoColor=white)](https://github.com/stompingcrit)

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:1a1a2e,50:16213e,100:0f3460&height=100&section=footer" />

</div>
