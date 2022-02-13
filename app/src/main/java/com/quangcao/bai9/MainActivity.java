package com.quangcao.bai9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Context context = MainActivity.this;

    TextView tv_kq;
    String kq = "";

    EditText edt_tk , edt_mk , edt_key , edt_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        tv_kq = findViewById(R.id.tv_kq);
        edt_tk = findViewById(R.id.edt_tk);
        edt_mk = findViewById(R.id.edt_mk);
        edt_key = findViewById(R.id.edt_key);
        edt_value = findViewById(R.id.edt_value);

    }

    public void dang_ky(View view){
        mAuth.createUserWithEmailAndPassword(
                edt_tk.getText().toString() ,
                edt_mk.getText().toString() )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(context , "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void  dang_nhap(View view){
        mAuth.signInWithEmailAndPassword(
                edt_tk.getText().toString() ,
                edt_mk.getText().toString() )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Đăng nhập thành công",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            kq += "\n email là :" + email + "\n";
            tv_kq.setText(kq);
        }
    }

    public void them_du_lieu(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bai9/" + edt_key.getText().toString() );

        myRef.setValue( edt_value.getText().toString() );

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Thêm Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cap_nhat_DATA(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bai9/" + edt_key.getText().toString() );

        myRef.setValue( edt_value.getText().toString() );

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doc_du_lieu(View view){
        StringBuilder stringBuilder = new StringBuilder();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bai9");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String key = dataSnapshot.getKey();
//                tv_kq.setText(key + "\n" + value);

                for (DataSnapshot i: dataSnapshot.getChildren() ){
                    stringBuilder.append(String.valueOf( i.getKey() + " --- " + i.getValue()) + "\n" );
                }
                tv_kq.setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                tv_kq.setText("Failed to read value.");
            }
        });

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference();
//        myRef.child("bai9").child("0").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.e("firebase", String.valueOf( task.getResult().getKey() + " --- "
//                            + task.getResult().getValue()));
////                    for (DataSnapshot i: task.getResult().getChildren()){
////                        Log.d("firebase", String.valueOf( i.getKey() + " --- " + i.getValue() ) );
////                    }
//
//                }
//            }
//        });
    } // end

    public void xoa_du_lieu(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bai9/" + edt_key.getText().toString() );

        myRef.removeValue();
    }

    public void insert_Array(View view){
        String[] arr = new String[]{"data 1" , "data 2" , "data 3" , "data 4"};

        FirebaseDatabase.getInstance().getReference().child("bai9")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        int nextKey = Integer.parseInt(String.valueOf(currentData.getChildrenCount())) + 1;
                        currentData.child("" + nextKey).setValue("Giá trị mới");
                        for (int i = 0; i < arr.length; i++) {
                            currentData.child("" + nextKey).setValue(arr[i]);
                            nextKey ++;
                        }
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        Toast.makeText(context, "Insert thành công", Toast.LENGTH_SHORT).show();
                    }
                });

//        Task<Void> task = FirebaseDatabase.getInstance().getReference()
//                .child("bai9").setValue(Arrays.asList(arr) );
    }

    public void insertAppend(View view){
        FirebaseDatabase.getInstance().getReference().child("bai9")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        int nextKey = Integer.parseInt(String.valueOf(currentData.getChildrenCount())) + 1;
                        currentData.child("" + nextKey).setValue("Giá trị mới");
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        Toast.makeText(context, "Insert thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}