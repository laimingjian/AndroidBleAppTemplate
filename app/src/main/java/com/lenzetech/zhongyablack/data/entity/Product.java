package com.lenzetech.zhongyablack.data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "mac", unique = true)})
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String mac;
    private int type;
    private boolean isAutoConnected;

    @Ignore
    public Product() {
    }

    public Product(String name, String mac, int type, boolean isAutoConnected) {
        this.name = name;
        this.mac = mac;
        this.type = type;
        this.isAutoConnected = isAutoConnected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAutoConnected() {
        return isAutoConnected;
    }

    public void setAutoConnected(boolean autoConnected) {
        isAutoConnected = autoConnected;
    }
}
