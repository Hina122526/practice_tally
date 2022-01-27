package com.hina122526.tally.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hina122526.tally.R;
import com.hina122526.tally.db.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    Context context;
    List<TypeBean> mDatas;

    int selectPos = 0;  //選中的項目

    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //此Adapter不考慮重複使用問題，因為所有的item都顯示在介面，沒有因為滑動而消失
    @Override
    public View getView(int position, View covertView, ViewGroup parent) {
        covertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv,parent,false);

        //尋找佈局中的元件
        ImageView iv = covertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = covertView.findViewById(R.id.item_recordfrag_tv);

        //獲得指定位置的資料來源
        TypeBean typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypename());

        //判斷該選項黨前有無被選中，如果有選中就設定為有顏色的圖片(原本是灰色)
        if (selectPos == position){
            iv.setImageResource(typeBean.getSimgeid());
        }
        else {
            iv.setImageResource(typeBean.getSimgeid());
        }
        return covertView;
    }


}
