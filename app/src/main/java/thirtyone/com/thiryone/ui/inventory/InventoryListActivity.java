package thirtyone.com.thiryone.ui.inventory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import thirtyone.com.thiryone.R;
import thirtyone.com.thiryone.databinding.ActivityInventoryBinding;
import thirtyone.com.thiryone.databinding.DialogAddFoodBinding;
import thirtyone.com.thiryone.databinding.DialogEditFoodBinding;
import thirtyone.com.thiryone.model.data.Food;


public class InventoryListActivity
        extends MvpViewStateActivity<InventoryListView, InventoryListPresenter>
        implements SwipeRefreshLayout.OnRefreshListener, InventoryListView {

    private static final String TAG = InventoryListActivity.class.getSimpleName();
    private ActivityInventoryBinding binding;
    private Realm realm;
    private RealmResults<Food> foodRealmResults;
    private InventoryListAdapter inventoryListAdapter;
    public String id;
    DialogEditFoodBinding editDialogBinding;
    DialogAddFoodBinding addDialogBinding;
    private Dialog dialog;
    private ArrayList<String> foodType;
    private ArrayAdapter<String> foodAdapter;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);




        realm = Realm.getDefaultInstance();



        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Inventory List");

        inventoryListAdapter = new InventoryListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(inventoryListAdapter);



        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
         binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });



        binding.addFood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addCar();
            }
        });

        populateFoodType();

    }


    public void populateFoodType(){

        foodType = new ArrayList<>();
        foodType.add("Vegetables");
        foodType.add("Fruits");
        foodType.add("Junk Food");



         foodAdapter = new ArrayAdapter<>(InventoryListActivity.this, R.layout.spinner_custom_item,foodType);


    }





    @NonNull
    @Override
    public InventoryListPresenter createPresenter() {
        return new InventoryListPresenter();
    }

    @NonNull
    @Override
    public ViewState<InventoryListView> createViewState() {
        setRetainInstance(true);
        return new InventoryViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        setFoodList();
    }


    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }


    @Override
    public void onRefresh() {


        setFoodList();
    }




    @Override
    public void setFoodList(){



        foodRealmResults = realm.where(Food.class).findAllAsync();
        inventoryListAdapter.setEventResult(realm.copyToRealmOrUpdate(foodRealmResults.where()
                .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
        inventoryListAdapter.notifyDataSetChanged();



        if(inventoryListAdapter.getItemCount()==0)
        {
            binding.foodNoRecyclerview.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }else
        {
            binding.foodNoRecyclerview.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }

        stopRefresh();
    }



    @Override
    public void stopRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeDialog(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        onRefresh();

        dialog.dismiss();

    }




    @Override
    public void setDeleteFoodList(final Food food) {


        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete this inventory?")
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteFood(String.valueOf(food.getFoodId()));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .show();


    }

    @Override
    public void setEditFoodList(final Food food) {


      dialog = new Dialog(this);




        editDialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_edit_food,
                null,
                false);

        editDialogBinding.setFood(food);






        editDialogBinding.editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editDialogBinding.editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int randomImage = presenter.getRandomImage(editDialogBinding.spType.getSelectedItem().toString());

                presenter.editFood(  String.valueOf(food.getFoodId()),editDialogBinding.etFoodName.getText().toString(),
                        editDialogBinding.spType.getSelectedItem().toString(),
                      randomImage);


            }
        });

        editDialogBinding.spType.setAdapter(foodAdapter);

        if(food.getFoodType().equalsIgnoreCase("Vegetables"))
            editDialogBinding.spType.setSelection(0);
       else  if(food.getFoodType().equalsIgnoreCase("Fruits"))
            editDialogBinding.spType.setSelection(0);
        else if(food.getFoodType().equalsIgnoreCase("Junk Food"))
            editDialogBinding.spType.setSelection(0);


        Glide.with(getApplicationContext())
                .load(food.getFoodImage())
                .centerCrop()
                .error(R.drawable.place_holder)
                .into(editDialogBinding.imageView);


        dialog.setContentView(editDialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();
    }









    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }









    public void addCar()
    {

      dialog = new Dialog(this);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        addDialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_add_food,
                null,
                false);


        addDialogBinding.setView(getMvpView());


        addDialogBinding.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addDialogBinding.Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int randomImage = presenter.getRandomImage(addDialogBinding.spType.getSelectedItem().toString());

                foodRealmResults = realm.where(Food.class).findAllAsync();
                presenter.registerFood(  addDialogBinding.etFoodName.getText().toString(),
                        addDialogBinding.spType.getSelectedItem().toString(),
                        randomImage,(foodRealmResults.size()+1));
            }
        });


        addDialogBinding.spType.setAdapter(foodAdapter);

        dialog.setContentView(addDialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();


    }




}
