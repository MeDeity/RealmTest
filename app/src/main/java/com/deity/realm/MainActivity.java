package com.deity.realm;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_test1)
    protected Button btn_test1;

    @BindView(R.id.btn_test2)
    protected Button btn_test2;

    @BindView(R.id.btn_test3)
    protected Button btn_test3;

    @BindView(R.id.btn_test4)
    protected Button btn_test4;

    @BindView(R.id.tv_description)
    protected TextView tv_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        Log.w("MainActivity", "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainHandle(DatabaseEvent event) {
        switch (event.getCode()) {
            case 1:
                UserDaoImpl.getInstance().addUserSync();
                break;
            case 2:
                UserDaoImpl.getInstance().addUserASync();
                break;
            case 11:
                UserDaoImpl.getInstance().addUser();
                break;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGroundHandle(DatabaseEvent event) {
        switch (event.getCode()) {
            case 3:
                UserDaoImpl.getInstance().addUserSync();
                break;
            case 4:
                UserDaoImpl.getInstance().addUserASync();
                break;
            case 12:
                UserDaoImpl.getInstance().addUser();
                break;
        }
    }


    public long getFileLength(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            return (f.length() / 1024);
        } else {
            throw new Resources.NotFoundException("文件未找到");
        }
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.btn_test1, R.id.btn_test2, R.id.btn_test3, R.id.btn_test4, R.id.btn_test5, R.id.btn_test6
            , R.id.btn_test10, R.id.btn_test11, R.id.btn_test12,R.id.btn_test7, R.id.btn_get_count})
    public void btnClickListener(Button button) {
        switch (button.getId()) {
            case R.id.btn_test1:
                UserDaoImpl.getInstance().addUserSync();
                break;
            case R.id.btn_test2:
                UserDaoImpl.getInstance().addUserASync();
                break;
            case R.id.btn_test3:
                EventBus.getDefault().post(new DatabaseEvent(1));
                break;
            case R.id.btn_test4:
                EventBus.getDefault().post(new DatabaseEvent(2));
                break;
            case R.id.btn_test5:
                EventBus.getDefault().post(new DatabaseEvent(1));
                break;
            case R.id.btn_test6:
                EventBus.getDefault().post(new DatabaseEvent(2));
                break;
            case R.id.btn_test10:
                UserDaoImpl.getInstance().addUser();
                break;
            case R.id.btn_test11:
                EventBus.getDefault().post(new DatabaseEvent(11));
                break;
            case R.id.btn_test12:
                EventBus.getDefault().post(new DatabaseEvent(12));
                break;
            case R.id.btn_test7://清除数据库
                UserDaoImpl.getInstance().dropTable();
                break;
            case R.id.btn_get_count:
                StringBuilder builder = new StringBuilder();
                Realm realm = Realm.getDefaultInstance();
                builder.append("当前数据库实例句柄:").append(Realm.getGlobalInstanceCount(realm.getConfiguration())).append("个")
                        .append("数据库文件大小:").append(getFileLength(realm.getPath())).append("KB");
                realm.close();
                tv_description.setText(builder.toString());
                break;
        }

    }
}
