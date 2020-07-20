package com.eliot.notebook.memo.ui;

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
 * @Package: com.eliot.notebook.memo.ui
 * @ClassName: MemoFragment
 * @Description: 备忘录部分UI的Fragment
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/20 14:37
 */
public class MemoFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root =inflater.inflate(R.layout.fragment_memo, container, false);
        return root;
    }
}
