package com.example.yumfood.seller.store_management.SellerReport;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SellerReportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SellerReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
