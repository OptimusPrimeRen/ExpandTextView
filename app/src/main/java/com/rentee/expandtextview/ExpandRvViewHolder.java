package com.rentee.expandtextview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * author: rentee
 * date: 2018/8/27
 * description：
 */
public class ExpandRvViewHolder extends RecyclerView.ViewHolder {

    private ExpandableTextView mEtv;
    private ImageView mIvArrow;
    private ExpandModel mModel;

    public ExpandRvViewHolder(View itemView) {
        super(itemView);
        mEtv = itemView.findViewById(R.id.tv_expand);
        mIvArrow = itemView.findViewById(R.id.iv_expand_arrow);
        mIvArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtv.toggle();
                mModel.setExpnadState(mEtv.getState());
                toggleArrow(mEtv.getState(), true);
            }
        });
    }

    public void updateView(final ExpandModel model) {
        mModel = model;
        mEtv.setText(mModel.getTxt());
        if (mModel.getExpnadState() == ExpandableTextView.STATE_UNKNOWN) {
            mEtv.initTextState(ExpandableTextView.STATE_COLLAPSE, new ExpandableTextView.TextStateInitListener() {
                @Override
                public void initFinish(byte state) {
                    mModel.setExpnadState(state);
                    toggleArrow(mModel.getExpnadState(), false);
                }
            });
        } else {
            mEtv.resetState(mModel.getExpnadState());
            toggleArrow(mModel.getExpnadState(), false);
        }
    }

    /**
     * 反转箭头
     */
    private void toggleArrow(byte state, boolean needAnim) {
        switch (state) {
            case ExpandableTextView.STATE_NOT_OVERFLOW:
                mIvArrow.setVisibility(View.INVISIBLE);
                break;
            case ExpandableTextView.STATE_EXPAND:
                mIvArrow.setVisibility(View.VISIBLE);
                if (needAnim) {
                    mIvArrow.animate().rotation(180).setDuration(ExpandableTextView.ANIM_DURATION).start();
                } else {
                    mIvArrow.setRotation(180);
                }
                break;
            case ExpandableTextView.STATE_COLLAPSE:
                mIvArrow.setVisibility(View.VISIBLE);
                if (needAnim) {
                    mIvArrow.animate().rotation(0).setDuration(ExpandableTextView.ANIM_DURATION).start();
                } else {
                    mIvArrow.setRotation(0);
                }
                break;
            default:
                break;
        }
    }
}
