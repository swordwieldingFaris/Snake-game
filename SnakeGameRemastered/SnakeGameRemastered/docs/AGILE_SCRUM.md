# Snake Game: Classic Remastered - Agile-Scrum Documentation

## Project Overview

- **Project Name**: Snake Game: Classic Remastered
- **Product Owner**: Game Development Team
- **Scrum Master**: Development Lead
- **Team Size**: 3-5 developers

## Sprint Schedule

- **Sprint Duration**: 2 weeks
- **Sprint Planning**: Monday of Sprint 1
- **Daily Stand-ups**: 10 minutes, every day
- **Sprint Review**: Last day of sprint
- **Retrospective**: After sprint review

## Product Backlog

### Sprint 1 (Current)
- [x] Core game mechanics (snake movement, food, collision)
- [x] Basic UI with game panel
- [x] Main game loop with 60 FPS target
- [ ] Input handling (arrow keys/WASD)
- [ ] Collision detection (walls, self)
- [ ] Basic scoring system

### Sprint 2
- [ ] Game modes (Classic, Time Attack, Survival, Puzzle)
- [ ] Power-up system
- [ ] Obstacles (static, moving, destructible)
- [ ] Pause/resume functionality
- [ ] Save/load system

### Sprint 3
- [ ] Enhanced UI with animations
- [ ] User profiles
- [ ] Customizable snake appearance
- [ ] Leaderboard system
- [ ] Achievements

### Sprint 4
- [ ] Testing and bug fixes
- [ ] Performance optimization
- [ ] Colorblind mode
- [ ] Sound effects
- [ ] Documentation

## Definition of Done

Each user story is complete when:
- Code implemented following coding standards
- Unit tests written (>80% coverage)
- Code reviewed by at least one team member
- Integration tests pass
- Documentation updated
- Build succeeds without warnings

## Daily Stand-up Format

1. What did I do yesterday?
2. What will I do today?
3. Are there any blockers?

## Sprint Planning Template

### User Story Template
```
As a [player], I want [feature] so that [benefit]
```

### Acceptance Criteria
- [ ] Criterion 1
- [ ] Criterion 2
- [ ] ...

### Tasks
- [ ] Task 1 (estimated hours)
- [ ] Task 2 (estimated hours)

## Sprint Review Template

### Demo
- Feature 1 - demonstrated
- Feature 2 - demonstrated
- ...

### Metrics
- Story points completed: X / Y
- Tests passing: X%
- Code coverage: X%

### Feedback
- What worked well?
- What didn't work well?
- Action items for next sprint

## Retrospective Template

### What went well?
- 

### What could improve?
- 

### Action items
- [ ] Action 1 - Owner
- [ ] Action 2 - Owner

## Git Branching Strategy

- `main` - Production-ready code
- `develop` - Integration branch
- `feature/feature-name` - New features
- `bugfix/bug-description` - Bug fixes
- `hotfix/issue-description` - Urgent fixes

### Commit Message Format
```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

Types: feat, fix, docs, style, refactor, test, chore

## CI/CD Pipeline

1. Push to develop branch
2. GitHub Actions runs:
   - Maven build
   - Unit tests
   - JaCoCo coverage check
3. Code review required
4. Merge to main after approval

## Contact

- Email: team@snakegame.com
- Repository: https://github.com/snakegame/snake-game-remastered