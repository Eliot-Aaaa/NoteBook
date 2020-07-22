package com.eliot.notebook.memo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.eliot.notebook.R;
import com.eliot.notebook.common.database.IDBManager;
import com.eliot.notebook.memo.database.MemoDBManager;
import com.eliot.notebook.common.Constants;
import com.eliot.notebook.memo.model.Memo;

import java.util.List;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.memo
 * @ClassName: MemoContentActivity
 * @Description: 备忘录内容编辑界面
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/21 14:19
 */
public class MemoContentActivity extends AppCompatActivity
{
    EditText content_edit;                          //内容编辑框
    Toolbar memo_content_tool_bar;                  //界面上方的ToolBar
    IDBManager dbManager;                           //数据库操作对象
    Memo memo;                                      //跳转到编辑界面时传入的对象
    int action;                                     //跳转进入编辑界面时是作为新增还是作为修改的判断

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_memo_content);

        //根据传入的对象初始化编辑框
        content_edit = findViewById(R.id.memo_context_edit);
        //获取传入的对象
        memo = (Memo) getIntent().getSerializableExtra("memo");
        if (memo != null)
        {
            //如果能获取到对象，说明传入不为空，是作为修改传入的
            content_edit.setText(memo.getContent());
            action = Constants.MEMO_ACTION_UPDATE;
        }
        else
        {
            //如果不能获取到对象，说明是作为新增传入的
            content_edit.setText("");
            action = Constants.MEMO_ACTION_NEW;
        }

        //设置Toolbar
        memo_content_tool_bar = findViewById(R.id.memo_content_tool_bar);
        memo_content_tool_bar.setTitle("");
        setSupportActionBar(memo_content_tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //设置显示系统自带返回键
        getSupportActionBar().setHomeButtonEnabled(true);           //设置自带返回键可用

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //初始化选项菜单
        getMenuInflater().inflate(R.menu.memo_content_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            //左上角返回按钮
            case android.R.id.home:
                finish();
                break;
            //撤销上一步修改按钮
            case R.id.memo_content_undo:
                break;
            //重做下一步修改按钮
            case R.id.memo_content_redo:
                break;
            //保存按钮
            case R.id.memo_content_save:
                //如果是新增备忘录动作，则调用新增备忘录函数
                if (action == Constants.MEMO_ACTION_NEW)
                    saveContent();
                //如果是修改备忘录动作，则调用修改备忘录函数
                else if (action == Constants.MEMO_ACTION_UPDATE)
                    updateContent(memo);
                break;
            default:
                break;
        }
        return true;
    }

    private void saveContent()
    {
        if (dbManager == null)
            dbManager = new MemoDBManager(getApplicationContext());
        long currentTime = System.currentTimeMillis();
        String content = content_edit.getText().toString();
        //如果编辑框中内容不为空，则往数据库新增一条数据，并且将动作改为修改备忘录
        if (content != null && !content.isEmpty())
        {
            memo = new Memo(currentTime, content);
            dbManager.insert(memo);
            List<Memo> memos = dbManager.query(memo, IDBManager.SORT_DESC);             //查询是为了将ID赋予Memo对象
            if (memos != null && memos.size() > 0)
                memo = memos.get(0);
            action = Constants.MEMO_ACTION_UPDATE;
            Toast.makeText(getApplicationContext(), "新增完成！", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateContent(Memo memo)
    {
        if (dbManager == null)
            dbManager = new MemoDBManager(getApplicationContext());
        long currentTime = System.currentTimeMillis();
        String content = content_edit.getText().toString();
        //如果编辑框中的内容不为空，且有做修改，则更新数据库
        if (content != null && !content.isEmpty() && !content.equals(memo.getContent()))
        {
            memo.setContent(content);
            memo.setModifyTime(currentTime);
            dbManager.update(memo);
            Toast.makeText(getApplicationContext(), "修改完成！", Toast.LENGTH_SHORT).show();
        }
    }
}
