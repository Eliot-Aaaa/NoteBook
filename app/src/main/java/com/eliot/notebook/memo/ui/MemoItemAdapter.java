package com.eliot.notebook.memo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eliot.notebook.R;
import com.eliot.notebook.common.Utils;
import com.eliot.notebook.memo.model.Memo;

import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo.ui
 * @ClassName: MemoItemAdapter
 * @Description: 备忘录概述列表ListView自定义adapter
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 11:29
 */
public class MemoItemAdapter extends ArrayAdapter
{
    public MemoItemAdapter(@NonNull Context context, int resource, List<Memo> object)
    {
        super(context, resource, object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        String content = ((Memo) getItem(position)).getContent();
        content = formatContent(content);
        long time = ((Memo)getItem(position)).getModifyTime();
        View root = LayoutInflater.from(getContext()).inflate(R.layout.memo_item, null);
        TextView content_text = root.findViewById(R.id.item_memo_content_text);
        content_text.setText(content);
        TextView time_text = root.findViewById(R.id.item_memo_time_text);
        time_text.setText(Utils.getCurrentTime(time));
        return root;
    }

    private String formatContent(String src)
    {
        //搜索回车键
        int firstEnterKeyIndex = src.indexOf("\n");
        if (firstEnterKeyIndex >= 0 && firstEnterKeyIndex < 16)
        {
            //说明存在回车键，截取第一个回车前的部分字符串
            String str = src.substring(0, firstEnterKeyIndex);
            if (firstEnterKeyIndex == 0)
                return str;
            else
                return str + " ...";
        }
        else if (src.length() > 15)
        {
            //字符串过长，截取部分显示
            String str = src.substring(0, 15);
            return str + " ...";
        }
        return src;
    }
}
