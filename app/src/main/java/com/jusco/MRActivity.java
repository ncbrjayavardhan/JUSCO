package com.jusco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import net.iharder.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class MRActivity extends AppCompatActivity {
    ListView listView;
    SQLiteDatabase db;
    private Uri outputFileUri;
    public static String SearchFlag = "";
    public static uploadRec reco;
    public static double latitude; // latitude
    public static double longitude;
    public static BilledRecords billedRecords;
    public static String BWfilename2;
    public static String BWfilename;
    public static String record;
    public static String List1;
    public static String List2;
    public static String List3;
    public static String List4;
    public static String List5;
    public static String List6;
    File EOD_File;
    String DBNAME="";

    // for data uploading

    String Str = "S";
    public static String res;
    //    String ServerIP = "183.82.128.143";
    String ServerIP = "124.123.41.255";
    String ServerPort = "25000";

    //***************


    private ProgressDialog progress;

    public static String DBP;
    int chkeod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr);
//        setContentView(R.layout.listview_mr);
        DBNAME=getResources().getString(R.string.DBNAME);
Button searchConsumer;
searchConsumer=findViewById(R.id.btnSearchScno);
        String[] listviewTitle = new String[]{
                getResources().getString(R.string.SScno), getResources().getString(R.string.Smtr), getResources().getString(R.string.Seq), getResources().getString(R.string.DupB), getResources().getString(R.string.Rep),getResources().getString(R.string.Du)};

        int[] listviewImage = new int[]{
                R.drawable.search_user, R.drawable.search_user,R.drawable.search_user, R.drawable.print_bill, R.drawable.reports,R.drawable.upload};


//        listView = (ListView) findViewById(R.id.list);
//        listView = (ListView) findViewById(R.id.list_view);

        reco = new uploadRec();


        String FilePath = Environment.getExternalStorageDirectory().getPath();
        EOD_File = new File(FilePath, "/jusco/Mob2PC/Login_FILE.txt");
        File f = new File(Environment.getExternalStorageDirectory().toString() + "/jusco/Mob2PC");
        f.mkdirs();
        File f2 = new File(Environment.getExternalStorageDirectory().toString() + "/jusco/PC2Mob");
        f2.mkdirs();
       /* try {
            setMobileNetworkfromLollipop(this);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 6; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title"};
        int[] to = {R.id.listview_image, R.id.listview_item_title};

        /*SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_img_mr, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list);
        androidListView.setAdapter(simpleAdapter);*/


