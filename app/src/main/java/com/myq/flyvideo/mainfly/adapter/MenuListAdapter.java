package com.myq.flyvideo.mainfly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myq.flyvideo.R;
import com.myq.flyvideo.mainfly.getdata.MovieListVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by guorui on 2020/8/11
 * Last update 2020-8-31 14:56:29
 * Description:
 **/
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyHolder> {

    private Context mContext;
    private List<MovieListVo.ListBean> mDatas;
    private OnitemClick onitemClick;   //定义点击事件接口

    public MenuListAdapter(Context context, List<MovieListVo.ListBean> datas) {
        super();
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return mDatas == null ? 0 : mDatas.size();
    }

    //定义设置点击事件监听的方法
    public void setOnitemClickLintener(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    //定义一个点击事件的接口
    public interface OnitemClick {
        void onItemClick(int position);
    }

    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(MenuListAdapter.MyHolder holder, final int position) {
        // TODO Auto-generated method stub
        holder.itemView.setTag(position);

        holder.tvName.setText(mDatas.get(position).getName());
        holder.tvMsg.setText(mDatas.get(position).getMsg().substring(mDatas.get(position).getName().length()).trim());
        holder.tvSource.setText(mDatas.get(position).getSource());
        Glide.with(mContext)
                .load(mDatas.get(position).getImg())
                .placeholder(R.mipmap.anying_load_big)
                .error(R.mipmap.anying_image_failed)
                .centerCrop()
                .into(holder.ivImg);


        if (onitemClick != null) {
            holder.linAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //在TextView的地方进行监听点击事件，并且实现接口
                    onitemClick.onItemClick(position);
                }
            });
        }

    }


    /**
     * 上拉加载更多，添加新数据
     */
    public void updataAdd(List<MovieListVo.ListBean> adddatas) {
        if (adddatas != null) {
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            for (int i = 0; i < adddatas.size(); i++) {
                mDatas.add(adddatas.get(i));
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    //获取当前item数据
    public MovieListVo.ListBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MenuListAdapter.MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_menulist, arg0, false);
        MenuListAdapter.MyHolder holder = new MenuListAdapter.MyHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvMsg, tvSource;
        private ImageView ivImg;
        private LinearLayout linAll;

        public MyHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.item_menulist_name);
            tvMsg = view.findViewById(R.id.item_menulist_msg);
            tvSource = view.findViewById(R.id.item_menulist_source);
            ivImg = view.findViewById(R.id.item_menulist_img);
            linAll = view.findViewById(R.id.item_menulist_linall);


        }

    }


}