package com.jusco;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportsSelection extends AppCompatActivity {
    ListView listView ;
    uploadRec rec;
    Integer pctosbm_count=0;
    Integer sbmtopc_count=0;
    Integer uploaded_count=0;
    Integer notUploaded_count=0;
    SQLiteDatabase db;
    public static String DBNAME="JUSCODB";




//    "0-OK MR",//0
//            "1-DoorLock",//1
//            "2-Defective Device",//2
//            "3-Abnormal Reading",//3
//            "4-Meter Missing",//4
//            "5-No Response",//5
//            "6-Recheck Required",//6
//            "7-Meter Tampered",//7
//            "9-Disconnected Reading"};//8


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_selection);

        DBNAME=getString(R.string.DBNAME);
        listView = (ListView) findViewById(R.id.list);
        String[] values =new String[10];


        values = new String[]{"               " + getResources().getString(R.string.UnitStatus).toString(),
                "               " + getResources().getString(R.string.DailyReport).toString(),
                "               " + getResources().getString(R.string.TotalReport).toString(),
                "               " + getResources().getString(R.string.LoadWiseReport).toString(),
                "               " + getResources().getString(R.string.UnbilledCons).toString()

//                "Android Example",
//                "List View Source Code",
//                "List View Array Adapter",
//                "Android Example List View"
        };



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values){
            @Override
            public View getView ( int position, View convertView, ViewGroup parent){
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


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
//      Toast.makeText(getApplicationContext(),
//      "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
//      .show();
                switch(itemPosition)
                {
                    case 0:
                        TotR(view);
                        Toast.makeText(getApplicationContext(),pctosbm_count.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),sbmtopc_count.toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Daywise_report(view);
                        break;
                    case 2:
                        Tot_cat_report(view);
                        break;
                    case 3:
//                        Loadwise_report(view);
                        break;
                    case 4:
                        Unbilled_Cons(view);
                        break;

                    default:
                        break;
                }
            }

        });

    }
    public void TotR(View view)
    {
        pctosbm_count= read_pctosbm_Count();
        sbmtopc_count=read_sbmtopc_Count();
        uploaded_count=uploaded_sbmtopc_Count();
        notUploaded_count=not_uploaded_sbmtopc_Count();

        Intent Reports=new Intent(this,Reports.class);
        Reports.putExtra("lbl1", getResources().getString(R.string.TotalRecords).toString());
        Reports.putExtra("lbl2", pctosbm_count.toString());
        Reports.putExtra("lbl3", getResources().getString(R.string.BilledRecords).toString());
        Reports.putExtra("lbl4", sbmtopc_count.toString());
        Reports.putExtra("lbl5", "Uploaded Records");
        Reports.putExtra("lbl6", uploaded_count.toString());
        Reports.putExtra("lbl7", "Not Uploaded Records");
        Reports.putExtra("lbl8", notUploaded_count.toString());

        startActivity(Reports);
    }
    public void Daywise_report(View view)
    {
        String report;
        report=Daywise_sbmtopc_Count();
        String[] BilledRec= StringUtils.split(report, ",");
        Intent Reports=new Intent(this,Reports.class);


        Reports.putExtra("lbl1", getResources().getString(R.string.MTRStatus).toString());
        Reports.putExtra("lbl2", BilledRec[0]);

        Reports.putExtra("lbl3", getResources().getString(R.string.IDFStatus).toString());
        Reports.putExtra("lbl4", BilledRec[1]);

        Reports.putExtra("lbl7", getResources().getString(R.string.ADFStatus).toString());
        Reports.putExtra("lbl8", BilledRec[3]);

        Reports.putExtra("lbl5", getResources().getString(R.string.RDFStatus).toString());
        Reports.putExtra("lbl6", BilledRec[2]);

        Reports.putExtra("lbl9", getResources().getString(R.string.ASUStatus).toString());
        Reports.putExtra("lbl10", BilledRec[4]);

        Reports.putExtra("lbl11", getResources().getString(R.string.NMStatus).toString());
        Reports.putExtra("lbl12", BilledRec[5]);

        Reports.putExtra("lbl13", getResources().getString(R.string.MCStatus).toString());
        Reports.putExtra("lbl14", BilledRec[6]);

        Reports.putExtra("lbl15", getResources().getString(R.string.MINStatus).toString());
        Reports.putExtra("lbl16", BilledRec[7]);

        Reports.putExtra("lbl17", getResources().getString(R.string.DiscRead).toString());
        Reports.putExtra("lbl18", BilledRec[8]);


        startActivity(Reports);
    }

    public void Tot_cat_report(View view)
    {
        String report;
        report=Tot_sbmtopc_Count();
        String[] BilledRec= StringUtils.split(report, ",");
        Intent Reports=new Intent(this,Reports.class);


        Reports.putExtra("lbl1", getResources().getString(R.string.MTRStatus).toString());
        Reports.putExtra("lbl2", BilledRec[0]);

        Reports.putExtra("lbl3", getResources().getString(R.string.IDFStatus).toString());
        Reports.putExtra("lbl4", BilledRec[1]);

        Reports.putExtra("lbl7", getResources().getString(R.string.ADFStatus).toString());
        Reports.putExtra("lbl8", BilledRec[3]);

        Reports.putExtra("lbl5", getResources().getString(R.string.RDFStatus).toString());
        Reports.putExtra("lbl6", BilledRec[2]);

        Reports.putExtra("lbl9", getResources().getString(R.string.ASUStatus).toString());
        Reports.putExtra("lbl10", BilledRec[4]);

        Reports.putExtra("lbl11", getResources().getString(R.string.NMStatus).toString());
        Reports.putExtra("lbl12", BilledRec[5]);

        Reports.putExtra("lbl13", getResources().getString(R.string.MCStatus).toString());
        Reports.putExtra("lbl14", BilledRec[6]);

        Reports.putExtra("lbl15", getResources().getString(R.string.MINStatus).toString());
        Reports.putExtra("lbl16", BilledRec[7]);

        Reports.putExtra("lbl17", getResources().getString(R.string.DiscRead).toString());
        Reports.putExtra("lbl18", BilledRec[8]);

        startActivity(Reports);
    }
    public Integer read_pctosbm_Count()
    {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        Integer count=0;
        Cursor c;
        db=openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);
        c=db.rawQuery("select count(*) from pctosbm",null);
        if(c.moveToFirst())
        {
            count=Integer.parseInt(c.getString(0));
        }
