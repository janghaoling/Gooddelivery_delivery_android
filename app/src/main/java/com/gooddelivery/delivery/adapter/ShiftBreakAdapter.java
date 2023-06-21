package com.gooddelivery.delivery.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.model.Shiftbreaktime;
import com.gooddelivery.delivery.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by santhosh@appoets.com on 28-09-2017.
 */

public class ShiftBreakAdapter extends RecyclerView.Adapter<ShiftBreakAdapter.MyViewHolder> {

    private List<Shiftbreaktime> list;
    private Context context;
    private Integer cou = 0;
    public ShiftBreakAdapter(List<Shiftbreaktime> list, Context con) {
        this.context = con;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_status_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Shiftbreaktime obj = list.get(position);


        if (position == 0 && GlobalData.shift.getStartTime() != null) {
            holder.breakTitle.setText(context.getString(R.string.init_shift));
            holder.breakIcon.setImageResource(R.drawable.ic_start_shift);
            holder.line.setVisibility(View.GONE);
            holder.ordersCount.setVisibility(View.INVISIBLE);
            holder.breakTime.setText(getTimeFromString(obj.getStartTime()));
        } else if (GlobalData.shift.getEndTime() != null && position == (list.size() - 1)) {
            holder.breakTitle.setText(context.getString(R.string.end_shift));
            holder.breakIcon.setImageResource(R.drawable.ic_end_shift);
            if(list.size() < 1){
                //cou += obj.getOrderCount();
                holder.ordersCount.setText(String.valueOf(list.get(position - 1).getOrderCount()) + " Orders");
            }else{
                if(GlobalData.shift.getShiftbreaktimes().size() == 0)
                    holder.ordersCount.setText(GlobalData.shift.getTotalOrder() + " Orders");
                else{
                    Integer val = GlobalData.shift.getTotalOrder() - cou ;

                    if(val < 0)
                        val = 0;

                    System.out.println("Total Order "+ GlobalData.shift.getTotalOrder() + " - cou "+ cou +" Value "+val);
                    holder.ordersCount.setText(val + " Orders");
                }
            }

            holder.breakTime.setText(getTimeFromString(obj.getEndTime()));
        } else {
            holder.breakTitle.setText(context.getString(R.string.break_) +" "+ position);
            holder.breakIcon.setImageResource(R.drawable.ic_break_shift);
            holder.ordersCount.setText(obj.getOrderCount() + " " + context.getString(R.string.orders));
            String timeStr = getTimeFromString(obj.getStartTime()) + " - " + getTimeFromString(obj.getEndTime());
            holder.breakTime.setText(timeStr);
            cou += obj.getOrderCount();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getTimeFromString(String dateTime) {
        String value = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

            if (dateTime != null) {
                value = sdf.format(df.parse(dateTime));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView ordersCount, breakTitle, breakTime;
        private ImageView breakIcon, line;

        private MyViewHolder(View view) {
            super(view);
            ordersCount = (TextView) view.findViewById(R.id.orders_count);
            breakTitle = (TextView) view.findViewById(R.id.break_title);
            breakTime = (TextView) view.findViewById(R.id.break_time);
            breakIcon = (ImageView) view.findViewById(R.id.break_icon);
            line = (ImageView) view.findViewById(R.id.line);
            //breakIcon.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == breakIcon.getId()) {
                Toast.makeText(v.getContext(), "breakIcon PRESSED = " + list.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
