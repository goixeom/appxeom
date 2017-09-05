package goixeom.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.activities.DetailDiscountActivity;
import goixeom.com.models.Discount;
import goixeom.com.utils.Constants;

/**
 * Created by DuongKK on 6/19/2017.
 */

public class AdapterDiscount extends RecyclerView.Adapter<AdapterDiscount.ViewHolder> {


    Context context;
    List<Discount> list;

    public AdapterDiscount(Context context, List<Discount> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_discount, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Discount discount = list.get(position);
        if (holder != null) {
            holder.tvDate.setText(Html.fromHtml(
                    String.format(context.getString(R.string.txt_format), "Hạn sử dụng ", discount.getEnd_date())));
            holder.tvDetail.setText(discount.getTitle());
            holder.tvDetail.setSelected(true);
            holder.tvCode.setText(discount.getPr_code());
            holder.cstvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailDiscountActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelableArrayList(Constants.MSG, (ArrayList<? extends Parcelable>) list);
                    b.putInt(Constants.POSITION,position);
                    i.putExtra(Constants.BUNDLE, b);
                    context.startActivity(i);
                }
            });
            if(discount.getImg()!=null && !discount.getImg().isEmpty()){
                Picasso.with(context).load(discount.getImg()).into(holder.img);
            }else{
                Picasso.with(context).load("https://goixeom.com/images/xeomer/duong-nguyen.jpg").into(holder.img);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout llRoot;
        TextView tvCode;
        TextView tvDetail;
        TextView tvDate;
        CustomTextView cstvDetail;
        RoundedImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            llRoot = (RelativeLayout) itemView.findViewById(R.id.ll_root);
            tvCode = (TextView) itemView.findViewById(R.id.tv_discount);
            tvDetail = (TextView) itemView.findViewById(R.id.tv_value_discount);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            cstvDetail = (CustomTextView) itemView.findViewById(R.id.tv_detail);
            img = (RoundedImageView) itemView.findViewById(R.id.img);
        }
    }
}
