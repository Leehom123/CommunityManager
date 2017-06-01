package com.haojiu.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haojiu.Utils.MyOpenHelper;
import com.haojiu.communitymanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leehom on 2017/5/11.
 */

public class GoodDetailsActivity extends Activity implements View.OnClickListener{
    private String[] SUP,YWY,CK;
    private Spinner sp_sup ,sp_ywy;
    private ArrayAdapter<String> adapter_sup,adapter_ywy;
    private int sp_item_sup,sp_item_ywy,sp_item_ck ,sup=0,ywy=0,ck=0;
    private Button btn_baocun_gooddetails;
    private SQLiteDatabase db;
    private TextView name_text_gooddetail;
    private float price;
    private String item_no;
    private String item_subno;
    private String item_barcode;
    private String name;
    private String item_class;
    private String item_unit_no;
    private String py_code;
    private String item_valid_day;
    private String item_valid_tipday;
    private int sheet_sn=1;
    private EditText text_num_gooddetail;
    private EditText text_pinum_gooddetail;
    private String sheet_no;
    private Spinner sp_ck;
    private ArrayAdapter<String> adapter_ck;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_gooddetails);
        setView();
    }

    private void setView() {
        text_num_gooddetail = (EditText)findViewById(R.id.text_num_gooddetail);
        text_pinum_gooddetail = (EditText)findViewById(R.id.text_pinum_gooddetail);
        sp_sup = (Spinner)findViewById(R.id.sp_sup);
        sp_ywy=(Spinner)findViewById(R.id.sp_ywy);
        sp_ck = (Spinner)findViewById(R.id.sp_ck);
        name_text_gooddetail = (TextView)findViewById(R.id.name_text_gooddetail);
        btn_baocun_gooddetails = (Button)findViewById(R.id.btn_baocun_gooddetails);
        TextView text_price_gooddetail = (TextView)findViewById(R.id.text_price_gooddetail);
        TextView common_title_TV_center = (TextView)findViewById(R.id.common_title_TV_center);
        common_title_TV_center.setText("商品入库");
        String goodCode = getIntent().getStringExtra("GoodCode");
        sheet_no = getIntent().getStringExtra("sheet_no");
        TextView text_code_gooddetail = (TextView)findViewById(R.id.text_code_gooddetail);
        text_code_gooddetail.setText(goodCode);
        TextView time_text_gooddetail = (TextView)findViewById(R.id.time_text_gooddetail);
        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String time = formatter.format(date);
        time_text_gooddetail.setText(time);
        btn_baocun_gooddetails.setOnClickListener(this);
        MyOpenHelper myOpenHelper = new MyOpenHelper(getApplicationContext());
        db = myOpenHelper.getWritableDatabase();

        Cursor cursor = db.query("VIEW_MOB_DOWN_ITEMINFO", null,"item_barcode=?" ,new String[]{goodCode},null, null,null, null);
        //Cursor cursor = db.rawQuery("select * from info", null);
        if (cursor!=null && cursor.getCount()>0) {
            while(cursor.moveToNext()) {
                //价格
                price = cursor.getFloat(1);
                //服务端商品编号
                item_no = cursor.getString(2);
                //供应商编号
                item_subno = cursor.getString(3);
                //条形码
                item_barcode = cursor.getString(4);
                //商品名称
                name = cursor.getString(5);
                item_class = cursor.getString(6);
                //单位：盒
                item_unit_no = cursor.getString(7);
                //拼音码
                py_code = cursor.getString(8);
                //有效期 开始
                item_valid_day = cursor.getString(9);
                //有效期 结束
                item_valid_tipday = cursor.getString(10);
                text_price_gooddetail.setText(price +"");
                name_text_gooddetail.setText(name);
            }
        }
        Cursor cursor_sup = db.query("VIEW_MOB_DOWN_SUPINFO", null,null ,null, null,null, null);
        if (cursor_sup!=null && cursor_sup.getCount()>0) {
            SUP = new String[cursor_sup.getCount()];
            while(cursor_sup.moveToNext()) {
                String sup_name= cursor_sup.getString(2);
                SUP[sup]=new String(sup_name);
                sup++;
            }
        }
        if(SUP == null){
            SUP = new String[]{};
        }
        Cursor cursor_ywy = db.query("VIEW_MOB_DOWN_USER", null,null ,null, null,null, null);
        if (cursor_ywy!=null && cursor_ywy.getCount()>0) {
            YWY = new String[cursor_ywy.getCount()];
            while(cursor_ywy.moveToNext()) {
                String ywy_name= cursor_ywy.getString(3);
                YWY[ywy]=new String(ywy_name);
                ywy++;
            }
        }

        if(YWY == null){
            YWY = new String[]{};
        }

        Cursor cursor_ck = db.query("VIEW_MOB_DOWN_BRANCH", null,null ,null, null,null, null);
        if (cursor_ck!=null && cursor_ck.getCount()>0) {
            CK = new String[cursor_ck.getCount()];
            while(cursor_ck.moveToNext()) {
                String ck_name= cursor_ck.getString(2);
                CK[ck]=new String(ck_name);
                ck++;
            }
        }

        if(CK == null){
            CK = new String[]{};
        }

        adapter_sup = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,SUP);
        adapter_ywy = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,YWY);
        adapter_ck = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CK);
        //设置下拉列表的风格
        adapter_sup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_ywy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_ck.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        sp_sup.setAdapter(adapter_sup);
        sp_ywy.setAdapter(adapter_ywy);
        sp_ck.setAdapter(adapter_ck);
        //添加事件Spinner事件监听
        sp_sup.setOnItemSelectedListener(new SpinnerSelectedListener());
        sp_ywy.setOnItemSelectedListener(new SpinnerSelectedListener());
        sp_ck.setOnItemSelectedListener(new SpinnerSelectedListener());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_baocun_gooddetails:

                ContentValues values = new ContentValues();
                values.put("mob_no", "00000");//手机唯一标识
                values.put("sheet_no", sheet_no);//手机端入库单单号
                values.put("sheet_sn", sheet_sn);//入库单商品顺序码
                values.put("branch_no", "");//仓库编号
                values.put("sup_no", sp_item_sup);//供货商编号
                values.put("emp_no", sp_item_ywy);//业务员编号
                values.put("item_no", item_no);//商品编号
                values.put("item_barcode", item_barcode);//商品条码
                values.put("item_price", price);//商品价格
                values.put("item_qty", text_num_gooddetail.getText().toString());//商品数量
                values.put("item_unit", item_unit_no);//商品单位
                values.put("lastdate", item_valid_tipday);//商品有效期
                values.put("batch_no", text_pinum_gooddetail.getText().toString());//批号
                values.put("color", "");//颜色
                values.put("size", "");//尺码
                values.put("item_name",name);//商品名称
                long insert = db.insert("VIEW_MOB_CG_IN", null, values);

                if (insert>0) {
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
//                    String num_good = Integer.parseInt(getIntent().getStringExtra("num_good"))+1+"";
                    startActivity(new Intent(GoodDetailsActivity.this,RukuDetailsActivity.class).putExtra("sheet_no",sheet_no));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                }

                db.close();
                break;
        }
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            switch (arg1.getId()){
                case R.id.sp_sup:
                    sp_item_sup=arg2;
                    break;
                case R.id.sp_ywy:
                    sp_item_ywy=arg2;
                    break;
                case R.id.sp_ck:
                    sp_item_ck=arg2;
                    break;
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
