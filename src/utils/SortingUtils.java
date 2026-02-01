package utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {

    public static <T> List<T> sortBy(List<T> list, Comparator<T> comparator) {
        List<T> sorted = new ArrayList<>(list);
        sorted.sort(comparator);
        return sorted;
    }

    public static <T> List<T> sortByDescending(List<T> list, Comparator<T> comparator) {
        List<T> sorted = new ArrayList<>(list);
        sorted.sort(comparator.reversed());
        return sorted;
    }

    public static List<String> sortStringsAlphabetically(List<String> strings) {
        return sortBy(strings, (s1, s2) -> s1.compareToIgnoreCase(s2));
    }

    public static List<String> sortStringsByLength(List<String> strings) {
        return sortBy(strings, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
    }

    public static <T> void sortAndPrint(List<T> list, Comparator<T> comparator, String description) {
        System.out.println("\n=== " + description + " ===");
        List<T> sorted = sortBy(list, comparator);
        sorted.forEach(item -> System.out.println("  " + item));
    }
}
