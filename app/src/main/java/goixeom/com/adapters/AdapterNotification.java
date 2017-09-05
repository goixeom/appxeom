package goixeom.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.activities.DetailNotificationActivity;
import goixeom.com.models.NotificationData;
import goixeom.com.utils.Constants;


/**
 * Created by DuongKK on 6/19/2017.
 */

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {


    Context context;
    List<NotificationData> list;

    public AdapterNotification(Context context, List<NotificationData> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NotificationData discount = list.get(position);
        if (holder != null) {
            holder.tvDate.setText("Ngày " + discount.getDate());
            holder.tvTitle.setText(discount.getTitle());
            holder.tvShort.setText(discount.getContent());
            holder.tvTime.setText("Lúc " + discount.getTime());
            holder.tvTitle.setSelected(true);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailNotificationActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable(Constants.MSG, discount);
                    i.putExtra(Constants.BUNDLE, b);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvTitle;
        CustomTextView tvShort;
        CustomTextView tvDate;
        CustomTextView tvTime;
        CustomTextView tvMore;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.tv_title);
            tvShort = (CustomTextView) itemView.findViewById(R.id.tv_short);
            tvMore = (CustomTextView) itemView.findViewById(R.id.tv_more);
            tvDate = (CustomTextView) itemView.findViewById(R.id.tv_date);
            tvTime = (CustomTextView) itemView.findViewById(R.id.tv_time);
        }
    }

}
