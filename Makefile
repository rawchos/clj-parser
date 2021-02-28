# These will be used later if I decide to add a Dockerfile
VERSION = 0.0.1
PROJECT = clj-parser

SHELL = /bin/bash

default: run

help: # Display help
	@awk -F ':|##' \
		'/^[^\t].+?:.*?##/ {\
			printf "\033[36m%-30s\033[0m %s\n", $$1, $$NF \
		}' $(MAKEFILE_LIST) | sort

repl: ## Run a repl locally
	clj -M:nrepl

run: ## Run the application locally
	clj -M:run

.PHONY: help repl run