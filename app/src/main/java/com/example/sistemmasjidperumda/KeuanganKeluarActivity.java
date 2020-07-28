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
import com.example.sistemmasjidperumda.model.DataPengeluaran;
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
import com.example.sistemmasjidperumda.model.DataPengeluaran;

public class KeuanganKeluarActivity extends AppCompatActivity {
    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    private TextView totalResult;

    //Global Variable
    private String type_keluar;
    private int amount_keluar;
    private String note_keluar;
    private String post_key_keluar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keuangan_keluar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("sistemmasjidperumda").child("pengeluaran").child(uId);

        mDatabase.keepSynced(true);

        fab_btn = findViewById(R.id.fab_pengeluaran);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });

        totalResult = findViewById(R.id.total_amount_pengeluaran);

        recyclerView = findViewById(R.id.recycler_home_pengeluaran);
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
                    DataPengeluaran data = snap.getValue(DataPengeluaran.class);

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
        AlertDialog.Builder mydialog = new AlertDialog.Builder(KeuanganKeluarActivity.this);

        LayoutInflater inflater = LayoutInflater.from(KeuanganKeluarActivity.this);
        View myview = inflater.inflate(R.layout.activity_input_data_pengeluaran, null);
        final AlertDialog dialog = mydialog.create();
        dialog.setView(myview);
        dialog.show();

        final EditText type_keluar = myview.findViewById(R.id.edt_type_pengeluaran);
        final EditText amount_keluar = myview.findViewById(R.id.edt_amount_pengeluaran);
        final EditText note_keluar = myview.findViewById(R.id.edt_note_pengeluaran);
        Button btnSave = myview.findViewById(R.id.btn_save_pengeluaran);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mType = type_keluar.getText().toString().trim();
                String mAmount = amount_keluar.getText().toString().trim();
                String mNote = note_keluar.getText().toString().trim();

                int ammint = Integer.parseInt(mAmount);

                if(TextUtils.isEmpty(mType)){
                    type_keluar.setError("Require Field...");
                    return;
                }

                if(TextUtils.isEmpty(mAmount)){
                    amount_keluar.setError("Require Field...");
                    return;
                }

                if (TextUtils.isEmpty(mNote)){
                    note_keluar.setError("Require Field...");
                    return;
                }

                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                DataPengeluaran data = new DataPengeluaran(mType, ammint, mNote, date, id);
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

        FirebaseRecyclerAdapter<DataPengeluaran, MyViewHolder> adapter = new FirebaseRecyclerAdapter<DataPengeluaran, MyViewHolder>
                (
                        DataPengeluaran.class,
                        R.layout.item_row_pengeluaran,
                        MyViewHolder.class,
                        mDatabase
                )
        {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final DataPengeluaran model, final int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmount(model.getAmount());

                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key_keluar = getRef(position).getKey();
                        type_keluar =model.getType();
                        note_keluar=model.getNote();
                        amount_keluar=model.getAmount();

                        updateDataPengeluaran();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public void updateDataPengeluaran(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(KeuanganKeluarActivity.this);
        LayoutInflater inflater = LayoutInflater.from(KeuanganKeluarActivity.this);
        View mView = inflater.inflate(R.layout.activity_update_data_pengeluaran, null);

        final AlertDialog dialog = mydialog.create();
        dialog.setView(mView);

        final EditText edt_type = mView.findViewById(R.id.edt_type_upd_pengeluaran);
        final EditText edt_amount = mView.findViewById(R.id.edt_amount_upd_pengeluaran);
        final EditText edt_note = mView.findViewById(R.id.edt_note_upd_pengeluaran);

        edt_type.setText(type_keluar);
        edt_type.setSelection(type_keluar.length());

        edt_amount.setText(String.valueOf(amount_keluar));
        edt_amount.setSelection(String.valueOf(amount_keluar).length());

        edt_note.setText(note_keluar);
        edt_note.setSelection(note_keluar.length());

        Button btnUpdate = mView.findViewById(R.id.btn_save_upd_pengeluaran);
        Button btnDelete = mView.findViewById(R.id.btn_delete_upd_pengeluaran);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_keluar = edt_type.getText().toString().trim();
                String mAmount = String.valueOf(amount_keluar);
                mAmount = edt_amount.getText().toString().trim();
                note_keluar = edt_note.getText().toString().trim();

                int intAmount = Integer.parseInt(mAmount);
                String date = DateFormat.getDateInstance().format(new Date());

                DataPengeluaran data = new DataPengeluaran(type_keluar, intAmount, note_keluar, date, post_key_keluar);

                mDatabase.child(post_key_keluar).setValue(data);
                dialog.dismiss();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(post_key_keluar).removeValue();

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
