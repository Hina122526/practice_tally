package com.hina122526.tally.db;

//負責管理資料庫的類別，可以操作表的內容(新增、刪除、修改、查詢)

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hina122526.tally.adapter.AccountAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBManager {
    private static SQLiteDatabase db;

    //初始化資料庫對象
    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context); //得到Helper類別
        db = helper.getWritableDatabase(); //得到資料庫對象
    }

    //讀取資料庫的資料並寫入內部儲存空間的集合中
    //kind:表示收入或支出

    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();

        //讀取typetb當中的資料
        String sql = "select * from typetb where kind =" + kind;
        Cursor cursor = db.rawQuery(sql, null);

        //循環讀取游標內容，儲存到對對象當中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int sImageid = cursor.getInt(cursor.getColumnIndexOrThrow("sImageid"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageid, kind1);
            list.add(typeBean);
        }
        return list;
    }

    //在記帳表當中插入一條元素
    public static void insertItemToAccounttb(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put("typename", bean.getTypename());
        values.put("sImageId", bean.getsImageId());
        values.put("beizhu", bean.getBeizhu());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());

        db.insert("accounttb", null, values); //要插入到哪張表中

    }

    //取得記帳表中某一天的所有收入支出情形
    public static List<AccountBean> getAccountListOneDayFromAccounttb(int year, int month, int day) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndexOrThrow("beizhu"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));

            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;

    }
}

