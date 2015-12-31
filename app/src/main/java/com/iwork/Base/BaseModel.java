package com.iwork.Base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JianTao on 15/12/31.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class BaseModel implements Parcelable {
    public BaseModel() {
    }

    protected BaseModel(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
