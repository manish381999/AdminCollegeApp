package com.tie.admincollegeapp.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tie.admincollegeapp.R;
import com.tie.admincollegeapp.databinding.ActivityUpdateFaculty2Binding;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateFaculty extends AppCompatActivity {
ActivityUpdateFaculty2Binding binding;
private String name, email,post,image,uniqueKey, category;
final int REQ=1;
private Bitmap bitmap = null;
private StorageReference storageReference;
private DatabaseReference databaseReference;
private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateFaculty2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

     databaseReference=FirebaseDatabase.getInstance().getReference();
     storageReference=FirebaseStorage.getInstance().getReference();

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        post=getIntent().getStringExtra("post");
        image=getIntent().getStringExtra("image");
        uniqueKey=getIntent().getStringExtra("key");
        category=getIntent().getStringExtra("category");


        try {
            Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.place_holder)
                    .into(binding.updateFacultyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.updateFacultyName.setText(name);
       binding.updateFacultyEmail.setText(email);
       binding.updateFacultyPost.setText(post);

       binding.updateFacultyImage.setOnClickListener(v -> openGallery());

       binding.updateFacultyBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             name=binding.updateFacultyName.getText().toString();
             email=binding.updateFacultyEmail.getText().toString();
             post=binding.updateFacultyPost.getText().toString();
             checkValidation();
           }
       });

       binding.deleteFacultyBtn.setOnClickListener(v -> deleteData());
    }

    private void deleteData() {
        databaseReference.child("Faculty").child(category).child(uniqueKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateFaculty.this, "Faculty Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UpdateFaculty.this,UpdateFacultyActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateFaculty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void checkValidation() {


        if (name.isEmpty()){
            binding.updateFacultyName.setError("empty");
            binding.updateFacultyName.requestFocus();
        }else if (email.isEmpty()){
            binding.updateFacultyEmail.setError("empty");
            binding.updateFacultyEmail.requestFocus();
        }else if (post.isEmpty()){
            binding.updateFacultyPost.setError("empty");
            binding.updateFacultyPost.requestFocus();
        }else if (bitmap==null){
             updateData(image);
        }else {
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] finalImg=byteArrayOutputStream.toByteArray();

        StorageReference reference=storageReference.child("Faculty").child(category);
        reference.putBytes(finalImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl=String.valueOf(uri);
                            updateData(downloadUrl);
                        }
                    });
                }
            }
        });
    }

    private void updateData(String s) {

     HashMap data=new HashMap();
     data.put("name",name);
     data.put("email",email);
     data.put("post",post);
     data.put("image",s);
     data.put("key",uniqueKey);

      databaseReference.child("Faculty").child(category).child(uniqueKey).setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateFaculty.this, "Faculty Updated Successfully", Toast.LENGTH_SHORT).show();
              Intent intent=new Intent(UpdateFaculty.this,UpdateFacultyActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateFaculty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.updateFacultyImage.setImageBitmap(bitmap);
        }
    }
}