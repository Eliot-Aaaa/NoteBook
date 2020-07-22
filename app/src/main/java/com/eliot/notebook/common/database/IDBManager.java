package com.eliot.notebook.common.database;

import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.common.database
 * @ClassName: IDBManager
 * @Description: 数据库操作接口
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/22 9:03
 */
public interface IDBManager<T>
{
    //查询时排序
    public final int SORT_ASC = 1;              //升序排列
    public final int SORT_DESC = 2;             //降序排列

    //创建表
    public abstract void createTable();

    //添加数据
    public abstract void insert(T object);

    //删除数据
    public abstract void delete(T object);

    //修改数据
    public abstract void update(T object);

    //查询数据
    public abstract List<T> query(T object, int sortType);

    //关闭数据库
    public abstract void close();
}
