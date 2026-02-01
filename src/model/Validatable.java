package model;

public interface Validatable<T> {

    boolean validate();

    String getValidationError();

    default boolean validateAndLog() {
        if (!validate()) {
            System.out.println("Validation Error: " + getValidationError());
            return false;
        }
        System.out.println("Validation passed for: " + this.getClass().getSimpleName());
        return true;
    }

    default String getValidationSummary() {
        return String.format("[%s] Valid: %s%s",
                this.getClass().getSimpleName(),
                validate(),
                validate() ? "" : " - " + getValidationError());
    }

    static <T> boolean isNotNull(T obj) {
        return obj != null;
    }

    static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    static boolean isPositive(int number) {
        return number > 0;
    }

    static boolean isValidYear(int year) {
        return year > 0 && year <= 2026;
    }
}
