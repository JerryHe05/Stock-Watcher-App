package com.example.stockdatawatcher.model;

public class Stock {
    private String symbol;
    private String company;
    private double price;
    private String lastUpdated;
    private String changePercent;
    private int volume;
    private double high;
    private double low;
    private boolean exists;
    private int amount;

    public Stock(String symbol) {
        this.symbol = symbol;
    }
    public Stock(String company, String symbol) {
        this.symbol = symbol;
        this.company = company;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public boolean isExists() {
        return exists;
    }
    public void setExists(boolean exists) {
        this.exists = exists;
    }
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

