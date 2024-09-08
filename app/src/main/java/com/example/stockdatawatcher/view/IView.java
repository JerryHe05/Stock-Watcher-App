package com.example.stockdatawatcher.view;

import com.example.stockdatawatcher.model.Stock;

public interface IView {
    void changeSymbolText(String symbol);
    public String getSymbolString();
}