//        try {
//            File myFile = new File("/sdcard/pctosbm.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null) {
//                //aBuffer += aDataRow + "\n";
//                count=count+1;
//            }
//            myReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return count;
    }
    public Integer read_sbmtopc_Count()
    {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        Integer count=0;
        Cursor c;
        db=openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);
        c=db.rawQuery("select count(*) from sbmtopc",null);
        if(c.moveToFirst())
        {
            count=Integer.parseInt(c.getString(0));
        }
        else
            count=0;

//        try {
//            File myFile = new File("/sdcard/DVVNL/Output/sbmtopc.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null)
//            {
//                count=count+1;
//            }
//            myReader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return count;
    }

    public Integer uploaded_sbmtopc_Count()
    {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        Integer count=0;
        Cursor c;
        db=openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);
        c=db.rawQuery("select count(*) from sbmtopc where Online_Flag_number='1'",null);
        if(c.moveToFirst())
        {
            count=Integer.parseInt(c.getString(0));
        }
        else
            count=0;

//        try {
//            File myFile = new File("/sdcard/DVVNL/Output/sbmtopc.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null)
//            {
//                count=count+1;
//            }
//            myReader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return count;
    }

    public Integer not_uploaded_sbmtopc_Count()
    {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        Integer count=0;
        Cursor c;
        db=openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);
        c=db.rawQuery("select count(*) from sbmtopc where Online_Flag_number='0'",null);
        if(c.moveToFirst())
        {
            count=Integer.parseInt(c.getString(0));
        }
        else
            count=0;

