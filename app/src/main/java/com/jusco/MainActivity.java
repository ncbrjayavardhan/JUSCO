package com.jusco;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;*/
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends AppCompatActivity {
    ActionBar actionbar;
    TextView tvAppname;
    LayoutParams layoutparams;

    public static  String SERVER_IP="124.123.41.255";//"192.168.1.12";
    RelativeLayout relativeLayout;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    int timeflag = 0;
    File EOD_File;
    private Uri outputFileUri;
    public static String Version;
    String ret;
    String result;
    public static String DBP;
    String LoginType = "";
    public static String Language = "E";
    public static String ServerIP = "";
    public static String ServerPort = "";
    public static String ServerPort3 = "";
    public static String Meter_Reader_Name = "";
    TextView response;
    String resp;
    //    TextView response;
    TextView txtDiscom, txtWork;
    Button btnLogin, btnExit;
    EditText etPassword, etUsname;
    SQLiteDatabase db;
    int chkeod;
    public static String DBNAME = "";
    public static String MacID, MacID2;
    public static String Login_name;
    private Context context;
    private Properties properties;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DBNAME = getString(R.string.DBNAME);
        ServerIP = getString(R.string.IPADDR);
        ServerPort = getString(R.string.PORT);
        ServerPort3 = getString(R.string.PORT);
        relativeLayout=findViewById(R.id.relMain);

        /*AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();*/
//        relativeLayout.getBackground().setAlpha(50);
        context = this;
        /*ActionBarTitleGravity();*/
        checkAndRequestPermissions();

        TelephonyManager telephonyManager1 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        check_self_permissions_Phone();
        MacID2 = telephonyManager1.getDeviceId(1);
        MacID = telephonyManager1.getDeviceId(2);

        Log.d("IMEI-1", MacID);
        Log.d("IMEI-2", MacID2);

        read_ipaddress();
        txtDiscom = (TextView) findViewById(R.id.DiscomName);
        txtWork = (TextView) findViewById(R.id.worktype);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnLogin = (Button) findViewById(R.id.btnSubmit);
        etUsname = (EditText) findViewById(R.id.etUser);
        TextView lbl1 = (TextView) findViewById(R.id.textView);



//        Bitmap newImageBitmap = addWaterMark(getResources().getResourceName(R.drawable.company_logo));
//        ImageView imageView = (ImageView) findViewById(R.id.imgwcomplogo);
//        imageView.setImageBitmap(newImageBitmap);

        Version = getString(R.string.APPVERSION);
        /*Version = getString(R.string.APPVERSION);*/
        String FilePath = Environment.getExternalStorageDirectory().getPath();
        EOD_File = new File(FilePath, "/JUSCO/Mob2PC/Login_FILE.txt");
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        etPassword = (EditText) findViewById(R.id.etPassword);
        lbl1.setText(Version);
        File f = new File(Environment.getExternalStorageDirectory().toString() + "/JUSCO/Mob2PC");
        f.mkdirs();
        File f2 = new File(Environment.getExternalStorageDirectory().toString() + "/JUSCO/PC2Mob");
        f2.mkdirs();
         f = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures");
        f.mkdirs();
         f = new File(Environment.getExternalStorageDirectory().toString() + "/CROP_IMAGES");
        f.mkdirs();


        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return false;
                if(keyCode == KeyEvent.KEYCODE_ENTER ){
                    login_event();
                    return true;
                }
                return false;
            }
        });
//        try {
//            ServerIP= Util.getProperty("ipaddress",getApplicationContext());
//            ServerPort= Util.getProperty("port",getApplicationContext());
////            btnok.setText(Util.getProperty("ok",getApplicationContext()));
//            Log.d("Server",ServerIP);
//            Log.d("Server",ServerPort);
//            Toast.makeText(this,ServerIP,Toast.LENGTH_SHORT);
//            Toast.makeText(this,ServerPort,Toast.LENGTH_SHORT);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//1001391295,1111391947
        create_db();
