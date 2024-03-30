package com.example.yumfood.seller.store_selection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.models.Store;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SellerAddNewStoreFragment extends Fragment {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private YumFoodDatabase yumFoodDatabase = new YumFoodDatabase();
    private EditText etStoreName;
    private EditText etAddress;
    private ImageView btnBack2;
    private ImageView imgChooseStorePicture;
    private Button btnAddStoretoDatabase;
    private TextView txtChooseCategoty;
    private String[] strstoreCategories = {"Đồ ăn", "Đồ uống", "Đồ chay", "Bánh kem"};
    private boolean[] actselected;
    private List<Integer> actselectedList = new ArrayList<>();
    private ActivityResultLauncher<Intent> checkPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imgChooseStorePicture.setImageURI(data.getData());
                    }
                }
            });


    public SellerAddNewStoreFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_add_new_store, container, false);
        btnBack2 = (ImageView) view.findViewById(R.id.buttonBack);
        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SellerAddNewStoreFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

            }
        });
        btnAddStoretoDatabase = (Button) view.findViewById(R.id.buttonAddStoretoDatabase);
        btnAddStoretoDatabase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Thêm cửa hàng mới thành công",Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(SellerAddNewStoreFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });


        txtChooseCategoty= (TextView) view.findViewById(R.id.txtChooseCategoty);
        txtChooseCategoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Chọn loại cửa hàng");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(strstoreCategories, actselected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // Check condition
                        if(b){
                            // When checkbox selected
                            // Add position in day list
                            actselectedList.add(i);
                            Collections.sort(actselectedList);
                        }
                        else{
                            actselectedList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("Xong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j = 0; j < actselectedList.size(); j++)
                        {
                            // Concat array value
                            stringBuilder.append(strstoreCategories[actselectedList.get(j)]);

                            if(j != actselectedList.size() -1)
                            {
                                stringBuilder.append(", ");
                            }
                        }
                        // Set text for spinner
                        txtChooseCategoty.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j = 0; j < actselected.length; j++)
                        {
                            actselected[j] = false;
                            actselectedList.clear();
                        }
                        txtChooseCategoty.setText("");
                    }
                });
                builder.show();
            }
        });// Inflate the layout for this fragment

        imgChooseStorePicture = view.findViewById(R.id.imgChooseStorePicture);

        imgChooseStorePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                checkPermission.launch(Intent.createChooser(intent, "Select Avatar"));

            }
        });
        etStoreName = view.findViewById(R.id.etStoreName);
        etAddress = view.findViewById(R.id.etAddress);
        btnAddStoretoDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storeName = etStoreName.getText().toString();
                String storeCategory = txtChooseCategoty.getText().toString();
                String storeAddress = etAddress.getText().toString();

                // Lấy mã user trong Session
                SharedPreferences preferences = getContext().getSharedPreferences("Session", getContext().MODE_PRIVATE);
                String owner = preferences.getString("userId", "default value");

                // Lưu thông tin store vào realtime database
                Store store = new Store();;
                store.setStoreName(storeName);
                store.setStoreCategory(storeCategory);
                store.setStoreAddress(storeAddress);
                store.setOwner(owner);
                store.setStoreStatus(1);
                Random rand = new Random();
                int dt = 10 + rand.nextInt(40);
                store.setDeliveryTime(dt +"mins");
                List<String> productGrouping = new ArrayList<>();
                productGrouping.add("Không xác định");
                store.setProductGrouping(productGrouping);

                yumFoodDatabase.insertStore(store, imgChooseStorePicture);
                Toast.makeText(getActivity(), "Thêm cửa hàng mới thành công",Toast.LENGTH_SHORT).show();

                NavHostFragment.findNavController(SellerAddNewStoreFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        return view;
    }
}