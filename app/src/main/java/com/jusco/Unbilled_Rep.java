package com.jusco;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Unbilled_Rep extends AppCompatActivity {

    ListView lstunB;
    List<String[]> records;
    String[] fetch_fieldnote_title;
    String[] fetch_fieldnote_id;
    SQLiteDatabase db;
    public static String[] Listrecords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbilled__rep);
        lstunB=findViewById(R.id.listUnB);
        DBConnections dbConnections=new DBConnections(this);
        records=dbConnections.getSelectionRecords2();
        fetch_fieldnote_title = new String[records.size()];
        fetch_fieldnote_id = new String[records.size()];
        Log.e("RecSize",String.valueOf(records.size()));
        int x=0;
        String assigning_fetch_fieldnote_title;
        String assigning_fetch_fieldnote_id;
        for (String[] name : records) {

            assigning_fetch_fieldnote_title = name[0] + "   " + name[1]+ "   " + name[2];
            assigning_fetch_fieldnote_id = name[1];
            fetch_fieldnote_title[x] = assigning_fetch_fieldnote_title;
            fetch_fieldnote_id[x] = assigning_fetch_fieldnote_id;
            x++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, fetch_fieldnote_title){
            @Override
            public View getView (int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                if (position % 2 == 1) {
                    // Set a background color for ListView regular row/item
//                    view.setBackgroundColor(Color.parseColor("#EAF2F8"));
                } else {
                    // Set the background color for alternate row/item
//                    view.setBackgroundColor(Color.parseColor("#F9EBEA"));
                }
                return view;
            }

        };
       /* View headerView = getLayoutInflater().inflate(R.layout.listview_header, null);
        lstunB.addHeaderView(headerView);
        lstunB.setHeaderDividersEnabled(true);*/

// Assign adapter to ListView
        lstunB.setAdapter(adapter);

        //SELECT pc.BP_Number,pc.Name_of_Consumer FROM pctosbm pc LEFT JOIN sbmtopc sb ON pc.BP_Number=sb.BP_Number WHERE sb.BP_Number IS NULL
    }
}
