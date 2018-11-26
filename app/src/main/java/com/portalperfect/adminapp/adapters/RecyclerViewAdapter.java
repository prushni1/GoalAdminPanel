package com.portalperfect.adminapp.adapters;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.portalperfect.adminapp.DataInterface;
import com.portalperfect.adminapp.Model;
import com.portalperfect.adminapp.R;

import java.util.ArrayList;
import java.util.List;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    DataInterface datainterfce;

    Context context;
    private SparseBooleanArray selectedItems= new SparseBooleanArray();;

    List<GetDataAdapter> getDataAdapter;

    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context  ) {

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_studentlist, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        GetDataAdapter getDataAdapter1 = getDataAdapter.get(position);



        holder.NameTextView.setText(getDataAdapter1.getName());
        holder.NameTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        holder.PhoneNumberTextView.setText(getDataAdapter1.getPhone_number());

        holder.checkBox.setOnCheckedChangeListener(null);

        //holder.checkBox.setChecked(imageModelArrayList.get(position).getSelected());
//if true, your checkbox will be selected, else unselected

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if( holder.checkBox.isChecked()) {

                    selectedItems.put(Integer.parseInt(holder.PhoneNumberTextView.getText().toString()), true);
                  // Toast.makeText(context, selectedItems + " selected!", Toast.LENGTH_SHORT).show();


                }
                else if(! holder.checkBox.isChecked()) {

                    selectedItems.delete(Integer.parseInt(holder.PhoneNumberTextView.getText().toString()));
                   // Toast.makeText(context, position + " Removed!", Toast.LENGTH_SHORT).show();

                }

                List<Integer> items = new ArrayList<Integer>(selectedItems.size());

                for (int i = 0; i < selectedItems.size(); i++) {
                    items.add(selectedItems.keyAt(i));
                  }
                Toast.makeText(context, items + " ", Toast.LENGTH_SHORT).show();

                // numbers.get(holder.getAdapterPosition()).setSelected(isChecked);

            }
        });


     }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());

        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return getDataAdapter.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        public TextView NameTextView;
        public TextView PhoneNumberTextView;

        protected CheckBox checkBox;



        public ViewHolder(View itemView) {

            super(itemView);
            view = itemView;

            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
            NameTextView = (TextView) itemView.findViewById(R.id.stu_name) ;
            PhoneNumberTextView = (TextView) itemView.findViewById(R.id.stu_id) ;





        }

    }


}
