package com.jusco;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*import com.pixolus.meterreading.MeterReadingActivity;
import com.pixolus.meterreading.MeterReadingResult;*/
//import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.highgui.Highgui;

import com.jusco.juscoLogic.Cs_Rural;
import com.jusco.juscoLogic.Cs_Urban;
import com.jusco.juscoLogic.Ds_HT;
import com.jusco.juscoLogic.Ds_Rural;
import com.jusco.juscoLogic.Ds_Urban;
import com.lvrenyang.io.BTPrinting;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class EntryActivity extends AppCompatActivity {
    CameraCharacteristics cameraCharacteristics;
    public static String retStr = "";
    private static BTPrinting bt = null;
    private static String SERVER_IP = "124.123.41.255";//"192.168.1.12";
    //    private static final String SERVER_IP=MainActivity.ServerIP;
//    private static final int SERVERPORT = 25000;
    private static int SERVERPORT = 25000;
    //    private static final int SERVERPORT = Integer.parseInt(MainActivity.ServerPort);
    public static int count = 0;
    public static String print_cat = "";
    public static String IntentFlag = " ";
    private Uri outputFileUri;
    static final int CAM_REQUEST = 1337;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File myFile;
    public static String upFlag;
    SQLiteDatabase db;
    public static String res1 = "";
    public static String Str1 = "S";
    public static String Load;
    public static String DBNAME = "JUSCODB";
    public static int Flag_KW = 0;
    TextView txtsts, txtRead, txtobc, txtcld, txtTelnum, txtsid, txtCurPF;
    public static Double ec = 0.00;
    public static Double t_ec = 0.00;
    public static Double tot_fc = 0.00;
    public static Double nor_fc = 0.00;
    public static Double penal_fc = 0.00;
    public static Double penal_ec = 0.00;
    public static Double Nor_ec = 0.00;
    public static String CurStatus;
    public static Integer BMnths = 1;
    public static Long BUnits = 0L;
    public static Integer NrCnt = 0;
    public static Integer MF = 1;
    public static Double unitsm = 0.00d;
    public static Double calCmd = 0.00;
    public static Integer Cat = 4;
    public static Double AssdUnits = 0.00;
    public static Double AdjAmnt = 0.00;
    public static Double curread = 0.00d;
    public static Double FPPPAChrg=0.00d;
    public static Double EnrgyChrg = 0.00;
    public static Double CurLoad = 0.00;
    //public static //Double Sload=1.00;
    public static Double FixedChrg = 0.00;
    public static Double ED = 0.00;
    public static Double NetAmntExArr = 0.00;
    public static int captureFlag = 0;
    public static int captureMDFlag = 0;
    public static int capturePRFlag = 0;
    public static Double MinChrg = 0.00;
    public static Double Regulatory_ch = 0.00;
    public static Double Temp_Ec = 0.00;
    public static Double Temp_Fc = 0.00;
    public static String EmpFlag = "";
    public static Double CapChrg = 0.00;
    public static Double CurPF = 0.00;
    public static Double CurCMD = 0.0;
    public static Double NetAmntInArr = 0.00;
    public static long LAmntInArr = 0;
    public static Double ArrNR = 0.00;
    public static Double CurIntrest = 0.00;
    public static Double calsur = 0.00;
    public static Integer templg = 0;
    public static Double tempgl = 0.00;
    public static Double BillLG = 0.00;
    public static Double fRebateB_amt = 0.00;
    public static Double ExtraDemand = 0.00;
    public static Double Arrear = 0.00;
    public static Long days = 0L;
    public static String Duedt = "";
    public static String Duedt_Chq = "";
    public static String Discdt = "";
    public static String mCurrentPhotoPath = "";
    public static String CMD = "1";
    public static String Billdate = "";
    public static String Billtime = "";
    public static String MnthMin = "1";
    public static String BillNo = "1";
    public static String ObrCode = "";
    public static String SBA_ID = "";
    public static String Telno = "";
    public static String MacID = "";
    public static String record = "";
    public static String imageFileName = "";
    public static String imageFileName2 = "";
    public static String imageFileName3 = "";
    public static String grayfilename="";
    public static EditText etSBAID;
    public static EditText etObcode;
    public static EditText etCload;
    public static TextView txtCLoad;
    public static TextView txtObcode;
    public static TextView txtSBAID;
    public static TextView tvCmd;
    public static TextView txtCCMD;
    public static TextView txtpf;
    public static TextView ttCurPF;
    public static TextView etCurPF;
    public static TextView txtReading;
    public static String BWfilename2 = "";
    public static String BWfilename = "";
    public static int capture_mtr_flag = 0;
    public static int capture_md_flag = 0;
    public static Integer cur_bill_month = 0;
    public static Integer cur_bill_year = 0;
    public static String Reading = "";
    public static Double pfs = 0.00;
    public static Double VCA = 0.00;
    public static Double Cess = 0.00;
    public static Double rebate_Ec = 0.00;
    public static Double rebate_FC = 0.00;
    public static Double rebate_VCA = 0.00;
    public static Double rebate_Duty = 0.00;
    public static Double rebate_val = 0.00;
    public static Double rebate_val_25 = 0.00;
    public static Double DLAdjAmnt = 0.00;
    public static Double penalcharge = 0.00;
    public static Double LTWTCS_chrg = 0.00;
    public static Double WTCS_chrg = 0.00;
    public static Double grossAmt = 0.00;
    public static Double cur_Surcharge = 0.00;
    public static String obr_remarks = "";
    public Double disc_amt = 0.00;
    ImageView imgMDcapt, imgRdgcapt;
    public static Spinner spStatus;
    public static Spinner spObr;
    public static Integer dc_min_flag = 0;
    public static Integer fix_min_flag = 0;
    Button btnIssue;
    EditText etReading, SBAID, etTelephone;
    String response = "";
    Context context;
    TextView txtMsg;
    public static Integer enter_flag = 0;
    public static Double fix_chrg_min = 0.00;
    public static Double sg_rebate = 0.00;
    public static Long sg_rebate_unit = 0L;
    public static Double sg_rebate_EC = 0.00;
    public static Double sg_rebate_FC = 0.00;
    public static Double sg_rebate_vca = 0.00;
    public static String bill_Cycle_date="";
    public static String bill_Cycle_todate="";
    Double RMD = 0.00;
    Double Normal_units = 0.00;
    Double penal_units = 0.00;
    Double penal_overshoot = 0.00;
    Double penal_factor = 0.00;
    Double t_Vca = 0.00;
    Double penal_vca = 0.00;
    Double normal_vca = 0.00;
    public static Double cat_tariff=0.00;
    Integer prev_sts=0;
    Double reb_early=0.00;
    Double reb_dig=0.00;
    Double cons1=0.00;
    Double cons2=0.00;
    Double cons3=0.00;
    Double cons4=0.00;
    Double cons5=0.00;
    Double cons6=0.00;
    Double avg_6_cons=0.00;
    public static Double Billed_units=0.00;



    final int PIC_CROP = 2;
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;


    public static String strReadingFlag = "";
    String[] Obr_Code = {"--Please Select--",
            "Condition Ok",
            "Meter Indoor",
            "Meter digit not clear",
            "Meter on Height",
            "Temporary Disconnected but not updated in database / Line DC",
            "Permanent Disconnected but not updated in database",
            "Meter Tampered",
            "Meter Seal Broken",
            "House Locked",
            "House not found",
            "Meter Defective",
            "Permanent Disconnected but not updated in database",
            "Temporary Disconnected but not updated in database / Line DC",
            "Meter Tampered",
            "Meter Display Faulty",
            "Meter Burnt",
            "Previous reading is higher than Current reading",
            "Un Metered",
            "Meter Bypassed"};
    public static uploadRec red = new uploadRec();
    String[] Status = {"*-Please select",
            "0-OK MR",//0
            "1-DoorLock",//1
            "2-Defective Device",//2
            "3-Abnormal Reading",//3
            "4-Meter Missing",//4
            "5-No Response",//5
            "6-Recheck Required",//6
            "7-Meter Tampered",//7
            "9-Disconnected Reading"};//8

    String[] sts0 = {Status[0], Status[1], Status[2], Status[3], Status[4], Status[5], Status[6], Status[7], Status[8]};//,Status[13]

    String[] obr0 = {Obr_Code[0], Obr_Code[1], Obr_Code[2], Obr_Code[3], Obr_Code[4], Obr_Code[5], Obr_Code[6], Obr_Code[7], Obr_Code[8]};
    String[] obr3 = {Obr_Code[0], Obr_Code[9], Obr_Code[10]};
    String[] obrASu = {Obr_Code[0], Obr_Code[11], Obr_Code[12], Obr_Code[13], Obr_Code[14], Obr_Code[15], Obr_Code[16], Obr_Code[17], Obr_Code[18], Obr_Code[19]};
    public static String[] Bill_Base = {"OK MR", "Door Lock", "Defective Device", "Abnormal Reading", "Meter Missing", "No Response", "Recheck Required", "Meter Tampered", "Disconnected Reading"};


    Integer[] Duty_per = {0, 3, 3, 8, 12, 0, 0, 0, 3, 0, 3, 0, 0, 40, 0, 3, 4, 5, 6, 10, 12};

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private Socket client;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Date Currdt = new Date();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        DBNAME = getResources().getString(R.string.DBNAME);
        context = this;
        txtsts = (TextView) findViewById(R.id.txtstatus);
        txtMsg = findViewById(R.id.txtMessage);
        txtRead = (TextView) findViewById(R.id.txtEread);
        txtobc = (TextView) findViewById(R.id.txtObr);
        txtcld = (TextView) findViewById(R.id.txtEcLoad);
        txtTelnum = (TextView) findViewById(R.id.txtETelno);
        txtsid = (TextView) findViewById(R.id.txtSBAID);
        txtCurPF = (TextView) findViewById(R.id.ttCurPF);
        btnIssue = (Button) findViewById(R.id.btnIB);
        spStatus = findViewById(R.id.spStatus);
        spObr = findViewById(R.id.spRemarks);
        /*cur_bill_month=Integer.parseInt(ConsFetchActivity.sbmRec.getBill_Month())+1;
        cur_bill_year=Integer.parseInt(ConsFetchActivity.sbmRec.getBill_Year());*/
        IntentFlag = "E";
        captureFlag = 0;
        capturePRFlag = 0;
        ExtraDemand = 0.00;
        fix_min_flag = 0;
        imgMDcapt = (ImageView) findViewById(R.id.imgMD);
        imgRdgcapt = (ImageView) findViewById(R.id.imgRdg);
        imgMDcapt.setVisibility(View.INVISIBLE);
        etSBAID = findViewById(R.id.etSBAID);
        txtsid.setVisibility(View.INVISIBLE);
        etSBAID.setVisibility(View.INVISIBLE);
        btnIssue.setEnabled(true);
        btnIssue.setVisibility(View.VISIBLE);
        ttCurPF = findViewById(R.id.ttCurPF);
        etCurPF = findViewById(R.id.etCurPF);
        etCload = findViewById(R.id.etCLoad);
        cat_tariff=0.00;
        grayfilename ="/storage/emulated/0/DCIM/Camera/grayfname.jpg";
        bill_Cycle_date=ConsFetchActivity.sbmRec.getSchedule_MR_Date();//Bill date from sch as per uma
//        bill_Cycle_todate=ConsFetchActivity.sbmRec.getSchedule_MR_Date();
        prev_sts=Integer.parseInt(ConsFetchActivity.sbmRec.getMRNOTE());
        if(bill_Cycle_date.contains("/")) {
            bill_Cycle_date = bill_Cycle_date.replace("/", "-");
        }

        /*Date d=new Date();
        SimpleDateFormat sdf_smrdt = new SimpleDateFormat("yyyyMMdd");
        try {
             d = sdf_smrdt.parse(ConsFetchActivity.sbmRec.getSchedule_MR_Date());
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        sdf_smrdt.applyPattern("dd/MM/yyyy");
        System.out.println(sdf_smrdt.format(d));
        bill_Cycle_todate=sdf_smrdt.format(d).toString();
        bill_Cycle_date=bill_Cycle_todate.substring(0,2) + "/" + String.valueOf(Integer.parseInt(bill_Cycle_todate.substring(3,5))-1)+"/" +bill_Cycle_todate.substring(6,10);*/
//        Calc_from_date(30);
        Calc_from_date(30);
//        bill_Cycle_todate=bill_Cycle_date;
cons1=Double.parseDouble(ConsFetchActivity.sbmRec.getCons1());
cons2=Double.parseDouble(ConsFetchActivity.sbmRec.getCons2());
cons3=Double.parseDouble(ConsFetchActivity.sbmRec.getCons3());
cons4=Double.parseDouble(ConsFetchActivity.sbmRec.getCons4());
cons5=Double.parseDouble(ConsFetchActivity.sbmRec.getCons5());
cons6=Double.parseDouble(ConsFetchActivity.sbmRec.getCons6());
        avg_6_cons=(cons1+cons2+cons3+cons4+cons5+cons6)/6;
        ArrayAdapter aa;
        //Creating the ArrayAdapter instance having the country list
        switch (prev_sts.toString()) {
            case "0":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "3":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "4":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "5":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "6":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "7":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "9":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "10":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "11":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "12":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "13":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "14":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "15":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            case "16":
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sts0);
                break;
            default:
                aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Status);
                break;

        }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spStatus.setAdapter(aa);


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
        MainActivity.MacID2 = telephonyManager1.getDeviceId(1);
        MainActivity.MacID = telephonyManager1.getDeviceId(2);
        MainActivity.Version = getString(R.string.APPVERSION);
