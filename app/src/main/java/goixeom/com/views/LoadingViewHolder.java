package goixeom.com.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import goixeom.com.R;

/**
 * Created by DuongKK on 8/14/2017.
 */

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;
    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
    }
}