package com.example.yumfood;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_Register;
    private TextView txt_Login;
    private EditText edtFullName;
    private EditText edtEmail;
    private EditText edtPhone;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private void initUI()
    {
        // Khai báo thành phần
        btn_Register = findViewById(R.id.btn_register);
        txt_Login = findViewById(R.id.txt_dangnhap);
        edtFullName = findViewById(R.id.regis_fullname);
        edtEmail = findViewById(R.id.regis_email);
        edtPhone = findViewById(R.id.regis_sdt);
        edtPassword = findViewById(R.id.regis_password);
        edtConfirmPassword =  findViewById(R.id.regis_confirmpass);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("Users");

        ProgressDialog progressDialog = new ProgressDialog(this);
        btn_Register.setOnClickListener(new View.OnClickListener() {
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
                if(TextUtils.isEmpty(edtConfirmPassword.getText().toString())){
                    edtConfirmPassword.setError("Require");
                    return;
                }
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String phone = edtPhone.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                String fullName = edtFullName.getText().toString();
                if(confirmPassword.equals(password))
                {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String newUserId = mAuth.getCurrentUser().getUid();
                                        myRef.child(newUserId).child("FullName").setValue(fullName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký không thành công",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Xác nhận mật khẩu không đúng! Vui lòng nhập lại.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}