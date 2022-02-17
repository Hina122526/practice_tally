package com.hina122526.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hina122526.tally.adapter.AccountAdapter;
import com.hina122526.tally.db.AccountBean;
import com.hina122526.tally.db.DBManager;
import com.hina122526.tally.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView todayLv; //本日已記錄的收支的各個項目ListView
    Button editbtn;
    //ListView的資料來源
    List<AccountBean> mDatas;
    AccountBean accountBean;
    AccountAdapter adapter;
    int year, month, day;

    //HeaderView的相關元件
    View headerView;
    TextView topOutTv, topInTv, topbugetTv, topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTime();
        initView();

        preferences = getSharedPreferences("buget", MODE_PRIVATE);

        //新增一個ListView的頭(最上面)Layout
        addLVHeaderView();

        mDatas = new ArrayList<>();

        //設定中間的Adapter，載入每一筆資料到列表內容中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    //初始化自帶View的方法
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editbtn = findViewById(R.id.main_btn_edit);
        editbtn = findViewById(R.id.main_btn_edit);

        editbtn.setOnClickListener(this);
        editbtn.setOnClickListener(this);
    }

    //ListView的頭部局的方法
    private void addLVHeaderView() {
        //Layout轉成一個View
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        //將轉換後的View插入todayLv
        todayLv.addHeaderView(headerView);

        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topbugetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbugetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    //取得現實中當下具體時間
    public void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Log.d("DATE", String.valueOf(year) + String.valueOf(month) + String.valueOf(day));
    }

    //當Activity開始運行時會載入輸入的資料
    @Override
    protected void onResume() {
        super.onResume();
        this.loadDBData();
        setTopTvShow();
    }

    //設定item_mainlv_top當中TextView內容的顯示
    private void setTopTvShow() {
        //從資料庫取得今日收入與支出總金額
        float incomeoneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "本日  支出 $" + outcomeOneDay + "  " + "收入 $" + incomeoneDay;
        topConTv.setText(infoOneDay);

        //從資料庫取得本月收入與支出總金額
        float incomeoneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("$" + incomeoneMonth);
        topOutTv.setText("$" + outcomeOneMonth);

        //設定預算剩餘默認顯示方式
        float bmoney = preferences.getFloat("bmoney", 0);//預算
        if (bmoney==0) {
            topbugetTv.setText("點選輸入預算金額");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            topbugetTv.setText("$ "+syMoney);
        }

    }

    public void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear(); //清除原來留下的資料，否則資料可能會一直重複疊加上去
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);
                startActivity(it1);
                break;

            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;

            case R.id.item_mainlv_top_iv_hide:
                //切換Text顯示或暗碼隱藏顯示
                toggleShow();
                break;
        }

        //代表headerView被點擊時
        if (view == headerView) {
        }
    }

    //跳出預算對話框的方法
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //將預算金額寫入共享參數
                SharedPreferences.Editor editor = preferences.edit();
                editor.commit();

                //計算剩餘的預算金額
                float outcomeMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);

                float syMoney = money - outcomeMoneyOneMonth; //剩餘預算
                topbugetTv.setText("$ " + syMoney);
            }
        });
    }

    Boolean isShow = true;

    //點選眼睛切換顯示或隱藏金額的方法
    private void toggleShow() {
        if (isShow) { //如果金額顯示、點選就會隱藏
            PasswordTransformationMethod transHideMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(transHideMethod);
            topOutTv.setTransformationMethod(transHideMethod);
            topbugetTv.setTransformationMethod(transHideMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        } else { //如果金額隱藏、點選就會金額顯示
            HideReturnsTransformationMethod transShowMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(transShowMethod);
            topOutTv.setTransformationMethod(transShowMethod);
            topbugetTv.setTransformationMethod(transShowMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }
}