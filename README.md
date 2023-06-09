# Java Chat Application

# Содержание
1. [Описание проекта](#описание-проекта) 
2. [Демонстрация работы чата](#демонстрация-работы-чата)
3. [Сборка](#сборка) \
    3.1. [Сборка сервера](#сборка-сервера) \
    3.2. [Сборка клиента](#сборка-клиента) 
4. [Запуск](#запуск) \
    4.1. [Запуск сервера](#запуск-сервера) \
    4.2. [Запуск клиента](#запуск-клиента) 
5. [Структура проекта](#структура-проекта) \
    5.1. [Структура сервера](#структура-сервера) \
    5.2. [Структура клиента](#структура-клиента) 
6. [Подключение базы данных](#подключение-базы-данных)  


# Описание проекта
Этот репозиторий содержит исходный код многопользовательского чата на Java. Приложение реализовано с использованием API сокетов Java и включает клиентскую и серверную части. Оно позволяет пользователям регистрироваться, входить в систему, обмениваться сообщениями и взаимодействовать в чат-комнатах. Для разработки данного проекта были использованы следующие технологии: `Java Core`, `Spring DI`, `Spring Data JDBC`, `Spring Security`, `HikariCP` и `Maven`. В качестве базы данных выбрана `PostgreSQL`. Для удобного администрирования базы данных использовался `pgAdmin`. Для удобной и повторяемой развертки базы и административного интерфейса использован `Docker Compose`.

# Демонстрация работы чата
https://github.com/ara-bakiev997/Chat/assets/106027588/39e160f9-06b0-4a8b-8acd-6953eceaa971

# Сборка
Чтобы собрать и использовать клиентскую и серверную части приложения, Вам понадобятся следующие инструменты:

- JDK 1.8 или выше.
- Сборщик Maven.
- Docker

### Сборка сервера
- Перейдите в папку `SocketServer`.
- Выполните команду `mvn package` для сборки проекта.
- После успешной сборки, в папке target появится файл `socket-server-jar-with-dependencies.jar`.

### Сборка клиента
- Перейдите в папку `SocketClient`.
- Выполните команду `mvn package` для сборки проекта.
- После успешной сборки, в папке target появится файл `socket-client-jar-with-dependencies.jar`.

# Запуск
### Запуск сервера
- Выполните команду `docker-compose up -d` для запуска базы данных и админки.
- Выполните команду `java -jar target/socket-server-jar-with-dependencies.jar --port=8080` для запуска сервера.

### Запуск клиента
- Выполните команду `java -jar target/socket-client-jar-with-dependencies.jar --server-port=8080` для запуска клиента.

# Структура проекта
### Структура сервера

##### Для реализации многопользовательского приложения все соединения должны обрабатываться в отдельных потоках. 
- Класс `Server` отвечает за обработку каждого подключения клиента в отдельном потоке. Когда клиент подключается, сервер принимает соединение и создает экземпляр класса `ClientConnection` для обработки данного подключения.
- Класс `ClientConnection` реализовывает логику обработки команд от клиента. Он получает сообщения от клиента через соединение, а затем выполняет соответствующие действия в зависимости от полученной команды.

##### Для удобной обработки команд клиента реализован паттерн `command` который состоит из следующих блоков.
- Команды (commands): В этом блоке находятся интерфейсы, представляющие отдельные команды, которые клиент может отправить на сервер. В нашем случае, это `LoginCommand` и `ManipulateRoomsCommand`.

- Исполнители команд (invokers): В этом блоке находятся классы которые cодержат логику выбора и вызова соответствующей команды.

- Получатели команд (receivers): В этом блоке находятся классы, которые выполняют непосредственную обработку команды. Они содержат бизнес-логику, связанную с выполнением действий, указанных в команде.

##### Все данные должны быть записаны в базу данных.
- Компоненты `services` предназначены для облегчения взаимодействия с базами данных и предоставления удобных методов для работы с соответствующими сущностями (сообщениями, комнатами, пользователями).

- Компоненты `repositories` представляют собой реализацию паттерна репозитория и предоставляют методы для взаимодействия с базой данных. Они обеспечивают абстракцию над операциями чтения, записи, обновления и удаления данных, скрывая детали взаимодействия с базой данных и предоставляя удобный интерфейс для работы с данными. 

- Компоненты `config` и `resources` предназначены для конфигурации приложения и управление ресурсами. Включает классы, такие как SocketsApplicationConfig, которые содержат настройки и зависимости приложения, а также файлы ресурсов, такие как db.properties, содержащие настройки подключения к базе данных.

Каждый блок выполняет свою роль в общей структуре сервера и отвечает за определенные аспекты функционирования приложения. Это позволяет разделить ответственности и облегчает сопровождение и расширение кодовой базы сервера.

<details>
<summary>Структура сервера</summary>

```yaml
├── README.txt
├── docker-compose.yml
├── pom.xml
└── src
    └── main
        ├── java
        │   └── edu
        │       └── school21
        │           └── sockets
        │               ├── app
        │               │   └── Main.java
        │               ├── config
        │               │   └── SocketsApplicationConfig.java
        │               ├── exceptions
        │               │   ├── CommandNotFoundExceptions.java
        │               │   └── RoomsNotFoundExceptions.java
        │               ├── models
        │               │   ├── ChatRoom.java
        │               │   ├── JsonObject.java
        │               │   ├── Message.java
        │               │   └── User.java
        │               ├── repositories
        │               │   ├── CrudRepository.java
        │               │   ├── messagesrepository
        │               │   │   ├── MessagesRepository.java
        │               │   │   └── MessagesRepositoryJdbcTemplateImpl.java
        │               │   ├── roomsrepository
        │               │   │   ├── RoomsRepository.java
        │               │   │   └── RoomsRepositoryJdbcTemplateImpl.java
        │               │   ├── usersrepository
        │               │   │   ├── UsersRepository.java
        │               │   │   └── UsersRepositoryJdbcTemplateImpl.java
        │               │   └── utils
        │               │       └── TableInitializer.java
        │               ├── server
        │               │   ├── ClientConnection.java
        │               │   ├── Server.java
        │               │   ├── commands
        │               │   │   ├── LoginСommand.java
        │               │   │   └── ManipulateRoomsCommand.java
        │               │   ├── invokers
        │               │   │   ├── LoginCommandSwitch.java
        │               │   │   └── ManipulateRoomsCommandSwitch.java
        │               │   └── receivers
        │               │       ├── LoginReceiver.java
        │               │       └── ManipulateRoomsReceiver.java
        │               └── services
        │                   ├── jsonservice
        │                   │   ├── JsonService.java
        │                   │   └── JsonServiceImpl.java
        │                   ├── messageservice
        │                   │   ├── MessagesService.java
        │                   │   └── MessagesServiceImpl.java
        │                   ├── roomservice
        │                   │   ├── RoomsService.java
        │                   │   └── RoomsServiceImpl.java
        │                   └── userservice
        │                       ├── UsersService.java
        │                       └── UsersServiceImpl.java
        └── resources
            ├── data.sql
            ├── db.properties
            └── schema.sql

```
</details>

### Структура клиента

##### Компоненты клиента отвечают за взаимодействие с сервером и выполнение операций.
- `Client` отвечает за установку соединения с сервером и запуск процессов чтения и записи данных. Класс создает сокет для подключения к серверу, инициализирует объекты `Reader` и `Writer` для обработки входящих и исходящих данных соответственно
- `Reader` отвечает за чтение данных из сокета. Он запускается в отдельном потоке и непрерывно прослушивает входящий поток данных от сервера. `Reader` получает ответы от сервера и передает их в соответствующие компоненты для обработки.

- `Writer` отвечает за запись данных в сокет. Он также работает в отдельном потоке и обрабатывает очередь исходящих сообщений. `Writer` получает команды от клиента и отправляет их на сервер для выполнения.

<details>
<summary>Структура клиента</summary>

```yaml
├── pom.xml
└── src
    └── main
        └── java
            └── edu
                └── school21
                    └── sockets
                        ├── app
                        │   └── Main.java
                        ├── client
                        │   ├── Client.java
                        │   ├── Reader.java
                        │   └── Writer.java
                        ├── models
                        │   └── JsonObject.java
                        └── services
                            └── jsonservice
                                ├── JsonService.java
                                └── JsonServiceImpl.java
```
</details>

# Подключение базы данных

В данном проекте используется PostgreSQL в связке с pgAdmin для администрирования базы данных. Для удобства развертывания и управления, вся инфраструктура базы данных запускается с помощью Docker Compose.

Docker Compose позволяет определить и описать контейнеры, их конфигурацию и зависимости в файле docker-compose.yml. В данном файле определены контейнеры для PostgreSQL и pgAdmin, а также настройки для каждого контейнера, включая порты, переменные окружения и другие параметры.

<details>
<summary>Файл docker-compose.yml</summary>

```yaml
version: '3.1'

services:

   db:
      image: postgres
      environment:
         POSTGRES_PASSWORD: admin
         POSTGRES_DB: database
      ports:
      - 5432:5432

   adminer:
      image: dpage/pgadmin4
      environment:
         PGADMIN_DEFAULT_EMAIL: user@domain.com
         PGADMIN_DEFAULT_PASSWORD: admin
      ports:
      - 80:80
```

</details>
