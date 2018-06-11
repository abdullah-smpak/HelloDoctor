package com.hellodoc.abdullah.hellodoc.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hellodoc.abdullah.hellodoc.Interface.ItemClickListener;
import com.hellodoc.abdullah.hellodoc.R;

public class DocCatviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtDocName;
    public ImageView imageView;

    private ItemClickListener itemClicklistener;

    public DocCatviewHolder(View itemView) {
        super(itemView);

        txtDocName = (TextView)itemView.findViewById(R.id.doc_cat);

        imageView=(ImageView) itemView.findViewById(R.id.doc_img);

        itemView.setOnClickListener(this);



    }

    public void setItemClicklistener(ItemClickListener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    @Override
    public void onClick(View view) {
        itemClicklistener.onClick(view,getAdapterPosition(),false);

    }
}
