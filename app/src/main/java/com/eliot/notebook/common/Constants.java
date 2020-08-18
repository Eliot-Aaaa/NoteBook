package com.eliot.notebook.common;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.common
 * @ClassName: Constants
 * @Description: 存储常量的类
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/22 17:19
 */
public class Constants
{
    //备忘录跳转动作：新增备忘录
    public static final int MEMO_ACTION_NEW = 0;

    //备忘录跳转动作：修改备忘录
    public static final int MEMO_ACTION_UPDATE = 1;

    //备忘录Activity跳转请求码，REQUEST_CODE_MODIFY_CONTENT-修改内容时跳转，REQUEST_CODE_ADD_NEW_CONTENT-新增备忘录跳转
    public static final int REQUEST_CODE_MODIFY_CONTENT = 1;
    public static final int REQUEST_CODE_ADD_NEW_CONTENT = 2;

    //备忘录Activity返回的结果码，RESULT_CODE_MODIFY_CONTENT-修改内容后返回，RESULT_CODE_ADD_NEW_CONTENT-新增内容后返回
    public static final int RESULT_CODE_MODIFY_CONTENT = 3;
    public static final int RESULT_CODE_ADD_NEW_CONTENT = 4;
}
