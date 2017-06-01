package com.haojiu.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haojiu.communitymanager.R;

import com.haojiu.Utils.MyOpenHelper;

/**
 * Created by leehom on 2017/4/28.
 */

public class SettingActivity extends Activity implements View.OnClickListener{

    private EditText et_local_address;
    private EditText et_cloud_address;
    private Button btn_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        setView();
        setDate();
    }
    private void setView() {
        et_local_address = (EditText)findViewById(R.id.et_local_address);
        et_cloud_address = (EditText)findViewById(R.id.et_cloud_address);
        btn_setting = (Button)findViewById(R.id.btn_setting);
    }
    private void setDate() {
        btn_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_setting://s设置按钮
                long update1 = 0;
                long update2 = 0;
                String text_local_address = et_local_address.getText().toString();
                String text_cloud_address = et_cloud_address.getText().toString();

                MyOpenHelper myOpenHelper = new MyOpenHelper(getApplicationContext());
                SQLiteDatabase db = myOpenHelper.getWritableDatabase();

                if(text_local_address != null && !"".equals(text_local_address)){
                    Cursor sheet_no_cs = db.query("SERVICE_ADDRESS", null, " addressname=? ", new String[]{"SERVICE_LOCAL_ADDRESS"}, null, null, null, null);
                    if(sheet_no_cs != null){
                        ContentValues localValues = new ContentValues();
                        localValues.put("addressname", "SERVICE_LOCAL_ADDRESS");
                        localValues.put("addressvalue", text_local_address);
                        if( sheet_no_cs.getCount() <= 0) {
                            update1 = db.insert("SERVICE_ADDRESS", null, localValues);
                        }else{
                            // Insert the new row, returning the primary key value of the new row
                            update1 = db.replace("SERVICE_ADDRESS", null, localValues);
                        }
                        sheet_no_cs.close();
                    }
                }

                if(text_cloud_address != null && !"".equals(text_cloud_address)){
                    Cursor sheet_no_cs = db.query("SERVICE_ADDRESS", null, " addressname=? ", new String[]{"SERVICE_CLOUD_ADDRESS"}, null, null, null, null);
                    if(sheet_no_cs != null){
                        ContentValues localValues = new ContentValues();
                        localValues.put("addressname", "SERVICE_CLOUD_ADDRESS");
                        localValues.put("addressvalue", text_cloud_address);
                        if( sheet_no_cs.getCount() <= 0) {
                            update2 = db.insert("SERVICE_ADDRESS", null, localValues);
                        }else{
                            // Insert the new row, returning the primary key value of the new row
                            update2 = db.replace("SERVICE_ADDRESS", null, localValues);
                        }
                        sheet_no_cs.close();
                    }
                }
                db.close();

                if (update1>0 || update2>0) {
                    Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
