package com.jusco;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

public class Utils {
    private Context mContext;
    public Utils(Context context) {
        this.mContext = context;




    }

    public interface OnDialogButtonClickListener {


        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }
        public void AltDialog_SimpleMsg(Context ctx,String title, String btnPositive, String btnNegetive, final OnDialogButtonClickListener  listener)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
        builder.setTitle(title);
//        builder.setMessage(message);
        builder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                // Do nothing but close the dialog
                listener.onPositiveButtonClicked();
               dialog.dismiss();
            }
        });
        builder.setNegativeButton(btnNegetive, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                listener.onNegativeButtonClicked();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();

    }

    public Dialog openDialog(@LayoutRes int layoutId) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(layoutId);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }



}
