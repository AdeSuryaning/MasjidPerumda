package com.example.sistemmasjidperumda;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    View myview;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        myview = itemView;
    }

    public void setType(String type) {
        TextView mType = myview.findViewById(R.id.type_pemasukan);
        mType.setText(type);
    }

    public void setNote(String note) {
        TextView mNote = myview.findViewById(R.id.note_pemasukan);
        mNote.setText(note);
    }

    public void setDate(String date) {
        TextView mDate = myview.findViewById(R.id.date_pemasukan);
        mDate.setText(date);
    }

    public void setAmount(int amount) {
        TextView mAmount = myview.findViewById(R.id.amount_pemasukan);
        String stam = String.valueOf(amount);
        mAmount.setText(stam);
    }
}
