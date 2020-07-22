package com.eliot.notebook.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName: NoteBook
 * @Package: com.eliot.notebook.common
 * @ClassName: Utils
 * @Description: 工具类
 * @Author: Eliot-Aaaa
 * @CreateDate: 2020/7/22 16:29
 */
public class Utils
{
    /**
     * @descption 通过传入的微秒数转换成字符串
     * @author Eliot-Aaaa
     * @time 2020/7/22 17:30
     * @para currentTime-要转换成字符串的时间（微妙）
     */
    public static String getCurrentTime(long currentTime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);
        return simpleDateFormat.format(date);
    }
}
