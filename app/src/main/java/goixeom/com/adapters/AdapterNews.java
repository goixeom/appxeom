package goixeom.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.activities.DetailNewsActivity;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.News;
import goixeom.com.utils.Constants;


/**
 * Created by DuongKK on 6/19/2017.
 */

public class AdapterNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    List<News> list;

    private SparseBooleanArray expandState = new SparseBooleanArray();
    private OnResponse<Integer> onResponse;
    public AdapterNews(Context context, List<News> list, OnResponse<Integer> onResponse) {
        this.context = context;
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            expandState.append(i, false);
        }
        this.onResponse = onResponse;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == News.VIEW_PHOTO) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_photo, parent, false));
        }
        return new ViewHolderNormal(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_text, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderObject, final int position) {
        final News item = list.get(position);
        if (holderObject != null) {
            if (holderObject instanceof ViewHolder) {
                final ViewHolder holder = (ViewHolder) holderObject;
                holder.expandableLayout.setExpanded(false);
                holder.tvContent.setText(item.getContent().replace("\n\n","\n"));
                holder.tvNewsPhotoDate.setText(item.getDate());
                holder.tvTitleNewsPhoto.setText(item.getTitle());
                holder.tvShortDescriptionNewPhoto.setText(item.getContent());
//                if (expandState.get(position, false)) {
//                    holder.expandableLayout.setExpanded(true);
//                    holder.tvMoreNewsPhoto.setText(R.string.thulai);
//
//                } else {
//                    holder.expandableLayout.setExpanded(false);
//                    holder.tvMoreNewsPhoto.setText(R.string.xem_chi_tiet);
//                    onResponse.onResponse(position);
//
//                }
                holder.tvMoreNewsPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        holder.expandableLayout.toggle();
//                        if (holder.expandableLayout.isExpanded()) {
//                            expandState.put(position, true);
//                            holder.tvMoreNewsPhoto.setText(R.string.thulai);
//                        } else {
//                            expandState.put(position, false);
//                            holder.tvMoreNewsPhoto.setText(R.string.xem_chi_tiet);
//                            onResponse.onResponse(position);
//
//                        }
                        Intent i = new Intent(context, DetailNewsActivity.class);
                        Bundle  b = new Bundle();
                        b.putParcelable(Constants.MSG,item);
                        i.putExtra(Constants.BUNDLE,b);
                        context.startActivity(i);
                    }
                });

                Picasso.with(context).load(item.getImage()).into(holder.imgNews);
            } else {
                final ViewHolderNormal holder = (ViewHolderNormal) holderObject;
                holder.expandableLayout.setExpanded(false);
                holder.tvNewsContent.setText(item.getContent().replace("\n\\n","\n"));
                holder.tvNewsDate.setText(item.getDate());
                holder.tvNewsTitle.setText(item.getTitle());
                holder.tvShortDescriptionNews.setText(item.getContent());
                holder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, DetailNewsActivity.class);
                        Bundle  b = new Bundle();
                        b.putParcelable(Constants.MSG,item);
                        i.putExtra(Constants.BUNDLE,b);
                        context.startActivity(i);
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_news)
        ImageView imgNews;
        @BindView(R.id.tv_news_photo_date)
        CustomTextView tvNewsPhotoDate;
        @BindView(R.id.tv_title_news_photo)
        CustomTextView tvTitleNewsPhoto;
        @BindView(R.id.tv_short_description_new_photo)
        CustomTextView tvShortDescriptionNewPhoto;
        @BindView(R.id.expandableLayout)
        ExpandableLayout expandableLayout;
        @BindView(R.id.tv_more_news_photo)
        CustomTextView tvMoreNewsPhoto;
        @BindView(R.id.tv_content_news)
        CustomTextView tvContent;
//        @BindView(R.id.web)
//        WebView web;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
//            web.clearCache(true);
//            web.clearHistory();
//            web.getSettings().setJavaScriptEnabled(true);
//            web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            web.canGoBackOrForward(1);
        }
    }

    static class ViewHolderNormal extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news_date)
        CustomTextView tvNewsDate;
        @BindView(R.id.tv_news_title)
        CustomTextView tvNewsTitle;
        @BindView(R.id.tv_short_description_news)
        CustomTextView tvShortDescriptionNews;
        @BindView(R.id.tv_news_content)
        CustomTextView tvNewsContent;
        @BindView(R.id.expandableLayout)
        ExpandableLayout expandableLayout;
        @BindView(R.id.tv_more)
        CustomTextView tvMore;

        ViewHolderNormal(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
