package com.kelompok2.Conf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kelompok2.crudgaji.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    List<Pegawai> pgws, pgwFilter;
    private Context context;
    private RecyclerViewClickListener mListener;
    CustomFilter filter;

    public Adapter(List<Pegawai> pgws, Context context, RecyclerViewClickListener listener) {
        this.pgws = pgws;
        this.pgwFilter = pgws;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pegawai, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder (final MyViewHolder holder, int position) {
        holder.mName.setText(pgws.get(position).getName());
        holder.mPosisi.setText(pgws.get(position).getDesg());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_user);
        requestOptions.error(R.drawable.ic_user);

        Glide.with(context)
                .load(pgws.get(position).getImg())
                .apply(requestOptions)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount(){
        return pgws.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter((ArrayList<Pegawai>) pgwFilter, this);
        }

        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        private CircleImageView mImage;
        private TextView mName, mPosisi;
        private RelativeLayout mRowContainer;

        public MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mImage = itemView.findViewById(R.id.img);
            mName = itemView.findViewById(R.id.name);
            mPosisi = itemView.findViewById(R.id.posisi);
            mRowContainer = itemView.findViewById(R.id.rowContainer);

            mListener = listener;
            mRowContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rowContainer:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
    }
}
