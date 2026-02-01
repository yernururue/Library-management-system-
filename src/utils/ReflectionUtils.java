package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    public static void printClassInfo(Class<?> clazz) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("REFLECTION UTILITY - CLASS INSPECTION");
        System.out.println("=".repeat(60));

        System.out.println("\n[CLASS INFO]");
        System.out.println("  Name: " + clazz.getName());
        System.out.println("  Simple Name: " + clazz.getSimpleName());
        System.out.println("  Package: " + (clazz.getPackage() != null ? clazz.getPackage().getName() : "default"));
        System.out.println(
                "  Superclass: " + (clazz.getSuperclass() != null ? clazz.getSuperclass().getSimpleName() : "None"));

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.println("  Implements: ");
            for (Class<?> iface : interfaces) {
                System.out.println("    - " + iface.getSimpleName());
            }
        }

        System.out.println("  Is Abstract: " + Modifier.isAbstract(clazz.getModifiers()));
        System.out.println("  Is Interface: " + clazz.isInterface());
    }

    public static void printFields(Class<?> clazz) {
        System.out.println("\n[FIELDS]");
        Field[] fields = clazz.getDeclaredFields();

        if (fields.length == 0) {
            System.out.println("  No declared fields");
            return;
        }

        for (Field field : fields) {
            String modifier = Modifier.toString(field.getModifiers());
            System.out.printf("  %s %s %s%n",
                    modifier.isEmpty() ? "default" : modifier,
                    field.getType().getSimpleName(),
                    field.getName());
        }
    }

    public static void printMethods(Class<?> clazz) {
        System.out.println("\n[METHODS]");
        Method[] methods = clazz.getDeclaredMethods();

        if (methods.length == 0) {
            System.out.println("  No declared methods");
            return;
        }

        for (Method method : methods) {
            String modifier = Modifier.toString(method.getModifiers());
            StringBuilder params = new StringBuilder();
            Class<?>[] paramTypes = method.getParameterTypes();

            for (int i = 0; i < paramTypes.length; i++) {
                params.append(paramTypes[i].getSimpleName());
                if (i < paramTypes.length - 1)
                    params.append(", ");
            }

            System.out.printf("  %s %s %s(%s)%n",
                    modifier.isEmpty() ? "default" : modifier,
                    method.getReturnType().getSimpleName(),
                    method.getName(),
                    params);
        }
    }

    public static void inspectObject(Object obj) {
        if (obj == null) {
            System.out.println("Cannot inspect null object");
            return;
        }

        Class<?> clazz = obj.getClass();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("OBJECT INSPECTION");
        System.out.println("=".repeat(60));
        System.out.println("  Runtime Type: " + clazz.getSimpleName());
        System.out.println("  Hash Code: " + obj.hashCode());

        System.out.println("\n[FIELD VALUES]");
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                System.out.printf("  %s = %s%n", field.getName(), value);
            } catch (IllegalAccessException e) {
                System.out.printf("  %s = [ACCESS DENIED]%n", field.getName());
            }
        }
    }

    public static void analyzeClass(Class<?> clazz) {
        printClassInfo(clazz);
        printFields(clazz);
        printMethods(clazz);
        System.out.println("\n" + "=".repeat(60) + "\n");
    }

    public static void compareClasses(Class<?> class1, Class<?> class2) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CLASS COMPARISON");
        System.out.println("=".repeat(60));

        System.out.println("\n  Class 1: " + class1.getSimpleName());
        System.out.println("  Class 2: " + class2.getSimpleName());

        System.out.println("\n  Inheritance:");
        System.out.println("    " + class1.getSimpleName() + " extends " + class2.getSimpleName() + ": " +
                class2.isAssignableFrom(class1));
        System.out.println("    " + class2.getSimpleName() + " extends " + class1.getSimpleName() + ": " +
                class1.isAssignableFrom(class2));

        System.out.println("\n  Field counts:");
        System.out.println("    " + class1.getSimpleName() + ": " + class1.getDeclaredFields().length + " fields");
        System.out.println("    " + class2.getSimpleName() + ": " + class2.getDeclaredFields().length + " fields");

        System.out.println("\n  Method counts:");
        System.out.println("    " + class1.getSimpleName() + ": " + class1.getDeclaredMethods().length + " methods");
        System.out.println("    " + class2.getSimpleName() + ": " + class2.getDeclaredMethods().length + " methods");
    }
}
