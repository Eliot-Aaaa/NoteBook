package com.eliot.notebook.settings.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eliot.notebook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.settings.ui
 * @ClassName: SettingsFragment
 * @Description: 设置界面Fragment
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/8/18 16:30
 */
public class SettingsFragment extends Fragment {

    List<String> listSettingTitle;
    SettingsItemAdapter mSettingsItemAdapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //列表控件对象获取
        recyclerView = root.findViewById(R.id.recycler_view_settings_list);
        //为列表设置布局为线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //添加列表所需要显示的Title List
        if (listSettingTitle == null)
            listSettingTitle = new ArrayList<>();
        listSettingTitle.add("头像");
        listSettingTitle.add("签名");

        //绑定Title List和Adapter
        mSettingsItemAdapter = new SettingsItemAdapter(getContext(), listSettingTitle);
        //绑定列表项的点击接口
        mSettingsItemAdapter.setOnClickListener(itemClickListener);
        recyclerView.setAdapter(mSettingsItemAdapter);
        return root;
    }

    //自定义列表项点击事件处理
    SettingsItemAdapter.ItemClickListener itemClickListener = new SettingsItemAdapter.ItemClickListener() {
        @Override
        public void onSingleClick(SettingsItemAdapter parent, int position) {
        }
    };
}
