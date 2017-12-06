package acffo.xqx.logrecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import acffo.xqx.xlogrecordlib.XLogRecordHelper;

public class MainActivity extends AppCompatActivity {

    XLogRecordHelper xLogRecordHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xLogRecordHelper = XLogRecordHelper.getInstance(this, "xdirx", "aaaa.txt");
        xLogRecordHelper.setFilterStr("xqxinfo");
        xLogRecordHelper.start();
        for (int i = 0; i < 100; i++) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xLogRecordHelper.stop();
    }
}
