package com.haojiu.ServiceDate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haojiu.Utils.MyOpenHelper;

/**
 * Created by leehom on 2017/4/28.
 */

public class ServiceDate {

    private static  String SERVICE_LOCAL_ADDRESS="";
    private static  String SERVICE_BUSI_ADDRESS="";

    public String getServiceLocalAddress(Context context){
        if (SERVICE_LOCAL_ADDRESS ==null || "".equals(SERVICE_LOCAL_ADDRESS) ){
            MyOpenHelper myOpenHelper = new MyOpenHelper(context);
            SQLiteDatabase db = myOpenHelper.getWritableDatabase();

            Cursor cursor = db.query("SERVICE_ADDRESS", null,null ,null, null,null, null);
            //Cursor cursor = db.rawQuery("select * from info", null);
            if (cursor!=null && cursor.getCount()>0) {
                    while(cursor.moveToNext()) {
                        String addressname= cursor.getString(1);
                        String addressvalue = cursor.getString(2);
                        SERVICE_LOCAL_ADDRESS=addressvalue;
                    }
                }
        }
        return SERVICE_LOCAL_ADDRESS;
    }
}
