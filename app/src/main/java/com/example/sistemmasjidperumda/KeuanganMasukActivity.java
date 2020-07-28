package com.example.sistemmasjidperumda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemmasjidperumda.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class KeuanganMasukActivity extends AppCompatActivity {

    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    private TextView totalResult;

    //Global Variable
    private String type;
    private int amount;
    private String note;
    private String post_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keuangan_masuk);

//        startActivity(new Intent(getApplicationContext(),MainActivity.class));

        //RadioButton//
        //Pemasukan//

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("sistemmasjidperumda").child("pemasukan").child(uId);

        mDatabase.keepSynced(true);

        fab_btn = findViewById(R.id.fab_pemasukan);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });

        totalResult = findViewById(R.id.total_amount_pemasukan);

        recyclerView = findViewById(R.id.recycler_home_pemasukan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

//        Total Sum Number;
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totalAmount = 0;
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Data data = snap.getValue(Data.class);

                    totalAmount += data.getAmount();
                    String sttotal = String.valueOf("Rp "+totalAmount);

                    totalResult.setText(sttotal);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RadioButton rbPemasukan = findViewById(R.id.rb_masuk);
        rbPemasukan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), KeuanganMasukActivity.class));
            }
        });

        RadioButton rbPengeluaran = findViewById(R.id.rb_keluar);
        rbPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), KeuanganKeluarActivity.class));
            }
        });

    }
    private void customDialog(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(KeuanganMasukActivity.this);

        LayoutInflater inflater = LayoutInflater.from(KeuanganMasukActivity.this);
        View myview = inflater.inflate(R.layout.activity_input_data_pemasukan, null);
        final AlertDialog dialog = mydialog.create();
        dialog.setView(myview);
        dialog.show();

        final EditText type = myview.findViewById(R.id.edt_type_pemasukan);
        final EditText amount = myview.findViewById(R.id.edt_amount_pemasukan);
        final EditText note = myview.findViewById(R.id.edt_note_pemasukan);
        Button btnSave = myview.findViewById(R.id.btn_save_pemasukan);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mType = type.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String mNote = note.getText().toString().trim();

                int ammint = Integer.parseInt(mAmount);

                if(TextUtils.isEmpty(mType)){
                    type.setError("Require Field...");
                    return;
                }

                if(TextUtils.isEmpty(mAmount)){
                    amount.setError("Require Field...");
                    return;
                }

                if (TextUtils.isEmpty(mNote)){
                    note.setError("Require Field...");
                    return;
                }

                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(mType, ammint, mNote, date, id);
                mDatabase.child(id).setValue(data);

                Toast.makeText(getApplicationContext(), "Data Add", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.item_row_pemasukan,
                        MyViewHolder.class,
                        mDatabase
                )
        {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmount(model.getAmount());

                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key = getRef(position).getKey();
                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();

                        updateData();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public void updateData(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(KeuanganMasukActivity.this);
        LayoutInflater inflater = LayoutInflater.from(KeuanganMasukActivity.this);
        View mView = inflater.inflate(R.layout.activity_update_data_pemasukan, null);

        final AlertDialog dialog = mydialog.create();
        dialog.setView(mView);

        final EditText edt_type = mView.findViewById(R.id.edt_type_upd_pemasukan);
        final EditText edt_amount = mView.findViewById(R.id.edt_amount_upd_pemasukan);
        final EditText edt_note = mView.findViewById(R.id.edt_note_upd_pemasukan);

        edt_type.setText(type);
        edt_type.setSelection(type.length());

        edt_amount.setText(String.valueOf(amount));
        edt_amount.setSelection(String.valueOf(amount).length());

        edt_note.setText(note);
        edt_note.setSelection(note.length());

        Button btnUpdate = mView.findViewById(R.id.btn_save_upd_pemasukan);
        Button btnDelete = mView.findViewById(R.id.btn_delete_upd_pemasukan);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = edt_type.getText().toString().trim();
                String mAmount = String.valueOf(amount);
                mAmount = edt_amount.getText().toString().trim();
                note = edt_note.getText().toString().trim();

                int intAmount = Integer.parseInt(mAmount);
                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(type, intAmount, note, date, post_key);

                mDatabase.child(post_key).setValue(data);
                dialog.dismiss();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bottomnavigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
