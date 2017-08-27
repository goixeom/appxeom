package goixeom.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import goixeom.com.R;
import goixeom.com.models.Discount;

/**
 * Created by DuongKK on 6/19/2017.
 */

public class AdapterDiscountMenu extends RecyclerView.Adapter<AdapterDiscountMenu.ViewHolder>{


    Context context;
    List<Discount> list;

    public AdapterDiscountMenu(Context context, List<Discount> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_discount_menu,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Discount discount = list.get(position);
        if(holder!=null){
            holder.tvDate.setText(discount.getEnd_date());
            holder.tvDetail.setText(discount.getTitle());
            holder.tvDetail.setSelected(true);
            holder.tvCode.setText(discount.getPr_code());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llRoot;
        TextView tvCode;
        TextView tvDetail;
        TextView tvDate;


        public ViewHolder(View itemView) {
            super(itemView);
            llRoot = (LinearLayout)itemView.findViewById(R.id.ll_root);
            tvCode = (TextView)itemView.findViewById(R.id.tv_discount);
            tvDetail = (TextView)itemView.findViewById(R.id.tv_value_discount);
            tvDate = (TextView)itemView.findViewById(R.id.tv_date);
        }
    }
}
