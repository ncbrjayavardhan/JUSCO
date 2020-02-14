package com.jusco;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
//import android.test.suitebuilder.annotation.Suppress;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import com.google.android.gms.location.LocationListener;
import android.telephony.TelephonyManager;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ConsFetchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    SQLiteDatabase db;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    public static EditText etScno;
    public static Double latitude; // latitude
    public static Double longitude; // longitude
    public static long gpsTime;
    public static uploadRec record = new uploadRec();
    public static SbmRec sbmRec = new SbmRec();
    public static Integer BillNo;
    public static String MacID;
    public static Double input_Load=0.00;
    public static String measurement_Load_code="";
    public static int calc_flag=0;
    TextView tvScno,tvBook,txtName,txtAddr,txtLoad,txtMtrno,txtMF;
    Button NextB;
    Button FetchB;

    public static String DBNAME="JUSCODB";
    RelativeLayout consFetch_2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cons_fetch);
        DBNAME=getResources().getString(R.string.DBNAME);
        tvScno=(TextView)findViewById(R.id.txtScno);
        tvBook=(TextView)findViewById(R.id.txtBook);
        txtName=(TextView)findViewById(R.id.txtname);
        txtAddr=(TextView)findViewById(R.id.txtaddr);
        txtLoad=(TextView)findViewById(R.id.txtLoad);
        txtMtrno=(TextView)findViewById(R.id.txtMtr);
        txtMF=(TextView)findViewById(R.id.txtMF);
        NextB=(Button)findViewById(R.id.btnNext);
        FetchB=  (Button) findViewById(R.id.btnFetch);
        measurement_Load_code="";

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
        consFetch_2=(RelativeLayout)findViewById(R.id.rtlConsfetch2);
        consFetch_2.setVisibility(View.INVISIBLE);
        MacID = telephonyManager.getDeviceId();
//        MacID="0";
//        decrypt_file();
        if (checkGooglePlayServices()) {
            buildGoogleApiClient();
            //prepare connection request
            createLocationRequest();
        }
        Button next = (Button) findViewById(R.id.btnNext);

        etScno = (EditText) findViewById(R.id.etScno);
        /*etScno.setRawInputType(Configuration.KEYBOARD_12KEY);
        etScno.setTransformationMethod(SingleLineTransformationMethod.getInstance());*/

        etScno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etScno.getText().length() < 10) {

                }
            }
        });
        etScno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return false;
                if(keyCode == KeyEvent.KEYCODE_ENTER ){
                    fetch_details(v);

                    return true;
                }
                return false;
            }
        });

    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void Read_data(String str) {

    }

    public int read_record_sbmtopc() {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        int Flag = 0;

//        record.recordCount = 0;
        BillNo = 0;
        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        try {

            Cursor c1 = db.rawQuery("SELECT count(*) FROM sbmtopc", null);
            //aBuffer += aDataRow + "\n";
            if(c1.moveToFirst()) {
                String scno = c1.getString(0);
                BillNo=Integer.parseInt(scno);
                BillNo=BillNo+1;
            }
            if (BillNo == 0) {
                BillNo = 1;
            }
        }catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e("TAG", "SQLiteConstraintException:" + e.getMessage());
        }
        catch (android.database.sqlite.SQLiteException e) {
            Log.e("TAG", "SQLiteException:" + e.getMessage());
        }
        catch (Exception e) {
            Log.e("TAG", "Exception:" + e.getMessage());
        }
        try {
            String cmpScno;
            cmpScno=etScno.getText().toString();
            String query="";
            query="SELECT * FROM sbmtopc WHERE BP_No='"+etScno.getText()+"' or Meter_Serial_No='" + etScno.getText() +"'";
            Cursor c = db.rawQuery(query, null);
            //aBuffer += aDataRow + "\n";
            if(c.moveToFirst()) {
                String scno = c.getString(c.getColumnIndex("BP_No"));
                String mtrno=c.getString(c.getColumnIndex("Meter_Serial_No"));

                if (scno.equals(cmpScno)  || mtrno.equals(cmpScno)) {
                    Flag = 1;
                } else
                    Flag = 0;
            }
        }catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e("TAG", "SQLiteConstraintException:" + e.getMessage());
        }
        catch (android.database.sqlite.SQLiteException e) {
            Log.e("TAG", "SQLiteException:" + e.getMessage());
        }
        catch (Exception e) {
            Log.e("TAG", "Exception:" + e.getMessage());
        }
