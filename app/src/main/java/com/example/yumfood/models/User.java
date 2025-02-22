package com.example.yumfood.models;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String cur_location;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCur_location() {
        return cur_location;
    }

    public void setCur_location(String cur_location) {
        this.cur_location = cur_location;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("fullName", fullName);
        result.put("cur_location", cur_location);
        result.put("phoneNumber", phoneNumber);
        return result;
    }
}
