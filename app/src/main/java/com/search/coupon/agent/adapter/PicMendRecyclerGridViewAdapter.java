package com.search.coupon.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.search.coupon.agent.R;

import java.util.ArrayList;

/**
 * @author baipeng
 * @Title PicMendRecyclerGridViewAdapter
 * @Date 2017/11/24 14:41
 * @Description PicMendRecyclerGridViewAdapter.
 */
public class PicMendRecyclerGridViewAdapter extends RecyclerView.Adapter<PicMendRecyclerGridViewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mDataList = new ArrayList<>();
    private LayoutInflater inf;

    public interface OnRecyclerViewItemListener {
        public void onItemClickListener(View view, int position);

        public void onItemDeleteClickListener(View view, String url);
    }

    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener) {
        mOnRecyclerViewItemListener = listener;
    }

    public PicMendRecyclerGridViewAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.mDataList = data;
        inf = LayoutInflater.from(mContext);

    }

    //该方法返回是ViewHolder，当有可复用View时，就不再调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inf.inflate(R.layout.item_upload_picture, viewGroup, false);
        return new ViewHolder(v);
    }

    //将数据绑定到子View，会自动复用View
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        if (viewHolder == null) {
            return;
        }
        if (mOnRecyclerViewItemListener != null) {
            itemOnClick(viewHolder);
        }
        String itemUrl = mDataList.get(i);
        if ("TYPE_ADD".equals(itemUrl)) {
            Glide.with(mContext).load(R.drawable.icon_add_img).into(viewHolder.defaultImage);
            viewHolder.delete.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).load(itemUrl).into(viewHolder.defaultImage);

            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setTag(itemUrl);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getTag() != null) {
                        if (v.getTag() instanceof String) {
                            String item = v.getTag().toString();
                            mOnRecyclerViewItemListener.onItemDeleteClickListener(viewHolder.itemView, item);
                        }
                    }
                }
            });
        }
    }

    //RecyclerView显示数据条数
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //自定义的ViewHolder,减少findViewById调用次数
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView defaultImage, delete;

        //在布局中找到所含有的UI组件
        public ViewHolder(View itemView) {
            super(itemView);
            defaultImage = (ImageView) itemView.findViewById(R.id.iv_add);
            delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    //单击事件
    private void itemOnClick(final RecyclerView.ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                mOnRecyclerViewItemListener.onItemClickListener(holder.itemView, position);
            }
        });
    }
}
