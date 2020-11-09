.DEFAULT_GOAL=help
PHP_VERSION=7.1
DEV_IMAGE_NAME="antonmarin/php:$(PHP_VERSION)-alpine-cli"
CMD_DOCKER_RUN=docker run -itv $(PWD):/app -w /app $(DEV_IMAGE_NAME)

exec:
	$(CMD_DOCKER_RUN) sh

lint: lint-cs lint-composer lint-mnd
lint-composer:
	docker run --rm -iv $(PWD):/app/ composer:1.9 validate
lint-cs:
	docker run --rm -iv $(PWD):/data/ cytopia/php-cs-fixer fix --dry-run --diff --allow-risky=yes
lint-mnd:
	docker run --rm -v $(PWD):/app dockerizedphp/phpmnd /app \
        --exclude=var --exclude=vendor --exclude=src/Pcs/Resources/blockslib/blocks \
        --non-zero-exit-on-violation
lint-dockerfile:
	docker run --rm -tv ${PWD}:/app hadolint/hadolint:v1.8.0 \
		hadolint \
		/app/alpine/Dockerfile \
		/app/debian/Dockerfile
lint-shell:
	docker run -tv "$PWD:/mnt" koalaman/shellcheck:v0.5.0 \
		--color=always --shell=sh --exclude=SC2181 \
		build/docker/*/*.sh *.sh
lint-yaml:
	docker run --rm -v "$(PWD):/app" -w /app sdesbure/yamllint sh -c "yamllint **/*.yml"
lint-cloud-init:
	docker run --rm -v "$(PWD):/app" -w /app nonstatic/cloud-init:v1 cloud-init devel schema --config-file /app/cloud-init.cfg
lint-json:
	docker run --rm -v "$(PWD):/app" cytopia/jsonlint -t '    ' /app/composer.json
lint-git:
	docker run --rm --ulimit nofile=1024 -v "$(PWD):/repo" -w "/repo" jorisroovers/gitlint --ignore=body-is-missing -c title-match-regex.regex="^[\w\s]*" --commits "origin/master..HEAD"
	echo "$(shell git rev-parse --abbrev-ref HEAD)" | grep -Eq '\w+' || echo "\033[31mError: git branch pattern failed\033[0m"

test: stan codeception
rebuild:
	docker build --build-arg PHP_VERSION=$(PHP_VERSION) -t $(DEV_IMAGE_NAME) -f docker/Dockerfile .
	$(CMD_DOCKER_RUN) rm -f composer.lock
	$(CMD_DOCKER_RUN) composer install
stan:
	$(CMD_DOCKER_RUN) vendor/bin/phpstan analyse . -vvv
codeception:
	$(CMD_DOCKER_RUN) vendor/bin/codecept run

help: #? help me
	@printf "\e[34;01mAvailable targets\033[0m\n"
	@awk '/^@?[a-zA-Z\-_0-9]+:/ { \
		nb = sub( /^#\? /, "", helpMsg ); \
		if(nb == 0) { \
			helpMsg = $$0; \
			nb = sub( /^[^:]*:.* #\? /, "", helpMsg ); \
		} \
		if (nb) \
			printf "\033[1;31m%-" width "s\033[0m %s\n", $$1, helpMsg; \
	} \
	{ helpMsg = $$0 }' \
	$(MAKEFILE_LIST) | column -ts:

manualhelp:
	@printf "\
		exec\t connect to shell of current dev image \n\
		lint\t prebuild validations \n\
		test\t test library \n\
	"
