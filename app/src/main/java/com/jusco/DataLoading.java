package com.jusco;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DataLoading extends AppCompatActivity {
    private ProgressDialog progress;
    ProgressDialog dialog;
    SQLiteDatabase db;
    public String retStr="a";
    ProgressBar pbar;
    ProgressDialog progressDialog;
//    SQLiteDatabase db;
    Context context;
    Button btndwn,btnOW,btnAD;
    TextView txtpgs;
    int ServerPort = 25000;
    String ServerIP = "124.123.41.255";
//    String ServerIP = "192.168.1.6";
    public final static int FILE_SIZE = 6022386;
    String FILE_TO_RECEIVED = Environment.getExternalStorageDirectory().getPath() + "/document.encrypted";
    public static String DBNAME="";
    JSONArray Data_Records;
    String dbFilepath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_loading);
        context=this;
        btndwn = (Button) findViewById(R.id.btnDown);
        btnOW = (Button) findViewById(R.id.btnOverwrite);
        btnAD = (Button) findViewById(R.id.btnAppend);
        txtpgs=(TextView) findViewById(R.id.txtprgs);
        pbar=(ProgressBar)findViewById(R.id.progressBar2);
        pbar.setProgress(0);
        DBNAME=getResources().getString(R.string.DBNAME);
        dbFilepath="";
        read_ipaddress();
        /*if(MainActivity.Language.equals("H"))
        {
            apply_hindi();
        }

        if(MainActivity.Language.equals("E"))
        {
            apply_English();
        }if(MainActivity.Language.equals("T"))
        {
            apply_Telugu();
        }*/

    }

    public void Overwrite_Data(View view)
    {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
        {
             AltDialog_Local("Local Data Loading","Do you wish to Overwrite/Append?", "Overwrite","Append","");
        }
////        overwritedata_db();
        else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");
            startActivityForResult(intent, 7);
        }
