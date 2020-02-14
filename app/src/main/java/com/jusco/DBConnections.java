package com.jusco;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBConnections extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "pctosbm";
    private static final String TABLE_CONTACTS2 = "sbmtopc";
    private static final String LOGIN_TABLE = "user_det_table";
     public static String DBNAME="JUSCODB";
    private Context context;


    //JUSCO INPUT STRUCT

    private static final String KEY_BP_Number="BP_Number";
    private static final String KEY_BP_NAME="BP_NAME";
    private static final String KEY_BP_ADDRESS1="BP_ADDRESS1";
    private static final String KEY_BP_ADDRESS2="BP_ADDRESS2";
    private static final String KEY_BP_ADDRESS3="BP_ADDRESS3";
    private static final String KEY_Sanc_Load="Sanc_Load";
    private static final String KEY_Meter_Number="Meter_Number";
    private static final String KEY_Mobile_Number="Mobile_Number";
    private static final String KEY_Meter_Phase="Meter_Phase";
    private static final String KEY_DTR_Number="DTR_Number";
    private static final String KEY_Feeder_Name="Feeder_Name";
    private static final String KEY_DTR_Location="DTR_Location";
    private static final String KEY_Prev_Billing_Date="Prev_Billing_Date";
    private static final String KEY_MRU="MRU";
   /* private static final String KEY_Cons1="Cons1";
    private static final String KEY_Cons2="Cons2";
    private static final String KEY_Cons3="Cons3";
    private static final String KEY_Cons4="Cons4";
    private static final String KEY_Cons5="Cons5";
    private static final String KEY_Cons6="Cons6";*/
    private static final String KEY_POSTINGDATE="POSTINGDATE";
//    private static final String KEY_CON_DATE="CON_DATE";
//    private static final String KEY_DL_COUNTER="DL_COUNTER";
//    private static final String KEY_CATEGORY="CATEGORY";
    private static final String KEY_E_Duty_Identifier="E_DUTY";
    private static final String KEY_PREV_READ="PREV_READ";
    private static final String KEY_MRNOTE="MRNOTE";
    private static final String KEY_AMOUNT01="AMOUNT01";
    private static final String KEY_AMOUNT02="AMOUNT02";
//    private static final String KEY_MF="MF";
//    private static final String KEY_METER_RENT="METER_RENT";
//    private static final String KEY_LPC="LPC";
//    private static final String KEY_INTERESTONSD="INTERESTONSD";
//    private static final String KEY_TDS="TDS";
    private static final String KEY_RebateEarly="RebateEarly";
    private static final String KEY_RebateDigital="RebateDigital";
    private static final String KEY_OTHERRECEIVABLE="OTHERRECEIVABLE";
//    private static final String KEY_PREVIOUSOS="PREVIOUSOS";
    private static final String KEY_FPPPACharg="FPPPACharg";
    private static final String KEY_office_Addr="office_Addr";
    private static final String KEY_office_Phone="office_Phone";
    private static final String KEY_Prev_rdg_dt="Prev_rdg_dt";
    private static final String KEY_Billing_Status="Billing_Status";
    private static final String KEY_Old_Meter_Number="Old_Meter_Number";
    private static final String KEY_Replacement_Dt="Replacement_Dt";
    private static final String KEY_Old_mtr_final_rdg="Old_mtr_final_rdg";
    private static final String KEY_New_mtr_initial_rdg="New_mtr_initial_rdg";


    //Fields for sbmtopc

    String KEY_PORTION="PORTION";
    String KEY_SCHEDULE_MR_DATE="SCHEDULE_MR_DATE";
    String KEY_METER_SERIAL_NO="METER_SERIAL_NO";
    String KEY_METER_PHASE="METER_PHASE";
    String KEY_BP_NO="BP_NO";
    String KEY_SANCTION_LOAD="SANCTION_LOAD";
    String KEY_UOM="UOM";
    String KEY_ADDR1="ADDR1";
    String KEY_ADDR2="ADDR2";
    String KEY_ADDR3="ADDR3";
    String KEY_MOB_NO="MOB_NO";
    String KEY_CON_DATE="CON_DATE";
    String KEY_DL_COUNTER="DL_COUNTER";
    String KEY_CATEGORY="CATEGORY";
    String KEY_PRE_READ="PRE_READ";
    String KEY_MF="MF";
