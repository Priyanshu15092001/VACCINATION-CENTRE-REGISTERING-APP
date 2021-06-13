package com.example.demolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomListViewActivity extends AppCompatActivity {
    ListView listView;
    String title[]={"Name1","Name2","Name3","Name4","Name5","Name6"};
String description[]={"Cricketer","Singer","App Developer","Software developer","Game developer","Soldier"};
int imagelist[]={R.drawable.banana,R.drawable.grapes,R.drawable.lemon,R.drawable.strawberry,R.drawable.orange};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view);
        CustomAdapter arrayAdapter= new CustomAdapter();
        listView=findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
    }
    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.custom_row_layout,null);
            TextView textTitle=convertView.findViewById(R.id.txtTitle);
            textTitle.setText(title[position]);
            TextView textDescription=convertView.findViewById(R.id.txtDescription);
            textDescription.setText(description[position]);
            ImageView imageView=convertView.findViewById(R.id.imageView);
            imageView.setImageResource(imagelist[position%imagelist.length]);
            return convertView;
        }
    }
}