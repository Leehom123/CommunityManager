package com.haojiu.Fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.haojiu.ServiceDate.ServiceData;
import com.haojiu.Utils.MyOpenHelper;
import com.haojiu.communitymanager.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leehom on 2017/4/28.
 */

public class GerenFragment extends Fragment implements View.OnClickListener{

    private Button aync_good;
    private Button aync_sup;
    private Button aync_user;
    private Button aync_cust;
    private Button aync_branch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geren_layout, container, false);
        setView(view);
        return view;
    }

    public void setView(View view) {
        aync_good = (Button)view.findViewById(R.id.aync_good);
        aync_sup = (Button)view.findViewById(R.id.aync_sup);
        aync_user = (Button)view.findViewById(R.id.aync_user);
        aync_branch = (Button)view.findViewById(R.id.aync_branch);
        aync_cust = (Button)view.findViewById(R.id.aync_cust);
        aync_branch.setOnClickListener(this);
        aync_cust.setOnClickListener(this);
        aync_good.setOnClickListener(this);
        aync_sup.setOnClickListener(this);
        aync_user.setOnClickListener(this);

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.aync_good:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils httpUtils = new HttpUtils(30 * 1000);
                        String URL = "http://" + ServiceData.getServiceLocalAddress(getActivity().getBaseContext()) + "/smartWM/service/QueryService?operate=asynItem";
                        httpUtils.send(HttpRequest.HttpMethod.POST,URL , new RequestCallBack<Object>() {
                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                String result1 = responseInfo.result.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(result1);
                                    int flag = jsonObject.getInt("flag");
                                    if (flag==1){
                                        JSONArray list = jsonObject.getJSONArray("list");
                                        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
                                        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
                                        db.execSQL("DELETE FROM VIEW_MOB_DOWN_ITEMINFO");
                                        for (int i=0;i<list.length();i++){
                                            JSONObject object=list.getJSONObject(i);
                                            double item_pf_price = object.getDouble("item_pf_price");
                                            String item_no = object.getString("item_no");
                                            String item_subno = "";
                                            if(object.has("item_subno")){
                                                item_subno = object.getString("item_subno");
                                            }
                                            String item_barcode = object.getString("item_barcode");
                                            String item_name = object.getString("item_name");
                                            String item_class = "";
                                            if(object.has("item_class")){
                                                item_class = object.getString("item_class");
                                            }
                                            String item_unit_no = object.getString("item_unit_no");
                                            String py_code = object.getString("py_code");
                                            double item_valid_day = 0;
                                            double item_valid_tipday = 0;
                                            if(object.has("item_valid_day")){
                                                item_valid_day = object.getDouble("item_valid_day");
                                            }
                                            if(object.has("item_valid_tipday")){
                                                item_valid_tipday = object.getDouble("item_valid_tipday");
                                            }
                                            Log.e("zx",item_pf_price+".."+item_no+".."+item_subno+".."+item_barcode+".."+item_name+".."
                                            +item_class+".."+item_unit_no+".."+py_code+".."+item_valid_day+".."+item_valid_tipday);


                                            ContentValues values = new ContentValues();
                                            values.put("item_pf_price", item_pf_price);
                                            values.put("item_no", item_no);
                                            values.put("item_subno", item_subno);
                                            values.put("item_barcode", item_barcode);
                                            values.put("item_name", item_name);
                                            values.put("item_class", item_class);
                                            values.put("item_unit_no", item_unit_no);
                                            values.put("py_code", py_code);
                                            values.put("item_valid_day", item_valid_day);
                                            values.put("item_valid_tipday", item_valid_tipday);
                                            long insert = db.insert("VIEW_MOB_DOWN_ITEMINFO", null, values);

                                            if (insert>0) {
                                                if (i==list.length()-1){
                                                    Toast.makeText(getActivity(), "同步商品成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getActivity(), "同步商品失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        db.close();
                                    }else{
                                        Toast.makeText(view.getContext(),jsonObject.getString("desc"),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
                    }
                }).start();

                break;
            case R.id.aync_sup:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils httpUtils = new HttpUtils(30 * 1000);
                        httpUtils.send(HttpRequest.HttpMethod.POST, "http://" + ServiceData.getServiceLocalAddress(getActivity().getBaseContext()) + "/smartWM/service/QueryService?operate=asynSup", new RequestCallBack<Object>() {

                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                String result1 = responseInfo.result.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(result1);
                                    String flag = jsonObject.getString("flag");
                                    if ("1".equals(flag)){
                                        JSONArray list = jsonObject.getJSONArray("list");
                                        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
                                        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
                                        db.execSQL("DELETE FROM VIEW_MOB_DOWN_SUPINFO");
                                        for (int i=0;i<list.length();i++){
                                            JSONObject object = list.getJSONObject(i);
                                            String sup_no = object.getString("sup_no");
                                            String sup_name = object.getString("sup_name");
                                            String py_code = object.getString("py_code");
                                            Log.e("zx",sup_no+"..."+sup_name+"..."+py_code);

                                            ContentValues values = new ContentValues();
                                            values.put("sup_no", sup_no);
                                            values.put("sup_name", sup_name);
                                            values.put("py_code", py_code);
                                            long insert = db.insert("VIEW_MOB_DOWN_SUPINFO", null, values);
                                            if (insert>0) {
                                                if (i==list.length()-1){
                                                    Toast.makeText(getActivity(), "同步供货商成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getActivity(), "同步供货商失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        db.close();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
                    }}).start();
                break;
            case R.id.aync_user:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils httpUtils = new HttpUtils(30 * 1000);
                        httpUtils.send(HttpRequest.HttpMethod.POST, "http://" + ServiceData.getServiceLocalAddress(getActivity().getBaseContext()) + "/smartWM/service/QueryService?operate=asynUser", new RequestCallBack<Object>() {

                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                String result1 = responseInfo.result.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(result1);
                                    String flag = jsonObject.getString("flag");
                                    if ("1".equals(flag)){
                                        JSONArray list = jsonObject.getJSONArray("list");
                                        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
                                        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
                                        db.execSQL("DELETE FROM VIEW_MOB_DOWN_USER");
                                        for (int i=0;i<list.length();i++){
                                            JSONObject object = list.getJSONObject(i);
                                            String user_id = object.getString("user_id");
                                            String oper_type = object.getString("oper_type");
                                            String user_name = object.getString("user_name");
                                            String mob_pw = object.getString("mob_pw");
                                            String user_status = object.getString("user_status");
                                            Log.e("zx",user_id+"..."+oper_type+"..."+user_name+"..."+mob_pw+"..."+user_status);

                                            ContentValues values = new ContentValues();
                                            values.put("user_id", user_id);
                                            values.put("oper_type", oper_type);
                                            values.put("user_name", user_name);
                                            values.put("mob_pw", mob_pw);
                                            values.put("user_status", user_status);
                                            long insert = db.insert("VIEW_MOB_DOWN_USER", null, values);
                                            if (insert>0) {
                                                if (i==list.length()-1){
                                                    Toast.makeText(getActivity(), "同步业务员成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getActivity(), "同步业务员失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        db.close();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
                    }}).start();
                break;
            //cuishh 添加同步仓库和客户 beign  2017-05-12 13:09 pm
            case R.id.aync_cust:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils httpUtils = new HttpUtils(30 * 1000);
                        httpUtils.send(HttpRequest.HttpMethod.POST, "http://" + ServiceData.getServiceLocalAddress(getActivity().getBaseContext()) + "/smartWM/service/QueryService?operate=asynCust", new RequestCallBack<Object>() {
                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                String result1 = responseInfo.result.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(result1);
                                    String flag = jsonObject.getString("flag");
                                    if ("1".equals(flag)){
                                        JSONArray list = jsonObject.getJSONArray("list");
                                        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
                                        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

                                        db.execSQL("DELETE FROM VIEW_MOB_DOWN_CUSTINFO");

                                        for (int i=0;i<list.length();i++){
                                            JSONObject object = list.getJSONObject(i);
                                            String cust_no = object.getString("cust_no");
                                            String cust_name = object.getString("cust_name");
                                            String py_code = object.getString("py_code");
                                            Log.e("zx",cust_no+"..."+cust_name+"..."+py_code);

                                            ContentValues values = new ContentValues();
                                            values.put("cust_no", cust_no);
                                            values.put("cust_name", cust_name);
                                            values.put("py_code", py_code);
                                            long insert = db.insert("VIEW_MOB_DOWN_CUSTINFO", null, values);
                                            if (insert>0) {
                                                if (i==list.length()-1){
                                                    Toast.makeText(getActivity(), "同步客户成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getActivity(), "同步客户失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        db.close();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
                    }
                }).start();
                break;

            case R.id.aync_branch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils httpUtils = new HttpUtils(30 * 1000);
                        httpUtils.send(HttpRequest.HttpMethod.POST, "http://" + ServiceData.getServiceLocalAddress(getActivity().getBaseContext()) + "/smartWM/service/QueryService?operate=asynBranch", new RequestCallBack<Object>() {

                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                String result1 = responseInfo.result.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(result1);
                                    String flag = jsonObject.getString("flag");
                                    if ("1".equals(flag)){
                                        JSONArray list = jsonObject.getJSONArray("list");
                                        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
                                        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

                                        db.execSQL("DELETE FROM VIEW_MOB_DOWN_BRANCH");

                                        for (int i=0;i<list.length();i++){
                                            JSONObject object = list.getJSONObject(i);
                                            String branch_no = object.getString("branch_no");
                                            String branch_name = object.getString("branch_name");
                                            String py_code = object.getString("py_code");
                                            Log.e("zx",branch_no+"..."+branch_name+"..."+py_code);

                                            ContentValues values = new ContentValues();
                                            values.put("branch_no", branch_no);
                                            values.put("branch_name", branch_name);
                                            values.put("py_code", py_code);
                                            long insert = db.insert("VIEW_MOB_DOWN_BRANCH", null, values);
                                            if (insert>0) {
                                                if (i==list.length()-1){
                                                    Toast.makeText(getActivity(), "同步仓库成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getActivity(), "同步仓库失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        db.close();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
                    }
                }).start();
                break;

        }
    }
}
