package com.haojiu.Utils;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haojiu.Bean.Good;
import com.haojiu.Bean.SheetNo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */

public class DbUtils {
    private Activity activity;
    //VIEW_MOB_ORDER utils :flag:0 入库 flag：1出库 flag：2盘点
    public String getNewOrder(){
        return this.getNewOrder("0");
    }

    public DbUtils(Activity activity){
        this.activity = activity;
    }

    public String getNewOrder(String flag){
        MyOpenHelper myOpenHelper = new MyOpenHelper(this.activity);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = formatter.format(date);

        String[] params = new String[]{"%P" + dateStr + "%",flag};
        Cursor sheet_no_cs = db.query("VIEW_MOB_ORDER", null, " order_no like ? and flag=? ", params, null, null, null, null);

        int maxOrder = 1;
        if (sheet_no_cs != null) {
            if(sheet_no_cs.getCount() > 0){
                maxOrder += sheet_no_cs.getCount();
            }
            sheet_no_cs.close();
        }
        String newOrder = "P" + dateStr + String.valueOf(maxOrder).substring(0);

        ContentValues values = new ContentValues();
        values.put("order_no", newOrder);
        values.put("flag",flag);
        db.insert("VIEW_MOB_ORDER", null, values);
        db.close();

        return newOrder;
    }

    public List<SheetNo> getOrderList(){
        return this.getOrderList("0");
    }

    public ArrayList<SheetNo> getOrderList(String flag){
        MyOpenHelper myOpenHelper = new MyOpenHelper(this.activity);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String[] params = new String[]{flag};
        Cursor sheet_no_cs = db.query("VIEW_MOB_ORDER", null, " flag=? ", params, null, null, null, null);
        ArrayList<SheetNo> orderList = new ArrayList<SheetNo>();

        if (sheet_no_cs != null) {
            while (sheet_no_cs.moveToNext()) {
                SheetNo sheet_no = new SheetNo(sheet_no_cs.getString(1));
                orderList.add(sheet_no);
            }
            sheet_no_cs.close();
        }
        db.close();

        return orderList;
    }

    public void deleteOrder(String order){
        deleteOrder(order,"0");
    }

    public void deleteOrder(String order,String flag){
        MyOpenHelper myOpenHelper = new MyOpenHelper(this.activity);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String tableName = "VIEW_MOB_CG_IN";
        if("1".equals(flag)){
            tableName = "VIEW_MOB_XS_OUT";
        }else if("2".equals(flag)){
            tableName = "VIEW_MOB_CHECK_YP";
        }

        db.delete(tableName, "sheet_no=? ", new String[]{order});
        db.delete("VIEW_MOB_ORDER", "order_no=? and flag=? ", new String[]{order,flag});
        db.close();
    }

    public Good findGood(String sheetNo,int order,String flag){
        if(flag == null){
            return null;
        }
        if("0".equals(flag) || "1".equals(flag)){
            return findGoodFromOI(sheetNo,order,flag);
        }
        if("2".equals(flag)){
            return findGoodFromCheck(sheetNo,order);
        }
        return null;
    }

