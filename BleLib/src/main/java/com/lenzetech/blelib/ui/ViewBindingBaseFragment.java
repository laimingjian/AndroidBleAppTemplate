package com.lenzetech.blelib.ui;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;


/**
 * author:created by mj
 * Date:2022/8/24 17:43
 * Description:使用ViewBinding的基类Fragment
 */
public abstract class ViewBindingBaseFragment<VB extends ViewBinding> extends Fragment {
    private VB mBinding;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = onCreateViewBinding(inflater, container);
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public Context getMContext() {
        return mContext;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Fragment 的存在时间比其视图长。请务必在 Fragment 的 onDestroyView() 方法中清除对绑定类实例的所有引用
        if (mBinding != null)
            mBinding = null;
    }

    public VB getViewBinding() {
        return mBinding;
    }

    //初始化ViewBinding方法
    protected abstract VB onCreateViewBinding(LayoutInflater inflater, ViewGroup container);

    //初始化数据，监听事件或接口和方法
    protected abstract void init();
}
