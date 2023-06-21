package com.gooddelivery.delivery.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gooddelivery.delivery.model.Notice;
import com.gooddelivery.delivery.R;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-09-2017.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    private List<Notice> list;
    private Context context;

    public NoticeAdapter(List<Notice> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notice, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notice obj = list.get(position);

        holder.title.setText(obj.getTitle());
        holder.created_at.setText(obj.getCreatedAt());
        holder.notice.setText(obj.getNotice());
        holder.note.setText(obj.getNote());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView item_view;
        private TextView title, created_at, notice, note;

        private MyViewHolder(View view) {
            super(view);
            item_view = (CardView) view.findViewById(R.id.item_view);
            title = (TextView) view.findViewById(R.id.title);
            created_at = (TextView) view.findViewById(R.id.created_at);
            notice = (TextView) view.findViewById(R.id.notice);
            note = (TextView) view.findViewById(R.id.note);
            item_view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == item_view.getId()) {
                //Toast.makeText(v.getContext(), list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
