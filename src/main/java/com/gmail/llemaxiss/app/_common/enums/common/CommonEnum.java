package com.gmail.llemaxiss.app._common.enums.common;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;

/**
 * Common interface for all enums
 */
public interface CommonEnum<T> {

  T getId();

  @NotNull
  static <T, E extends Enum<E> & CommonEnum<T>> E findById(@NotNull T id, @NotNull Class<E> enumClass) {
    E[] enumValues = enumClass.getEnumConstants();

    return Arrays.stream(enumValues)
      .filter(enumValue ->
        enumValue.getId()
          .equals(id)
      )
      .findFirst()
      .orElseThrow(() -> {
        String message = String.format("Invalid value %s for enum %s", id, enumClass.getSimpleName());
        
        return new CommonException(
          ErrorCode.INVALID_ENUM_VALUE,
          message
        );
      });
  }
  
}
