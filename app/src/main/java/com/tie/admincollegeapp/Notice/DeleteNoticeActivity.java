package com.tie.admincollegeapp.Notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tie.admincollegeapp.databinding.ActivityDeleteNoticeBinding;


import java.util.ArrayList;

public class DeleteNoticeActivity extends AppCompatActivity {

ActivityDeleteNoticeBinding binding;
private ArrayList<NoticeData> list;
private NoticeAdapter adapter;
FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeleteNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();

        getNotice();
    }

    private void getNotice() {
        database.getReference().child("Notice").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    NoticeData noticeData=dataSnapshot.getValue(NoticeData.class);
                    list.add(noticeData);
                }
                binding.deleteRv.setHasFixedSize(true);
                adapter=new NoticeAdapter(DeleteNoticeActivity.this,list);
                LinearLayoutManager layoutManager=new LinearLayoutManager(DeleteNoticeActivity.this);
                binding.deleteRv.setLayoutManager(layoutManager);
                binding.deleteRv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}