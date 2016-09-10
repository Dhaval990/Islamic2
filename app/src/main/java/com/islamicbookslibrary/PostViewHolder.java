package com.islamicbookslibrary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.islamicbookslibrary.model.Product;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public ImageView readView;
    public TextView numStarsView;
    public TextView numReadView;
    public TextView bodyView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        readView = (ImageView) itemView.findViewById(R.id.read);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        numReadView = (TextView) itemView.findViewById(R.id.post_num_read);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Product product, View.OnClickListener starClickListener) {
        numStarsView.setText(String.valueOf(product.stars.size()));
        numReadView.setText(String.valueOf(product.reads.size()));
        titleView.setText(product.getProTitle());
        bodyView.setText(product.getProDescription());
        starView.setOnClickListener(starClickListener);
    }
}