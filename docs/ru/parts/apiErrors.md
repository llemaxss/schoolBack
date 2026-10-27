[Назад](../README.md)

# Ошибки API

Все исключения перехватывает `GlobalExceptionHandler` (@RestControllerAdvice) и возвращает `ErrorResponse`.
Бизнес-ошибки - `CommonException` -> HTTP 400.
Валидация `@Valid` - автоматически в `fieldErrors`.
