package model;

public interface Borrowable {

    boolean isAvailable();

    void borrow();

    void returnItem();

    default double calculateDefaultLateFee(int daysOverdue, double dailyRate) {
        if (daysOverdue <= 0)
            return 0.0;
        return daysOverdue * dailyRate;
    }

    default String getBorrowingStatus() {
        return isAvailable() ? "Available for borrowing" : "Currently borrowed";
    }

    static boolean isValidBorrowingPeriod(int days) {
        return days > 0 && days <= 30;
    }

    static double getMaxLateFee(double dailyRate) {
        return dailyRate * 30;
    }
}
