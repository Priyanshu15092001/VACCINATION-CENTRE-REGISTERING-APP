package com.example.demolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomListViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<VaccineCenter>vcenter;
    String pin, date;
    SharedPreferences sharedPreferences;
  //  ArrayList<String> centerList,districtList;
   // ArrayList<Boolean>availableList;
  //  String title[]={"Name1","Name2","Name3","Name4","Name5","Name6"};
//String description[]={"Cricketer","Singer","App Developer","Software developer","Game developer","Soldier"};
//int imagelist[]={R.drawable.banana,R.drawable.grapes,R.drawable.lemon,R.drawable.strawberry,R.drawable.orange};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        pin=sharedPreferences.getString("pinShared","Pincode");
        date=sharedPreferences.getString("dateShared","Date");
          recyclerView=findViewById(R.id.listview);
          recyclerView.setLayoutManager(new LinearLayoutManager(this));
          vcenter=new ArrayList<VaccineCenter>();
        String urlString = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+pin+"&date="+date;
        StringRequest request = new StringRequest(Request.Method.GET, urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CustomListViewActivity.this, "URL hit successfully", Toast.LENGTH_SHORT).show();
                try {
                    String name, district;
                    JSONObject parentObj = new JSONObject(response);
                    JSONArray centers = parentObj.getJSONArray("centers");
                    JSONObject jsonObject;
                    JSONArray sessionArray;
                    boolean isAvailable=false;
                    int available_capacity,min_age_limit;
                    getSupportActionBar().setTitle("Centers Found:"+centers.length());
                    for (int i = 0; i < centers.length(); i++) {
                        jsonObject = centers.getJSONObject(i);
                        name = jsonObject.getString("name");
                        district = jsonObject.getString("district_name");
                       isAvailable=false;
                        sessionArray=jsonObject.getJSONArray("sessions");
                        for (int j=0;j<sessionArray.length();j++)
                        {
                            available_capacity=(sessionArray.getJSONObject(j)).getInt("available_capacity");
                            min_age_limit=(sessionArray.getJSONObject(j)).getInt("min_age_limit");
                            if (available_capacity>0&&min_age_limit==45)
                            {
                                district+=", "+available_capacity+" available";
                                isAvailable=true;
                            }
                        }

                        VaccineCenter vaccineCenter=new VaccineCenter(name,district,isAvailable);
                        vcenter.add(vaccineCenter);
                    }
                    VaccineAdapter arrayAdapter=new VaccineAdapter();
                    recyclerView =findViewById(R.id.listview);
                    recyclerView.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomListViewActivity.this, "INVALID PIN! TRY AGAIN", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(CustomListViewActivity.this);
        requestQueue.add(request);
    }
    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return vcenter.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.custom_row_layout,null);
            TextView textTitle=convertView.findViewById(R.id.txtTitle);
            textTitle.setText(vcenter.get(position).getName());
            TextView textDescription=convertView.findViewById(R.id.txtDescription);
            textDescription.setText(vcenter.get(position).getDistrictName());
            ImageView imageView=convertView.findViewById(R.id.imageView);
            if (vcenter.get(position).getAvailable())
            {
                imageView.setImageResource(R.drawable.vaccinetrue);
            }else
            { imageView.setImageResource(R.drawable.vaccine);
            }
            return convertView;
        }
    }
   public class VaccineAdapter extends RecyclerView.Adapter< VaccineAdapter.VaccineRowHolder>
    {
        @NotNull
        @NonNull
        @Override
        public VaccineAdapter.VaccineRowHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView=getLayoutInflater().inflate(R.layout.custom_row_layout,null);
            return new VaccineRowHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull VaccineAdapter.VaccineRowHolder holder, int position) {
            holder.textTitle.setText(vcenter.get(position).getName());
            holder.textDescription.setText(vcenter.get(position).getDistrictName());
            if (vcenter.get(position).getAvailable())
            {
                holder.imageView.setImageResource(R.drawable.vaccinetrue);
            }else
            { holder.imageView.setImageResource(R.drawable.vaccine);
            }
        }

        @Override
        public int getItemCount() {
            return vcenter.size();
        }
        public class VaccineRowHolder extends RecyclerView.ViewHolder
        {    TextView textTitle,textDescription;
             ImageView imageView;

            public VaccineRowHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                textTitle=itemView.findViewById(R.id.txtTitle);
                textDescription=itemView.findViewById(R.id.txtDescription);
                imageView=itemView.findViewById(R.id.imageView);
            }
        }
    }
}