package com.kjp.shoppingcart.utils;

import jakarta.ws.rs.InternalServerErrorException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ObjectUtils {
  public static Set<String> getNullPropertyNames(Object object) {
    BeanWrapper src = new BeanWrapperImpl(object);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }
    return emptyNames;
  }

  public static <T> T getInstanceWithNotNullFields(
      T dataObject, T defaultDataObject, Class<T> targetClass) {

    if (dataObject == null || defaultDataObject == null || targetClass == null) {
      throw new IllegalArgumentException(
          "Invalid Parameters: dataObject, defaultDataObject and targetClass must not be null");
    }

    Field[] dataObjectFields = dataObject.getClass().getDeclaredFields();
    Field[] defaultDataObjectFields = defaultDataObject.getClass().getDeclaredFields();

    Map<String, Object> data =
        getMapWithNotNullData(
            dataObjectFields, defaultDataObjectFields, dataObject, defaultDataObject);

    return createInstanceWithAllArgsConstructor(targetClass, data);
  }

  private static Field findFieldByName(Field[] fields, String name) {
    for (Field field : fields) {
      if (field.getName().equals(name)) {
        return field;
      }
    }
    return null;
  }

  private static <T> Map<String, Object> getMapWithNotNullData(
      Field[] fields, Field[] defaultFields, T dataObject, T defaultDataObject) {
    Map<String, Object> data = new HashMap<>();

    try {
      for (Field defaultField : defaultFields) {

        Field dataField = findFieldByName(fields, defaultField.getName());

        if (dataField == null) {
          continue;
        }

        dataField.setAccessible(true);
        Object dataFieldValue = dataField.get(dataObject);
        String dataFieldName = dataField.getName();

        if (dataFieldValue != null) {
          data.put(dataFieldName, dataFieldValue);
          continue;
        }

        defaultField.setAccessible(true);
        Object defaultDataFieldValue = defaultField.get(defaultDataObject);
        String defaultDataFieldName = defaultField.getName();
        data.put(defaultDataFieldName, defaultDataFieldValue);
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e.getMessage());
    }

    return data;
  }

  private static <T> T createInstanceWithAllArgsConstructor(
      Class<T> classObject, Map<String, Object> data) {
    Constructor<?>[] constructors = classObject.getDeclaredConstructors();
    Constructor<?> compatibleConstructor = findCompatibleConstructor(constructors, data);

    if (compatibleConstructor == null) {
      throw new IllegalArgumentException("Compatible constructor not found");
    }

    Object[] ordenedData =
        getOrdenedParamsValueByConstructorParamsOrder(compatibleConstructor, data);

    try {
      compatibleConstructor.setAccessible(true);
      return classObject.cast(compatibleConstructor.newInstance(ordenedData));
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  private static Object[] getOrdenedParamsValueByConstructorParamsOrder(
      Constructor<?> constructor, Map<String, Object> map) {
    Parameter[] params = constructor.getParameters();
    Object[] ordenedParamsValues = new Object[params.length];

    for (int i = 0; i < params.length; i++) {
      ordenedParamsValues[i] = map.get(params[i].getName());
    }

    return ordenedParamsValues;
  }

  private static Constructor<?> findCompatibleConstructor(
      Constructor<?>[] constructors, Map<String, Object> data) {
    for (Constructor<?> constructor : constructors) {
      if (paramsEquals(constructor.getParameters(), data.keySet())) {
        return constructor;
      }
    }
    return null;
  }

  private static boolean paramsEquals(Parameter[] params, Set<String> paramsNames) {
    if (params.length != paramsNames.size()) {
      return false;
    }

    for (Parameter param : params) {
      if (!paramsNames.contains(param.getName())) {
        return false;
      }
    }

    return true;
  }
}
