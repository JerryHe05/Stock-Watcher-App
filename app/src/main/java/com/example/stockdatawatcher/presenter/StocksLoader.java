package com.example.stockdatawatcher.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stockdatawatcher.model.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// Singleton to load stocks
public final class StocksLoader {

    // Note free keys have up to 25 queries a day
    // use demo key "demo" for examples given on the Alphavantage documentation
    //private final String key = "demo";
    private final String key = "GCOFJJ20L7GLFYI9";
    private static StocksLoader loader;
    private StockApiCallback apiCallback;

    private StocksLoader() {

    }

    public interface StockApiCallback {
        void performQueryResult(Stock stock);
    }

    public void setApiCallback(StockApiCallback apiCallback) {
        this.apiCallback = apiCallback;
    }

    public static StocksLoader getInstance() {
        if (loader == null) loader = new StocksLoader();

        return loader;
    }

    public ArrayList<Stock> getStocksBasedOnKeyword(RequestQueue requestQueue, String keyword) {

        ArrayList<Stock> stocks = new ArrayList<>();

        String thisURL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + keyword + "&apikey=" + key;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, thisURL, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("BestMatches");
                    for (int i = 0; i < arr.length(); i++){
                        String company = (arr.getJSONObject(i)).getString("2. name");

                        String symbol = (arr.getJSONObject(i)).getString("1. symbol");

                        stocks.add(new Stock(company, symbol));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "Error Parsing JSON: " + e.getMessage());
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String responseBody = new String(error.networkResponse.data);
                    Log.e("API_ERROR", "Error Response: " + responseBody);
                }
                Log.e("API_ERROR", "Error fetching stock details: " + error.getMessage());
            }
        });
        requestQueue.add(request);
        return stocks;
    }

    public void fetchStockInfo(RequestQueue requestQueue, Stock stock) {
        String thisURL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stock.getSymbol() + "&apikey=" + key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, thisURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stockPrice = response.getJSONObject("Global Quote").getString("05. price");
                            stock.setPrice(Double.parseDouble(stockPrice));

                            String stockPercentChange = response.getJSONObject("Global Quote").getString("10. change percent");
                            stock.setChangePercent(stockPercentChange);

                            String stockLastUpdated = response.getJSONObject("Global Quote").getString("07. latest trading day");
                            stock.setLastUpdated(stockLastUpdated);

                            String stockVolume = response.getJSONObject("Global Quote").getString("06. volume");
                            stock.setVolume(Integer.parseInt(stockVolume));

                            String stockHigh = response.getJSONObject("Global Quote").getString("03. high");
                            stock.setHigh(Double.parseDouble(stockHigh));

                            String stockLow = response.getJSONObject("Global Quote").getString("04. low");
                            stock.setLow(Double.parseDouble(stockLow));

                            stock.setExists(true);
                            apiCallback.performQueryResult(stock);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Error Parsing JSON: " + e.getMessage());
                        }
                        catch (NumberFormatException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Error Parsing to double: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String responseBody = new String(error.networkResponse.data);
                            Log.e("API_ERROR", "Error Response: " + responseBody);
                        }
                        Log.e("API_ERROR", "Error fetching stock details: " + error.getMessage());
                    }
                });
        requestQueue.add(request);
    }
}
