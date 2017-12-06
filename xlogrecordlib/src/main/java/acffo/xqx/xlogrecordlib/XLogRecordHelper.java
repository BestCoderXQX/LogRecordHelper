package acffo.xqx.xlogrecordlib;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @author xqx
 * @email djlxqx@163.com
 * blog:http://www.cnblogs.com/xqxacm/
 * createAt 2017/12/6
 * description: 日志记录
 * 将Log日志记录到文件中，方便用户查看
 */

public class XLogRecordHelper {
    private static XLogRecordHelper instance = null;
    private LogDumper mLogDumper = null;
    private int mPId;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US);//日期格式;
    private static Date date = new Date();//因为log日志是使用日期命名的，使用静态成员变量主要是为了在整个程序运行期间只存在一个.log文件中;
    private boolean isSaveToSDCard = false ; // 文件 是否存到sd卡中

    private static String File_Location; // 文件存储路径
    private static String fileDir = "xLogHelper" ; // 文件存储位置
    private static String fileName = "xLogFile"; // 文件名
    private static String filterStr ; // 过滤信息

    /**
     * 单例模式获取对象
     * @param context
     * @return
     */
    public static XLogRecordHelper getInstance(Context context , String dir , String name) {
        if (instance == null) {
            fileName = name ;
            fileDir = dir;
            instance = new XLogRecordHelper(context);
        }
        return instance;
    }

    /**
     * 构造方法，进行初始化
     * @param context
     */
    private XLogRecordHelper(Context context  ) {
        init(fileDir );  // 初始化文件存储目录
        mPId = android.os.Process.myPid();
    }

    /**
     * 初始化目录
     * */
    public void init(String fileDir) {
        if (isSaveToSDCard) { // 设置记录log文件是否存储到sd卡中
            // 如果有SDCard
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File_Location = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + fileDir+"/";
            } else {
                // 如果SD卡不存在，就保存到手机内置存储控件
                File_Location = Environment.getExternalStorageDirectory() + "/"+fileDir+"/";
            }
        }else{
            // 存储到手机内置存储控件
            File_Location = Environment.getExternalStorageDirectory() + "/"+fileDir+"/";
        }
        File file = new File(File_Location,fileName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始记录
     */
    public void start() {
        if (mLogDumper == null) {
            mLogDumper = new LogDumper(String.valueOf(mPId), File_Location);
        }
        mLogDumper.start();
    }

    /**
     * 结束记录
     */
    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {

        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String mPID;
        private FileOutputStream out = null;

        public LogDumper(String pid, String dir) {
            mPID = pid;
            try {
                out = new FileOutputStream(new File(dir, fileName));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            cmds = "logcat -s "+filterStr;
        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(
                        logcatProc.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (out != null && line.contains(mPID)) {
                        out.write(( dateFormat.format(date) +":  " + line + "\n")
                                .getBytes());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }
            }
        }
    }

    /**
     * 设置过滤信息
     * @param filterStr
     */
    public static void setFilterStr(String filterStr) {
        XLogRecordHelper.filterStr = filterStr;
    }
}
