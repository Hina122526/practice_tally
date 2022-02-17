package com.hina122526.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hina122526.tally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIV;
    Button ensureBtn;
    EditText moneyEt;

    public interface OnEnsureListener {
        public void onEnsure(float money);
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buget);
        cancelIV = findViewById(R.id.dialog_buget_iv_error);
        ensureBtn = findViewById(R.id.dialog_buget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_buget_et);
        cancelIV.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_buget_iv_error:
                cancel();
                break;
            case R.id.dialog_buget_btn_ensure:
                //取得輸入後按下確定的數值
                String data = moneyEt.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(), "請輸入金額", Toast.LENGTH_SHORT).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if (money < 0) {
                    Toast.makeText(getContext(), "金額必須大於0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(money);
                }

                cancel();
                break;
        }
    }
    //將dialog的尺寸與螢幕尺寸一致
    public void setDialogSize(){
        //取得目前視窗的對象(?)
        Window window = getWindow();
        //取得視窗的參數
        WindowManager.LayoutParams wlp = window.getAttributes();
        //取得螢幕的寬度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth()); //對話視窗符合螢幕尺寸大小
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