/*        // Defined Array values to show in ListView
        String[] values=new String[10];


            values = new String[]{"          " +getResources().getString(R.string.SScno).toString(), //Search by Service No
                    "          " + getResources().getString(R.string.Smtr).toString(), //Search by Meter No
                    "          " + getResources().getString(R.string.Seq).toString(),
                    "          " + getResources().getString(R.string.DupB).toString(),//Duplicate Bill
                    "          " + getResources().getString(R.string.Rep).toString(),//Reports
                    "          " + getResources().getString(R.string.Du).toString() //Data Upload
//                "          No Consumer Entry",
//                "          Automatic Meter Reading"
//                "Android Example",
//                "List View Source Code",
//                "List View Array Adapter",
//                "Android Example List View"
            };



        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

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
        listView.setAdapter(adapter);*/

        // ListView Item Click Listener
     /*   androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
//                String itemValue = (String) androidListView.getItemAtPosition(position);

                // Show Alert
//      Toast.makeText(getApplicationContext(),
//      "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
//      .show();
                switch(itemPosition)
                {
                    case 0:
                        SearchFlag="S";
                        MR_Search_Scno(view);
                        break;
                    case 1:
                        SearchFlag="M";
//                        MR_Search_Mtr(view);
                        break;
                    case 2:
                        SearchFlag="S";
                        Search_Cons(view);
                        break;
                    case 3:
                        SearchFlag="D";
                        Issue_Duplicate_Bill(view);
                        break;
                    case 4:
                        Reports_Selection(view);
                        break;
                    case 5:
                        Data_copy(view);
                        break;
                    case 6:
                        No_Scno(view);
                        break;
                    case 7:
                        SearchFlag="A";
                        Automatic_Meter_Reading(view);
                    default:
                        break;
                }
            }

        });
*/
    }



    public void write_eod(String dt)
    {
        File eodfile;
        String FilePath = Environment.getExternalStorageDirectory().getPath();
        eodfile = new File(FilePath, "/jusco/Mob2PC/Login_FILE.txt");
        if (isExternalStorageWritable()) {
            if (eodfile.exists()) {
                eodfile.delete();
                try {
                    FileOutputStream fOut = new FileOutputStream(eodfile, true);
                    try {
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        eodfile.createNewFile();
                        myOutWriter.write(dt);
                        myOutWriter.flush();
                        myOutWriter.close();
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {

                    }
                    //nodeCounter++;
                } catch (IOException e) {

                }
            } else {
                try {
                    eodfile.createNewFile();
                    FileOutputStream fOut2 = new FileOutputStream(eodfile, true);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut2);
                    myOutWriter.append(dt);
                    myOutWriter.close();
                    myOutWriter.flush();
                    fOut2.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            myFile.setExecutable(true,false);
        } else {
            Log.i("logGPSData", "Error");
        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public int checkEOD()
    {
        int check=-1;
        String aDataRow=null;
        try {
            File myFile = new File("/sdcard/jusco/Mob2PC/Login_FILE.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader2 = new BufferedReader(
                    new InputStreamReader(fIn));
            while ((aDataRow = myReader2.readLine()) != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String date = sdf.format(new Date());
                if(aDataRow.equals(date))
                {
                    check=1;
                }
                else
                {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//                    alertDialogBuilder.setMessage("EOD Backup to be done");
//                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
//
//                        }
//                    });

//            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
                    check=0;
                }
            }
            myReader2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return check;
    }

    public void MR_Search_Scno(View view){
        Intent consFetch=new Intent(this,ConsFetchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(consFetch);
    }

    public void MR_Search_Mtr(View view)
    {
//        Intent MtrFetch=new Intent(this,ScannedBarcodeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        Intent MtrFetch=new Intent(this,BarcodeReadingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        /*Intent MtrFetch=new Intent(this,MtrSearch.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(MtrFetch);*/
    }

    public void Issue_Duplicate_Bill(View view)
    {
       /* SearchFlag="D";
        Intent DupBill=new Intent(this,DuplicateBill.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(DupBill);*/
    }
    public void No_Scno(View view)
    {
       /* Intent SelectPrinter=new Intent(this,SelectPrinter.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(SelectPrinter);*/
//        Intent NoCosnumerEntry=new Intent(this,NoCosnumerEntry.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(NoCosnumerEntry);
    }

    public void Reports_Selection(View view)
    {
        Intent ReportsSelect=new Intent(MRActivity.this,ReportsSelection.class);
        startActivity(ReportsSelect);
    }

    public void Search_Cons(View view)
    {
        /*Intent Search_cons=new Intent(MRActivity.this,Search.class);
        startActivity(Search_cons);*/
    }

    public void Automatic_Meter_Reading(View view){

//
       /* Intent Automatic_Meter_Reading= new Intent(MRActivity.this,MtrSelect.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        Intent Automatic_Meter_Reading= new Intent(MRActivity.this,FragmentLayout.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Automatic_Meter_Reading);*/
    }

    public void Data_copy(View view) {


        Intent intent = new Intent(MRActivity.this, UploadActivity.class);
        startActivity(intent);

       /*upload_data_to_server();*/


//      ProgressDialog progress = new ProgressDialog(MRActivity.this);
//        progress.setMessage("UPLoading...Please Wait");
//        progress.setIndeterminate(false);
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//        new MRActivity.MyTask(progress).execute();

    }


    public class MyTask extends AsyncTask<Void, Void, Void> {

        public MyTask(ProgressDialog progress) {

            (MRActivity.this).progress = progress;
        }

        public void onPreExecute() {

            progress.show();
        }

        public Void doInBackground(Void... unused) {

            Datasend();
            return null;
        }

        public void onPostExecute(Void unused) {

            progress.dismiss();

           /* Intent intent = new Intent(MRActivity.this, UploadActivity.class);
            startActivity(intent);*/
        }
    }

    //Upload Data From Upload activity

    public void Datasend() {

        Cursor c = null;
        Cursor c1 = null;
        String scno;

        try {

            db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("select * from sbmtopc where UFLAG='0'", null);
            if (c.moveToFirst()) {
                scno = c.getString(0);
                do {
                    record = "";
                    record += c.getString(0) + "$" + c.getString(1) + "$" + "0" + "$" + c.getString(3);
                    record += "$" + c.getString(4) + "$" + c.getString(5) + "$" + c.getString(6) + "$" + c.getString(7) + "$" + c.getString(8);
                    record += "$" + c.getString(9) + "$" + c.getString(10) + "$" + c.getString(11) + "$" + c.getString(12);
                    record += "$" + c.getString(13) + "$" + c.getString(14) + "$" + c.getString(15) + "$" + c.getString(16) + "$" + "00" + "$" + c.getString(18) + "$" + c.getString(19);
                    record += "$" + c.getString(20) + "$" + "00" + "$" + c.getString(22) + "$" + "00" + "$" + c.getString(24);
                    record += "$" + "00" + "$" + c.getString(26) + "$" + c.getString(27) + "$" + c.getString(28) + "$" + c.getString(29);
                    record += "$" + c.getString(30) + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + c.getString(37) + "$" + "00";
                    record += "$" + "00000000" + "$" + c.getString(40) + "$" + c.getString(41) + "$" + c.getString(42) + "$" + "0000000000" + "$" + c.getString(44) + "$" + c.getString(45) + "$" + "00";
                    record += "$" + c.getString(47) + "$" + c.getString(48) + "$" + c.getString(49) + "$" + "00" + "$" + c.getString(51) + "$" + c.getString(52);
                    record += "$" + c.getString(53) + "$" + c.getString(54) + "$" + c.getString(55) + "$" + c.getString(56);
                    record += "$" + c.getString(57) + "$" + c.getString(59) + "$" + c.getString(60) + "$" + c.getString(61) + "$" + c.getString(62) + "$" + c.getString(63) + "$" + c.getString(64) + "$" + MainActivity.Version + "$" + c.getString(66) + "$" + c.getString(67);
//                    send_sock();
                    send_rdg_image(c.getString(60));
                    send_md_image(c.getString(61));

                    try{
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    Upload upload = new Upload(ServerIP, Integer.parseInt(ServerPort));
                    upload.execute();

                    try{
                        if(res.equals(Str)){

                            db.execSQL("Update sbmtopc set UFlag='1' where scno='" + c.getString(0) + "' and BookNo='" + c.getString(1) + "'");
                        } else{

                            db.execSQL("Update sbmtopc set UFlag='0' where scno='" + c.getString(0) + "' and BookNo='" + c.getString(1) + "'");
                        }
                    }catch (NullPointerException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        res = "IOException: " + e.toString();
                    }


                } while (c.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (db != null)
                db.close();
        }

    }

    public void send_sock() {

        record = "";
        record += billedRecords.servno + "$" + billedRecords.Book + "$" + "0" + "$" + billedRecords.Billdate;
        record += "$" + billedRecords.Billtime + "$" + billedRecords.BillNo.toString() + "$" + billedRecords.BMnths + "$" + billedRecords.MnthMin + "$" + billedRecords.Sload;
        record += "$" + billedRecords.CurLoad + "$" + billedRecords.CurPF + "$" + billedRecords.curread + "$" + billedRecords.CurStatus;
        record += "$" + billedRecords.BUnits + "$" + billedRecords.PrevAdjUnits + "$" + billedRecords.FixedChrg + "$" + billedRecords.EnrgyChrg + "$" + "00" + "$" + billedRecords.ED + "$" + billedRecords.CurIntrest;
        record += "$" + billedRecords.IntPEDArrs + "$" + "00" + "$" + billedRecords.DLAdjAmntED + "$" + "00" + "$" + billedRecords.Group;
        record += "$" + "00" + "$" + billedRecords.BillLG + "$" + billedRecords.NetAmntExArr + "$" + billedRecords.ArrearsOld + "$" + billedRecords.ArrearsCur;
        record += "$" + billedRecords.NetAmntInArr + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + billedRecords.AssdUnits + "$" + "00";
        record += "$" + "00000000" + "$" + billedRecords.ObrCode + "$" + billedRecords.SBA_ID + "$" + billedRecords.MacID + "$" + "0000000000" + "$" + billedRecords.Duedt + "$" + billedRecords.Discdt + "$" + "00";
        record += "$" + billedRecords.Prevreading + "$" + billedRecords.ArrNR + "$" + billedRecords.CapChrg + "$" + "00" + "$" + "00" + "$" + billedRecords.MtrCost;
        record += "$" + billedRecords.RODO_Amt + "$" + billedRecords.Regulatory_ch + "$" + billedRecords.str17 + "$" + billedRecords.Telno;
        record += "$" + billedRecords.fRebateB_amt + "$" + billedRecords.MinChrg + "$" + billedRecords.imageFileName + "$" + billedRecords.imageFileName2 + "$" + billedRecords.latitude + "$" + billedRecords.longitude + "$" + billedRecords.Areacode + "$" + MainActivity.Version;
    }

    public void send_rdg_image(String Path) {

        if (Path.equalsIgnoreCase("0") || Path == null) {
            BWfilename = "0";
            BWfilename2 = "0";
        } else {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BWfilename = Environment.getExternalStorageDirectory() + "/" + Path;
            try {
                File f = new File(BWfilename);

                outputFileUri = Uri.fromFile(f);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bm = BitmapFactory.decodeFile(outputFileUri.getPath(),
                    options);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] byte_arr = out.toByteArray();
            BWfilename = Base64.encodeBytes(byte_arr);
        }
    }

    public void send_md_image(String Path) {

        if (Path.equalsIgnoreCase("0") || Path == null) {
            BWfilename = "0";
            BWfilename2 = "0";
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BWfilename2 = Environment.getExternalStorageDirectory() + "/" + Path;
            try {
                File f = new File(BWfilename2);

                outputFileUri = Uri.fromFile(f);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bm = BitmapFactory.decodeFile(outputFileUri.getPath(),
                    options);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] byte_arr = out.toByteArray();
            BWfilename2 = Base64.encodeBytes(byte_arr);
        }
    }

    private class Upload extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;

        String Filename;
        String response=" ";


        Upload(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
            Filename = record + "$" + BWfilename.toString() + "$" + BWfilename2.toString();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                String number = Filename;
                String sendMessage = number + "\n";
                bw.write(sendMessage);
                bw.flush();

                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
                response = message;
                res=response;
                System.out.println("Message received from the server : " + message);


            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } catch (NullPointerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            super.onPostExecute(result);

        }
    }


    //--------------------------






    @Override
    public void onBackPressed ()
    {
        Intent inte = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inte);
    }


    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }
    public void read_sbm_2_pc(Cursor c)
    {
        billedRecords.servno=c.getString(0);
        billedRecords.Book=c.getString(1);
        billedRecords.SeqNo=c.getString(2);
        billedRecords.Billdate=c.getString(3);
        billedRecords.Billtime=c.getString(4);
        billedRecords.BillNo=c.getString(5);
        billedRecords.BMnths=c.getString(6);
        billedRecords.MnthMin=c.getString(7);
        billedRecords.Sload=c.getString(8);
        billedRecords.CurLoad=c.getString(9);
        billedRecords.CurPF=c.getString(10);
        billedRecords.curread=c.getString(11);
        billedRecords.CurStatus=c.getString(12);
        billedRecords.BUnits=c.getString(13);
        billedRecords.PrevAdjUnits=c.getString(14);
        billedRecords.FixedChrg=c.getString(15);
        billedRecords.EnrgyChrg=c.getString(16);
        billedRecords.str1=c.getString(17);
        billedRecords.ED=c.getString(18);
        billedRecords.CurIntrest=c.getString(19);
        billedRecords.IntPEDArrs=c.getString(20);
        billedRecords.str2=c.getString(21);
        billedRecords.DLAdjAmntED=c.getString(22);
        billedRecords.str3=c.getString(23);
        billedRecords.Group=c.getString(24);
        billedRecords.str4=c.getString(25);
        billedRecords.BillLG=c.getString(26);
        billedRecords.NetAmntExArr=c.getString(27);
        billedRecords.ArrearsOld=c.getString(28);
        billedRecords.ArrearsCur=c.getString(29);
        billedRecords.NetAmntInArr=c.getString(30);
        billedRecords.str5=c.getString(31);
        billedRecords.str6=c.getString(32);
        billedRecords.str7=c.getString(33);
        billedRecords.str8=c.getString(34);
        billedRecords.str9=c.getString(35);
        billedRecords.str10=c.getString(36);
        billedRecords.AssdUnits=c.getString(37);
        billedRecords.str11=c.getString(38);
        billedRecords.str12=c.getString(39);
        billedRecords.ObrCode=c.getString(40);
        billedRecords.SBA_ID=c.getString(41);
        billedRecords.MacID=c.getString(42);
        billedRecords.str13=c.getString(43);
        billedRecords.Duedt=c.getString(44);
        billedRecords.Discdt=c.getString(45);
        billedRecords.str14=c.getString(46);
        billedRecords.Prevreading=c.getString(47);
        billedRecords.ArrNR=c.getString(48);
        billedRecords.CapChrg=c.getString(49);
        billedRecords.str15=c.getString(50);
        billedRecords.str16=c.getString(51);
        billedRecords.MtrCost=c.getString(52);
//        billedRecords.RODO_Amt=c.getString(53);
        billedRecords.OTS_Amt=c.getString(53);
        billedRecords.Regulatory_ch=c.getString(54);
        billedRecords.str17=c.getString(55);
        billedRecords.Telno=c.getString(56);
        billedRecords.fRebateB_amt=c.getString(57);
        billedRecords.MinChrg=c.getString(59);
        billedRecords.imageFileName=c.getString(60);
        billedRecords.imageFileName2=c.getString(61);
        billedRecords.Areacode=c.getString(62);
//        billedRecords.latitude=st.nextToken();
//        billedRecords.longitude=st.nextToken();
    }

    public void read_ipaddress() {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";

        try {
            File myFile = new File("/sdcard/ipaddress.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                //aBuffer += aDataRow + "\n";
                ServerIP = aDataRow.substring(0, 15);

                break;

            }
            myReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void upload_data_to_server()
    {
        String Reading_Image="";
        String Meter_Image="";

        List<String[]> data=new ArrayList<String[]>();
        List<uploadRec> data2=new ArrayList<uploadRec>();
        DBConnections dbConnections = new DBConnections(this);
        data=dbConnections.getAllDiscRecords();
        String assigning_fetch_fieldnote_title="";
        String[] namesArr = new String[data.size()];
        fetch_fieldnote_title = new String[data.size()];
        int x=0;
        for (String[] name2 : data) {
            Reading_Image="";
            uploadRec uploadPojo=new uploadRec();
            uploadPojo.setSBM_NO(name2[0]);
            uploadPojo.setAddl_Security_Raised(name2[1]);
            uploadPojo.setBank_Name(name2[2]);
            uploadPojo.setBill_Amount_After_Due_Date(name2[3]);
            uploadPojo.setBill_Date(name2[4]);
            uploadPojo.setBill_Generation_Status(name2[5]);
            uploadPojo.setBill_issue_date(name2[6]);
            uploadPojo.setBill_Net_within_due_date(name2[7]);
            uploadPojo.setBILL_NO(name2[8]);
            uploadPojo.setBill_Time(name2[9]);
            uploadPojo.setBP_Number(name2[10]);
            uploadPojo.setCash_Due_Date(name2[11]);
            uploadPojo.setCess(name2[12]);
            uploadPojo.setCheque_Due_Date(name2[13]);
            uploadPojo.setChq_Amt(name2[14]);
            uploadPojo.setChq_date(name2[15]);
            uploadPojo.setChq_Number(name2[16]);
            uploadPojo.setCons_Mob_No(name2[17]);
            uploadPojo.setCredit_DLAmt(name2[18]);
            uploadPojo.setCur_Bill_Month(name2[19]);
            uploadPojo.setCur_Bill_Year(name2[20]);
            uploadPojo.setCur_Mtr_Sts(name2[21]);
            uploadPojo.setCur_PF(name2[22]);
            uploadPojo.setDistribution_Code(name2[23]);
            uploadPojo.setDuty(name2[24]);
            uploadPojo.setEC(name2[25]);
            uploadPojo.setEvent_log_number(name2[26]);
            uploadPojo.setFC(name2[27]);
            uploadPojo.setLow_PF_Penality_Incentive(name2[28]);
            uploadPojo.setLTCS_Charge(name2[29]);
            uploadPojo.setManual_Demand(name2[30]);
            uploadPojo.setMeter_Reader_Name(name2[31]);
            uploadPojo.setMeter_Reading_Unit(name2[32]);
            uploadPojo.setMeter_Rent(name2[33]);
            uploadPojo.setMF(name2[34]);
            uploadPojo.setMisc_flag_mark(name2[35]);
            uploadPojo.setOffice_Incharge(name2[36]);
            uploadPojo.setOnline_Flag_number(name2[37]);
            uploadPojo.setPenality_FC(name2[38]);
            uploadPojo.setPenalty_20_Addl_Chg(name2[39]);
            uploadPojo.setPenalty_EC(name2[40]);
            uploadPojo.setPole_Number(name2[41]);
            uploadPojo.setPosting_Date(name2[42]);
            uploadPojo.setPres_Read_KW_RMD(name2[43]);
            uploadPojo.setPres_Read_KWH(name2[44]);
            uploadPojo.setPrev_Arrears(name2[45]);
            uploadPojo.setPrev_Read(name2[46]);
            uploadPojo.setRebate_EC(name2[47]);
            uploadPojo.setRebate_FC(name2[48]);
            uploadPojo.setRebate_other(name2[49]);
            uploadPojo.setRound_Off_amount(name2[50]);
            uploadPojo.setRoute_Seq_No(name2[51]);
            uploadPojo.setSBM_Sw_Ver(name2[52]);
            uploadPojo.setSD_Arrear(name2[53]);
            uploadPojo.setSD_Interest(name2[54]);
            uploadPojo.setSurcharge(name2[55]);
            uploadPojo.setSurcharge_Arrears(name2[56]);
            uploadPojo.setTotal_Bill(name2[57]);
            uploadPojo.setUnit_Billed(name2[58]);
            uploadPojo.setVCA_Charge(name2[59]);
            uploadPojo.setVendor_code(name2[60]);
            uploadPojo.setWTCS_Surcharge(name2[61]);
            uploadPojo.setZone_Name(name2[62]);
            *//*uploadPojo.setRdg_Img(name2[63]);

            uploadPojo.setRdg_Img(Upload_image(name2[23]));
            uploadPojo.setMtr_Img(Upload_image(name2[24]));*//*
            data2.add(uploadPojo);
        }
        DataPojo datapojo= new DataPojo();

        datapojo.setData(data2);

        RestServices.upload_data_to_server(this,datapojo);
    }*/
}
