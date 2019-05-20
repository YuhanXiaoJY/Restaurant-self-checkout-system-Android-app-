package com.example.javaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import static java.security.AccessController.getContext;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    private Context mContext;
    private List<Dish> mDishList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        ImageView dishImage;
        TextView dishName;

        public ViewHolder(View view)
        {
            super(view);
            cardView = (CardView) view;
            dishImage = (ImageView) view.findViewById(R.id.dish_image);
            dishName = (TextView) view.findViewById(R.id.dish_name);
        }
    }
    public DishAdapter(List<Dish>dishList)
    {
        mDishList = dishList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mContext == null)
            mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.dish_item, parent, false);

        //click the card
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = holder.getAdapterPosition();
                Dish dish = mDishList.get(position);
                //save the dish data to the intent
                Intent intent = new Intent(mContext, DishActivity.class);
                intent.putExtra(DishActivity.DISH_NAME, dish.getName());
                intent.putExtra(DishActivity.DISH_IMAGE_ID, dish.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Dish dish = mDishList.get(position);
        holder.dishName.setText(dish.getName());
        Glide.with(mContext).load(dish.getImageId()).into(holder.dishImage);
    }
    @Override
    public int getItemCount()
    {
        return mDishList.size();
    }
}
