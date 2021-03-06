package com.lndroid.lndroidlib.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lndroid.lndroidlib.R;


/**
 * Created by Administrator on 2015/12/22.
 */
public class LoadingPage extends CoordinatorLayout {
    public static final int STATE_UNKOWN = 0;
    //    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;
    public int state = STATE_UNKOWN;

    //    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View emptyView;// 空界面
    private View successView;// 加载成功的界面
    private TextView mErrorTip_tv;   // 错误页面的提示文本

    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSuccessView(View successView) {
        this.successView = successView;
    }

    public View getSuccessView() {
        return successView;
    }

    public void setState(int value) {
        state = value;
    }

    private void init() {
//        loadingView = createLoadingView(); // 创建了加载中的界面
//        if (loadingView != null) {
//            this.addView(loadingView, new LayoutParams(
//                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        }
        errorView = createErrorView(); // 加载错误界面
        if (errorView != null) {
            this.addView(errorView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        emptyView = createEmptyView(); // 加载空的界面
        if (emptyView != null) {
            this.addView(emptyView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        showPage();// 根据不同的状态显示不同的界面
    }

    // 根据不同的状态显示不同的界面
    public void showPage() {
//        if (loadingView != null) {
//            loadingView.setVisibility(state == STATE_UNKOWN
//                    || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
//        }
        if (errorView != null) {
            errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (emptyView != null) {
            emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (successView != null) {
            successView.setVisibility(state == STATE_UNKOWN
                    || state == STATE_SUCCESS ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /* 创建了空的界面 */
    private View createEmptyView() {
        View view = View.inflate(this.getContext(), R.layout.loadpage_empty,
                null);
        return view;
    }

    /* 创建了错误界面 */
    private View createErrorView() {
        View view = View.inflate(this.getContext(), R.layout.loadpage_error,
                null);
        mErrorTip_tv = (TextView)view.findViewById(R.id.loadpage_error_tips);
        Button page_bt = (Button) view.findViewById(R.id.page_bt);
        page_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (reLoadListener != null) {
                    reLoadListener.reLoad();
                }
            }
        });
        return view;
    }

    public void setErrorViewTips(String error){
        mErrorTip_tv.setText(error);
    }

//    /* 创建加载中的界面 */
//    private View createLoadingView() {
//        View view = View.inflate(this.getContext(),
//                R.layout.loadpage_loading, null);
//        return view;
//    }

    public enum LoadResult {
        error(2), empty(3), success(4);

        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public interface ReLoadListener {
        void reLoad();
    }

    private ReLoadListener reLoadListener;

    public void setReLoadListener(ReLoadListener reLoadListener) {
        this.reLoadListener = reLoadListener;
    }

}
