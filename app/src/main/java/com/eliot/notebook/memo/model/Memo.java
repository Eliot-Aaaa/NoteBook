package com.eliot.notebook.memo.model;

import java.io.Serializable;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo.model
 * @ClassName: Memo
 * @Description: Memo模型
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 15:46
 */
public class Memo implements Serializable
{
    private long modifyTime;                    //修改时间
    private String content;                     //内容
    private int id;                             //在数据库的id

    public Memo(long modifyTime, String content)
    {
        this.modifyTime = modifyTime;
        this.content = content;
    }

    public Memo(){}

    public void setModifyTime(long modifyTime)
    {
        this.modifyTime = modifyTime;
    }

    public long getModifyTime()
    {
        return modifyTime;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
