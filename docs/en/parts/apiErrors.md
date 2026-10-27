[Back](../README.md)

# API errors

All exceptions are caught by `GlobalExceptionHandler` (@RestControllerAdvice) and returned as `ErrorResponse`.
Business errors - `CommonException` -> HTTP 400.
`@Valid` validation failures are mapped into `fieldErrors` automatically.
