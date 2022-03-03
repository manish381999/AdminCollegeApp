package com.tie.admincollegeapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import com.tie.admincollegeapp.Faculty.UpdateFacultyActivity;
import com.tie.admincollegeapp.Notice.DeleteNoticeActivity;
import com.tie.admincollegeapp.Notice.UploadNoticeActivity;
import com.tie.admincollegeapp.databinding.ActivityMainBinding ;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.addNotice.setOnClickListener(v ->openUploadNoticeActivity());
        binding.addGalleryImage.setOnClickListener(v -> openUploadImageActivity());
        binding.addEbook.setOnClickListener(v -> openUploadEbookActivity());
        binding.addFaculty.setOnClickListener(v -> openUpdateFacultyActivity());
        binding.deleteNotice.setOnClickListener(v -> openDeleteNoticeActivity());
    }

    private void openDeleteNoticeActivity(){
        startActivity(new Intent(MainActivity.this, DeleteNoticeActivity.class));
    }

    private void openUpdateFacultyActivity() {
        startActivity(new Intent(MainActivity.this, UpdateFacultyActivity.class));
    }

    private void openUploadEbookActivity() {
        startActivity(new Intent(MainActivity.this,UploadEbookActivity.class));
    }


    private void openUploadNoticeActivity(){
        startActivity(new Intent(MainActivity.this, UploadNoticeActivity.class));
}

   private void openUploadImageActivity(){
        startActivity(new Intent(MainActivity.this,UploadImageActivity.class));
   }
}