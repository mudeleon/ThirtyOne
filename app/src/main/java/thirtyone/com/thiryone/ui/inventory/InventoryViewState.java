package thirtyone.com.thiryone.ui.inventory;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


class InventoryViewState implements RestorableViewState<InventoryListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<InventoryListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(InventoryListView view, boolean retained) {

    }
}
