package com.eliot.notebook.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
    List<Memo> listMemo;                //备忘录数据列表
    ImageButton imageButtonAddMemo;           //新建备忘录按钮
    MemoDBManager mMemoDBManager;           //数据库操作对象
    RecyclerView recyclerViewMemo;             //备忘录列表RecyclerView
    TextView textViewNoMemoHint;              //没有备忘录记录时的提示文本

    MemoItemAdapter mMemoItemAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root =inflater.inflate(R.layout.fragment_memo, container, false);

        //添加便签概略列表
        recyclerViewMemo = root.findViewById(R.id.memo_list);
        textViewNoMemoHint = root.findViewById(R.id.memo_text_view);
        mMemoDBManager = new MemoDBManager(getContext());
        mMemoDBManager.createTable();

        //初始化新增备忘录按钮，并且设置点击事件：点击后跳转到编辑内容Activity
        imageButtonAddMemo = root.findViewById(R.id.btn_memo_add);
        imageButtonAddMemo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), MemoContentActivity.class);
                startActivity(intent);
            }
        });

        //设置RecyclerView的layoutManager为StaggeredGridLayoutManager（瀑布流）
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewMemo.setLayoutManager(layoutManager);

        //获取数据库对象的指定List对象
        listMemo = mMemoDBManager.getMemoList();
        mMemoDBManager.query(null, IDBManager.SORT_DESC);               //查询数据库，更新数据库的List
        //初始化列表显示
        if (recyclerViewMemo != null && listMemo.size() > 0)
        {
            mMemoItemAdapter = new MemoItemAdapter(getContext(), listMemo);
            mMemoItemAdapter.setOnClickListener(clickListener);
            recyclerViewMemo.setAdapter(mMemoItemAdapter);
        }

        //为List的Item添加动画效果
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(300);
        defaultItemAnimator.setRemoveDuration(300);
        recyclerViewMemo.setItemAnimator(defaultItemAnimator);

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //在onResume调用更新列表函数
        updateList();
    }

    //添加item的点击事件（单击、长按）
    MemoItemAdapter.ItemClickListener clickListener = new MemoItemAdapter.ItemClickListener() {

        @Override
        public void onSingleClick(MemoItemAdapter parent, int position) {
            //点击后直接跳转到内容编辑界面，并且将对应项的对象传递到下一个Activity
            Intent intent = new Intent(getContext(), MemoContentActivity.class);
            intent.putExtra("memo", parent.getMemoList().get(position));
            startActivity(intent);
        }

        @Override
        public void onLongClick(MemoItemAdapter parent, int position) {
            //获取点击的列表项对应的对象，并将其从数据库删除
            Memo memo = parent.getMemoList().get(position);
            mMemoDBManager.delete(memo);
            listMemo.remove(position);

            //调用notifyItemRemoved才能将删除的动画效果显示出来
            mMemoItemAdapter.notifyItemRemoved(position);
            mMemoItemAdapter.notifyItemRangeChanged(position, listMemo.size() - position);                  //需要添加这句，不然item位置错乱，会报数组边界溢出的错误
            if (listMemo.size() <= 0)
            {
                recyclerViewMemo.setVisibility(View.GONE);
                textViewNoMemoHint.setVisibility(View.VISIBLE);
            }
        }
    };

    //更新备忘录数据列表
    public void updateList()
    {
        //调用数据库查询功能更新List
        mMemoDBManager.query(null, IDBManager.SORT_DESC);
        if (listMemo != null && listMemo.size() > 0)
        {
            mMemoItemAdapter.notifyDataSetChanged();                //使用notifyDataSetChanged更新列表，而不是重新setAdapter
            recyclerViewMemo.setVisibility(View.VISIBLE);
            textViewNoMemoHint.setVisibility(View.GONE);
        }else
        {
            //无数据时显示提示文本
            recyclerViewMemo.setVisibility(View.GONE);
            textViewNoMemoHint.setVisibility(View.VISIBLE);
        }
    }

}