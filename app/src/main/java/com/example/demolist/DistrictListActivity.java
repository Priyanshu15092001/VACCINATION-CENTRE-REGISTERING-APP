package com.example.demolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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

public class DistrictListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DistrictVaccineCenter> vcenter;
    String districtID, date;
    SharedPreferences sharedPreferences;
    AutoCompleteTextView searchInput;
    DistrictVaccineAdapter districtVaccineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_list);
        districtVaccineAdapter =new DistrictVaccineAdapter(this);
        vcenter=new ArrayList<DistrictVaccineCenter>();
      sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        districtID=sharedPreferences.getString("districtId","DistrictID");
        date=sharedPreferences.getString("dateShared","Date");
        searchInput=findViewById(R.id.search_bar);
        searchInput.setThreshold(1);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchedTerm=s.toString();
                ArrayList<DistrictVaccineCenter>tempList=new ArrayList<DistrictVaccineCenter>() ;
                for (DistrictVaccineCenter vc:vcenter)
                {
                    if (vc.getName().toLowerCase().contains(searchedTerm.toLowerCase())||vc.getVaccineType().toLowerCase().contains(searchedTerm.toLowerCase()))
                    {
                        tempList.add(vc);
                    }
                }
                districtVaccineAdapter.updateList(tempList);

            }
        });
        recyclerView=findViewById(R.id.districtlistview);
        recyclerView.setAdapter(districtVaccineAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DistrictListActivity.this));
        String urlString="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id="+districtID+"&date="+date;
        StringRequest request = new StringRequest(Request.Method.GET, urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DistrictListActivity.this, "URL hit successfully", Toast.LENGTH_SHORT).show();
                try {
                    String name = "", address = "", vaccineType = "";
                    JSONObject parentObj = new JSONObject(response);
                    JSONArray centers = parentObj.getJSONArray("sessions");
                    JSONObject jsonObject;
                    boolean isAvailable = false;
                    int available_capacity = 0, min_age_limit = 0, dose1 = 0, dose2 = 0;
                    Boolean allow_all_age=false;
                    getSupportActionBar().setTitle("Centers Found:" + centers.length());
                    for (int i = 0; i < centers.length(); i++) {
                        dose1 = 0;
                        dose2 = 0;
                        ArrayList<String> slotList = new ArrayList<String>();
                        jsonObject = centers.getJSONObject(i);
                        JSONArray slot = jsonObject.getJSONArray("slots");
                        for (int j = 0; j < slot.length(); j++) {
                            slotList.add(slot.getString(j));
                        }
                        name = jsonObject.getString("name");
                        address = jsonObject.getString("address");
                        isAvailable = false;
                        available_capacity = jsonObject.getInt("available_capacity");
                        min_age_limit = jsonObject.getInt("min_age_limit");
                            allow_all_age = jsonObject.getBoolean("allow_all_age");
                        vaccineType = jsonObject.getString("vaccine");
                        dose1 = jsonObject.getInt("available_capacity_dose1");
                        dose2 = jsonObject.getInt("available_capacity_dose2");

                        if (available_capacity > 0) {
                            isAvailable = true;
                        }


                        DistrictVaccineCenter vaccineCenter = new DistrictVaccineCenter(name, address, vaccineType, min_age_limit, allow_all_age, available_capacity, dose1, dose2, isAvailable, slotList);
                        vcenter.add(vaccineCenter);
                    }
                    districtVaccineAdapter.updateList(vcenter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DistrictListActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(DistrictListActivity.this);
        requestQueue.add(request);
    }

    public class DistrictVaccineAdapter extends RecyclerView.Adapter<DistrictVaccineAdapter.ViewHolder>{
        ArrayList<DistrictVaccineCenter>vcenterList=new ArrayList<DistrictVaccineCenter>();
        Context context;
        public DistrictVaccineAdapter(Context context) {
            this.context=context;
        }
        public void updateList(ArrayList<DistrictVaccineCenter>list)
        {
            vcenterList=list;
            notifyDataSetChanged();
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(context).inflate(R.layout.customdistric_row_layout,null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull DistrictListActivity.DistrictVaccineAdapter.ViewHolder holder, int position) {
            String slotstr="";
            String address=vcenterList.get(position).getAddress();
            String vaccinetype=vcenterList.get(position).getVaccineType();
            int minAge=vcenterList.get(position).getMin_age_limit();
            Boolean allow_all_age=vcenterList.get(position).getAllow_all_age();
            int capacity=vcenterList.get(position).getAvailable_capacity();
            int dose1=vcenterList.get(position).getDose1();
            int dose2=vcenterList.get(position).getDose2();
            for (String str:vcenterList.get(position).slot)
                slotstr=slotstr+str+"\n\t\t    ";
            String details="";

                details = "Address:" + address + "\n"
                        + "Vaccine:" + vaccinetype + "\n"
                        + "Available:" + capacity + "\n"
                        + "1st dose:" + dose1 + "\n"
                        + "2nd dose:" + dose2 + "\n"
                        + "Min Age:" + minAge + "\n"
                        + "Allow all age:"+ allow_all_age + "\n"
                        +"Slots" + slotstr;


            holder.textTitle.setText(vcenterList.get(position).getName());
            holder.textDescription.setText(details);
            if (vcenterList.get(position).getAvailable())
            {
                holder.imageView.setImageResource(R.drawable.vaccinetrue);
            }else
            { holder.imageView.setImageResource(R.drawable.vaccine);
            }
        }

        @Override
        public int getItemCount() {
            return vcenterList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textTitle,textDescription;
            ImageView imageView;
            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                textTitle=itemView.findViewById(R.id.txtTitle);
                textDescription=itemView.findViewById(R.id.txtDescription);
                imageView=itemView.findViewById(R.id.imageView);
            }
        }
    }
}