//    String KEY_MRNOTE="MRNOTE";
    String KEY_CONSUMPTON1="cons1";
    String KEY_CONSUMPTON2="cons2";
    String KEY_CONSUMPTON3="cons3";
    String KEY_CONSUMPTON4="cons4";
    String KEY_CONSUMPTON5="cons5";
    String KEY_CONSUMPTON6="cons6";
    private static final String KEY_E_DUTY="E_DUTY";
    String KEY_KWH_ADJUSTMENT="KWH_ADJUSTMENT";
    String KEY_ED_ADJUSTMENT="ED_ADJUSTMENT";
    String KEY_METER_RENT="METER_RENT";
    String KEY_FIXEDCHARG="FIXEDCHARG";
    private static final String KEY_LPC="LPC";
    String KEY_INTERESTONSD="INTERESTONSD";
    private static final String KEY_TDS="TDS";
    String KEY_REBATEEARLY="REBATEEARLY";
    String KEY_REBATEDIGITAL="REBATEDIGITAL";
    String KEY_OTHER_RECEIVABLE="OTHERRECEIVABLE";
    String KEY_PREVIOUSOS="PREVIOUSOS";
    String KEY_FPPPACHARG="FPPPACHARG";



    //    private static final String KEY_BP_Number="BP_Number";
    private static final String KEY_CURR_READING="CURR_READING";
//    private static final String KEY_MRNOTE="MRNOTE";
    private static final String KEY_BILLED_UNITS="BILLED_UNITS";
    private static final String KEY_NETUNITS="NETUNITS";
    private static final String KEY_ENERGY_CHARGE="ENERGY_CHARGE";
//    private static final String KEY_E_DUTY="E_DUTY";
//    private static final String KEY_METER_RENT="METER_RENT";
    private static final String KEY_FIXEDCHARGE="FIXEDCHARGE";
