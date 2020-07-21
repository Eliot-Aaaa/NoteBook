package com.eliot.notebook.memo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eliot.notebook.R;
import com.eliot.notebook.common.database.DBManager;
import com.eliot.notebook.memo.model.Memo;

import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo
 * @ClassName: MemoContentActivity
 * @Description: 便签内容编辑界面
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 14:19
 */
public class MemoContentActivity extends AppCompatActivity
{
    EditText content_edit;
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_memo_content);
        content_edit = findViewById(R.id.memo_context_edit);
        String content = getIntent().getStringExtra("content");
        content_edit.setText(content);

        btn = findViewById(R.id.btn_save_memo);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DBManager dbManager = new DBManager(getApplicationContext());
                String time = "20200721";
                String content = content_edit.getText().toString();
                if (content==null)
                    return;
                dbManager.insert("memo", new Memo(time, content));
                List<Memo> memoList = dbManager.query();
            }
        });
    }
}
