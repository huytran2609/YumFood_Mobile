package com.example.yumfood.seller.store_management.SellerManageMenu.product;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.R;
import com.example.yumfood.models.Product;

public class SellerAddProductActivity extends AppCompatActivity {
    private EditText etProductName, etPrice, etDescription;
    private SwitchCompat swIsAvailable;
    private Button btnAddProduct;
    private YumFoodDatabase yumFoodDatabase;
    private ImageView ivProductAvatar;
    private ImageView ibBack;

    private void initUi()
    {
        etPrice = (EditText) findViewById(R.id.et_price);
        etProductName = (EditText) findViewById(R.id.et_product_name);
        etDescription = (EditText) findViewById(R.id.et_product_description);
        btnAddProduct = (Button) findViewById(R.id.btn_add_product);
        swIsAvailable = (SwitchCompat) findViewById(R.id.sw_is_available);
        ivProductAvatar = (ImageView) findViewById(R.id.iv_product_avatar);
        ibBack = (ImageView) findViewById(R.id.activity_add_product_ib_back);
    }

    private ActivityResultLauncher<Intent> checkPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        ivProductAvatar.setImageURI(data.getData());
                    }
                }
            });

    private void choosePicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        checkPermission.launch(Intent.createChooser(intent, "Select Avatar"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);
        initUi();
        yumFoodDatabase = new YumFoodDatabase();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = etProductName.getText().toString();
                int price = Integer.parseInt(etPrice.getText().toString());
                String description = etDescription.getText().toString();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("Session", MODE_PRIVATE);
                String storeId = prefs.getString("storeId", "No name defined");
                int isAvailable = 0;
                if(swIsAvailable.isChecked())
                    isAvailable = 1;

                Product product= new Product();
                product.setProductName(productName);
                product.setPrice(price);
                product.setProductGrouping("Không xác định");
                if(!description.isEmpty())
                    product.setProductDescription("");
                else
                    product.setProductDescription(description);
                product.setStoreId(storeId);
                product.setAvailable(isAvailable);
                yumFoodDatabase.insertProduct(product, ivProductAvatar);
                Toast.makeText(getApplicationContext(), "Thêm món ăn mới thành công",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ivProductAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
