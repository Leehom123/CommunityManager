package com.haojiu.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class CircleCommitBean {
    private int sum = 0;
    private int success = 0;
    private int fail = 0;
    private int position = 0;
    private List<Integer> failList = new ArrayList<Integer>();

    private List<Integer> succesList = new ArrayList<Integer>();
    private boolean  currentFlag = false;

    public void commitSucess(){
        position ++;
        success ++;
        currentFlag = true;
        int position = failList.size() + succesList.size();
        succesList.add(position);
    }

    public void commitFail(){
        position ++;
        fail ++;
        currentFlag = false;
        int position = failList.size() + succesList.size();
        failList.add(position);
    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(boolean currentFlag) {
        this.currentFlag = currentFlag;
    }

    public int getSum() {
        return sum;
    }

    public int getSuccess() {
        return success;
    }

    public int getFail() {
        return fail;
    }

    public List<Integer> getFailList() {
        return failList;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public void setFailList(List<Integer> failList) {
        this.failList = failList;
    }

    public List<Integer> getSuccesList() {
        return succesList;
    }

    public void setSuccesList(List<Integer> succesList) {
        this.succesList = succesList;
    }
}
