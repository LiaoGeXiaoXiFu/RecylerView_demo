package com.example.dianshang_rv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 撩个小媳妇 on 2018/5/24.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private List<ReaBean.DataBean> list;
    private Context context;
    private List<Integer> height;
    private ItemClickListener itemClickListener;
    Bitmap des_bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    private String url = "http://img1.imgtn.bdimg.com/it/u=4054100184,7315927&fm=27&gp=0.jpg";

    public HomeAdapter(Context context, List<ReaBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /**
         * 得到item的LayoutParams布局参数
         */
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        holder.itemView.setLayoutParams(params);//把params设置item布局
        //加载图片
        Glide
                .with(context)
                .load(list.get(position).getIcon())
                .into(holder.image);
        //holder.image.setImageBitmap(des_bitmap);
        holder.textView.setText(list.get(position).getName());//为控件绑定数据
        //为TextView添加监听回调
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemSubViewClick(holder.textView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView image;

        public MyViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.id_img);
            textView = (TextView) itemView.findViewById(R.id.id_num);
            //为item添加普通点击回调
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(itemView, getPosition());
                    }
                }
            });
            //为item添加长按回调
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemLongClick(itemView, getPosition());
                    }
                    return true;
                }
            });
        }
    }

    public void addItem(int position) {
        list.add(position, list.get(position));
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
}