package com.deity.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 这里添加文件描述
 * Create by fengwenhua at 2018/9/6
 **/
public class UserEntity extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
