package com.lenzetech.zhongyablack.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.lenzetech.blelib.utils.ToastUtils;
import com.lenzetech.zhongyablack.MyApp;
import com.lenzetech.zhongyablack.R;
import com.lenzetech.zhongyablack.ble.BleManager;
import com.lenzetech.zhongyablack.ble.CommandManager;

public class EditDialog {
    private static AlertDialog editDialog;

    public static void showEditNumber(Context context, Boolean cancelable, int position, int hint) {
        if (editDialog != null && editDialog.isShowing()) {
            editDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.myDialog);
        //创建对话框
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.edit_dialog_layout, null);
        //获取自定义布局
        builder.setView(layout);
        //设置对话框的布局
        editDialog = builder.create();
        //生成最终的对话框
        editDialog.setCanceledOnTouchOutside(cancelable);
        TextView tv_title = layout.findViewById(R.id.tv_title);
        TextView tv_cancel = layout.findViewById(R.id.tv_btn);
        TextView tv_confirm = layout.findViewById(R.id.tv_btn2);
        EditText et_content = layout.findViewById(R.id.et_content);
        switch (position) {
            case 1:
                tv_title.setText(R.string.main_driver);
                break;
            case 2:
                tv_title.setText(R.string.co_pilot);
                break;
            case 3:
                tv_title.setText(R.string.lfd_rfd);
                break;
            case 4:
                tv_title.setText(R.string.lbd_rbd);
                break;
            case 5:
                tv_title.setText(R.string.sn);
                break;
        }
        et_content.setText(String.valueOf(hint));
        tv_cancel.setOnClickListener(v -> editDialog.dismiss());
        tv_confirm.setOnClickListener(v -> {
            if (et_content.getText().toString().length() == 0 || et_content.getText().toString().length() > 3) {
                ToastUtils.showShort(R.string.input_tips);
                return;
            } else {
                int input = Integer.parseInt(et_content.getText().toString());
                if (input > 200 || input < 0) {
                    ToastUtils.showShort(R.string.input_tips);
                    return;
                }
                switch (position) {
                    case 1:
                        BleManager.getInstance().getAppViewModel().getMdLampNum().postValue(input);
                        break;
                    case 2:
                        BleManager.getInstance().getAppViewModel().getCpLampNum().postValue(input);
                        break;
                    case 3:
                        BleManager.getInstance().getAppViewModel().getFdLampNum().postValue(input);
                        break;
                    case 4:
                        BleManager.getInstance().getAppViewModel().getRdLampNum().postValue(input);
                        break;
                    case 5:
                        BleManager.getInstance().getAppViewModel().getLightSn().postValue(input);
                        break;
                }
                if (position != 5)
                    CommandManager.getInstance().flowLightSet(BleManager.getInstance().getBleViewModel().getConnectedMac(), position, input);
                else
                    CommandManager.getInstance().lightSnImport(BleManager.getInstance().getBleViewModel().getConnectedMac(), input);
            }
            editDialog.dismiss();
        });

        final Window commentDialogWindow = editDialog.getWindow();
        //设置居中显示
        if (commentDialogWindow != null) {
            commentDialogWindow.setGravity(Gravity.CENTER);
        }
        editDialog.show();
    }

    public static void showPassword(Context context, Boolean cancelable) {
        if (editDialog != null && editDialog.isShowing()) {
            editDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.myDialog);
        //创建对话框
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.edit_dialog_layout, null);
        //获取自定义布局
        builder.setView(layout);
        //设置对话框的布局
        editDialog = builder.create();
        //生成最终的对话框
        editDialog.setCanceledOnTouchOutside(cancelable);
        TextView tv_title = layout.findViewById(R.id.tv_title);
        TextView tv_cancel = layout.findViewById(R.id.tv_btn);
        TextView tv_confirm = layout.findViewById(R.id.tv_btn2);
        EditText et_content = layout.findViewById(R.id.et_content);
        tv_title.setText(R.string.pwd_title);
        tv_cancel.setOnClickListener(v -> editDialog.dismiss());
        tv_confirm.setOnClickListener(v -> {
            if (et_content.getText().toString().length() == 0) {
                ToastUtils.showShort(R.string.pwd_error_tips);
                return;
            } else {
                int input = Integer.parseInt(et_content.getText().toString());
                if (input != 1689) {
                    ToastUtils.showShort(R.string.pwd_error_tips);
                    return;
                } else {
                    MyApp.getInstance().setPassword(true);
                    Intent intent = new Intent(context, FactorySettingsActivity.class);
                    context.startActivity(intent);
                }
            }
            editDialog.dismiss();
        });

        final Window commentDialogWindow = editDialog.getWindow();
        //设置居中显示
        if (commentDialogWindow != null) {
            commentDialogWindow.setGravity(Gravity.CENTER);
        }
        editDialog.show();
    }

    public static AlertDialog getDialog() {
        return editDialog;
    }

    public static void dismiss() {
        if (editDialog != null && editDialog.isShowing()) {
            editDialog.dismiss();
        }
    }
}
