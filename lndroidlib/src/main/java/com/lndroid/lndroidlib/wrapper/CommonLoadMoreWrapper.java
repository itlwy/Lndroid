package com.lndroid.lndroidlib.wrapper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lndroid.lndroidlib.R;
import com.zhy.adapter.recyclerview.recyclerview.wrapper.LoadMoreWrapper;


/**
 * Created by itlwy on 17/6/18.
 */
public class CommonLoadMoreWrapper<T> extends LoadMoreWrapper<T> implements View.OnClickListener {

    public static final int STATUS_LOADING = 4;  // 加载中
    public static final int STATUS_SUCCESS = 5;  // 成功
    public static final int STATUS_FAILS = 6;   // 失败
    public static final int STATUS_EMPTY = 7;    //暂无更多
    private View mFooterView;
    private LinearLayout mTipLLT;
    private TextView mTipTv;
    private LinearLayout mLoadingLLT;
    private int mStatus = STATUS_LOADING;


    public CommonLoadMoreWrapper(RecyclerView.Adapter adapter, Context context) {
        super(adapter);
        mFooterView = View.inflate(context, R.layout.default_loading, null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
        mFooterView.setLayoutParams(params);
        setLoadMoreView(mFooterView);
        mLoadingLLT = (LinearLayout) mFooterView.findViewById(R.id.loading_llt);
        mTipLLT = (LinearLayout) mFooterView.findViewById(R.id.tip_llt);
        mTipTv = (TextView) mFooterView.findViewById(R.id.tip_tv);
        mTipTv.setOnClickListener(this);
    }

    public void switchLoadMoreStatus(int statusCode) {
        switch (statusCode) {
            case STATUS_EMPTY: {
                mLoadingLLT.setVisibility(View.GONE);
                mTipLLT.setVisibility(View.VISIBLE);
                mTipTv.setText("暂无更多");
                break;
            }
            case STATUS_LOADING: {
                mLoadingLLT.setVisibility(View.VISIBLE);
                mTipLLT.setVisibility(View.GONE);
                break;
            }
            case STATUS_SUCCESS: {
                mLoadingLLT.setVisibility(View.GONE);
                mTipLLT.setVisibility(View.VISIBLE);
                mTipTv.setText("加载成功");
                notifyDataSetChanged();
                break;
            }
            case STATUS_FAILS: {
                mLoadingLLT.setVisibility(View.GONE);
                mTipLLT.setVisibility(View.VISIBLE);
                mTipTv.setText("加载失败,点击重试");
                break;
            }
        }
        this.mStatus = statusCode;
    }

    @Override
    public void onClick(View v) {
        mOnLoadMoreListener.onLoadMoreRequested();
    }


}
