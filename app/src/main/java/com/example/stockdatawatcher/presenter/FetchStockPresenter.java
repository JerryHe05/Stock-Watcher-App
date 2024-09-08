package com.example.stockdatawatcher.presenter;

import com.example.stockdatawatcher.model.IModel;
import com.example.stockdatawatcher.view.IView;

public class FetchStockPresenter implements IPresenter {
    IModel model;
    IView view;

    public FetchStockPresenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }
    @Override
    public void validateSymbol() {
        String symbol = model.getSymbol();

        if (symbol.equals("")) {
            view.changeSymbolText("No Symbol");
        }
        else {
            view.changeSymbolText(symbol);
        }
    }
}
