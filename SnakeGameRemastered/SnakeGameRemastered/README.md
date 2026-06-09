# SNAKE : CLASSIC REMASTERED

Retro Snake gameplay with modern polish, built in Java Swing using MVC architecture.

## Highlights

- **Modern menu flow** with dedicated screens for main menu, game, high scores, statistics, and achievements.
- **Polished controls/UI** with rounded gradient buttons, glow hover effects, and consistent spacing.
- **Pause overlay** (press `P` or menu Pause) with `Resume`, `Restart`, and `Quit`.
- **Save/Load support** from the `Game` menu (`savegame.dat` serialization).
- **Theme + accessibility toggles** for Dark/Light mode and Color-Blind mode.
- **Persistent player feedback** through lifetime statistics, achievements, and unlock toasts.
- **SQLite high scores** persisted in `snake_game.db`.

## Game Modes

- **Classic**: Standard progression gameplay.
- **Survival**: Dynamic challenge with periodic obstacle growth and increasing speed.
- **Time Attack**: Score as much as possible before the 60-second timer ends.

## Features

- **Core gameplay**: Snake movement, growth, collisions, food logic, power-ups, levels.
- **Mode-aware behavior**: Distinct runtime rules for Survival and Time Attack.
- **Score handling**: Multiplier-aware scoring and score persistence at game end.
- **Menus**:
  - `Game`: New Game, Pause, Save Game, Load Game, Exit
  - `View`: High Scores, Statistics, Achievements
  - `Settings`: Sound, Dark/Light, Color-Blind, Difficulty
- **Persistent preferences**: Theme, sound, color-blind mode, and difficulty are saved between sessions.

## Requirements

- **Java** 21+
- **Maven** 3.8+
- **OS** Windows/Linux/macOS (Swing desktop app)

## Run

From the project root:

```bash
mvn -DskipTests clean package
java -jar target/snake-game-remastered.jar
```

## Controls

- **Arrow Keys / WASD**: Move snake
- **P**: Pause/Resume overlay
- **Menu > Game > Pause**: Open pause overlay
- **Menu > Game > Save Game / Load Game**: Persist and restore state

## Configuration and Persistence

- **App config**: `game-config.properties` (optional defaults; auto-fallback if absent)
- **User preferences**: `user-preferences.properties`
- **Save file**: `savegame.dat`
- **Statistics file**: `player-stats.dat`
- **Scores database**: `snake_game.db`

## Project Structure

```text
SnakeGameRemastered/
├── pom.xml
├── src/main/java/com/snakegame/
│   ├── controller/        # Game flow and input orchestration
│   ├── model/             # Domain models and game state
│   ├── service/           # Save/load and score services
│   ├── util/              # DB manager, config, sound, stats manager
│   ├── view/              # Swing screens/components/overlays
│   └── SnakeGameApplication.java
└── src/test/java/com/snakegame/
    ├── model/
    └── integration/
```

## Notes

- The project follows MVC and keeps view logic in Swing components, model logic in `model`, and orchestration in controllers/services.
- If this is your first run, preference/config/save/stat files are created automatically when needed.

## License

MIT