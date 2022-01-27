package com.hina122526.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.hina122526.tally.adapter.RecordPagerAdapter;
import com.hina122526.tally.frag_record.IncomeFragment;
import com.hina122526.tally.frag_record.OutcomeFragment;


import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        //設定ViewPager頁面
        initPager();
    }

    private void initPager(){
        //初始化ViewPager
        List<Fragment>fragmentList = new ArrayList<>();

        //收入支出頁面，放在Fragment中
        OutcomeFragment outFrag = new OutcomeFragment();//支出
        IncomeFragment inFrag = new IncomeFragment(); //收入

        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        //Create Adapter
        RecordPagerAdapter pagerAdapter =  new RecordPagerAdapter(getSupportFragmentManager(),fragmentList);
        //Set Adapter
        viewPager.setAdapter(pagerAdapter);
        //TabLayout與ViewPager連結
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}