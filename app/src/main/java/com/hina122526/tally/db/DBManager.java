package com.hina122526.tally.db;

//負責管理資料庫的類別，可以操作表的內容(新增、刪除、修改、查詢)

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static SQLiteDatabase db;

    //初始化資料庫對象
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context); //得到Helper類別
        db = helper.getWritableDatabase(); //得到資料庫對象
    }

    //讀取資料庫的資料並寫入內部儲存空間的集合中
    //kind:表示收入或支出

    public static List<TypeBean>getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();

        //讀取typetb當中的資料
        String sql = "select * from typetb where kind =" + kind;
        Cursor cursor = db.rawQuery(sql,null);

        //循環讀取游標內容，儲存到對對象當中
        while (cursor.moveToNext()){
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int sImageid = cursor.getInt(cursor.getColumnIndexOrThrow("sImageid"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

            TypeBean typeBean = new TypeBean(id,typename,imageId,sImageid,kind);
            list.add(typeBean);
        }
        return list;
    }
}