//        try {
//            File myFile = new File("/sdcard/DVVNL/Output/sbmtopc.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null)
//            {
//                count=count+1;
//            }
//            myReader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return count;
    }

    public String Daywise_sbmtopc_Count()
    {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        String ret="";
        Cursor c;
        Integer count1,count2,count3,count4,count5,count6,count7,count8,count9;
        count1=count2=count3=count4=count5=count6=count7=count8=0;
        count9=0;
        db=openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);
//    "0-OK MR",//0
//            "1-DoorLock",//1
//            "2-Defective Device",//2
//            "3-Abnormal Reading",//3
//            "4-Meter Missing",//4
//            "5-No Response",//5
//            "6-Recheck Required",//6
//            "7-Meter Tampered",//7
//            "9-Disconnected Reading"};//8

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date());
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='0' or mrnote-'OK MR'",null);
        if (c.moveToFirst())
        {
            count1=Integer.parseInt(c.getString(0));
        }
        else
            count1=0;

        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='1' or mrnote='DOORLOCK'",null);
        if (c.moveToFirst())
        {
            count2=Integer.parseInt(c.getString(0));
        }
        else
            count2=0;
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='2' or mrnote='Defective Device'",null);
        if (c.moveToFirst())
        {
            count3=Integer.parseInt(c.getString(0));
        }
        else
            count3=0;
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='3' or mrnote='Abnormal Reading'",null);
        if (c.moveToFirst())
        {
            count4=Integer.parseInt(c.getString(0));
        }
        else
            count4=0;
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='4' or mrnote='Meter Missing'",null);
        if (c.moveToFirst())
        {
            count5=Integer.parseInt(c.getString(0));
        }
        else
            count5=0;
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='5' or mrnote='No Response'",null);
        if (c.moveToFirst())
        {
            count6=Integer.parseInt(c.getString(0));
        }
        else
            count6=0;
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='6' or mrnote='Recheck Required'",null);
        if (c.moveToFirst())
        {
            count7=Integer.parseInt(c.getString(0));
        }
        else
            count7=0;
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='7' or mrnote='Meter Tampered'",null);
        if (c.moveToFirst())
        {
            count8=Integer.parseInt(c.getString(0));
        }
        else
            count8=0;
        c= db.rawQuery("select count(*) from sbmtopc where Bill_date='" + date +"' and mrnote='9' or mrnote='Disconnected Reading'",null);
        if (c.moveToFirst())
        {
            count9=Integer.parseInt(c.getString(0));
        }
        else
            count9=0;
