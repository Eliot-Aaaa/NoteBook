package com.eliot.notebook.settings.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eliot.notebook.R;

import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.settings.ui
 * @ClassName: SettingsItemAdapter
 * @Description: 设置界面列表RecyclerView自定义adapter
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/8/18 16:35
 */
public class SettingsItemAdapter extends RecyclerView.Adapter<SettingsItemAdapter.SettingsItemViewHolder> {

    List<String> listSettingItems;
    Context mContext;
    ItemClickListener mItemClickListener;

    public SettingsItemAdapter(Context context, List<String> list)
    {
        this.listSettingItems = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SettingsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_settings, parent, false);
        return new SettingsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsItemViewHolder holder, final int position) {
        holder.title.setText(listSettingItems.get(position));

        //注册点击事件，绑定到自定义接口
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onSingleClick(SettingsItemAdapter.this, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listSettingItems.size();
    }

    public class SettingsItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        public SettingsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_item_settings_item_title);
        }
    }

    //定义一个监听器注册函数，将自定义监听接口注册上
    public void setOnClickListener(ItemClickListener itemClickListener)
    {
        this.mItemClickListener = itemClickListener;
    }

    //自定义单击事件接口
    public interface ItemClickListener
    {
        public abstract void onSingleClick(SettingsItemAdapter parent, int position);
    }
}
