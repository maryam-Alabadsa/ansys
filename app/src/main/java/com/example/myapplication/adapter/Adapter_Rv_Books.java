package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.BooksItemBinding;
import com.example.myapplication.listener.BookDesInterface;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.ConstantsList;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Rv_Books extends RecyclerView.Adapter<Adapter_Rv_Books.MyViewHolder> {
    Context context;
    ArrayList<ConstantsList> list = new ArrayList<>();
    BookDesInterface bookDesInterface;

    public Adapter_Rv_Books(Context context, BookDesInterface bookDesInterface) {
        this.context = context;
        this.bookDesInterface = bookDesInterface;
    }

    public ArrayList<ConstantsList> getList() {
        return list;
    }

    public void setList(ArrayList<ConstantsList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        BooksItemBinding binding;

        public MyViewHolder(BooksItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BooksItemBinding binding = BooksItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(binding);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.binding.tvNameBook.setText(list.get(position).getBooks().getName_book());
        holder.binding.tvNameWriter.setText(list.get(position).getBooks().getName_writer());
        Glide.with(context).load(list.get(position).getBooks().getImg_uri()).into(holder.binding.imgBook);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookDesInterface != null) {
                    bookDesInterface.layout(list.get(position).getId(),list.get(position).getBooks().getMost_listened());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}