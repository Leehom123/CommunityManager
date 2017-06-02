package com.haojiu.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haojiu.Bean.Good;
import com.haojiu.Utils.DbUtils;
import com.haojiu.communitymanager.R;

/**
 * Created by Administrator on 2017/5/26.
 */

public class DetailsReadonlyActivity extends Activity {
    private Good good = null;
    private TextView item_name_value;
    private TextView item_barcode_value;
    private TextView item_quatity_value;
    private TextView item_unit_value;
    private TextView item_branch_value;
    private TextView sheet_sn_value;

    private RelativeLayout cust_name;
    private RelativeLayout sup_name;
    private TextView cust_name_value;
    private TextView sup_name_value;
    private TextView item_price_value;
    private TextView item_batch_no_value;
    private TextView item_size_value;
    private TextView item_color_value;
    private RelativeLayout item_price;

    private RelativeLayout item_batch_no;
    private RelativeLayout item_color;
    private RelativeLayout item_size;
    private TextView status_value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.details_readonly_layout);
        TextView title_text = (TextView)findViewById(R.id.common_title_TV_center);
        String  flag = getIntent().getStringExtra("flag");
        if("0".equals(flag)){
            title_text.setText("入库商品信息");
        }else if("1".equals(flag)){
            title_text.setText("出库商品信息");
        }else{
            title_text.setText("盘点商品信息");
        }

        setView();
    }

    private void setView() {
        String sheet_no = getIntent().getStringExtra("sheet_no");
        String  flag = getIntent().getStringExtra("flag");
        int sheet_sn = getIntent().getIntExtra("sheet_sn",0);
        DbUtils du = new DbUtils(this);
        good = du.findGood(sheet_no,sheet_sn,flag);

        sheet_sn_value = (TextView)findViewById(R.id.sheet_sn_value);
        sheet_sn_value.setText(" 顺序号：" + (int)good.getSheet_sn() + "");

        item_name_value = (TextView)findViewById(R.id.item_name_value);
        item_name_value.setText(" 商品名称：" + good.getItem_name());

        item_barcode_value = (TextView)findViewById(R.id.item_barcode_value);
        item_barcode_value.setText(" 条形码：" + good.getItem_barcode());

        item_quatity_value = (TextView)findViewById(R.id.item_quatity_value);
        item_quatity_value.setText(" 商品数量：" + good.getItem_qty() + "");

        item_unit_value = (TextView)findViewById(R.id.item_unit_value);
        item_unit_value.setText(" 商品单位：" + good.getItem_unit());

        item_branch_value = (TextView)findViewById(R.id.item_branch_value);
        item_branch_value.setText(" 仓库：" + good.getBranchName());

        status_value = (TextView)findViewById(R.id.status_value);
        status_value.setText(" 状态："+(good.getStatus() == null || "0".equals(good.getStatus()) ? "未提交":"已提交"));

        if(!"0".equals(flag) && !"1".equals(flag)){
            cust_name = (RelativeLayout) findViewById(R.id.cust_name);
            sup_name = (RelativeLayout) findViewById(R.id.sup_name);
            item_price = (RelativeLayout)findViewById(R.id.item_price);
            item_batch_no = (RelativeLayout)findViewById(R.id.item_batch_no);
            item_color = (RelativeLayout)findViewById(R.id.item_color);
            item_size = (RelativeLayout)findViewById(R.id.item_size);

            cust_name.setVisibility(View.GONE);
            sup_name.setVisibility(View.GONE);
            item_price.setVisibility(View.GONE);
            item_batch_no.setVisibility(View.GONE);
            item_color.setVisibility(View.GONE);
            item_size.setVisibility(View.GONE);
            return;
        }

        if("0".equals(flag)){
            cust_name = (RelativeLayout) findViewById(R.id.cust_name);
            cust_name.setVisibility(View.GONE);
            sup_name_value = (TextView) findViewById(R.id.sup_name_value);
            sup_name_value.setText(" 供货商：" + good.getSupName());
        }else{
            sup_name = (RelativeLayout) findViewById(R.id.sup_name);
            sup_name.setVisibility(View.GONE);
            cust_name_value = (TextView) findViewById(R.id.cust_name_value);
            cust_name_value.setText(" 客户："+good.getSupName());
        }

        item_price_value = (TextView)findViewById(R.id.item_price_value);
        item_price_value.setText(" 价格：" + good.getItem_price());

        item_batch_no_value = (TextView)findViewById(R.id.item_batch_no_value);
        item_batch_no_value.setText(" 批次号："+good.getBatch_no());

        item_color_value = (TextView)findViewById(R.id.item_color_value);
        item_color_value.setText(" 尺寸："+good.getColor());

        item_size_value = (TextView)findViewById(R.id.item_size_value);
        item_size_value.setText(" 颜色："+good.getSize());


    }
}