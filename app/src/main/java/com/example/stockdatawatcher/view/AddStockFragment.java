package com.example.stockdatawatcher.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.stockdatawatcher.R;
import com.example.stockdatawatcher.model.Stock;
import com.example.stockdatawatcher.presenter.StocksLoader;
import com.example.stockdatawatcher.sqlite.DBHelper;

public class AddStockFragment extends Fragment implements StocksLoader.StockApiCallback {

    EditText symbolEditText, amountEditText;
    Button addStockButton, viewPortfolioButton;
    String symbolString;
    int amount;
    private StocksLoader loader = StocksLoader.getInstance();
    private RequestQueue requestQueue;
    private boolean stockExists;
    private DBHelper DB;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addstock, container, false);
        initializeViews(view);
        DB = new DBHelper(getContext());
        addStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                symbolString = symbolEditText.getText().toString();
                if (isPositiveInteger(amountEditText.getText().toString())) {
                    amount = Integer.parseInt(amountEditText.getText().toString());
                    Stock stock = new Stock(symbolString);
                    if (!symbolString.isEmpty()) {
                        loader.fetchStockInfo(requestQueue, stock);
                    } else {
                        Toast.makeText(view.getContext(), "Please enter a valid symbol", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(view.getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPortfolioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new StocksSearchFragment());
            }
        });

        return view;
    }

    private void initializeViews(View view) {
        symbolEditText = view.findViewById(R.id.StockSymbolEditText);
        amountEditText = view.findViewById(R.id.StockAmountEditText);
        addStockButton = view.findViewById(R.id.AddStockButton);
        viewPortfolioButton = view.findViewById(R.id.ViewPortfolioButton);
        requestQueue = Volley.newRequestQueue(view.getContext());
        loader.setApiCallback(this);
    }
    private boolean isPositiveInteger(String input) {
        try {
            int val = Integer.parseInt(input);
            return val > 0;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void performQueryResult(Stock stock) {
        if (stock.isExists()) {
            stock.setAmount(amount);
            boolean successfulDBUpdate = DB.changePortfolio(stock.getSymbol(), amount, stock.getPrice());
            if (successfulDBUpdate) {
                Toast.makeText(getContext(), "Successfully added to Portfolio", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "An Error Occurred When Updating Portfolio", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getContext(), "Please enter a valid symbol", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
