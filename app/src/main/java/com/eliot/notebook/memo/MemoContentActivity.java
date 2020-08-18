package com.eliot.notebook.memo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.eliot.notebook.memo.database.ContentCache;
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

    //文本的原始内容和修改内容，用于判定是否可用保存按钮
    String originalContent;
    String modifyContent;
    boolean showingSaveButton;                         //是否正在显示保存按钮可用，true为正在显示可用，false为显示不可用

    ContentCache cache;                                 //内容缓冲类对象

    boolean showingUndoButton, showingRedoButton;       //是否正在显示撤销和重做的按钮可用，true为正在显示可用，false为显示不可用

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
            originalContent = memo.getContent();
            modifyContent = originalContent;
        }
        else
        {
            //如果不能获取到对象，说明是作为新增传入的
            content_edit.setText("");
            action = Constants.MEMO_ACTION_NEW;
            originalContent = "";
            modifyContent = originalContent;
        }
        showingSaveButton = false;                                  //初始默认不可点击保存按钮
        showingUndoButton = false;                                  //初始默认不可点击撤销按钮
        showingRedoButton = false;                                  //初始默认不可点击重做按钮

        //为EditText控件添加文本监视器，当文本发生变化时回调
        content_edit.addTextChangedListener(contentTextWatcher);

        //设置Toolbar
        memo_content_tool_bar = findViewById(R.id.memo_content_tool_bar);
        memo_content_tool_bar.setTitle("");
        setSupportActionBar(memo_content_tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //设置显示系统自带返回键
        getSupportActionBar().setHomeButtonEnabled(true);           //设置自带返回键可用

        //内容缓冲初始化
        cache = ContentCache.getInstance();                         //获取对象实例
        cache.registerEditText(content_edit);                       //将编辑框注册
        cache.initCache(originalContent);                           //缓冲区数据初始化

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
                cache.undo();
                break;
            //重做下一步修改按钮
            case R.id.memo_content_redo:
                cache.redo();
                break;
            //保存按钮
            case R.id.memo_content_save:
                //如果是新增备忘录动作，则调用新增备忘录函数
                if (action == Constants.MEMO_ACTION_NEW)
                {
                    saveContent();
                    setResult(Constants.RESULT_CODE_ADD_NEW_CONTENT);
                }

                //如果是修改备忘录动作，则调用修改备忘录函数
                else if (action == Constants.MEMO_ACTION_UPDATE) {
                    updateContent(memo);
                    setResult(Constants.RESULT_CODE_MODIFY_CONTENT);
                }
                break;
            default:
                break;
        }
        return true;
    }

    //选项菜单的准备函数，使用invalidateOptionsMenu()调用此函数
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //设置保存按钮的显示状态：可用或者不可用
        menu.findItem(R.id.memo_content_save).setEnabled(showingSaveButton);
        //设置撤销按钮的显示状态：可用或者不可用
        menu.findItem(R.id.memo_content_undo).setEnabled(showingUndoButton);
        //设置重做按钮的显示状态：可用或者不可用
        menu.findItem(R.id.memo_content_redo).setEnabled(showingRedoButton);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        //捕获返回键事件，返回时退出当前界面
        finish();
    }

    //文本监视器
    TextWatcher contentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cache.add(s.toString());                                    //将修改后的内容添加进缓冲
        }

        @Override
        public void afterTextChanged(Editable s) {
            //将修改内容填入modifyContent，并且更新选项菜单
            modifyContent = s.toString();
            //当原始内容和修改内容相等时，说明没有进行修改，不可点击保存按钮；当修改内容为空，也就是编辑框内没有内容时，禁止使用保存按钮
            boolean canShowSave = ! (originalContent.equals(modifyContent) || modifyContent.equals(""));
            //当前保存按钮显示状态与检测到是否可显示冲突时，更新UI
            if (showingSaveButton != canShowSave)
            {
                showingSaveButton = canShowSave;
                invalidateOptionsMenu();
            }

            //当前撤销按钮显示状态与检测到是否可显示冲突时，更新UI
            if (showingUndoButton != cache.canUndo)
            {
                showingUndoButton = cache.canUndo;
                invalidateOptionsMenu();
            }
            //当前重做按钮显示状态与检测到是否可显示冲突时，更新UI
            if (showingRedoButton != cache.canRedo)
            {
                showingRedoButton = cache.canRedo;
                invalidateOptionsMenu();
            }
        }
    };

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
            //新增文本内容后originalContent改变，并且重新刷新选项菜单
            originalContent = content;
            showingSaveButton = false;
            invalidateOptionsMenu();
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
            //修改文本内容后originalContent改变，并且重新刷新选项菜单
            originalContent = content;
            showingSaveButton = false;
            invalidateOptionsMenu();
        }
    }
}
