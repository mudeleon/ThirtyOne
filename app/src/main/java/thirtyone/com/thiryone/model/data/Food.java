package thirtyone.com.thiryone.model.data;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Food extends RealmObject {




    @PrimaryKey
    private String foodId;

    private String foodName;
    private int foodImage;
    private String foodType;


    public Food() {
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(int foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }



}