//        write_mr_details();
        try {
            setMobileNetworkfromLollipop(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*chkeod = checkEOD();
        if (chkeod == 1) {

        } else {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            String date = sdf.format(new Date());
            SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
            String time = sdf2.format(new Date());
            String currentDBPath = "/data/" + "com.jusco" + "/databases/" + DBNAME;

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String backupDBPath = "DMP_" + date + "_" + time;//+ telephonyManager.getDeviceId();

            DBP = backupDBPath;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            /*Client1 myClient = new Client1(ServerIP, Integer.parseInt(ServerPort3), response);
            myClient.execute();*/

            /*SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
            String date2 = sdf3.format(new Date());

            write_eod(date2);



            Toast.makeText(this, "Finished", Toast.LENGTH_LONG).show();*/

//        }

    }

    public void Exit_Process(View v) {
        onBackPressed();
    }


    public void Validate_login(View view) {

        login_event();

    }
    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public int checkEOD() {
        int check = -1;
        String aDataRow = null;
        try {
            File myFile = new File("/sdcard/JUSCO/Mob2PC/Login_FILE.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader2 = new BufferedReader(
                    new InputStreamReader(fIn));
            while ((aDataRow = myReader2.readLine()) != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String date = sdf.format(new Date());
                if (aDataRow.equals(date)) {
                    check = 1;
                } else {
                    check = 0;
                }
            }
            myReader2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return check;
    }

    public void Move_Process(View view) {
        Read_MobileNumber();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void Read_MobileNumber() {
        TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

//        String Mobile_no = telephonyService.getSimOperator();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String Mobile_no = telephonyService.getLine1Number();
        Toast.makeText(this, Mobile_no, Toast.LENGTH_LONG).show();
    }

    //    public void Read_SimNumber()
//    {
//        TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String SIM_slno=telephonyService.getSimSerialNumber();
//        //        Toast.makeText(this,SIM_slno,Toast.LENGTH_LONG).show();
//    }
    public void setMobileDataState(boolean mobileDataEnabled) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        } catch (Exception ex) {
            Log.e("ERROR", "Error setting mobile data state", ex);
        }
    }

    public boolean getMobileDataState() {
        try {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);
                return mobileDataEnabled;
            }
        } catch (Exception ex) {
            Log.e("ERROR", "Error getting mobile data state", ex);
        }
        return false;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean getMobileDataEnabled(Context context) {
        try {
            ConnectivityManager mcm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class ownerClass = mcm.getClass();
            Method method = ownerClass.getMethod("getMobileDataEnabled");
            return (Boolean) method.invoke(mcm);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    void enableInternet(boolean yes) {
        ConnectivityManager iMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Method iMthd = null;
        try {
            iMthd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (Exception e) {
        }
        iMthd.setAccessible(false);

        if (yes) {

            try {
                iMthd.invoke(iMgr, true);
                Toast.makeText(getApplicationContext(), "Data connection Enabled", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                //dataButton.setChecked(false);
                Toast.makeText(getApplicationContext(), "IllegalArgumentException", Toast.LENGTH_SHORT).show();

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), "IllegalAccessException", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                //dataButton.setChecked(false);
                Toast.makeText(getApplicationContext(), "InvocationTargetException", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                iMthd.invoke(iMgr, false);
                Toast.makeText(getApplicationContext(), "Data connection Disabled", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //dataButton.setChecked(true);
                Toast.makeText(getApplicationContext(), "Error Disabling Data connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setMobileNetworkfromLollipop(Context context) throws Exception {
        String command = null;
        int state = 0;
        try {
            // Get the current state of the mobile network.
            state = isMobileDataEnabledFromLollipop(context) ? 0 : 1;
            // Get the value of the "TRANSACTION_setDataEnabled" field.
            String transactionCode = getTransactionCode(context);
            // Android 5.1+ (API 22) and later.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                SubscriptionManager mSubscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                // Loop through the subscription list i.e. SIM list.
                for (int i = 0; i < mSubscriptionManager.getActiveSubscriptionInfoCountMax(); i++) {
                    if (transactionCode != null && transactionCode.length() > 0) {
                        // Get the active subscription ID for a given SIM card.
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        int subscriptionId = mSubscriptionManager.getActiveSubscriptionInfoList().get(i).getSubscriptionId();
                        // Execute the command via `su` to turn off
                        // mobile network for a subscription service.
                        command = "service call phone " + transactionCode + " i32 " + subscriptionId + " i32 " + state;
                        executeCommandViaSu(context, "-c", command);
                    }
                }
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                // Android 5.0 (API 21) only.
                if (transactionCode != null && transactionCode.length() > 0) {
                    // Execute the command via `su` to turn off mobile network.
                    command = "service call phone " + transactionCode + " i32 " + state;
                    executeCommandViaSu(context, "-c", command);
                }
            }
        } catch(Exception e) {
            // Oops! Something went wrong, so we throw the exception here.
            throw e;
        }

    }
    private static void executeCommandViaSu(Context context, String option, String command) {
        boolean success = false;
        String su = "su";
        for (int i=0; i < 3; i++) {
            // Default "su" command executed successfully, then quit.
            if (success) {
                break;
            }
            // Else, execute other "su" commands.
            if (i == 1) {
                su = "/system/xbin/su";
            } else if (i == 2) {
                su = "/system/bin/su";
            }
            try {
                // Execute command as "su".
                Runtime.getRuntime().exec(new String[]{su, option, command});
            } catch (IOException e) {
                success = false;
                // Oops! Cannot execute `su` for some reason.
                // Log error here.
            } finally {
                success = true;
            }
        }
    }

    //    To check if the mobile network is enabled or not:
    private static boolean isMobileDataEnabledFromLollipop(Context context) {
        boolean state = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
        }
        return state;
    }
    //    To get the value of the "TRANSACTION_setDataEnabled" transaction field dynamically:
    private static String getTransactionCode(Context context) throws Exception {
        try {
            final TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final Class<?> mTelephonyClass = Class.forName(mTelephonyManager.getClass().getName());
            final Method mTelephonyMethod = mTelephonyClass.getDeclaredMethod("getITelephony");
            mTelephonyMethod.setAccessible(true);
            final Object mTelephonyStub = mTelephonyMethod.invoke(mTelephonyManager);
            final Class<?> mTelephonyStubClass = Class.forName(mTelephonyStub.getClass().getName());
            final Class<?> mClass = mTelephonyStubClass.getDeclaringClass();
            final Field field = mClass.getDeclaredField("TRANSACTION_setDataEnabled");
            field.setAccessible(true);
            return String.valueOf(field.getInt(null));
        } catch (Exception e) {
            // The "TRANSACTION_setDataEnabled" field is not available,
            // or named differently in the current API level, so we throw
            // an exception and inform users that the method is not available.
            throw e;
        }
    }

    public void create_db()
    {
        SQLiteDatabase db;
        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pctosbm(" +
                "MRU varchar2(10),\n"+
                "Portion varchar2(10),\n"+
                "Schedule_MR_date varchar2(30),\n"+
                "Meter_Serial_No varchar2(10),\n"+
                "Meter_Phase varchar2(10),\n"+
                "BP_No varchar2(10),\n"+
                "BPNAME varchar2(30),\n"+
                "Sanction_Load varchar2(10),\n"+
                "UOM varchar2(10),\n"+
                "Address1 varchar2(30),\n"+
                "Address2 varchar2(30),\n"+
                "Address3 varchar2(30),\n"+
                "Mob_No varchar2(10),\n"+
                "CON_DATE varchar2(10),\n"+
                "DL_COUNTER varchar2(10),\n"+
                "cons1 varchar2(10),\n"+
                "cons2 varchar2(10),\n"+
                "cons3 varchar2(10),\n"+
                "cons4 varchar2(10),\n"+
                "cons5 varchar2(10),\n"+
                "cons6 varchar2(10),\n"+
                "CATEGORY varchar2(15),\n"+
                "PRE_READ varchar2(10),\n"+
                "MF varchar2(10),\n"+
                "MRNOTE varchar2(15),\n"+
                "E_DUTY varchar2(20),\n"+
                "kwh_Adjustment varchar2(10),\n"+
                "ED_adjustment varchar2(10),\n"+
                "METER_RENT varchar2(10),\n"+
                "FIXEDCHARGE varchar2(10),\n"+
                "LPC varchar2(10),\n"+
                "INTERESTOnSD varchar2(10),\n"+
                "TDS varchar2(10),\n"+
                "RebateEarly varchar2(10),\n"+
                "RebateDigital varchar2(10),\n"+
                "OTHERRECEIVABLE varchar2(10),\n"+
                "PREVIOUSOS varchar2(10),\n"+
                "FPPPACharg varchar2(10));");

//        "New_mtr_initial_rdg varchar2(20));");
//                "Cons6 varchar(10));");//28


        db.execSQL("CREATE TABLE IF NOT EXISTS sbmtopc(" +//1
                "BP_No varchar2(20),\n"+
                "Meter_Serial_No varchar2(10),\n"+
                "CURR_READING varchar2(20),\n"+
                "MRNOTE varchar2(20),\n"+
                "BILLED_UNITS varchar2(20),\n"+
                "NETUNITS varchar2(20),\n"+
                "ENERGY_CHARGE varchar2(20),\n"+
                "E_DUTY varchar2(20),\n"+
                "METER_RENT varchar2(20),\n"+
                "FIXEDCHARGE varchar2(20),\n"+
                "LPC varchar2(20),\n"+
                "INTERESTONSD varchar2(20),\n"+
                "TDS varchar2(20),\n"+
                "RebateEarly varchar2(20),\n"+
                "RebateDigital varchar2(20),\n"+
                "Rebate_total varchar2(20),\n"+
                "OTHERRECEIVABLE varchar2(20),\n"+
                "PREVIOUSOS varchar2(20),\n"+
                "FPPPACharg varchar2(20),\n"+
                "Bill_Amt varchar2(20),\n"+
                "TOTAL varchar2(20),\n"+
                "SBM_NO varchar2(20),\n"+
                "LAT varchar2(20),\n"+
                "LONG varchar2(20),\n"+
                "SBM_Sw_Ver varchar2(20),\n"+
                "BILL_NO varchar2(20),\n"+
                "Bill_Date varchar2(20),\n"+
                "Bill_Time varchar2(20),\n"+
                "Cons_Mob_No varchar2(20),\n"+
                "Cur_Mtr_Sts varchar2(20),\n"+
                "Prev_Read varchar2(20),\n"+
                "Pres_Read_KW_RMD varchar2(20),\n"+
                "Cur_PF varchar2(20),\n"+
                "Bill_Due_date varchar2(20),\n"+
                "Round_Off_amount varchar2(20),\n"+
                "Bill_Net_within_due_date varchar2(20),\n"+
                "Bill_Amount_After_Due_Date varchar2(20),\n"+
                "Bill_Generation_Status varchar2(20),\n"+
                "Meter_Reader_Name varchar2(20),\n"+
                "Obr_Code varchar2(20),\n"+
                "Online_Flag_number varchar2(20),\n"+
                "cat_tarriff varchar2(20),\n"+
                "MRU varchar2(20),\n"+
                "Portion varchar2(20),\n"+
                "Rdg_img_Path varchar2(20));");


//                "Longitude    varchar(50));");//40

        String CREATE_LOGIN_TABLE="CREATE TABLE IF NOT EXISTS  user_det_table (" +
                "USER_ID         varchar(10),\n" +
                "USER_PWD        varchar(8),\n" +
                "USER_NAME        varchar(50),\n" +
                "USER_TYPE        varchar(15))";
        db.execSQL(CREATE_LOGIN_TABLE);
        db.close();
    }

    public void write_mr_details()
    {
        String sql="delete from user_det_table";
        SQLiteDatabase db;
        db = openOrCreateDatabase("JUSCODB", Context.MODE_PRIVATE, null);
        db.execSQL(sql);

        sql="Insert into user_det_table values('admin','1611','jayavardhan','admin')";
        db.execSQL(sql);
        sql="Insert into user_det_table values('user','1111','sairam','mr')";
        db.execSQL(sql);
        sql="Insert into user_det_table values('ad11','1511','Shiva','admin')";
        db.execSQL(sql);
        sql="Insert into user_det_table values('use1','1511','kishore','mr')";
        db.execSQL(sql);
        db.close();

    }


    public void write_eod(String dt)
    {
        File eodfile;
        String FilePath = Environment.getExternalStorageDirectory().getPath();
        eodfile = new File(FilePath, "/CSPDCL/Mob2PC/Login_FILE.txt");
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
    //    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if(item.equals("Meter Reader"))
        {
            LoginType="M";
        }
        else if(item.equals("Supervisor"))
        {
            LoginType="S";
        }
        else
        {
            LoginType="";
        }
        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + LoginType, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        LoginType="";
    }

    private class Login_Client extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        TextView textResponse;
        String Filename;
        String Dataform;


        Login_Client(String addr, int port, TextView txtResponse, String textResponse, String Data) {
            dstAddress = addr;
            dstPort = port;
            this.textResponse = txtResponse;
            Filename = textResponse;
            Dataform = Data;

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                if (Filename.equals("LM")) {
                    String number = Filename + "," + Dataform;
                    String sendMessage = number + "\n";
                    bw.write(sendMessage);
                    bw.flush();

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    response = message;
                    System.out.println("Message received from the server : " + message);
                }


                if (Filename.equals("LS")) {
                    String number = Filename + "," + Dataform;
                    String sendMessage = number + "\n";
                    bw.write(sendMessage);
                    bw.flush();

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    response = message;
                    System.out.println("Message received from the server : " + message);

                }
                if (Filename.equals("NS")) {
                    String number = Filename + "," + Dataform;
                    String sendMessage = number + "\n";
                    bw.write(sendMessage);
                    bw.flush();

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    response = message;
                    System.out.println("Message received from the server : " + message);

                }
                if (Filename.equals("NM")) {
                    String number = Filename + "," + Dataform;
                    String sendMessage = number + "\n";
                    bw.write(sendMessage);
                    bw.flush();

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    response = message;
                    System.out.println("Message received from the server : " + message);

                }
                /*
                 * notice: inputStream.read() will block if no data return
                 */
                else if (Filename.equals("F")) {
                    String number = "F";
                    String sendMessage = number + "\n";
                    bw.write(sendMessage);
                    bw.flush();

                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    response = message;
                    System.out.println("Message received from the server : " + message);

                  /*  if (message.equals("F")) {
                        number = MainActivity.DBP;
                        sendMessage = number + "\n";
                        bw.write(sendMessage);
                        bw.flush();
                    } else {
                        response = message;
                    }*/
                }

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
//            textResponse.setText(response);
            String Filename1 = response;
            String tem[]=Filename1.split(":");
            String LM=tem[0];
            String OTP=tem[1];

            try {
                if(LM.equals("LM")){

                    /*Intent i=new Intent(MainActivity.this,OtpActivity.class);

                    i.putExtra("OTP", OTP);

                    startActivity(i);*/


                } else if (LM.equals("LS")) {

                    /*Intent i=new Intent(MainActivity.this,OtpActivity.class);
                    i.putExtra("OTP", OTP);
                    startActivity(i);*/


                } else if (LM.equals("NS")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setMessage("Invalid Supervisor Authentication");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            etPassword.setText("");
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (LM.equals("NM")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setMessage("Invalid MR Authentication");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            etPassword.setText("");
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setMessage("Invalid Authentication");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            etPassword.setText("");
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }catch (NullPointerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("No Response from Server");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        etPassword.setText("");
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("No Response from Server");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        etPassword.setText("");
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            super.onPostExecute(result);
        }
    }
 /*   private class EOD_Client extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        TextView textResponse;
        String Filename;
        String Dataform;

        EOD_Client(String addr, int port, TextView txtResponse, String textResponse, String Data) {
            dstAddress = addr;
            dstPort = port;
            this.textResponse = txtResponse;
            Filename = textResponse;
            Dataform = Data;

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String scno;
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                Cursor c=null;


                db=openOrCreateDatabase("DVVNLDB", Context.MODE_PRIVATE,null);
                db.execSQL("Update sbmtopc set UFlag='0'");
                c=db.rawQuery("select * from sbmtopc where UFLAG='0'",null);
                if(c.moveToFirst())
                {
                    scno=c.getString(0);
                    do{
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
                        record += "$" + c.getString(57) + "$" + c.getString(59) + "$" + c.getString(60) + "$" + c.getString(61) + "$" + c.getString(62) + "$" + c.getString(63) + "$" + c.getString(64) + "$" + MainActivity.Version;
//                        send_sock();
                        send_rdg_image(c.getString(60));
                        send_md_image(c.getString(61));
//                        SocketSend();
                        String number = record + "$" + BWfilename.toString() + "$" + BWfilename2.toString() ;
                        String sendMessage = number;
                        bw.write(sendMessage);
                        bw.flush();
//                    System.out.println(c.getString(0));
//                Toast.makeText(this,c.getString(0),Toast.LENGTH_LONG).show();
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        db.execSQL("Update sbmtopc set UFlag='1' where scno='" + c.getString(0) + "' and BookNo='" + c.getString(1) + "'");
                    }while(c.moveToNext());
                }
                db.close();
//                String number = Filename ;
//                String sendMessage = number + "\n";
//                bw.write(sendMessage);
//                bw.flush();

//                    InputStream is = socket.getInputStream();
//                    InputStreamReader isr = new InputStreamReader(is);
//                    BufferedReader br = new BufferedReader(isr);
//                    String message = br.readLine();
//                    response = message;
//                    System.out.println("Message received from the server : " + message);

			/*
             * notice: inputStream.read() will block if no data return
			 */
/*            } catch (UnknownHostException e) {
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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            textResponse.setText(response);
            try {
            } catch (NullPointerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("No Response from Server");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        etPassword.setText("");
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("No Response from Server");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        etPassword.setText("");
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            super.onPostExecute(result);
        }
    }*/

    @Override
    protected void onStart() {

//        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                //Show Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Need Storage Permission");
//                builder.setMessage("This app needs storage permission.");
//                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else if (permissionStatus.getBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
//                //Previously Permission Request was cancelled with 'Dont Ask Again',
//                // Redirect to Settings after showing Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Need Storage Permission");
//                builder.setMessage("This app needs storage permission.");
//                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        sentToSettings = true;
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else {
//                //just request the permission
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
//            }
//
//            SharedPreferences.Editor editor = permissionStatus.edit();
//            editor.putBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
//            editor.commit();
//
//
//        } else {
//            //You already have the permission, just go ahead.
//            proceedAfterPermission();
//        }

        super.onStart();
    }
    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }
    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
    //     * @param permissions  The requested permissions. Never null.
    //     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
    //     * @see #requestPermissions(String[], int)
     */
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void notion(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

//    public static Bitmap mark(Bitmap src, String watermark, Point location, Color color, int alpha, int size, boolean underline) {
//        int w = src.getWidth();
//        int h = src.getHeight();
//        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
//
//        Canvas canvas = new Canvas(result);
//        canvas.drawBitmap(src, 0, 0, null);
//
//        Paint paint = new Paint();
//        paint.setColor(color);
//        paint.setAlpha(alpha);
//        paint.setTextSize(size);
//        paint.setAntiAlias(true);
//        paint.setUnderlineText(underline);
//        canvas.drawText(watermark, location.x, location.y, paint);
//
//        return result;
//    }

    private static Bitmap addWaterMark(String WatermarkPath) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 1;
        boolean imageSet = false;

        // TODO http://developer.android.com/training/displaying-bitmaps/load-bitmap.html


        while(!imageSet) {
            try {
                Bitmap PhotoBitmap = BitmapFactory.decodeFile(WatermarkPath, bitmapOptions);
                Bitmap WatermarkBitmap = BitmapFactory.decodeFile(WatermarkPath);

                int w = WatermarkBitmap.getWidth();
                int h = WatermarkBitmap.getHeight();

                Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);

                Canvas canvas = new Canvas(result);

                //canvas.drawBitmap(PhotoBitmap, 0, 0, null);

                canvas.drawBitmap(PhotoBitmap, new Rect(0, 0, PhotoBitmap.getWidth(), PhotoBitmap.getHeight()),
                        new Rect(0, 0, w, h), null);

                canvas.drawBitmap(WatermarkBitmap, 0, 0, null);

                imageSet = true;

                return result;
            } catch (OutOfMemoryError E) {
                bitmapOptions.inSampleSize *= 2;
            }
        }
        //TODO legenda
        //Paint paint = new Paint();
        //paint.setColor(Color.RED);
        //paint.setTextSize(18);
        //paint.setAntiAlias(true);
        //paint.setUnderlineText(true);
        //canvas.drawText(WatermarkText, 20, 25, paint);
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        /*if(id==R.id.English)
        {
            apply_English();
            return true;
        }*/

        /*if(id==R.id.Hindi)
        {
            apply_hindi();
            return true;
        }*/
        /*if(id==R.id.Telugu)
        {
            apply_Telugu();
            return true;
        }*/



        return super.onOptionsItemSelected(item);
    }

    public void apply_hindi()
    {
        Language="H";
        String languageToLoad = "hi"; // your language
        Locale  locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration   config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_main);

    }

    public void apply_English()
    {
        Language="E";
        String languageToLoad = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_main);


    }

    public void apply_Telugu()
    {
        Language="T";
        String languageToLoad = "te"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_main);


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
                SERVER_IP = aDataRow.substring(0, 15);
                break;
            }
            myReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        int storage = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        int storageread = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int loc = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION");
        int loc2 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION");
        int network = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_NETWORK_STATE");
        int phone = ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE");
        int bluetooth = ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH");
        int internet = ContextCompat.checkSelfPermission(this, "android.permission.INTERNET");
        List<String> listPermissionsNeeded = new ArrayList();
        if (camera != 0) {
            listPermissionsNeeded.add("android.permission.CAMERA");
        }
        if (storage != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (loc2 != 0) {
            listPermissionsNeeded.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (loc != 0) {
            listPermissionsNeeded.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (network != 0) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (storageread != 0) {
            listPermissionsNeeded.add("android.permission.ACCESS_NETWORK_STATE");
        }
        if (phone != 0) {
            listPermissionsNeeded.add("android.permission.READ_PHONE_STATE");
        }
        if (bluetooth != 0) {
            listPermissionsNeeded.add("android.permission.BLUETOOTH");
        }
        if (internet != 0) {
            listPermissionsNeeded.add("android.permission.INTERNET");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
//                Logger logger;
                if (grantResults.length <= 0 || grantResults[0] != 0) {
//                    logger = this.Log;
//                    Logger.m18d(getApplicationContext(), "yes", "no");
                    return;
                }
//
            default:
                return;
        }
    }

    public void login_event(){
        Context ctx=MainActivity.this;
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etUsername = findViewById(R.id.etUser);
        DBConnections dbConnections= new DBConnections(this);
        String[] Login_det=dbConnections.getLoginRecord(etUsername.getText().toString().trim(),etPassword.getText().toString().trim());
        if(Login_det[0].equals("0"))
        {
            result = RestServices.Login_service(context.getResources(), etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
            if (result.indexOf("$") >= 0) {
                String[] abc;
                abc = result.split("\\$");
                if (abc[0].equals("OK")) {
                    Login_name = abc[1];
                    if (abc[1].equals("admin")) {
                        dbConnections.add_login_record(etUsername.getText().toString().trim(),etPassword.getText().toString().trim(),"",abc[1]);
                        Intent intent = new Intent(this, AdminActivity.class);
                        startActivity(intent);
                    } else if (abc[1].equals("mr")) {
                        Meter_Reader_Name=abc[2];
                        dbConnections.add_login_record(etUsername.getText().toString().trim(),etPassword.getText().toString().trim(),abc[2],abc[1]);
                        Intent intent = new Intent(this, MRActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, MRActivity.class);
                        startActivity(intent);
                    }
                }
            }
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Incorrect Password");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        etPassword.setText("");
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
        else
        {
            if((etPassword.getText().toString().equals(Login_det[1]) && etUsername.getText().toString().equals(Login_det[0])) && Login_det[3].equals("admin"))
            {
                Login_name=Login_det[0];
                Meter_Reader_Name=Login_det[2];
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            }
            else if((etPassword.getText().toString().equals(Login_det[1]) && etUsername.getText().toString().equals(Login_det[0])) && Login_det[3].equals("mr"))
            {
                Meter_Reader_Name=Login_det[2];
                Login_name=Login_det[0];
//                Log.e("MR",Meter_Reader_Name);
                Intent intent = new Intent(this, MRActivity.class);
                startActivity(intent);
            }
        }
        /*if(etPassword.getText().toString().equals("1111"))
        {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        }
        else if(etPassword.getText().toString().equals("2222"))
        {
            Intent intent = new Intent(this, MRActivity.class);
            startActivity(intent);
        }*/

        /* if(result.length()>0) {
         *//* Intent intent = new Intent(this, MRActivity.class);
//            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);*//*
        }*/
//        if (etPassword.getText().toString().trim().equals("1611")) {
////            Intent intent = new Intent(this, ChequeCashCollection.class);
//            Intent intent = new Intent(this, MRActivity.class);
//            startActivity(intent);
//        } else if (etPassword.getText().toString().trim().equals("8152")) {
//            Intent intent = new Intent(this, AdminActivity.class);
//            startActivity(intent);
//        }
    }

    private void ActionBarTitleGravity() {
        // TODO Auto-generated method stub
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.actionbar = getActionBar();

        tvAppname = new TextView(getApplicationContext());

        layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        tvAppname.setLayoutParams(layoutparams);

        tvAppname.setText("ACTIONBAR");

        tvAppname.setTextColor(Color.BLACK);

        tvAppname.setGravity(Gravity.CENTER);

        tvAppname.setTextSize(20);

        this.actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        this.actionbar.setCustomView(tvAppname);

    }


}
