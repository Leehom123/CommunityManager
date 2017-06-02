package com.haojiu.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.haojiu.DefineView.MyImageView;
import com.haojiu.Zxing.Decode.DecodeThread;
import com.haojiu.Zxing.ScanListener;
import com.haojiu.Zxing.ScanManager;
import com.haojiu.communitymanager.R;

/**
 * Created by leehom on 2017/4/28.
 */

public class ZxingActivity extends Activity implements ScanListener {

    private SurfaceView scanPreview;
    private View scanContainer ,scanCropView;
    private ImageView scanLine;
    private ImageView viewById;
    private ScanManager scanManager;
    public static final int REQUEST_SCAN_MODE_BARCODE_MODE = 0X100;
    private int scanMode=REQUEST_SCAN_MODE_BARCODE_MODE;
    private MyImageView scan_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_zxing);
        setView();
    }

    private void setView() {
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        viewById = (ImageView)findViewById(R.id.authorize_return);
        scan_image = (MyImageView)findViewById(R.id.scan_image);

        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, scanMode, this);
    }

    @Override
    public void scanResult(Result rawResult, Bundle bundle) {
        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        scan_image.setVisibility(View.VISIBLE);
        Log.e("zx","结果："+rawResult.getText());
        startActivity(new Intent(ZxingActivity.this,GoodDetailsActivity.class).putExtra("GoodCode",rawResult.getText().toString()).putExtra("sheet_no",getIntent().getStringExtra("sheet_no")).putExtra("busiType",getIntent().getStringExtra("busiType")));
        finish();
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if(e.getMessage()!=null&&e.getMessage().startsWith("相机")){
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
        scan_image.setVisibility(View.GONE);
    }
}
