package com.jusco;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothManager;
import org.opencv.android.Utils;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;
import com.amigos.thermalAPI.Bluetooth_Img_ThermalAPI;
import com.jusco.myprinter.Global;
import com.jusco.myprinter.WorkService;
import com.jusco.utils.FileUtils;
import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Pos;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.UUID;

public class ThermalPrint extends AppCompatActivity {

//    private static Handler mHandler = null;

    static double minThresh;
    static double maxThresh;
    static String Hexstr;
    static String morphImgSave;
    static String imagesDir2;
    static int imgheight;
    static int imgwidth;
    static int splitStrLength;
    static String capturedImgSave;
    static boolean flag;
    static {
        ThermalPrint.minThresh = 70.0;
        ThermalPrint.maxThresh = 255.0;
        ThermalPrint.Hexstr = "";
        ThermalPrint.morphImgSave = "";
        ThermalPrint.imagesDir2 = Environment.getExternalStorageDirectory().getPath();
        ThermalPrint.imgheight = 250;
        ThermalPrint.imgwidth = 384;
        ThermalPrint.splitStrLength = 24000;
        ThermalPrint.capturedImgSave = "";
        ThermalPrint.flag = false;
    }


    BluetoothManager btpObject;
    final int PIC_CROP = 1;
    private static final int REQUEST_ENABLE_BT = 1;
    //    ListView listDevicesFound;
    BluetoothAdapter bluetoothAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    static final UUID MY_UUID = UUID.randomUUID();
    ArrayAdapter<String> btArrayAdapter;
    TextView stateBluetooth;
    ListView listDevicesFound;
    String address;
    String imagesDir = Environment.getExternalStorageDirectory().getPath();
    private static Pos pos = new Pos();
    private static BTPrinting bt = null;
    public final String btAddressDir = Environment.getExternalStorageDirectory()
            + "";
    public final String DATA_PATH1 = Environment.getExternalStorageDirectory()
            + "/";
    byte[] imgData=null;
    EditText btaddr,text;
    private ImageView imageViewPicture;
    String PrintFlag;
    Button cont,prt;
    uploadRec uploadRec;
    SbmRec sbmRec;
    private static Handler mHandler = null;
    public static String[] Bill_Base = {"OK MR", "Door Lock", "Defective Device", "Abnormal Reading", "Meter Missing", "No Response", "Recheck Required", "Meter Tampered", "Disconnected Reading"};
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(device.getAddress() + "\n"
                        + device.getName());
                btArrayAdapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thermal_print);
        uploadRec=null;
        sbmRec=null;

        InitGlobalString();

        WorkService.cb = new IOCallBack() {
            // WorkThread线程回调
            public void OnOpen() {
                // TODO Auto-generated method stub
                if(null != mHandler)
                {
                    Message msg = mHandler.obtainMessage(Global.MSG_IO_ONOPEN);
                    mHandler.sendMessage(msg);
                }
            }

            public void OnClose() {
                // TODO Auto-generated method stub
                if(null != mHandler)
                {
                    Message msg = mHandler.obtainMessage(Global.MSG_IO_ONCLOSE);
                    mHandler.sendMessage(msg);
                }
            }

        };
        mHandler = new MHandler(this);
        WorkService.addHandler(mHandler);

        if (null == WorkService.workThread) {
            Intent intent = new Intent(this, WorkService.class);
            startService(intent);
        }

        handleIntent(getIntent());
        mHandler = new MHandler(this);
        WorkService.addHandler(mHandler);
        /*String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("STRING_I_NEED");
                uploadRec= (viipl.com.cspdcl.uploadRec) extras.getSerializable("uploadrec");
                sbmRec= (SbmRec) extras.getSerializable("sbmrec");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }*/
        uploadRec=DuplicateBill.uploadRec;
        sbmRec=DuplicateBill.sbmRec;
        btaddr=(EditText) findViewById(R.id.editText);
        text=(EditText) findViewById(R.id.editText2);
        imageViewPicture = (ImageView) findViewById(R.id.imageView);
        listDevicesFound = (ListView) findViewById(R.id.devicesfound2);
        btArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);
        cont=findViewById(R.id.button);
        prt=findViewById(R.id.button2);

