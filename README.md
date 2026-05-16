<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:1a1a2e,50:16213e,100:0f3460&height=160&section=header&text=VotingSystem&fontSize=42&fontColor=e94560&animation=fadeIn&fontAlignY=55" />

</div>

<div align="center">

### Production-ready voting plugin for Minecraft servers

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-62B47A?style=for-the-badge&logo=minecraft&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Paper](https://img.shields.io/badge/Paper%20%2F%20Purpur-supported-blue?style=for-the-badge)
![Version](https://img.shields.io/badge/version-1.0.0-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-yellow?style=for-the-badge)

**Create multiple votings • Beautiful GUI • One vote per player • HEX colors**

</div>

---

## Features

- Multiple voting definitions in `config.yml`
- Only one active voting at a time
- Player voting GUI via `/vote`
- Dynamic `{votes}` placeholder in lore
- One vote per player (no revote)
- Tie handling with random winner among leaders
- No-votes modes: `NOTHING` and `RANDOM`
- Extendable action system (`[console]`, `[message]`)
- Separate `messages.yml` and `sounds.yml`
- Adventure API messages with HEX color support

## Commands

- `/vote` - open active voting GUI
- `/vote forcestart <id>` - force start voting
- `/vote forceend` - force end active voting
- `/vote reload` - reload configs

## Permissions

- `votingsystem.use`
- `votingsystem.admin.forcestart`
- `votingsystem.admin.forceend`
- `votingsystem.admin.reload`

## Build

```bash
gradle build
```

Result jar:

`build/libs/VotingSystem-1.0.0.jar`

<div align="center">

[![](https://img.shields.io/badge/made%20by-stompingcrit-e94560?style=flat-square&logo=github)](https://github.com/stompingcrit)

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:1a1a2e,50:16213e,100:0f3460&height=80&section=footer" />

