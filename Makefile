.DEFAULT_GOAL=help
PHP_VERSION=7.1
DEV_IMAGE_NAME="antonmarin/php:$(PHP_VERSION)-alpine-cli"
CMD_DOCKER_RUN=docker run -itv $(PWD):/app -w /app $(DEV_IMAGE_NAME)

exec:
	$(CMD_DOCKER_RUN) sh

lint: lint-cs lint-composer lint-mnd
lint-cloud-init:
	docker run --rm -v $(PWD):/app -w /app nonstatic/cloud-init:v1 cloud-init devel schema --config-file /app/cloud-init.cfg
lint-composer:
	docker run --rm -iv $(PWD):/app/ composer:1.9 validate
	docker run --rm -v $(PWD):/app phpqa/security-checker security:check /app/composer.lock
lint-cpd:
	docker run --rm -v $(PWD):/app phpqa/phpcpd /app
lint-cs:
	docker run --rm -iv $(PWD):/data/ cytopia/php-cs-fixer fix --dry-run --diff --allow-risky=yes
lint-checkstyle:
	docker run --rm -v $(PWD):/app -v $HOME/checkstyle.xml:/config/checkstyle.xml solucionesgbh/checkstyle checkstyle /config/checkstyle.xml /app
lint-dockerfile:
	docker run --rm -tv ${PWD}:/app hadolint/hadolint:v1.8.0 \
		hadolint \
		/app/alpine/Dockerfile \
		/app/debian/Dockerfile
	docker run --rm --net host --pid host --userns host --cap-add audit_control \
        -e DOCKER_CONTENT_TRUST=$DOCKER_CONTENT_TRUST \
        -v /var/lib:/var/lib \
        -v /var/run/docker.sock:/var/run/docker.sock \
        -v /usr/lib/systemd:/usr/lib/systemd \
        -v /etc:/etc --label docker_bench_security \
        docker/docker-bench-security
lint-doctrine:
	bin/doctrine orm:validate-schema
	bin/console doctrine:schema:validate
lint-dotenv: #? dotenv-linter.github.io'
	docker run --rm -v $(PWD):/app dotenvlinter/dotenv-linter -r /app/
lint-git:
	docker run --rm --ulimit nofile=1024 -v "$(PWD):/repo" -w "/repo" jorisroovers/gitlint --ignore=body-is-missing -c title-match-regex.regex="^[\w\s]*" --commits "origin/master..HEAD"
	echo "$(shell git rev-parse --abbrev-ref HEAD)" | grep -Eq '\w+' || echo "\033[31mError: git branch pattern failed\033[0m"
	docker run --rm -v $(PWD):/app willhallonline/git-sizer:alpine
lint-helm:
	docker run --rm -v $(PWD):/app dtzar/helm-kubectl helm lint \
	  -f "/app/helm/values.yaml" \
	  -f "/app/helm/values-dev.yaml" \
	  "/app/helm"
lint-json:
	docker run --rm -v "$(PWD):/app" cytopia/jsonlint -t '    ' /app/composer.json
lint-markdown:
	docker run --rm -v $(PWD):/app markdownlint/markdownlint -i /app
lint-mnd:
	docker run --rm -v $(PWD):/app dockerizedphp/phpmnd /app \
        --exclude=var --exclude=vendor --exclude=src/Pcs/Resources/blockslib/blocks \
        --non-zero-exit-on-violation
lint-shell:
	docker run --rm -tv "$PWD:/mnt" koalaman/shellcheck:v0.5.0 \
		--color=always --shell=sh --exclude=SC2181 \
		build/docker/*/*.sh *.sh
lint-swagger:
	docker run --rm -v $(PWD):/app stoplight/spectral lint -F warn /app/swagger.yml
lint-yaml:
	docker run --rm -v $(PWD):/app -w /app sdesbure/yamllint sh -c "yamllint /app/*.yml"

test: stan codeception
rebuild:
	docker build --build-arg PHP_VERSION=$(PHP_VERSION) -t $(DEV_IMAGE_NAME) -f docker/Dockerfile .
	$(CMD_DOCKER_RUN) rm -f composer.lock
	$(CMD_DOCKER_RUN) composer install
stan:
	$(CMD_DOCKER_RUN) vendor/bin/phpstan analyse . -vvv
codeception:
	$(CMD_DOCKER_RUN) vendor/bin/codecept run
codeclimate:
	docker run --rm -t \
      --env CODECLIMATE_CODE=$(PWD) \
      --volume $(PWD):/code \
      --volume /var/run/docker.sock:/var/run/docker.sock \
      --volume /tmp/cc:/tmp/cc \
      codeclimate/codeclimate analyze

phpmetrics:
	docker run --rm -v $(PWD):/app --user $(id -u):$(id -g) herloct/phpmetrics /app

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
