package com.gmail.llemaxiss.app.common.enums.common;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
@JsonComponent
public class CommonEnumDeserializer extends JsonDeserializer<Enum<? extends CommonEnum<?>>> implements ContextualDeserializer {
  
  private Class<?> enumClass;
  
  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Enum<? extends CommonEnum<?>> deserialize(
    JsonParser jsonParser,
    DeserializationContext deserializationContext
  ) throws IOException, JacksonException {
    String value = jsonParser.getText();
    
    if (StringUtils.isBlank(value)) {
      return null;
    }
    
    return CommonEnum.findById(value, (Class) enumClass);
  }
  
  @Override
  public JsonDeserializer<?> createContextual(
    DeserializationContext deserializationContext,
    BeanProperty beanProperty
  ) throws JsonMappingException {
    Class<?> rawClass = deserializationContext.getContextualType()
      .getRawClass();
    
    if (CommonEnum.class.isAssignableFrom(rawClass)) {
      return new CommonEnumDeserializer(rawClass);
    }
    
    return this;
  }
  
}
