package com.mmadapps.practiseproject.Flight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mmadapps.practiseproject.Flight.beans.flightdetails;
import com.mmadapps.practiseproject.R;
import com.mmadapps.practiseproject.Utils.Helper;

import java.sql.SQLException;
import java.util.ArrayList;

public class listFlight extends AppCompatActivity {

    ListView list;
    ArrayList<flightdetails> flight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_flight);
        initializeView();
    }

    private void initializeView() {
        list= (ListView) findViewById(R.id.listView);
        flight=new ArrayList<>();
        Helper helper=new Helper(getApplicationContext());
        try {
            helper.openDataBase();
            flight=helper.getLanguageList();
            helper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        setVaslues();

    }

    private void setVaslues() {
        list.setAdapter(new myAdapter());
    }

    public class myAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(flight==null||flight.size()==0)
                return  0;
            return flight.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.layout,parent,false);
            }
            TextView txt1= (TextView) convertView.findViewById(R.id.textView5);
            TextView txt2= (TextView) convertView.findViewById(R.id.textView6);
            TextView txt3= (TextView) convertView.findViewById(R.id.textView7);
            TextView txt4= (TextView) convertView.findViewById(R.id.textView8);
            TextView txt5= (TextView) convertView.findViewById(R.id.textView9);
            TextView txt6= (TextView) convertView.findViewById(R.id.textView10);

            txt1.setText(flight.get(position).getMcarrierFsCode());
            txt2.setText(flight.get(position).getMflightNumber());
            txt3.setText(flight.get(position).getMdepartureAirportFsCode());
            txt4.setText(flight.get(position).getMarrivalAirportFsCode());
            txt5.setText(flight.get(position).getMdepartureTime());
            txt6.setText(flight.get(position).getMarrivalTime());


            return convertView;
        }
    }
}
