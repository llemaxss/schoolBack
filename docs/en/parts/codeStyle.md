[Back](../README.md)

# Code style

## Formatting

- Indent is 2 spaces. Empty lines inside classes and methods keep the indent too.
- Within a class, leave 3 empty lines: after `{` and `}`, as well as before `}`.
- Call chains: every next `.method(...)` goes on a new line.
If a call has an expression as an argument,
arguments are moved to separate lines,
the closing parenthesis is on its own line too:

```java
mockMvc.perform(
  post(UserController.URL)
    .header(
      HttpHeaders.AUTHORIZATION,
      adminBearerToken()
    )
)
  .andExpect(
    status()
      .isCreated()
  );
```

- Method order in classes and interfaces: `get` methods first, then `create`, `update`, `delete`, then the others (for example `activate/deactivate`), private methods at the end.
- Enum constants are not sorted alphabetically: they are grouped by meaning (for example `Permission` by entity and by CRUD).

## Call nesting

At most two levels: `a(b(c))` is fine, `a(b(c(d)))` is not.
Extract all nested logic into variables or private methods.
Also, move lambdas that contain multiple actions into separate methods.

```java
// bad
return new FieldError(name, findCode(error(result)), message(error(result)));

// good
String code = findCode(error);
String message = error.getDefaultMessage();

return new FieldError(name, code, message);
```

Exclusions are possible, provided that the readability of the code is not compromised.

## Method names: `get` and `find`

- `getX` - **"return or throw"**: always returns an object or throws an exception (`CommonException` with an `ErrorCode`). Never returns `null`.
- `findX` - **"find or return null"**: returns `Optional<X>`. An empty `Optional` means "not found". The method itself never returns `null`.
- `null` should not be in returned values.
Exception: overridden methods of third-party APIs whose contract requires `null`.
- Derived repository methods (`findByUsername`, etc.) return `Optional` as well.

## Nullability annotations

- A parameter **without an annotation** means "may be `null`". We do not write `@Nullable` on parameters.
- If `null` is not allowed, sets `@NotNull`, and `@NotBlank` for `String`. The same applies to returned values: `@NotNull` above the method, `@NotBlank` for a string.
- `@Nullable` above a method is allowed only where `Optional` cannot be returned (overriding a third-party API). Otherwise use `Optional`.

## Validation of request models

Request models use only `@NotNull`, `@NotBlank`.
Other validation-annotations (`@Size`, `@Min`, `@Max`, etc.) are not written.
Specific formats (for example a document number with an exact number of characters), they are checked in the service.

## Tests

- URLs come from controller constants (`UserController.URL`), not built from strings.
- Error codes are compared through the enum: `ErrorCode.X.getId()`, no string literals.
- Within the code, organize tests into blocks using inner classes (with the `@Nested` annotation). The main blocks are:`Create\Update\Delete\Other`.
- Wrap each test block in `//region <name> //endregion <name>` comments to improve code readability and navigation.