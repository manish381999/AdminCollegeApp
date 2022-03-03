package com.tie.admincollegeapp;

import static java.util.Objects.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tie.admincollegeapp.databinding.ActivityUploadEbookBinding;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class UploadEbookActivity extends AppCompatActivity {
ActivityUploadEbookBinding binding;

 private Uri ebookData;
 FirebaseStorage storage;
 FirebaseDatabase database;
 private ProgressDialog pd;
 final int REQ=2;
 private String ebookName ;
 private String tile ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadEbookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        pd=new ProgressDialog(this);

        binding.addPdf.setOnClickListener(v -> openDocument());

        binding.uploadEbookBtn.setOnClickListener(v -> {
            checkValidation();

        });
    }



    private void checkValidation() {
        tile=binding.pdfTitle.getText().toString();
        if (tile.isEmpty()){
            binding.pdfTitle.setError("Empty");
            binding.pdfTitle.requestFocus();
        }else if (ebookData==null){
            Toast.makeText(this, "Please upload pdf", Toast.LENGTH_SHORT).show();
        }else {
            uploadEbook();
        }

    }

    private void uploadEbook(){
        pd.setTitle("Please wait");
        pd.setMessage("Uploading Ebook...");
        pd.show();

        StorageReference reference=storage.getReference().child("Ebook/"+ebookName +":"+System.currentTimeMillis()+".pdf");
        reference.putFile(ebookData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Task<Uri>uriTask=taskSnapshot.getStorage().getDownloadUrl();
               while (!uriTask.isComplete());
               Uri uri=uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadEbookActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadUrl ) {
        final String uniqueKey=database.getReference().push().getKey();
        HashMap data=new HashMap();
        data.put("EbookTitle",tile);
        data.put("EbookUrl",downloadUrl);

        database.getReference().child("Ebook").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadEbookActivity.this, "Ebook uploaded", Toast.LENGTH_SHORT).show();
                binding.pdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadEbookActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openDocument() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
      startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);

    }


    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ && resultCode==RESULT_OK &&data!=null){
            ebookData=data.getData();

            if (ebookData.toString().startsWith("content://")){
                Cursor cursor=null;
                try {
                    cursor = UploadEbookActivity.this.getContentResolver().query(ebookData, null, null, null, null);
                    if (cursor!=null && cursor.moveToFirst()){
                        ebookName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (ebookData.toString().startsWith("file://")){
                ebookName=new File(ebookData.toString()).getName();
            }

            binding.pdfTextview.setText(ebookName);
        }
    }
}