// ***********************************************************
        try {

            FileInputStream fstream = new FileInputStream(DATA_PATH1
                    + "BTaddress2.txt");

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                btaddr.setText(strLine);
                address = strLine;
            }
            in.close();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        // ***********************************************************
        listDevicesFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /*
                 * Toast.makeText(getApplicationContext(),
                 * ""+listDevicesFound.getCount(), Toast.LENGTH_SHORT).show();
                 */
                String selection = (String) (listDevicesFound
                        .getItemAtPosition(position));
                Toast.makeText(getApplicationContext(),
                        "BLUETOOTH ADDRESS IS SAVED SUCCESSFULLY",
                        Toast.LENGTH_SHORT).show();
                address = selection.substring(0, 17);
                btaddr.setText(address);
                try {
                    File myFile = new File(DATA_PATH1 + "BTaddress2.txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(
                            fOut);
                    myOutWriter.append(address);
                    myOutWriter.close();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        btaddr.setText("02:0B:3D:D1:B8:B0");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        /*Bitmap bm = getImageFromAssetsFile("gray.png");
        if (null != bm) {
            imageViewPicture.setImageBitmap(bm);
        }*/

        CheckBlueToothState();
        bt=new BTPrinting();

        pos.Set(bt);
    }
    public void connect(View v)
    {
        if(btaddr.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter Bluetooth printer address",Toast.LENGTH_LONG).show();
        }
        else
        {
            String BTAddress =btaddr.getText().toString();
            if(bt.Open(BTAddress))
            {
                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Failed to connect",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void print(View v)
    {
        String pr="sudhir";
        pos.IO.Write(pr.getBytes(),0,pr.length());
        bt.Write(pr.getBytes(),0,pr.length());
    }

    public void dfonts(View v)
    {
        if(text.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter text",Toast.LENGTH_SHORT).show();
        }
        else {


            //pos.POS_SetCharSetAndCodePage(0, 0);
            //pos.POS_S_TextOut(pszString, encoding, nOrgx, nWidthTimes,
            // nHeightTimes, nFontType, nFontStyle);
            // Example parameters  of the  function for printing text in different format
           /*public static final int FONTSTYLE_NORMAL = 0x00;
            public static final int FONTSTYLE_BOLD = 0x08;
            public static final int FONTSTYLE_UNDERLINE1 = 0x80;
            public static final int FONTSTYLE_UNDERLINE2 = 0x100;
            public static final int FONTSTYLE_UPSIDEDOWN = 0x200;
            public static final int FONTSTYLE_BLACKWHITEREVERSE = 0x400;
            encoding = "US-ASCII";
            norgx=0;
            enlarge width 0,1
            enalarge height 0,1
            font type 0,1,2
            text align 0,1,2*/

            String nm = "";
            pos.POS_S_TextOut(nm, "US-ASCII", 0, 1, 1, 1, 0x80);




            if(MRActivity.SearchFlag.equals("D")) {
                print_dup_msg1_data();
               /* print_dup_msg2_data();
                Print_dup_Stub();*/
//                print_note_data();
            }
            else {

//                print_picture(Environment.getExternalStorageDirectory() + "/" + EntryActivity.imageFileName);

                Print_logo();
                print_msg1_data();
//                Print_Image();

                /*print_msg2_data();
                Print_Stub();
                print_note_data();*/
            }
//            String pr = text.getText().toString();
//            pr=pr.concat("\n");
//            pos.IO.Write(pr.getBytes(), 0, pr.length());


        }



    }

    public void print_msg1_data()
    {

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
        uploadRec.setFPPPACharg(String.format("%.2f",EntryActivity.FPPPAChrg));

     /*  uploadRec.setLAT("0.00");
       uploadRec.setLONG("0.00");*/

        String msg;
       /* msg = ("         JUSCO   \n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*/
//        Print_logo();

        msg = ("  ---------SPOT BILL--------  \n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="BillNo:" + ConsFetchActivity.BillNo + " Dt:" + uploadRec.getBill_Date()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="BPNo:" + ConsFetchActivity.sbmRec.getBP_Number()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Name:" + ConsFetchActivity.sbmRec.getBP_NAME()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Addr:" + ConsFetchActivity.sbmRec.getBP_ADDRESS1()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ConsFetchActivity.sbmRec.getBP_ADDRESS2()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=ConsFetchActivity.sbmRec.getBP_ADDRESS3()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="MTRNO:"+ ConsFetchActivity.sbmRec.getMeter_Number() +"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="Sanc.Load:" + ConsFetchActivity.sbmRec.getSanc_Load() + ConsFetchActivity.sbmRec.getUOM()+ " PH:"+ConsFetchActivity.sbmRec.getMeter_Phase()+ "\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="Category:" + ConsFetchActivity.sbmRec.getCATEGORY()+ " MF:"+ConsFetchActivity.sbmRec.getMF() + "\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="BillCycle:" + EntryActivity.bill_Cycle_date.substring(3,10) + " to " + EntryActivity.bill_Cycle_todate.substring(3,10)+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        Print_Image();
//        print_picture(Environment.getExternalStorageDirectory() + "/" + EntryActivity.imageFileName);
        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="PrevRdg:" + ConsFetchActivity.sbmRec.getPREV_READ() + "Kwh"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="CurRdg:" + uploadRec.getCURR_READING() + "Kwh"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="Units:" + uploadRec.getBILLED_UNITS() + "Kwh"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="MtrSts:" + Bill_Base[Integer.parseInt(uploadRec.getCur_Mtr_Sts())]+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="MD:" + uploadRec.getCur_MD() + "KW"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="Last 6 Months Consumption"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="1)" + ConsFetchActivity.sbmRec.getCons1() + " 2)" + ConsFetchActivity.sbmRec.getCons2() +" 3)" + ConsFetchActivity.sbmRec.getCons3() +"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="4)" + ConsFetchActivity.sbmRec.getCons4()+" 5)" + ConsFetchActivity.sbmRec.getCons5() + " 6)" + ConsFetchActivity.sbmRec.getCons6()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
//        Stubmsg=(string_append2("Payable Amt:", DuplicateBill.billedRecords.NetAmntInArr.toString())+ "\n");
        msg=string_append2("EC(@" + uploadRec.getCat_tariff() + " /Unit):", String.format("%.2f",Double.valueOf(uploadRec.getENERGY_CHARGE()))) +"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("JED:" , String.format("%.2f",Double.valueOf(uploadRec.getE_DUTY())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("Meter Rent:" , String.format("%.2f",Double.valueOf(ConsFetchActivity.sbmRec.getMETER_RENT())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("FC:" , String.format("%.2f",Double.valueOf(uploadRec.getFIXEDCHARGE())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("FPPPA:" , String.format("%.2f",Double.valueOf(uploadRec.getFPPPACharg())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("BillAmt:" , String.format("%.2f",Double.valueOf(uploadRec.getBill_Amt())))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("Rebate:" , String.format("%.2f",Double.valueOf(uploadRec.getTot_rebate())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("AdjAmnt:" , String.format("%.2f",EntryActivity.AdjAmnt))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("TDS:" , String.format("%.2f",Double.valueOf(ConsFetchActivity.sbmRec.getTDS())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("LPC:" , String.format("%.2f",Double.valueOf(ConsFetchActivity.sbmRec.getLPC())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("Oth:",String.format("%.2f",Double.valueOf("0.00")))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("SD.Intr:" , String.format("%.2f",Double.valueOf(ConsFetchActivity.sbmRec.getINTERESTONSD())))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("O/S:" , String.format("%.2f",Double.valueOf(ConsFetchActivity.sbmRec.getPREVIOUSOS())))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg=string_append2("Total:" , String.format("%.2f",Double.valueOf(uploadRec.getTOTAL())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());

        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Due Dt:"+uploadRec.getBill_Due_date()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Lat:" + uploadRec.getLAT()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Lon:" + uploadRec.getLONG()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
    }
    public void print_msg2_data()
    {
        String msg2;
        /*DecimalFormat twoECForm = new DecimalFormat("#.##");
        EntryActivity.FixedChrg = Double.valueOf(twoECForm.format(EntryActivity.FixedChrg));
        msg2= (string_append2("FC:", EntryActivity.FixedChrg.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());

        if(EntryActivity.EnrgyChrg.doubleValue()==0.00) {
            msg2 =  (string_append2("MC:", EntryActivity.MinChrg.toString())+ "\n");
        }
        else {
            msg2 =  (string_append2("EC:", EntryActivity.EnrgyChrg.toString())+ "\n");
        }
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("ED:", EntryActivity.ED.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Cess:", EntryActivity.Cess.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Mtr Rent:", String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getMeter_Rent())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("LT/WT SChg:", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Pen.Chg(E+F):", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("DLAdjAmt:", EntryActivity.DLAdjAmnt.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Rebate:", EntryActivity.rebate_val.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Misc Chg:", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("ASD Raise:", String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getAdditional_Security_Raised())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Sec.Intr:", String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getSD_Interest())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("VCA:", EntryActivity.VCA.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Cur Bill Amt:", EntryActivity.NetAmntExArr.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Sec.Arr:",  String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getSD_Arrear())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Arrear:",  String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getPrevious_Arrears())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("SDBG:", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("Bill L/G:", EntryActivity.BillLG.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("-----------------------"+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2=(string_append2("Net Amt:", EntryActivity.NetAmntInArr.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("-----------------------"+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("Lat:" + ConsFetchActivity.latitude + " Lon:" + ConsFetchActivity.longitude)+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("Due by Chq:" + EntryActivity.Duedt_Chq)+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("Due by Cash:" + EntryActivity.Duedt)+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("-----------------------"+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/
    }
    void Print_Stub()
    {
        String Stubmsg="";

        Stubmsg= (" Payment Receipt"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= ("------------------------"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());

        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "           Signature"+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "------------------------"+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
    }

    public void print_note_data()
    {
        String msg= ("JUSCO(OFFICE USE)"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("   Payment Receipt   "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("------------------------"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
       /* msg= ("Zone/DC Code:"+ConsFetchActivity.sbmRec.getZone_Code() +"\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
        msg= ("BP No:"+ConsFetchActivity.sbmRec.getBP_Number()+"\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("Bill Month:"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("Bill No "+EntryActivity.BillNo+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("Net Amt:"+EntryActivity.NetAmntInArr.toString()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("Gross Amt "+""+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "           Signature"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("------------------------"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("    BILL RECEIPT    "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("------------------------"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("Date:"+EntryActivity.Billdate+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
       /* msg= ("Zone:"+ConsFetchActivity.sbmRec.getZone_Code()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
        msg= ("BP No:"+ConsFetchActivity.sbmRec.getBP_Number()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
       /* msg= ("Bill Mnth:"+ConsFetchActivity.sbmRec.getBill_Month()+"-"+ConsFetchActivity.sbmRec.getBill_Year()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
        msg= ("Bill No:"+EntryActivity.BillNo+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("CurRDG&STS:"+EntryActivity.curread+"   "+EntryActivity.CurStatus+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "                Sign"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("------------------------"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());



    }
    public String string_append2(String msg1,String msg2)
    {
        String ConcatStr;
        StringBuffer strbuf=new StringBuffer();
        strbuf.append(msg1);
        int len2=strbuf.length();
        StringBuffer strbuf2=new StringBuffer();
        strbuf2.append(msg2);
        int len=strbuf2.length();
        while(len<28-len2)
        {
            strbuf=strbuf.append(" ");
            len++;
        }
        ConcatStr=strbuf.toString()+strbuf2.toString();
        return ConcatStr;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    void Print_dup_Stub()
    {
        /*String Stubmsg="";
        Stubmsg=(" Payment Receipt/ CounterFoil "+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=("---------------------------"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=("Ar/Grp:" + MRActivity.reco.Areacode + "/" + MRActivity.reco.Group + " Bk/SC:" + MRActivity.reco.Book + "/" + MRActivity.reco.servno+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=("Disc:" + DuplicateBill.billedRecords.Discdt + " Due:" + DuplicateBill.billedRecords.Duedt+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=("Pres Rdg:" + DuplicateBill.billedRecords.curread + " BillDt:" + DuplicateBill.billedRecords.Billdate+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=("Name: " + MRActivity.reco.Consname+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=(string_append2("Payable Amt:", DuplicateBill.billedRecords.NetAmntInArr.toString())+ "\n");
//        pos.POS_S_TextOut(Stubmsg, "US-ASCII", 0, 1, 1, 1, 0x08);
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=("Paid Dt:           Sign:"+ "\n");
//        pos.POS_S_TextOut(Stubmsg, "US-ASCII", 0, 1, 1, 1, 0x00);
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg=("---------------------------"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());*/
    }

    public void print_dup_msg2_data()
    {
        String msg2;
       /* Double FixedChrg =Double.parseDouble(uploadRec.getFC());
        DecimalFormat twoECForm = new DecimalFormat("#.##");
        FixedChrg = Double.valueOf(twoECForm.format(FixedChrg));*/

        /*Double rebate=Double.parseDouble(uploadRec.getRebate_EC())+Double.parseDouble(uploadRec.getRebate_FC())+Double.parseDouble(uploadRec.getRebate_other());
        DecimalFormat tworebForm = new DecimalFormat("#.##");
        rebate = Double.valueOf(tworebForm.format(rebate));

        msg2= (string_append2("   ", FixedChrg.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/
        /*if((Double.parseDouble(uploadRec.getEC())).doubleValue()==0.00) {
            msg2 =  (string_append2("   ", EntryActivity.MinChrg.toString())+ "\n");
        }
        else {*/
        /*msg2 =  (string_append2("   ", uploadRec.getEC())+ "\n");
//        }
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("   ", uploadRec.getDuty())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("     ", uploadRec.getCess())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", uploadRec.getMeter_Rent())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("           ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("             ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", uploadRec.getCredit_DLAmt())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("       ",  rebate.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/
        msg2= (string_append2("         ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        /*msg2= (string_append2("          ", sbmRec.getAdditional_Security_Raised()) + "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", sbmRec.getSD_Interest())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/
       /* msg2= (string_append2("    ", uploadRec.getVCA_Charge())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("             ", uploadRec.getTotal_Bill())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
      *//*  msg2= (string_append2("        ",  sbmRec.getSD_Arrear())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("       ",  sbmRec.getPrevious_Arrears())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*//*
        msg2= (string_append2("     ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/
        msg2= (string_append2("         ", uploadRec.getRound_Off_amount())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("                       "+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2=(string_append2("        ", uploadRec.getBill_Net_within_due_date())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("           ") + "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2(" ", uploadRec.getBill_Net_within_due_date()))+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (" "+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());

       /* msg2= ("Lat:" + ConsFetchActivity.latitude + " Lon:" + ConsFetchActivity.longitude)+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/

     /*   msg2= ("           " + uploadRec.getCheque_Due_Date())+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("            " + uploadRec.getCash_Due_Date())+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/
       /* msg2= (" "+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
    }

    public void print_dup_msg1_data()
    {
        sbmRec=DuplicateBill.sbmRec;
        uploadRec=DuplicateBill.uploadRec;
        String msg;
       /* msg = ("          JUSCO  \n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*/
        Print_logo();
        msg = ("--------DUPLICATE BILL--------\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        /*msg =  ("  "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*/
        msg="BillNo:" + uploadRec.getBILL_NO() + " Dt:" + uploadRec.getBill_Date()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="BPNo:" + uploadRec.getBP_Number()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Name:" + sbmRec.getBP_NAME()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Addr:" + sbmRec.getBP_ADDRESS1()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= sbmRec.getBP_ADDRESS2()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=sbmRec.getBP_ADDRESS3()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="MTRNO:"+ sbmRec.getMeter_Number() +"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Sanc.Load:" + sbmRec.getSanc_Load() +"KW"+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Category:" + sbmRec.getCATEGORY()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="BillCycle:" + sbmRec.getPOSTINGDATE() + "-" + uploadRec.getBill_Date()+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        Print_Dup_Image();
        msg="PrevRdg:" + sbmRec.getPREV_READ() + "Kwh"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="CurRdg:" + uploadRec.getCURR_READING() + "Kwh"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Units:" + uploadRec.getBILLED_UNITS() + "Kwh"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="MtrSts:" + Bill_Base[Integer.parseInt(uploadRec.getCur_Mtr_Sts())]+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="MD:" + uploadRec.getPres_Read_KW_RMD() + "KW"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Last 6 Months Consumption"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="1)" + sbmRec.getCons1() + " 2)" + sbmRec.getCons2() +" 3)" + sbmRec.getCons3() +"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="4)" + sbmRec.getCons4()+" 5)" + sbmRec.getCons5() + " 6)" + sbmRec.getCons6()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
