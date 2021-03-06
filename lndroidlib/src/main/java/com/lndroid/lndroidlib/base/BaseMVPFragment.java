package com.lndroid.lndroidlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lndroid.lndroidlib.factory.FragmentFactory;
import com.lndroid.lndroidlib.utils.ViewUtils;
import com.lndroid.lndroidlib.view.LoadingPage;

import butterknife.ButterKnife;


/**
 * Created by mac on 16/10/2.
 */

public abstract class BaseMVPFragment extends Fragment {

    private LoadingPage loadingPage;
    private String fragmentKey;
    private View successView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentKey = getFragmentKey();
        inits(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (loadingPage == null) {
            loadingPage = new LoadingPage(getActivity());
            if (successView == null) {
                successView = initView(inflater, container, savedInstanceState);
                loadingPage.setSuccessView(successView);
//                if (successView.getParent() != null)
//                    ViewUtils.removeParent(successView);
                loadingPage.addView(successView, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
            loadingPage.setReLoadListener(getReloadListener());
        } else {
            ViewUtils.removeParent(loadingPage);// 移除frameLayout之前的爹
            ButterKnife.bind(this, successView);
        }
        return loadingPage;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        if (!TextUtils.isEmpty(fragmentKey)) {
            BaseMVPFragment target = FragmentFactory.get(fragmentKey);
            if (target != null) {
                FragmentFactory.remove(fragmentKey);
            }
        }
        super.onDestroy();
    }

//    public void showLoadingView() {
//        loadingPage.setState(LoadingPage.STATE_LOADING);
//        loadingPage.showPage();
//    }

    public void showEmptyView() {
        loadingPage.setState(LoadingPage.STATE_EMPTY);
        loadingPage.showPage();
    }

    public void showErrorView(String error) {
        loadingPage.setState(LoadingPage.STATE_ERROR);
        loadingPage.setErrorViewTips(error);
        loadingPage.showPage();
    }

    public void showSuccessView() {
        loadingPage.setState(LoadingPage.STATE_SUCCESS);
        loadingPage.showPage();
    }

    public void killMyself() {
        this.getFragmentManager().popBackStack();
    }

    public void showToast(String messaga) {
        Toast.makeText(getActivity(), messaga, Toast.LENGTH_LONG).show();
    }

    public View getLayoutView() {
        return loadingPage;
    }

    protected abstract void inits(Bundle savedInstanceState);

    /**
     * 创建加载成功的View
     *
     * @return
     */
    protected abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState);

    /**
     * 标志每个fragment的key
     *
     * @return
     */
    protected abstract String getFragmentKey();

    /**
     * 添加重新加载监听
     *
     * @return
     */
    protected abstract LoadingPage.ReLoadListener getReloadListener();
}
