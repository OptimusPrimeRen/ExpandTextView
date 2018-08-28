package com.rentee.expandtextview;

/**
 * author: rentee
 * date: 2018/8/27
 * description：
 */
public class ExpandModel {
    private String mTxt;
    private byte mExpnadState; //ExpandableTextView的展开/收起状态（由于RecyclerView的复用问题，需要放到了外部进行存储）

    public String getTxt() {
        return mTxt;
    }

    public void setTxt(String txt) {
        mTxt = txt;
    }

    public byte getExpnadState() {
        return mExpnadState;
    }

    public void setExpnadState(byte expnadState) {
        mExpnadState = expnadState;
    }
}
