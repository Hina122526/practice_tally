package com.hina122526.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.hina122526.tally.utils.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

//ADD頁面支出區塊

public class OutcomeFragment extends Fragment {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIV;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    private TypeBaseAdapter adapter;
    AccountBean accountBean; //將需要插入到記帳本中的資料保存成一個對象

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean(); //建立對象
        accountBean.setTypename("其他");
        accountBean.setsImageId();
    }

    public OutcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        //在GridView添加資料或功能的方法
        loadDataToGV();
        setGVListener(); //設定GridView的每一個選項點擊事件
        return view;
    }
    //GridView的點擊事件
    private void setGVListener(){
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetChanged(); //提示繪製發生改變
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);

                int simageId = typeBean.getSimgeid();
                typeIV.setImageResource(simageId); //點擊圖片時，上方圖片樣式跟著改變

            }
        });
    }



    //給GridView添加資料的方法
    private void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
        //獲得資料庫當中的資料來源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();

    }

    private void initView(View view){
        keyboardView = view.findViewById((R.id.frag_record_keyboard));
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIV = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typeGv = view.findViewById(R.id.frag_record_gv);

        //顯示自定義鍵盤
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView,moneyEt);
        boardUtils.showKeyboard();

        //監聽點擊按鈕
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //按下確定
                //取得紀錄資料，儲存在資料庫
                //回到上一頁
            }
        });
    }

}