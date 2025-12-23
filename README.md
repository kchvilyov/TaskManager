# TaskManager
🎯 Задание: «Модуль управления задачами (Task Manager Core)»
Цель: Разработать на чистом Kotlin (без Android/iOS специфики) модуль, реализующий логику управления задачами, с использованием требуемых в вакансии архитектурных подходов и технологий.
Фокус на проверяемых навыках:
Kotlin & ООП
Архитектура (Clean Architecture, MVI/MVVM)
Многопоточность (Coroutines)
Внедрение зависимостей (ручная реализация или Koin)
Локальная база данных (эмуляция через Room-подобный интерфейс)
Структурирование кода, тестируемость

📋 Техническое задание (на 4 часа)
1. Реализуйте доменный слой (Domain Layer) – ~45 мин
   Создайте data class Task с полями: id: Int, title: String, description: String, isCompleted: Boolean, createdAt: Long.
   Создайте интерфейс репозитория TaskRepository с методами: getAllTasks(), getTaskById(id: Int), insertTask(task: Task), updateTask(task: Task), deleteTask(id: Int).

2. Реализуйте слой данных (Data Layer) – ~1 час
   Создайте LocalTaskDataSource — класс, реализующий TaskRepository.
   Вместо реальной БД: используйте MutableList<Task> для хранения задач в памяти.
   Имитируйте асинхронные операции, используя kotlinx.coroutines (например, delay(100) и flow).
   Создайте TaskRepositoryImpl, который будет использовать LocalTaskDataSource.

3. Реализуйте логику презентации (Presentation Layer) по принципу MVI – ~1.5 часа
   Создайте sealed class TaskIntent с действиями: LoadTasks, AddTask, ToggleTask.
   Создайте data class TaskState со списком задач и полем isLoading.
   Создайте класс TaskViewModel (или TaskProcessor).
   В нём реализуйте:
   Поток (StateFlow/SharedFlow) для TaskState.
   Функцию processIntent(intent: TaskIntent), которая в зависимости от намерения вызывает методы репозитория и обновляет состояние.
   Используйте Coroutines (viewModelScope эмулировать через CoroutineScope).

4. Реализуйте простой DI – ~30 мин
   Соберите зависимости (DataSource, Repository, ViewModel) вручную в классе AppContainer или, если успеете, используйте Koin (минимальная настройка: single, factory).

5. Напишите базовые тесты – ~45 мин
   Протестируйте TaskViewModel/TaskProcessor с использованием kotlinx-coroutines-test.
   Проверьте сценарии: начальная загрузка, добавление задачи, переключение статуса.
   ✅ Критерии оценки (что проверит ревьюер)
   Чистота архитектуры: Четкое разделение на слои, зависимости направлены внутрь (Domain не знает о Data).
   Корректность Kotlin: Использование data class, sealed class, extension-функций, nullable-типов.
   Работа с асинхронностью: Безопасное использование Coroutines, StateFlow/SharedFlow.
   Тестируемость: Классы принимают зависимости через конструктор (интерфейсы), легко мокаются в тестах.
   Читаемость и структура: Понятные названия, логическое разделение по пакетам/файлам.

📱 Интеграция с платформами:
Android: Данный модуль станет ядром Android-приложения.
TaskViewModel будет использован в Jetpack Compose-экране.
LocalTaskDataSource будет заменён на реализацию с Room.
AppContainer будет переписан с использованием Hilt.
iOS: Этот модуль может быть скомпилирован в iOS-фреймворк с помощью Kotlin Multiplatform Mobile (KMM).
На стороне Swift будет создан UIViewController, подписанный на StateFlow (черед KMM-обёртки), для отображения данных в UITableView/SwiftUI.

This project uses [Gradle](https://gradle.org/).
To build and run the application, use the *Gradle* tool window by clicking the Gradle icon in the right-hand toolbar,
or run it directly from the terminal:

* Run `./gradlew run` to build and run the application.
* Run `./gradlew build` to only build the application.
* Run `./gradlew check` to run all checks, including tests.
* Run `./gradlew clean` to clean all build outputs.

Note the usage of the Gradle Wrapper (`./gradlew`).
This is the suggested way to use Gradle in production projects.

[Learn more about the Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

[Learn more about Gradle tasks](https://docs.gradle.org/current/userguide/command_line_interface.html#common_tasks).

This project follows the suggested multi-module setup and consists of the `app` and `core` subprojects.
The shared build logic was extracted to a convention plugin located in `buildSrc`.

This project uses a version catalog (see `gradle/libs.versions.toml`) to declare and version dependencies
and both a build cache and a configuration cache (see `gradle.properties`).