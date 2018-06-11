package com.hellodoc.abdullah.hellodoc.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hellodoc.abdullah.hellodoc.Interface.ItemClickListener;
import com.hellodoc.abdullah.hellodoc.R;

public class DocListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Doc_name;
    public ImageView Doc_pic;

    private ItemClickListener itemClicklistener;

    public void setItemClicklistener(ItemClickListener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    public DocListViewHolder(View itemView) {
        super(itemView);

        Doc_name = (TextView)itemView.findViewById(R.id.doc_name);

        Doc_pic=(ImageView) itemView.findViewById(R.id.doc_pic);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        itemClicklistener.onClick(view,getAdapterPosition(),false);

    }
}
