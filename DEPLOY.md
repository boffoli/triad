# Deploy

This project ships as a Docker image. End users do not need the source code.

## Requirements
- Docker Desktop or Docker Engine installed and running.

## Run with Docker Compose (recommended)
```bash
docker compose -f docker-compose.prod.yml up -d
```

Check logs:
```bash
docker logs -f triad
```

Stop:
```bash
docker compose -f docker-compose.prod.yml down
```

Update to latest image:
```bash
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d
```

## Run with Docker (single command)
```bash
docker run -d --name triad -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 ghcr.io/boffoli/triad:latest
```

## Notes
- The container listens on port 8080.
- Logs are available with `docker logs -f triad`.
