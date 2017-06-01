package com.haojiu.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haojiu.Bean.Good;
import com.haojiu.Fragment.RukuFragment;
import com.haojiu.Utils.CircleCommitBean;
import com.haojiu.Utils.MyOpenHelper;
import com.haojiu.Utils.PermissionUtil;
import com.haojiu.communitymanager.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by leehom on 2017/4/28.
 */

public class RukuDetailsActivity extends Activity implements View.OnClickListener{

    private RelativeLayout rl_saomiao;
    private ListView lv_good;
    private int num_good=0;
    //拍照权限请求码
    private static final int REQUEST_PICTURE_PERMISSION = 1;
    //拍照权限
    private static final String[] PICTURE_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String name;
    private String barcode;
    private String sheet_no;
    private String num_good1;
    private ArrayList<Good> goodsList;
    private SQLiteDatabase db;
    private RelativeLayout rl_commit;
    private CircleCommitBean cCommitBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_rukudetails);
        setView();
    }

    private void setView() {
        sheet_no = getIntent().getStringExtra("sheet_no");

        rl_commit = (RelativeLayout)findViewById(R.id.rl_commit);
        rl_commit.setOnClickListener(this);
        rl_saomiao = (RelativeLayout)findViewById(R.id.rl_saomiao);
        lv_good = (ListView)findViewById(R.id.lv_good);
        TextView title_text = (TextView)findViewById(R.id.common_title_TV_center);
        title_text.setText("商品列表");
        rl_saomiao.setOnClickListener(this);
        MyOpenHelper myOpenHelper = new MyOpenHelper(getApplicationContext());
        db = myOpenHelper.getWritableDatabase();
        goodsList = new ArrayList<>();
        query();//查询更新的数据库中的数据
        lv_good.setAdapter(new RukuListViewAdaptor(getApplicationContext()));
        PermissionUtil permissionUtil = new PermissionUtil(this);
        //若没有权限
        if (!permissionUtil.hasPermissionGranted(PICTURE_PERMISSIONS)) {
            //请求所需权限
            permissionUtil.requestRequiredPermissions(PICTURE_PERMISSIONS, R.string.need_permissions, REQUEST_PICTURE_PERMISSION);
            return;
        }
        if (this.isFinishing()) {
            return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_saomiao://扫描图标
                //跳转到扫描页
                startActivity(new Intent(RukuDetailsActivity.this,ZxingActivity.class).putExtra("sheet_no",getIntent().getStringExtra("sheet_no")));
                finish();
                break;
            case R.id.rl_commit:
                rl_commit.setClickable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cCommitBean = new CircleCommitBean();
                        cCommitBean.setSum(goodsList.size());
                        query();
                        for (int i=0;i<goodsList.size();i++){
                            RequestParams params = new RequestParams();
                            String status = goodsList.get(i).getStatus();

                            if(status != null && "1".equals(status)){
                                cCommitBean.commitSucess();
                                if(i >= goodsList.size() -1){
                                    commitCallBack(false);
                                }
                                continue;
                            }
                            params.addBodyParameter("phoneId", "00000");//手机唯一标识
                            params.addBodyParameter("sheet_no", sheet_no);//手机端入库单单号
                            params.addBodyParameter("sheet_sn", goodsList.get(i).getSheet_sn()+"");//入库单商品顺序码
                            params.addBodyParameter("branch_no", "02");//仓库编号
                            params.addBodyParameter("sup_no", goodsList.get(i).getSup_no()+"");//供货商编号
                            params.addBodyParameter("emp_no", goodsList.get(i).getEmp_no()+"");//业务员编号
                            params.addBodyParameter("item_no", goodsList.get(i).getItem_no()+"");//商品编号
                            params.addBodyParameter("item_barcode", goodsList.get(i).getItem_barcode()+"");//商品条码
                            params.addBodyParameter("item_price", goodsList.get(i).getItem_price()+"");//商品价格
                            params.addBodyParameter("item_qty", goodsList.get(i).getItem_qty()+"");//商品数量
                            params.addBodyParameter("item_unit", goodsList.get(i).getItem_unit()+"");//商品单位
                            float lastdate = goodsList.get(i).getLastdate();
                            params.addBodyParameter("lastdate", "2017-07-04");//商品有效期
                            params.addBodyParameter("batch_no", goodsList.get(i).getBatch_no()+"");//批号
                            params.addBodyParameter("color", goodsList.get(i).getColor()+"");//颜色
                            params.addBodyParameter("size", goodsList.get(i).getSize()+"");//尺码
                            HttpUtils httpUtils=new HttpUtils(30*1000);
                            httpUtils.send(HttpRequest.HttpMethod.POST, "http://192.168.0.101:8080/smartWM/service/BusiService?operate=inBranch", params, new RequestCallBack<Object>() {
                                @Override
                                public void onSuccess(ResponseInfo<Object> responseInfo) {
                                    String response = responseInfo.result.toString();
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        String flag = object.getString("flag");
                                        if (flag!=null && !("".equals(flag))){
                                            int row = object.getInt("row");
                                            cCommitBean.commitSucess();
                                        }else {
                                            cCommitBean.commitFail();
                                        }
                                        commitCallBack(true);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Toast.makeText(getApplicationContext(),"连接服务器失败",Toast.LENGTH_SHORT).show();
                                    cCommitBean.commitFail();
                                    commitCallBack(true);
                                }
                            });
                        }
                    }
                }).start();