//      AltDialog_Local("Local Data Loading","Do you wish to Overwrite/Append?", "Overwrite","Append","");


          /*if(retStr.equals("YES")) {

              Toast.makeText(DataLoading.this, retStr, Toast.LENGTH_SHORT).show();

          }*/

               /* LocalData_Async lda=new LocalData_Async();
                lda.execute();*/



    }
   /* public void decrypt_file() {
        {
            String key = "Mary has one cat";
//            String key = "Z8LSq0wWwB5v+6YJzurcP463H3F12iZh74fDj4S74oUH4EONkiKb2FmiWUbtFh97GG/c/lbDE47mvw6j94yXxKHOpoqu6zpLKMKPcOoSppcVWb2q34qENBJkudXUh4MWcreondLmLL2UyydtFKuU9Sa5VgY/CzGaVGJABK2ZR94=";
            File inputFile = new File("/sdcard/document.encrypted");
//        File encryptedFile = new File("document.encrypted");
            File decryptedFile = new File("/sdcard/document.decrypted");
//        Toast.makeText(this,Environment.getRootDirectory().toString()+"/document.decrypted",Toast.LENGTH_LONG);
//System.out.println(Environment.getRootDirectory().toString()+"/document.decrypted");
            try {
//            CryptoUtils.encrypt(key, inputFile, encryptedFile);
                CryptoUtils.decrypt(key, inputFile, decryptedFile);
            } catch (CryptoException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }*/

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

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public String AltDialog(String title,String message, String btnPositive,String btnNegetive)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                retStr="YES";
                overwritedata_db();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(btnNegetive, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                retStr="NO";
                // Do nothing
                appenddata_db();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return retStr;
    }

    public void Append_Data(View v)
    {
        appenddata_db();
    }

    public void btndwn(View view) {
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
           /* Socketsend send = new Socketsend(ServerIP, ServerPort);
            send.execute();*/

            Data_Records=RestServices.Data_Fetch(context.getResources());

            if(Data_Records !=null ) {
                //Value is not null
                AltDialog("GPRS Data Loading", "Do you wish to Overwrite/Append?", "Overwrite", "Append", Data_Records);

            }
            else
            {
                Toast.makeText(this,"Data Not Received from Server",Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(DataLoading.this, "Network Not Available", Toast.LENGTH_LONG).show();
        }
    }




    public void overwritedata_db()
    {
        String aBuffer = "";
        String aDataRow = "";
        String retVal;
        String[] data=new String[100];

        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        db.delete("pctosbm",null,null);
        db.delete("sbmtopc",null,null);
        db.close();
        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pctosbm(" +
                "BP_Number                     varchar(10),\n" +//1
                "MRU                          varchar(8),\n" +//2
                "Legacy_Number                varchar(20),\n" +//3
                "Consumer_Index_Number        varchar(20),\n" +//4
                "Name_of_Consumer             varchar(24),\n" +//5
                "Address1                     varchar(24),\n" +//6
                "Address2                     varchar(24),\n" +//7
                "Address3                     varchar(24),\n" +//8
                "Mobile_No                    varchar(10),\n" +//9
                "Nature_of_Supply                  varchar(1),\n" +//10
                "Purpose                           varchar(24),\n" +//11
                "Tariff_Code                           varchar(10),\n" +//12
                "rate_category                           varchar(4),\n" +//13
                "Duty_Code                           varchar(2),\n" +//14
                "LPF_Flag                           varchar(1),\n" +//15
                "RMD_Flag                           varchar(1),\n" +//16
                "Cess_IP                           varchar(4),\n" +//17
                "Rebate_FC                           varchar(3),\n" +//18
                "Rebate_Type                           varchar(4),\n" +//20
                "Meter_Rent                           varchar(3),\n" +//21
                "Meter_Phase                           varchar(1),\n" +//22
                "KWH_MF                           varchar(5),\n" +//23
                "Contract_Load                           varchar(9),\n" +//24
                "Measurement_Load_Code                           varchar(1),\n" +//25
                "LT_Capacitor_Code_for_LowPower                          varchar(1),\n" +//26
                "Welder_Capacitor_Code_for_Low                          varchar(1),\n" +//27
                "Average_Unit_for_Defective                           varchar(5),\n" +//28
                "Average_MD                           varchar(3),\n" +//29
                "Average_PF                           varchar(3),\n" +//30
                "Meter_Status                           varchar(2),\n" +//31
                "PFL_COUNTER                           varchar(2),\n" +//32
                "Average_Amount                           varchar(6),\n" +//33
                "Amount_from_the_operand_DL_AMNT                          varchar(6),\n" +//34
                "Bill_Month                           varchar(2),\n" +//35
                "Bill_Year                           varchar(4),\n" +//36
                "Route_SeqNo                          varchar(10),\n" +//37
                "MeterMake_and_No                          varchar(10),\n" +//38
                "SD_Held                           varchar(10),\n" +//39
                "Previous_Arrears                           varchar(10),\n" +//40
                "Sucharge_Arrears                           varchar(10),\n" +//41
                "SD_Arrear                           varchar(10),\n" +//42
                "Additional_Security_Raised                           varchar(10),\n" +//43
                "SD_Interest                           varchar(10),\n" +//44
                "Manual_Demand                           varchar(10),\n" +//45
                "Meter_Reading_Previous_KWH                          varchar(7),\n" +//46
                "Initial_Reading                           varchar(7),\n" +//47
                "Final_Reading                           varchar(7),\n" +//48
                "Old_Meter_Consumption                           varchar(5),\n" +//49
                "Consumer_Status                          varchar(2),\n" +//50
                "Cheque_Accept_Status                           varchar(1),\n" +//51
                "Last_Payment_Date                           varchar(10),\n" +//52
                "Posting_Date                           varchar(10),\n" +//53
                "Office_Phone                           varchar(12),\n" +//54
                "Agriculture_Unit_Rebate                          varchar(6),\n" +//55
                "Issue_Date                          varchar(10),\n" +//56
                "Cheque_Due_Date                          varchar(10),\n" +//57
                "Cash_Due_Date                          varchar(10),\n" +//58
                "Print_Flag                          varchar(1),\n" +//59
                "VCA_Rate                          varchar(8),\n" +//60
                "Last_Meter_Reading_Date                           varchar(10),\n" +//61
                "Officers_Incharge                           varchar(20),\n" +//62
                "Date_of_TarrifChange                          varchar(10),\n" +//63
                "Zone_Code                          varchar(10),\n" +//64
                "SBM_No                          varchar(8),\n" +//65
                "Vendor_Code                          varchar(2),\n" +//66
                "Reader_Name                          varchar(24),\n" +//67
                "Shedule_MRD                          varchar(10),\n" +//68
                "SG_Rebate_Cons_Capping                varchar(10),\n" +//69
                "SG_Rebate_Measurement_Code varchar(10));");//70

            db.execSQL("CREATE TABLE IF NOT EXISTS sbmtopc(ScNo       varchar(6),\n" +
                    "BookNo       varchar(4),\n" +
                    "SeqNo       varchar(4),\n" +
                    "Bdate       varchar(10),\n" +
                    "Btime       varchar(8),\n" +
                    "BillNo       varchar(7),\n" +
                    "BMnths       varchar(2),\n" +
                    "MnthMin       varchar(1),\n" +
                    "SLoad       varchar(7),\n" +
                    "CurLoad       varchar(7),\n" +
                    "CurPF       varchar(6),\n" +
                    "ClRead       varchar(6),\n" +
                    "ClStatus       varchar(2),\n" +
                    "BUnits       varchar(6),\n" +
                    "PrevAdjUnits       varchar(6),\n" +
                    "FixedChrg       varchar(9),\n" +
                    "EnrgyChrg       varchar(10),\n" +
                    "Rebate       varchar(9),\n" +
                    "ED       varchar(9),\n" +
                    "CurIntrest       varchar(9),\n" +
                    "IntPEDArrs       varchar(11),\n" +
                    "DLAdjAmntCC       varchar(9),\n" +
                    "DLAdjAmntED       varchar(7),\n" +
                    "temp1       varchar(8),\n" +
                    "Grp       varchar(1),\n" +
                    "ExtAmnt2       varchar(9),\n" +
                    "BillLG       varchar(9),\n" +
                    "NetAmntExArr       varchar(11),\n" +
                    "PArrsOldFY       varchar(11),\n" +
                    "PArrsCurFY       varchar(11),\n" +
                    "NetAmntInArr       varchar(11),\n" +
                    "BPFlag       varchar(1),\n" +
                    "RcptNo       varchar(4),\n" +
                    "AmntPaid       varchar(11),\n" +
                    "ChqNo       varchar(6),\n" +
                    "BankName       varchar(20),\n" +
                    "BankCode       varchar(9),\n" +
                    "AssdUnits       varchar(6),\n" +
                    "AssdAmnt       varchar(9),\n" +
                    "ChqDate       varchar(10),\n" +
                    "Rmrks       varchar(2),\n" +
                    "SBAssID       varchar(4),\n" +
                    "SBMchnID       varchar(4),\n" +
                    "LPNo       varchar(10),\n" +
                    "Due       varchar(10),\n" +
                    "Disc       varchar(10),\n" +
                    "CurCMD       varchar(9),\n" +
                    "PrevReading       varchar(6),\n" +
                    "ArrearNR       varchar(9),\n" +
                    "CapChrg       varchar(9),\n" +
                    "Flag       varchar(1),\n" +
                    "UFlag       varchar(1),\n" +
                    "MtrRent       varchar(9),\n" +
                    "RODO_Amt       varchar(10),\n" +
                    "freg_chrg       varchar(10),\n" +
                    "USRNO       varchar(11),\n" +
                    "Telephoneno       varchar(11),\n" +
                    "fRebateB_amt       varchar(10),\n" +
                    "fRebate_amt       varchar(10),\n" +
                    "MinChrg       varchar(10),\n" +
                    "Image1       varchar(25),\n" +
                    "Image2       varchar(25),\n" +
                    "Lat       varchar(25),\n" +
                    "Lon       varchar(25),\n" +
                    "Areacode       varchar(4),\n" +
                    "AppVer       varchar(20))");
        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        try {
            File myFile;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
             myFile = new File("/sdcard/jusco.txt");
            }
            else {
                 myFile = new File(dbFilepath);
            }
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
//                String scno = aDataRow.substring(0, 6);
                data=aDataRow.split("\\$");

                String Instval = "INSERT INTO pctosbm VALUES('" + data[0].replace("'","") + "','" + data[1].trim().replace("'","") + "','" +
                        data[2].trim().replace("'","") + "','" +
                        data[3].trim().replace("'","") + "','" +
                        data[4].trim().replace("'","") + "','" +
                        data[5].trim().replace("'","") + "','" +
                        data[6].trim().replace("'","") + "','" +
                        data[7].trim().replace("'","") + "','" +
                        data[8].trim().replace("'","") + "','" +
                        data[9].trim().replace("'","") + "','" +
                        data[10].trim().replace("'","") + "','" +
                        data[11].trim().replace("'","") + "','" +
                        data[12].trim().replace("'","") + "','" +
                        data[13].trim().replace("'","") + "','" +
                        data[14].trim().replace("'","") + "','" +
                        data[15].trim().replace("'","") + "','" +
                        data[16].trim().replace("'","") + "','" +
                        data[17].trim().replace("'","") + "','" +
                        data[18].trim().replace("'","") + "','" +
                        data[19].trim().replace("'","") + "','" +
                        data[20].trim().replace("'","") + "','" +
                        data[21].trim().replace("'","") + "','" +
                        data[22].trim().replace("'","") + "','" +
                        data[23].trim().replace("'","") + "','" +
                        data[24].trim().replace("'","") + "','" +
                        data[25].trim().replace("'","") + "','" +
                        data[26].trim().replace("'","") + "','" +
                        data[27].trim().replace("'","") + "','" +
                        data[28].trim().replace("'","") + "','" +
                        data[29].trim().replace("'","") + "','" +
                        data[30].trim().replace("'","") + "','" +
                        data[31].trim().replace("'","") + "','" +
                        data[32].trim().replace("'","") + "','" +
                        data[33].trim().replace("'","") + "','" +
                        data[34].trim().replace("'","") + "','" +
                        data[35].trim().replace("'","") + "','" +
                        data[36].trim().replace("'","") + "','" +
/*                        data[37].trim().replace("'","") + "','" +
                        data[38].trim().replace("'","") + "','" +
                        data[39].trim().replace("'","") + "','" +
                        data[40].trim().replace("'","") + "','" +
                        data[41].trim().replace("'","") + "','" +
                        data[42].trim().replace("'","") + "','" +
                        data[43].trim().replace("'","") + "','" +
                        data[44].trim().replace("'","") + "','" +*/

                        data[37].trim().replace("'","") + "');";

                        /*data[30].trim().replace("'","") + "','" +
                        data[31].trim().replace("'","") + "','" +
                        data[32].trim().replace("'","") + "','" +
                        data[33].trim().replace("'","") + "','" +
                        data[34].trim().replace("'","") + "','" +
                        data[35].trim().replace("'","") + "','" +
                        data[36].trim().replace("'","") + "','" +
                        data[37].trim().replace("'","") + "','" +
                        data[38].trim().replace("'","") + "','" +
                        data[39].trim().replace("'","") + "','" +
                        data[40].trim().replace("'","") + "','" +
                        data[41].trim().replace("'","") + "','" +
                        data[42].trim().replace("'","") + "','" +
                        data[43].trim().replace("'","") + "','" +
                        data[44].trim().replace("'","") + "','" +
                        data[45].trim().replace("'","") + "','" +
                        data[46].trim().replace("'","") + "','" +
                        data[47].trim().replace("'","") + "','" +
                        data[48].trim().replace("'","") + "','" +
                        data[49].trim().replace("'","") + "','" +
                        data[50].trim().replace("'","") + "','" +
                        data[51].trim().replace("'","") + "','" +
                        data[52].trim().replace("'","") + "','" +
                        data[53].trim().replace("'","") + "','" +
                        "0"+ "','" +
                        "0"+ "','" +
                        data[56].trim().replace("'","") + "','" +
                        data[57].trim().replace("'","") + "','" +
                        "0"+ "','" +
                        data[59].trim().replace("'","") + "','" +
                        data[60].trim().replace("'","") + "','" +
                        data[61].trim().replace("'","") + "','" +
                        data[62].trim().replace("'","") + "','" +
                        data[63].trim().replace("'","") + "','" +
                        data[64].trim().replace("'","") + "','" +
                        data[65].trim().replace("'","") + "','" +
                        data[66].trim().replace("'","") + "','" +
                        data[67].trim().replace("'","") + "','" +
                        data[68].trim().replace("'","") + "','" +*/

                db.execSQL(Instval);
            }
            myReader.close();

            ///Mask these line after testing completion
            /*String sql="Update pctosbm set meter_status='15'";
            db.execSQL(sql);*/
            ///Mask these line after testing completion
            db.close();
//            deleteFiles("/sdcard/document.decrypted");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        showMessage("Data Loading", "Completed");
    }

    public void appenddata_db()
    {
        String aBuffer = "";
        String aDataRow = "";
        String retVal;
        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
//        decrypt_file();

        try {
//            File myFile = new File("/sdcard/jusco.txt");
            File myFile = new File(dbFilepath);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
//                String scno = aDataRow.substring(0, 6);
                String[] data=aDataRow.split("\\$");

                String Instval = "INSERT INTO pctosbm VALUES('" + data[0].replace("'","") + "','" + data[1].trim().replace("'","") + "','" +
                        data[2].trim().replace("'","") + "','" +
                        data[3].trim().replace("'","") + "','" +
                        data[4].trim().replace("'","") + "','" +
                        data[5].trim().replace("'","") + "','" +
                        data[6].trim().replace("'","") + "','" +
                        data[7].trim().replace("'","") + "','" +
                        data[8].trim().replace("'","") + "','" +
                        data[9].trim().replace("'","") + "','" +
                        data[10].trim().replace("'","") + "','" +
                        data[11].trim().replace("'","") + "','" +
                        data[12].trim().replace("'","") + "','" +
                        data[13].trim().replace("'","") + "','" +
                        data[14].trim().replace("'","") + "','" +
                        data[15].trim().replace("'","") + "','" +
                        data[16].trim().replace("'","") + "','" +
                        data[17].trim().replace("'","") + "','" +
                        data[18].trim().replace("'","") + "','" +
                        data[19].trim().replace("'","") + "','" +
                        data[20].trim().replace("'","") + "','" +
                        data[21].trim().replace("'","") + "','" +
                        data[22].trim().replace("'","") + "','" +
                        data[23].trim().replace("'","") + "','" +
                        data[24].trim().replace("'","") + "','" +
                        data[25].trim().replace("'","") + "','" +
                        data[26].trim().replace("'","") + "','" +
                        data[27].trim().replace("'","") + "','" +
                        data[28].trim().replace("'","") + "','" +
                        data[29].trim().replace("'","") + "','" +
                        data[30].trim().replace("'","") + "','" +
                        data[31].trim().replace("'","") + "','" +
                        data[32].trim().replace("'","") + "','" +
                        data[33].trim().replace("'","") + "','" +
                        data[34].trim().replace("'","") + "','" +
                        data[35].trim().replace("'","") + "','" +
                        data[36].trim().replace("'","") + "','" +
/*                        data[37].trim().replace("'","") + "','" +
                        data[38].trim().replace("'","") + "','" +
                        data[39].trim().replace("'","") + "','" +
                        data[40].trim().replace("'","") + "','" +
                        data[41].trim().replace("'","") + "','" +
                        data[42].trim().replace("'","") + "','" +
                        data[43].trim().replace("'","") + "','" +
                        data[44].trim().replace("'","") + "','" +*/

                        data[37].trim().replace("'","") + "');";

                        /*data[30].trim().replace("'","") + "','" +
                        data[31].trim().replace("'","") + "','" +
                        data[32].trim().replace("'","") + "','" +
                        data[33].trim().replace("'","") + "','" +
                        data[34].trim().replace("'","") + "','" +
                        data[35].trim().replace("'","") + "','" +
                        data[36].trim().replace("'","") + "','" +
                        data[37].trim().replace("'","") + "','" +
                        data[38].trim().replace("'","") + "','" +
                        data[39].trim().replace("'","") + "','" +
                        data[40].trim().replace("'","") + "','" +
                        data[41].trim().replace("'","") + "','" +
                        data[42].trim().replace("'","") + "','" +
                        data[43].trim().replace("'","") + "','" +
                        data[44].trim().replace("'","") + "','" +
                        data[45].trim().replace("'","") + "','" +
                        data[46].trim().replace("'","") + "','" +
                        data[47].trim().replace("'","") + "','" +
                        data[48].trim().replace("'","") + "','" +
                        data[49].trim().replace("'","") + "','" +
                        data[50].trim().replace("'","") + "','" +
                        data[51].trim().replace("'","") + "','" +
                        data[52].trim().replace("'","") + "','" +
                        data[53].trim().replace("'","") + "','" +
                        "0"+ "','" +
                        "0"+ "','" +
                        data[56].trim().replace("'","") + "','" +
                        data[57].trim().replace("'","") + "','" +
                        "0"+ "','" +
                        data[59].trim().replace("'","") + "','" +
                        data[60].trim().replace("'","") + "','" +
                        data[61].trim().replace("'","") + "','" +
                        data[62].trim().replace("'","") + "','" +
                        data[63].trim().replace("'","") + "','" +
                        data[64].trim().replace("'","") + "','" +
                        data[65].trim().replace("'","") + "','" +
                        data[66].trim().replace("'","") + "','" +
                        data[67].trim().replace("'","") + "','" +
                        data[68].trim().replace("'","") + "','" +*/
                db.execSQL(Instval);
            }
            myReader.close();
            db.close();
//            deleteFiles("/sdcard/document.decrypted");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        showMessage("Data Loading", "Completed");
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

    public String AltDialog(String title,String message, String btnPositive,String btnNegetive, final JSONArray Data)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                retStr="GPRSYES";
//                overwritedata_db();
               /* overwrite_data_rest(Data);
                dialog.dismiss();*/
                dialog.dismiss();
                LocalData_Async localData_async = new LocalData_Async();
                localData_async.execute();
            }
        });
        builder.setNegativeButton(btnNegetive, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                retStr="GPRSNO";
                // Do nothing
                dialog.dismiss();
                LocalData_Async localData_async = new LocalData_Async();
                localData_async.execute();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return retStr;
    }

    public void overwrite_data_rest(JSONArray Data)
    {
        JSONArray aDataRow = Data;
        String retVal;
        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        db.delete("pctosbm",null,null);
        db.delete("sbmtopc",null,null);
        db.close();
        pctosbm2(Data);
        String sql="";

        ///Mask these line after testing completion
        /*sql="Update pctosbm set meter_status=''";
        db.execSQL(sql);*/
        ///Mask these line after testing completion
//        showMessage("Data Loading", "Completed");
    }

    public void append_data_rest(JSONArray Data)
    {
        JSONArray aDataRow = Data;
        String retVal;
        /*db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        db.delete("pctosbm",null,null);
        db.delete("sbmtopc",null,null);
        db.close();*/
        pctosbm2(Data);
        String sql="";

        ///Mask these line after testing completion
        /*sql="Update pctosbm set meter_status=''";
        db.execSQL(sql);*/
        ///Mask these line after testing completion
//        showMessage("Data Loading", "Completed");
    }

    public void pctosbm2(JSONArray Data)
    {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        String[] record2;

        for (int i = 0; i < Data.length(); i++) {
            try {
                JSONObject json_data = Data.getJSONObject(i);
                DBConnections dbConnections = new DBConnections(this);
                dbConnections.addrecord(json_data);
//                Log.e("Loading",record2.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String AltDialog_Local(String title,String message, String btnPositive,String btnNegetive,String Path)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                retStr="YES";
//                overwritedata_db();
                dialog.dismiss();
                LocalData_Async localData_async = new LocalData_Async();
                localData_async.execute();
            }
        });
        builder.setNegativeButton(btnNegetive, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                retStr="NO";
                // Do nothing
                /*appenddata_db();
                dialog.dismiss();*/
                dialog.dismiss();
                LocalData_Async localData_async = new LocalData_Async();
                localData_async.execute();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return retStr;
    }

    /*private void showProgressDialog() {
        if (this.sDialog == null) {
            this.sDialog = new SweetAlertDialog(this, 1);
            this.sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            this.sDialog.setTitleText("Loading");
            this.sDialog.setCancelable(false);
            this.sDialog.show();
        }
        this.sDialog.show();
    }
    private void dismissProgressDialog() {
        if (this.sDialog != null && this.sDialog.isShowing()) {
            this.sDialog.dismiss();
        }
    }
    protected void onDestroy() {
        dismissProgressDialog();
//        Debug.stopMethodTracing();
        super.onDestroy();
    }*/

    public class LocalData_Async extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog= new ProgressDialog(DataLoading.this);
        public LocalData_Async() {
            dialog.setMessage("Data Loading, please wait.");
            dialog.setTitle("Writing Data into DB");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }
        @Override
        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... args) {
            // do background work here
            if(retStr.equals("YES")) {
                overwritedata_db();
            }
            else if(retStr.equals("NO"))
            {
                appenddata_db();
            }
            else if(retStr.equals("GPRSYES"))
            {
                overwrite_data_rest(Data_Records);
            }
            else if(retStr.equals("GPRSNO"))
            {
                append_data_rest(Data_Records);
            }
            return null;
        }

        protected void onPostExecute(Void result) {
//            dialog.dismiss();
//            Toast.makeText(DataLoading.this,"Data Download Completed",Toast.LENGTH_SHORT).show();
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
//                Toast.makeText(DataLoading.this,"Data Download Completed",Toast.LENGTH_SHORT).show();
                showMessage("DataLoading","Completed");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode==RESULT_OK){

                    String PathHolder = data.getData().getPath();
                    dbFilepath=PathHolder;
                    Toast.makeText(this, PathHolder , Toast.LENGTH_LONG).show();
                    AltDialog_Local("Local Data Loading","Do you wish to Overwrite/Append?", "Overwrite","Append",PathHolder);

                }
                break;

        }
    }
}

