package se233.chapter2.controller;

import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.util.ArrayList;

public class Initialize {
    public static ArrayList<Currency> initialize_app() {
        Currency c = new Currency("THB","USD");

        // Number 1: change number to 30 days check EventHandler for 2more  //#1
        ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(c.getBaseFrom(), c.getShortCode(), 30);
        c.setHistorical(c_list);
        c.setCurrent(c_list.get(c_list.size() - 1));
        ArrayList<Currency> currencyList = new ArrayList<>();
        currencyList.add(c);
        return currencyList;
    }
}
