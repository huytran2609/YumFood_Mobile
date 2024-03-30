package com.example.yumfood.seller.store_management.SellerManageMenu;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SellerManageMenuViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SellerManageMenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}