    public Good findGoodFromOI(String sheetNo,int order,String flag){
        if(flag == null){
            return null;
        }

        String tableName = "VIEW_MOB_CG_IN";
        if("1".equals(flag)){
            tableName = "VIEW_MOB_XS_OUT";
        }

        MyOpenHelper myOpenHelper = new MyOpenHelper(this.activity);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        Cursor good_cs = db.query(tableName, null, " sheet_no=? and sheet_sn=? ", new String[]{sheetNo,order+""}, null, null, null, null);
        Good good = null;

        if(good_cs != null){
            while (good_cs.moveToNext()) {
                String mob_no = good_cs.getString(1);
                String sheet_no = good_cs.getString(2);
                float sheet_sn = good_cs.getFloat(3);
                String branch_no = good_cs.getString(4);
                String cust_no = null;
                String sup_no = null;
                if("0".equals(flag)){
                    sup_no = good_cs.getString(5);
                }else{
                    cust_no = good_cs.getString(5);
                }
                String emp_no = good_cs.getString(6);


                String item_no = good_cs.getString(7);
                String item_barcode = good_cs.getString(8);
                float item_price = good_cs.getFloat(9);

                float item_qty = good_cs.getFloat(10);
                String item_unit = good_cs.getString(11);
                float lastdate = good_cs.getFloat(12);

                String batch_no =  good_cs.getString(13);
                String color = good_cs.getString(14);
                String size = good_cs.getString(15);
                String item_name = good_cs.getString(16);
                String status = good_cs.getString(17);

                good = new Good(mob_no, sheet_no, sheet_sn, branch_no, sup_no,cust_no, emp_no, item_no, item_barcode,
                item_price, item_qty, item_unit, lastdate, batch_no, color, size,item_name,status);
            }

            good_cs.close();
        }

        if(good != null){
            String branchNo = good.getBranch_no();
            if(branchNo != null && !"".equals(branchNo)){
                good_cs = db.query("VIEW_MOB_DOWN_BRANCH", null, "  branch_no=? ", new String[]{}, null, null, null, null);
                if(good_cs != null){
                    while (good_cs.moveToNext()) {
                        good.setBranchName(good_cs.getString(2));
                    }
                    good_cs.close();
                }
            }

            String custNo = good.getCust_no();
            if(custNo != null && !"".equals(custNo)){
                good_cs = db.query("VIEW_MOB_DOWN_CUSTINFO", null, "  cust_no=? ", new String[]{custNo}, null, null, null, null);
                if(good_cs != null){
                    while (good_cs.moveToNext()) {
                        good.setCustName(good_cs.getString(2));
                    }
                    good_cs.close();
                }
            }

            String supNo = good.getSup_no();
            if(supNo != null && !"".equals(supNo)){
                good_cs = db.query("VIEW_MOB_DOWN_SUPINFO", null, "  sup_no=? ", new String[]{supNo}, null, null, null, null);
                if(good_cs != null){
                    while (good_cs.moveToNext()) {
                        good.setSupName(good_cs.getString(2));
                    }
                    good_cs.close();
                }
            }

            String empNo = good.getEmp_no();
            if(empNo != null && !"".equals(empNo)){
                good_cs = db.query("VIEW_MOB_DOWN_USER", null, "  user_id=? ", new String[]{empNo}, null, null, null, null);
                if(good_cs != null){
                    while (good_cs.moveToNext()) {
                        good.setEmpName(good_cs.getString(3));
                    }
                    good_cs.close();
                }
            }
        }
        db.close();
        return good;
    }

    public Good findGoodFromCheck(String sheetNo,int order){
        MyOpenHelper myOpenHelper = new MyOpenHelper(this.activity);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        Cursor good_cs = db.query("VIEW_MOB_CHECK_YP", null, " sheet_no=? and sheet_sn=? ", new String[]{sheetNo,order+""}, null, null, null, null);
        Good good = null;

        if(good_cs != null){
            while (good_cs.moveToNext()) {
                String mob_no = good_cs.getString(1);
                String sheet_no = good_cs.getString(2);
                float sheet_sn = good_cs.getFloat(3);
                String branch_no = good_cs.getString(4);
                String item_no = good_cs.getString(5);
                String item_barcode = good_cs.getString(6);
                float item_qty = good_cs.getFloat(7);
                String item_unit = good_cs.getString(8);
                String status = good_cs.getString(9);

                good = new Good(mob_no,  sheet_no,  sheet_sn,  branch_no, item_no,
                        item_barcode, item_qty, item_unit,status);
            }
            good_cs.close();
        }

        if(good != null){
            String branchNo = good.getBranch_no();
            if(branchNo != null && !"".equals(branchNo)){
                good_cs = db.query("VIEW_MOB_DOWN_BRANCH", null, "  branch_no=? ", new String[]{branchNo}, null, null, null, null);
                if(good_cs != null){
                    while (good_cs.moveToNext()) {
                        good.setBranchName(good_cs.getString(2));
                    }
                    good_cs.close();
                }
            }

            String item_no = good.getItem_no();
            if(item_no != null && !"".equals(item_no)){
                good_cs = db.query("VIEW_MOB_DOWN_ITEMINFO", null, "  item_no=? ", new String[]{item_no}, null, null, null, null);
                if(good_cs != null){
                    while (good_cs.moveToNext()) {
                        good.setItem_name(good_cs.getString(5));
                    }
                    good_cs.close();
                }
            }
        }

        db.close();
        return good;
    }
}
