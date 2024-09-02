package com.example.stockdatawatcher.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.stockdatawatcher.R;
import com.example.stockdatawatcher.model.Stock;
import com.example.stockdatawatcher.presenter.StocksLoader;

public class SearchAStockFragment extends Fragment implements StocksLoader.StockApiCallback {
    TextView stockPriceTextView, changeTextView, lastUpdatedTextView, volumeTextView,
            highTextView, lowTextView;
    EditText symbol;
    Button updateButton;
    String symbolString, companyString;
    private StocksLoader loader = StocksLoader.getInstance();
    private Stock stock;
    private RequestQueue requestQueue;
    private double previousPrice = 0.0;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onestock, container, false);

        stockPriceTextView = view.findViewById(R.id.stockPriceTextView);
        changeTextView = view.findViewById(R.id.changePercentTextView);
        lastUpdatedTextView = view.findViewById(R.id.lastUpdatedTextView);
        volumeTextView = view.findViewById(R.id.volumeTextView);
        highTextView = view.findViewById(R.id.highTextView);
        lowTextView = view.findViewById(R.id.lowTextView);

        updateButton = view.findViewById(R.id.updateButton);
        symbol = view.findViewById(R.id.symbolName);
        requestQueue = Volley.newRequestQueue(view.getContext());
        loader.setApiCallback(this);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                symbolString = symbol.getText().toString();

                Stock stock = new Stock(companyString, symbolString);
                if(!symbolString.isEmpty())
                {
                    loader.fetchStockPrice(requestQueue, stock);
                }
                else {
                    Toast.makeText(view.getContext(), "Please enter a valid symbol", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void displayStockData(Stock stock) {
        stockPriceTextView.setText(String.format("%2f", stock.getPrice()));
        changeTextView.setText("Change Percent: " + stock.getChangePercent());
        lastUpdatedTextView.setText("Last Updated: " + stock.getLastUpdated());
        volumeTextView.setText("Volume: " + stock.getVolume());
        highTextView.setText("High: " + stock.getHigh());
        lowTextView.setText("Low: " + stock.getLow());
    }
}
