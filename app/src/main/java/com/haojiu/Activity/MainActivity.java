package com.haojiu.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haojiu.Fragment.ChaxunFragment;
import com.haojiu.Fragment.ChukuFragment;
import com.haojiu.Fragment.GerenFragment;
import com.haojiu.Fragment.PandianFragment;
import com.haojiu.Fragment.RukuFragment;
import com.haojiu.Fragment.ShenheFragment;
import com.haojiu.communitymanager.R;

/**
 * Created by leehom on 2017/4/28.
 */

public class MainActivity extends Activity {

    private RukuFragment rukuFragment;
    private ChukuFragment chukuFragment;
    private PandianFragment pandianFragment;
    private ChaxunFragment chaxunFragment;
    private ShenheFragment shenheFragment;
    private GerenFragment gerenFragment;
    private FragmentManager fm;
    // 当前选中的(0:入库,1:出库,2:盘点，3.查询，4.审核，5.个人)
    private int mCurrentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        setView();
    }

    private void setView() {
        rukuFragment = new RukuFragment();
        chukuFragment = new ChukuFragment();
        pandianFragment = new PandianFragment();
        chaxunFragment = new ChaxunFragment();
        shenheFragment = new ShenheFragment();
        gerenFragment = new GerenFragment();

        RelativeLayout lay_ruku = (RelativeLayout)findViewById(R.id.lay_ruku);
        RelativeLayout lay_chuku = (RelativeLayout)findViewById(R.id.lay_chuku);
        RelativeLayout lay_pandian = (RelativeLayout)findViewById(R.id.lay_pandian);
        RelativeLayout lay_shenhe = (RelativeLayout)findViewById(R.id.lay_shenhe);
        RelativeLayout lay_chaxun = (RelativeLayout)findViewById(R.id.lay_chaxun);
        RelativeLayout lay_geren = (RelativeLayout)findViewById(R.id.lay_geren);
        View view_line_1 = findViewById(R.id.view_line_1);
        View view_line_2 = findViewById(R.id.view_line_2);
        View view_line_3 = findViewById(R.id.view_line_3);
        View view_line_4 = findViewById(R.id.view_line_4);
        View view_line_5 = findViewById(R.id.view_line_5);

        String result_auth = getIntent().getStringExtra("result_auth");


        fm = getFragmentManager();
        android.app.FragmentTransaction FT = fm.beginTransaction();
        FT.add(R.id.Fregment_content, rukuFragment);
        FT.hide(rukuFragment);
        FT.commitAllowingStateLoss();

        FT = fm.beginTransaction();
        FT.add(R.id.Fregment_content, chukuFragment);
        FT.hide(chukuFragment);
        FT.commitAllowingStateLoss();

        FT = fm.beginTransaction();
        FT.add(R.id.Fregment_content, pandianFragment);
        FT.hide(pandianFragment);
        FT.commitAllowingStateLoss();

        FT = fm.beginTransaction();
        FT.add(R.id.Fregment_content, chaxunFragment);
        FT.hide(chaxunFragment);
        FT.commitAllowingStateLoss();

        FT = fm.beginTransaction();
        FT.add(R.id.Fregment_content, shenheFragment);
        FT.hide(shenheFragment);
        FT.commitAllowingStateLoss();

        FT = fm.beginTransaction();
        FT.add(R.id.Fregment_content, gerenFragment);
        FT.hide(gerenFragment);
        FT.commitAllowingStateLoss();

        // 设置底部按钮状态
        mCurrentIndex = -1;

        if (result_auth.equals("0")){//查询
            lay_ruku.setVisibility(View.GONE);
            lay_chuku.setVisibility(View.GONE);
            lay_pandian.setVisibility(View.GONE);
            lay_shenhe.setVisibility(View.GONE);

            view_line_1.setVisibility(View.GONE);
            view_line_2.setVisibility(View.GONE);
            view_line_3.setVisibility(View.GONE);
            view_line_4.setVisibility(View.GONE);
            setBottomBtn(3);
        }else if(result_auth.equals("1")){//出入库，盘点，查询
            view_line_4.setVisibility(View.GONE);
            lay_shenhe.setVisibility(View.GONE);
            setBottomBtn(0);
        }else {
            setBottomBtn(0);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setBottomBtn(0);
    }

    /**
     * 设置底部按钮状态
     *
     * @param index
     */
    public void setBottomBtn(int index) {
        // 入库
        TextView mRKText = (TextView) findViewById(R.id.tx_ruku);
        // 出库
        TextView mCKText = (TextView) findViewById(R.id.tx_chuku);
        // 盘点
        TextView mPDText = (TextView) findViewById(R.id.tx_pandian);
        // 查询
        TextView mCXText = (TextView) findViewById(R.id.tx_chaxun);
        // 审核
        TextView mSHText = (TextView) findViewById(R.id.tx_shenhe);
        // 个人
        TextView mGRText = (TextView) findViewById(R.id.tx_geren);


        if (index == 0) {// 任务
            mRKText.setSelected(true);
            mCKText.setSelected(false);
            mPDText.setSelected(false);
            mCXText.setSelected(false);
            mSHText.setSelected(false);
            mGRText.setSelected(false);
        } else if (index == 1) {// 排行
            mRKText.setSelected(false);
            mCKText.setSelected(true);
            mPDText.setSelected(false);
            mCXText.setSelected(false);
            mSHText.setSelected(false);
            mGRText.setSelected(false);

        } else if (index == 2) {// 伴郎
            mRKText.setSelected(false);
            mCKText.setSelected(false);
            mPDText.setSelected(true);
            mCXText.setSelected(false);
            mSHText.setSelected(false);
            mGRText.setSelected(false);

        }else if (index==3){
            mRKText.setSelected(false);
            mCKText.setSelected(false);
            mPDText.setSelected(false);
            mCXText.setSelected(true);
            mSHText.setSelected(false);
            mGRText.setSelected(false);
        }else if (index==4){
            mRKText.setSelected(false);
            mCKText.setSelected(false);
            mPDText.setSelected(false);
            mCXText.setSelected(false);
            mSHText.setSelected(true);
            mGRText.setSelected(false);
        }else if (index==5){
            mRKText.setSelected(false);
            mCKText.setSelected(false);
            mPDText.setSelected(false);
            mCXText.setSelected(false);
            mSHText.setSelected(false);
            mGRText.setSelected(true);
        }

//        DBUtil.saveTabIndex(index, this);
        if (mCurrentIndex != index) {

            android.app.FragmentTransaction FT = fm.beginTransaction();

            // 隐藏当前画面
            if (mCurrentIndex == 0) {
                FT.hide(rukuFragment);
            } else if (mCurrentIndex == 1) {
                FT.hide(chukuFragment);
            } else if (mCurrentIndex == 2) {
                FT.hide(pandianFragment);
            }else if (mCurrentIndex == 3){
                FT.hide(chaxunFragment);
            }else if (mCurrentIndex == 4){
                FT.hide(shenheFragment);
            }else if (mCurrentIndex == 5){
                FT.hide(gerenFragment);
            }

            if (index == 0) {
                FT.show(rukuFragment);
            } else if (index == 1) {
                FT.show(chukuFragment);
            } else if (index == 2) {
                FT.show(pandianFragment);
            } else if (index == 3) {
                FT.show(chaxunFragment);
            } else if (index == 4) {
                FT.show(shenheFragment);
            } else if (index == 5) {
                FT.show(gerenFragment);
            }
            FT.commit();

            mCurrentIndex = index;
        }
    }


    /**
     * 入库按钮
     *
     * @param paramView
     */
    public void rukuClick(View paramView) {
        // setRadioButton(0);
        // setFragmentIndicator(0, false);
        setBottomBtn(0);

    }
    /**
     * 出库按钮
     *
     * @param paramView
     */
    public void chukuClick(View paramView) {
        // setRadioButton(0);
        // setFragmentIndicator(0, false);
        setBottomBtn(1);

    }
    /**
     * 盘点按钮
     *
     * @param paramView
     */
    public void pandianClick(View paramView) {
        // setRadioButton(0);
        // setFragmentIndicator(0, false);
        setBottomBtn(2);

    }
    /**
     * 查询按钮
     *
     * @param paramView
     */
    public void chaxunClick(View paramView) {
        // setRadioButton(0);
        // setFragmentIndicator(0, false);
        setBottomBtn(3);

    }
    /**
     * 审核按钮
     *
     * @param paramView
     */
    public void shenheClick(View paramView) {
        // setRadioButton(0);
        // setFragmentIndicator(0, false);
        setBottomBtn(4);

    }
    /**
     * 个人按钮
     *
     * @param paramView
     */
    public void gerenClick(View paramView) {
        // setRadioButton(0);
        // setFragmentIndicator(0, false);
        setBottomBtn(5);

    }

}
