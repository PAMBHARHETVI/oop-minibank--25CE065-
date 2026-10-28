package util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import model.annotation.MaxLength;
import model.annotation.Positive;
public final class AnnotationValidator {
    private AnnotationValidator() {}
    public static String[] validate(Object obj) {
        if (obj == null) return new String[]{"Object cannot be null."};
        List<String> errors = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (field.isAnnotationPresent(Positive.class)) {
                    Positive rule = field.getAnnotation(Positive.class);
                    if (!(value instanceof Number) || ((Number)value).longValue() <= 0)
                        errors.add(field.getName() + " " + rule.message());
                }
                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength rule = field.getAnnotation(MaxLength.class);
                    if (value instanceof String s && s.length() > rule.value())
                        errors.add(field.getName() + " must have at most " + rule.value() + " characters");
                }
            } catch (ReflectiveOperationException | RuntimeException ex) {
                errors.add("Unable to validate field " + field.getName());
            }
        }
        return errors.toArray(String[]::new);
    }
}
