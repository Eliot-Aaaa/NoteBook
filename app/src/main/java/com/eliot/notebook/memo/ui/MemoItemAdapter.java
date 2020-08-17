package com.eliot.notebook.memo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eliot.notebook.R;
import com.eliot.notebook.common.Utils;
import com.eliot.notebook.memo.model.Memo;

import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo.ui
 * @ClassName: MemoItemAdapter
 * @Description: 备忘录概述列表RecyclerView自定义adapter
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 11:29
 */
public class MemoItemAdapter extends RecyclerView.Adapter<MemoItemAdapter.MemoItemViewHolder>
{
    final int CONTENT_SIZE_LIMIT = 28;                              //列表显示文字内容限制的字数

    List<Memo> mListMemo;
    Context mContext;
    ItemClickListener mItemClickListener;

    public MemoItemAdapter(Context context, List<Memo> memoList)
    {
        this.mContext = context;
        this.mListMemo = memoList;
    }

    @NonNull
    @Override
    public MemoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(mContext).inflate(R.layout.memo_item, parent, false);
        return new MemoItemViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemoItemViewHolder holder, final int position) {
        Memo memo = mListMemo.get(position);
        String contentStr = memo.getContent();
        long timeUs = memo.getModifyTime();
        holder.textViewContentText.setText(formatContent(contentStr));
        holder.textViewTimeText.setText(Utils.getCurrentTime(timeUs));

        //将View添加监听事件，并且绑定到自定义接口
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onSingleClick(MemoItemAdapter.this, position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onLongClick(MemoItemAdapter.this, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListMemo.size();
    }

    public List<Memo> getMemoList()
    {
        return mListMemo;
    }

    public class MemoItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewContentText, textViewTimeText;
        public MemoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContentText = itemView.findViewById(R.id.item_memo_content_text);
            textViewTimeText = itemView.findViewById(R.id.item_memo_time_text);
        }
    }

    //定义一个监听器注册函数，将自定义监听接口注册上
    public void setOnClickListener(ItemClickListener itemClickListener)
    {
        this.mItemClickListener = itemClickListener;
    }

    //自定义单击和长按事件接口
    public interface ItemClickListener
    {
        public abstract void onSingleClick(MemoItemAdapter parent, int position);

        public abstract void onLongClick(MemoItemAdapter parent, int position);
    }

    //内容长度限制，根据CONTENT_SIZE_LIMIT限制显示的内容字数
    private String formatContent(@NonNull String src)
    {
        if (src.length() > CONTENT_SIZE_LIMIT)
        {
            //字符串过长，截取部分显示
            String str = src.substring(0, CONTENT_SIZE_LIMIT);
            return str + " ...";
        }
        return src;
    }
}
