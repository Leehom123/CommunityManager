package com.haojiu.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.haojiu.ServiceDate.ServiceData;
import com.haojiu.communitymanager.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.haojiu.Utils.MD5Utils;

/**
 * Created by leehom on 2017/4/28.
 */

public class RegistActivity extends Activity implements View.OnClickListener {
    private static final String[] AUTH={"查询","出入库，盘点，查询","所有权限"};
    private Spinner sp_auth;
    private ArrayAdapter<String> adapter;
    private EditText et_regist_username;
    private EditText et_regist_password;
    private EditText et_turename;
    private EditText et_comname;
    private EditText et_comaddress;
    private EditText et_re_pass;
    private Button btn_regist;
    private int sp_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_regist);
        setView();
    }

    private void setView() {
        et_regist_username = (EditText)findViewById(R.id.et_regist_username);
        et_regist_password = (EditText)findViewById(R.id.et_regist_password);
        et_turename = (EditText)findViewById(R.id.et_turename);
        et_comname = (EditText)findViewById(R.id.et_comname);
        et_comaddress = (EditText)findViewById(R.id.et_comaddress);
        et_re_pass = (EditText)findViewById(R.id.et_re_pass);
        btn_regist = (Button) findViewById(R.id.btn_regist);
        sp_auth = (Spinner) findViewById(R.id.sp_auth);
        btn_regist.setOnClickListener(this);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,AUTH);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        sp_auth.setAdapter(adapter);
        //添加事件Spinner事件监听
        sp_auth.setOnItemSelectedListener(new SpinnerSelectedListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regist://注册
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String rigist_username = et_regist_username.getText().toString();
                        String regist_password = et_regist_password.getText().toString();
                        String turename = et_turename.getText().toString();
                        String comname = et_comname.getText().toString();
                        String comaddress = et_comaddress.getText().toString();
                        String mi_regist_pass = MD5Utils.md5(regist_password);
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("userId", rigist_username);
                        params.addBodyParameter("pass", mi_regist_pass);
                        params.addBodyParameter("userName", turename);
                        params.addBodyParameter("phoneId", "000000");
                        params.addBodyParameter("comName", comname);
                        params.addBodyParameter("address", comaddress);
                        params.addBodyParameter("auth", sp_item+"");
                        HttpUtils httpUtils = new HttpUtils(30 * 1000);
                        httpUtils.send(HttpRequest.HttpMethod.POST, "http://" + ServiceData.getServiceCloudAddress(getBaseContext()) + "/smartWM/service/OrgService?operate=regist", params, new RequestCallBack<Object>() {
                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                String result1 = responseInfo.result.toString();
                                JSONObject jsStr = null;
                                try {
                                    jsStr = new JSONObject(result1);
                                    int flag = jsStr.getInt("flag");
                                    if (flag==1){
                                        String desc = jsStr.getString("desc");
                                        JSONObject result = jsStr.getJSONObject("result");
                                        int result_flag = result.getInt("flag");
                                        String result_desc = result.getString("desc");
                                        if (result_flag==1){
                                            Toast.makeText(getApplicationContext(),result_desc,Toast.LENGTH_SHORT).show();
                                            Log.e("zx",result_desc);
                                            startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),result_desc,Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(),"注册失败！",Toast.LENGTH_SHORT).show();
                                        Log.e("zx","注册失败！");
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

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            sp_item=arg2;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
