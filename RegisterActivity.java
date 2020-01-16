package com.example.textapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username,email,password;//city;
    Button btn_register;
    //CheckBox traveller,localite;

    FirebaseAuth auth;
    DatabaseReference reference;

    String domain="",home="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username=(MaterialEditText)findViewById(R.id.username);
        email=(MaterialEditText)findViewById(R.id.email);
        password=(MaterialEditText)findViewById(R.id.password);
        btn_register=(Button)findViewById(R.id.btn_register);
        /*city=(MaterialEditText)findViewById(R.id.city);
        traveller=(CheckBox)findViewById(R.id.traveller);
        localite=(CheckBox)findViewById(R.id.localite);*/

        auth=FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=username.getText().toString().trim();
                String txt_password=password.getText().toString().trim();
                String txt_email=email.getText().toString().trim();

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)){
                    Toast.makeText(RegisterActivity.this,"All fields are required",Toast.LENGTH_LONG).show();
                }
                else if(txt_password.length()<5){
                    Toast.makeText(RegisterActivity.this,"Password must contain atleast 5 characters",Toast.LENGTH_LONG).show();
                }
                /*else {
                    if(traveller.isChecked()){
                        domain="traveller";
                        home="";
                    }
                    if(localite.isChecked()){
                        domain="localite";
                        home=city.getText().toString().trim();
                    }
                    register(txt_username,txt_email,txt_password);
                }*/
            }
        });

        /*localite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(localite.isChecked()){
                    city.setVisibility(View.VISIBLE);
                }else{
                    city.setVisibility(View.GONE);
                }
            }
        });*/
    }

    private void  register(final String username, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final FirebaseUser firebaseUser=auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid=firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");
                            hashMap.put("search",username.toLowerCase());
                            /*hashMap.put("domain",domain);
                            hashMap.put("home",home);*/

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterActivity.this,"You can't register with this email or password",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
