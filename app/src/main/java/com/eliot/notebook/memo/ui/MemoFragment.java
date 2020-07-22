package com.eliot.notebook.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eliot.notebook.R;
import com.eliot.notebook.common.database.IDBManager;
import com.eliot.notebook.memo.MemoContentActivity;
import com.eliot.notebook.memo.database.MemoDBManager;
import com.eliot.notebook.memo.model.Memo;

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
    List<Memo> memoList;                //备忘录数据列表
    ImageButton btn_memo_add;           //新建备忘录按钮
    IDBManager mDBManager;              //数据库操作对象
    ListView memo_list;                 //备忘录列表ListView
    TextView memoTextView;              //没有备忘录记录时的提示文本
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root =inflater.inflate(R.layout.fragment_memo, container, false);

        //添加便签概略列表
        memo_list = root.findViewById(R.id.memo_list);
        memoTextView = root.findViewById(R.id.memo_text_view);
        mDBManager = new MemoDBManager(getContext());
        mDBManager.createTable();

        //给listView添加点击事件监听
        memo_list.setOnItemClickListener(mOnItemClickListener);
        //给listView添加长按点击事件监听，长按时删除数据
        memo_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Memo memo = (Memo) parent.getItemAtPosition(position);
                mDBManager.delete(memo);
                updateList();
                return true;
            }
        });

        //初始化新增备忘录按钮，并且设置点击事件：点击后跳转到编辑内容Activity
        btn_memo_add = root.findViewById(R.id.btn_memo_add);
        btn_memo_add.setOnClickListener(new View.OnClickListener()
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

    @Override
    public void onResume()
    {
        super.onResume();
        updateList();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mDBManager != null)
            mDBManager.close();
    }

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //点击后直接跳转到内容编辑界面，并且将对应项的对象传递到下一个Activity
            Intent intent = new Intent(getContext(), MemoContentActivity.class);
            intent.putExtra("memo", ((Memo)parent.getItemAtPosition(position)));
            startActivity(intent);
        }
    };

    //更新备忘录数据列表
    public void updateList()
    {
        memoList = mDBManager.query(null, IDBManager.SORT_DESC);
        if (memoList != null && memoList.size() > 0)
        {
            MemoItemAdapter adapter = new MemoItemAdapter(getContext(), R.layout.memo_item, memoList);
            memo_list.setAdapter(adapter);
            memo_list.setVisibility(View.VISIBLE);
            memoTextView.setVisibility(View.GONE);
        }else
        {
            //无数据时显示提示文本
            memo_list.setVisibility(View.GONE);
            memoTextView.setVisibility(View.VISIBLE);
        }
    }

}
