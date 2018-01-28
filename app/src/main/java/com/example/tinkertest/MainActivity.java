package com.example.tinkertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Tinker.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button requestPatchButton = (Button) findViewById(R.id.requestPatch);

        //immediately 为 true, 每次强制访问服务器更新
        requestPatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerPatch.with().fetchPatchUpdate(true);
                //woshi 大神
                //我是大神中的大神
            }
        });

        Button requestConfigButton = (Button) findViewById(R.id.requestConfig);

        //immediately 为 true, 每次强制访问服务器更新
        requestConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerPatch.with().fetchDynamicConfig(new ConfigRequestCallback() {

                    @Override
                    public void onSuccess(HashMap<String, String> configs) {
                        TinkerLog.w(TAG, "request config success, config:" + configs);
                    }

                    @Override
                    public void onFail(Exception e) {
                        TinkerLog.w(TAG, "request config failed, exception:" + e);
                    }
                }, true);
            }
        });

        Button cleanPatchButton = (Button) findViewById(R.id.cleanPatch);

        cleanPatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerPatch.with().cleanAll();
            }
        });

        Button killSelfButton = (Button) findViewById(R.id.killSelf);

        killSelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }


}
