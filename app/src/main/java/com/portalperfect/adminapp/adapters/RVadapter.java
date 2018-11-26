package com.portalperfect.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.portalperfect.adminapp.AddNewStudent;
import com.portalperfect.adminapp.AddNewsLetter;
import com.portalperfect.adminapp.FeeManagement;
import com.portalperfect.adminapp.HomeScreen;
import com.portalperfect.adminapp.MyUtility;
import com.portalperfect.adminapp.R;
import com.portalperfect.adminapp.ResultManagement;
import com.portalperfect.adminapp.SetGoal;

import java.util.List;
import java.util.Set;


public class RVadapter extends RecyclerView.Adapter<RVadapter.PersonViewHolder> {

   public Context context;
    CardView cv;
    List<Person> persons;


    public RVadapter(List<Person> persons) {

        this.persons=persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items,parent,false);

        PersonViewHolder pvh=new PersonViewHolder(v);


        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, final int position) {


        holder.personPhoto.setImageResource(persons.get(position).photoid);




    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder{


        CardView cv;
        ImageView personPhoto;
        String clicked;


        MyUtility mu;



        Context mcontext;
        void PersonViewHolder(Context context){
            mcontext=context;
        }


        public PersonViewHolder(View itemView) {
            super(itemView);

            cv=(CardView)itemView.findViewById(R.id.cardview1);
            personPhoto=(ImageView)itemView.findViewById(R.id.iv_image);


            mu=new MyUtility();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();

               //     Toast.makeText(v.getContext(), " position : " + position, Toast.LENGTH_SHORT).show();


// 0: student 1:result 2:attendence 3:fees
                    if ( mu.isInternetAvailable(v.getContext()) ==true) {



                    if(position==0){

                        Intent it=new Intent(v.getContext(), AddNewStudent.class) ;
                        v.getContext().startActivity(it);



                    }
                   else if(position==1){

                        Intent it=new Intent(v.getContext(), ResultManagement.class) ;
                        v.getContext().startActivity(it);



                    }
                    else  if(position==2){


                        Intent it=new Intent(v.getContext(), HomeScreen.class) ;
                        v.getContext().startActivity(it);

                    }
                    else  if(position==3){

                        Intent it=new Intent(v.getContext(), FeeManagement.class) ;
                        v.getContext().startActivity(it);


                    }
                    else  if(position==4){

                        Intent it=new Intent(v.getContext(), SetGoal.class) ;
                        v.getContext().startActivity(it);


                    }
                    else  if(position==5){

                        Intent it=new Intent(v.getContext(), AddNewsLetter.class) ;
                        v.getContext().startActivity(it);


                    }

                    }


                    else{
                        Toast.makeText(v.getContext(),"Please Connect to Internet.", Toast.LENGTH_LONG).show();
                    }



                }
            });


        }
    }

}

