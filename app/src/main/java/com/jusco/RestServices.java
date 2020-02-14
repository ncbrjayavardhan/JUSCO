package com.jusco;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

public class RestServices {
    //    public static String ipaddress="192.168.1.14";
    public static String ipaddress="124.123.41.255";
//    public static String ipaddress=MainActivity.SERVER_IP;
    public static String port="8080";
    public static String Root_URL="http://"+ipaddress+":"+port+"/";
    public static String URL=Root_URL +"WebserviceRestful_server/rest/demo/hello3";
    public static String login_URL="";
    public static String URL3=Root_URL +"WebserviceRestful_server/rest/dataDownload/json";
    public static String URL4=Root_URL +"WebserviceRestful_server/rest/DisconnectedDataService/DataSave";

  /*  public static String URL5="http://192.168.100.80:8080/Jusco_WebServices/rest2/jusco/download";
    public static String URL6="http://192.168.100.80:8080/Jusco_WebServices/rest2/jusco/output";
    public static String URL7="http://192.168.100.80:8080/Jusco_WebServices/rest2/jusco/Login";*/

    public static String URL5="http://"+ ipaddress+":8081/Jusco_WebServices/rest2/jusco/download";
    public static String URL6="http://"+ ipaddress+":8081/Jusco_WebServices/rest2/jusco/output";
    public static String URL7="http://"+ ipaddress+":8081/Jusco_WebServices/rest2/jusco/Login";

