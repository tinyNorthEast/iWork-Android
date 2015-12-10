package com.iwork.model;

/**
 * Created by JianTao on 15/12/10.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class QiniuPicUrl {
    /**
     * key : iWork_user_img
     * hash : FgXd0jM6PRS8gZLj-I4Y7YHdGRgq
     * width : 300
     * height : 300
     */

    private String key;
    private String hash;
    private int width;
    private int height;

    public void setKey(String key) {
        this.key = key;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getKey() {
        return key;
    }

    public String getHash() {
        return hash;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