//    private static final String KEY_LPC="LPC";
//    private static final String KEY_INTERESTONSD="INTERESTONSD";
//    private static final String KEY_TDS="TDS";
//    private static final String KEY_RebateEarly="RebateEarly";
//    private static final String KEY_RebateDigital="RebateDigital";
//    private static final String KEY_OTHERRECEIVABLE="OTHERRECEIVABLE";
//    private static final String KEY_PREVIOUSOS="PREVIOUSOS";
//    private static final String KEY_FPPPACharg="FPPPACharg";
    private static final String KEY_TOTAL="TOTAL";
    private static final String KEY_SBM_NO="SBM_NO";
    private static final String KEY_SBM_Sw_Ver="SBM_Sw_Ver";
    private static final String KEY_BILL_NO="BILL_NO";
    private static final String KEY_Bill_Date="Bill_Date";
    private static final String KEY_Bill_Time="Bill_Time";
    private static final String KEY_Cons_Mob_No="Cons_Mob_No";
    private static final String KEY_Cur_Mtr_Sts="Cur_Mtr_Sts";
    private static final String KEY_Prev_Read="Prev_Read";
    private static final String KEY_Pres_Read_KW_RMD="Pres_Read_KW_RMD";
    private static final String KEY_Cur_PF="Cur_PF";
    private static final String KEY_Bill_issue_date="Bill_issue_date";
    private static final String KEY_Meter_Rent="Meter_Rent";
    private static final String KEY_Round_Off_amount="Round_Off_amount";
    private static final String KEY_Bill_Net_within_due_date="Bill_Net_within_due_date";
    private static final String KEY_Bill_Amount_After_Due_Date="Bill_Amount_After_Due_Date";
    private static final String KEY_Bill_Generation_Status="Bill_Generation_Status";
    private static final String KEY_Meter_Reader_Name="Meter_Reader_Name";
    private static final String KEY_Event_log_number="Event_log_number";
    private static final String KEY_Online_Flag_number="Online_Flag_number";
    private static final String KEY_Rdg_img_Path="Rdg_img_Path";
    private static final String KEY_Latitude="Latitude";
    private static final String KEY_Longitude="Longitude";


    //LoginTable
    private static final String KEY_USER_ID="USER_ID";
    private static final String KEY_USER_PWD="USER_PWD";
    private static final String KEY_USER_NAME="USER_NAME";
    private static final String KEY_USER_TYPE="USER_TYPE";



    public DBConnections(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INPUT_TABLE="CREATE TABLE IF NOT EXISTS" + " " + TABLE_CONTACTS+"(" +

                "MRU varchar2(10),\n"+
                "Portion varchar2(10),\n"+
                "Schedule_MR_date varchar2(30),\n"+
                "Meter_Serial_No varchar2(10),\n"+
                "Meter_Phase varchar2(10),\n"+
                "BP_No varchar2(10),\n"+
                "BPNAME varchar2(30),\n"+
                "Sanction_Load varchar2(10),\n"+
                "UOM varchar2(10),\n"+
                "Addr1 varchar2(30),\n"+
                "Addr2 varchar2(30),\n"+
                "Addr3 varchar2(30),\n"+
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
                "FPPPACharg varchar2(10));";

//                "New_mtr_initial_rdg varchar2(20));";

        db.execSQL(CREATE_INPUT_TABLE);

        String CREATE_OUTPUT_TABLE="CREATE TABLE IF NOT EXISTS"+ " "+ TABLE_CONTACTS2 +"(" +
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
                "Rdg_img_Path varchar2(20));";
//                "Longitude    varchar(50));";//68
        db.execSQL(CREATE_OUTPUT_TABLE);
//        db.close(); // Closing database connection

        String CREATE_LOGIN_TABLE="CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE + "(" +
                "USER_ID         varchar(10),\n" +
                "USER_PWD        varchar(8),\n" +
                "USER_NAME        varchar(50),\n" +
                "USER_TYPE        varchar(15))";

        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS2);

        // Create tables again
        onCreate(db);

    }
    // code to add the new contact
    void addrecord(JSONObject record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_PORTION,record.getString("PORTION"));
            values.put(KEY_MRU,record.getString("MRU"));
            values.put(KEY_SCHEDULE_MR_DATE,record.getString("SCHEDULE_MR_DATE"));
            values.put(KEY_METER_SERIAL_NO,record.getString("METER_SERIAL_NO"));
            values.put(KEY_METER_PHASE,record.getString("METER_PHASE"));
            values.put(KEY_BP_NO,record.getString("BP_NO"));
            values.put("BPNAME",record.getString("BPNAME"));
            values.put(KEY_SANCTION_LOAD,record.getString("SANCTION_LOAD"));
            values.put(KEY_UOM,record.getString("UOM"));
            values.put("ADDRESS1",record.getString("ADDR1"));
            values.put("ADDRESS2",record.getString("ADDR2"));
            values.put("ADDRESS3",record.getString("ADDR3"));
            values.put(KEY_MOB_NO,record.getString("MOB_NO"));
            values.put(KEY_CON_DATE,record.getString("CON_DATE"));
            values.put(KEY_DL_COUNTER,record.getString("DL_COUNTER"));
            values.put(KEY_CATEGORY,record.getString("CATEGORY"));
            values.put(KEY_PRE_READ,record.getString("PRE_READ"));
            values.put(KEY_MF,record.getString("MF"));
            values.put(KEY_MRNOTE,record.getString("MRNOTE"));
            if(record.getString("CONSUMPTON1")==null)
            {
                values.put(KEY_CONSUMPTON1,"0");
            }
            else {
                values.put(KEY_CONSUMPTON1, record.getString("CONSUMPTON1"));
            }
            if(record.getString("CONSUMPTON2")==null)
            {
                values.put(KEY_CONSUMPTON2,"0");
            }
            else {
                values.put(KEY_CONSUMPTON2, record.getString("CONSUMPTON2"));
            }

