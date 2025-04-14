# default target (run with `make` or `make dev`)
dev:
	COMPOSE_BAKE=true docker compose -f docker-compose.yml -f docker-compose.dev.yml up --build

# stop and remove containers
down:
	COMPOSE_BAKE=true docker compose -f docker-compose.yml -f docker-compose.dev.yml down

# rebuild containers without cache
rebuild:
	COMPOSE_BAKE=true docker compose -f docker-compose.yml -f docker-compose.dev.yml build --no-cache

# view logs
logs:
	COMPOSE_BAKE=true docker compose -f docker-compose.yml -f docker-compose.dev.yml logs -f

# clean up (containers, networks, volumes)
clean: down
	COMPOSE_BAKE=true docker system prune -f

.PHONY: dev down rebuild logs clean
