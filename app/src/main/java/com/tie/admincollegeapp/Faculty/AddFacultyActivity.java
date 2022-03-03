package com.tie.admincollegeapp.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tie.admincollegeapp.R;
import com.tie.admincollegeapp.databinding.ActivityAddFacultyBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddFacultyActivity extends AppCompatActivity {
ActivityAddFacultyBinding binding;
  final int REQ = 1;
  private Bitmap bitmap;
    private String category;
    private String name, email, post, downloadUrl="";
    FirebaseDatabase database;
    FirebaseStorage storage;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        pd=new ProgressDialog(this);

        String[] item=new String[]{"Select Category", "CSE","CSIT","Electrical","Mechanical"};
        binding.addFacultyCategory.setAdapter(new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,item));

        binding.addFacultyCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=binding.addFacultyCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.addFacultyImage.setOnClickListener(v -> openGallery());

       binding.addFacultyBtn.setOnClickListener(v ->   checkValidation());
    }

    private void checkValidation() {
        name=binding.addFacultyName.getText().toString();
        email=binding.addFacultyEmail.getText().toString();
        post=binding.addFacultyPost.getText().toString();

        if (name.isEmpty()){
            binding.addFacultyName.setError("Please enter name");
            binding.addFacultyName.requestFocus();
        }else if (email.isEmpty()){
            binding.addFacultyEmail.setError("Please enter email");
            binding.addFacultyEmail.requestFocus();
        }else if (post.isEmpty()){
            binding.addFacultyPost.setError("Please enter post");
            binding.addFacultyPost.requestFocus();
        }else if (category.equals("Select Category")){
            Toast.makeText(this, "Please provide category", Toast.LENGTH_SHORT).show();
        }else if (bitmap==null){
            pd.setTitle("Please wait");
            pd.setMessage("Uploading");
            pd.show();
            uploadData();
        }else {
            pd.setTitle("Please wait");
            pd.setMessage("Uploading");
            pd.show();
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[]finalImg=byteArrayOutputStream.toByteArray();

        StorageReference reference=storage.getReference().child("Faculty").child(category);
        reference.putBytes(finalImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
           if (task.isSuccessful()){
               reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                  downloadUrl=String.valueOf(uri);
                  uploadData();
                   }
               });
           }else {
               Toast.makeText(AddFacultyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
           }
            }
        });
    }
private void uploadData(){
        final String  uniqueKey=database.getReference().push().getKey();

        FacultyData facultyData=new FacultyData(name,email,post,downloadUrl,uniqueKey);
        database.getReference().child("Faculty").child(category).child(uniqueKey).setValue(facultyData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddFacultyActivity.this, "Faculty Uploaded", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddFacultyActivity.this,UpdateFacultyActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddFacultyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
}
    private void openGallery(){
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            binding.addFacultyImage.setImageBitmap(bitmap);
        }
    }
}