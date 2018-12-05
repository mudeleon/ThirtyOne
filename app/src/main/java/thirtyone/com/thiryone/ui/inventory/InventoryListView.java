package thirtyone.com.thiryone.ui.inventory;

import com.hannesdorfmann.mosby.mvp.MvpView;

import thirtyone.com.thiryone.model.data.Food;


public interface InventoryListView extends MvpView {




    void startLoading();

    void stopLoading();


    void setDeleteFoodList(Food food);




    void setEditFoodList(Food food);

    void setFoodList();


    void stopRefresh();

    void showError(String message);

    void closeDialog(String message);

    void onRefresh();




}
