# Название

## Краткое описание

Описание бизнес-процесса за который отвечает сервис. 

Production URL: https://domain.com

Business documentation: https://confluence.domain.com

Specification: https://swagger.domain.com/serviceA.yml, https://confluence.domain.com/EventBus

> Например: оформление заказа; сбор заказа; регистрация пользователя.

## Стратегия развития проекта

Коротко: mvp; stable;

### Риски

- Плохо знаем бизнес-модель
- Жесткий дэдлайн при фиксированном скоупе - Новый год

### Подходы

Примеры:
- Толстый контроллер + smoke test
- TDD + nodesign
- hexagon + split unit,integration tests
- acceptance tests at https://github.com/...

## Ответственные

- Тех: Сергей Сергеевич @serg
- Продукт: Иван Иванович @ivan

## Каналы коммуникации

- #tech. Тех ревью. Помощь в проектировании.
- jira.domain.com/BUGS. Несовершенства системы.
- status.domain.com. Работоспособность узлов

## Стейджинг

Как пользоваться тестовыми серверами

## Troubleshooting

В случае появления ошибки Symfony Exception "Type error: " на страницах *** , необходимо выкатить свежий дамп .

При ошибке TransportException иногда помогает перевыкат https://jenkins.domain.com/job/