/*private class Socketsend extends AsyncTask<Void, Integer, Void> {

        String dstAddress;
        int dstPort;
        String Filename;
        Socketsend(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
            Filename  = "D" + "$" + "D";
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Socket socket = null;
            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                socket = new Socket(dstAddress, dstPort);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                String number = Filename;
                String sendMessage = number + "\n";
                bw.write(sendMessage);
                bw.flush();
                byte[] mybytearray = new byte[FILE_SIZE];
                InputStream is = socket.getInputStream();
                fos = new FileOutputStream(FILE_TO_RECEIVED);
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray, 0, mybytearray.length);
                current = bytesRead;
//                Integer val=current;
//                Integer val2=bytesRead;
                do {
                    bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                    if (bytesRead >= 0) current += bytesRead;
                    publishProgress((current));
//                    progress_show();
//                    val=current;
//                    val2=bytesRead;
//                    Log.d("DownloadData1",(val.toString()));
//                    Log.d("DownloadData2",(val2.toString()));
                } while (bytesRead > -1);
                bos.write(mybytearray, 0, current);
                bos.flush();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bos != null) try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socket != null) try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Data","Done");
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer...Progress)
        {
            super.onProgressUpdate();
            setProgress(Progress[0]);

            pbar.setProgress(Progress[0]);
            txtpgs.setText(Progress[0].toString());

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            AltDialog("Data Download", "Do you wish to Overwrite/Append?", "Overwrite","Append");
        }


    }*/
