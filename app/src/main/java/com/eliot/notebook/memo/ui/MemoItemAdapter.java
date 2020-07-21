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
 * @Description: 类描述
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
        String str = ((Memo) getItem(position)).getContent();
        View root = LayoutInflater.from(getContext()).inflate(R.layout.memo_item, null);
        TextView textView = root.findViewById(R.id.item_memo_text);
        textView.setText(str);
        return root;
    }
}
