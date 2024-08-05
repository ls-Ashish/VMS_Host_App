package com.leegosolutions.vms_host_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.ArrayList;

public class CS_UpcomingVisitorsAdapter extends RecyclerView.Adapter<CS_UpcomingVisitorsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CS_VisitorsModel> al_Visitors;
    private OnItemClickListener listener = null;

    public CS_UpcomingVisitorsAdapter(Context context, ArrayList<CS_VisitorsModel> al_Visitors, OnItemClickListener listener) {
        try {
            this.context = context;
            this.al_Visitors = al_Visitors;
            this.listener = listener;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        try {
            // Inflate the layout for each item (e.g., TextView)
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.single_row_home_upcoming_visitors, parent, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            CS_VisitorsModel model = al_Visitors.get(position);

            boolean isConnected = model.isConnected();
            boolean isVisitorDataFound = model.isVisitorDataFound();

            if (!isConnected) {
                holder.tv_Message.setText(context.getResources().getString(R.string.no_connection));
                holder.ll_Content.setVisibility(View.INVISIBLE);
                holder.rl_NoConnection.setVisibility(View.VISIBLE);

            } else if (!isVisitorDataFound) {
                holder.tv_Message.setText(context.getResources().getString(R.string.home_upcoming_visitors_no_visitors));
                holder.ll_Content.setVisibility(View.INVISIBLE);
                holder.rl_NoConnection.setVisibility(View.VISIBLE);

            } else {
                holder.ll_Content.setVisibility(View.VISIBLE);
                holder.rl_NoConnection.setVisibility(View.GONE);

                holder.tv_AppointmentNo.setText(model.getAppointmentNo());
                holder.tv_Name.setText(model.getName());
                holder.tv_Type.setText(model.getType());
                holder.tv_MobileNo.setText(model.getMobileNo());
                holder.tv_StartDate.setText(model.getStartDate());
                holder.tv_EndDate.setText(model.getEndDate());
                holder.tv_Status.setText(model.getStatus());

                String overNight = model.getOvernights();
                holder.tv_Overnights.setText(overNight.equals("0") ? "-" : overNight);

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.onViewPager2TabItemClick(model);

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                }
            });

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public int getItemCount() {
        return al_Visitors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_Content;
        private RelativeLayout rl_NoConnection;
        TextView tv_AppointmentNo, tv_Name, tv_Type, tv_MobileNo, tv_StartDate, tv_EndDate, tv_Overnights, tv_Status, tv_Message;

        public ViewHolder(View itemView) {
            super(itemView);
            try {
                ll_Content = itemView.findViewById(R.id.ll_Content);
                rl_NoConnection = itemView.findViewById(R.id.rl_NoConnection);
                tv_AppointmentNo = itemView.findViewById(R.id.tv_AppointmentNo);
                tv_Name = itemView.findViewById(R.id.tv_Name);
                tv_Type = itemView.findViewById(R.id.tv_Type);
                tv_MobileNo = itemView.findViewById(R.id.tv_MobileNo);
                tv_StartDate = itemView.findViewById(R.id.tv_StartDate);
                tv_EndDate = itemView.findViewById(R.id.tv_EndDate);
                tv_Overnights = itemView.findViewById(R.id.tv_Overnights);
                tv_Status = itemView.findViewById(R.id.tv_Status);
                tv_Message = itemView.findViewById(R.id.tv_Message);

            } catch (Exception ignored) {
                // todo - check to save
            }
        }
    }

    public interface OnItemClickListener {
        void onViewPager2TabItemClick(CS_VisitorsModel model);
    }
}
