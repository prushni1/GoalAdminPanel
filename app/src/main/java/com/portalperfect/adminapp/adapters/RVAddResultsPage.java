package com.portalperfect.adminapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.portalperfect.adminapp.EditModel;
import com.portalperfect.adminapp.R;

import java.util.ArrayList;
import java.util.List;

public class RVAddResultsPage extends RecyclerView.Adapter<RVAddResultsPage.ViewHolder>  {


    Context context;
    private SparseBooleanArray allIds= new SparseBooleanArray();

    private SparseBooleanArray allmarks= new SparseBooleanArray();


    public   ArrayList<EditModel> editModelArrayList;

    List<GetDataAdapter> getDataAdapter;

    public RVAddResultsPage(List<GetDataAdapter> getDataAdapter, Context context, ArrayList<EditModel> editModelArrayList  ) {

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;

        this.editModelArrayList = editModelArrayList;

    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_resultspage, parent, false);

        ViewHolder viewHolder = new  ViewHolder(v);


        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final  ViewHolder holder, final int position) {

        GetDataAdapter getDataAdapter1 = getDataAdapter.get(position);



        holder.NameTextView.setText(getDataAdapter1.getName());
        holder.NameTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        holder.PhoneNumberTextView.setText(getDataAdapter1.getPhone_number());


        allIds.put(Integer.parseInt(holder.PhoneNumberTextView.getText().toString()), true);


        holder.edittxt_marks.setText(editModelArrayList.get(position).getEditTextValue());
        Log.d("print","yes");

    }


    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(allIds.size());


        for (int i = 0; i < allIds.size(); i++) {
            items.add(allIds.keyAt(i));
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

        public EditText edittxt_marks;


        public ViewHolder(View itemView) {

            super(itemView);
            view = itemView;

            NameTextView = (TextView) itemView.findViewById(R.id.tv_student_nm) ;
            PhoneNumberTextView = (TextView) itemView.findViewById(R.id.tv_student_id) ;

            edittxt_marks=(EditText)itemView.findViewById(R.id.edittxt_marks);



            edittxt_marks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    editModelArrayList.get(getAdapterPosition()).setEditTextValue( (edittxt_marks.getText().toString()));


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }


}


