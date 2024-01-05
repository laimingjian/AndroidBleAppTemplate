package com.lenzetech.zhongyablack.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lenzetech.blelib.ble.BleDevice;
import com.lenzetech.zhongyablack.ble.BleManager;

import java.util.ArrayList;
import java.util.List;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ViewHolder> {
    private final Context mContext;
    private List<BleDevice> dataList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public ScanListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ScanListAdapter(Context mContext, List<BleDevice> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<BleDevice> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScanListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutItemScanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull ScanListAdapter.ViewHolder holder, int position) {
        BleDevice device = dataList.get(position);
        holder.tv_name.setText(device.getBluetoothDevice().getName());
        holder.tv_mac.setText(device.getBluetoothDevice().getAddress());
        holder.tv_rssi.setText(String.valueOf(device.getRssi()));
        holder.btn.setOnClickListener(view -> BleManager.getInstance().connectDevice(device.getBluetoothDevice()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(View v, BleDevice device);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout container;
        TextView tv_name;
        TextView tv_mac;
        TextView tv_rssi;
        AppCompatButton btn;


        public ViewHolder(@NonNull LayoutItemScanBinding binding) {
            super(binding.getRoot());
            container = binding.container;
            tv_name = binding.tvName;
            tv_mac = binding.tvMac;
            tv_rssi = binding.tvRssi;
            btn = binding.btn;
            container.setOnClickListener(this);
            btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, dataList.get(getAdapterPosition()));
            }
        }
    }
}
