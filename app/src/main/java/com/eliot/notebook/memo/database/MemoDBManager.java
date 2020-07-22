package com.eliot.notebook.memo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eliot.notebook.common.database.DBHelper;
import com.eliot.notebook.common.database.IDBManager;
import com.eliot.notebook.memo.model.Memo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo.database
 * @ClassName: MemoDBManager
 * @Description: 备忘录数据库操作函数
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/22 9:09
 */
public class MemoDBManager implements IDBManager<Memo>
{
    private static final String CREATE_TABLE_MEMO = "CREATE TABLE IF NOT EXISTS memo(_id INTEGER PRIMARY KEY AUTOINCREMENT,time LONGBLOB, content VARCHAR)";

    private static final String TABLE_NAME = "memo";

    private DBHelper helper;
    private SQLiteDatabase db;

    public MemoDBManager(Context context)
    {
        helper = DBHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void createTable()
    {
        db.execSQL(CREATE_TABLE_MEMO);
    }

    @Override
    public void insert(Memo object)
    {
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(null, ?, ?)", new Object[]{object.getModifyTime(), object.getContent()});
    }

    @Override
    public void delete(Memo object)
    {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE time=\'" + object.getModifyTime() + "\' AND content=\'" + object.getContent() + "\' AND _id=" + object.getId();
        db.execSQL(sql);
    }

    @Override
    public void update(Memo object)
    {
        String sql = "UPDATE " + TABLE_NAME + " SET time=" + object.getModifyTime() + ",content=\'" + object.getContent() + "\' WHERE _id=" + object.getId();
        db.execSQL(sql);
    }

    @Override
    public List<Memo> query(Memo object, int sortType)
    {
        List<Memo> memoList = new ArrayList<>();
        String sql = "SELECT * FROM memo";
        if (object != null)
        {
            sql = "SELECT * FROM memo " + "WHERE time=" + object.getModifyTime() + " AND content=\'" + object.getContent() + "\'";
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext())
            {
                Memo memo = new Memo(object.getModifyTime(), object.getContent());
                int id = c.getInt(c.getColumnIndex("_id"));
                memo.setId(id);
                memoList.add(memo);
            }
            c.close();
            return memoList;
        }

        if (sortType == IDBManager.SORT_DESC)
            sql = "SELECT * FROM memo ORDER BY time DESC";
        Cursor c =db.rawQuery(sql, null);
        while (c.moveToNext())
        {
            Memo memo = new Memo();
            memo.setModifyTime(c.getLong(c.getColumnIndex("time")));
            memo.setContent(c.getString(c.getColumnIndex("content")));
            memo.setId(c.getInt(c.getColumnIndex("_id")));
            memoList.add(memo);
        }
        c.close();
        return memoList;
    }

    @Override
    public void close()
    {
        db.close();
    }
}