//            values.put(KEY_CONSUMPTON2,record.getString("CONSUMPTON2"));
            if(record.getString("CONSUMPTON3")==null)
            {
                values.put(KEY_CONSUMPTON3,"0");
            }
            else {
                values.put(KEY_CONSUMPTON3, record.getString("CONSUMPTON3"));
            }
//            values.put(KEY_CONSUMPTON3,record.getString("CONSUMPTON3"));
            if(record.getString("CONSUMPTON4")==null)
            {
                values.put(KEY_CONSUMPTON4,"0");
            }
            else {
                values.put(KEY_CONSUMPTON4, record.getString("CONSUMPTON4"));
            }
//            values.put(KEY_CONSUMPTON4,record.getString("CONSUMPTON4"));
            if(record.getString("CONSUMPTON5")==null)
            {
                values.put(KEY_CONSUMPTON5,"0");
            }
            else {
                values.put(KEY_CONSUMPTON5, record.getString("CONSUMPTON5"));
            }
//            values.put(KEY_CONSUMPTON5,record.getString("CONSUMPTON5"));
            if(record.getString("CONSUMPTON6")==null)
            {
                values.put(KEY_CONSUMPTON6,"0");
            }
            else {
                values.put(KEY_CONSUMPTON6, record.getString("CONSUMPTON6"));
            }
//            values.put(KEY_CONSUMPTON6,record.getString("CONSUMPTON6"));
            values.put(KEY_E_Duty_Identifier,record.getString("e_DUTY"));
            values.put(KEY_KWH_ADJUSTMENT,record.getString("KWH_ADJUSTMENT"));
            values.put(KEY_ED_ADJUSTMENT,record.getString("ED_ADJUSTMENT"));
            values.put(KEY_METER_RENT,record.getString("METER_RENT"));
            values.put(KEY_FIXEDCHARGE,record.getString("FIXEDCHARGE"));
            values.put(KEY_LPC,record.getString("LPC"));
            values.put(KEY_INTERESTONSD,record.getString("INTERESTONSD"));
            values.put(KEY_TDS,record.getString("TDS"));
            values.put(KEY_REBATEEARLY,record.getString("REBATEEARLY"));
            values.put(KEY_REBATEDIGITAL,record.getString("REBATEDIGITAL"));
            values.put(KEY_OTHER_RECEIVABLE,record.getString("OTHER_RECEIVABLE"));
            values.put(KEY_PREVIOUSOS,record.getString("PREVIOUSOS"));
            values.put(KEY_FPPPACHARG,record.getString("FPPPACHARG"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single Record
    String[] getRecord(String id) {
        String[] record=new String[1024];
        SQLiteDatabase db = this.getReadableDatabase();
//        Integer value;
//        value=Integer.parseInt(id);
//        value=value+1;
//        id=value.toString();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                        KEY_BP_Number,KEY_CATEGORY,KEY_BP_NAME
                         }, KEY_BP_Number + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        record[0]=(cursor.getString(0));//LAT
        record[1]=cursor.getString(1);//LON
        record[2]=cursor.getString(2);//SCNO
        record[3]=cursor.getString(3);//BOOK
        record[4]=cursor.getString(4);//METNO
        record[5]=cursor.getString(5);//NAME
        record[6]=cursor.getString(6);//ADR1
        record[7]=cursor.getString(7);//ADR2
        record[8]=cursor.getString(8);//DUEAMT
        record[9]=cursor.getString(9);//LPA
        record[10]=cursor.getString(10);//LPD
        record[11]=cursor.getString(11);//DTR
        record[12]=cursor.getString(12);//POLE
        record[13]=cursor.getString(13);//Uniq
//        db.close(); // Closing database connection
        // return contact
        return record;
    }

    // code to get all contacts in a list view
    public List<String[]> getAllRecords() {
        String[] title=new String[1024];
        List<String[]> list = new ArrayList<String[]>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                title=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(4)};
                list.add(title);
//                cursor.moveToNext();
                // Adding contact to list
//                list.add(title);
            } while (cursor.moveToNext());
        }
