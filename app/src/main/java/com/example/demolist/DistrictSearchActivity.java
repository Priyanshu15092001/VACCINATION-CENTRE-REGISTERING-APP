package com.example.demolist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DistrictSearchActivity extends AppCompatActivity  {
Spinner state,district;

ArrayList<String>stateName,districtName;
HashMap<String,Integer> stateID,districtID;
TextView date;
    int IDstate = 0,IDdistrict=0;
String stateTxt,districtTxt;
SharedPreferences sharedPreferences;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_search);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar2);
        date=findViewById(R.id.date);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        ImageButton cal=findViewById(R.id.calender);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        DistrictSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(DistrictSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
        state = findViewById(R.id.spinnerstate);
        district = findViewById(R.id.spinnerdistrict);
        stateName=new ArrayList<String>();
        stateName.add("Choose your state");

        stateID=new HashMap<>();
        stateID.put("Choose your state",0);

        districtName=new ArrayList<String>();
        districtName.add("Choose your district");
        districtID=new HashMap<>();
        districtID.put("Choose your district",0);


        String urlState = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
        StringRequest requeststate = new StringRequest(Request.Method.GET, urlState, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray states = parent.getJSONArray("states");
                    JSONObject stateobj;
                    for (int i = 0; i < states.length(); i++) {
                        stateobj = states.getJSONObject(i);

                        String name=stateobj.getString("state_name");
                        int id=stateobj.getInt("state_id");
                        stateName.add(name);
                        stateID.put(name,id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DistrictSearchActivity.this, "Server error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(DistrictSearchActivity.this);
        requestQueue.add(requeststate);


        ArrayAdapter<String>stateAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout, stateName);
        stateAdapter.setDropDownViewResource(R.layout.dropdown_layout);
        state.setAdapter(stateAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    IDstate=0;  districtName.clear();
    districtName.add("Choose your district");
            IDstate =stateID.get(parent.getItemAtPosition(position).toString());
            stateTxt=parent.getItemAtPosition(position).toString();
                String urlDistrict = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + IDstate;
                StringRequest requestdistrict = new StringRequest(Request.Method.GET, urlDistrict, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(DistrictSearchActivity.this, "hitted", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject parent = new JSONObject(response);
                            JSONArray districts = parent.getJSONArray("districts");
                            JSONObject districtobj;
                            for (int i = 0; i < districts.length(); i++) {
                                districtobj = districts.getJSONObject(i);
                                districtName.add(districtobj.getString("district_name"));

                                districtID.put(districtobj.getString("district_name"),districtobj.getInt("district_id"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(DistrictSearchActivity.this, "Server error", Toast.LENGTH_LONG).show();
                            }
                        }
                );
                RequestQueue requestQueue2 = Volley.newRequestQueue(DistrictSearchActivity.this);
                requestQueue2.add(requestdistrict);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayAdapter<String>districtAdapter=new ArrayAdapter<String>(this,R.layout.spinner_layout, districtName);
        districtAdapter.setDropDownViewResource(R.layout.dropdown_layout);
        district.setAdapter(districtAdapter);
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 IDdistrict=districtID.get(parent.getItemAtPosition(position));
                 districtTxt=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }




    public void searchButtonClick(View view) {
        if (stateTxt.equals("Choose your state"))
        {
            Toast.makeText(this, "Enter your state", Toast.LENGTH_LONG).show();
        }
        else if (districtTxt.equals("Choose your district"))
        {
            Toast.makeText(this, "Enter your district", Toast.LENGTH_LONG).show();
        }
        else if (date.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Enter date", Toast.LENGTH_LONG).show();
        }
        else
        {
            sharedPreferences.edit().putString("districtId", IDdistrict+"").putString("dateShared", date.getText().toString()).apply();
        Intent intent=new Intent(this,DistrictListActivity.class);
        startActivity(intent);}
    }

    public void pinButtonClick(View view) {
        Intent intent=new Intent(this,SearchActivity.class);
        startActivity(intent);
    }


}