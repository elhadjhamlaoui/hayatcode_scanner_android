package com.hayatcode.scanner.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.hayatcode.scanner.R;
import com.hayatcode.scanner.Utils;
import com.hayatcode.scanner.data.UserLocalStore;
import com.hayatcode.scanner.model.Record;
import com.hayatcode.scanner.model.User;

import java.util.ArrayList;


public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.viewHolder>  {

    Context context;
    ArrayList<Record> records ;
    User user;
    UserLocalStore userLocalStore;

    public RecordsAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;

        userLocalStore = new UserLocalStore(context);
        user = userLocalStore.getLoggedInUser();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.record_list_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {

        Record record = records.get(position);
        holder.label.setText(record.getName());

    }

    @Override
    public int getItemCount() {
        return records.size();
    }



    class viewHolder extends RecyclerView.ViewHolder{
        TextView label;
        TextView view;
        ConstraintLayout rootLayout;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            rootLayout = itemView.findViewById(R.id.root);
            view = itemView.findViewById(R.id.view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(records.get(getAdapterPosition()).getUrl()));
                    context.startActivity(browserIntent);
                }
            });

            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                    final ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<String>(context,
                                    android.R.layout.simple_list_item_1);
                    arrayAdapter.add("View");


                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(records.get(getAdapterPosition()).getUrl()));
                                context.startActivity(browserIntent);

                            }
                        }
                    });
                    builderSingle.show();
                }
            });



        }

    }



}
