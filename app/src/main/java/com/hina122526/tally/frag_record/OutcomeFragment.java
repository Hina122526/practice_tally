package com.hina122526.tally.frag_record;

import androidx.fragment.app.Fragment;

import com.hina122526.tally.R;
import com.hina122526.tally.db.DBManager;
import com.hina122526.tally.db.TypeBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutcomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();

        //獲得資料庫當中的資料來源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();

        typeTv.setText("其他");
        typeIV.setImageResource(R.mipmap.ic_qita_fs);

    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);
    }
}