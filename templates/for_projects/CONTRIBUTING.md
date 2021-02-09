# Contributing

## Strategy

Коротко: mvp; stable;

### Risks

- Плохо знаем бизнес-модель
- Жесткий дэдлайн при фиксированном скоупе - Новый год

### Principles

Примеры:

- Толстый контроллер + smoke test
- TDD + nodesign
- hexagon + split unit,integration tests
- acceptance tests at <https://github.com/>...

## Getting started

How to begin contributing?

`docker-compose up -d`

## Documenting

Are there any not automated documenting conventions?

### Specification

- Update specification before editing HTTP or message API

### PhpDoc

- Describe shape of arrays in form of used StaticAnalysisTool

## Code

Are there any not automated code conventions?

### Backend

- CodeStyle контролируется PHP-CS-Fixer. Для запуска удобно использовать
  docker `docker run --rm  -v ${PWD}:/data cytopia/php-cs-fixer:2 fix -vvv .`
- Мы используем магические цифры только в шаблонах. Контролируем при помощи phpmnd.
  Подробнее в `Taskfile.yml`
- Используем PHPStan

### Frontend

### Pull request

How to integrate?

- After creating PullRequest approvers receive notification and review
- After approve successful PR author merge and deploy yourself or
  requesting competent persons

## Tools

What tools and utilities we use and what for?

- [Composer scripts](https://getcomposer.org/doc/articles/scripts.md) to
  build application code. composer may require 4G and docker 5+ G of RAM
- [docker-compose](https://docs.docker.com/compose/) to deploy while dev
- Полезные команды:
  - `docker-compose exec php bash`
  - `task` [https://taskfile.dev](https://taskfile.dev)

## Test

How to verify application is working properly?

- `make test`

## Releasing

What versioning schema used? How to package application with configs?

- We use [CalVer](https://calver.org/) ![CalVer](https://img.shields.io/badge/calver-YYYY.0M.MICRO-22bfda.svg)
- Application package in docker images and distribute through [Docker Hub](https://hub.docker.com/search?type=image)

## Deploy

How to add secrets to pipeline? How to deploy application?

- We use GCP (Google cloud platform) and continuous deployment from master branch.
- To use first [install `gcloud` cli tool](https://cloud.google.com/sdk/install).

## Runtime

What servers run application?

- Visit `Dockerfile`s for info about running apps

or

Код: PHP7.1, Symfony 3.4.18, angularJS 1.3.8
База: PostgreSQL.
Деплой: Jenkins

## Monitoring

Where can you identify application failures? How to add monitoring of state index?

- NewRelic
- Sentry
- ELK/Graylog
- We follow <https://prometheus.io/docs/practices/naming/>
