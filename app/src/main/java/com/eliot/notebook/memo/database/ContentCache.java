package com.eliot.notebook.memo.database;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo.database
 * @ClassName: ContentCache
 * @Description: 备忘录内容缓存，用于内容修改的撤销和重做
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/28 10:19
 */
public class ContentCache {
    int index;                                      //当前缓存索引
    int count;                                      //缓存容量
    List<String> contentList;                       //缓存内容列表
    static ContentCache cache;                      //缓存对象，用于单例模式
    EditText editText;                              //注册的文本编辑框
    boolean isUndo, isRedo;                         //判断正在进行的状态：isUndo-true为正在执行撤销操作，false则反之；isRedo-true为正在执行重做操作，false则反之
    public boolean canUndo, canRedo;                //判断动作是否可执行：canUndo-true为可执行撤销操作，false为不可执行；canRedo-true为可执行重做操作，false为不可执行

    //单例模式，获取当前ContentCache对象
    public static ContentCache getInstance()
    {
        if (cache == null)
            cache = new ContentCache();
        return cache;
    }

    //初始化缓存区，将初始内容作为参数传入
    public void initCache(String originalContent)
    {
        index = 0;
        contentList = new ArrayList<>();
        contentList.add(index, originalContent);
        count = 0;
    }

    //在缓存区中添加一条内容
    public void add(String content)
    {
        //判断当前状态，如果不是在撤销或重做的操作时调用则添加进缓冲
        if (!isUndo && !isRedo)
        {
            index++;
            contentList.add(index, content);
            count = index;
        }
        //如果是在撤销或者重做的操作时调用，则不予添加，且将状态标志重置为false
        else
        {
            isUndo = false;
            isRedo = false;
        }

        //判断index的状态，如果index在0和count之间，此时可撤销可重做
        if (index > 0 && index < count)
        {
            canUndo = true;
            canRedo = true;
        }
        //如果index为0，说明已经撤销到最初始位置，此时不能再撤销了，但是可以执行重做操作
        else if (index == 0)
        {
            canRedo = true;
            canUndo = false;
        }
        //如果index为count，说明已经重做到缓存最末的位置了，此时不能再重做，但是可以执行撤销操作
        else if (index == count)
        {
            canRedo = false;
            canUndo = true;
        }
    }

    //注册要进行修改的EditText
    public void registerEditText(EditText editText)
    {
        this.editText = editText;
    }

    //撤销修改
    public void undo()
    {
        isUndo = true;
        index = index - 1;

        if (index > 0)
        {
            canRedo = true;
            editText.setText(contentList.get(index));
        }
        else if (index == 0)
        {
            canUndo = false;
            editText.setText(contentList.get(index));
        }
        else
        {
            index = 0;
            canUndo = false;
        }
    }

    //重做修改
    public void redo()
    {
        isRedo = true;
        index = index + 1;

        if (index < count)
        {
            editText.setText(contentList.get(index));
        }
        else if (index == count)
        {
            canRedo = false;
            editText.setText(contentList.get(index));
        }
        else
        {
            index = count;
            canRedo = false;
        }
    }

}
