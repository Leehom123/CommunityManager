package com.haojiu.Fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

import com.haojiu.Activity.RukuDetailsActivity;
import com.haojiu.Adapter.GroupAdapter;
import com.haojiu.Bean.SheetNo;
import com.haojiu.Utils.MyOpenHelper;
import com.haojiu.communitymanager.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by leehom on 2017/4/28.
 */

public class RukuFragment extends Fragment implements View.OnClickListener,  AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView lv_rukudan;
    private int order = 0;
    private RelativeLayout rl_add;
    private String sheet_no;
    private ArrayList<SheetNo> sheetNos;
    private View view;
    private PopupWindow popupWindow;
    private ListView lv_group;
    private List<String> groups;
    private SQLiteDatabase db;
    private String sheet_no_del;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ruku_layout, container, false);
        setView(view);
        loadOrder();
        refreshRukudan(view);
        return view;
    }

    public void setView(View view) {
        lv_rukudan = (ListView) view.findViewById(R.id.lv_rukudan);
        rl_add = (RelativeLayout) view.findViewById(R.id.rl_add);
        rl_add.setOnClickListener(this);
        lv_rukudan.setOnItemLongClickListener(this);
    }


    @Override
    public void onClick(View view) {
        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        switch (view.getId()) {
            case R.id.rl_add://新增按钮
                order++;
                //生成订单号
                long timeMillis = System.currentTimeMillis();
                Date date = new Date(timeMillis);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                String time = formatter.format(date);
                DecimalFormat f = new DecimalFormat("00000");
                String str = f.format(order);
                sheet_no = "SJD" + time + str;
                //将入库单号存入本地数据库
                ContentValues values = new ContentValues();
                values.put("order_no", sheet_no);
                long insert = db.insert("VIEW_MOB_ORDER", null, values);
                if (insert > 0) {
                    Toast.makeText(getActivity(), "入库单号已保存", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "入库单号保存失败", Toast.LENGTH_SHORT).show();
                }
                refreshRukudan(view);
                break;
        }
        db.close();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        sheet_no_del = sheetNos.get(position).getSheet_no();
        showWindow(view);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(getActivity(),RukuDetailsActivity.class).putExtra("sheet_no",sheetNos.get(i).getSheet_no()));
    }


    class RukuListViewAdaptor extends BaseAdapter {

        Context context;

        public RukuListViewAdaptor(Context context) {
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
            String sheet_no = sheetNos.get(i).getSheet_no();
            sheet_no_tv.setText(sheet_no);
            return view;
        }
    }


    public void loadOrder() {
        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateStr = formatter.format(date);

        String[] params = new String[]{"%" + dateStr + "%"};
        Cursor sheet_no_cs = db.query("VIEW_MOB_ORDER", null, " order_no like ? ", params, null, null, null, null);

        int maxOrder = 0;
        if (sheet_no_cs != null && sheet_no_cs.getCount() > 0) {
            while (sheet_no_cs.moveToNext()) {
                String sheet_no = sheet_no_cs.getString(1);
                if (sheet_no == null || "".equals(sheet_no)) {
                    continue;
                }
                int thisOrder = Integer.valueOf(sheet_no.substring(sheet_no.length() - 3, sheet_no.length()));
                //thisOrder
                if (thisOrder > maxOrder) {
                    maxOrder = thisOrder;
                }
            }
        }
        order = maxOrder;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrder();
        refreshRukudan(view);
    }

    public void refreshRukudan(View view) {
        //查询
        MyOpenHelper myOpenHelper = new MyOpenHelper(getActivity());
        db = myOpenHelper.getWritableDatabase();
        sheetNos = new ArrayList<SheetNo>();
        Cursor sheet_no_cs = db.query("VIEW_MOB_ORDER", null, null, null, null, null, null);
        if (sheet_no_cs != null && sheet_no_cs.getCount() > 0) {
            while (sheet_no_cs.moveToNext()) {
                String sheet_no = sheet_no_cs.getString(1);
                SheetNo sheetno = new SheetNo(sheet_no);
                sheetNos.add(sheetno);
            }
        }
        lv_rukudan.setAdapter(new RukuListViewAdaptor(view.getContext()));
        lv_rukudan.setOnItemClickListener(this);
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                startActivity(new Intent(getActivity(),RukuDetailsActivity.class).putExtra("sheet_no",sheetNos.get(i).getSheet_no()));
//            }
//        });
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

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                db.delete("VIEW_MOB_ORDER", "order_no=? ", new String[]{sheet_no_del});
                refreshRukudan(view);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }
}