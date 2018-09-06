package com.deity.realm;

import android.util.Log;

import io.realm.Realm;

/**
 * 这里添加文件描述
 * Create by fengwenhua at 2018/9/6
 **/
public class UserDaoImpl {


    private static UserDaoImpl userDao = new UserDaoImpl();

    private UserDaoImpl(){}

    public static UserDaoImpl getInstance(){
        return userDao;
    }

    /**
     * executeTransaction()
     * 同步执行,在当前线程上执行
     */
    public void addUserSync(){
        Realm realm = Realm.getDefaultInstance();
        long lastTimestamp = System.currentTimeMillis();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserEntity userEntity = realm.createObject(UserEntity.class,System.currentTimeMillis());
                userEntity.setName("代号:"+System.currentTimeMillis());
                realm.copyToRealmOrUpdate(userEntity);
            }
        });
        realm.close();
        long totalTimestamp = System.currentTimeMillis() - lastTimestamp;
        Log.i("UserDaoImpl","addUserSync用时:"+totalTimestamp);
    }

    /**
     * 异步执行,另开工作线程
     */
    public void addUserASync(){
        Realm realm = Realm.getDefaultInstance();
        long lastTimestamp = System.currentTimeMillis();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserEntity userEntity = realm.createObject(UserEntity.class,System.currentTimeMillis());
                userEntity.setName("代号:"+System.currentTimeMillis());
                realm.copyToRealmOrUpdate(userEntity);
            }
        });
        realm.close();
        long totalTimestamp = System.currentTimeMillis() - lastTimestamp;
        Log.i("UserDaoImpl","addUserASync(理论上耗时不计):"+totalTimestamp);
    }



    public void addUser(){
        Realm realm = Realm.getDefaultInstance();
        long lastTimestamp = System.currentTimeMillis();
        try {
            realm.beginTransaction();
            UserEntity userEntity = realm.createObject(UserEntity.class,System.currentTimeMillis());
            userEntity.setName("代号:"+System.currentTimeMillis());
            realm.copyToRealmOrUpdate(userEntity);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }finally {
            realm.close();
        }
        long totalTimestamp = System.currentTimeMillis() - lastTimestamp;
        Log.i("UserDaoImpl","addUser:"+totalTimestamp);
    }

      //TODO WARNING 以下Realm.getDefaultInstance() 形式 尝试压缩数据库文件失败(default.realm)
//    public void dropTable(){
//        long lastTimestamp = System.currentTimeMillis();
//        try {
//            Realm.getDefaultInstance().beginTransaction();
//            Realm.getDefaultInstance().delete(UserEntity.class);
//            Realm.getDefaultInstance().commitTransaction();
//        }catch (Exception e){
//            Realm.getDefaultInstance().cancelTransaction();
//        }
//        Realm.getDefaultInstance().close();
//        boolean isCompactSuccess = Realm.compactRealm(Realm.getDefaultInstance().getConfiguration());
//        long totalTimestamp = System.currentTimeMillis() - lastTimestamp;
//        Log.i("UserDaoImpl","用时:"+totalTimestamp+" 压缩数据库文件是否成功:"+isCompactSuccess);
//    }

    // 以下代码在 数据库实例只有一个的情况下 压缩数据库文件成功(default.realm)
    public void dropTable(){
        Realm realm = Realm.getDefaultInstance();
        long lastTimestamp = System.currentTimeMillis();
        int size1 = 0;
        int size2 = 0;
        try {
            realm.beginTransaction();
            size1 = realm.where(UserEntity.class).findAll().size();
            realm.delete(UserEntity.class);
            size2 = realm.where(UserEntity.class).findAll().size();
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
        realm.close();
        boolean isCompactSuccess = Realm.compactRealm(realm.getConfiguration());
        long totalTimestamp = System.currentTimeMillis() - lastTimestamp;
        Log.i("UserDaoImpl","清除之前总计:"+size1+"清除之后:"+size2+"用时:"+totalTimestamp+" 压缩数据库文件是否成功:"+isCompactSuccess);
    }


}
