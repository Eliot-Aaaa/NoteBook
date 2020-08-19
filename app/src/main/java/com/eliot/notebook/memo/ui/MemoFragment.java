package com.eliot.notebook.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eliot.notebook.R;
import com.eliot.notebook.common.Constants;
import com.eliot.notebook.common.database.IDBManager;
import com.eliot.notebook.memo.MemoContentActivity;
import com.eliot.notebook.memo.database.MemoDBManager;
import com.eliot.notebook.memo.model.Memo;

import java.util.Collections;
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

    boolean isAddNewMemo = false;               //根据上一个Activity返回码和请求码判定是否是新增备忘录后返回
    boolean isModifyMemo = false;               //根据上一个Activity返回码和请求码判定是否是修改备忘录后返回

    List<Integer> listSelected;                 //长按后选择的item项的位置信息集合
    ImageButton imageButtonSelectAll, imageButtonDelete;       //多选时的全选按钮和删除按钮
    LinearLayout layoutSelectMenu;            //包含全选按钮和删除按钮的布局

    static final int MSG_FLASH_ALL_LIST = 1;    //用于Handler的Message，表示刷新list全部item

    static final int ANIM_TIME_REMOVE_ITEM = 200;       //列表项移除的动画时间
    static final int ANIM_TIME_ADD_NEW_ITEM = 300;      //新增列表项的动画时间

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root =inflater.inflate(R.layout.fragment_memo, container, false);

        //添加便签概略列表
        recyclerViewMemo = root.findViewById(R.id.recycler_view_memo_list);
        textViewNoMemoHint = root.findViewById(R.id.text_view_memo_no_memo_hint);
        mMemoDBManager = new MemoDBManager(getContext());
        mMemoDBManager.createTable();

        //初始化新增备忘录按钮，并且设置点击事件：点击后跳转到编辑内容Activity
        imageButtonAddMemo = root.findViewById(R.id.image_button_memo_add_new);
        imageButtonAddMemo.setOnClickListener(buttonOnClickListener);

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
            mMemoItemAdapter.setOnClickListener(itemClickListener);
            recyclerViewMemo.setAdapter(mMemoItemAdapter);
        }

        //为List的Item添加动画效果
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(ANIM_TIME_ADD_NEW_ITEM);
        defaultItemAnimator.setRemoveDuration(ANIM_TIME_REMOVE_ITEM);
        recyclerViewMemo.setItemAnimator(defaultItemAnimator);

        layoutSelectMenu = root.findViewById(R.id.layout_memo_select_menu);
        layoutSelectMenu.setVisibility(View.GONE);
        imageButtonSelectAll = root.findViewById(R.id.image_button_select_menu_select_all);
        imageButtonSelectAll.setOnClickListener(buttonOnClickListener);

        imageButtonDelete = root.findViewById(R.id.image_button_select_menu_delete);
        imageButtonDelete.setOnClickListener(buttonOnClickListener);

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //在onResume调用更新列表函数
        updateList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_CODE_ADD_NEW_CONTENT && (resultCode == Constants.RESULT_CODE_ADD_NEW_CONTENT || resultCode == Constants.RESULT_CODE_MODIFY_CONTENT))
        {
            isAddNewMemo = true;
        }else if (requestCode == Constants.REQUEST_CODE_MODIFY_CONTENT && resultCode == Constants.RESULT_CODE_MODIFY_CONTENT)
        {
            isModifyMemo = true;
        }
    }

    //添加按钮的点击事件
    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                //新增备忘录按钮，点击后进入内容编辑界面
                case R.id.image_button_memo_add_new:
                    Intent intent = new Intent(getContext(), MemoContentActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_CODE_ADD_NEW_CONTENT);
                    break;
                //出现选择列表功能时，单击选择全部选中列表项
                case R.id.image_button_select_menu_select_all:
                    listSelected.clear();
                    for (int i = 0; i < listMemo.size(); i++)
                    {
                        listSelected.add(i);
                    }
                    //将list赋值后需要通知adapter刷新列表
                    mMemoItemAdapter.notifyDataSetChanged();
                    imageButtonSelectAll.setBackgroundResource(R.drawable.ic_selected_all);
                    break;
                //显示选择列表时的删除按钮，点击将选中的项删除
                case R.id.image_button_select_menu_delete:
                    //给list排序，避免出现总数改变之后，删除到位置大的导致数组越界
                    Collections.sort(listSelected);
                    int size = listSelected.size();
                    for (int i = size - 1; i >= 0; i--)
                    {
                        int position = listSelected.get(i);
                        Memo memo = listMemo.get(position);
                        mMemoDBManager.delete(memo);
                        listMemo.remove(position);

                        //调用notifyItemRemoved才能将删除的动画效果显示出来
                        mMemoItemAdapter.notifyItemRemoved(position);
                        mMemoItemAdapter.notifyItemRangeChanged(position, listMemo.size() - position);                  //需要添加这句，不然item位置错乱，会报数组边界溢出的错误

                        listSelected.remove(i);
                    }
                    //通过handler将消息发送出去刷新列表，为避免与删除列表项的动画出现重叠，延后300ms进行
                    Message message = mHandler.obtainMessage();
                    message.what = MSG_FLASH_ALL_LIST;
                    mHandler.sendMessageDelayed(message, ANIM_TIME_REMOVE_ITEM + 300);

                    if (listMemo.size() == 0)
                    {
                        recyclerViewMemo.setVisibility(View.GONE);
                        textViewNoMemoHint.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //添加item的点击事件（单击、长按）
    MemoItemAdapter.ItemClickListener itemClickListener = new MemoItemAdapter.ItemClickListener() {

        @Override
        public void onSingleClick(MemoItemAdapter parent, int position) {
            //假设不是处于列表选择状态，点击后直接跳转到内容编辑界面，并且将对应项的对象传递到下一个Activity
            if (!mMemoItemAdapter.getSelecting())
            {
                Intent intent = new Intent(getContext(), MemoContentActivity.class);
                intent.putExtra("memo", parent.getMemoList().get(position));
                startActivityForResult(intent, Constants.REQUEST_CODE_MODIFY_CONTENT);
            }
            //如果是处于列表选择状态，单击是选中或者取消选中列表
            else
            {
                //加入从已选list中查找到当前位置是已选，则从list中删除该项，假设非已选，则新增至list中
                if (listSelected.contains(position))
                {
                    listSelected.remove(listSelected.indexOf(position));
                }
                else {
                    listSelected.add(position);
                }

                //如果未选中任何列表项，则退出选择状态；如果有任意选中，则刷新对应的列表项显示
                if (listSelected.size() == 0)
                {
                    Message message = mHandler.obtainMessage();
                    message.what = MSG_FLASH_ALL_LIST;
                    mHandler.sendMessage(message);
                }
                else if (listSelected.size() == listMemo.size())
                {
                    imageButtonSelectAll.setBackgroundResource(R.drawable.ic_selected_all);
                    mMemoItemAdapter.notifyItemChanged(position);
                }
                else
                {
                    imageButtonSelectAll.setBackgroundResource(R.drawable.ic_select_all);
                    mMemoItemAdapter.notifyItemChanged(position);
                }
            }
        }

        @Override
        public void onLongClick(MemoItemAdapter parent, int position) {
            //长按显示选择列表
            if (!mMemoItemAdapter.getSelecting())
            {
                mMemoItemAdapter.setSelecting(true);
                listSelected = mMemoItemAdapter.getListSelected();
                listSelected.clear();
                listSelected.add(position);
                mMemoItemAdapter.notifyDataSetChanged();
                layoutSelectMenu.setVisibility(View.VISIBLE);
                imageButtonAddMemo.setVisibility(View.GONE);
                if (listSelected.size() == listMemo.size())
                    imageButtonSelectAll.setBackgroundResource(R.drawable.ic_selected_all);
            }
        }
    };

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what)
            {
                //刷新所有列表项
                case MSG_FLASH_ALL_LIST:
                    mMemoItemAdapter.setSelecting(false);
                    mMemoItemAdapter.notifyDataSetChanged();
                    layoutSelectMenu.setVisibility(View.GONE);
                    imageButtonAddMemo.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    //更新备忘录数据列表
    public void updateList()
    {
        //调用数据库查询功能更新List
        mMemoDBManager.query(null, IDBManager.SORT_DESC);
        if (isAddNewMemo)
        {
            if (mMemoItemAdapter == null)
            {
                mMemoItemAdapter = new MemoItemAdapter(getContext(), listMemo);
                mMemoItemAdapter.setOnClickListener(itemClickListener);
                recyclerViewMemo.setAdapter(mMemoItemAdapter);
            }else {
                mMemoItemAdapter.notifyItemInserted(0);
                recyclerViewMemo.scrollToPosition(0);
                mMemoItemAdapter.notifyItemRangeChanged(0, listMemo.size());            //更新position，避免出现position错位的问题
            }
            isAddNewMemo = false;
            recyclerViewMemo.setVisibility(View.VISIBLE);
            textViewNoMemoHint.setVisibility(View.GONE);
        }
        else if (isModifyMemo)
        {
            mMemoItemAdapter.notifyDataSetChanged();
            isModifyMemo = false;
        }
        else {
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

}