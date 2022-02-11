package com.hina122526.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.hina122526.tally.adapter.AccountAdapter;
import com.hina122526.tally.db.AccountBean;
import com.hina122526.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView todayLv; //本日已記錄的收支的各個項目ListView
    //ListView的資料來源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTime();

        todayLv = findViewById(R.id.main_lv);
        mDatas = new ArrayList<>();

        //設定中間的Adapter，載入每一筆資料到列表內容中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }
    //取得現實中今日的具體時間
    public void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH)+1;
        Log.d("DATE", String.valueOf(year)+String.valueOf(month)+String.valueOf(day));
    }

    //當Activity開始運行時會載入輸入的資料
    @Override
    protected void onResume() {
        super.onResume();
        this.loadDBData();
    }

    public void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear(); //清除原來留下的資料，否則資料可能會一直重複疊加上去
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_lv_search:
                break;

            case R.id.main_btn_edit:
                Intent it1 = new Intent(this,RecordActivity.class);
                startActivity(it1);
                break;

            case R.id.main_btn_more:
                break;

        }
    }
}