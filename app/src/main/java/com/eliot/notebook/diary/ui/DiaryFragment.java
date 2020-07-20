package com.eliot.notebook.diary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eliot.notebook.R;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.diary.ui
 * @ClassName: DiaryFragment
 * @Description: 日记部分UI的Fragment
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/20 14:52
 */
public class DiaryFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_diary, container, false);
        return root;
    }
}
