package com.lenzetech.zhongyablack.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lenzetech.zhongyablack.R;

public class TipsDialog {
    private static AlertDialog tipsDialog;

    public static void show(Context context, Boolean cancelable, int type) {
        if (tipsDialog != null && tipsDialog.isShowing()) {
            tipsDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.myDialog);
        //创建对话框
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.tips_dialog_layout, null);
        //获取自定义布局
        builder.setView(layout);
        //设置对话框的布局
        tipsDialog = builder.create();
        //生成最终的对话框
        tipsDialog.setCanceledOnTouchOutside(cancelable);
        TextView tv_title = layout.findViewById(R.id.tv_title);
        TextView tv_cancel = layout.findViewById(R.id.tv_btn);
        TextView tv_confirm = layout.findViewById(R.id.tv_btn2);
        TextView tv_content = layout.findViewById(R.id.tv_content);
        switch (type) {
            case 1:
                tv_content.setText(R.string.no_support_tips);
                break;
            case 2:
                tv_content.setText(R.string.success);
                break;
            case 3:
                tv_content.setText(R.string.failed);
                break;

        }
        tv_cancel.setOnClickListener(v -> tipsDialog.dismiss());
        tv_confirm.setOnClickListener(v -> {
            tipsDialog.dismiss();
        });

        final Window commentDialogWindow = tipsDialog.getWindow();
        //设置居中显示
        if (commentDialogWindow != null) {
            commentDialogWindow.setGravity(Gravity.CENTER);
        }
        tipsDialog.show();
    }

    public static AlertDialog getDialog() {
        return tipsDialog;
    }

    public static void dismiss() {
        if (tipsDialog != null && tipsDialog.isShowing()) {
            tipsDialog.dismiss();
        }
    }
}
