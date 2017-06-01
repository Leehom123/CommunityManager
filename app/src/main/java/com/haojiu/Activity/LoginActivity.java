package com.haojiu.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button btn_login_setting;
    private Button btn_login_register;
    private Button btn_login;
    private EditText et_username;
    private EditText et_password;
    private HttpUtils httpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        setView();
    }

    private void setView() {
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login_setting = (Button)findViewById(R.id.btn_login_setting);
        btn_login_register = (Button)findViewById(R.id.btn_login_register);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login_setting.setOnClickListener(this);
        btn_login_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login://登陆
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text_username = et_username.getText().toString();
                        String text_password = et_password.getText().toString();
                        String mi_password = MD5Utils.md5(text_password);
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("userId", text_username);
                        params.addBodyParameter("pass", mi_password);
                        httpUtils = new HttpUtils(30 * 1000);
                        String URL = "http://"+ ServiceData.getServiceCloudAddress(getBaseContext()) +"/smartWM/service/OrgService?operate=login";
                        httpUtils.send(HttpRequest.HttpMethod.POST, URL, params, new RequestCallBack<Object>() {
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
                                            String result_auth = result.getString("auth");
                                            Toast.makeText(getApplicationContext(),result_desc,Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("result_auth",result_auth));
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),result_desc,Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(),"登陆失败！",Toast.LENGTH_SHORT).show();
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
            case R.id.btn_login_setting://设置
                startActivity(new Intent(LoginActivity.this,SettingActivity.class));
                break;
            case R.id.btn_login_register://注册
                startActivity(new Intent(LoginActivity.this,RegistActivity.class));
                break;
        }
    }
}
