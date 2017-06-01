package com.haojiu.Bean;

/**
 * Created by leehom on 2017/5/12.
 */

public class Good {
    private String mob_no;
    private String sheet_no;
    private float sheet_sn;
    private String branch_no;
    private String sup_no;
    private String emp_no;
    private String item_no;
    private String item_barcode;
    private float item_price;
    private float item_qty;
    private String item_unit;
    private float lastdate;
    private String batch_no;
    private String color;
    private String size;
    private String item_name;
    private String status;//状态：是否已提价 0：未提交，1：已提交

    public Good(String mob_no, String sheet_no, float sheet_sn, String branch_no, String sup_no, String emp_no, String item_no, String item_barcode,
                float item_price, float item_qty, String item_unit, float lastdate, String batch_no, String color, String size,String item_name,String status) {
        this.mob_no = mob_no;
        this.sheet_no = sheet_no;
        this.sheet_sn = sheet_sn;
        this.branch_no = branch_no;
        this.sup_no = sup_no;
        this.emp_no = emp_no;
        this.item_no = item_no;
        this.item_barcode = item_barcode;
        this.item_price = item_price;
        this.item_qty = item_qty;
        this.item_unit = item_unit;
        this.lastdate = lastdate;
        this.batch_no = batch_no;
        this.color = color;
        this.size = size;
        this.item_name=item_name;
        this.status = status;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getSheet_no() {
        return sheet_no;
    }

    public void setSheet_no(String sheet_no) {
        this.sheet_no = sheet_no;
    }

    public float getSheet_sn() {
        return sheet_sn;
    }

    public void setSheet_sn(float sheet_sn) {
        this.sheet_sn = sheet_sn;
    }

    public String getBranch_no() {
        return branch_no;
    }

    public void setBranch_no(String branch_no) {
        this.branch_no = branch_no;
    }

    public String getSup_no() {
        return sup_no;
    }

    public void setSup_no(String sup_no) {
        this.sup_no = sup_no;
    }

    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getItem_barcode() {
        return item_barcode;
    }

    public void setItem_barcode(String item_barcode) {
        this.item_barcode = item_barcode;
    }

    public float getItem_price() {
        return item_price;
    }

    public void setItem_price(float item_price) {
        this.item_price = item_price;
    }

    public float getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(float item_qty) {
        this.item_qty = item_qty;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public float getLastdate() {
        return lastdate;
    }

    public void setLastdate(float lastdate) {
        this.lastdate = lastdate;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
