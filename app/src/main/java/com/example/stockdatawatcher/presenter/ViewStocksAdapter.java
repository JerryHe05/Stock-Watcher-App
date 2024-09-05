package com.example.stockdatawatcher.presenter;

import android.widget.Filterable;
import android.content.Context;
import android.net.Uri;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.stockdatawatcher.R;
import com.example.stockdatawatcher.model.Stock;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.List;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class ViewStocksAdapter extends RecyclerView.Adapter<ViewStocksAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Stock> stocks;
    private OnStockClickListener listener;
    private List<Stock> getStocksFilter;
    private RequestQueue requestQueue;
    private StocksLoader loader = StocksLoader.getInstance();

    public interface OnStockClickListener {
        void onItemClick(Stock stock);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (invalidCharSequence(charSequence)){
                    filterResults.values = getStocksFilter;
                    filterResults.count = getStocksFilter.size();
                }
                else {
                    String searchStr = charSequence.toString().toLowerCase();
                    List<Stock> newStocks = new ArrayList<>();
                    getItemsFromFilter(newStocks, searchStr);

                    filterResults.values = newStocks;
                    filterResults.count = newStocks.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stocks = (List<Stock>) filterResults.values;
                // update adapter for user
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public ViewStocksAdapter(Context context, List<Stock> stocks, OnStockClickListener listener) {
        this.context = context;
        this.stocks = stocks;
        this.getStocksFilter = stocks;
        this.listener = listener;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stock_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Stock stock = stocks.get(position);

        holder.SymbolTextView.setText(stock.getSymbol());
        holder.AmountTextView.setText("Amount Owned: " + stock.getAmount());
        holder.ValueTextView.setText("Value: " + stock.getPrice() * stock.getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(stock);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (stocks == null) return 0;

        return stocks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView AmountTextView, SymbolTextView, ValueTextView;
        CardView stockCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.AmountTextView = itemView.findViewById(R.id.AmountTextView);
            this.SymbolTextView = itemView.findViewById(R.id.SymbolTextView);
            this.ValueTextView = itemView.findViewById(R.id.ValueTextView);
            this.stockCard = itemView.findViewById(R.id.stockCard);
        }
    }

    private boolean findMatch(Stock stock, String searchStr) {
        return stock.getSymbol().toLowerCase().contains(searchStr.toLowerCase());
    }

    private void getItemsFromFilter(List<Stock> newStocks, String searchStr) {
        for (Stock stock: getStocksFilter){
            if (findMatch(stock, searchStr)) {
                newStocks.add(stock);
            }
        }
    }

    private boolean invalidCharSequence(CharSequence charSequence) {
        return (charSequence == null || charSequence.length() == 0);
    }

}
