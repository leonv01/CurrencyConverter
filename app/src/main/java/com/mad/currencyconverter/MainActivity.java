package com.mad.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ExchangeRateDatabase database;
    Spinner spinnerIn, spinnerOut;
    Button calculate;
    EditText valueIn;

    TextView valueOutResult;
    ShareActionProvider shareActionProvider;

    CurrencyItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new ExchangeRateDatabase();

        adapter = new CurrencyItemAdapter(database);

        spinnerIn = (Spinner) findViewById(R.id.spinnerValueIn);
        spinnerOut = (Spinner) findViewById(R.id.spinnerValueTo);
        spinnerOut.setAdapter(adapter);
        spinnerIn.setAdapter(adapter);

        // creating decimal textfields for conversion
        valueIn = (EditText) findViewById(R.id.decimalValueIn);
        valueOutResult = (TextView) findViewById(R.id.textValueResult);

        calculate = (Button) findViewById(R.id.buttonConvert);
        calculate.setOnClickListener(ev -> convert());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareText(null);
        return true;
    }

    private void setShareText(String text){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if(text != null){
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        shareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.my_menu_entry) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    Intent currencyIntent = new Intent(MainActivity.this, CurrencyListActivity.class);
                    startActivity(currencyIntent);
                    return true;
                }
            });
        }
        if(item.getItemId() == R.id.refresh_entry){
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    updateCurrencies();
                    return true;
                }
            });
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String enteredValue = valueIn.getText().toString();

        int sourceCurrency = spinnerIn.getSelectedItemPosition();
        int targetCurrency = spinnerOut.getSelectedItemPosition();
        editor.putInt("SourceCurrency", sourceCurrency);
        editor.putInt("TargetCurrency", targetCurrency);
        editor.putString("EnteredValue", enteredValue);

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCurrencies();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        int sourceCurrency = prefs.getInt("SourceCurrency", 0);
        int targetCurrency = prefs.getInt("TargetCurrency", 0);
        String enteredValue = prefs.getString("EnteredValue", "");

        spinnerIn.setSelection(sourceCurrency);
        spinnerOut.setSelection(targetCurrency);
        valueIn.setText(enteredValue);
    }

    public void convert(){
        // get current selected items by the spinners
        String inputItem = (String) spinnerIn.getSelectedItem();
        String outputItem = (String) spinnerOut.getSelectedItem();

        // get exchange rate
        double input;
        try {
            input = Double.parseDouble(valueIn.getText().toString());
        }
        catch (NumberFormatException e){
            input = 0;
        }

        double output = database.convert(input, inputItem, outputItem);

        // format result into currency with fitting signings
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(outputItem));
        String str = format.format(output);
        valueOutResult.setText(str);
        setShareText(str);
    }

    public void updateCurrencies(){
        RefreshRate.updateCurrencies(database);
        adapter.notifyDataSetChanged();
    }
}