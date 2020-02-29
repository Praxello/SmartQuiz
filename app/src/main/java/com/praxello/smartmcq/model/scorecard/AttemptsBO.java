package com.praxello.smartmcq.model.scorecard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AttemptsBO implements Parcelable {
    public String categoryTitle;
    public int total;

    protected AttemptsBO(Parcel in) {
        categoryTitle = in.readString();
        total = in.readInt();
    }

    public static final Creator<AttemptsBO> CREATOR = new Creator<AttemptsBO>() {
        @Override
        public AttemptsBO createFromParcel(Parcel in) {
            return new AttemptsBO(in);
        }

        @Override
        public AttemptsBO[] newArray(int size) {
            return new AttemptsBO[size];
        }
    };

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @NonNull
    @Override
    public String toString() {
        return "categoryTitle"+categoryTitle+"\n total"+total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryTitle);
        dest.writeInt(total);
    }
}
