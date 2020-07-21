package com.eliot.notebook.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eliot.notebook.R;
import com.eliot.notebook.common.database.DBManager;
import com.eliot.notebook.memo.MemoContentActivity;
import com.eliot.notebook.memo.model.Memo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo.ui
 * @ClassName: MemoFragment
 * @Description: 备忘录部分UI的Fragment
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/20 14:37
 */
public class MemoFragment extends Fragment
{
    List<Memo> memoList;
    ImageButton btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root =inflater.inflate(R.layout.fragment_memo, container, false);

        //添加便签概略列表
        ListView memo_list = root.findViewById(R.id.memo_list);
        initList();
        DBManager manager = new DBManager(getContext());
        manager.createTable("memo", "time VARCHAR, content VARCHAR");

        memoList = manager.query();
        if (memoList != null && memoList.size() > 0)
        {
            MemoItemAdapter adapter = new MemoItemAdapter(getContext(), R.layout.memo_item, memoList);
            memo_list.setAdapter(adapter);
        }
        //给listview添加监听
        memo_list.setOnItemClickListener(mOnItemClickListener);

        btn = root.findViewById(R.id.btn_memo_add);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), MemoContentActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //点击后直接跳转到内容编辑界面
            Intent intent = new Intent(getContext(), MemoContentActivity.class);
            intent.putExtra("content", ((Memo)parent.getItemAtPosition(position)).getContent());
            startActivity(intent);
        }
    };

    private void initList()
    {
        if (memoList == null)
            memoList = new ArrayList<>();
        /*memoList.add(new Memo("20200716", "nothing happen"));
        memoList.add(new Memo("20200720", "nothing happen too"));*/
    }
}
