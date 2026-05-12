# VotingSystem

Production-ready Minecraft voting plugin for Purpur/Paper 1.21.x (Java 21).

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

