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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haojiu.Bean.Good;
import com.haojiu.Utils.DbUtils;
import com.haojiu.Utils.MyOpenHelper;
import com.haojiu.communitymanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leehom on 2017/5/11.
 */

public class GoodDetailsActivity extends Activity implements View.OnClickListener{
    private String[] SUP,YWY,CK,CUST;
    private String[] SUP_NO,YWY_NO,CK_NO,CUST_NO;
    private Spinner sp_sup ,sp_cust,sp_ywy,sp_ck;
    private ArrayAdapter<String> adapter_sup,adapter_ywy,adapter_cust,adapter_ck;
    private int sp_item_sup,sp_item_cust,sp_item_ywy,sp_item_ck ,sup=0,cust=0,ywy=0,ck=0;
    private Button btn_baocun_gooddetails;
    private SQLiteDatabase db;
    private TextView name_text_gooddetail;
    private TextView unit_text_gooddetail;
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
    private int sheet_sn=1;//for insert
    private EditText text_num_gooddetail;
    private EditText text_pinum_gooddetail;
    private EditText text_color_gooddetail;
    private EditText text_size_gooddetail;
    private String sheet_no;
    private String busiType;
    private String sheetSn;//for update
    private RelativeLayout rl_sup;
    private RelativeLayout rl_cust;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_gooddetails);
        sheet_no = getIntent().getStringExtra("sheet_no");
        busiType = getIntent().getStringExtra("flag");
        sheetSn = getIntent().getStringExtra("sheetSn");
        setView();
    }

    private void setView() {
        text_num_gooddetail = (EditText)findViewById(R.id.text_num_gooddetail);
        text_pinum_gooddetail = (EditText)findViewById(R.id.text_pinum_gooddetail);
        text_color_gooddetail = (EditText)findViewById(R.id.text_color_gooddetail);
        text_size_gooddetail = (EditText)findViewById(R.id.text_size_gooddetail);
        rl_sup = (RelativeLayout)findViewById(R.id.rl_sup);
        rl_cust = (RelativeLayout)findViewById(R.id.rl_cust);
        if(!"0".equals(busiType)){
            rl_sup.setVisibility(View.GONE);
        }
        if(!"1".equals(busiType)){
            rl_cust.setVisibility(View.GONE);
        }
        sp_sup = (Spinner)findViewById(R.id.sp_sup);
        sp_cust = (Spinner)findViewById(R.id.sp_cust);
        if(!"0".equals(busiType)){
            sp_sup.setVisibility(View.GONE);
        }
        if(!"1".equals(busiType)){
            sp_cust.setVisibility(View.GONE);
        }
        sp_ywy=(Spinner)findViewById(R.id.sp_ywy);
        sp_ck = (Spinner)findViewById(R.id.sp_ck);
        name_text_gooddetail = (TextView)findViewById(R.id.name_text_gooddetail);
        unit_text_gooddetail = (TextView)findViewById(R.id.unit_text_gooddetail);
        btn_baocun_gooddetails = (Button)findViewById(R.id.btn_baocun_gooddetails);
        TextView text_price_gooddetail = (TextView)findViewById(R.id.text_price_gooddetail);
        TextView common_title_TV_center = (TextView)findViewById(R.id.common_title_TV_center);
        if("0".equals(busiType)){
            common_title_TV_center.setText("商品入库");
        }

        if("1".equals(busiType)){
            common_title_TV_center.setText("商品出库");
        }

        if("2".equals(busiType)){
            common_title_TV_center.setText("商品盘点");
        }

        String goodCode = "";
        if(sheetSn == null || "".equals(sheetSn)){//添加
            goodCode = getIntent().getStringExtra("GoodCode");
        }else{//插入
            DbUtils du = new DbUtils(this);
            Good good = du.findGood(sheet_no,Integer.valueOf(sheetSn),busiType);
            goodCode = good.getItem_barcode();
        }

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
                unit_text_gooddetail.setText(item_unit_no);
            }
        }
        Cursor cursor_sup = db.query("VIEW_MOB_DOWN_SUPINFO", null,null ,null, null,null, null);
        if (cursor_sup!=null && cursor_sup.getCount()>0) {
            SUP = new String[cursor_sup.getCount()];
            SUP_NO = new String[cursor_sup.getCount()];
            while(cursor_sup.moveToNext()) {
                String sup_name= cursor_sup.getString(2);
                String sup_no= cursor_sup.getString(1);
                SUP[sup]=new String(sup_name);
                SUP_NO[sup]=new String(sup_no);
                sup++;
            }
            cursor_sup.close();
        }
        if(SUP == null){
            SUP = new String[]{};
            SUP_NO = new String[]{};
        }

        Cursor cursor_cust = db.query("VIEW_MOB_DOWN_CUSTINFO", null,null ,null, null,null, null);
        if (cursor_cust!=null && cursor_cust.getCount()>0) {
            CUST = new String[cursor_cust.getCount()];
            CUST_NO = new String[cursor_cust.getCount()];
            while(cursor_cust.moveToNext()) {
                String cust_name= cursor_cust.getString(2);
                String cust_no= cursor_cust.getString(1);
                CUST[cust]=new String(cust_name);
                CUST_NO[cust]=new String(cust_no);
                cust++;
            }
            cursor_cust.close();
        }
        if(CUST == null){
            CUST = new String[]{};
            CUST_NO = new String[]{};
        }

        Cursor cursor_ywy = db.query("VIEW_MOB_DOWN_USER", null,null ,null, null,null, null);
        if (cursor_ywy!=null && cursor_ywy.getCount()>0) {
            YWY = new String[cursor_ywy.getCount()];
            YWY_NO = new String[cursor_ywy.getCount()];
            while(cursor_ywy.moveToNext()) {
                String ywy_name= cursor_ywy.getString(3);
                String ywy_no= cursor_ywy.getString(1);
                YWY[ywy]=new String(ywy_name);
                YWY_NO[ywy]=new String(ywy_no);
                ywy++;
            }
            cursor_ywy.close();
        }

        if(YWY == null){
            YWY = new String[]{};
        }
        if(YWY_NO == null){
            YWY_NO = new String[]{};
        }

        Cursor cursor_ck = db.query("VIEW_MOB_DOWN_BRANCH", null,null ,null, null,null, null);
        if (cursor_ck!=null && cursor_ck.getCount()>0) {
            CK = new String[cursor_ck.getCount()];
            CK_NO = new String[cursor_ck.getCount()];

            while(cursor_ck.moveToNext()) {
                String ck_name= cursor_ck.getString(2);
                String ck_no= cursor_ck.getString(1);
                CK[ck]=new String(ck_name);
                CK_NO[ck]=new String(ck_no);
                ck++;
            }
            cursor_ck.close();
        }

        if(CK == null){
            CK = new String[]{};
        }

        if(CK_NO == null){
            CK_NO = new String[]{};
        }

        String tableName = "VIEW_MOB_CG_IN";
        if("1".equals(busiType)){
            tableName = "VIEW_MOB_XS_OUT";
        }else if("2".equals(busiType)){
            tableName = "VIEW_MOB_CHECK_YP";
        }
        Cursor cursor_ins = db.rawQuery(" select max(sheet_sn) from " + tableName +" where sheet_no=? ", new String[]{sheet_no} );
        if (cursor_ins!=null && cursor_ins.getCount()>0) {
            while(cursor_ins.moveToNext()) {
                sheet_sn = cursor_ins.getInt(0) + 1;
            }
            cursor_ins.close();
        }

        if(CK == null){
            CK = new String[]{};
        }
        if(CK_NO == null){
            CK_NO = new String[]{};
        }

        adapter_sup = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,SUP);
        adapter_cust = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,CUST);
        adapter_ywy = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,YWY);
        adapter_ck = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CK);
        //设置下拉列表的风格
        adapter_sup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_cust.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_ywy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_ck.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        if("0".equals(busiType)){
            sp_sup.setAdapter(adapter_sup);
        }
        if("1".equals(busiType)){
            sp_cust.setAdapter(adapter_cust);
        }
        sp_ywy.setAdapter(adapter_ywy);
        sp_ck.setAdapter(adapter_ck);
        //添加事件Spinner事件监听
        sp_sup.setOnItemSelectedListener(new SpinnerSelectedListener());
        sp_ywy.setOnItemSelectedListener(new SpinnerSelectedListener());
        sp_ck.setOnItemSelectedListener(new SpinnerSelectedListener());

        if(sheetSn != null && !"".equals(sheetSn)){
            DbUtils du = new DbUtils(this);
            Good good = du.findGood(sheet_no,Integer.valueOf(sheetSn),busiType);
            if(good != null){
                //(int)good.getSheet_sn();
                //text_num_gooddetail (EditText) 数量
                text_num_gooddetail.setText(good.getItem_qty() + "");
                text_color_gooddetail.setText(good.getColor()==null ? "":good.getColor());
                text_size_gooddetail.setText(good.getSize()==null ? "":good.getSize());
                unit_text_gooddetail.setText(good.getItem_unit());
                text_pinum_gooddetail.setText(good.getBatch_no());
                Integer ywyPosition = getListPosition(YWY_NO,good.getEmp_no());
                if(ywyPosition != null){
                    sp_ywy.setSelection(ywyPosition);
                }

                Integer custPosition = getListPosition(CUST_NO,good.getCust_no());
                if(custPosition != null){
                    sp_cust.setSelection(custPosition);
                }

                Integer supPosition = getListPosition(SUP_NO,good.getSup_no());
                if(supPosition != null){
                    sp_sup.setSelection(supPosition);
                }

                Integer branchPosition = getListPosition(CK_NO,good.getBranch_no());
                if(branchPosition != null){
                    sp_ck.setSelection(branchPosition);
                }
                //sp_sup ,sp_cust,,sp_ck;

                good.getBranch_no();
                //日期

               // Spinner sp_ck = (Spinner)findViewById(R.id.sp_ck);
               // sp_ck.setSelection(0);

                if(!"0".equals(busiType) && !"1".equals(busiType)){
//                    cust_name = (RelativeLayout) findViewById(R.id.cust_name);
//                    sup_name = (RelativeLayout) findViewById(R.id.sup_name);
//                    item_price = (RelativeLayout)findViewById(R.id.item_price);
//                    item_batch_no = (RelativeLayout)findViewById(R.id.item_batch_no);
//                    item_color = (RelativeLayout)findViewById(R.id.item_color);
//                    item_size = (RelativeLayout)findViewById(R.id.item_size);
//
//                    cust_name.setVisibility(View.GONE);
//                    sup_name.setVisibility(View.GONE);
//                    item_price.setVisibility(View.GONE);
//                    item_batch_no.setVisibility(View.GONE);
//                    item_color.setVisibility(View.GONE);
//                    item_size.setVisibility(View.GONE);
//                    return;
                }

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_baocun_gooddetails:

                ContentValues values = new ContentValues();
                values.put("mob_no", "00000");//手机唯一标识
                values.put("sheet_no", sheet_no);//手机端入库单单号
                values.put("sheet_sn", sheet_sn);//入库单商品顺序码
                values.put("branch_no", getStringFromList(CK_NO,sp_item_ck));//仓库编号
                values.put("item_no", item_no);//商品编号
                values.put("item_barcode", item_barcode);//商品条码
                values.put("item_qty", text_num_gooddetail.getText().toString());//商品数量
                values.put("item_unit", item_unit_no);//商品单位

                if("0".equals(busiType)){
                    values.put("sup_no", getStringFromList(SUP_NO,sp_item_sup));//供货商编号
                }
                if("1".equals(busiType)){
                    values.put("cust_no", getStringFromList(CUST_NO,sp_item_cust));//客户编号
                }
                if("0".equals(busiType) || "1".equals(busiType)) {
                    values.put("emp_no", getStringFromList(YWY_NO,sp_item_ywy));//业务员编号
                    values.put("item_price", price);//商品价格
                    values.put("lastdate", item_valid_tipday);//商品有效期
                    values.put("batch_no", text_pinum_gooddetail.getText().toString());//批号
                    values.put("color", "");//颜色
                    values.put("size", "");//尺码
                    values.put("item_name", name);//商品名称
                }
                String table_name = "VIEW_MOB_CG_IN";
                if("1".equals(busiType)){
                    table_name = "VIEW_MOB_XS_OUT";
                }else if("2".equals(busiType)){
                    table_name = "VIEW_MOB_CHECK_YP";
                }
                long insert = db.insert(table_name, null, values);

                if (insert>0) {
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
//                    String num_good = Integer.parseInt(getIntent().getStringExtra("num_good"))+1+"";
                    if("0".equals(busiType)){
                        startActivity(new Intent(GoodDetailsActivity.this,RukuDetailsActivity.class).putExtra("sheet_no",sheet_no));
                    }
                    if("1".equals(busiType)){
                        startActivity(new Intent(GoodDetailsActivity.this,OutDetailsActivity.class).putExtra("sheet_no",sheet_no));
                    }
                    if("2".equals(busiType)){
                        startActivity(new Intent(GoodDetailsActivity.this,CheckDetailsActivity.class).putExtra("sheet_no",sheet_no));
                    }
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
                case R.id.sp_cust:
                    sp_item_cust=arg2;
                    break;
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private String getStringFromList(String[] para,int position){
        if(para == null || position < 0){
            return "";
        }
        if(para.length < position + 1){
            return "";
        }
        return para[position];
    }

    private Integer getListPosition(String[] para,String str){
        if(para == null || para.length <= 0 || str==null || "".equals(str)){
            return null;
        }
        for(int i=0;i<para.length;i++){
            if(str.equals(para[i])){
                return i;
            }
        }
        return null;
    }
}
