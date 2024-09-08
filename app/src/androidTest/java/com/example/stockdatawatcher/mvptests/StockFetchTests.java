package com.example.stockdatawatcher.mvptests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.example.stockdatawatcher.model.Stock;
import com.example.stockdatawatcher.presenter.FetchStockPresenter;
import com.example.stockdatawatcher.presenter.IPresenter;
import com.example.stockdatawatcher.view.IView;
import com.example.stockdatawatcher.model.IModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockFetchTests {
    @Mock
    public IView view;

    @Mock
    public IModel model;

    @Test
    public void testOnFetchResult(){
        when(view.getSymbolString()).thenReturn("IBM");
        when(model.getSymbol()).thenReturn("IBM");

        IPresenter presenter = new FetchStockPresenter(model, view);

        presenter.validateSymbol();
        verify(view).changeSymbolText("IBM");
    }


}