//        try {
//
//            File myFile = new File("/sdcard/DVVNL/Output/sbmtopc.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null)
//            {
//                String[] BilledRec= StringUtils.split(aDataRow, "$");
//                if(BilledRec[3].equals(date))
//                {
//                    if(BilledRec[12].equals("1"))
//                    {
//                        count1=count1+1;
//                    }
//                    if(BilledRec[12].equals("2"))
//                    {
//                        count2=count2+1;
//                    }
//                    if(BilledRec[12].equals("4"))
//                    {
//                        count7=count7+1;
//                    }
//                    if(BilledRec[12].equals("6"))
//                    {
//                        count3=count3+1;
//                    }
//                    if(BilledRec[12].equals("9"))
//                    {
//                        count4=count4+1;
//                    }
//                    if(BilledRec[12].equals("14"))
//                    {
//                        count8=count8+1;
//                    }
//                    if(BilledRec[12].equals("15"))
//                    {
//                        count6=count6+1;
//                    }
//                    if(BilledRec[12].equals("16"))
//                    {
//                        count5=count5+1;
//                    }
//
//                }
//            }
//            myReader.close();
//            ret=count1.toString()+","+count2.toString()+","+count3.toString()+","+count4.toString()+","+count5.toString()+","+count6.toString()+","+count7.toString()+","+count8.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ret=count1.toString()+","+count2.toString()+","+count3.toString()+","+count4.toString()+","+count5.toString()+","+count6.toString()+","+count7.toString()+","+count8.toString()+","+count9.toString();
        return ret;
    }

    public String Tot_sbmtopc_Count()
    {

        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        String ret="";
        Integer count1,count2,count3,count4,count5,count6,count7,count8,count9;
        count1=count2=count3=count4=count5=count6=count7=count8=count9=0;
        Cursor c;
        db=openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);

        c=db.rawQuery("select count(*) from sbmtopc where mrnote='0' or mrnote='OK MR'",null);
        if (c.moveToFirst())
        {
            count1=Integer.parseInt(c.getString(0));
        }
        else
            count1=0;


        c=db.rawQuery("select count(*) from sbmtopc where mrnote='1' or mrnote='DoorLock' ",null);
        if (c.moveToFirst())
        {
            count2=Integer.parseInt(c.getString(0));
        }
        else
            count2=0;

        c=db.rawQuery("select count(*) from sbmtopc where mrnote='2' or mrnote='Defective Device'",null);
        if (c.moveToFirst())
        {
            count3=Integer.parseInt(c.getString(0));
        }
        else
            count3=0;
        c=db.rawQuery("select count(*) from sbmtopc where mrnote='3' or mrnote='Abnormal Reading'",null);
        if (c.moveToFirst())
        {
            count4=Integer.parseInt(c.getString(0));
        }
        else
            count4=0;

        c=db.rawQuery("select count(*) from sbmtopc where mrnote='4' or mrnote='Meter Missing'",null);
        if (c.moveToFirst())
        {
            count5=Integer.parseInt(c.getString(0));
        }
        else
            count5=0;

        c=db.rawQuery("select count(*) from sbmtopc where mrnote='5' or mrnote='No Response'",null);
        if (c.moveToFirst())
        {
            count6=Integer.parseInt(c.getString(0));
        }
        else
            count6=0;

        c=db.rawQuery("select count(*) from sbmtopc where mrnote='6' or mrnote='Recheck Required'",null);
        if (c.moveToFirst())
        {
            count7=Integer.parseInt(c.getString(0));
        }
        else
            count7=0;

        c=db.rawQuery("select count(*) from sbmtopc where mrnote='7' or mrnote='Meter Tampered'",null);
        if (c.moveToFirst())
        {
            count8=Integer.parseInt(c.getString(0));
        }
        else
            count8=0;

        c=db.rawQuery("select count(*) from sbmtopc where mrnote='9' or mrnote='Disconnected Reading'",null);
        if (c.moveToFirst())
        {
            count9=Integer.parseInt(c.getString(0));
        }
        else
            count9=0;


