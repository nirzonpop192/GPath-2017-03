package com.siddiquinoor.restclient.views.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.siddiquinoor.restclient.R;

/**
 * @author : POP
 * @since : 8/24/2015.
 */
public class ADNotificationManager {


    // public     AlertDialog alertDialog;

    /**
     * Function to display simple Confirm Alert Dialog
     *
     * @param context - application context
     * @param message - alert message
     */

    public void showConfirmDialog(Context context, String message) {
        showAlertDialog(context, "Confirm", message, null, false);
    }

    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {

        showAlertDialog(context, title, message, status, false);
    }

    /**
     * Function to display simple Error Alert Dialog
     *
     * @param context - application context
     * @param message - alert message
     */
    public void showInfromDialog(Context context, String title, String message) {
        showAlertDialog(context, title, message, true, false);
    }


    /**
     * Function to display simple Error Alert Dialog
     *
     * @param context - application context
     * @param message - alert message
     */
    public void showInvalidDialog(Context context, String title, String message) {
        showAlertDialog(context, title, message, null, false);
    }

    /**
     * Function to display simple Error Alert Dialog
     *
     * @param context - application context
     * @param message - alert message
     */
    public void showErrorDialog(Context context, String message) {
        showAlertDialog(context, null, message, null, true);
    }

    /**
     * Function to display simple Alert Dialog
     *
     * @param context   - application context
     * @param title     - alert dialog title
     * @param message   - alert message
     * @param status    - success/failure (used to set icon)
     *                  - pass null if you don't want icon
     * @param errorMssg -determine the cancelabel
     */
    private void showAlertDialog(Context context, String title, String message, Boolean status, boolean errorMssg) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        if (errorMssg) {
            alertDialog.setTitle("Error");
            alertDialog.setCancelable(false);
        }
        // Setting Dialog Title
        else
            alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);


        if (status != null) {
            alertDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
            alertDialog.setIcon(R.drawable.icon_info);
        } else {
            alertDialog.setIcon(null);
        }
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    public void showWarningDialog(Context context, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();


        alertDialog.setTitle("Error");
        alertDialog.setCancelable(true);

        // Setting Dialog Message
        alertDialog.setMessage(message);


        alertDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        alertDialog.setIcon(R.drawable.warning_32);


        // Setting  Button
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        /** Setting Dialog Title */
        alertDialog.setTitle("GPS is settings");

        /** Setting Dialog Message */
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        /** On pressing Settings button */
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        /** on pressing cancel button */
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        /** Showing Alert Message */
        alertDialog.show();
    }

    /**
     * @param activity Invoking activity reference
     * @param title    title of the dialog
     * @param msg      massage
     */

    public void showCustomDialog(final Activity activity, String title, String msg, final Intent intent) {
        /**
         * Create  AlertDialog.Builder
         */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder((Context) activity);
        /**
         * convert the layout xml convert java object by inflater
         */
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        dialogBuilder.setView(dialogView);

        final TextView tvTitle = (TextView) dialogView.findViewById(R.id.custom_dialog_title);
        tvTitle.setText(title);

        final TextView tvMsg = (TextView) dialogView.findViewById(R.id.custom_dialog_msg);
        tvMsg.setText(msg);

        Button dialogButton = (Button) dialogView.findViewById(R.id.custom_dialog_button);


        final AlertDialog dialog = dialogBuilder.create();

        dialogButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                activity.finish();
                                                activity.startActivity(intent);
                                            }
                                        }
        );

        dialog.show();

    }

    private AlertDialog.Builder createCustomBuilder(Activity activity, String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder((Context) activity);
        /**
         * convert the layout xml convert java object by inflater
         */
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        dialogBuilder.setView(dialogView);

        final TextView tvTitle = (TextView) dialogView.findViewById(R.id.custom_dialog_title);
        tvTitle.setText(title);

        final TextView tvMsg = (TextView) dialogView.findViewById(R.id.custom_dialog_msg);
        tvMsg.setText(msg);

        Button dialogButton = (Button) dialogView.findViewById(R.id.custom_dialog_button);


        return dialogBuilder;
    }

}
