# Java Chat Application

# Содержание
1. [Описание проекта](#описание-проекта) \
2. [Структура проекта](#структура-проекта) \
    2.1. [Структура сервера](#структура-сервера) \
    2.2. [Структура клиента](#структура-клиента) \
3. [Подключение базы данных](#chapter-iv) \
4. [Сборка](#chapter-ii) \
    4.1. [Сборка сервера](#general-rules) \
    4.2. [Сборка клиента](#general-rules) \
5. [Запуск](#chapter-iii) \
    5.1. [Запуск сервера](#exercise-00-registration) \
    5.2. [Запуск клиента](#exercise-00-registration) \
6. [Описание кода](#chapter-v) \


# Описание проекта
Это репозиторий, содержащий исходный код многопользовательского чат-приложения на Java. Приложение реализовано с использованием Java Sockets API и включает клиентскую и серверную части. Оно позволяет пользователям регистрироваться, входить в систему, обмениваться сообщениями и взаимодействовать в чат-комнатах.

# Структура проекта
### Структура сервера

##### Класс `Server` является основным компонентом серверной части приложения. 
- Он отвечает за обработку каждого подключения клиента в отдельном потоке. Когда клиент подключается, сервер принимает соединение и создает экземпляр класса `ClientConnection` для обработки данного подключения.
- Класс `ClientConnection` реализововывает логику обработки команд от клиента. Он получает сообщения от клиента через соединение, а затем выполняет соответствующие действия в зависимости от полученной команды.

##### Для удобной обработки команд клиента реализован паттерн `command` который состоит из следующих блоков:
- Команды (commands): В этом блоке находятся интерфейсы, представляющие отдельные команды, которые клиент может отправить на сервер. В нашем случае, это `LoginCommand` и `ManipulateRoomsCommand`.

- Исполнители команд (invokers): В этом блоке находятся классы которые cодержат логику выбора и вызова соответствующей команды.

- Получатели команд (receivers): В этом блоке находятся классы, которые выполняют непосредственную обработку команды. Они содержат бизнес-логику, связанную с выполнением действий, указанных в команде.

##### Все данные должны быть записаны в базу данных.
- Компоненты `services` предназначены для облегчения взаимодействия с базами данных и предоставления удобных методов для работы с соответствующими сущностями (сообщениями, комнатами, пользователями).

- Компоненты `repositories` представляют собой реализацию паттерна репозитория и предоставляют методы для взаимодействия с базой данных. Они обеспечивают абстракцию над операциями чтения, записи, обновления и удаления данных, скрывая детали взаимодействия с базой данных и предоставляя удобный интерфейс для работы с данными. 

- Компоненты `config` и `resources` предназначены для конфигурации приложения и управление ресурсами. Включает классы, такие как SocketsApplicationConfig, которые содержат настройки и зависимости приложения, а также файлы ресурсов, такие как db.properties, содержащие настройки подключения к базе данных.

Каждый блок выполняет свою роль в общей структуре сервера и отвечает за определенные аспекты функционирования приложения.Это позволяет разделить ответственности и облегчает сопровождение и расширение кодовой базы сервера.


<details>
<summary>Нажмите, чтобы увидеть полную структуру сервера</summary>

```
- docker-compose.yml
- pom.xml
- src
  - main
    - java
      - edu
        - school21
          - sockets
            - app
              - Main.java
            - config
              - SocketsApplicationConfig.java
            - exceptions
              - CommandNotFoundExceptions.java
              - RoomsNotFoundExceptions.java
            - models
              - ChatRoom.java
              - Message.java
              - User.java
            - repositories
              - CrudRepository.java
              - messagesrepository
                - MessagesRepository.java
                - MessagesRepositoryJdbcTemplateImpl.java
              - roomsrepository
                - RoomsRepository.java
                - RoomsRepositoryJdbcTemplateImpl.java
              - usersrepository
                - UsersRepository.java
                - UsersRepositoryJdbcTemplateImpl.java
              - utils
                - TableInitializer.java
            - server
              - ClientConnection.java
              - Server.java
              - commands
                - LoginСommand.java
                - ManipulateRoomsCommand.java
              - invokers
                - LoginCommandSwitch.java
                - ManipulateRoomsCommandSwitch.java
              - receivers
                - LoginReceiver.java
                - ManipulateRoomsReceiver.java
            - services
              - messageservice
                - MessagesService.java
                - MessagesServiceImpl.java
              - roomservice
                - RoomsService.java
                - RoomsServiceImpl.java
              - userservice
                - UsersService.java
                - UsersServiceImpl.java
    - resources
      - data.sql
      - db.properties
      - schema.sql
```

</details>
### Структура клиента




# Chapter II
### General Rules
- Use this page as the only reference. Do not listen to any rumors and speculations about how to prepare your solution.
- Now there is only one Java version for you, 1.8. Make sure that compiler and interpreter of this version are installed on your machine.
- You can use IDE to write and debug the source code.
- The code is read more often than written. Read carefully the [document](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf) where code formatting rules are given. When performing each task, make sure you follow the generally accepted [Oracle standards](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html).

- Comments are not allowed in the source code of your solution. They make it difficult to read the code.
- Pay attention to the permissions of your files and directories.
- To be assessed, your solution must be in your GIT repository.
- Your solutions will be evaluated by your bootcamp mates.
- You should not leave in your "src" directory any other file than those explicitly specified by the exercise instructions. It is recommended that you modify your .gitignore to avoid accidents.
- When you need to get precise output in your programs, it is forbidden to display a precalculated output instead of performing the exercise correctly.
- Have a question? Ask your neighbor on the right. Otherwise, try with your neighbor on the left.
- Your reference manual: mates / Internet / Google. And one more thing. There's an answer to any question you may have on Stackoverflow. Learn how to ask questions correctly.
- Read the examples carefully. They may require things that are not otherwise specified in the subject.
- Use System.out for output.
- And may the Force be with you!
- Never leave that till tomorrow which you can do today ;)

# Chapter III
### Exercise 00 – Registration

Exercise 00: Registration ||
---|---
Turn-in directory	| ex00
Files to turn-in |	Chat-folder

Before you start creating a full-scale, multi-user chat, you need to implement core functionality and build the foundational architecture of the system.

Now you need to create two applications: socket-server and socket-client. Server shall support connecting a single client and be made as a separate Maven project. Server JAR file is launched as follows:
```
$ java -jar target/socket-server.jar --port=8081
```

Client is also a separate project:
```
$ java -jar target/socket-client.jar --server-port=8081
```

In this task, you need to implement the registration functionality. Example of the client operation:
```
Hello from Server!
> signUp
Enter username:
> Marsel
Enter password:
> qwerty007
Successful!
```

Connection must be closed after Successful! message appears.

To ensure secure storage of passwords, use a hashing mechanism with PasswordEncoder and BCryptPasswordEncoder (see Spring Security components). Bin for this component shall be described in a class of SocketsApplicationConfig configuration and used in UsersService.

Key client/server interaction logic and the use of UsersService via Spring Context shall be implemented in Server class.

**Additional requirements**:
- Use a single DataSource—HikariCP
- Repository operation shall be implemented via JdbcTemplate
- Services, repositories, utility classes shall be context bins.

Server application architecture (client application is at the developer's discretion):

- Char
    - SocketServer
        - src
            - main
                - java
                    - edu.school21.sockets
                        - server
                            -	Server
                        - models
                            -	User
                        - services
                            - UsersService
                            - UsersServiceImpl
                        - repositories
                            - CrudRepository
                            - UsersRepository
                            - UsersRepositoryImpl
                        - app
                            - Main
                        - config
                            - SocketsApplicationConfig
                - resources
                    - db.properties
        - pom.xml

# Chapter IV
### Exercise 01 – Messaging

Exercise 01: Messaging ||
---|---
Turn-in directory |	ex01
Files to turn-in |	Chat-folder

Once you have implemented the application backbone, you should provide multi-user message exchange.

You need to modify the application so that it supports the following chat user life cycle:
1. Registration
2. Sign in (if no user is detected, close a connection)
3. Sending messages (each user connected to the server must receive a message)
4. Logout

Example of the application operation on the client side:
```
Hello from Server!
> signIn
Enter username:
> Marsel
Enter password:
> qwerty007
Start messaging
> Hello!
Marsel: Hello!
NotMarsel: Bye!
> Exit
You have left the chat.
```
Each message shall be saved in the database and contain the following information:
- Sender
- Message text
- Sending time

**Note**:
- For comprehensive testing, several jar files of the client application shall be run.

# Chapter V
### Exercise 02 – Rooms

Exercise 02: Rooms ||
---|---
Turn-in directory |	ex02
Files to turn-in |	Chat-folder

To make our application fully-featured, let's add the concept of "chatrooms" to it. Each chatroom can have a certain set of users. The chatroom contains a set of messages from participating users.

Each user can:
1.	Create a chatroom
2.	Choose a chatroom
3.	Send a message to a chatroom
4.	Leave a chatroom

When the user re-enters the application, 30 last messages shall be displayed in the room the user visited previously.

Example of the application operation on the client side:
```
Hello from Server!
1. signIn
2. SignUp
3. Exit
> 1
Enter username:
> Marsel
Enter password:
> qwerty007
1.	Create room
2.	Choose room
3.	Exit
> 2
Rooms:
1. First Room
2. SimpleRoom
3. JavaRoom
4. Exit
> 3
Java Room ---
JavaMan: Hello!
> Hello!
Marsel: Hello!
> Exit
You have left the chat.
```

Using JSON format for message exchange will be a special task for you. In this way, each user command or message must be transferred to the server (and received from the server) in the form of a JSON line.

For example, a command for sending a message may look as follows (specific contents of messages are at the discretion of a developer):
```JSON
{
  "message" : "Hello!",
  "fromId" : 4,
  "roomId": 10
}
```
