package goixeom.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.activities.DetailBooking;
import goixeom.com.models.History;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;

/**
 * Created by DuongKK on 6/19/2017.
 */

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {


    Context context;
    List<History> list;

    public AdapterHistory(Context context, List<History> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return new ViewHolder(layoutInflater.inflate(R.layout.row_history,parent,false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final History discount = list.get(position);
        if (holder != null) {
            String[] timeSplit = discount.getDate().split("-");
            holder.tvDate.setText(timeSplit[0]);
            holder.tvTime.setText("Lúc " + timeSplit[1]);
            holder.tvDes.setText(CommonUtils.getGreateAddressStr(discount.getEnd()));
            holder.tvFrom.setText(CommonUtils.getGreateAddressStr(discount.getStart()));
//            holder.ll_root.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(context, DetailBooking.class);
//                    i.putExtra(Constants.BOOKING, discount.getId());
//                    context.startActivity(i);
//                }
//            });
            holder.tvdistance.setText(CommonUtils.round(discount.getDistance(),1)+" Km");
            holder.tvGoDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailBooking.class);
                    i.putExtra(Constants.BOOKING, discount.getId());
                    context.startActivity(i);
                }
            });
            int rate = discount.getVote();
            if (rate >= 4) {
                holder.llRate.setBackgroundResource(R.drawable.ic_rectangle_rated);
            } else if (rate == 3) {
                holder.llRate.setBackgroundResource(R.drawable.ic_rectangle_rated_gray);

            } else {
                holder.llRate.setBackgroundResource(R.drawable.ic_rectangle_rated_black);

            }
            holder.tvRate.setText(rate + "★");
            // TODO: 8/11/2017 tvDetail -> distance
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFrom;
        TextView tvDes;
        TextView tvTime;
        TextView tvDate;
        TextView tvRate;
        CustomTextView tvdistance;
        CustomTextView tvGoDetail;
        LinearLayout llRate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFrom = (TextView) itemView.findViewById(R.id.tv_from);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvdistance = (CustomTextView) itemView.findViewById(R.id.tv_go_detail);
            tvGoDetail = (CustomTextView) itemView.findViewById(R.id.tv_go_detail_1);
            tvRate = (CustomTextView) itemView.findViewById(R.id.tv_rate);
            llRate = (LinearLayout) itemView.findViewById(R.id.ll_rate);
        }
    }
}
