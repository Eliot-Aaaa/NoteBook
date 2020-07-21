package com.eliot.notebook.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.common.database
 * @ClassName: DBHelper
 * @Description: 类描述
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 16:01
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
