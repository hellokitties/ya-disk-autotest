Проект автотестов для сервиса [Яндекс.Диска](https://yandex.ru/dev/disk/rest/). Реализованы проверки методов POST, PUT, DELETE.

**Используемые технологии:** Java, Junit5, RestAssured, Awaitility

**Локальный запуск:**
1. Установить токен для авторизации в переменную среды OAUTH_TOKEN. Как токен описано [в инструкции](https://yandex.ru/dev/disk-api/doc/ru/concepts/quickstart)
2. Выполнить команду *mvn test*
