package thirtyone.com.thiryone.ui.inventory;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;
import thirtyone.com.thiryone.R;
import thirtyone.com.thiryone.app.Endpoints;
import thirtyone.com.thiryone.model.data.Food;


@SuppressWarnings("ConstantConditions")
public class InventoryListPresenter extends MvpBasePresenter<InventoryListView> {

    private Realm realm;


    public void registerFood(String name,
                            String type,
                            int image,
                            int id
                           ) {

        realm = Realm.getDefaultInstance();

        if (name.equals("")  ||  type.equals("")) {
            getView().showError("Fill-up all fields");
        }else {

            Log.d("Sasas>>",">>>"+id);
            getView().startLoading();
            final Food addfood = new Food();
            addfood.setFoodId(String.valueOf(id));
            addfood.setFoodName(name);
            addfood.setFoodImage(image);
            addfood.setFoodType(type);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(addfood);
                }
            });

            getView().stopLoading();
            getView().closeDialog("Adding Successful!");

        }

    }

    public void editFood(final String id,final String name,
                           final String type,
                            final int image
                           ) {
        realm = Realm.getDefaultInstance();
        if (name.equals("")  ||  type.equals("")) {
            getView().showError("Fill-up all fields");
        }else {
            getView().startLoading();


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Food food = realm.where(Food.class).equalTo("foodId", id).findFirst();
                    if(food != null) {
                        food.setFoodName(name);
                        food.setFoodImage(image);
                        food.setFoodType(type);
                    }
                }
            });
            getView().stopLoading();
            getView().closeDialog("Update Successful!");
        }

    }


    public void deleteFood(final String id) {


            getView().startLoading();

        realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Food food = realm.where(Food.class).equalTo("foodId", id).findFirst();
                    if(food != null) {
                        food.deleteFromRealm();
                    }
                }
            });
        getView().stopLoading();
        getView().showError("Delete Successful!");
        getView().setFoodList();


    }


    public int getRandomImage(String type)
    {
        int urlReturn = 0;

        if(type.equals("Vegetables"))
            urlReturn = R.drawable.placeholder_veg;
        else if(type.equals("Fruits"))
            urlReturn = R.drawable.placeholder_fruits;
        else if(type.equals("Junk Food"))
            urlReturn = R.drawable.placeholder_junks;

        return  urlReturn;
    }








}
