# Contributing

## Getting started

Что сделать чтобы приступить к разработке?

`docker-compose up -d`

## Documenting

Какие есть не автоматизированные соглашения в написании документации?

### Specification

- При изменении api или порождаемых событий обязательно обновить спеку

### PhpDoc

- Если метод возвращает array, то в phpdoc следует описать форму этого массива

## Code

Какие есть не автоматизированные соглашения в написании кода?

### Backend

- CodeStyle контролируется PHP-CS-Fixer. Для запуска удобно использовать docker
`docker run --rm  -v ${PWD}:/data cytopia/php-cs-fixer:2 fix -vvv .`
- Мы используем магические цифры только в шаблонах. Контролируем при помощи phpmnd.
Подробнее в `Taskfile.yml`
- Используем PHPStan

### Frontend

### Pull request

Как интегрируем код?

- После оформления PullRequest, аппруверы автоматически получают уведомление
о необходимости ревью
- После получения аппрува автор PullRequest сливает его и выкатывает версию
сам или привлекая компетентных лиц

## Tools

Какие инструменты мы используем и для чего?

- Composer scripts to build application code. composer may require 4G
and docker 5+ G of RAM
- docker-compose to deploy while dev
- Полезные команды:
  - `docker-compose exec php bash`
  - `task` [https://taskfile.dev](https://taskfile.dev)

## Tests

Как проверить приложение на работоспособность?

## Releasing

Как выпустить релиз?

- Как добавить переменные в пайплайн

## Deploy

Где находятся наши серверы и как доставить приложение на них?

We use GCP (Google cloud platform) and continuous deployment from master branch.

To use first [install `gcloud` cli tool](https://cloud.google.com/sdk/install).

## Runtime

Если у нас не golang то существует среда выполнения, как узнать что в ней есть?

Visit `Dockerfile`s for info about running apps

## Monitoring

Куда смотреть что все хорошо во время деплоя своих изменений?