//        db.close();
        return Flag;
    }

    public int read_record_external() {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        int retn = 0;


        try {
            File myFile = new File("/sdcard/document.decrypted");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                //aBuffer += aDataRow + "\n";
                String scno = aDataRow.substring(0, 6);
                if (scno.equals(record.BP_Number)) {
                    retn = 0;
                    Read_data(aDataRow);
                    break;
                } else {
                    retn = 1;
                }
            }
            myReader.close();
            deleteFiles("/sdcard/document.decrypted");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retn;
    }

    public void playwithRawFiles() throws IOException {


    }// PlayWithSDFiles

    public void fetch_details(View view) {
        int Flag;
        final Button Fetchbtn = (Button) findViewById(R.id.btnFetch);
        final EditText etScno = (EditText) findViewById(R.id.etScno);
        final EditText Bookno = (EditText) findViewById(R.id.etBook);
        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvaddr1 = (TextView) findViewById(R.id.tvaddr1);
        final TextView tvaddr2 = (TextView) findViewById(R.id.tvaddr2);
        final TextView tvaddr3 = (TextView) findViewById(R.id.tvaddr3);
        final TextView tvload = (TextView) findViewById(R.id.tvLoad);
        final TextView tvMtrno = (TextView) findViewById(R.id.tvmtrno);
        final TextView tvMf = (TextView) findViewById(R.id.tvMF);

        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);

        if (etScno.getText().toString().isEmpty()) {
            Context ct = getApplicationContext();
            Toast tMessage = Toast.makeText(ct, "Enter Service No", Toast.LENGTH_SHORT);
            tMessage.show();

        } else {
            record.BP_Number = etScno.getText().toString();
            if (Bookno.getText().toString().isEmpty()) {
                Context ct2 = getApplicationContext();
                Toast tMessage2 = Toast.makeText(ct2, "Enter record.Book No", Toast.LENGTH_SHORT);
                tMessage2.show();
            } else {
                Flag = read_record_sbmtopc();

                if (Flag == 0) {
                    String query="";
                    query="SELECT * FROM pctosbm WHERE BP_No like '%" +etScno.getText()+"' or Meter_Serial_No='" + etScno.getText() +"'";
                    Log.e("query",query);
                    Cursor c=db.rawQuery(query, null);
                    if(c.moveToFirst())
                    {
                        read_pctosbm(c);
                        Date schDate= new Date();
                        SimpleDateFormat schSdf=new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            schDate=schSdf.parse(sbmRec.getSchedule_MR_Date());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date todayDate=new Date();

                        SimpleDateFormat todaySdf=new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            todayDate=todaySdf.parse(new Date().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Long datediff=calculateDays(schDate,todayDate);
//                        if(datediff>=30 && datediff<=50) {
//                        if(datediff>=23 && datediff<=60) {
                        if(datediff>=0 && datediff<=120) {
                            if(sbmRec.getBP_NAME().length()>30) {
                                tvName.setText("  " + StringUtils.capitalize(sbmRec.getBP_NAME().toLowerCase().substring(0, 16)));
                            }
                            else
                            {
                                tvName.setText("  " + StringUtils.capitalize(sbmRec.getBP_NAME().toLowerCase()));
                            }
                            if(sbmRec.getBP_ADDRESS1().length()>30) {
                                    tvaddr1.setText(StringUtils.capitalize(sbmRec.getBP_ADDRESS1().toLowerCase().substring(0, 16)));
                                }
                            else{
                                    tvaddr1.setText(StringUtils.capitalize(sbmRec.getBP_ADDRESS1().toLowerCase()));
                                }
                            if(sbmRec.getBP_ADDRESS2().length()>30) {
                                tvaddr2.setText(StringUtils.capitalize(sbmRec.getBP_ADDRESS2().toLowerCase().substring(0, 16)));
                            }
                            else
                            {
                                tvaddr2.setText(StringUtils.capitalize(sbmRec.getBP_ADDRESS2().toLowerCase()));
                            }
                            Double Load = 0.00;
                            Load = Double.valueOf(sbmRec.getSanc_Load());

                            tvload.setText(sbmRec.getSanc_Load());
                            tvMtrno.setText(sbmRec.getMeter_Number());
                            tvMf.setText(sbmRec.getMF());

                            String query2 = "SELECT * FROM sbmtopc WHERE BP_No='" + sbmRec.getBP_Number() + "'";

                            Cursor c1 = db.rawQuery(query2, null);

                            if (c1.moveToFirst()) {
                                calc_flag = 1;
                            } else {
                                calc_flag = 0;
                            }
                            Button next = (Button) findViewById(R.id.btnNext);
                            next.setEnabled(true);
                            consFetch_2.setVisibility(View.VISIBLE);
                        }
                        else
                        {
//                            Toast.makeText(this,"Check Schedule Date",Toast.LENGTH_SHORT).show();
                            showMessage("Error", "Check Schedule Date");
                        }
                    }
                    else
                    {
                        showMessage("Error", "Invalid Service No");
                        clearText();
                        consFetch_2.setVisibility(View.INVISIBLE);
                    }
                    db.close();

                } else {
                    Toast.makeText(getApplicationContext(), "Already Billed", Toast.LENGTH_SHORT).show();
                    /*Utils mDialogutils=new Utils(this);
                    mDialogutils.AltDialog_SimpleMsg(this,"Already Billed", "YES","",this);*/

                }
//                } else {
//                    Toast tst = Toast.makeText(getApplicationContext(), "Already Billed", Toast.LENGTH_SHORT);
//                    tst.show();
//                }
            }
        }
        hideKeyboard(ConsFetchActivity.this);
    }
    public void entry_sts_activity(View view) {
        Intent demoMeterReadingIntent = new Intent(ConsFetchActivity.this, EntryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(demoMeterReadingIntent);
    }
    @Override
    public void onBackPressed() {
        Intent inte = new Intent(this, MRActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inte);
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
//            String time = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(mLastLocation.getTime());
//            Toast.makeText(this, "Time:" + time,Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //Toast.makeText(this, "Update -> Latitude:" + mLastLocation.getLatitude()+", Longitude:"+mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            /*
             * google play services is missing or update is required
             *  return code could be
             * SUCCESS,
             * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
             * SERVICE_DISABLED, SERVICE_INVALID.
             */
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {

            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
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

    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        final EditText etBPnum =  findViewById(R.id.etScno);
        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvaddr1 = (TextView) findViewById(R.id.tvaddr1);
        final TextView tvaddr2 = (TextView) findViewById(R.id.tvaddr2);
        final TextView tvaddr3 = (TextView) findViewById(R.id.tvaddr3);
        final TextView tvload = (TextView) findViewById(R.id.tvLoad);
        final TextView tvMtrno = (TextView) findViewById(R.id.tvmtrno);
        final TextView tvMf = (TextView) findViewById(R.id.tvMF);
        etScno.setText("");
        tvName.setText("");
        tvaddr1.setText("");
        tvaddr2.setText("");
        tvload.setText("");
        tvMtrno.setText("");
        tvMf.setText("");
        Button next = (Button) findViewById(R.id.btnNext);
        next.setEnabled(true);
    }

    public void read_pctosbm(Cursor c)
    {

        sbmRec.setBP_Number(c.getString(c.getColumnIndex("BP_No")));
        sbmRec.setBP_NAME(c.getString(c.getColumnIndex("BPNAME")));
        sbmRec.setBP_ADDRESS1(c.getString(9));
        sbmRec.setBP_ADDRESS2(c.getString(10));
        sbmRec.setBP_ADDRESS3(c.getString(11));
        sbmRec.setSanc_Load(c.getString(c.getColumnIndex("Sanction_Load")));
        sbmRec.setMeter_Number(c.getString(c.getColumnIndex("Meter_Serial_No")));
        sbmRec.setMobile_Number(c.getString(c.getColumnIndex("Mob_No")));
        sbmRec.setMeter_Phase(c.getString(c.getColumnIndex("Meter_Phase")));
        sbmRec.setSchedule_MR_Date(c.getString(c.getColumnIndex("Schedule_MR_date")));
        sbmRec.setUOM(c.getString(c.getColumnIndex("UOM")));
        /*sbmRec.setDTR_Number(c.getString(c.getColumnIndex("DTR_Number")));
        sbmRec.setFeeder_Name(c.getString(c.getColumnIndex("Feeder_Name")));
        sbmRec.setDTR_Location(c.getString(c.getColumnIndex("DTR_Location")));
        sbmRec.setPrev_Billing_Date(c.getString(c.getColumnIndex("Prev_Billing_Date")));*/
        sbmRec.setCons1(c.getString(c.getColumnIndex("cons1")));
        sbmRec.setCons2(c.getString(c.getColumnIndex("cons2")));
        sbmRec.setCons3(c.getString(c.getColumnIndex("cons3")));
        sbmRec.setCons4(c.getString(c.getColumnIndex("cons4")));
        sbmRec.setCons5(c.getString(c.getColumnIndex("cons5")));
        sbmRec.setCons6(c.getString(c.getColumnIndex("cons6")));
//        sbmRec.setPOSTINGDATE(c.getString(c.getColumnIndex("POSTINGDATE")));
        sbmRec.setCON_DATE(c.getString(c.getColumnIndex("CON_DATE")));
        sbmRec.setDL_COUNTER(c.getString(c.getColumnIndex("DL_COUNTER")));
        sbmRec.setCATEGORY(c.getString(c.getColumnIndex("CATEGORY")));
        sbmRec.setE_Duty_Identifier(c.getString(c.getColumnIndex("E_DUTY")));
        sbmRec.setPREV_READ(c.getString(c.getColumnIndex("PRE_READ")));
        sbmRec.setMRNOTE(c.getString(c.getColumnIndex("MRNOTE")));
        sbmRec.setKwh_adj(c.getString(c.getColumnIndex("kwh_Adjustment")));
        sbmRec.setEd_adj(c.getString(c.getColumnIndex("ED_adjustment")));
        sbmRec.setMF(c.getString(c.getColumnIndex("MF")));
        sbmRec.setMETER_RENT(c.getString(c.getColumnIndex("METER_RENT")));
        sbmRec.setLPC(c.getString(c.getColumnIndex("LPC")));
        sbmRec.setINTERESTONSD(c.getString(c.getColumnIndex("INTERESTOnSD")));
        sbmRec.setTDS(c.getString(c.getColumnIndex("TDS")));
        sbmRec.setRebateEarly(c.getString(c.getColumnIndex("RebateEarly")));
        sbmRec.setRebateDigital(c.getString(c.getColumnIndex("RebateDigital")));
        sbmRec.setOTHERRECEIVABLE(c.getString(c.getColumnIndex("OTHERRECEIVABLE")));
        sbmRec.setPREVIOUSOS(c.getString(c.getColumnIndex("PREVIOUSOS")));
        sbmRec.setFPPPACharg(c.getString(c.getColumnIndex("FPPPACharg")));
        sbmRec.setFixedCharge(c.getString(c.getColumnIndex("FIXEDCHARGE")));
        sbmRec.setMRU(c.getString(c.getColumnIndex("MRU")));
        sbmRec.setPortion(c.getString(c.getColumnIndex("Portion")));
        /*sbmRec.setOffice_Addr(c.getString(c.getColumnIndex("office_Addr")));
        sbmRec.setOffice_Phone(c.getString(c.getColumnIndex("office_Phone")));
        sbmRec.setPrev_rdg_dt(c.getString(c.getColumnIndex("Prev_rdg_dt")));
        sbmRec.setBilling_Status(c.getString(c.getColumnIndex("Billing_Status")));
        sbmRec.setOld_Meter_Number(c.getString(c.getColumnIndex("Old_Meter_Number")));
        sbmRec.setReplacement_Dt(c.getString(c.getColumnIndex("Replacement_Dt")));
        sbmRec.setOld_mtr_final_rdg(c.getString(c.getColumnIndex("Old_mtr_final_rdg")));
        sbmRec.setNew_mtr_initial_rdg(c.getString(c.getColumnIndex("New_mtr_initial_rdg")));*/
    }

    /*public void diff_twodays()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date firstDate = null;
        try {
            firstDate = sdf.parse(sbmRec.getSchedule_MR_Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date secondDate = null;
        try {
            secondDate = sdf.parse(String.valueOf(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

//        assertEquals(diff, 6);
    }*/

    public static long calculateDays(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }


}