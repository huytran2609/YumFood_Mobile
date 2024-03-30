package com.example.yumfood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.yumfood.models.Notification;
import com.example.yumfood.models.OrdAddress;
import com.example.yumfood.models.Order;
import com.example.yumfood.models.Product;
import com.example.yumfood.models.Review;
import com.example.yumfood.models.Store;
import com.example.yumfood.models.Topping;
import com.example.yumfood.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class YumFoodDatabase {
    private DatabaseReference mDatabase;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public static void insertProduct(Product product, ImageView ivProductAvatar) {
    }

    public void loadStoreNameAndAddress(String storeId, TextView txtName, TextView txtAddress){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(storeId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Store store = task.getResult().getValue(Store.class);
                    txtName.setText(store.getStoreName());
                    txtAddress.setText(store.getStoreAddress());
                }
            }
        });
    }

    public void loadShippingAddress(String orderId, TextView txtCusName, TextView txtAddress){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("orders").child(orderId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Order order = task.getResult().getValue(Order.class);
                    txtCusName.setText(order.getShippingAddress().getReceiver());
                    txtAddress.setText(order.getShippingAddress().getAddress().replace(", Vietnam", "").replace(", Việt Nam", ""));
                }
            }
        });
    }
    public void loadUserFullnameToTextView(String userId, TextView tv)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    tv.setText(user.getFullName());
                }
            }
        });
    }
    public void loadUserFullNameAndPhoneToTextView(String userId, TextView tvName, TextView tvPhone)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    tvName.setText(user.getFullName());
                    tvPhone.setText(user.getPhoneNumber());
                }
            }
        });
    }

    public void loadShippingAddressToTextViewByOrderId(String orderId, TextView tvName, TextView tvAddress)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("orders").child(orderId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Order order = task.getResult().getValue(Order.class);
                    tvName.setText(order.getShippingAddress().getReceiver());
                    tvAddress.setText(order.getShippingAddress().getAddress().replace(", Vietnam", "").replace(", Việt Nam", ""));
                }
            }
        });
    }

    public void loadStoreNameAndAddressToTextView(String storeId, TextView tvName, TextView tvAddress)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(storeId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Store store = task.getResult().getValue(Store.class);
                    tvName.setText(store.getStoreName());
                    tvAddress.setText(store.getStoreAddress());
                }
            }
        });
    }
    public void insertNotification(Notification notification){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("Users").child(notification.getUserId()).child("notifications").push().getKey();

        try {
            Map<String, Object> storeValues = notification.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Users/" + notification.getUserId() + "/notifications/" + key, storeValues);
            mDatabase.updateChildren(childUpdates);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void insertTopping(Topping topping, String storeId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        String key = databaseRef.child("stores").child(storeId).child("menu").child("topping").push().getKey();
        topping.setToppingId(key);
        Map<String, Object> toppingValues = topping.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stores/" + storeId + "/menu/topping/"+ key, toppingValues);
        databaseRef.updateChildren(childUpdates);
    }

    public void loadImageToImageView(@NonNull ImageView iv, String folderName, String image) {
        if (iv != null) {
            String fileName = image.replace(folderName + "/", "");
            StorageReference firebaseStorage = storage.getReference().child(folderName).child(fileName);
            try{
                final File localFile = File.createTempFile(fileName.replace(".png",""),"png");
                firebaseStorage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        iv.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("loadImageToImageView", "Error loading image from Firebase Storage: " + e.getMessage());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFileInStorageFirebase(String filePath) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child(filePath);
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // An error occurred while deleting the file
            }
        });
    }

    public void deleteProduct(Product product, Context context) {
        mDatabase = FirebaseDatabase.getInstance().getReference("stores/"+product.getStoreId()+"/menu/products/"+product.getProductId());
        mDatabase.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                deleteFileInStorageFirebase(product.getProductImage());
            }
        });
    }

    public void updateProduct(Product product) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("stores").child(product.getStoreId()).child("menu").child("products").child(product.getProductId());
        productRef.setValue(product);
    }

    private DatabaseReference getStoreRef(String storeId) {
        return FirebaseDatabase.getInstance().getReference().child("stores").child(storeId);
    }

    public void updateProductGroupingForStore(String storeId, List<String> productGrouping) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(storeId).child("productGrouping").setValue(productGrouping);
    }



    public void loadUserFullname(String userId, TextView tv)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    tv.setText(user.getFullName());
                }
            }
        });

    }
    public void loadStoreInfoToTextView(String storeId, TextView tvName, Switch ivSwitch, ImageView Avt, Context context)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(storeId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Store store = task.getResult().getValue(Store.class);
                    tvName.setText(store.getStoreName());
                    if(store.getStoreStatus()==1) ivSwitch.setChecked(true);
                    Glide.with(context).load(store.getAvatar()).into(Avt);
                }
            }
        });
    }
    public void updateStoreStatus(String storeId, int storeStatus) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(storeId).child("storeStatus").setValue(storeStatus);
    }
    public void updateOrder(Order order) {
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Map<String, Object> values = order.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/orders/"+ order.getOrderId(),values);
        mDatabase.updateChildren(childUpdates);
    }

    public void insertStoreComment(Review review, String store_ID, String user_ID){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        try {
            Map<String, Object> reviewValues = review.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/stores/" + store_ID + "/reviews/" + user_ID, reviewValues);
            mDatabase.updateChildren(childUpdates);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateRatingtotal(String store_ID){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stores").child(store_ID).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float total = 0;
                int num_review = 0;
                for (DataSnapshot querysnapshot: snapshot.getChildren()) {
                    Review review = querysnapshot.getValue(Review.class);
                    total += review.getRating();
                    num_review += 1;
                }
                mDatabase.child("stores").child(store_ID).child("rating").setValue(total / num_review);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void insertLoveStore(Store store, String user_ID){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        try {
            Map<String, Object> storeValues = store.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Users/" + user_ID + "/love_list/" + store.getStoreId(), storeValues);
            mDatabase.updateChildren(childUpdates);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void loadCustomerShippingAddressToTextView(String userId, TextView tvName, TextView tvPhone ,TextView tvAddress)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    tvName.setText(user.getFullName());
                    tvAddress.setText(user.getCur_location().replace(", Vietnam", "").replace(", Việt Nam", ""));
                    tvPhone.setText(user.getPhoneNumber());
                }
            }
        });
    }

    public void insertOrder(Order order) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("orders").push().getKey();
        order.setOrderId(key);

        Map<String, Object> orderValues = order.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/orders/" + key, orderValues);
        mDatabase.updateChildren(childUpdates);
    }

    public void deleteOrdAddress(String ordAddress_phone, String user_ID)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query addressQuery = mDatabase.child("Users").child(user_ID).child("address_ord_list").orderByChild("phone_number").equalTo(ordAddress_phone);
        addressQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot addSnapshot: snapshot.getChildren()) {
                    addSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERR", "delete ord address");
            }
        });
    }

    public void insertOrdAddress(OrdAddress address, String user_ID){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("Users").child(user_ID).child("address_ord_list").push().getKey();

        try {
            Map<String, Object> addValues = address.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Users/" + user_ID + "/address_ord_list/" + key, addValues);
            mDatabase.updateChildren(childUpdates);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void insertStore(Store store, ImageView imageView) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("stores").push().getKey();
        store.setStoreId(key);

        /* Xử lý thêm ảnh vô firebase */
        StorageReference storageRef = storage.getReference( "store_image/store-avatar" + key + ".png");
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                /* Thêm dữ liệu vô Realtime database */
                Map<String, Object> storeValues = store.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/stores/" + key, storeValues);
                mDatabase.updateChildren(childUpdates);

                /* Lấy url sau khi upload ảnh thành công */
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        store.setAvatar(uri.toString());
                        /* Thêm dữ liệu vô Realtime database */
                        mDatabase.child("stores").child(key).child("avatar").setValue(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        });
    }
}
