package com.haojiu.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haojiu.Activity.OutDetailsActivity;
import com.haojiu.Adapter.GroupAdapter;
import com.haojiu.Bean.SheetNo;
import com.haojiu.Utils.DbUtils;
import com.haojiu.communitymanager.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leehom on 2017/4/28.
 */

public class ChukuFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private ListView lv_chukudan;
    private RelativeLayout rl_add;
    private SQLiteDatabase db;

    private String sheet_no;
    private ArrayList<SheetNo> sheetNos;
    private String sheet_no_del;
    private PopupWindow popupWindow;
    private View view;
    private ListView lv_group;
    private List<String> groups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chuku_layout, container, false);
        setView(view);
        refreshChukudan(view);
        return view;
    }

    public void setView(View view) {
        lv_chukudan = (ListView) view.findViewById(R.id.lv_chukudan);
        rl_add = (RelativeLayout) view.findViewById(R.id.rlc_add);
        rl_add.setOnClickListener(this);
        lv_chukudan.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlc_add://新增按钮
                DbUtils dbu = new DbUtils(getActivity());
                sheet_no = dbu.getNewOrder("1");
                refreshChukudan(view);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        sheet_no_del = sheetNos.get(position).getSheet_no();
        showWindow(view);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshChukudan(view);
    }

    public void refreshChukudan(View view) {
        //查询
        DbUtils dbu = new DbUtils(getActivity());
        sheetNos = dbu.getOrderList("1");
        lv_chukudan.setAdapter(new ChukuListViewAdaptor(view.getContext()));
        lv_chukudan.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(getActivity(),OutDetailsActivity.class).putExtra("sheet_no",sheetNos.get(i).getSheet_no()));

    }

    class ChukuListViewAdaptor extends BaseAdapter {

        Context context;

        public ChukuListViewAdaptor(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return sheetNos.size();
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
            view = inflater.inflate(R.layout.ruku_item, null);
            TextView sheet_no_tv = (TextView) view.findViewById(R.id.sheet_no_tv);
            TextView rukudan_tv = (TextView) view.findViewById(R.id.rukudan_tv);

            String sheet_no = sheetNos.get(i).getSheet_no();
            rukudan_tv.setText("出库单：");
            sheet_no_tv.setText(sheet_no);
            return view;
        }
    }

    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.pop_layout, null);

            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            lv_group.setDivider(null);
            // 加载数据
            groups = new ArrayList<String>();
            groups.add("删除");

            GroupAdapter groupAdapter = new GroupAdapter(getActivity(), groups);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 300, 110);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        Log.i("coder", "xPos:" + xPos);

        popupWindow.showAsDropDown(parent, xPos,0);
        if(lv_group == null){
            return;
        }
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                DbUtils dbu = new DbUtils(getActivity());
                dbu.deleteOrder(sheet_no_del,"1");
                refreshChukudan(view);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }
}
