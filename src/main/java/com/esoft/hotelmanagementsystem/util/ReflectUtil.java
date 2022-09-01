package com.esoft.hotelmanagementsystem.util;

import com.esoft.hotelmanagementsystem.exception.CommonException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public final class ReflectUtil {

    public static <T> T newInstanceOf(Class<T> type) {
        try {
            T obj;
            Constructor<T> constructor = type.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            obj = constructor.newInstance();

            return obj;
        } catch (Exception e) {
            log.error("Error in ReflectUtil.newInstanceOf : " + e.getMessage(), e);
            throw new CommonException(e.getMessage(), e);
        }
    }

    public static <T> void setFieldData(T instance, Field field, Object data) {
        try {
            field.setAccessible(true);
            field.set(instance, data);
        } catch (Exception e) {
            log.error("Error in ReflectUtil.setFieldData : " + e.getMessage(), e);
            throw new CommonException(e.getMessage(), e);
        }
    }

    public static Object castValue(Field field, String rawValue) {
        Class<?> fieldType = field.getType();
        return getValueObject(field, rawValue, fieldType);
    }

    private static Object getValueObject(Field field, String rawValue, Class<?> fieldType) {

        if (null != rawValue) {
            String value = rawValue.trim();

            Object o;
            if (fieldType == int.class || fieldType == Integer.class) {
                o = Integer.parseInt(value);

            } else if (fieldType == BigDecimal.class) {
                o = new BigDecimal(value);

            } else if (fieldType == long.class || fieldType == Long.class) {
                o = Long.parseLong(value);

            } else if (fieldType == double.class || fieldType == Double.class) {
                o = Double.parseDouble(value);

            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                o = parseStringToBool(value);

            } else if (fieldType == float.class || fieldType == Float.class) {
                o = Float.parseFloat(value);

            } else if (fieldType == Date.class) {
                o = dateValue(value);

            } else if (fieldType == LocalDate.class) {
                o = localDateValue(value);

            } else if (fieldType == LocalDateTime.class) {
                o = localDateTimeValue(value);

            } else if (fieldType.isEnum()) {
                o = enumValue(value, fieldType);

            } else if (value.isEmpty()) {
                o = null;
            } else if (fieldType == List.class) {
                o = castListValue(value, field);
            } else {
                o = value;
            }
            return o;
        }else {
            return null;
        }
    }

    private static Boolean parseStringToBool(String value) {
        if ("true".equalsIgnoreCase(value.trim())) {
            return true;
        }

        if ("false".equalsIgnoreCase(value.trim())) {
            return false;
        }

        /* LibreOffice compatibility:
         *
         * LibreOffice booleans are stored as formula =TRUE() or =FALSE() which is parsed by POI to 1 or 0.
         */
        if ("1".equals(value.trim())) {
            return true;
        }

        if ("0".equals(value.trim())) {
            return false;
        }

        throw new CommonException("add");
    }

    private static Date dateValue(String value) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss\"");
            return sdf.parse(value);
        } catch (ParseException e) {
            log.error("Error in Casting - : " + e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    private static LocalDate localDateValue(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/M/yyyy"));
    }

    private static LocalDateTime localDateTimeValue(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm:ss"));
    }


    private static Object enumValue(String value, Class<?> type) {
        return Arrays.stream(type.getEnumConstants())
                .filter(o -> ((Enum<?>) o).name().equals(value))
                .findFirst()
                .orElseGet(() -> {
                    IllegalArgumentException e = new IllegalArgumentException("No enumeration " + type.getSimpleName() + "." + value);
                    log.error("Error in enumValue cast -" + e.getMessage(), e);
                    return null;
                });

    }

    private static Object castListValue(String value, Field field) {
        final ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        final Type fieldType = genericType.getActualTypeArguments()[0];
        String[] valueList = value.split("\\s*,\\s*");

        if (fieldType == Integer.class) {
            return Stream.of(valueList)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } else if (fieldType == BigDecimal.class) {
            return Stream.of(valueList)
                    .map(BigDecimal::new)
                    .collect(Collectors.toList());
        } else if (fieldType == Long.class) {
            return Stream.of(valueList)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } else if (fieldType == Double.class) {
            return Stream.of(valueList)
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
        } else if (fieldType == Boolean.class) {
            return Stream.of(valueList)
                    .map(ReflectUtil::parseStringToBool)
                    .collect(Collectors.toList());
        } else if (fieldType == Float.class) {
            return Stream.of(valueList)
                    .map(Float::parseFloat)
                    .collect(Collectors.toList());
        } else {
            return Arrays.asList(valueList);
        }
    }
}
