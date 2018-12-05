package thirtyone.com.thiryone.ui.inventory;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import thirtyone.com.thiryone.R;
import thirtyone.com.thiryone.app.Endpoints;
import thirtyone.com.thiryone.databinding.ItemFoodListBinding;
import thirtyone.com.thiryone.model.data.Food;


public class InventoryListAdapter extends RecyclerView.Adapter<InventoryListAdapter.ViewHolder> {
    private List<Food> food;
    private final Context context;
    private final InventoryListView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public InventoryListAdapter(Context context, InventoryListView view) {
        this.context = context;
        this.view = view;
        food = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFoodListBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_food_list,
                parent,
                false);








        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(InventoryListAdapter.ViewHolder holder, final int position) {
        holder.itemEventBinding.setFood(food.get(position));
        holder.itemEventBinding.setView(view);



        holder.itemEventBinding.foodListEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                view.setEditFoodList(food.get(position));

            }
        });


        holder.itemEventBinding.foodListDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                view.setDeleteFoodList(food.get(position));


            }
        });



        Glide.with(holder.itemView.getContext())
                .load(food.get(position).getFoodImage())
                .centerCrop()
                .error(R.drawable.place_holder)
                .into(holder.itemEventBinding.foodListImage);



    }


    public void clear() {
        food.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    public void setEventResult(List<Food> food) {
        this.food = food;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFoodListBinding itemEventBinding;

        public ViewHolder(ItemFoodListBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }



    }
}
