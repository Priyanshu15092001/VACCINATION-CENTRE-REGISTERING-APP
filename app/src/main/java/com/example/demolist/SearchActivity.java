package com.example.demolist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
   EditText pin,date;
   SharedPreferences sharedPreferences;
   DatePickerDialog.OnDateSetListener setListener;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        pin=findViewById(R.id.pin);
        date=findViewById(R.id.date);
        ImageButton cal=findViewById(R.id.calender);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        SearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String dateFormat = "";
                        month=month+1;
                        String dayStr=""+day,monthStr=""+month;
                        if (day<10)
                            dayStr="0"+dayStr;
                        if (month<10)
                            monthStr="0"+monthStr;
                             dateFormat = dayStr + "-" +monthStr+ "-" + year;
                        date.setText(dateFormat);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

    }
    public void searchButtonClick(View view)
    {
        if (pin.getText().toString().isEmpty())
        {
            Toast.makeText(this, "ENTER PINCODE", Toast.LENGTH_LONG).show();
        }
        else if (date.getText().toString().isEmpty())
        {
            Toast.makeText(this, "ENTER DATE", Toast.LENGTH_SHORT).show();
        }
        else {
            String pinShared=this.pin.getText().toString();
            String dateShared=this.date.getText().toString();
            sharedPreferences.edit().putString("pinShared", pinShared).putString("dateShared", dateShared).apply();
            Intent intent = new Intent(this, CustomListViewActivity.class);
            startActivity(intent);
        }
    }
}