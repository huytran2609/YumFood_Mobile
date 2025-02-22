package com.example.yumfood.models;


import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Store implements Serializable{
    private String storeId;
    private String storeName;
    private String storeCategory;
    private String owner;
    private String avatar;

    public void setRating(float rating) {
        this.rating = rating;
    }

    private float rating;
    private String deliveryTime;
    private String storeAddress;
    private List<String> productGrouping;
    private int storeStatus;

    public float getRating() {
        return rating;
    }

    public Store(String storeId, String storeName, String storeCategory, String owner, String avatar, float rating, String deliveryTime, String storeAddress) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeCategory = storeCategory;
        this.owner = owner;
        this.avatar = avatar;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.storeAddress = storeAddress;
    }

    public Store() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    public String getAvatar() {
        return avatar;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public String getOwner() {
        return owner;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<String> getProductGrouping() {
        return productGrouping;
    }

    public void setProductGrouping(List<String> productGrouping) {
        this.productGrouping = productGrouping;
    }

    public int getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(int storeStatus) {
        this.storeStatus = storeStatus;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("storeId", storeId);
        result.put("storeName", storeName);
        result.put("storeCategory", storeCategory);
        result.put("storeAddress", storeAddress);
        result.put("owner", owner);
        result.put("avatar", avatar);
        result.put("rating", rating);
        result.put("deliveryTime", deliveryTime);
        result.put("productGrouping", productGrouping);
        result.put("storeStatus", storeStatus);
        return result;
    }
}
