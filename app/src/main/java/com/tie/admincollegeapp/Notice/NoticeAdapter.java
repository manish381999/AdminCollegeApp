package com.tie.admincollegeapp.Notice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tie.admincollegeapp.R;
import com.tie.admincollegeapp.databinding.NoticefeedItemLayoutBinding;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

 private Context context;
 private ArrayList<NoticeData> list;

    public NoticeAdapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.noticefeed_item_layout,parent,false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, @SuppressLint("RecyclerView") int position) {
     NoticeData  noticeData=list.get(position);

     holder.binding.deleteNoticeTitle.setText(noticeData.getTitle());

        try {
            if (noticeData.getImage()!=null)
            Picasso.get()
                    .load(noticeData.getImage())
                    .placeholder(R.drawable.place_holder)
                    .into(holder.binding.deleteNoticeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to delete this notice ? ");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference =FirebaseDatabase.getInstance().getReference().child("Notice");
                                reference.child(noticeData.getKey()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                notifyItemRemoved(position);
                            }
                        }
                );
                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.cancel();
                            }
                        }
                );

                AlertDialog dialog=null;
                try {
                     dialog=builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (dialog!=null){
                    dialog.show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder{
NoticefeedItemLayoutBinding binding;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=NoticefeedItemLayoutBinding.bind(itemView);
        }
    }
}
