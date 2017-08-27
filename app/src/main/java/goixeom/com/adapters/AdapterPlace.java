package goixeom.com.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.MyPlace;

/**
 * Created by DuongKK on 6/16/2017.
 */

public class AdapterPlace extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<MyPlace> list;
    Context context;
    OnResponse<Integer> onResponse;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private SparseBooleanArray sparseArray = new SparseBooleanArray();
    private OnResponse<Integer> onResponseFavourite;
    private OnResponse<Integer> onResponseRemoveFavourite;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }
    public AdapterPlace(RecyclerView recyclerView, List<MyPlace> list, Context context, OnResponse<Integer> onResponse,OnResponse<Integer> onResponseFavoute,OnResponse<Integer> onResponseRemoveFavoute) {
        this.list = list;
        for (int i = 0 ;i< list.size();i++) {
            sparseArray.append(i,list.get(i).isFavourite());
        }
        this.context = context;
        this.onResponse = onResponse;
        this.onResponseFavourite = onResponseFavoute;
        this.onResponseRemoveFavourite = onResponseRemoveFavoute;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadmore();
                    }
                    isLoading = true;
                }
            }
        });
    }
public void clearSparse(){
    sparseArray.clear();
}
    public AdapterPlace(List<MyPlace> list, Context context, OnResponse<Integer> onResponse) {
        this.list = list;
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add, parent, false);
            return new ViewHolderPlace(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loadmore, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyPlace item = list.get(position);
        if (holder instanceof ViewHolderPlace) {
            ViewHolderPlace userViewHolder = (ViewHolderPlace) holder;
            if (holder != null) {
                userViewHolder.cb.setChecked(sparseArray.get(position));
                userViewHolder.tv.setText(item.getDescription());
                if (item.getName() == null || item.getName().isEmpty())
                    userViewHolder.tvName.setVisibility(View.GONE);
                else {
                    userViewHolder.tvName.setText(item.getName());
                }
                userViewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getName() != null && !item.getName().isEmpty()) {
                            item.setDescription(item.getName() + ", " + item.getDescription());
                            list.add(position, item);
                        }
                        onResponse.onResponse(position);
                    }
                });
                if(item.getIsFavourite()==1){
                    userViewHolder.cb.setChecked(true);
                    sparseArray.append(position,true);
                }
                userViewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        sparseArray.append(position,b);
                        if(b) {
                            onResponseFavourite.onResponse(position);
                            item.setFavourite(0);
                        }else{
                            onResponseRemoveFavourite.onResponse(position);
                            item.setFavourite(1);
                        }
                        list.set(position,item);
                    }
                });
                if (item.getType() == null || item.getType().isEmpty()) return;
                if (item.getType().equals(context.getString(R.string.bus))) {
                    userViewHolder.img.setImageResource(R.drawable.ic_terminal_icon);
                } else if (item.getType().equals(context.getString(R.string.shop))) {
                    userViewHolder.img.setImageResource(R.drawable.ic_shop_icon);
                } else if (item.getType().equals(context.getString(R.string.food))) {
                    userViewHolder.img.setImageResource(R.drawable.ic_food_icon);
                }


            }
        }else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    public void setLoaded(boolean loaded) {
        isLoading = loaded;
    }
    public class ViewHolderPlace extends RecyclerView.ViewHolder {
        CustomTextView tv;
        CustomTextView tvName;
        LinearLayout llRoot;
        ImageView img;
        CheckBox cb;
        public ViewHolderPlace(View itemView) {
            super(itemView);
            tv = (CustomTextView) itemView.findViewById(R.id.tv_address);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
            tvName = (CustomTextView) itemView.findViewById(R.id.tv_name);
            img = (ImageView) itemView.findViewById(R.id.img_icon);
            llRoot = (LinearLayout) itemView.findViewById(R.id.ll_root);
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }
}
