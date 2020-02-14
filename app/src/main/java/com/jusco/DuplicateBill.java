package com.jusco;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class DuplicateBill extends AppCompatActivity {
    //public static sbmrecords record=new sbmrecords();
    SQLiteDatabase db;
    EditText eScno;
    TextView DScno,Dbook;
    Button ISB;
    public static BilledRecords billedRecords;
    public static uploadRec uploadRec;
    public static SbmRec sbmRec;
    public static String DBNAME="JUSCODB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplicate_bill);
        billedRecords=new BilledRecords();
        eScno=(EditText)findViewById(R.id.etScno);
        DScno=(TextView)findViewById(R.id.txtScno);
        /* Dbook=(TextView)findViewById(R.id.txtBook);*/
        ISB=(Button)findViewById(R.id.btnDupBill);



    }

    public int read_record_sbmtopc() {
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String scno1=eScno.getText().toString();
        String aBuffer = "";
        int Flag = 0;
        uploadRec= new uploadRec();
        db = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        try {
            Cursor c = db.rawQuery("SELECT * FROM sbmtopc WHERE bp_number='" + scno1 + "'", null);
            if(c.moveToFirst()) {

                uploadRec.setBP_Number(c.getString(0));
                uploadRec.setCURR_READING(c.getString(1));
                uploadRec.setMRNOTE(c.getString(2));
                uploadRec.setBILLED_UNITS(c.getString(3));
                uploadRec.setNETUNITS(c.getString(4));
                uploadRec.setENERGY_CHARGE(c.getString(5));
                uploadRec.setE_DUTY(c.getString(6));
                uploadRec.setMETER_RENT(c.getString(7));
                uploadRec.setFIXEDCHARGE(c.getString(8));
                uploadRec.setLPC(c.getString(9));
                uploadRec.setINTERESTONSD(c.getString(10));
                uploadRec.setTDS(c.getString(11));
                uploadRec.setRebateEarly(c.getString(12));
                uploadRec.setRebateDigital(c.getString(13));
                uploadRec.setTot_rebate(c.getString(14));
                uploadRec.setOTHERRECEIVABLE(c.getString(15));
                uploadRec.setPREVIOUSOS(c.getString(16));
                uploadRec.setFPPPACharg(c.getString(17));
                uploadRec.setBill_Amt(c.getString(18));
                uploadRec.setTOTAL(c.getString(19));
                uploadRec.setSBM_NO(c.getString(20));
                uploadRec.setLAT(c.getString(21));
                uploadRec.setLONG(c.getString(22));
                uploadRec.setSBM_Sw_Ver(c.getString(23));
                uploadRec.setBILL_NO(c.getString(24));
                uploadRec.setBill_Date(c.getString(25));
                uploadRec.setBill_Time(c.getString(26));
                uploadRec.setCons_Mob_No(c.getString(27));
                uploadRec.setCur_Mtr_Sts(c.getString(28));
                uploadRec.setPrev_Read(c.getString(29));
                uploadRec.setPres_Read_KW_RMD(c.getString(30));
                uploadRec.setCur_PF(c.getString(31));
                uploadRec.setBill_Due_date(c.getString(32));
                uploadRec.setRound_Off_amount(c.getString(33));
                uploadRec.setBill_Net_within_due_date(c.getString(34));
                uploadRec.setBill_Amount_After_Due_Date(c.getString(35));
                uploadRec.setBill_Generation_Status(c.getString(36));
                uploadRec.setMeter_Reader_Name(c.getString(37));
                uploadRec.setObr_Code(c.getString(38));
                uploadRec.setOnline_Flag_number(c.getString(39));
                uploadRec.setCat_tariff(c.getString(c.getColumnIndex("cat_tarriff")));
                uploadRec.setRdg_img_Path(c.getString(41));

                Cursor c1 = db.rawQuery("SELECT * FROM pctosbm WHERE bp_number='" + scno1 + "'", null);
                if(c1.moveToFirst()) {
                    Read_data_DB(c1);
                }
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
//        try {
//            File myFile = new File("/sdcard/DVVNL/Output/sbmtopc.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//            while ((aDataRow = myReader.readLine()) != null) {
//                //aBuffer += aDataRow + "\n";
//                String scno = aDataRow.substring(0, 6);
//                if (scno.equals(eScno.getText().toString())) {
//                    read_sbm_to_pc(aDataRow);
//                    Flag = 1;
//                    break;
//                }
//            }
//            myReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return Flag;
    }
    public void read_record_external()
    {
//        StringBuffer stringBuffer = new StringBuffer();
//        String aDataRow = "";
//        String aBuffer = "";
//
//        try {
//            File myFile = new File("/sdcard/pctosbm.txt");
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//
//            while ((aDataRow = myReader.readLine()) != null) {
//                //aBuffer += aDataRow + "\n";
//                String scno = aDataRow.substring(0, 6);
//                if (scno.equals(eScno.getText().toString())) {
//                    Read_data(aDataRow);
//                    break;
//                }
//            }
//            myReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void Read_data(String str) {
        /*MRActivity.reco.setServno(str.substring(0, 6).trim());
        MRActivity.reco.setBook(str.substring(6, 10).trim());
        MRActivity.reco.setSeqno("0");
        MRActivity.reco.setDTCode(str.substring(14, 25).trim());
        MRActivity.reco.setPolecode(str.substring(25, 36).trim());
        MRActivity.reco.setAreacode(str.substring(36, 42).trim());
        MRActivity.reco.setLPNo(str.substring(42, 52).trim());
        MRActivity.reco.setConsname(str.substring(52, 72).trim());
        MRActivity.reco.setAddr1(str.substring(72, 92).trim());
        MRActivity.reco.setAddr2(str.substring(92, 112).trim());
        MRActivity.reco.setCat(str.substring(132, 136).trim());
        //record.setPF(str.substring(136, 141).trim());
        MRActivity.reco.setMF(str.substring(141, 143).trim());
        MRActivity.reco.setSload(str.substring(145, 148).trim());
        MRActivity.reco.setMtrno(str.substring(152, 158).trim());
        MRActivity.reco.setPrevrdgdt(str.substring(162, 170).trim());
        String Prdt=MRActivity.reco.Prevrdgdt.substring(0,2) + "/" +MRActivity.reco.Prevrdgdt.substring(2,4)+"/" +MRActivity.reco.Prevrdgdt.substring(4,8);
        MRActivity.reco.setPrevrdgdt(Prdt);
        MRActivity.reco.setPrevreading(str.substring(170, 175).trim());
        MRActivity.reco.setPrevsts(str.substring(176, 177).trim());
        MRActivity.reco.setArrearsOld(str.substring(208, 219).trim());
        MRActivity.reco.setArrearsCur(str.substring(219, 230).trim());
        MRActivity.reco.setIntPRevArrs(str.substring(230, 239).trim());
        MRActivity.reco.setIntPEDArrs(str.substring(239, 246).trim());
        MRActivity.reco.setDIntPEDArrs(Double.parseDouble(MRActivity.reco.IntPEDArrs));
        MRActivity.reco.setDLAdjAmntRev(str.substring(246, 255).trim());
        MRActivity.reco.setDLAdjAmntED(str.substring(255, 262).trim());
        MRActivity.reco.setFlags(str.substring(262, 263).trim());
        MRActivity.reco.setNoAC(str.substring(266, 268).trim());
        MRActivity.reco.setSec(str.substring(268, 270).trim());
        MRActivity.reco.setGroup(str.substring(270, 271).trim());
        MRActivity.reco.setDivsn(str.substring(271, 273).trim());
        MRActivity.reco.setChqDisFlg(str.substring(273, 275).trim());
        MRActivity.reco.setBankCode(str.substring(275, 284).trim());
        MRActivity.reco.setAssdUnits(str.substring(284, 290).trim());
        MRActivity.reco.setAssdAmnt(str.substring(290, 299).trim());
        MRActivity.reco.setDLAdjUnits(str.substring(299, 305).trim());
        MRActivity.reco.setSDAmnt(str.substring(305, 314).trim());
        MRActivity.reco.setSupDate(str.substring(314, 322).trim());
        MRActivity.reco.setPayRcvDate(str.substring(322, 330).trim());
        MRActivity.reco.setExtraF1(str.substring(330, 341).trim());
        MRActivity.reco.setExtraData1(str.substring(350, 361).trim());
        MRActivity.reco.setExtraData2(str.substring(361, 370).trim());
        MRActivity.reco.setBillBaseFlg(str.substring(370, 371).trim());
        MRActivity.reco.setMtrCost(str.substring(371, 380).trim());
        MRActivity.reco.setAssdAmnt(str.substring(380, 389).trim());
        MRActivity.reco.setSBAssID(str.substring(389, 393).trim());
        MRActivity.reco.setPrevCMD(str.substring(393, 400).trim());
        MRActivity.reco.setArrcnt(str.substring(393, 400).trim());
        MRActivity.reco.setLastPaidAmnt(str.substring(400, 409).trim());
        MRActivity.reco.setOldMtrClReading(str.substring(409, 415).trim());
        MRActivity.reco.setMC(str.substring(415, 423).trim());
        MRActivity.reco.setNewMtrNo(str.substring(423, 433).trim());
       MRActivity.reco.setOTS_Amt(str.substring(433, 443).trim());
//       MRActivity.reco.setOTS_Amt(str.substring(443, 453).trim());


        MRActivity.reco.setPrevAdjUnits("0");
        //record.setTelephoneno(str.substring(453,464).trim());
        //record.setfRebate_amt(str.substring(464,474).trim());*/
    }
    public void read_sbm_to_pc(String BilledRecord)
    {
//        String[] BilledRec= BilledRecord.split("$");
//        StringTokenizer st = new StringTokenizer(BilledRecord,"$");
//        billedRecords.servno=st.nextToken();
//        billedRecords.Book=st.nextToken();
//        billedRecords.SeqNo=st.nextToken();
//        billedRecords.Billdate=st.nextToken();
//        billedRecords.Billtime=st.nextToken();
//        billedRecords.BillNo=st.nextToken();
//        billedRecords.BMnths=st.nextToken();
//        billedRecords.MnthMin=st.nextToken();
//        billedRecords.Sload=st.nextToken();
//        billedRecords.CurLoad=st.nextToken();
//        billedRecords.CurPF=st.nextToken();
//        billedRecords.curread=st.nextToken();
//        billedRecords.CurStatus=st.nextToken();
//        billedRecords.BUnits=st.nextToken();
//        billedRecords.PrevAdjUnits=st.nextToken();
//        billedRecords.FixedChrg=st.nextToken();
//        billedRecords.EnrgyChrg=st.nextToken();
//        billedRecords.str1=st.nextToken();
//        billedRecords.ED=st.nextToken();
//        billedRecords.CurIntrest=st.nextToken();
//        billedRecords.IntPEDArrs=st.nextToken();
//        billedRecords.str2=st.nextToken();
//        billedRecords.DLAdjAmntED=st.nextToken();
//        billedRecords.str3=st.nextToken();
//        billedRecords.Group=st.nextToken();
//        billedRecords.str4=st.nextToken();
//        billedRecords.BillLG=st.nextToken();
//        billedRecords.NetAmntExArr=st.nextToken();
//        billedRecords.ArrearsOld=st.nextToken();
//        billedRecords.ArrearsCur=st.nextToken();
//        billedRecords.NetAmntInArr=st.nextToken();
//        billedRecords.str5=st.nextToken();
//        billedRecords.str6=st.nextToken();
//        billedRecords.str7=st.nextToken();
//        billedRecords.str8=st.nextToken();
//        billedRecords.str9=st.nextToken();
//        billedRecords.str10=st.nextToken();
//        billedRecords.AssdUnits=st.nextToken();
//        billedRecords.str11=st.nextToken();
//        billedRecords.str12=st.nextToken();
//        billedRecords.ObrCode=st.nextToken();
//        billedRecords.SBA_ID=st.nextToken();
//        billedRecords.MacID=st.nextToken();
//        billedRecords.str13=st.nextToken();
//        billedRecords.Duedt=st.nextToken();
//        billedRecords.Discdt=st.nextToken();
//        billedRecords.str14=st.nextToken();
//        billedRecords.Prevreading=st.nextToken();
//        billedRecords.ArrNR=st.nextToken();
//        billedRecords.CapChrg=st.nextToken();
//        billedRecords.str15=st.nextToken();
//        billedRecords.str16=st.nextToken();
//        billedRecords.MtrCost=st.nextToken();
//        billedRecords.RODO_Amt=st.nextToken();
//        billedRecords.Regulatory_ch=st.nextToken();
//        billedRecords.str17=st.nextToken();
//        billedRecords.Telno=st.nextToken();
//        billedRecords.fRebateB_amt=st.nextToken();
//        billedRecords.MinChrg=st.nextToken();
//        billedRecords.imageFileName=st.nextToken();
//        billedRecords.latitude=st.nextToken();
//        billedRecords.longitude=st.nextToken();
    }

    public void read_sbm_2_pc(Cursor c)
    {
        billedRecords.servno=c.getString(c.getColumnIndex("SCNO"));
        billedRecords.Book=c.getString(c.getColumnIndex("BOOK"));
        billedRecords.SeqNo=c.getString(c.getColumnIndex("SEQNO"));
        billedRecords.Billdate=c.getString(c.getColumnIndex("Bdate"));
        billedRecords.Billtime=c.getString(c.getColumnIndex("Btime"));
        billedRecords.BillNo=c.getString(c.getColumnIndex("BillNo"));
        billedRecords.BMnths=c.getString(c.getColumnIndex("BMnths"));
        billedRecords.MnthMin=c.getString(c.getColumnIndex("MnthMin"));
        billedRecords.Sload=c.getString(c.getColumnIndex("SLoad"));
        billedRecords.CurLoad=c.getString(c.getColumnIndex("CurLoad"));
        billedRecords.CurPF=c.getString(c.getColumnIndex("CurPF"));
        billedRecords.curread=c.getString(c.getColumnIndex("ClRead"));
        billedRecords.CurStatus=c.getString(c.getColumnIndex("ClStatus"));
        billedRecords.BUnits=c.getString(c.getColumnIndex("BUnits"));
        billedRecords.PrevAdjUnits=c.getString(c.getColumnIndex("PrevAdjUnits"));
        billedRecords.FixedChrg=c.getString(c.getColumnIndex("FixedChrg"));
        billedRecords.EnrgyChrg=c.getString(c.getColumnIndex("EnrgyChrg"));
        billedRecords.str1=c.getString(c.getColumnIndex("Rebate"));
        billedRecords.ED=c.getString(c.getColumnIndex("ED"));
        billedRecords.CurIntrest=c.getString(c.getColumnIndex("CurIntrest"));
        billedRecords.IntPEDArrs=c.getString(c.getColumnIndex("IntPEDArrs"));
        billedRecords.str2=c.getString(c.getColumnIndex("DLAdjAmntCC"));
        billedRecords.DLAdjAmntED=c.getString(c.getColumnIndex("DLAdjAmntED"));
        billedRecords.str3=c.getString(c.getColumnIndex("temp"));
        billedRecords.Group=c.getString(c.getColumnIndex("Grp"));
        billedRecords.str4=c.getString(c.getColumnIndex("ExtAmnt2"));
        billedRecords.BillLG=c.getString(c.getColumnIndex("BillLG"));
        billedRecords.NetAmntExArr=c.getString(c.getColumnIndex("NetAmntExArr"));
        billedRecords.ArrearsOld=c.getString(c.getColumnIndex("PArrsOldFY"));
        billedRecords.ArrearsCur=c.getString(c.getColumnIndex("PArrsCurFY"));
        billedRecords.NetAmntInArr=c.getString(c.getColumnIndex("NetAmntInArr"));
        billedRecords.str5=c.getString(c.getColumnIndex("BPFlag"));
        billedRecords.str6=c.getString(c.getColumnIndex("RcptNo"));
        billedRecords.str7=c.getString(c.getColumnIndex("AmntPaid"));
        billedRecords.str8=c.getString(c.getColumnIndex("ChqNo"));
        billedRecords.str9=c.getString(c.getColumnIndex("BankName"));
        billedRecords.str10=c.getString(c.getColumnIndex("BankCode"));
        billedRecords.AssdUnits=c.getString(c.getColumnIndex("AssdUnits"));
        billedRecords.str11=c.getString(c.getColumnIndex("AssdAmnt"));
        billedRecords.str12=c.getString(c.getColumnIndex("ChqDate"));
        billedRecords.ObrCode=c.getString(c.getColumnIndex("Rmrks"));
        billedRecords.SBA_ID=c.getString(c.getColumnIndex("SBAssID"));
        billedRecords.MacID=c.getString(c.getColumnIndex("SBMchnID"));
        billedRecords.str13=c.getString(c.getColumnIndex("LPNo"));
        billedRecords.Duedt=c.getString(c.getColumnIndex("Due"));
        billedRecords.Discdt=c.getString(c.getColumnIndex("Disc"));
        billedRecords.str14=c.getString(c.getColumnIndex("CurCMD"));
        billedRecords.Prevreading=c.getString(c.getColumnIndex("PrevReading"));
        billedRecords.ArrNR=c.getString(c.getColumnIndex("ArrearNR"));
        billedRecords.CapChrg=c.getString(c.getColumnIndex("CapChrg"));
        billedRecords.str15=c.getString(c.getColumnIndex("Flag"));
        billedRecords.str16=c.getString(c.getColumnIndex("UFlag"));
        billedRecords.MtrCost=c.getString(c.getColumnIndex("MtrRent"));
        billedRecords.RODO_Amt=c.getString(c.getColumnIndex("RODO_Amt"));
        billedRecords.Regulatory_ch=c.getString(c.getColumnIndex("freg_chrg"));
        billedRecords.str17=c.getString(c.getColumnIndex("USRNO"));
        billedRecords.Telno=c.getString(c.getColumnIndex("Telephoneno"));
        billedRecords.fRebateB_amt=c.getString(c.getColumnIndex("fRebateB_amt"));
        billedRecords.MinChrg=c.getString(c.getColumnIndex("MinChrg"));
        billedRecords.imageFileName=c.getString(c.getColumnIndex("image1"));
        billedRecords.OTS_Amt=c.getString(c.getColumnIndex("RODO_AMT"));
//        billedRecords.latitude=st.nextToken();
//        billedRecords.longitude=st.nextToken();
    }
    public void Read_data_DB(Cursor c) {
        sbmRec = new SbmRec();
        sbmRec.setBP_Number(c.getString(c.getColumnIndex("BP_Number")));
        sbmRec.setBP_NAME(c.getString(c.getColumnIndex("BP_NAME")));
        sbmRec.setBP_ADDRESS1(c.getString(c.getColumnIndex("BP_ADDRESS1")));
        sbmRec.setBP_ADDRESS2(c.getString(c.getColumnIndex("BP_ADDRESS2")));
        sbmRec.setBP_ADDRESS3(c.getString(c.getColumnIndex("BP_ADDRESS3")));
        sbmRec.setSanc_Load(c.getString(c.getColumnIndex("Sanc_Load")));
        sbmRec.setMeter_Number(c.getString(c.getColumnIndex("Meter_Number")));
        sbmRec.setMobile_Number(c.getString(c.getColumnIndex("Mobile_Number")));
        sbmRec.setMeter_Phase(c.getString(c.getColumnIndex("Meter_Phase")));
        sbmRec.setDTR_Number(c.getString(c.getColumnIndex("DTR_Number")));
        sbmRec.setFeeder_Name(c.getString(c.getColumnIndex("Feeder_Name")));
        sbmRec.setDTR_Location(c.getString(c.getColumnIndex("DTR_Location")));
        sbmRec.setPrev_Billing_Date(c.getString(c.getColumnIndex("Prev_Billing_Date")));
        sbmRec.setCons1(c.getString(c.getColumnIndex("Cons1")));
        sbmRec.setCons2(c.getString(c.getColumnIndex("Cons2")));
        sbmRec.setCons3(c.getString(c.getColumnIndex("Cons3")));
        sbmRec.setCons4(c.getString(c.getColumnIndex("Cons4")));
        sbmRec.setCons5(c.getString(c.getColumnIndex("Cons5")));
        sbmRec.setCons6(c.getString(c.getColumnIndex("Cons6")));
        sbmRec.setPOSTINGDATE(c.getString(c.getColumnIndex("POSTINGDATE")));
        sbmRec.setCON_DATE(c.getString(c.getColumnIndex("CON_DATE")));
        sbmRec.setDL_COUNTER(c.getString(c.getColumnIndex("DL_COUNTER")));
        sbmRec.setCATEGORY(c.getString(c.getColumnIndex("CATEGORY")));
        sbmRec.setE_Duty_Identifier(c.getString(c.getColumnIndex("E_Duty_Identifier")));
        sbmRec.setPREV_READ(c.getString(c.getColumnIndex("PREV_READ")));
        sbmRec.setMRNOTE(c.getString(c.getColumnIndex("MRNOTE")));
        sbmRec.setAMOUNT01(c.getString(c.getColumnIndex("AMOUNT01")));
        sbmRec.setAMOUNT02(c.getString(c.getColumnIndex("AMOUNT02")));
        sbmRec.setMF(c.getString(c.getColumnIndex("MF")));
        sbmRec.setMETER_RENT(c.getString(c.getColumnIndex("METER_RENT")));
        sbmRec.setLPC(c.getString(c.getColumnIndex("LPC")));
        sbmRec.setINTERESTONSD(c.getString(c.getColumnIndex("INTERESTONSD")));
        sbmRec.setTDS(c.getString(c.getColumnIndex("TDS")));
        sbmRec.setRebateEarly(c.getString(c.getColumnIndex("RebateEarly")));
        sbmRec.setRebateDigital(c.getString(c.getColumnIndex("RebateDigital")));
        sbmRec.setOTHERRECEIVABLE(c.getString(c.getColumnIndex("OTHERRECEIVABLE")));
        sbmRec.setPREVIOUSOS(c.getString(c.getColumnIndex("PREVIOUSOS")));
        sbmRec.setFPPPACharg(c.getString(c.getColumnIndex("FPPPACharg")));
        sbmRec.setOffice_Addr(c.getString(c.getColumnIndex("office_Addr")));
        sbmRec.setOffice_Phone(c.getString(c.getColumnIndex("office_Phone")));
        sbmRec.setPrev_rdg_dt(c.getString(c.getColumnIndex("Prev_rdg_dt")));
        sbmRec.setBilling_Status(c.getString(c.getColumnIndex("Billing_Status")));
        sbmRec.setOld_Meter_Number(c.getString(c.getColumnIndex("Old_Meter_Number")));
        sbmRec.setReplacement_Dt(c.getString(c.getColumnIndex("Replacement_Dt")));
        sbmRec.setOld_mtr_final_rdg(c.getString(c.getColumnIndex("Old_mtr_final_rdg")));
        sbmRec.setNew_mtr_initial_rdg(c.getString(c.getColumnIndex("New_mtr_initial_rdg")));
    }
    public void Duplicate_bill(View view) {
        int Flag;
        Flag = read_record_sbmtopc();//Checking Sbm to PC File for Service No
                Intent billdisp = new Intent(this, ThermalPrint.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent billdisp = new Intent(this, ThermalPrint.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            billdisp.putExtra("uploadrec", (Serializable) uploadRec);
//            billdisp.putExtra("sbmrec", (Serializable) sbmRec);
                startActivity(billdisp);
            }
        }














