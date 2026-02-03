APP_NAME := triad
IMAGE := $(APP_NAME):latest

.PHONY: build test run docker docker-run docker-runtime docker-runtime-run compose up down

build:
	./mvnw -q -DskipTests package

test:
	./mvnw -q test

run:
	./mvnw spring-boot:run

docker:
	docker build -t $(IMAGE) .

docker-run:
	docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 $(IMAGE)

docker-runtime: build
	docker build -f Dockerfile.runtime -t $(IMAGE) .

docker-runtime-run:
	docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 $(IMAGE)

compose:
	docker compose up --build

up: compose

down:
	docker compose down
