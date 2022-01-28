package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.LibraryItemLayoutBinding;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.models.ConstantsList;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.UnfinishedBooks;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UnfinishedBooksAdapter extends RecyclerView.Adapter<UnfinishedBooksAdapter.MyViewHolder> {
    Context context;
    ArrayList<Library> list = new ArrayList<>();
    BookDesInterface bookDesInterface;

    public UnfinishedBooksAdapter(Context context, BookDesInterface bookDesInterface) {
        this.context = context;
        this.bookDesInterface = bookDesInterface;
    }

    public ArrayList<Library> getList() {
        return list;
    }

    public void setList(ArrayList<Library> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LibraryItemLayoutBinding binding;

        public MyViewHolder(LibraryItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Toast.makeText(context, "onCreateViewHolder", Toast.LENGTH_SHORT).show();
        LibraryItemLayoutBinding binding = LibraryItemLayoutBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        UnfinishedBooksAdapter.MyViewHolder myViewHolder = new UnfinishedBooksAdapter.MyViewHolder(binding);
        return myViewHolder;    }

    //    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Toast.makeText(context, "adapter", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < Constants.LIST.size(); i++) {
            if (Constants.LIST.get(i).getId().equals(list.get(position).getBookId())) {
                holder.binding.tvBookName.setText(Constants.LIST.get(i).getBooks().getName_book());
                holder.binding.tvBookWriter.setText(Constants.LIST.get(i).getBooks().getName_writer());
                Glide.with(context).load(Constants.LIST.get(i).getBooks().getImg_uri()).into(holder.binding.imgBook);
            }
        }
        String realDurationMillis = TimeUnit.MILLISECONDS.toMinutes(list.get(position).getDuration())
                + ":"
                + (TimeUnit.MILLISECONDS.toSeconds(list.get(position).getDuration())
                - (TimeUnit.MILLISECONDS.toMinutes(list.get(position).getDuration()) * 60)
        );
        holder.binding.tvTime.setText(realDurationMillis);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookDesInterface != null) {
                    bookDesInterface.layout(list.get(position).getBookId(), null);
                }
            }
        });


    }


    @Override
    public int getItemCount() {

        return list != null ? list.size() : 0;
    }


}
