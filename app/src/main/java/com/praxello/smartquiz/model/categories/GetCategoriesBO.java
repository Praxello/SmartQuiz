package com.praxello.smartquiz.model.categories;

public class GetCategoriesBO {
    public int categoryId;
    public String categoryTitle;
    public int isActive;

    public  GetCategoriesBO(){

    }
    public GetCategoriesBO(int categoryId, String categoryTitle, int isActive) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.isActive = isActive;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
