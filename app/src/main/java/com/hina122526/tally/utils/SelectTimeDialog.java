package com.hina122526.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hina122526.tally.R;

import java.nio.Buffer;

//在記錄頁面彈出對話框
public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    EditText hourEt,minuateEt;
    DatePicker datePicker;
    Button ensureBtn,cancelbtn;

    public interface OnEnsureListener {
        public void onEnsure(String time, int year, int month, int day);
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);

        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuateEt = findViewById(R.id.dialog_time_et_minute);
        datePicker = findViewById(R.id.dialog_time_dp);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelbtn = findViewById(R.id.dialog_time_btn_cancel);

        ensureBtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);

        hideDatePickerHeader();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_time_btn_cancel:
                cancel();
                break;
            case R.id.dialog_time_btn_ensure:
                int year = datePicker.getYear(); //年份選擇
                int month = datePicker.getMonth()+1; //月份預設從0開始，0-11
                int dayOfMonth = datePicker.getDayOfMonth();

                String monthStr = String.valueOf(month);
                if (month<10){
                    monthStr = "0"+month;
                }

                String dayStr = String.valueOf(dayOfMonth);
                if (dayOfMonth<10){
                    dayStr = "0"+dayOfMonth;
                }

                //取得按下確定後所選的時間(Hr、Min)資料
                String hourStr = hourEt.getText().toString();
                String minuateStr = minuateEt.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty((hourStr))) {
                    hour = Integer.parseInt(hourStr);
                    hour = hour%24;
                }
                int minuate = 0;
                if (!TextUtils.isEmpty(minuateStr)) {
                    minuate = Integer.parseInt(minuateStr);
                    minuate = minuate%60;
                }

                hourStr = String.valueOf(hour);
                minuateStr = String.valueOf(minuate);
                if (hour<10){
                    hourStr = "0"+hour;
                }
                if (minuate<10){
                    minuateStr = "0"+minuate;
                }

                String timeFormat = year+"年"+monthStr+"月"+dayStr+"日 "+hourStr+":"+minuateStr;
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(timeFormat,year,month,dayOfMonth);
                }

                cancel();
                break;
        }
    }
    //隱藏DatePicker
    private void hideDatePickerHeader(){
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }

        //5.0+
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout","id","android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            child.setLayoutParams(layoutParamsChild);
            return;
        }
        //6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header","id","android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }
}
