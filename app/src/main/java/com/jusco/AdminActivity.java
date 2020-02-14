package com.jusco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;
import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AdminActivity extends AppCompatActivity {
    SQLiteDatabase db;
    public static String DBNAME="";

    Button Billing,pctosbm,sbmtopc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        DBNAME=getResources().getString(R.string.DBNAME);
        Billing=(Button)findViewById(R.id.btnBilling);
        pctosbm=(Button)findViewById(R.id.btnDownload);
        sbmtopc=(Button)findViewById(R.id.btnUpload);

    }

    public void pctosbm(View view)
    {
        Intent DataLoading=new Intent(this,DataLoading.class);
        startActivity(DataLoading);
    }

    public void sbmtopc(View view)
    {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String date = sdf.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
        String time = sdf2.format(new Date());
        String currentDBPath = "/data/"+ "com.jusco" +"/databases/"+DBNAME;
        String backupDBPath = "DMP_" + date + "_" + time+".db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "DB NO!", Toast.LENGTH_LONG).show();
        }
    }

    public void billing(View view)
    {
        /*Intent intent = new Intent(this,ConsFetchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }
//    public void decrypt_file() {
//        {
//            String key = "Mary has one cat";
//            File inputFile = new File("/sdcard/document.encrypted");
//            File decryptedFile = new File("/sdcard/document.decrypted");
//            try {
//                CryptoUtils.decrypt(key, inputFile, decryptedFile);
//            } catch (CryptoException ex) {
//                System.out.println(ex.getMessage());
//                ex.printStackTrace();
//            }
//        }
//    }

//    public static void deleteFiles(String path) {
//        File file = new File(path);
//        if (file.exists()) {
//            String deleteCmd = "rm -r " + path;
//            Runtime runtime = Runtime.getRuntime();
//            try {
//                runtime.exec(deleteCmd);
//            } catch (IOException e) {
//            }
//        }
//    }

/*    public void apply_hindi()
    {
        Billing.setText(getResources().getString(R.string.Billing));
        pctosbm.setText(getResources().getString(R.string.pctosbm));
        sbmtopc.setText(getResources().getString(R.string.sbmtopc));
    }
    public void apply_English()
    {
        Billing.setText(getResources().getString(R.string.BillingE));
        pctosbm.setText(getResources().getString(R.string.pctosbmE));
        sbmtopc.setText(getResources().getString(R.string.sbmtopcE));
    }

    public void apply_Telugu()
    {
        Billing.setText(getResources().getString(R.string.BillingT));
        pctosbm.setText(getResources().getString(R.string.pctosbmT));
        sbmtopc.setText(getResources().getString(R.string.sbmtopcT));
    }*/

    @Override
    public void onBackPressed() {
        Intent inte = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inte);
    }

}
