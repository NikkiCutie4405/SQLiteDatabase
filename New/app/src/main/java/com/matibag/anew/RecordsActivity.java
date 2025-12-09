package com.matibag.anew;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordsActivity extends ListActivity {
    public DBHelper Conn;
    public Intent DispForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Conn = new DBHelper(this);
        try {
            ArrayList<String> Records = Conn.GetAllData();
            if (Records != null && Records.size() > 0) {
                setListAdapter(new ArrayAdapter<>(RecordsActivity.this, android.R.layout.simple_list_item_1, Records));
            } else {
                Toast.makeText(getApplicationContext(), "No Records Found!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // Pass the record id to UpdateActivity via Intent extras (do not use static fields)
        int recordId = Conn.RecordsId.get(position);
        DispForm = new Intent(this, UpdateActivity.class);
        DispForm.putExtra("record_id", recordId);
        startActivity(DispForm);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}