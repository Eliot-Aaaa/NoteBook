package com.eliot.notebook.memo.model;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo.model
 * @ClassName: Memo
 * @Description: Memo模型
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 15:46
 */
public class Memo
{
    private String modifyTime;
    private String content;

    public Memo(String modifyTime, String content)
    {
        this.modifyTime = modifyTime;
        this.content = content;
    }

    public Memo(){}

    public void setModifyTime(String modifyTime)
    {
        this.modifyTime = modifyTime;
    }

    public String getModifyTime()
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
}
