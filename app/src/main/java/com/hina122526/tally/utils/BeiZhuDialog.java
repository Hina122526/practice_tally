package com.hina122526.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hina122526.tally.R;

public class BeiZhuDialog extends Dialog implements View.OnClickListener {
    EditText et;
    Button cancelBtn,ensureBtn;
    OnEnsureListener onEnsureListener;

    //設定回調接口的方法(?)
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BeiZhuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialo_beizhu); //設定彈出的對話框要顯示的layout

        et = findViewById(R.id.dialog_beizhu_et);
        cancelBtn = findViewById(R.id.dialog_beizhu_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_beizhu_btn_ensure);

        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);

    }

    public interface OnEnsureListener{
        public void onEnsure();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_beizhu_btn_cancel:
                cancel();
                break;
            case R.id.dialog_beizhu_btn_ensure:
                if (onEnsureListener!=null){
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    //取得輸入資料的方法
    public String getEditText(){
        return et.getText().toString().trim();
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