//        get_meter_reader_details();


        //MacID = Build.SERIAL;
        final EditText etTel = (EditText) findViewById(R.id.etTelno);
        final EditText etSts = (EditText) findViewById(R.id.etStatus);
        final EditText etRead = (EditText) findViewById(R.id.etReading);
        etSBAID = (EditText) findViewById(R.id.etSBAID);
        etObcode = (EditText) findViewById(R.id.etObcode);
        txtObcode = (TextView) findViewById(R.id.txtObcode);
        txtSBAID = (TextView) findViewById(R.id.txtSBAID);
        txtReading = (TextView) findViewById(R.id.txtReading);
        txtCLoad = (TextView) findViewById(R.id.txtCLoad);
        etCload = (EditText) findViewById(R.id.etCLoad);
        ttCurPF = (TextView) findViewById(R.id.ttCurPF);
        txtpf = (TextView) findViewById(R.id.txtpf);
        txtCCMD = (TextView) findViewById(R.id.txtCCMD);
        tvCmd = (TextView) findViewById(R.id.tvCmd);
        if (ConsFetchActivity.sbmRec.getMobile_Number() != null) {
            etTel.setText(ConsFetchActivity.sbmRec.getMobile_Number());
        }
        String FilePath = Environment.getExternalStorageDirectory().getPath();
        myFile = new File(FilePath, "/Jusco/Mob2PC/Data.txt");
        TextView tvcmd = (TextView) findViewById(R.id.tvCmd);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());
        Billdate = date;
        SimpleDateFormat sdt = new SimpleDateFormat("hh:mm:ss");
        Billtime = sdt.format(new Date());
        try {
            Currdt = sdf.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(ConsFetchActivity.sbmRec.getMeter_Phase().equals("4"))
        {
            etRead.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etRead.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
        }else
        {
            etRead.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        SimpleDateFormat PrevDt = new SimpleDateFormat("dd/MM/yyyy");
        /*try {
            Date prevdate = PrevDt.parse(ConsFetchActivity.sbmRec.getLast_Meter_Reading_Date());
            days = calculateDays(prevdate, Currdt);
            Log.e("Days",days.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        days = 45L;
        BMnths = (int) (days / 30);
        if (days % 30 > 15)
            BMnths++;
        if (BMnths == 0)
            BMnths = 1;
        if (BMnths < 0) {
            Toast.makeText(this, "Check your System Date", Toast.LENGTH_LONG).show();
            Button btn = (Button) findViewById(R.id.btnIB);
            btn.setVisibility(View.INVISIBLE);

            /*Button btnCap1 = (Button) findViewById(R.id.btnCapture);
            btnCap1.setVisibility(View.INVISIBLE);

            Button btnCap2 = (Button) findViewById(R.id.btnMDCap);
            btnCap2.setVisibility(View.INVISIBLE);*/
        } else {
//            Calc_due_Chq(3);
            Calc_due(15);
//            Calc_disc(14);
        }

//        capture_mtr_image();
//        BMnths = 1;

    }

    public void Calc_due(Integer value) {
        Date m = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf2.parse(m.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date d = sdf.parse("20130526160000");
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }*/
        cal.add(Calendar.DATE, value); // 10 is the days you want to add or subtract
        m = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Duedt = sdf.format(m);
//        Duedt=ConsFetchActivity.sbmRec.getCash_Due_Date();
    }

    public void Calc_from_date(Integer value) {
        Date m = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        try {
//            cal.setTime(sdf2.parse(bill_Cycle_todate));
            cal.setTime(sdf2.parse(bill_Cycle_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date d = sdf.parse("20130526160000");
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }*/
        cal.add(Calendar.DATE, value); // 10 is the days you want to add or subtract
        m = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        bill_Cycle_date = sdf.format(m);
        bill_Cycle_todate = sdf.format(m);
//        Duedt=ConsFetchActivity.sbmRec.getCash_Due_Date();
    }


    public void capture_photo(View view) {
        capture_mtr_image();

    }

    public void capture_mtr_image()
    {
        String imagesDir = Environment.getExternalStorageDirectory().getPath();
        imageFileName = "Pictures/" + ConsFetchActivity.sbmRec.getBP_Number() + ".JPEG";
        String capturedImgSave = null;
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

            StrictMode.setVmPolicy(builder.build());

            File f = new File(imagesDir, imageFileName);
            capturedImgSave = f.getPath();
            outputFileUri = Uri.fromFile(f);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra("output", Uri.fromFile(f));
            startActivityForResult(intent, 1337);
            captureFlag = 1;
            capture_mtr_flag = 1;
            capture_md_flag = 0;
//            getResizedBitmapImage();


            Button ISB = (Button) findViewById(R.id.btnIB);
            ISB.setEnabled(true);
//            photo_resize();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        deleteFiles(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
    }
    public void issuebill(View view) {
        Context ctx = getApplicationContext();
        EditText etSts = (EditText) findViewById(R.id.etStatus);
        final EditText etRead = (EditText) findViewById(R.id.etReading);
        EditText etObr = (EditText) findViewById(R.id.etObcode);
        EditText etcLoad = (EditText) findViewById(R.id.etCLoad);
        //EditText etCCmd = (EditText) findViewById(R.id.etCCMD);
        EditText etTel = (EditText) findViewById(R.id.etTelno);
        etSBAID = (EditText) findViewById(R.id.etSBAID);
        EditText etCurPF = (EditText) findViewById(R.id.etCurPF);
        CurLoad = Double.parseDouble(etCload.getText().toString());
//        read_image();
//        deleteFiles(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
        if(etTel.getText().toString()=="")
        {
            Telno ="0";
        }
        else {
            Telno = etTel.getText().toString();
        }
        String[] cSts=new String[4];
        cSts = spStatus.getSelectedItem().toString().split("\\-");
        CurStatus=cSts[0];
        if(Double.parseDouble(etRead.getText().toString())>Double.parseDouble(ConsFetchActivity.sbmRec.getPREV_READ())) {
            if (capture_mtr_flag == 1) {
                if (CurStatus.equals("*")) {
                    Toast.makeText(this, "Please select Proper Status", Toast.LENGTH_SHORT).show();
                } else {
                    String ret_result = AltDialog("Verify", "You have Entered Curr Reading as " + etRead.getText().toString() + ". Do you wish to Continue", "YES", "NO");
                    if (ret_result.equals("YES")) {

                    } else {
//                        Toast.makeText(getApplicationContext(), "Re-enter the correct Reading.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Please Capture Image", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Current Reading is less", Toast.LENGTH_SHORT).show();
        }
    }

    public void calc_bill_jusco(String Cursts,String PrevSts,String Read,String cat)
    {
//        Init_var();
        Double Mulfact =(Double.parseDouble(ConsFetchActivity.sbmRec.getMF()));
        Double prevRdg = (Double.parseDouble(ConsFetchActivity.sbmRec.getPREV_READ()));
        curread = (Double.parseDouble(Read));
        Double calc_units = (curread - prevRdg) * Mulfact;
//        unitsm=Math.floor(calc_units);
        unitsm=calc_units;
        Billed_units=unitsm;
        int bill_flag=0;
        CMD=etCload.getText().toString();
//        performGammaCorrection("/storage/emulated/0/Pictures/" + ConsFetchActivity.sbmRec.getBP_Number() + ".JPEG");
        switch(cat){

            case "DS-RURAL":
            case "NMDS-RURAL":
                Ds_Rural dsr=new Ds_Rural();
                EnrgyChrg=dsr.calc_DSRec(unitsm);
                FixedChrg=dsr.calc_DSRfc(1);
                ED=dsr.calc_DSRed(unitsm);
                FPPPAChrg=dsr.calc_fppa(unitsm,Double.parseDouble(ConsFetchActivity.sbmRec.getFPPPACharg()));
                break;

            case "DS-URBAN":
            case "NMDS-URBAN":
                Ds_Urban dsu=new Ds_Urban();
                EnrgyChrg=dsu.calc_DSUec(unitsm);
                FixedChrg=dsu.calc_DSUfc(1);
                ED=dsu.calc_DSUed(unitsm);
                FPPPAChrg=dsu.calc_fppa(unitsm,Double.parseDouble(ConsFetchActivity.sbmRec.getFPPPACharg()));
                break;

            case "DSHT":
                Ds_HT dsht=new Ds_HT();
                EnrgyChrg=dsht.calc_DSHTec(unitsm);
                FixedChrg=dsht.calc_DSHTfc(ConsFetchActivity.input_Load);
                ED=dsht.calc_DSHTed(unitsm);
//                FPPPAChrg=dsht.calc_fppa(unitsm,0.00);
                break;
            case "CS-RURAL":
            case "NMCS-RURAL":
                Cs_Rural csr =new Cs_Rural();
                EnrgyChrg=csr.calc_CSRec(unitsm);
                FixedChrg=csr.calc_CSRfc(1);
                ED= csr.calc_CSRed(unitsm);
                FPPPAChrg=csr.calc_fppa(unitsm,Double.parseDouble(ConsFetchActivity.sbmRec.getFPPPACharg()));

                break;
            case "CS-URBAN":
            case "NMCS-URBAN":
                Cs_Urban csu =new Cs_Urban();
                EnrgyChrg=csu.calc_CSUec(unitsm);
                FixedChrg=csu.calc_CSUfc(Double.parseDouble(ConsFetchActivity.sbmRec.getSanc_Load()));
                ED= csu.calc_CSUed(unitsm);
                FPPPAChrg=csu.calc_fppa(unitsm,Double.parseDouble(ConsFetchActivity.sbmRec.getFPPPACharg()));
                break;
            default:
                bill_flag=1;
                break;
        }

        if(bill_flag==0) {

            reb_early=Double.parseDouble(ConsFetchActivity.sbmRec.getRebateEarly());
            reb_dig=Double.parseDouble(ConsFetchActivity.sbmRec.getRebateDigital());
            if(reb_early>0.00d)
            {
                reb_early*=-1;
            }
            if(reb_dig>0.00d)
            {
                reb_dig*=-1;
            }
            rebate_val=reb_early + reb_dig;


            NetAmntExArr = (EnrgyChrg)
                    + (ED)
                    + (FixedChrg)
                    + FPPPAChrg
                    + Double.parseDouble(ConsFetchActivity.sbmRec.getMETER_RENT())
                    + DLAdjAmnt;


            DecimalFormat twoNAEAForm = new DecimalFormat("#.##");


            NetAmntExArr = Double.valueOf(twoNAEAForm.format(NetAmntExArr));
        /*Log.e("Penal(E+F)",penalcharge.toString());
        Log.e("DLAdjAmnt",DLAdjAmnt.toString());
        Log.e("NetAmntExArr",NetAmntExArr.toString());*/
            AdjAmnt=Double.parseDouble(ConsFetchActivity.sbmRec.getKwh_adj())+Double.parseDouble(ConsFetchActivity.sbmRec.getEd_adj());

            if(ConsFetchActivity.calc_flag==0) {
                NetAmntInArr = NetAmntExArr
                        + Double.parseDouble(ConsFetchActivity.sbmRec.getPREVIOUSOS())
                        + rebate_val
                        +AdjAmnt
                        +Double.parseDouble(ConsFetchActivity.sbmRec.getLPC())
                        +Double.parseDouble(ConsFetchActivity.sbmRec.getTDS())
                        +Double.parseDouble(ConsFetchActivity.sbmRec.getOTHERRECEIVABLE())
                        +Double.parseDouble(ConsFetchActivity.sbmRec.getINTERESTONSD());
            /*DecimalFormat twocurSuchgForm = new DecimalFormat("#.##");
            cur_Surcharge = Double.valueOf(twocurSuchgForm.format(cur_Surcharge));

            grossAmt = NetAmntInArr + cur_Surcharge;*/
            }
            else
            {
                NetAmntInArr = NetAmntExArr
                        +AdjAmnt
                        + rebate_val;
            }
            NetAmntInArr= (double)Math.round(NetAmntInArr);

            DecimalFormat twogrossAmtForm = new DecimalFormat("#.##");
            grossAmt = Double.valueOf(twogrossAmtForm.format(grossAmt));

            BillLG = NetAmntInArr % 10;

            DecimalFormat twoBLGForm = new DecimalFormat("#.##");
            BillLG = Double.valueOf(twoBLGForm.format(BillLG));

            if (BillLG < 5.00f) {
                BillLG *= -1.00;
//                NetAmntInArr = NetAmntInArr + BillLG;
            } else if (BillLG > 5.00f) {
                BillLG = 10.00 - BillLG;
//                NetAmntInArr = NetAmntInArr + BillLG;
            }

            DecimalFormat twoNAIAForm = new DecimalFormat("#.##");
            NetAmntInArr = Double.valueOf(twoNAIAForm.format(NetAmntInArr));

            Double Bill_lg_gross = 0.00;
            Bill_lg_gross = grossAmt % 10;

            if (Bill_lg_gross < 5.00f) {
                Bill_lg_gross *= -1.00;
                grossAmt = grossAmt + Bill_lg_gross;
            } else if (Bill_lg_gross > 5.00f) {
                Bill_lg_gross = 10.00 - Bill_lg_gross;
                grossAmt = grossAmt + Bill_lg_gross;
            }

            if (NetAmntInArr == 0.00) {
                cur_Surcharge = 0.00;
                grossAmt = 0.00;
            }

            format_values();
/*            Log.e("FC", FixedChrg.toString());
            Log.e("EC", EnrgyChrg.toString());
            Log.e("Duty", ED.toString());
            Log.e("Rebate", rebate_val.toString());
            Log.e("DLADJ", DLAdjAmnt.toString());
            Log.e("Total", NetAmntExArr.toString());
            Log.e("BillLG", BillLG.toString());
            Log.e("NetAmntInArr", NetAmntInArr.toString());*/
            store_print_bill();
        }
        else
        {
            Toast.makeText(this,"Bill Cannot be Generated",Toast.LENGTH_SHORT).show();
        }
    }

    public void format_values()
    {

        DecimalFormat twoForm = new DecimalFormat("#.##");
        EnrgyChrg = Double.valueOf(twoForm.format(EnrgyChrg));
        FixedChrg = Double.valueOf(twoForm.format(FixedChrg));
        ED = Double.valueOf(twoForm.format(ED));
        BillLG = Double.valueOf(twoForm.format(BillLG));
        rebate_val=Double.valueOf(twoForm.format(rebate_val));
        DLAdjAmnt=Double.valueOf(twoForm.format(DLAdjAmnt));
    }

    public void store_print_bill()
    {
//        write_sbmtopc();
        send_rdg_image();
        insert_data();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        response = RestServices.Send_Data();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        try{
            if(response.equals("OK")){

                db.execSQL("Update sbmtopc set online_flag_number='1' where BP_No='" + ConsFetchActivity.sbmRec.getBP_Number() + "'");
            } else{

                db.execSQL("Update sbmtopc set online_flag_number='0' where BP_No='" + ConsFetchActivity.sbmRec.getBP_Number()+ "'");
            }
        }catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            res1 = "IOException: " + e.toString();
        }
        boolean retFlag=false;
        ThermalPrint tp = new ThermalPrint();
        bt=new BTPrinting();
        String c=tp.connect_printer(bt);
        if(c.equals("C"))
        {
            tp.Print_logo2(this.getResources());
            tp.print_msg1_data();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bt.Close();
            Intent billdisp = new Intent(EntryActivity.this, MRActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(billdisp);
        }
        else {
        Intent billdisp = new Intent(EntryActivity.this, ThermalPrint.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(billdisp);
        }

       /* Intent billdisp = new Intent(EntryActivity.this, ThermalPrint.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(billdisp);*/
    }

    public void send_rdg_image() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BWfilename = Environment.getExternalStorageDirectory() + "/" + imageFileName;
//        BWfilename = Environment.getExternalStorageDirectory() + "/" + imageFileName2;
//        Picasso.with(this).load("file:" + BWfilename).into(imageView);
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
    public void insert_data() {

        uploadRec uploadRec = new uploadRec();
        uploadRec.setBill_Date(EntryActivity.Billdate);
        uploadRec.setCURR_READING(EntryActivity.curread.toString());
        uploadRec.setBILLED_UNITS(EntryActivity.unitsm.toString());
        uploadRec.setCur_Mtr_Sts(EntryActivity.CurStatus);
        uploadRec.setCur_MD(EntryActivity.CMD);
        uploadRec.setCat_tariff(EntryActivity.cat_tariff.toString());
        uploadRec.setENERGY_CHARGE(EntryActivity.EnrgyChrg.toString());
        uploadRec.setE_DUTY(EntryActivity.ED.toString());
        uploadRec.setFIXEDCHARGE(EntryActivity.FixedChrg.toString());
        uploadRec.setTot_rebate(EntryActivity.rebate_val.toString());
        uploadRec.setBill_Amt(EntryActivity.NetAmntExArr.toString());
        uploadRec.setTOTAL(EntryActivity.NetAmntInArr.toString());
        uploadRec.setBill_Due_date(EntryActivity.Duedt);
        uploadRec.setLAT(String.valueOf(ConsFetchActivity.latitude));
        uploadRec.setLONG(String.valueOf(ConsFetchActivity.longitude));

        /*uploadRec.setLAT("0.00");
        uploadRec.setLONG("0.00");*/


        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        @SuppressLint("DefaultLocale")
        String Instval = "INSERT INTO sbmtopc VALUES(" + //1
                "'" + ConsFetchActivity.sbmRec.getBP_Number() + //2
                "','" + ConsFetchActivity.sbmRec.getMeter_Number() +
                "','" + EntryActivity.curread + //3
                "','" + EntryActivity.CurStatus + //4
                "','" + EntryActivity.unitsm + //5 Billed_Units
                "','" + EntryActivity.unitsm +//6
                "','" + EntryActivity.EnrgyChrg +//7
                "','" + EntryActivity.ED + //9  Meter_Reading_Unit
                "','" + ConsFetchActivity.sbmRec.getMETER_RENT() + //11
                "','" + EntryActivity.FixedChrg + //12
                "','" + ConsFetchActivity.sbmRec.getLPC() + //13
                "','" + ConsFetchActivity.sbmRec.getINTERESTONSD() + //15
                "','" + ConsFetchActivity.sbmRec.getTDS()+ //16 RMD
                "','" + ConsFetchActivity.sbmRec.getRebateEarly() +//17
                "','" + ConsFetchActivity.sbmRec.getRebateDigital()+ //18 //Billed Units
                "','" + EntryActivity.rebate_val + //19
                "','" + ConsFetchActivity.sbmRec.getOTHERRECEIVABLE() + //20 //Posting Date
                "','" + String.format("%.2f",Double.valueOf(ConsFetchActivity.sbmRec.getPREVIOUSOS())) +//21
                "','" + String.format("%.2f",Double.valueOf(ConsFetchActivity.sbmRec.getFPPPACharg())) + //22
                "','" + String.format("%.2f",Double.valueOf(EntryActivity.NetAmntExArr)) + //23
                "','" + String.format("%.2f",Double.valueOf(EntryActivity.NetAmntInArr)) + //24
                "','" + MainActivity.MacID + //27 LTCS_Charge
                "','" + ConsFetchActivity.latitude + //28 WTCS_Surcharge
                "','" + ConsFetchActivity.longitude +//29 Penalty_EC
                "','" + MainActivity.Version + //30 Penalty_FC
                "','" + ConsFetchActivity.BillNo +//31 Penalty_20_Addl_Chg
                "','" + EntryActivity.Billdate +//32
                "','" + EntryActivity.Billtime + //33
                "','" + EntryActivity.Telno+ //34
                "','" + EntryActivity.CurStatus + //35 Credit_DLAmt
                "','" + ConsFetchActivity.sbmRec.getPREV_READ() +//39
                "','" + EntryActivity.CMD + //43 Round_Off_amount
                "','" + EntryActivity.CurPF +//44 Bill_Net_within_due_date
                "','" + EntryActivity.Duedt + //45 Surcharge
                "','" + EntryActivity.BillLG + //46 Bill_Amount_After_Due_Date
                "','" + EntryActivity.NetAmntInArr + //47 Cheque_Due_Date
                "','" + EntryActivity.NetAmntInArr +//48 Cash_Due_Date
                "','" + "0" +//49 Bill_Generation_Status
                "','" + MainActivity.Meter_Reader_Name + //50 Meter_Reader_Name
                "','" + "0" + //51 Bank_Name
                "','" + "0" + //51 Bank_Name
                "','" + EntryActivity.cat_tariff + //51 Bank_Name
                "','" + ConsFetchActivity.sbmRec.getMRU() + //51 Bank_Name
                "','" + ConsFetchActivity.sbmRec.getPortion()+ //51 Bank_Name
                "','" + EntryActivity.imageFileName + "')";//68 Remarks
        db.execSQL(Instval);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        bt.Close();
        Intent intent = new Intent(this,MRActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    public Bitmap getResizedBitmapImage(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = (float)newWidth / (float)width;
        float scaleHeight = (float)newHeight / (float)height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public boolean saveImageToInternalStorage(Bitmap image) {

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = openFileOutput("desiredFilename.jpg", Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    public void photo_resize()
    {
        Bitmap imagedata = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" +imageFileName);
        Bitmap bm=getResizedBitmapImage(imagedata,384,84);
        saveImageToInternalStorage(bm);
    }

    public String AltDialog(String title,String message,String btnPositive,String btnNegetive)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                retStr="YES";
//                overwritedata_db();
                calc_bill_jusc();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(btnNegetive, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                retStr="NO";
                // Do nothing
//                appenddata_db();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return retStr;
    }

    public void calc_bill_jusc()
    {
        Context ctx = getApplicationContext();
        EditText etSts = (EditText) findViewById(R.id.etStatus);
        final EditText etRead = (EditText) findViewById(R.id.etReading);
        EditText etObr = (EditText) findViewById(R.id.etObcode);
        EditText etcLoad = (EditText) findViewById(R.id.etCLoad);
        //EditText etCCmd = (EditText) findViewById(R.id.etCCMD);
        EditText etTel = (EditText) findViewById(R.id.etTelno);
        etSBAID = (EditText) findViewById(R.id.etSBAID);
        EditText etCurPF = (EditText) findViewById(R.id.etCurPF);
        CurLoad = Double.parseDouble(etCload.getText().toString());
//        deleteFiles(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");


        String[] cSts=new String[4];
        cSts = spStatus.getSelectedItem().toString().split("\\-");
        CurStatus=cSts[0];

        Toast.makeText(this, "Bill Calculation in progress. Please Wait.", Toast.LENGTH_SHORT).show();
        btnIssue.setEnabled(false);
        btnIssue.setVisibility(View.INVISIBLE);
//        photo_resize();
        Double Prev_rdg = 0.00d;
        Prev_rdg = Double.parseDouble(ConsFetchActivity.sbmRec.getPREV_READ());

        if (Double.parseDouble(etRead.getText().toString()) > Prev_rdg) {
//                                                            calc_bill(CurStatus, ConsFetchActivity.sbmRec.getMeter_Status(), etRead.getText().toString(), etcLoad.getText().toString());
            CurLoad = Double.parseDouble(etcLoad.getText().toString());
//                                                calc_bill_cspdcl(CurStatus,ConsFetchActivity.sbmRec.getMeter_Status(), etRead.getText().toString());
            String prev_mr = prev_sts.toString();
            if (((ConsFetchActivity.sbmRec.getMRNOTE().equals("OK MR") || prev_sts.toString().equals("0")) && EntryActivity.CurStatus.equals("0"))) {

                calc_bill_jusco(CurStatus, prev_sts.toString(), etRead.getText().toString(), ConsFetchActivity.sbmRec.getCATEGORY());
            } else {
//                calc_bill_jusco(CurStatus, ConsFetchActivity.sbmRec.getMRNOTE(),"0", ConsFetchActivity.sbmRec.getCATEGORY());
                Billed_units=unitsm-avg_6_cons;
                EntryActivity.curread = Double.parseDouble(etRead.getText().toString());
                EntryActivity.unitsm = 0.00;
//                EntryActivity.CurStatus);
                EntryActivity.CMD = "0";
//                EntryActivity.cat_tariff.toString());
                EntryActivity.EnrgyChrg = 0.00;
                EntryActivity.ED = 0.00;
                EntryActivity.FixedChrg = 0.00;
                EntryActivity.rebate_val = 0.00;
                EntryActivity.NetAmntExArr = 0.00;
                EntryActivity.NetAmntInArr = 0.00;
                insert_data();
                Toast.makeText(getApplicationContext(), "Not issueing the bill for this consumer.", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (((ConsFetchActivity.sbmRec.getMRNOTE().equals("OK MR") || prev_sts.toString().equals("0")) && EntryActivity.CurStatus.equals("0"))) {
                Toast tst = Toast.makeText(getApplicationContext(), "Current Reading is Less", Toast.LENGTH_SHORT);
                tst.show();
            } else {
                Billed_units=unitsm-avg_6_cons;
                insert_data();
                Toast.makeText(getApplicationContext(), "Not issueing the bill for this consumer.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performGammaCorrection(String Path) {
        //! [changing-contrast-brightness-gamma-correction]
        /*double gammaValue = 1.4;
        Mat matImgSrc = new Mat();
        matImgSrc = Imgcodecs.imread(Path);


        Mat lookUpTable = new Mat(1, 256, CvType.CV_8UC1);
        byte[] lookUpTableData = new byte[(int) (lookUpTable.total()*lookUpTable.channels())];
        for (int i = 0; i < lookUpTable.cols(); i++) {
            lookUpTableData[i] = saturate(Math.pow(i / 255.0, gammaValue) * 255.0);
        }
        lookUpTable.put(0, 0, lookUpTableData);
        Mat img = new Mat();
        Core.LUT(matImgSrc, lookUpTable, img);*/
        //! [changing-contrast-brightness-gamma-correction]

//        imgModifLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(img)));
//        frame.repaint();
    }
    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }

    public static Bitmap createGrayscale(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOut);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(src, 0, 0, paint);
        return bmOut;
    }

    public void read_image() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BWfilename = Environment.getExternalStorageDirectory() + "/" + imageFileName;
         grayfilename ="/storage/emulated/0/DCIM/Camera/grayfname.jpg";
//        Picasso.with(this).load("file:" + BWfilename).into(imageView);
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
        Bitmap graybmp=createGrayscale(bm);

        File f =new File(grayfilename);

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        Bitmap bitmap = graybmp;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void performCrop(){


        try {
            String imagesDir = Environment.getExternalStorageDirectory().getPath();
//            imageFileName2 = "Pictures/" + ConsFetchActivity.sbmRec.getBP_Number() + "_CROP.JPEG";
            imageFileName2 = "CROP_IMAGES/" + ConsFetchActivity.sbmRec.getBP_Number() + "_CROP.JPEG";
            File f = new File(imagesDir, imageFileName2);
            Uri cropuri=Uri.fromFile(f);
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //indicate image type and Uri
//            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.setDataAndType(outputFileUri, "image/*");
            cropIntent.putExtra("output", cropuri);

            //set crop properties
            cropIntent.putExtra("crop", "true");

            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 16);
            cropIntent.putExtra("aspectY", 9);

            //indicate output X and Y


            cropIntent.putExtra("outputX", 150);
            cropIntent.putExtra("outputY", 150);

            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if(requestCode == CAMERA_CAPTURE){
                picUri = data.getData();
                performCrop();
            }

            if(requestCode == 1337){
//                picUri = data.getData();
                performCrop();
            }
            else if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
//get the cropped bitmap
//                Bitmap thePic = extras.getParcelable("data");
//                Bitmap thePic = extras.getParcelable("return-data");

            }

        }
    }
}