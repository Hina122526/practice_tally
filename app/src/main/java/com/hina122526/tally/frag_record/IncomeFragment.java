package com.hina122526.tally.frag_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hina122526.tally.R;
import com.hina122526.tally.db.DBManager;
import com.hina122526.tally.db.TypeBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();

        //獲得資料庫當中的資料來源
        List<TypeBean> intlist = DBManager.getTypeList(1);
        typeList.addAll(intlist);
        adapter.notifyDataSetChanged();

        typeTv.setText("其他");
        typeIV.setImageResource(R.mipmap.in_qt_fs);

    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }

}