    //http://localhost:8080/CSPDCL_WebServices/rest2/cspdcl/download
    public static String TAG="RestServices";
    public static String LUser;
    public static String LPass;
    public static String TAG_Username;
    public static String TAG_Password;
    public static String response;
    public static String[] records;
    public static Context context;
    public static DataPojo str;
    public static String Login_service(Resources resources, String Username, String Password)  {
        LUser=Username;
        LPass=Password;
//        URL2=Root_URL+LoginURL;
//        login_URL=resources.getString(R.string.IPADDR)+"/"+resources.getString(R.string.loginURL);
        login_URL=URL7;
        try {
            response=new SendRequest().execute("Login").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static JSONArray Data_Fetch(Resources resources)
    {
        URL3=URL5;//.getString(R.string.ipaddress)+"/"+resources.getString(R.string.DataFetchURL);
        JSONArray contacts=null;
        try {
            response=new SendRequest().execute("DataFetch").get();
            try {
                JSONObject jsonObj = new JSONObject(response);
                // Getting JSON Array node
                contacts = jsonObj.getJSONArray("li");

                for(int i=0; i<contacts.length(); i++) {
                    JSONObject json_data = contacts.getJSONObject(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//            Log.e("reclen",String.valueOf(records.length));
        return contacts;
    }

    public static class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                java.net.URL url=null ;//= new URL(URL2);
                JSONObject postDataParams = new JSONObject();
                if(arg0[0].equals("Login")) {

                    url = new URL(URL7);
                    postDataParams.put("username", LUser);
                    postDataParams.put("pwd", LPass);
                    postDataParams.put("imei1", MainActivity.MacID);
                    postDataParams.put("imei2", MainActivity.MacID2);
                }
                if(arg0[0].equals("DataFetch")) {
                    url = new URL(URL5);
                    postDataParams.put("Data", "Data");
                    postDataParams.put("id", MainActivity.Login_name);
                    postDataParams.put("imei1", MainActivity.MacID);
                    postDataParams.put("imei2", MainActivity.MacID2);
                }
                if(arg0[0].equals("Send")) {
                    url = new URL(URL6);

                    uploadRec uploadRec = new uploadRec();
                    uploadRec.setBP_Number(ConsFetchActivity.sbmRec.getBP_Number());
                    uploadRec.setMETER_SERIAL_NO(ConsFetchActivity.sbmRec.getMeter_Number());
                    uploadRec.setCURR_READING(EntryActivity.curread.toString());
                    uploadRec.setMRNOTE(EntryActivity.CurStatus);
                    uploadRec.setBILLED_UNITS(EntryActivity.unitsm.toString());
                    uploadRec.setNETUNITS(EntryActivity.unitsm.toString());
                    uploadRec.setENERGY_CHARGE(EntryActivity.EnrgyChrg.toString());
                    uploadRec.setE_DUTY(EntryActivity.ED.toString());
                    uploadRec.setMETER_RENT(ConsFetchActivity.sbmRec.METER_RENT);
                    uploadRec.setFIXEDCHARGE(EntryActivity.FixedChrg.toString());
                    uploadRec.setLPC(ConsFetchActivity.sbmRec.getLPC());
                    uploadRec.setINTERESTONSD(ConsFetchActivity.sbmRec.getINTERESTONSD());
                    uploadRec.setTDS(ConsFetchActivity.sbmRec.getTDS());
                    uploadRec.setRebateEarly(ConsFetchActivity.sbmRec.RebateEarly);
                    uploadRec.setRebateDigital(ConsFetchActivity.sbmRec.getRebateDigital());
                    uploadRec.setTot_rebate(EntryActivity.rebate_val.toString());
                    uploadRec.setOTHERRECEIVABLE(ConsFetchActivity.sbmRec.OTHERRECEIVABLE);
                    uploadRec.setPREVIOUSOS(ConsFetchActivity.sbmRec.getPREVIOUSOS());
                    uploadRec.setFPPPACharg(ConsFetchActivity.sbmRec.getFPPPACharg());
                    uploadRec.setBill_Amt(EntryActivity.NetAmntExArr.toString());
                    uploadRec.setTOTAL(EntryActivity.NetAmntInArr.toString());
                    uploadRec.setSBM_NO(MainActivity.MacID);
                    uploadRec.setLAT(ConsFetchActivity.latitude.toString());
                    uploadRec.setLONG(ConsFetchActivity.longitude.toString());
                    uploadRec.setSBM_Sw_Ver(MainActivity.Version);
                    uploadRec.setBILL_NO(EntryActivity.BillNo);
                    uploadRec.setBill_Date(EntryActivity.Billdate);
                    uploadRec.setBill_Time(EntryActivity.Billtime);
                    uploadRec.setCons_Mob_No(EntryActivity.Telno);
                    uploadRec.setCur_Mtr_Sts(EntryActivity.CurStatus);
                    uploadRec.setPrev_Read(ConsFetchActivity.sbmRec.getPREV_READ());
                    uploadRec.setPres_Read_KW_RMD(EntryActivity.CMD);
                    uploadRec.setCur_PF(EntryActivity.CurPF.toString());
                    uploadRec.setBill_Due_date(EntryActivity.Duedt);
                    uploadRec.setRound_Off_amount("0");
                    uploadRec.setBill_Net_within_due_date(EntryActivity.NetAmntInArr.toString());
                    uploadRec.setBill_Amount_After_Due_Date(EntryActivity.NetAmntInArr.toString());
                    uploadRec.setBill_Generation_Status("Y");
                    uploadRec.setMeter_Reader_Name(MainActivity.Login_name);
                    uploadRec.setObr_Code("OK");
                    uploadRec.setOnline_Flag_number("S");
                    uploadRec.setCat_tariff(EntryActivity.cat_tariff.toString());
                    uploadRec.setRdg_img_Path(EntryActivity.BWfilename);
                    uploadRec.setMRU(ConsFetchActivity.sbmRec.getMRU());
                    uploadRec.setPortion(ConsFetchActivity.sbmRec.getPortion());
                    List<uploadRec> data2=new ArrayList<uploadRec>();
                    data2.add(uploadRec);
                    DataPojo datapojo= new DataPojo();
                    datapojo.setData(data2);
                    String val=new Gson().toJson(datapojo);
                    postDataParams.put("li",val);
                }

                if(arg0[0].equals("Upload")) {
                    url = new URL(URL6);
//                    url2 = new URL(Upload_URL5);
                    String val=new Gson().toJson(str);
//                    JSONArray array = new JSONArray(val);
//                    JSONObject object= new JSONObject(val);
                    postDataParams.put("li", val);
                }
                Log.e("params",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception2: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();
//            textView.setText(result);
            response=result;
            Log.e(TAG,result);
        }
    }

    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public static String Send_Data()  {
        try {
            response=new SendRequest().execute("Send").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String upload_data_to_server(Context ctx, DataPojo data)  {
        context=ctx;
        str=data;
        try {
            response=new SendRequest().execute("Upload").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }
}
