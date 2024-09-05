package com.example.stockdatawatcher.view;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.stockdatawatcher.R;
import com.example.stockdatawatcher.model.Stock;
import com.example.stockdatawatcher.presenter.StocksLoader;
import com.example.stockdatawatcher.presenter.ViewStocksAdapter;
import com.example.stockdatawatcher.sqlite.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class StocksSearchFragment extends Fragment implements ViewStocksAdapter.OnStockClickListener{
    private RecyclerView recyclerView;
    private ViewStocksAdapter adapter;
    private List<Stock> stocks;
    private SearchView searchView;

    private StocksLoader loader = StocksLoader.getInstance();
    private DBHelper DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stocksearch, container, false);

        initializeViews(view);
        DB = new DBHelper(view.getContext());
        setAdapter(view);
        populateSearch();
        setUpSearchListener();

        return view;
    }

    private void initializeViews(View view){
        recyclerView = view.findViewById(R.id.rvItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView = view.findViewById(R.id.search_view);
    }

    private void setAdapter(View view) {
        stocks = new ArrayList<>();

        adapter = new ViewStocksAdapter(view.getContext(), stocks, this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    private void populateSearch() {
        Cursor cursor = DB.getPortfolioData();

        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No Stocks in Portfolio", Toast.LENGTH_SHORT).show();
        }

        else {
            while (cursor.moveToNext()) {
                String symbol =cursor.getString(0);
                int amount = cursor.getInt(1);
                double price = cursor.getFloat(2);
                Stock stock = new Stock(symbol);
                stock.setAmount(amount);
                stock.setPrice(price);

                stocks.add(stock);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void setUpSearchListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchStr = newText;
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onItemClick(Stock stock) {
        loadFragment(new StockViewFragment(stock.getSymbol()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}