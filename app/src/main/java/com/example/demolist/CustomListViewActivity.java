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

public class CustomListViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<VaccineCenter>vcenter;
    String pin, date;
    SharedPreferences sharedPreferences;
    AutoCompleteTextView searchInput;
    VaccineAdapter vaccineAdapter;
  //  ArrayList<String> centerList,districtList;
   // ArrayList<Boolean>availableList;
  //  String title[]={"Name1","Name2","Name3","Name4","Name5","Name6"};
//String description[]={"Cricketer","Singer","App Developer","Software developer","Game developer","Soldier"};
//int imagelist[]={R.drawable.banana,R.drawable.grapes,R.drawable.lemon,R.drawable.strawberry,R.drawable.orange};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view);
        vaccineAdapter=new VaccineAdapter(this);
        vcenter=new ArrayList<VaccineCenter>();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);//700103
        pin=sharedPreferences.getString("pinShared","Pincode");
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
                                                   ArrayList<VaccineCenter>tempList=new ArrayList<VaccineCenter>() ;
                                                   for (VaccineCenter vc:vcenter)
                                                   {
                                                       if (vc.getName().toLowerCase().contains(searchedTerm.toLowerCase())||vc.getVaccineType().toLowerCase().contains(searchedTerm.toLowerCase()))
                                                       {
                                                           tempList.add(vc);
                                                       }
                                                   }
                                                       vaccineAdapter.updateList(tempList);
                                               }
                                           });
                recyclerView = findViewById(R.id.listview);
        recyclerView.setAdapter(vaccineAdapter);
          recyclerView.setLayoutManager(new LinearLayoutManager(CustomListViewActivity.this));

        String urlString = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pin+"&date="+date;
        StringRequest request = new StringRequest(Request.Method.GET, urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CustomListViewActivity.this, "URL hit successfully", Toast.LENGTH_SHORT).show();
                try {
                    String name="", district="",vaccineType="",address="",fee_type="";
                    JSONObject parentObj = new JSONObject(response);
                    JSONArray centers = parentObj.getJSONArray("sessions");
                    JSONObject jsonObject;

                    boolean isAvailable=false;
                    int available_capacity=0,min_age_limit=0,max_age_limit=0,dose1=0,dose2=0;

                    getSupportActionBar().setTitle("Centers Found:"+centers.length());
                    for (int i = 0; i < centers.length(); i++) {
                        dose1=0;
                        dose2=0;
                        ArrayList<String>slotList=new ArrayList<String>();
                        max_age_limit=0;
                        jsonObject = centers.getJSONObject(i);
                         JSONArray slot=jsonObject.getJSONArray("slots");
                         for (int j=0;j<slot.length();j++)
                         {
                             slotList.add(slot.getString(j));
                         }
                        name = jsonObject.getString("name");
                        district = jsonObject.getString("district_name");
                        address=jsonObject.getString("address");
                        fee_type=jsonObject.getString("fee_type");
                        isAvailable = false;
                        available_capacity = jsonObject.getInt("available_capacity");
                        min_age_limit = jsonObject.getInt("min_age_limit");
                        if (min_age_limit!=45)
                          max_age_limit=jsonObject.getInt("max_age_limit");
                        vaccineType = jsonObject.getString("vaccine");
                        dose1=jsonObject.getInt("available_capacity_dose1");
                        dose2=jsonObject.getInt("available_capacity_dose2");

                        if (available_capacity > 0)
                        {
                            isAvailable = true;
                        }


                        VaccineCenter vaccineCenter = new VaccineCenter(name, district,address, vaccineType,fee_type, min_age_limit,max_age_limit, available_capacity,dose1,dose2, isAvailable,slotList);
                        vcenter.add(vaccineCenter);
                    }
                    vaccineAdapter.updateList(vcenter);
                    } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomListViewActivity.this, "INVALID PIN! TRY AGAIN", Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(CustomListViewActivity.this);
        requestQueue.add(request);
    }
  /*  private class CustomAdapter extends BaseAdapter {

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
    }*/
  public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.ViewHolder>
    { ArrayList<VaccineCenter>vcenterList=new ArrayList<VaccineCenter>();
        Context context;
        public class ViewHolder extends RecyclerView.ViewHolder
        {    TextView textTitle,textDescription;
            ImageView imageView;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                textTitle=itemView.findViewById(R.id.txtTitle);
                textDescription=itemView.findViewById(R.id.txtDescription);
                imageView=itemView.findViewById(R.id.imageView);
            }
        }
        public VaccineAdapter(Context context) {
            this.context=context;
        }
        public void updateList(ArrayList<VaccineCenter>list)
        {
            vcenterList=list;
            notifyDataSetChanged();
        }


        @NotNull
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(context).inflate(R.layout.custom_row_layout,null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            String slotstr="";
            String district=vcenterList.get(position).getDistrictName();
            String vaccinetype=vcenterList.get(position).getVaccineType();
            String address=vcenterList.get(position).getAddress();
            String feetype=vcenterList.get(position).getFee_type();
            int minAge=vcenterList.get(position).getMin_age_limit();
            int maxAge=vcenterList.get(position).getMax_age_limit();
            int capacity=vcenterList.get(position).getAvailable_capacity();
           int dose1=vcenterList.get(position).getDose1();
            int dose2=vcenterList.get(position).getDose2();
            for (String str:vcenterList.get(position).slot)
                   slotstr=slotstr+str+"\n\t\t    ";
            String details="";
                if(maxAge==0) {
                     details = "District:" + district + "\n"
                             + "Address:" + address + "\n"
                            + "Vaccine:" + vaccinetype + "\n"
                             + "Fee Type:" + feetype + "\n"
                            + "Available:" + capacity + "\n"
                            + "1st dose:" + dose1 + "\n"
                            + "2nd dose:" + dose2 + "\n"
                            + "Min Age:" + minAge + "\n"
                             +"Slots" + slotstr;
                }
                else
                {
                    details = "District:" + district + "\n"
                            + "Address:" + address + "\n"
                            + "Vaccine:" + vaccinetype + "\n"
                            + "Fee Type:" + feetype + "\n"
                            + "Available:" + capacity + "\n"
                            + "1st dose:" + dose1 + "\n"
                            + "2nd dose:" + dose2 + "\n"
                            + "Min Age:" + minAge + "\n"
                            + "Max Age:"+maxAge+"\n"
                            + "Slots:" +slotstr;
                }
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

    }
}