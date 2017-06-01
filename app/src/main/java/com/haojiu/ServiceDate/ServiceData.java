package com.haojiu.ServiceDate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haojiu.Utils.MyOpenHelper;

/**
 * Created by leehom on 2017/4/28.
 */

public class ServiceData {

    private static  String SERVICE_LOCAL_ADDRESS="";
    private static  String SERVICE_CLOUD_ADDRESS="";
    public static void setServiceCloudAddress(String cloudAddress){
        SERVICE_CLOUD_ADDRESS = cloudAddress;
    }

    public static void setServiceLocalAddress(String localAddress){
        SERVICE_LOCAL_ADDRESS = localAddress;
    }

    public static String getServiceCloudAddress(Context context){
        if(SERVICE_CLOUD_ADDRESS == null || "".equals(SERVICE_CLOUD_ADDRESS)){
            MyOpenHelper myOpenHelper = new MyOpenHelper(context);
            SQLiteDatabase db = myOpenHelper.getWritableDatabase();
                Cursor sheet_no_cs = db.query("SERVICE_ADDRESS", null, " addressname=? ", new String[]{"SERVICE_CLOUD_ADDRESS"}, null, null, null, null);
            if(sheet_no_cs != null){
                if(sheet_no_cs.getCount() > 0){
                    while (sheet_no_cs.moveToNext()) {
                        SERVICE_CLOUD_ADDRESS = sheet_no_cs.getString(2);
                    }
                }
                sheet_no_cs.close();
            }
            db.close();
        }
        return SERVICE_CLOUD_ADDRESS;
    }

    public static String getServiceLocalAddress(Context context){
        if (SERVICE_LOCAL_ADDRESS ==null || "".equals(SERVICE_LOCAL_ADDRESS) ){
            MyOpenHelper myOpenHelper = new MyOpenHelper(context);
            SQLiteDatabase db = myOpenHelper.getWritableDatabase();

            Cursor cursor = db.query("SERVICE_ADDRESS", null, " addressname=? ", new String[]{"SERVICE_LOCAL_ADDRESS"}, null, null, null, null);
            if (cursor!=null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String addressname = cursor.getString(1);
                        String addressvalue = cursor.getString(2);
                        SERVICE_LOCAL_ADDRESS = addressvalue;
                    }
                }
                cursor.close();
            }
            db.close();
        }
        return SERVICE_LOCAL_ADDRESS;
    }
}
