package com.matibag.anew;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends Activity {
    public EditText Fname, Mname, Lname, Address, Email;
    public String FNAME = null, MNAME = null, LNAME = null, ADDRESS = null, EMAIL = null;
    public Intent DispForm;
    public DBHelper Conn;
    public Cursor rs;
    private int recordId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Fname = findViewById(R.id.Fname);
        Mname = findViewById(R.id.Mname);
        Lname = findViewById(R.id.Lname);
        Address = findViewById(R.id.Addre);
        Email = findViewById(R.id.Gmail);
        Conn = new DBHelper(this);

        // Read record id from Intent extras
        recordId = getIntent().getIntExtra("record_id", -1);
        if (recordId == -1) {
            Toast.makeText(this, "Invalid record id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            rs = Conn.getData(recordId);
            if (rs != null && rs.moveToFirst()) {
                Fname.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_FNAME)));
                Mname.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_MNAME)));
                Lname.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_LNAME)));
                Address.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_ADDRESS)));
                Email.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_EMAIL)));
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Back(View view) {
        DispForm = new Intent(this, RecordsActivity.class);
        startActivity(DispForm);
    }

    public void UpdateRecord(View view) {
        FNAME = Fname.getText().toString().trim();
        MNAME = Mname.getText().toString().trim();
        LNAME = Lname.getText().toString().trim();
        ADDRESS = Address.getText().toString().trim();
        EMAIL = Email.getText().toString().trim();

        if (FNAME.isEmpty()) {
            Fname.setError("Please Enter Your First Name");
            Fname.requestFocus();
        } else if (MNAME.isEmpty()) {
            Mname.setError("Please Enter Your Middle Name");
            Mname.requestFocus();
        } else if (LNAME.isEmpty()) {
            Lname.setError("Please Enter Your Last Name");
            Lname.requestFocus();
        } else if (ADDRESS.isEmpty()) {
            Address.setError("Please Enter Your Address");
            Address.requestFocus();
        } else if (EMAIL.isEmpty()) {
            Email.setError("Please Enter Your Email");
            Email.requestFocus();
        } else {
            try {
                boolean ok = Conn.UpdateRecords(FNAME, MNAME, LNAME, ADDRESS, EMAIL, recordId);
                if (ok) {
                    Fname.setText("");
                    Mname.setText("");
                    Lname.setText("");
                    Address.setText("");
                    Email.setText("");
                    Toast.makeText(getApplicationContext(), "RECORD UPDATED!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "UPDATE FAILED!", Toast.LENGTH_SHORT).show();
                }
                DispForm = new Intent(this, RecordsActivity.class);
                startActivity(DispForm);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void DeleteRecord(View view) {
        try {
            Conn.DeleteRecord(recordId);
            Toast.makeText(getApplicationContext(), "RECORD DELETED!", Toast.LENGTH_SHORT).show();
            DispForm = new Intent(this, RecordsActivity.class);
            startActivity(DispForm);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}