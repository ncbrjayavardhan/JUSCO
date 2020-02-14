package com.jusco;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jusco.MRActivity.BWfilename;
import static com.jusco.MRActivity.BWfilename2;
import static com.jusco.MRActivity.record;

public class UploadActivity extends AppCompatActivity {


    IntentFilter intentfilter;
    int deviceStatus;
    String currentBatteryStatus="Battery Info";
    int batteryLevel;
    int batlevelVal=0;

    String[] fetch_fieldnote_title;
    TextView textView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    SQLiteDatabase db;
    String Str="S";
    String DBNAME="";

    public static String res;
    String response2=" ";
    //    String ServerIP = "183.82.128.143";
    String ServerPort = "25000";
    String ServerIP = "124.123.41.255";
//    String ServerPort = "25000";

    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        DBNAME=getResources().getString(R.string.DBNAME);
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        read_ipaddress();
        textView = (TextView) findViewById(R.id.textView);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
    }


    @Override
    protected void onStart() {
        super.onStart();
        /*Datasend();*/
        upload_data_to_server();
       /* UploadData_Async uploadData_async = new UploadData_Async();
        uploadData_async.execute();*/
        Display();
    }

    public void Datasend() {

        Cursor c = null;
        Cursor c1 = null;
        String scno;

        try {

            db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("select * from sbmtopc where Online_Flag_number='0'", null);
            if (c.moveToFirst()) {
                scno = c.getString(0);
                Log.i("SCNO",scno);
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
//                    send_md_image(c.getString(61));

                    try{
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    SocketSend();

                    /*try{
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
*/
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    db.execSQL("Update sbmtopc set UFlag='1' where scno='" + c.getString(0) + "' and BookNo='" + c.getString(1) + "'");
                    /*try{
                        if(res.equals(Str)){

                            db.execSQL("Update sbmtopc set UFlag='1' where scno='" + c.getString(0) + "' and BookNo='" + c.getString(1) + "'");
                        } else{

                            db.execSQL("Update sbmtopc set UFlag='0' where scno='" + c.getString(0) + "' and BookNo='" + c.getString(1) + "'");
                        }*/
//                    }catch (NullPointerException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                        res = "IOException: " + e.toString();
//                    }

                } while (c.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (db != null)
                db.close();
        }

    }

    public void send_sock() {

//        record = "";
//        record += billedRecords.servno + "$" + billedRecords.Book + "$" + "0" + "$" + billedRecords.Billdate;
//        record += "$" + billedRecords.Billtime + "$" + billedRecords.BillNo.toString() + "$" + billedRecords.BMnths + "$" + billedRecords.MnthMin + "$" + billedRecords.Sload;
//        record += "$" + billedRecords.CurLoad + "$" + billedRecords.CurPF + "$" + billedRecords.curread + "$" + billedRecords.CurStatus;
//        record += "$" + billedRecords.BUnits + "$" + billedRecords.PrevAdjUnits + "$" + billedRecords.FixedChrg + "$" + billedRecords.EnrgyChrg + "$" + "00" + "$" + billedRecords.ED + "$" + billedRecords.CurIntrest;
//        record += "$" + billedRecords.IntPEDArrs + "$" + "00" + "$" + billedRecords.DLAdjAmntED + "$" + "00" + "$" + billedRecords.Group;
//        record += "$" + "00" + "$" + billedRecords.BillLG + "$" + billedRecords.NetAmntExArr + "$" + billedRecords.ArrearsOld + "$" + billedRecords.ArrearsCur;
//        record += "$" + billedRecords.NetAmntInArr + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + "00" + "$" + billedRecords.AssdUnits + "$" + "00";
//        record += "$" + "00000000" + "$" + billedRecords.ObrCode + "$" + billedRecords.SBA_ID + "$" + billedRecords.MacID + "$" + "0000000000" + "$" + billedRecords.Duedt + "$" + billedRecords.Discdt + "$" + "00";
//        record += "$" + billedRecords.Prevreading + "$" + billedRecords.ArrNR + "$" + billedRecords.CapChrg + "$" + "00" + "$" + "00" + "$" + billedRecords.MtrCost;
//        record += "$" + billedRecords.RODO_Amt + "$" + billedRecords.Regulatory_ch + "$" + billedRecords.str17 + "$" + billedRecords.Telno;
//        record += "$" + billedRecords.fRebateB_amt + "$" + billedRecords.MinChrg + "$" + billedRecords.imageFileName + "$" + billedRecords.imageFileName2 + "$" + billedRecords.latitude + "$" + billedRecords.longitude + "$" + billedRecords.Areacode + "$" + MainActivity.Version;
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
            bm.compress(Bitmap.CompressFormat.PNG, 0, out);
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

    public void Display(){
        String Total;
        String upload;
        String No;


        try {

            db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("select Count(*) from sbmtopc ", null);//For Total records
            if (c.moveToFirst()) {
                Total = c.getString(0);
                textView1.setVisibility(View.VISIBLE);
                textView1.setText(getResources().getString(R.string.TotalRecords)+": " + Total);

            }
            db.close();
            db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("select count(*) from sbmtopc where Online_Flag_number='0'", null);//For not uploaded records
            if (c.moveToFirst()) {
                No = c.getString(0);
                textView2.setVisibility(View.VISIBLE);

                textView2.setText(getResources().getString(R.string.Not_Upload_Record)+ ": " + No);

            }
            db.close();
            db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("select count(*) from sbmtopc where Online_Flag_number='1'", null);//For Uploaded records
            if (c.moveToFirst()) {
                upload = c.getString(0);
                textView3.setVisibility(View.VISIBLE);

                textView3.setText(getResources().getString(R.string.Upload_Records) + ": " + upload);

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null)
                db.close();
        }


    }

    public void SocketSend() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Socket client;
                PrintWriter printwriter;
                int port = 25000;
                String Send_String = "";
//                Send_String = BWfilename.toString() + "?" + BWfilename2.toString() + "?" + record  ;
                Send_String =  record  + "$" + BWfilename.toString() ;//+ "$" + BWfilename2.toString();
                try {
                    client = new Socket(ServerIP, port);
                    printwriter = new PrintWriter(client.getOutputStream(), true);
                    printwriter.write(Send_String);
                    printwriter.flush();
                    printwriter.close();
                    client.close();
                    Thread.sleep(3000);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    public void upload_data_to_server()
    {
        String Reading_Image="";
        String Meter_Image="";
        String[] title=new String[1024];
        List<String[]> data=new ArrayList<String[]>();
        List<uploadRec> data2=new ArrayList<uploadRec>();

//        this.registerReceiver(broadcastreceiver,intentfilter);

//        if(batlevelVal>25) {
           /* DBConnections dbConnections = new DBConnections(this);
            data = dbConnections.getAllDiscRecords();*/

        String selectQuery = "SELECT  * FROM sbmtopc where online_flag_number = '0'";
        SQLiteDatabase db2;
        db2 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db2.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                title=new String[]{cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17),
                        cursor.getString(18),
                        cursor.getString(19),
                        cursor.getString(20),
                        cursor.getString(21),
                        cursor.getString(22),
                        cursor.getString(23),
                        cursor.getString(24),
                        cursor.getString(25),
                        cursor.getString(26),
                        cursor.getString(27),
                        cursor.getString(28),
                        cursor.getString(29),
                        cursor.getString(30),
                        cursor.getString(31),
                        cursor.getString(32),
                        cursor.getString(33),
                        cursor.getString(34),
                        cursor.getString(35),
                        cursor.getString(36),
                        cursor.getString(37),
                        cursor.getString(38),
                        cursor.getString(39),
                        cursor.getString(40),
                        cursor.getString(41),
                        cursor.getString(42),
                        cursor.getString(43),
                        /*cursor.getString(42),
                        cursor.getString(43),
                        cursor.getString(44),
                        cursor.getString(45),
                        cursor.getString(46),
                        cursor.getString(47),
                        cursor.getString(48),
                        cursor.getString(49),
                        cursor.getString(50),
                        cursor.getString(51),
                        cursor.getString(52),
                        cursor.getString(53),
                        cursor.getString(54),
                        cursor.getString(55),
                        cursor.getString(56),
                        cursor.getString(57),
                        cursor.getString(58),
                        cursor.getString(59),
                        cursor.getString(60),
                        cursor.getString(61),
                        cursor.getString(62),
                        cursor.getString(63),
                        cursor.getString(64),
                        cursor.getString(65),
                        cursor.getString(66),
                        cursor.getString(67),*/
                        cursor.getString(44)};
                data=new ArrayList<String[]>();
                data.add(title);
                String assigning_fetch_fieldnote_title = "";
                String[] namesArr = new String[data.size()];
                fetch_fieldnote_title = new String[data.size()];
                int x = 0;
                for (String[] name2 : data) {
                    Reading_Image = "";
                    uploadRec uploadPojo = new uploadRec();

                    uploadPojo.setBP_Number(name2[0]);
                    uploadPojo.setMETER_SERIAL_NO(name2[1]);
                    uploadPojo.setCURR_READING(name2[2]);
                    uploadPojo.setMRNOTE(name2[3]);
                    uploadPojo.setBILLED_UNITS(name2[4]);
                    uploadPojo.setNETUNITS(name2[5]);

                    uploadPojo.setENERGY_CHARGE(name2[6]);
                    uploadPojo.setE_DUTY(name2[7]);
                    uploadPojo.setMETER_RENT(name2[8]);

                    uploadPojo.setFIXEDCHARGE(name2[9]);
                    uploadPojo.setLPC(name2[10]);
                    uploadPojo.setINTERESTONSD(name2[11]);
                    uploadPojo.setTDS(name2[12]);

                    uploadPojo.setRebateEarly(name2[13]);
                    uploadPojo.setRebateDigital(name2[14]);
                    uploadPojo.setTot_rebate(name2[15]);
                    uploadPojo.setOTHERRECEIVABLE(name2[16]);
                    uploadPojo.setPREVIOUSOS(name2[17]);

                    uploadPojo.setFPPPACharg(name2[18]);
                    uploadPojo.setBill_Amt(name2[19]);
                    uploadPojo.setTOTAL(name2[20]);
                    uploadPojo.setSBM_NO(name2[21]);

                    uploadPojo.setLAT(name2[22]);
                    uploadPojo.setLONG(name2[23]);
                    uploadPojo.setSBM_Sw_Ver(name2[24]);
                    uploadPojo.setBILL_NO(name2[25]);

                    uploadPojo.setBill_Date(name2[26]);
                    uploadPojo.setBill_Time(name2[27]);
                    uploadPojo.setCons_Mob_No(name2[28]);
                    uploadPojo.setCur_Mtr_Sts(name2[29]);//to be deleted

                    uploadPojo.setPrev_Read(name2[30]);
                    uploadPojo.setPres_Read_KW_RMD(name2[31]);
                    uploadPojo.setCur_PF(name2[32]);
                    uploadPojo.setBill_Due_date(name2[33]);

                    uploadPojo.setRound_Off_amount("0");
                    uploadPojo.setBill_Net_within_due_date(name2[35]);
                    uploadPojo.setBill_Amount_After_Due_Date(name2[36]);
                    uploadPojo.setBill_Generation_Status("Y");
                    uploadPojo.setMeter_Reader_Name(name2[38]);

                    uploadPojo.setObr_Code("OK");
                    uploadPojo.setOnline_Flag_number("S");
                    uploadPojo.setCat_tariff(name2[41]);
                    uploadPojo.setMRU(name2[42]);
                    uploadPojo.setPortion(name2[43]);
                    send_rdg_image(name2[44]);
                    uploadPojo.setRdg_img_Path(BWfilename);
                    data2=new ArrayList<uploadRec>();
                    data2.add(uploadPojo);
                    DataPojo datapojo = new DataPojo();

                    datapojo.setData(data2);

                    response2 = RestServices.upload_data_to_server(this, datapojo);

                    if (response2.equals("OK") || response2.equals("ok") || response2.equals("Ok") || response2.equals("oK")) {
                        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
                        db.execSQL("Update sbmtopc set Online_Flag_number='1' where Online_Flag_number='0' and bp_no='" + name2[0] + "'");
                        db.close();
                    }
                }
            } while (cursor.moveToNext());
        }
            /*DataPojo datapojo = new DataPojo();

            datapojo.setData(data2);

            response2 = RestServices.upload_data_to_server(this, datapojo);

            if (response2.equals("OK") || response2.equals("ok") || response2.equals("Ok") || response2.equals("oK")) {
                db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
                db.execSQL("Update sbmtopc set Online_Flag_number='1' where Online_Flag_number='0'");
                db.close();
            }*/
        /*    showMessage("Success", "Data sent to server");
        }*/
       /* else{
            showMessage("Error", "Battery Level is too low to send data.");
        }*/
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel=(int)(((float)level / (float)scale) * 100.0f);
            batlevelVal=batteryLevel;
            if(deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING){

                Log.e("Bat",currentBatteryStatus+" = Charging at "+batteryLevel+" %");

            }

            if(deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING){

                Log.e("Bat",currentBatteryStatus+" = Discharging at "+batteryLevel+" %");

            }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL){

                Log.e("Bat",currentBatteryStatus+"= Battery Full at "+batteryLevel+" %");

            }

            if(deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN){

                Log.e("Bat",currentBatteryStatus+" = Unknown at "+batteryLevel+" %");
            }


            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING){

                Log.e("Bat",currentBatteryStatus+" = Not Charging at "+batteryLevel+" %");

            }

        }
    };

    public class UploadData_Async extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog= new ProgressDialog(UploadActivity.this);
        public UploadData_Async() {
            dialog.setMessage("Data Uploading, please wait.");
            dialog.setTitle("Sending Data to Server");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }
        @Override
        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... args) {
            // do background work here
            upload_data_to_server();
            return null;
        }

        protected void onPostExecute(Void result) {
//            dialog.dismiss();
//            Toast.makeText(DataLoading.this,"Data Download Completed",Toast.LENGTH_SHORT).show();
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
//                Toast.makeText(DataLoading.this,"Data Download Completed",Toast.LENGTH_SHORT).show();
                showMessage("DataUplaod","Completed");
            }
        }
    }


}



