package com.mad.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditCurrencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_currency);

        String currency = (String) getIntent().getSerializableExtra("currency");
        String conversionRate = (String) getIntent().getSerializableExtra("conversionRate");

        TextView textViewCurrencyName = (TextView) findViewById(R.id.text_view_conversion_name);
        textViewCurrencyName.setText(currency);

        TextView textViewConversionOld = (TextView) findViewById(R.id.text_view_conversion_old);
        textViewConversionOld.setText(conversionRate);

        EditText decimalField = (EditText) findViewById(R.id.decimal_field_conversion);

        Button conversionButton = (Button) findViewById(R.id.button_conversion);
        conversionButton.setOnClickListener(ev ->{
            double input;
            try{
                input = Double.parseDouble(decimalField.getText().toString());
            }catch (NumberFormatException e){
                input = 0;
            }
            textViewConversionOld.setText(Double.toString(input));

        });
    }
}