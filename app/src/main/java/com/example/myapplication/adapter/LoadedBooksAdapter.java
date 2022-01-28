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
import com.example.myapplication.listener.BookLoadedInterface;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.LoadedBooks;
import com.example.myapplication.models.UnfinishedBooks;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoadedBooksAdapter extends RecyclerView.Adapter<LoadedBooksAdapter.MyViewHolder> {
    Context context;
    ArrayList<LoadedBooks> list = new ArrayList<>();
    BookLoadedInterface bookLoadedInterface;

    public LoadedBooksAdapter(Context context, BookLoadedInterface bookLoadedInterface) {
        this.context = context;
        this.bookLoadedInterface = bookLoadedInterface;
    }

    public ArrayList<LoadedBooks> getList() {
        return list;
    }

    public void setList(ArrayList<LoadedBooks> list) {
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
        LibraryItemLayoutBinding binding = LibraryItemLayoutBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        LoadedBooksAdapter.MyViewHolder myViewHolder = new LoadedBooksAdapter.MyViewHolder(binding);
        return myViewHolder;
    }

    String name;

    @Override
    public void onBindViewHolder(LoadedBooksAdapter.MyViewHolder holder, int position) {
        for (int i = 0; i < Constants.LIST.size(); i++) {
            if (Constants.LIST.get(i).getId().equals(list.get(position).getBookId())) {
                holder.binding.tvBookName.setText(Constants.LIST.get(i).getBooks().getName_book());
                holder.binding.tvBookWriter.setText(Constants.LIST.get(i).getBooks().getName_writer());
                Glide.with(context).load(Constants.LIST.get(i).getBooks().getImg_uri()).into(holder.binding.imgBook);
                name = Constants.LIST.get(i).getBooks().getName_book();
            }
        }
        holder.binding.tvTime.setText(list.get(position).getLoadedData());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookLoadedInterface != null) {
                    Toast.makeText(context, name+"   1", Toast.LENGTH_SHORT).show();

                    bookLoadedInterface.layout(name);
                }
            }
        });


    }


    @Override
    public int getItemCount() {

        return list != null ? list.size() : 0;
    }


}
