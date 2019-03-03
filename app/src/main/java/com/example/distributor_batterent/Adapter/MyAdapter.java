package com.example.distributor_batterent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.example.distributor_batterent.Model.Order;
import com.example.distributor_batterent.R;
import com.example.distributor_batterent.RentedList;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private List<Order> orderListFull;

    public MyAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        orderListFull = new ArrayList<>(orderList);
    }

    private Filter exampleFiler = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Order> filteredList = new ArrayList<>();


            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(orderListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Order item : orderListFull) {
                    if (item.getUserName().toLowerCase().contains(filterPattern) ) {
                        filteredList.add(item);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            orderList.clear();
            orderList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };
    public Filter getFilter(){
        return exampleFiler;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.battery_item2,viewGroup,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.modelNumber.setText("Model: "+orderList.get(i).getProduct());
        viewHolder.productNumber.setText("Prod. No: "+orderList.get(i).getDistributorId());
        viewHolder.userName.setText("Username : "+orderList.get(i).getUserName());
        viewHolder.productAmount.setText("Amount : "+orderList.get(i).getAmount());
        viewHolder.userAddress.setText("Address : "+orderList.get(i).getAddress());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView modelNumber,productNumber,userName,productAmount,userAddress;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            modelNumber=itemView.findViewById(R.id.modelNumber);
            productNumber=itemView.findViewById(R.id.productNumber);
            userName=itemView.findViewById(R.id.userName);
            productAmount=itemView.findViewById(R.id.productAmount);
            userAddress=itemView.findViewById(R.id.userAddress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, RentedList.class));
                }
            });
        }
    }
}
