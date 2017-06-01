package com.haojiu.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

	 	public MyOpenHelper(Context context) {
		super(context,"communitymanager.db",null,2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("create table if not exists SERVICE_ADDRESS(_id integer primary key autoincrement,addressname verchar(20),addressvalue verchar(20))");
		db.execSQL("create table if not exists VIEW_MOB_DOWN_ITEMINFO(_id integer primary key autoincrement,item_pf_price float, item_no verchar(8), item_subno verchar(25), item_barcode verchar(25),item_name verchar(60), item_class verchar(20), item_unit_no verchar(10), py_code verchar(30),item_valid_day float, item_valid_tipday float)");
		db.execSQL("create table if not exists VIEW_MOB_DOWN_SUPINFO(_id integer primary key autoincrement,sup_no verchar(12),sup_name verchar(60), py_code verchar(30));");
		db.execSQL("create table if not exists VIEW_MOB_DOWN_USER(_id integer primary key autoincrement,user_id verchar(4),oper_type verchar(4),user_name verchar(30),mob_pw verchar(20),user_status verchar(1));");
		//drop table VIEW_MOB_CG_IN
        db.execSQL(" drop table  VIEW_MOB_CG_IN ");
        db.execSQL("create table if not exists VIEW_MOB_CG_IN(_id integer primary key autoincrement,mob_no verchar(16),sheet_no verchar(16),sheet_sn int,branch_no verchar(6),sup_no verchar(12),emp_no verchar(4),item_no verchar(8),item_barcode verchar(25),item_price float,item_qty float,item_unit verchar(10),lastdate float,batch_no verchar(20),color verchar(10),size verchar(10),item_name verchar(15),status verchar(1));");
		db.execSQL("create table if not exists VIEW_MOB_ORDER(_id integer primary key autoincrement,order_no verchar(16));");
		db.execSQL("create table if not exists VIEW_MOB_DOWN_BRANCH(_id integer primary key autoincrement,branch_no verchar(6),branch_name verchar(30), py_code verchar(15));");
		db.execSQL("create table if not exists VIEW_MOB_DOWN_CUSTINFO(_id integer primary key autoincrement,cust_no verchar(12),cust_name verchar(60), py_code verchar(30));");
	}
}
