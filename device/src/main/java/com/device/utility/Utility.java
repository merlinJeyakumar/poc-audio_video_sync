package com.device.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;

import static android.content.Context.VIBRATOR_SERVICE;

public class Utility {
    private static Vibrator getVibratorService(Context context) {
        return (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
    }

    @SuppressLint("MissingPermission")
    private static void createVibration(Context context, long vibrationLength) {
        Vibrator vibrator = getVibratorService(context);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrationLength, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(vibrationLength);
        }
    }

    public static boolean isMockEnabled(Context context, Location location) {
        // returns true if mock location enabled, false if not enabled.
        boolean isMock = false;
        if (android.os.Build.VERSION.SDK_INT >= 18) { //TODO: required to find out correct procedure to Check Mock Location is Enabled/Disabled
            isMock = location.isFromMockProvider();
            if (!isMock) {
                AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                isMock = (opsManager.checkOpNoThrow(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(), context.getApplicationContext().getPackageName()) == AppOpsManager.MODE_ALLOWED);
            }
        } else {
            isMock = !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        }
        return isMock;
    }

    public static void sendEmail(Context context, String emailAddress, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAddress, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(emailIntent, "Send email.").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void getLocationSettings(Activity mActivity, int requestCode) {
        mActivity.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), requestCode);
    }

    public static void getAppSettings(Activity mActivity, int resultCode) {
        mActivity.startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", mActivity.getApplication().getPackageName(), null)), resultCode);
    }
}