//                finish();
                break;
        }
    }


    public void query(){
        goodsList.clear();
        Cursor sheet_no_cs = db.query("VIEW_MOB_CG_IN", null, "sheet_no=?",new String[]{sheet_no}, null, null, null, null);
        if (sheet_no_cs!=null && sheet_no_cs.getCount()>0) {
            while (sheet_no_cs.moveToNext()) {
                String mob_no = sheet_no_cs.getString(1);
                String sheetno = sheet_no_cs.getString(2);
                float sheet_sn = sheet_no_cs.getFloat(3);
                String branch_no = sheet_no_cs.getString(4);
                String sup_no = sheet_no_cs.getString(5);
                String emp_no = sheet_no_cs.getString(6);
                String item_no = sheet_no_cs.getString(7);
                String bar_code = sheet_no_cs.getString(8);
                float item_price = sheet_no_cs.getFloat(9);
                float item_qty = sheet_no_cs.getFloat(10);
                String item_unit = sheet_no_cs.getString(11);
                float lastdate = sheet_no_cs.getFloat(12);
                String batch_no = sheet_no_cs.getString(13);
                String color = sheet_no_cs.getString(14);
                String size = sheet_no_cs.getString(15);
                String item_name = sheet_no_cs.getString(16);
                String status = sheet_no_cs.getString(17);
                Good good=new Good( mob_no,  sheetno,  sheet_sn,  branch_no,  sup_no,  emp_no,
                        item_no, bar_code, item_price,  item_qty, item_unit,  lastdate,  batch_no,
                        color,size,item_name,status);
                goodsList.add(good);
            }
        }
        sheet_no_cs.close();
       // db.close();
    }
    class RukuListViewAdaptor extends BaseAdapter {
        Context context;
        public RukuListViewAdaptor(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return goodsList.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.chuku_item, null);
            TextView good_name_item = (TextView)view.findViewById(R.id.good_name_item);
            TextView good_code_item = (TextView)view.findViewById(R.id.good_code_item);
            good_name_item.setText(goodsList.get(i).getItem_name());
            good_code_item.setText(goodsList.get(i).getItem_barcode());
            String status = goodsList.get(i).getStatus();

            if(status != null && "1".equals(status)){
                good_name_item.setTextColor(getResources().getColor(R.color.red_color));
                good_code_item.setTextColor(getResources().getColor(R.color.red_color));
                TextView name = (TextView)view.findViewById(R.id.rukudan_tv);
                TextView code = (TextView)view.findViewById(R.id.rukugood_tv);
                name.setTextColor(getResources().getColor(R.color.red_color));
                code.setTextColor(getResources().getColor(R.color.red_color));
            }
            return view;
        }
    }

    public void commitCallBack(boolean hasLoop){
        if(cCommitBean.isCurrentFlag()){
            Good good = goodsList.get(this.cCommitBean.getPosition()-1);
            ContentValues values = new ContentValues();
            values.put("status", "1");//key为字段名，value为值
            db.update("VIEW_MOB_CG_IN", values, "sheet_no=? and sheet_sn=? ", new String[]{sheet_no,good.getSheet_sn() + ""});
            //db.close();
        }
        if(cCommitBean.getPosition() >= cCommitBean.getSum()) {
            if(!hasLoop) {
                Looper.prepare();
            }
            if (cCommitBean.getFail() > 0) {
                Toast.makeText(getApplicationContext(), "入库单[" + sheet_no + "]中" + cCommitBean.getFail() + "条商品提交失败！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "入库单[" + sheet_no + "]本次提交成功", Toast.LENGTH_SHORT).show();
                db.delete("VIEW_MOB_CG_IN", "sheet_no=? ", new String[]{sheet_no});
                db.delete("VIEW_MOB_ORDER", "order_no=? ", new String[]{sheet_no});
            }
            if(!hasLoop) {
                Looper.loop();
            }
            //startActivity(new Intent(RukuDetailsActivity.this,RukuFragment.class));
            rl_commit.setClickable(true);
            finish();
        }
    }
}