//        db.close(); // Closing database connection
        // return contact list
        return list;
    }

    public List<String[]> getSelectionRecords(String From, String to) {
        String[] title=new String[1024];
        List<String[]> list = new ArrayList<String[]>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " +TABLE_CONTACTS + " where cast(due_amt as decimal)>="+ From +" and cast(due_amt as decimal)<" + to + " order by scno";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                title=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(4)};
                list.add(title);
//                cursor.moveToNext();
                // Adding contact to list
//                list.add(title);
            } while (cursor.moveToNext());
        }
//        db.close(); // Closing database connection
        // return contact list
        return list;
    }


    // code to update the single contact
    public int updateRecord(String contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BP_Number, "");
//        values.put(KEY_BOOK, "");
//        db.close(); // Closing database connection
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_BP_Number + " = ?",
                new String[] { String.valueOf(contact) });
    }

    // Deleting single contact
    public void deleteContact(String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_BP_Number + " = ?",
                new String[] { String.valueOf(contact) });
//        db.close();
    }
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
//        db.close(); // Closing database connection
        // return count
        return cursor.getCount();
    }

    public int getDiscCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
//        db.close(); // Closing database connection
        // return count
        return cursor.getCount();
    }

    void addDiscrecord(String[] record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BP_Number, record[0]);
        /*values.put(KEY_MRU, record[1]);
        values.put(KEY_Legacy_Number, record[2]);*/
        // Inserting Row
        db.insert(TABLE_CONTACTS2, null, values);
        db.close(); // Closing database connection
    }
    public List<String[]> getAllDiscRecords() {
        String[] title=new String[1024];
        List<String[]> list = new ArrayList<String[]>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS2 + " where online_flag_number = '0'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
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
                        cursor.getString(67),
                        cursor.getString(68)};
                list.add(title);

//                cursor.moveToNext();
                // Adding contact to list
//                list.add(title);
            } while (cursor.moveToNext());
        }
//        db.close(); // Closing database connection
        // return contact list
        return list;
    }

    public List<String[]> getSelectionRecords2() {
        String[] title=new String[1024];
        List<String[]> list = new ArrayList<String[]>();
        // Select All Query
        String selectQuery="SELECT pc.BP_No,pc.BPName,pc.Mob_No FROM pctosbm pc LEFT JOIN sbmtopc sb ON pc.BP_No=sb.BP_No WHERE sb.BP_No IS NULL";
        Log.e("DB:",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                    title = new String[]{cursor.getString(0), cursor.getString(1),cursor.getString(2)};
                    list.add(title);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void add_login_record(String userid,String userpwd,String username,String usertype)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_USER_ID, userid);
            values.put(KEY_USER_PWD,userpwd);
            values.put(KEY_USER_NAME,username);
            values.put(KEY_USER_TYPE,usertype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inserting Row
        db.insert(LOGIN_TABLE, null, values);

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void del_login_record()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        /*try {
            values.put(KEY_USER_ID, userid);
            values.put(KEY_USER_PWD,userpwd);
            values.put(KEY_USER_NAME,username);
            values.put(KEY_USER_TYPE,usertype);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // Inserting Row
        db.delete(LOGIN_TABLE, null, new String[]{"*"});

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public String[] getLoginRecord(String username,String user_pass) {
        String[] title=new String[1024];
        List<String[]> list = new ArrayList<String[]>();
        // Select All Query
        String selectQuery="SELECT * from user_det_table where user_id='"+username+"' and user_pwd='"+user_pass+"'";
        Log.e("DB:",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                title = new String[]{cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getString(3)};

            } while (cursor.moveToNext());
        }
        else
        {
            title=new String[]{"0","0","0"};
        }
        return title;
    }
}
