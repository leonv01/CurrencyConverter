package com.mad.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ExchangeRateDatabase database;
    Spinner spinnerIn, spinnerOut;
    Button calculate;
    EditText valueIn;

    TextView valueOutResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new ExchangeRateDatabase();

        String[] currencies = database.getCurrencies();

        CurrencyItemAdapter adapter = new CurrencyItemAdapter(database);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.my_menu, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.my_menu_entry) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    Intent currencyIntent = new Intent(MainActivity.this, CurrencyListActivity.class);
                    startActivity(currencyIntent);
                    return false;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    }
}