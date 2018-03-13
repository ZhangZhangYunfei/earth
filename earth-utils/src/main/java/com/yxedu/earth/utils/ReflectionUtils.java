package com.yxedu.earth.utils;


import java.lang.reflect.Field;

/**
 * Reflection Utils. refer to "org.springframework.util.ReflectionUtils".
 */
public class ReflectionUtils {

  /**
   * Attempt to find a {@link Field field} on the supplied {@link Class} with the
   * supplied {@code name} and/or {@link Class type}. Searches all superclasses
   * up to {@link Object}. <br>
   * <b>No local cache is used !</b>
   * @param clazz the class to introspect
   * @param name the name of the field, non-null
   * @param type the type of the field (may be {@code null} if name is specified)
   * @return the corresponding Field object, or {@code null} if not found
   */
  public static Field findField(Class<?> clazz, String name, Class<?> type) {
    Class<?> searchType = clazz;
    while (Object.class != searchType && searchType != null) {
      Field[] fields = searchType.getDeclaredFields();
      for (Field field : fields) {
        if (name.equals(field.getName()) && type.equals(field.getType())) {
          return field;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  /**
   * Set the field represented by the supplied {@link Field field object} on the
   * specified {@link Object target object} to the specified {@code value}.
   * In accordance with {@link Field#set(Object, Object)} semantics, the new value
   * is automatically unwrapped if the underlying field has a primitive type.
   * @param clazz the class to introspect
   * @param field the name of the field, non-null
   * @param type the type of the field (may be {@code null} if name is specified)
   * @param target the target object on which to set the field
   * @param value the field value to set (may be {@code null})
   */
  public static <T, F> void setField(Class<T> clazz, String field, Class<F> type,
                              T target, F value) {
    Field setField = findField(clazz, field, type);
    if (setField == null) {
      return;
    }
    try {
      setField.setAccessible(true);
      setField.set(target, value);
    } catch (IllegalAccessException ex) {
      throw new IllegalStateException(
          "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
    }
  }
}
