package com.example.yumfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yumfood.customer.home.HomeActivity;
import com.example.yumfood.models.User;
import com.example.yumfood.seller.store_selection.SellerStoreSelectionActivity;
import com.example.yumfood.shipper.ShipperActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button btn_Login;
    private TextView txt_Register;
    private EditText edtEmail;
    private EditText edtPassword;
    public static String UID = "66CwgtA9lrZAlkLuKiFzYR9GgHF3";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private void initUI()
    {
        // Khai báo thành phần
        btn_Login = findViewById(R.id.btn_login);
        txt_Register = findViewById(R.id.txt_dangky);
        edtEmail = findViewById(R.id.login_email);
        edtPassword = findViewById(R.id.login_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ProgressDialog progressDialog = new ProgressDialog(this);
        initUI();
        btn_Login.setOnClickListener(new View.OnClickListener() {

            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    edtEmail.setError("Require");
                    return;
                }
                if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    edtPassword.setError("Require");
                    return;
                }
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    UID = mAuth.getCurrentUser().getUid();

                                    /* Lưu thông tin user đăng nhập vào Session */
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(UID);
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User userInfo = snapshot.getValue(User.class);
                                            SharedPreferences.Editor editor = LoginActivity.this.getSharedPreferences("Session", MODE_PRIVATE).edit();
                                            editor.putString("userId", UID);
                                            editor.apply();
//                                            userSession.saveUser(userInfo);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent intent;
                                    if(email.equalsIgnoreCase("seller1@gmail.com")){
                                        intent = new Intent(LoginActivity.this, SellerStoreSelectionActivity.class);
                                    }
                                    else if(email.equalsIgnoreCase("shipper1@gmail.com")){
                                        intent = new Intent(LoginActivity.this, ShipperActivity.class);
                                    }
                                    else{
                                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    }
                                    startActivity(intent);
                                    finishAffinity();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        txt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}
