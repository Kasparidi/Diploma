# Дипломный проект
[План автоматизации тестирования](https://github.com/Kasparidi/Diploma/blob/master/documentation/Plan.md)

Запуск с MySQL:
1. Открыть Intellij IDEA
1. Склонировать репозиторий: ``git clone https://github.com/Kasparidi/Diploma``
1. Запустить контейнеры: ``docker compose up``
1. Запустить SUT: ``java -jar ./artifacts/aqa-shop.jar``
1. Запустить тесты: (2 * Ctrl) ``gradlew test``