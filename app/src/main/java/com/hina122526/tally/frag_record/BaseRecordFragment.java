package com.hina122526.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hina122526.tally.R;
import com.hina122526.tally.db.AccountBean;
import com.hina122526.tally.db.DBManager;
import com.hina122526.tally.db.TypeBean;
import com.hina122526.tally.utils.BeiZhuDialog;
import com.hina122526.tally.utils.KeyBoardUtils;
import com.hina122526.tally.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//ADD頁面支出區塊

public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIV;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    public TypeBaseAdapter adapter;
    AccountBean accountBean; //將需要插入到記帳本中的資料保存成一個對象

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean(); //建立對象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        //將時間功能初始化
        setInitTime();
        //在GridView添加資料或功能的方法
        loadDataToGV();
        setGVListener(); //設定GridView的每一個選項點擊事件
        return view;
    }

    //取得當前時間的方法，並將時間寫入timeTv裡
    public void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年mm月dd日 HH:MM");
        String time = sdf.format(date);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance(); //得到當前日曆內容對象
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH)+1;
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    //GridView的點擊事件 -- 點擊選項、上方展示發生改變
    private void setGVListener(){
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetChanged(); //提示繪製發生改變
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);

                accountBean.setTypename(typename);

                int simageId = typeBean.getSimgeid();
                typeIV.setImageResource(simageId); //點擊圖片時，上方圖片樣式跟著改變

                accountBean.setsImageId(simageId);
            }
        });
    }


    //給GridView添加資料的方法
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    private void initView(View view){
        keyboardView = view.findViewById((R.id.frag_record_keyboard));
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIV = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typeGv = view.findViewById(R.id.frag_record_gv);

        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        //顯示自定義鍵盤
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView,moneyEt);
        boardUtils.showKeyboard();

        //設定街口，監聽按鈕被點擊
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //取得輸入的金額
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")){
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //取得紀錄資料，儲存在資料庫
                saveAccountToDB();
                //回到上一頁
                getActivity().finish();
            }
        });
    }

    //讓子類一定要重寫此抽象方法
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frag_record_tv_time:
                showtTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }

    //彈出時間的對話框
    private void showtTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();

        //設定確定按鈕被點擊的Listener
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    };

    //彈出備註對話框
    public void showBZDialog(){
        final BeiZhuDialog dialog =new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if(!TextUtils.isEmpty(msg)){
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }
}