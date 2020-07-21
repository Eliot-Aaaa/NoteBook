package com.eliot.notebook.common.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eliot.notebook.memo.model.Memo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.common.database
 * @ClassName: DBManager
 * @Description: 类描述
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 16:11
 */
public class DBManager
{
    DBHelper helper;
    SQLiteDatabase db;
    DBManager dbManager;
    public DBManager(Context context)
    {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void createTable(String tableName, String param)
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + param + ")";
        db.execSQL(sql);
    }

    public void insert(String tableName, Memo meno)
    {
        db.execSQL("INSERT INTO " + tableName + " VALUES(null, ?, ?)", new Object[]{meno.getModifyTime(), meno.getContent()});
    }

    public List<Memo> query()
    {
        List<Memo> memoList = new ArrayList<>();
        Cursor c =db.rawQuery("SELECT * FROM memo", null);
        c.moveToFirst();
        while (c.moveToNext())
        {
            Memo memo = new Memo();
            memo.setModifyTime(c.getString(c.getColumnIndex("time")));
            memo.setContent(c.getString(c.getColumnIndex("content")));
            memoList.add(memo);
        }
        c.close();
        return memoList;
    }

}
