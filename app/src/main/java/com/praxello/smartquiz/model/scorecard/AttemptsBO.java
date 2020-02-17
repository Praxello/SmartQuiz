package com.praxello.smartquiz.model.scorecard;

import androidx.annotation.NonNull;

public class AttemptsBO {
    public String categoryTitle;
    public int total;

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
}
