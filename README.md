```
napthe-root/
├── napthe-common/          # Shared core logic
│   ├── src/main/java/com/napthe/
│   │   ├── common/         # Core interfaces and models
│   │   ├── provider/       # Payment provider interfaces
│   │   └── strategy/       # Base strategy definitions
│   └── build.gradle
│
├── napthe-paper/           # Minecraft Paper implementation
│   ├── src/main/java/com/napthe/paper/
│   │   ├── polling/        # Polling implementation
│   │   ├── command/        # In-game commands
│   │   └── listener/       # Bukkit/Paper listeners
│   └── build.gradle
│
├── napthe-web/             # Web service implementation
│   ├── src/main/java/com/napthe/web/
│   │   ├── webhook/        # Webhook implementation
│   │   ├── controller/     # REST endpoints
│   │   └── security/       # Webhook validation
│   └── build.gradle
│
├── settings.gradle
└── build.gradle
```

Note:

- [ ] implementation database to save payment history
- [ ] find a way to generate unique order id for payos
- [ ] get order id cross server, use redis caching for cross server, on one server, use atomiclong
-
- [x] implement payos
- [x] test thesieutoc
- [x] idea: always have a timer task that check on the queue and process the payment