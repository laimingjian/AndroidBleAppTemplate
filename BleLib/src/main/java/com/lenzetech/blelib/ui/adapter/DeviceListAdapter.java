package com.lenzetech.blelib.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenzetech.blelib.R;
import com.lenzetech.blelib.ble.BleDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * author:created by mj
 * Date:2022/8/24 17:34
 * Description:显示扫描到的设备列表的适配器
 */
public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private List<BleDevice> dataList;

    public DeviceListAdapter(Context mContext, List<BleDevice> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    public DeviceListAdapter(Context mContext) {
        this.mContext = mContext;
        if (dataList == null)
            dataList = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<BleDevice> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.device_item_layout, parent, false);
        return new BleViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BleViewHolder vh = (BleViewHolder) holder;
        vh.tv_name.setText(dataList.get(position).getBluetoothDevice().getName());
    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class BleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_name;
        private Button btn_connect;

        public BleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            btn_connect = itemView.findViewById(R.id.btn_connect);
            itemView.setOnClickListener(this);
            btn_connect.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, dataList.get(getLayoutPosition()));
            }
        }
    }

    //自定义一个RecycleViewItem点击事件监听的回调接口用来实现Click事件
    public interface OnItemClickListener {
        //当recycleView某个Item被点击时回调
        void onItemClick(View v, BleDevice bleItem);
//        void onItemClick(View v, int position, Entity date);
    }

    //声明自定义的接口
    private OnItemClickListener onItemClickListener;

    //提供setter方法供外部调用
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
