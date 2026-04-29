.PHONY: help test run curl

help: ## Show available targets
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  %-10s %s\n", $$1, $$2}'

test: ## Run all tests
	./gradlew test

run: ## Run the service on http://localhost:8080
	./gradlew run

curl: ## Send a sample analyse request (requires `make run` in another terminal)
	curl -sS -X POST http://localhost:8080/api/v1/analyse \
	  -H 'Content-Type: application/json' \
	  -d '{"text":"Amount 100 Amount 150 Amount 110 Amount 12 Amount 18 Amount 1000 Amount 200 Amount 2500 Amount 25 Amount 300 Amount 400 Amount 500 Amount 600 Amount 700 Amount 900","significanceLevel":0.05}'
	@echo
