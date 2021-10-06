package com.example.lab2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder> {

    Context context;
    ArrayList<Planner>plannerList;

    public PlannerAdapter(Context ct, ArrayList<Planner> plannerList) {
        context = ct;
        this.plannerList = plannerList;
    }
    @NonNull
    @Override
    public PlannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.planner, parent, false);
        return new PlannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerViewHolder holder, int position) {
        holder.textView1.setText(plannerList.get(position).title);
        holder.textView2.setText(plannerList.get(position).date);
        holder.textView3.setText(plannerList.get(position).time);
    }

    @Override
    public int getItemCount() {
        return plannerList.size();
    }

    public static class PlannerViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2, textView3;
        Button edit, delete;
        public PlannerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView9);
            textView2 = itemView.findViewById(R.id.textView10);
            textView3 = itemView.findViewById(R.id.textView11);
            edit = itemView.findViewById(R.id.button3);
            delete = itemView.findViewById(R.id.delete);

            edit.setOnClickListener(view -> edit());
            delete.setOnClickListener(view -> {
                try {
                    delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private void delete() throws IOException {
            int counter = 0;
            int position = -1;
            ArrayList<Planner> plannerList = XMLOperator.parseXml(itemView.getContext());
            for(Planner planner: plannerList) {
                String title = textView1.getText().toString();
                String date = textView2.getText().toString();
                String time = textView3.getText().toString();
                counter = counter + 1;
                if(planner.title.equals(title) && planner.date.equals(date) && planner.time.equals(time)) {
                    position = counter;
                }
            }
            plannerList.remove(position - 1);
            XMLOperator.writeXml(itemView.getContext(), plannerList);
            Intent intent = new Intent(itemView.getContext(), MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            itemView.getContext().startActivity(intent);
        }

        private void edit() {
            TextView textView1 = itemView.findViewById(R.id.textView9);
            TextView textView2 = itemView.findViewById(R.id.textView10);
            TextView textView3 = itemView.findViewById(R.id.textView11);
            Intent intent = new Intent(itemView.getContext(), UpdateActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.putExtra("Title", textView1.getText());
            intent.putExtra("Date", textView2.getText());
            intent.putExtra("Time", textView3.getText());
            itemView.getContext().startActivity(intent);
        }
    }
}
