package com.tie.admincollegeapp.Faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tie.admincollegeapp.R;
import com.tie.admincollegeapp.databinding.FacultyItemLayoutBinding;

import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyAdapterViewHolder>{

     List<FacultyData> list;
     Context context;
     String  category;

    public FacultyAdapter(List<FacultyData> list, Context context, String  category) {
        this.list = list;
        this.context = context;
        this. category= category;
    }

    @NonNull
    @Override
    public FacultyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new FacultyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyAdapterViewHolder holder, int position) {

        FacultyData facultyData=list.get(position);

        holder.binding.facultyName.setText(facultyData.getName());
        holder.binding.facultyEmail.setText(facultyData.getEmail());
        holder.binding.facultyPost.setText(facultyData.getPost());

        try {
            Picasso.get()
                    .load(facultyData.getImage())
                    .placeholder(R.drawable.place_holder)
                    .into(holder.binding.facultyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.binding.facultyUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UpdateFaculty.class);
                intent.putExtra("name",facultyData.getName());
                intent.putExtra("email",facultyData.getEmail());
                intent.putExtra("post",facultyData.getPost());
                intent.putExtra("image",facultyData.getImage());
                intent.putExtra("key",facultyData.getKey());
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class FacultyAdapterViewHolder extends RecyclerView.ViewHolder {
     FacultyItemLayoutBinding binding;
        public FacultyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=FacultyItemLayoutBinding.bind(itemView);
        }
    }
}
