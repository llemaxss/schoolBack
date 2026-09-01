package com.gmail.llemaxiss.app.common.enums;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Common interface for all enums
 */
public interface CommonEnum<T> extends Serializable {

  T getId();

  @Nullable
  static <T, E extends Enum<E> & CommonEnum<T>> E findById(@NotNull T id, @NotNull Class<E> enumClass) {
    E[] enumValues = enumClass.getEnumConstants();

    return Arrays.stream(enumValues)
      .filter(enumValue ->
        enumValue.getId()
          .equals(id)
      )
      .findFirst()
      .orElse(null);
  }
  
}
