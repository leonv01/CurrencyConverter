package com.mad.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ExchangeRateDatabase database = new ExchangeRateDatabase();
    Spinner spinnerIn, spinnerOut;
    Button calculate;
    EditText valueIn;

    TextView valueOutResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] currencies = database.getCurrencies();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_view_item, R.id.text_view,currencies);
        spinnerIn = (Spinner) findViewById(R.id.spinnerValueIn);
        spinnerOut = (Spinner) findViewById(R.id.spinnerValueTo);
        spinnerOut.setAdapter(adapter);
        spinnerIn.setAdapter(adapter);

        // creating decimal textfields for conversion
        valueIn = (EditText) findViewById(R.id.decimalValueIn);
        valueOutResult = (TextView) findViewById(R.id.textValueResult);

        calculate = (Button) findViewById(R.id.buttonConvert);
        calculate.setOnClickListener(ev -> convert());
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
        double fromValue = database.getExchangeRate(inputItem);
        double toValue = database.getExchangeRate(outputItem);

        // convert from current currency into Euro
        input /= fromValue;

        // convert from Euro to target currency
        input *= toValue;

        // format result into currency with fitting signings
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(outputItem));
        String str = format.format(input);
        valueOutResult.setText(str);
    }
}