//        try {
//            File myFile = new File("/sdcard/DVVNL/Output/sbmtopc.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null)
//            {
//                String[] BilledRec= StringUtils.split(aDataRow, "$");
//                    if(BilledRec[12].equals("1"))
//                    {
//                        count1=count1+1;
//                    }
//                    if(BilledRec[12].equals("2"))
//                    {
//                        count2=count2+1;
//                    }
//                    if(BilledRec[12].equals("4"))
//                    {
//                        count7=count7+1;
//                    }
//                    if(BilledRec[12].equals("6"))
//                    {
//                        count3=count3+1;
//                    }
//                    if(BilledRec[12].equals("9"))
//                    {
//                        count4=count4+1;
//                    }
//                    if(BilledRec[12].equals("14"))
//                    {
//                        count8=count8+1;
//                    }
//                    if(BilledRec[12].equals("15"))
//                    {
//                        count6=count6+1;
//                    }
//                    if(BilledRec[12].equals("16"))
//                    {
//                        count5=count5+1;
//                    }
//
//
//            }
//            myReader.close();
//            ret=count1.toString()+","+count2.toString()+","+count3.toString()+","+count4.toString()+","+count5.toString()+","+count6.toString()+","+count7.toString()+","+count8.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ret=count1.toString()+","+count2.toString()+","+count3.toString()+","+count4.toString()+","+count5.toString()+","+count6.toString()+","+count7.toString()+","+count8.toString()+","+count9.toString();
        return ret;
    }

    public void Unbilled_Cons(View view)
    {
//        Intent unB=new Intent(this,Unbilled_Rep.class);
        Intent unB=new Intent(this,UnbilledRep2.class);
        startActivity(unB);

    }
    public void Loadwise_report(View view)
    {
        /*String report;

        report=Loadwise_sbmtopc_Count();
        String[] BilledRec= StringUtils.split(report, ",");
        Intent Reports=new Intent(this,Reports.class);


        Reports.putExtra("lbl1", getResources().getString(R.string.Load1_5).toString());
        Reports.putExtra("lbl2", BilledRec[0]);

        Reports.putExtra("lbl3", getResources().getString(R.string.Load5_10).toString());
        Reports.putExtra("lbl4", BilledRec[1]);

        Reports.putExtra("lbl5", getResources().getString(R.string.Above10).toString());
        Reports.putExtra("lbl6", BilledRec[2]);*/

//        Reports.putExtra("lbl5","RDF" );
//        Reports.putExtra("lbl6", BilledRec[2]);
//
//        Reports.putExtra("lbl9","ASU" );
//        Reports.putExtra("lbl10", BilledRec[4]);
//
//        Reports.putExtra("lbl11","NM" );
//        Reports.putExtra("lbl12", BilledRec[5]);
//
//        Reports.putExtra("lbl13","MC" );
//        Reports.putExtra("lbl14", BilledRec[6]);
//
//        Reports.putExtra("lbl15","MIN" );
//        Reports.putExtra("lbl16", BilledRec[7]);

//        startActivity(Reports);
    }

    public String Loadwise_sbmtopc_Count()
    {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        String ret="";
        Integer count1,count2,count3,count4,count5,count6,count7,count8;
        count1=count2=count3=count4=count5=count6=count7=count8=0;
        Cursor c;
        db=openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);

        c=db.rawQuery("select count(*) from sbmtopc where sload<5",null);
        if(c.moveToFirst())
        {
            count1=Integer.parseInt(c.getString(0));
        }
        else
        {
            count1=0;
        }

        c=db.rawQuery("select count(*) from sbmtopc where sload>=5 and sload<10",null);
        if(c.moveToFirst())
        {
            count2=Integer.parseInt(c.getString(0));
        }
        else
        {
            count2=0;
        }
        c=db.rawQuery("select count(*) from sbmtopc where sload=10 and sload>10",null);
        if(c.moveToFirst())
        {
            count3=Integer.parseInt(c.getString(0));
        }
        else
        {
            count3=0;
        }

//        try {
//            File myFile = new File("/sdcard/DVVNL/Output/sbmtopc.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null)
//            {
//                String[] BilledRec= StringUtils.split(aDataRow, "$");
//                Double Sancload=Double.parseDouble(BilledRec[8]);
//                if(Sancload>=1 && Sancload<5)
//                {
////                    count1=count1+1;
//                }
//                if(Sancload>=5 && Sancload<10)
//                {
//                    count2=count2+1;
//                }
//                if(Sancload>=10)
//                {
//                    count3=count3+1;
//                }
//            }
//            myReader.close();
//            ret=count1.toString()+","+count2.toString()+","+count3.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ret=count1.toString()+","+count2.toString()+","+count3.toString();
        return ret;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mrAct=new Intent(this,MRActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mrAct);
    }
}
