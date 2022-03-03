package com.tie.admincollegeapp.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tie.admincollegeapp.R;
import com.tie.admincollegeapp.databinding.ActivityUpdateFacultyBinding;
import com.tie.admincollegeapp.databinding.ActivityUploadEbookBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class UpdateFacultyActivity extends AppCompatActivity {

ActivityUpdateFacultyBinding binding;

private List<FacultyData> list1, list2,list3,list4;
private  FacultyAdapter adapter;
FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();

        binding.fabadd.setOnClickListener(v -> {
            startActivity(new Intent(UpdateFacultyActivity.this,AddFacultyActivity.class));
        });

        cseDepartment();
        csitDepartment();
        electricalDepartment();
        mechanicalDepartment();

    }

    private void mechanicalDepartment() {
    database.getReference().child("Faculty").child("Mechanical").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            list4=new ArrayList<>();
            if (!snapshot.exists()){
                binding.MechanicalNoData.setVisibility(View.VISIBLE);
                binding.rvMechanical.setVisibility(View.GONE);
            }else {
                binding.MechanicalNoData.setVisibility(View.GONE);
                binding.rvMechanical.setVisibility(View.VISIBLE);

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    FacultyData facultyData=dataSnapshot.getValue(FacultyData.class);
                    list4.add(facultyData);
                }
                binding.rvMechanical.setHasFixedSize(true);
                adapter=new FacultyAdapter(list4,UpdateFacultyActivity.this,"Mechanical");
                LinearLayoutManager layoutManager=new LinearLayoutManager(UpdateFacultyActivity.this);
                binding.rvMechanical.setAdapter(adapter);
                binding.rvMechanical.setLayoutManager(layoutManager);

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(UpdateFacultyActivity.this ,error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void electricalDepartment() {
        database.getReference().child("Faculty").child("Electrical").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3=new ArrayList<>();
                if (!snapshot.exists()){
                    binding.ElectricalNoData.setVisibility(View.VISIBLE);
                    binding.rvElectrical.setVisibility(View.GONE);
                }else {
                    binding.ElectricalNoData.setVisibility(View.GONE);
                    binding.rvElectrical.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        FacultyData facultyData=dataSnapshot.getValue(FacultyData.class);
                        list3.add(facultyData);
                    }
                    binding.rvElectrical.setHasFixedSize(true);
                    adapter=new FacultyAdapter(list3,UpdateFacultyActivity.this,"Electrical");
                    LinearLayoutManager layoutManager=new LinearLayoutManager(UpdateFacultyActivity.this);
                    binding.rvElectrical.setAdapter(adapter);
                    binding.rvElectrical.setLayoutManager(layoutManager);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void csitDepartment() {
        database.getReference().child("Faculty").child("CSIT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               list2=new ArrayList<>();
            if (!snapshot.exists()){
                binding.csitNoData.setVisibility(View.VISIBLE);
                binding.rvCsit.setVisibility(View.GONE);
            }else {
                binding.csitNoData.setVisibility(View.GONE);
                binding.rvCsit.setVisibility(View.VISIBLE);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    FacultyData facultyData=dataSnapshot.getValue(FacultyData.class);
                    list2.add(facultyData);
                }
                binding.rvCsit.setHasFixedSize(true);
                adapter=new FacultyAdapter(list2,UpdateFacultyActivity.this,"CSIT");
                LinearLayoutManager layoutManager=new LinearLayoutManager(UpdateFacultyActivity.this);
                binding.rvCsit.setAdapter(adapter);
                binding.rvCsit.setLayoutManager(layoutManager);


            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cseDepartment() {
        database.getReference().child("Faculty").child("CSE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1=new ArrayList<>();
                if (!snapshot.exists()){
                    binding.cseNoData.setVisibility(View.VISIBLE);
                    binding.rvCse.setVisibility(View.GONE);
                }else {
                    binding.cseNoData.setVisibility(View.GONE);
                    binding.rvCse.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        FacultyData  facultyData=dataSnapshot.getValue(FacultyData.class);
                        list1.add(facultyData);
                    }

                    binding.rvCse.setHasFixedSize(true);
                    adapter=new FacultyAdapter(list1,UpdateFacultyActivity.this,"CSE");
                    LinearLayoutManager layoutManager=new LinearLayoutManager(UpdateFacultyActivity.this);
                    binding.rvCse.setLayoutManager(layoutManager);
                    binding.rvCse.setAdapter(adapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}