//        Stubmsg=(string_append2("Payable Amt:", DuplicateBill.billedRecords.NetAmntInArr.toString())+ "\n");
        msg=string_append2("EC(@" + uploadRec.getCat_tariff() + " /Unit):", String.format("%.2f",Double.valueOf(uploadRec.getENERGY_CHARGE()))) +"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("JED:" , String.format("%.2f",Double.valueOf(uploadRec.getE_DUTY())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("Meter Rent:" , String.format("%.2f",Double.valueOf(sbmRec.getMETER_RENT())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("FC:" , String.format("%.2f",Double.valueOf(uploadRec.getFIXEDCHARGE())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("Rebate:" , String.format("%.2f",Double.valueOf(uploadRec.getTot_rebate())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("BillAmt:" , String.format("%.2f",Double.valueOf(uploadRec.getBill_Amt())))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("LPC:" , String.format("%.2f",Double.valueOf(sbmRec.getLPC())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("Oth:",String.format("%.2f",Double.valueOf("0.00")))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("SD.Intr:" , String.format("%.2f",Double.valueOf(sbmRec.getINTERESTONSD())))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("O/S:" , String.format("%.2f",Double.valueOf(uploadRec.getPREVIOUSOS())))+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg=string_append2("Total:" , String.format("%.2f",Double.valueOf(uploadRec.getTOTAL())))+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Due Dt:"+uploadRec.getBill_Due_date()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Lat:" + uploadRec.getLAT()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="Lon:" + uploadRec.getLONG()+"\n" ;
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="-------------------------"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg="\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
    }
    private void CheckBlueToothState() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        adapter.startDiscovery();
    }
    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bt.Close();
        Intent intent = new Intent(this,MRActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    void Print_Image()
    {
        //byte[] imgData=null;
        try {

            Bitmap  mBitmap = prepareImgToPrint(imagesDir + "/" + EntryActivity.imageFileName2);


            pos.POS_PrintPicture(mBitmap, 384, 0);


        } catch (Exception e) {

        }
    }



    void Print_Dup_Image()
    {
        try {
            Uri uri= Uri.parse(Environment.getExternalStorageDirectory() + "/" + uploadRec.getRdg_img_Path());
            performCrop(uri);
            Bitmap  mBitmap = prepareImgToPrint(Environment.getExternalStorageDirectory() + "/" + uploadRec.getRdg_img_Path());
            pos.POS_PrintPicture(mBitmap, 384, 384);
        } catch (Exception e) {

        }
    }

    public void Print_logo()
    {
        //byte[] imgData=null;
        try {
//            imgData = prepareImgToPrint(imagesDir + "/" + EntryActivity.imageFileName);
            String imageUri = "drawable://" + R.drawable.jusco_print;
            String imgpath=imageUri.toString();
//            Bitmap  mBitmap2 = prepareImgToPrint(imgpath);
            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),  R.drawable.jusco_print);
//            pos.POS_PrintBWPic(mBitmap, 408, 306);
//            Bitmap mBitmap2=prepareImgToPrint(imageUri);
            pos.POS_PrintPicture(mBitmap, 384, 0);
//            pos.POS_PrintBWPic(mBitmap, 384, 384);

        } catch (Exception e) {

        }
    }

    public Bitmap prepareImgToPrint(String imagepath) throws InterruptedException {
        Bitmap imagedata = BitmapFactory.decodeFile(imagepath);

//        Bitmap contrastbmp=adjustedContrast(imagedata,1.50);
//        Bitmap resizebmp = this.getResizedBitmapImage(imagedata, 90, 80);
//        Bitmap resizebmp = this.getResizedBitmapImage(imagedata, 384, 576);
//        Bitmap resizebmp =getResizedBitmap(imagedata, 90, 80);
//        return resizebmp;
        return imagedata;
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
    public void pictureprint(View v)
    {
        Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable()).getBitmap();
//        Bitmap mBmap=
        pos.POS_PrintBWPic(mBitmap, 384, 0);

    }

    public void preprint(View view)
    {
        if(text.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter text",Toast.LENGTH_SHORT).show();
        }
        else {
            String nm = "";
            pos.POS_S_TextOut(nm, "US-ASCII", 0, 1, 1, 1, 0x80);
            if(MRActivity.SearchFlag.equals("D")) {

                print_dup_msg1_data();
                print_dup_msg2_data();
                pre_Print_dup_Stub();
                pre_print_dup_note_data();
            }
            else {
                pre_print_msg1_data();
                /*pre_print_msg2_data();
                pre_Print_Stub();
                pre_print_note_data();*/
            }
        }

    }


    public void pre_print_msg1_data()
    {
        String msg;
        msg = ("            \n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg = ("            \n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("  "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
       /* msg =  ("        " + ConsFetchActivity.sbmRec.getZone_Code()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*/
        msg =  ("        " + MainActivity.Version+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("       " + MainActivity.MacID + "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("    " + EntryActivity.Billdate+" "+ EntryActivity.Billtime+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("         " + EntryActivity.BillNo+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("  "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
       /* msg =  ("          " + ConsFetchActivity.sbmRec.getRate_category()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*/
        msg =  ("          " + ConsFetchActivity.sbmRec.getBP_Number()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
       /* msg =  ("LNO: " + ConsFetchActivity.sbmRec.getLegacy_Number()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*/
        /*msg =  ("               "+ConsFetchActivity.sbmRec.getLast_Payment_Date() + "\n");//+ConsFetchActivity.sbmRec.getLast_Payment_Date().substring(0,4) +"/"+ConsFetchActivity.sbmRec.getLast_Payment_Date().substring(5,2)+"/"+ConsFetchActivity.sbmRec.getLast_Payment_Date().substring(8,2)
        pos.IO.Write(msg.getBytes(), 0, msg.length());*/
        msg =  ("  "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("  "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("  "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        /*msg =  (ConsFetchActivity.sbmRec.getName_of_Consumer()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  (ConsFetchActivity.sbmRec.getAddress1()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  (ConsFetchActivity.sbmRec.getAddress2()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("TelNo: " + EntryActivity.Telno+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("        "+ConsFetchActivity.sbmRec.getTariff_Code()+ "  "+ConsFetchActivity.sbmRec.getMeter_Phase()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
       *//* msg =  ("          " + " "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*//*

        msg =  ("       " + ConsFetchActivity.sbmRec.getPurpose()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("           " + ConsFetchActivity.input_Load+ "KW\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("         " + ConsFetchActivity.sbmRec.getSD_Held()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("      " + ConsFetchActivity.sbmRec.getMeterMake_and_No()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("          " + ConsFetchActivity.sbmRec.getAverage_Unit_for_Defective()+ "    " + String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getKWH_MF()))+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg =  ("      "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("         " + EntryActivity.curread + " " + EntryActivity.cur_bill_month+"-"+EntryActivity.cur_bill_year + " " +EntryActivity.CurStatus+"\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("         " + ConsFetchActivity.sbmRec.getMeter_Reading_Previous_KWH() + " " + ConsFetchActivity.sbmRec.getBill_Month() + "-" + ConsFetchActivity.sbmRec.getBill_Year() +" " +ConsFetchActivity.sbmRec.getMeter_Status()+"\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("        " + EntryActivity.BUnits+"\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("          " + "0.0"+ "       "+ ConsFetchActivity.sbmRec.getManual_Demand()+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("           " + EntryActivity.CurStatus+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());
       *//* msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0, msg.length());*//*
*/
    }
    public void pre_print_msg2_data()
    {
        String msg2;
        /*DecimalFormat twoECForm = new DecimalFormat("#.##");
        EntryActivity.FixedChrg = Double.valueOf(twoECForm.format(EntryActivity.FixedChrg));
        msg2= (string_append2("   ", EntryActivity.FixedChrg.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        if(EntryActivity.EnrgyChrg.doubleValue()==0.00) {
            msg2 =  (string_append2("   ", EntryActivity.MinChrg.toString())+ "\n");
        }
        else {
            msg2 =  (string_append2("   ", EntryActivity.EnrgyChrg.toString())+ "\n");
        }
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("   ", EntryActivity.ED.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("     ", EntryActivity.Cess.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getMeter_Rent())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("           ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("             ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", EntryActivity.DLAdjAmnt.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("       ", EntryActivity.rebate_val.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("          ", String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getAdditional_Security_Raised())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getSD_Interest())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("    ", EntryActivity.VCA.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("             ", EntryActivity.NetAmntExArr.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("        ",  String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getSD_Arrear())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("       ",  String.valueOf(Double.parseDouble(ConsFetchActivity.sbmRec.getPrevious_Arrears())))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("     ", "0.00")+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2("         ", String.format("%.2f",EntryActivity.BillLG))+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("                       "+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2=(string_append2("        ", EntryActivity.NetAmntInArr.toString())+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("           ") + "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (string_append2(" " , EntryActivity.NetAmntInArr.toString()))+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= (" "+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());

       *//* msg2= ("Lat:" + ConsFetchActivity.latitude + " Lon:" + ConsFetchActivity.longitude)+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*//*

        msg2= ("           " + EntryActivity.Duedt_Chq)+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= ("            " + EntryActivity.Duedt)+ "\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
       *//* msg2= (" "+ "\n");
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*//*
        msg2= "  "+"\n";
        pos.IO.Write(msg2.getBytes(), 0, msg2.length());*/

    }
    public void pre_Print_Stub()
    {
        String Stubmsg="";

       /* Stubmsg= (" Payment Receipt"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= ("------------------------"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());*/

        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "           "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
    }

    public void pre_print_note_data()
    {
        /*String msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("      "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        *//*msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("      "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*//*
        msg= ("             "+ConsFetchActivity.sbmRec.getZone_Code() +"\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("      "+ConsFetchActivity.sbmRec.getBP_Number()+"\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("           "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+EntryActivity.BillNo+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+EntryActivity.NetAmntInArr.toString()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("          "+""+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("     "+EntryActivity.Billdate+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("     "+ConsFetchActivity.sbmRec.getZone_Code()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("      "+ConsFetchActivity.sbmRec.getBP_Number()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("          "+ConsFetchActivity.sbmRec.getBill_Month()+"-"+ConsFetchActivity.sbmRec.getBill_Year()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+EntryActivity.BillNo+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("           "+EntryActivity.curread+"   "+EntryActivity.CurStatus+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
       *//* msg= "                Sign"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("------------------------"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*//*
       *//* msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
    }
    public void pre_Print_dup_Stub()
    {
        String Stubmsg="";

       /* Stubmsg= (" Payment Receipt"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= ("------------------------"+ "\n");
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());*/

        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "  "+"\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= "           "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
        Stubmsg= " "+ "\n";
        pos.IO.Write(Stubmsg.getBytes(), 0, Stubmsg.length());
    }

    public void pre_print_dup_note_data()
    {
        String msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("      "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        /*msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("      "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
       /* msg= ("             "+uploadRec.getZone_Name() +"\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
        msg= ("      "+uploadRec.getBP_Number()+"\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("           "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+uploadRec.getBILL_NO()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+uploadRec.getBill_Net_within_due_date()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("          "+""+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= "                    "+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= (" "+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("     "+uploadRec.getBill_Date()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
       /* msg= ("     "+uploadRec.getZone_Name()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("      "+uploadRec.getBP_Number()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("          "+uploadRec.getCur_Bill_Month()+"-"+uploadRec.getCur_Bill_Year()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("        "+uploadRec.getBILL_NO()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= ("           "+uploadRec.getPres_Read_KWH()+"   "+uploadRec.getCur_Mtr_Sts()+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
       /* msg= "                Sign"+"\n";
        pos.IO.Write(msg.getBytes(), 0, msg.length());
        msg= ("------------------------"+ "\n");
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
       /* msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());
        msg= "  "+ "\n";
        pos.IO.Write(msg.getBytes(), 0,msg.length());*/
    }

    public String connect_printer(BTPrinting bt)
    {
        String sts="0";

        File myFile=new File(DATA_PATH1+"BTaddress2.txt");
        if(myFile.exists()) {
            try {

                FileInputStream fstream = new FileInputStream(DATA_PATH1
                        + "BTaddress2.txt");
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;

                while ((strLine = br.readLine()) != null) {
//                btaddr.setText(strLine);
                    address = strLine;
                }
                sts = "C";
                in.close();
            } catch (Exception e) {// Catch exception if any
                sts = "D";
                System.err.println("Error: " + e.getMessage());
            }
//        CheckBlueToothState();

            pos.Set(bt);

            if (address.equals("")) {
                sts = "D";
                Toast.makeText(getApplicationContext(), "Please enter Bluetooth printer address", Toast.LENGTH_LONG).show();
            } else {
                String BTAddress = address;
                if (bt.Open(BTAddress)) {
                    sts = "C";
//                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                } else {
                    sts = "D";
//                    Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            sts="E";
        }
        return sts;
    }

    public void print_picture(String path)
    {
        String picturePath = path;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, opts);
        opts.inJustDecodeBounds = false;
        WorkService.workThread.connectBt("02:0B:3D:D1:B8:B0");
        if (opts.outWidth > 1200) {
            opts.inSampleSize = opts.outWidth / 1200;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, opts);
        if (null != bitmap) {
            int nPaperWidth = 384;

            WorkService.cb = new IOCallBack() {
                // WorkThread线程回调
                public void OnOpen() {
                    // TODO Auto-generated method stub
                    if(null != mHandler)
                    {
                        Message msg = mHandler.obtainMessage(Global.MSG_IO_ONOPEN);
                        mHandler.sendMessage(msg);
                    }
                }

                public void OnClose() {
                    // TODO Auto-generated method stub
                    if(null != mHandler)
                    {
                        Message msg = mHandler.obtainMessage(Global.MSG_IO_ONCLOSE);
                        mHandler.sendMessage(msg);
                    }
                }
            };
            mHandler = new MHandler(this);
            WorkService.addHandler(mHandler);
            if (WorkService.workThread.isConnected()) {
                Bundle data = new Bundle();
                // data.putParcelable(Global.OBJECT1, mBitmap);
                data.putParcelable(Global.PARCE1, bitmap);
                data.putInt(Global.INTPARA1, nPaperWidth);
                data.putInt(Global.INTPARA2, 0);
                WorkService.workThread.handleCmd(
                        Global.CMD_POS_PRINTPICTURE, data);
            } else {
                Toast.makeText(this, Global.toast_notconnect,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class MHandler extends Handler {

        WeakReference<ThermalPrint> mActivity;

        MHandler(ThermalPrint activity) {
            mActivity = new WeakReference<ThermalPrint>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ThermalPrint theActivity = mActivity.get();
            switch (msg.what) {

                case Global.CMD_POS_PRINTPICTURERESULT: {
                    int result = msg.arg1;
                    Toast.makeText(
                            theActivity,
                            (result == 1) ? Global.toast_success
                                    : Global.toast_fail, Toast.LENGTH_SHORT).show();
                    Log.v("THER", "Result: " + result);
                    break;
                }

            }
        }
    }


    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public Bitmap supress_green(String Path)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(Path);; //Assuming you have a bitmap somehow
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                bitmap.setPixel(x, y, bitmap.getPixel(x, y) & 0x00FF00);
            }
        }
        return bitmap;
    }

    private Bitmap adjustedContrast(Bitmap src, double value)
    {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.green(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.blue(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public static Bitmap getResizedBitmap(final Bitmap bm, final int newHeight, final int newWidth) {

        String reSizeSave = null;
        final int width = bm.getWidth();
        final int height = bm.getHeight();
        final float scaleWidth = newWidth / (float)width;
        final float scaleHeight = newHeight / (float)height;
        final Size imgSize = new Size((double)newHeight, (double)newWidth);
        final Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        final Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        final Mat resizeMat = new Mat();
        Utils.bitmapToMat(resizedBitmap, resizeMat);
        final File resizeFile = new File(ThermalPrint.imagesDir2, "resize.jpg");
        reSizeSave = resizeFile.getPath();
        Highgui.imwrite(reSizeSave, resizeMat);
        final int a = 0;
        final Mat grey = new Mat(imgSize, 0);
        final Mat morphologyImgMat = new Mat(imgSize, 0);
        final Bitmap greyBitmap = Bitmap.createBitmap(resizeMat.width(), resizeMat.height(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(resizeMat, grey, 7);
        final Bitmap morpBitmap = Bitmap.createBitmap(resizeMat.width(), resizeMat.height(), Bitmap.Config.ARGB_8888);
        Imgproc.equalizeHist(grey, grey);
        Imgproc.threshold(grey, morphologyImgMat, ThermalPrint.minThresh, ThermalPrint.maxThresh, 1);
        Utils.matToBitmap(morphologyImgMat, morpBitmap);
        Highgui.imwrite(ThermalPrint.morphImgSave, morphologyImgMat);
        final StringBuffer sb = new StringBuffer();
        final StringBuffer sbHex = new StringBuffer();
        int b = 0;
        final int len = ThermalPrint.imgwidth / 8 * ThermalPrint.imgheight;
        final byte[] bArry = new byte[len];
        int hexInt = 0;
        for (int x = 0; x < ThermalPrint.imgheight; ++x) {
            for (int y = 0; y < ThermalPrint.imgwidth; ++y) {
                final int clr = morpBitmap.getPixel(y, x) & 0xFF;
                if (clr == 0) {
                    sb.append("1");
                    b = (byte)(b << 1);
                }
                else if (clr == 255) {
                    sb.append("0");
                    b = (byte)((b << 1) + 1);
                }
                else {
                    sb.append("x");
                }
                if (y % 8 == 7) {
                    bArry[hexInt] = (byte)b;
                    ++hexInt;
                    b = 0;
                }
            }
        }
//        final ConvertBinary2Hexadecimal b2hex = new ConvertBinary2Hexadecimal();
//        Bluetooth_Img_ThermalAPI.Hexstr = ConvertBinary2Hexadecimal.convertBinary2Hexadecimal(bArry);
//        Bluetooth_Img_ThermalAPI.flag = true;
        return resizedBitmap;
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            } else {
                handleSendRaw(intent);
            }
        }

    }

    private void handleSendText(Intent intent) {
        Uri textUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (textUri != null) {
            // Update UI to reflect text being shared

            if (WorkService.workThread.isConnected()) {
                byte[] buffer = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x39, 0x01 }; // 设置中文，切换双字节编码。
                Bundle data = new Bundle();
                data.putByteArray(Global.BYTESPARA1, buffer);
                data.putInt(Global.INTPARA1, 0);
                data.putInt(Global.INTPARA2, buffer.length);
                WorkService.workThread.handleCmd(Global.CMD_POS_WRITE, data);
            }
            if (WorkService.workThread.isConnected()) {
                String path = textUri.getPath();
                String strText = FileUtils.ReadToString(path);
                byte buffer[] = strText.getBytes();

                Bundle data = new Bundle();
                data.putByteArray(Global.BYTESPARA1, buffer);
                data.putInt(Global.INTPARA1, 0);
                data.putInt(Global.INTPARA2, buffer.length);
                data.putInt(Global.INTPARA3, 128);
                WorkService.workThread.handleCmd(
                        Global.CMD_POS_WRITE_BT_FLOWCONTROL, data);

            } else {
                Toast.makeText(this, Global.toast_notconnect,
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    }

    private void handleSendRaw(Intent intent) {
        Uri textUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (textUri != null) {
            // Update UI to reflect text being shared
            if (WorkService.workThread.isConnected()) {
                String path = textUri.getPath();
                byte buffer[] = FileUtils.ReadToMem(path);
                // Toast.makeText(this, "length:" + buffer.length,
                // Toast.LENGTH_LONG).show();
                Bundle data = new Bundle();
                data.putByteArray(Global.BYTESPARA1, buffer);
                data.putInt(Global.INTPARA1, 0);
                data.putInt(Global.INTPARA2, buffer.length);
                data.putInt(Global.INTPARA3, 256);
                WorkService.workThread.handleCmd(
                        Global.CMD_POS_WRITE_BT_FLOWCONTROL, data);

            } else {
                Toast.makeText(this, Global.toast_notconnect,
                        Toast.LENGTH_SHORT).show();
            }

            // finish();
        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

//            String path = getRealPathFromURI(imageUri);
            String path = imagesDir+"/"+EntryActivity.imageFileName;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            opts.inJustDecodeBounds = false;
            if (opts.outWidth > 1200) {
                opts.inSampleSize = opts.outWidth / 1200;
            }

            Bitmap mBitmap = BitmapFactory.decodeFile(path);

            if (mBitmap != null) {
                if (WorkService.workThread.isConnected()) {
                    Bundle data = new Bundle();
                    data.putParcelable(Global.PARCE1, mBitmap);
                    data.putInt(Global.INTPARA1, 384);
                    data.putInt(Global.INTPARA2, 0);
                    WorkService.workThread.handleCmd(
                            Global.CMD_POS_PRINTPICTURE, data);
                } else {
                    Toast.makeText(this, Global.toast_notconnect,
                            Toast.LENGTH_SHORT).show();
                }
            }
            finish();

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.MediaColumns.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null,
                null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
    private void InitGlobalString() {
        Global.toast_success = getString(R.string.toast_success);
        Global.toast_fail = getString(R.string.toast_fail);
        Global.toast_notconnect = getString(R.string.toast_notconnect);
        Global.toast_usbpermit = getString(R.string.toast_usbpermit);
    }


    public void Print_logo2(Resources Path) {

        try {
            String imageUri = "drawable://" + R.drawable.jusco_print;
            Bitmap mBitmap = BitmapFactory.decodeResource(Path, R.drawable.jusco_print);
            pos.POS_PrintPicture(mBitmap, 384, 0);
        } catch (Exception e) {

        }
    }
}
