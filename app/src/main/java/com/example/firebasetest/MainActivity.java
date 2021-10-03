package com.example.firebasetest;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText et_user_name,et_user_email;
    Button btn_save;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<User> arrayList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclev);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        et_user_name = findViewById(R.id.et_user_name);
        et_user_email = findViewById(R.id.et_user_email);
        btn_save = findViewById(R.id.btn_save);

        //firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        //readUser();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    User User = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(User); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        adapter = new recycleAdapter(arrayList);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserName = et_user_name.getText().toString();
                String getUserEmail = et_user_email.getText().toString();

                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("name", getUserName);
                result.put("email", getUserEmail);

                writeNewUser("4",getUserName,getUserEmail);

            }
        });
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(MainActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();

                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                                    User User = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                                    arrayList.add(User); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                                }
                                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // 디비를 가져오던중 에러 발생 시
                                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(MainActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void readUser(){
        mDatabase.child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
