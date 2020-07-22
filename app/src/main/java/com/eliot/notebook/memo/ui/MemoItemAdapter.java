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
        String time = ((Memo)getItem(position)).getModifyTime();
        View root = LayoutInflater.from(getContext()).inflate(R.layout.memo_item, null);
        TextView content_text = root.findViewById(R.id.item_memo_content_text);
        content_text.setText(content);
        TextView time_text = root.findViewById(R.id.item_memo_time_text);
        time_text.setText(time);
        return root;
    }
}
