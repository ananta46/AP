package se233.chapter2.model;

public class CurrencyEntity {
    private final Double rate;
    private final String date;

    public CurrencyEntity(Double rate, String date) {
        this.rate = rate;
        this.date = date;
    }

    public Double getRate() {
        return rate;
    }

    public String getTimestamp() {
        return date;
    }

    public String toString() {
        return String.format("%s %.4f", date, rate);
    }
}
