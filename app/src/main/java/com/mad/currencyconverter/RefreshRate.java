package com.mad.currencyconverter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RefreshRate {
    private final static OkHttpClient client = new OkHttpClient();

    public static void updateCurrencies(ExchangeRateDatabase database){
        String queryString = "https://www.floatrates.com/daily/eur.json";

        try{
            Request request = new Request.Builder().url(queryString).build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            JSONObject root = new JSONObject(responseBody);

            for (String currencies : database.getCurrencies()) {
                if(!currencies.equals("EUR") && !currencies.equals("HRK")) {
                    JSONObject object = root.getJSONObject(currencies.toLowerCase());
                    String rate = object.getString("rate");
                    database.setExchangeRate(currencies, Double.parseDouble(rate));
                }
            }
        }catch (IOException e){
            Log.e("Okhttp", "Error");
        }catch (JSONException e){
            Log.e("JSON", "Error2");
            e.printStackTrace();
        }


    }
}
