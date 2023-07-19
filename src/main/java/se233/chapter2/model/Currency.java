package se233.chapter2.model;

import java.util.ArrayList;

public class Currency {
    private String shortCode;
    private String baseFrom;              //From Number 6 also check initialize for the update #1
    private CurrencyEntity current;
    private ArrayList<CurrencyEntity> historical;
    private Boolean isWatch;
    private Double watchRate;

    public Currency(String baseFrom, String shortCode) {
        this.baseFrom = "THB";
        this.shortCode = shortCode;
        this.isWatch = false;
        this.watchRate = 0.0;
    }

    public String getBaseFrom() {         //From Number 6
        return baseFrom;
    }
    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public CurrencyEntity getCurrent() {
        return current;
    }

    public void setCurrent(CurrencyEntity current) {
        this.current = current;
    }

    public ArrayList<CurrencyEntity> getHistorical() {
        return historical;
    }

    public void setHistorical(ArrayList<CurrencyEntity> historical) {
        this.historical = historical;
    }

    public Boolean getWatch() {
        return isWatch;
    }

    public void setWatch(Boolean watch) {
        isWatch = watch;
    }

    public Double getWatchRate() {
        return watchRate;
    }

    public void setWatchRate(Double watchRate) {
        this.watchRate = watchRate;
    }
}