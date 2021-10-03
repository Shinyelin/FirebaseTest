package com.example.firebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleAdapter extends RecyclerView.Adapter<Holder>
{
    ArrayList<User> user_list;
    recycleAdapter(ArrayList<User> list) { this.user_list = list; }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    { Context context = parent.getContext();
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.item_recycle, parent, false); return new Holder(view);
}

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {
        holder.email.setText(user_list.get(position).email);
        holder.userName.setText(user_list.get(position).userName);

    }
    @Override
    public int getItemCount()
    { return user_list.size(); }


}
class Holder extends RecyclerView.ViewHolder
{
    TextView email;
    TextView userName;

public Holder(@NonNull View itemView)
{ super(itemView);
    email = itemView.findViewById(R.id.email);
    userName = itemView.findViewById(R.id.userName);
} }
